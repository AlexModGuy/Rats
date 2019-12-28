package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityPlagueDoctor;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.IronGolemEntity;

import java.util.EnumSet;
import java.util.List;

public class PlagueDoctorAIFollowGolem extends Goal {
    private final EntityPlagueDoctor villager;
    private IronGolemEntity ironGolem;
    private int takeGolemRoseTick;
    private boolean tookGolemRose;

    public PlagueDoctorAIFollowGolem(EntityPlagueDoctor villagerIn) {
        this.villager = villagerIn;
        this.setMutexFlags(EnumSet.of(Flag.MOVE));
    }

    public boolean shouldExecute() {
        if (this.villager.getGrowingAge() >= 0) {
            return false;
        } else if (!this.villager.world.isDaytime()) {
            return false;
        } else {
            List<IronGolemEntity> list = this.villager.world.getEntitiesWithinAABB(IronGolemEntity.class, this.villager.getBoundingBox().grow(6.0D, 2.0D, 6.0D));

            if (list.isEmpty()) {
                return false;
            } else {
                for (IronGolemEntity IronGolemEntity : list) {
                    if (IronGolemEntity.getHoldRoseTick() > 0) {
                        this.ironGolem = IronGolemEntity;
                        break;
                    }
                }

                return this.ironGolem != null;
            }
        }
    }

    /**
     * Returns whether an in-progress Goal should continue executing
     */
    public boolean shouldContinueExecuting() {
        return this.ironGolem.getHoldRoseTick() > 0;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        this.takeGolemRoseTick = this.villager.getRNG().nextInt(320);
        this.tookGolemRose = false;
        this.ironGolem.getNavigator().clearPath();
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask() {
        this.ironGolem = null;
        this.villager.getNavigator().clearPath();
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void tick() {
        this.villager.getLookController().setLookPositionWithEntity(this.ironGolem, 30.0F, 30.0F);

        if (this.ironGolem.getHoldRoseTick() == this.takeGolemRoseTick) {
            this.villager.getNavigator().tryMoveToEntityLiving(this.ironGolem, 0.5D);
            this.tookGolemRose = true;
        }

        if (this.tookGolemRose && this.villager.getDistanceSq(this.ironGolem) < 4.0D) {
            this.ironGolem.setHoldingRose(false);
            this.villager.getNavigator().clearPath();
        }
    }
}