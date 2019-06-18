package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.RatCommand;
import com.github.alexthe666.rats.server.entity.RatUtils;
import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RatAITargetItems<T extends EntityItem> extends EntityAITarget {
    protected final RatAITargetItems.Sorter theNearestAttackableTargetSorter;
    protected final Predicate<? super EntityItem> targetEntitySelector;
    private final int targetChance;
    protected EntityItem targetEntity;
    private EntityRat rat;

    public RatAITargetItems(EntityRat creature, boolean checkSight) {
        this(creature, checkSight, false);
        this.setMutexBits(0);
    }

    public RatAITargetItems(EntityRat creature, boolean checkSight, boolean onlyNearby) {
        this(creature, 0, checkSight, onlyNearby, null);
    }

    public RatAITargetItems(EntityRat creature, int chance, boolean checkSight, boolean onlyNearby, @Nullable final Predicate<? super T> targetSelector) {
        super(creature, checkSight, onlyNearby);
        this.targetChance = chance;
        this.rat = creature;
        this.theNearestAttackableTargetSorter = new RatAITargetItems.Sorter(creature);
        this.targetEntitySelector = new Predicate<EntityItem>() {
            @Override
            public boolean apply(@Nullable EntityItem item) {
                if(rat.getCommand() == RatCommand.GATHER || rat.getCommand() == RatCommand.HARVEST){
                    return item != null  && !item.getItem().isEmpty() && rat.canRatPickupItem(item.getItem());
                }
                return item instanceof EntityItem && !item.getItem().isEmpty() && RatUtils.isRatFood(item.getItem()) && rat.canRatPickupItem(item.getItem());
            }
        };
        this.setMutexBits(0);
    }

    @Override
    public boolean shouldExecute() {
        if (!rat.canMove() || this.taskOwner.isRiding() || rat.isInCage()) {
            return false;
        }
        List<EntityItem> list = this.taskOwner.world.getEntitiesWithinAABB(EntityItem.class, this.getTargetableArea(this.getTargetDistance()), this.targetEntitySelector);

        if (list.isEmpty()) {
            return false;
        } else {
            Collections.sort(list, this.theNearestAttackableTargetSorter);
            this.targetEntity = list.get(0);
            return true;
        }
    }

    protected double getTargetDistance() {
        return 32D;
    }


    protected AxisAlignedBB getTargetableArea(double targetDistance) {
        return this.taskOwner.getEntityBoundingBox().grow(targetDistance, targetDistance, targetDistance);
    }

    @Override
    public void startExecuting() {
        this.taskOwner.getNavigator().tryMoveToXYZ(this.targetEntity.posX, this.targetEntity.posY, this.targetEntity.posZ, 1);
        super.startExecuting();
    }

    @Override
    public void updateTask() {
        super.updateTask();
        if (this.targetEntity == null || this.targetEntity != null && this.targetEntity.isDead) {
            this.resetTask();
        }
        if (this.targetEntity != null && !this.targetEntity.isDead && this.taskOwner.getDistanceSq(this.targetEntity) < 1) {
            EntityRat rat = (EntityRat) this.taskOwner;
            ItemStack duplicate = this.targetEntity.getItem().copy();
            duplicate.setCount(1);
            this.targetEntity.getItem().shrink(1);
            if(!rat.getHeldItem(EnumHand.MAIN_HAND).isEmpty() && !rat.world.isRemote){
                rat.entityDropItem(rat.getHeldItem(EnumHand.MAIN_HAND), 0.0F);
            }
            rat.setHeldItem(EnumHand.MAIN_HAND, duplicate);
            if (this.targetEntity.getThrower() != null) {
                EntityPlayer targetPlayer = this.taskOwner.world.getPlayerEntityByName(this.targetEntity.getThrower());
                if (!rat.isTamed() && targetPlayer != null && !rat.hasPlague() && RatUtils.isCheese(duplicate)) {
                    rat.wildTrust += 10;
                    rat.world.setEntityState(rat, (byte) 82);
                    if (rat.wildTrust >= 100 && rat.getRNG().nextInt(3) == 0) {
                        rat.world.setEntityState(rat, (byte) 83);
                        rat.setTamed(true);
                        rat.setOwnerId(targetPlayer.getUniqueID());
                        rat.setCommand(RatCommand.FOLLOW);
                    }
                }
            }
            resetTask();
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        return !this.taskOwner.getNavigator().noPath();
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