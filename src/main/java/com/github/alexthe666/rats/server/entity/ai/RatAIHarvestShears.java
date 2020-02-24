package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.RatCommand;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.IShearable;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class RatAIHarvestShears extends Goal {
    private static final int RADIUS = 16;
    private static final ItemStack SHEAR_STACK = new ItemStack(Items.SHEARS);
    private final EntityRat entity;
    private Entity targetSheep = null;
    private boolean reachedSheep = false;
    private int fishingCooldown = 1000;
    private int throwCooldown = 0;
    private Random rand = new Random();
    private Predicate<LivingEntity> SHEAR_PREDICATE = new com.google.common.base.Predicate<LivingEntity>() {
        public boolean apply(@Nullable LivingEntity entity) {
            return entity != null && entity instanceof IShearable && ((IShearable) entity).isShearable(SHEAR_STACK, entity.world, entity.getPosition());
        }
    };

    public RatAIHarvestShears(EntityRat entity) {
        super();
        this.entity = entity;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean shouldExecute() {
        if (!this.entity.canMove() || !this.entity.isTamed() || this.entity.getCommand() != RatCommand.HARVEST || this.entity.isInCage() || !entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_SHEARS)) {
            return false;
        }
        if (!this.entity.getHeldItemMainhand().isEmpty()) {
            return false;
        }
        resetTarget();
        return targetSheep != null;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return targetSheep != null && this.entity.getHeldItemMainhand().isEmpty();
    }

    public void resetTask() {
        this.entity.getNavigator().clearPath();
        resetTarget();
    }


    @Override
    public void tick() {
        if (this.targetSheep != null && this.targetSheep.isAlive() && this.entity.getHeldItemMainhand().isEmpty()) {
            this.entity.getNavigator().tryMoveToEntityLiving(this.targetSheep, 1.25D);
            if (entity.getDistance(targetSheep) < 1.5D) {
                if (targetSheep instanceof IShearable) {
                    java.util.List<ItemStack> drops = ((IShearable) targetSheep).onSheared(new ItemStack(Items.SHEARS), this.entity.world, targetSheep.getPosition(), 0);
                    for (ItemStack stack : drops) {
                        targetSheep.entityDropItem(stack, 0.0F);
                    }
                }
                this.targetSheep = null;
                this.resetTask();
            }
        } else {
            this.resetTask();
        }
    }

    private void resetTarget() {
        int radius = this.entity.getSearchRadius();
        AxisAlignedBB bb = new AxisAlignedBB(-radius, -radius, -radius, radius, radius, radius).offset(entity.getSearchCenter());
        List<LivingEntity> list = this.entity.world.<LivingEntity>getEntitiesWithinAABB(LivingEntity.class, bb, (com.google.common.base.Predicate<? super LivingEntity>) SHEAR_PREDICATE);
        LivingEntity closestSheep = null;
        for (LivingEntity base : list) {
            if (closestSheep == null || base.getDistanceSq(entity) < closestSheep.getDistanceSq(entity)) {
                closestSheep = base;
            }
        }
        if (closestSheep != null) {
            this.targetSheep = closestSheep;
        }
    }

}