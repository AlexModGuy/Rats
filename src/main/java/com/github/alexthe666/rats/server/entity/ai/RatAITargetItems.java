package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.RatCommand;
import com.github.alexthe666.rats.server.entity.RatUtils;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

public class RatAITargetItems<T extends ItemEntity> extends TargetGoal {
    protected final RatAITargetItems.Sorter theNearestAttackableTargetSorter;
    protected final Predicate<? super ItemEntity> targetEntitySelector;
    protected int executionChance;
    protected boolean mustUpdate;
    protected ItemEntity targetEntity;
    private EntityRat rat;

    public RatAITargetItems(EntityRat creature, boolean checkSight) {
        this(creature, checkSight, false);
    }

    public RatAITargetItems(EntityRat creature, boolean checkSight, boolean onlyNearby) {
        this(creature, 10, checkSight, onlyNearby, null);
    }

    public RatAITargetItems(EntityRat creature, int chance, boolean checkSight, boolean onlyNearby, @Nullable final Predicate<? super T> targetSelector) {
        super(creature, checkSight, onlyNearby);
        this.executionChance = chance;
        this.rat = creature;
        this.theNearestAttackableTargetSorter = new RatAITargetItems.Sorter(creature);
        this.targetEntitySelector = new Predicate<ItemEntity>() {
            @Override
            public boolean apply(@Nullable ItemEntity item) {
                ItemStack stack = item.getItem();
                if (rat.isTargetCommand()) {
                    return item != null && !stack.isEmpty() && rat.canRatPickupItem(stack);
                }
                return !stack.isEmpty() && RatUtils.shouldRaidItem(stack) && rat.canRatPickupItem(stack);
            }
        };
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean shouldExecute() {
        if (!rat.canMove() || this.goalOwner.isPassenger() && !rat.isRidingSpecialMount() || rat.isInCage() || rat.isTargetCommand() && rat.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_FARMER)) {
            return false;
        }
        if (!this.mustUpdate) {
            long worldTime = this.goalOwner.world.getGameTime() % 10;
            if (this.rat.getIdleTime() >= 100 && worldTime != 0) {
                return false;
            }
            if (this.rat.getRNG().nextInt(this.executionChance) != 0 && worldTime != 0) {
                return false;
            }
        }
        List<ItemEntity> list = this.goalOwner.world.getEntitiesWithinAABB(ItemEntity.class, this.getTargetableArea(this.getTargetDistance()), this.targetEntitySelector);
        if (list.isEmpty()) {
            return false;
        } else {
            Collections.sort(list, this.theNearestAttackableTargetSorter);
            this.targetEntity = list.get(0);
            this.mustUpdate = false;
            return true;
        }
    }

    protected double getTargetDistance() {
        return 16D;
    }


    protected AxisAlignedBB getTargetableArea(double targetDistance) {
        Vector3d renderCenter = new Vector3d(this.rat.getSearchCenter().getX()+ 0.5, this.rat.getSearchCenter().getY()+ 0.5, this.rat.getSearchCenter().getZ() + 0.5D);
        double renderRadius = this.rat.getSearchRadius();
        AxisAlignedBB aabb = new AxisAlignedBB(-renderRadius, -renderRadius, -renderRadius, renderRadius, renderRadius, renderRadius);
        return aabb.offset(renderCenter);
    }

    @Override
    public void startExecuting() {
        this.goalOwner.getNavigator().tryMoveToXYZ(this.targetEntity.getPosX(), this.targetEntity.getPosY(), this.targetEntity.getPosZ(), 1);
        super.startExecuting();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.targetEntity == null || this.targetEntity != null && !this.targetEntity.isAlive()) {
            this.resetTask();
        }
        if (this.targetEntity != null && this.targetEntity.isAlive() && this.goalOwner.getDistanceSq(this.targetEntity) < 3.5F * rat.getRatDistanceModifier() && rat.getHeldItem(Hand.MAIN_HAND).isEmpty()) {
            EntityRat rat = (EntityRat) this.goalOwner;
            ItemStack duplicate = this.targetEntity.getItem().copy();
            int extractSize = rat.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_PLATTER) ? this.targetEntity.getItem().getCount() : 1;
            duplicate.setCount(extractSize);
            this.targetEntity.getItem().shrink(extractSize);
            if (!rat.getHeldItem(Hand.MAIN_HAND).isEmpty() && !rat.world.isRemote) {
                rat.entityDropItem(rat.getHeldItem(Hand.MAIN_HAND), 0.0F);
            }
            rat.setHeldItem(Hand.MAIN_HAND, duplicate);
            if (this.targetEntity.getThrowerId() != null) {
                PlayerEntity targetPlayer = this.goalOwner.world.getPlayerByUuid(this.targetEntity.getThrowerId());
                if (targetPlayer != null && RatUtils.isCheese(duplicate)) {
                    if (!rat.isTamed() && rat.canBeTamed()) {
                        rat.wildTrust += 10;
                        rat.cheeseFeedings++;
                        rat.world.setEntityState(rat, (byte) 82);
                        if (rat.wildTrust >= 100 && rat.getRNG().nextInt(3) == 0 || rat.cheeseFeedings >= 15) {
                            rat.world.setEntityState(rat, (byte) 83);
                            rat.setTamedByMonster(false);
                            rat.setTamedBy(targetPlayer);
                            rat.setCommand(RatCommand.FOLLOW);
                        }
                    } else {
                        String untameableText = "entity.rats.rat.untameable";
                        if (rat.getOwner() != null && !rat.getOwnerId().equals(targetPlayer.getUniqueID())) {
                            untameableText = "entity.rats.rat.tamed_by_other";
                        }
                        if (!rat.isOwner(targetPlayer)) {
                            targetPlayer.sendStatusMessage(new TranslationTextComponent(untameableText), true);
                        }
                    }

                }
            }
            resetTask();
        }
    }

    public void makeUpdate() {
        this.mustUpdate = true;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return !this.goalOwner.getNavigator().noPath();
    }

    public static class Sorter implements Comparator<Entity> {
        private final Entity theEntity;

        public Sorter(Entity theEntityIn) {
            this.theEntity = theEntityIn;
        }

        public int compare(Entity p_compare_1_, Entity p_compare_2_) {
            double d0 = this.theEntity.getDistanceSq(p_compare_1_);
            double d1 = this.theEntity.getDistanceSq(p_compare_2_);
            return d0 < d1 ? -1 : (d0 > d1 ? 1 : 0);
        }
    }

}