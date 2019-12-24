package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.RatCommand;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class RatAIHarvestBreeder extends Goal {
    private static final int RADIUS = 16;
    private static final ItemStack SHEAR_STACK = new ItemStack(Items.SHEARS);
    private final EntityRat entity;
    private Entity targetSheep = null;
    private boolean reachedSheep = false;
    private int fishingCooldown = 1000;
    private int throwCooldown = 0;
    private Random rand = new Random();
    private Predicate<LivingEntity> perRatPredicate;

    public RatAIHarvestBreeder(EntityRat entity) {
        super();
        this.entity = entity;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean shouldExecute() {
        if (!this.entity.canMove() || !this.entity.isTamed() || this.entity.getCommand() != RatCommand.HARVEST || this.entity.isInCage() || !entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_BREEDER)) {
            return false;
        }
        if (this.entity.getHeldItemMainhand().isEmpty()) {
            return false;
        }
        resetTarget();
        return targetSheep != null;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return targetSheep != null && !this.entity.getHeldItemMainhand().isEmpty();
    }

    public void resetTask() {
        this.entity.getNavigator().clearPath();
        resetTarget();
    }

    @Override
    public void tick() {
        if (this.targetSheep != null && this.targetSheep.isAlive() && !this.entity.getHeldItemMainhand().isEmpty()) {
            this.entity.getNavigator().tryMoveToEntityLiving(this.targetSheep, 1D);
            if (entity.getDistance(targetSheep) < 1.5D) {
                if (targetSheep instanceof AnimalEntity && !((AnimalEntity) targetSheep).isInLove()) {
                    ((AnimalEntity) targetSheep).setInLove(null);
                    this.entity.getHeldItemMainhand().shrink(1);
                }
                this.targetSheep = null;
                this.resetTask();
            }
        } else {
            this.resetTask();
        }
    }

    private void resetTarget() {
        perRatPredicate = new com.google.common.base.Predicate<LivingEntity>() {
            public boolean apply(@Nullable LivingEntity entity) {
                return entity != null && entity instanceof AnimalEntity && !entity.isChild() && !((AnimalEntity) entity).isInLove() && ((AnimalEntity) entity).getGrowingAge() == 0 && ((AnimalEntity) entity).isBreedingItem(RatAIHarvestBreeder.this.entity.getHeldItemMainhand());
            }
        };
        List<LivingEntity> list = this.entity.world.<LivingEntity>getEntitiesWithinAABB(LivingEntity.class, this.entity.getBoundingBox().grow(RADIUS), (com.google.common.base.Predicate<? super LivingEntity>) perRatPredicate);
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