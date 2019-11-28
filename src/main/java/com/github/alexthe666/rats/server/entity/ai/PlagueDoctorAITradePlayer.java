package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityPlagueDoctor;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.PlayerEntity;

public class PlagueDoctorAITradePlayer extends EntityAIBase {
    private final EntityPlagueDoctor villager;

    public PlagueDoctorAITradePlayer(EntityPlagueDoctor villagerIn) {
        this.villager = villagerIn;
        this.setMutexBits(5);
    }

    public boolean shouldExecute() {
        if (!this.villager.isEntityAlive()) {
            return false;
        } else if (this.villager.isInWater()) {
            return false;
        } else if (!this.villager.onGround) {
            return false;
        } else if (this.villager.velocityChanged) {
            return false;
        } else {
            PlayerEntity PlayerEntity = this.villager.getCustomer();

            if (PlayerEntity == null) {
                return false;
            } else if (this.villager.getDistanceSq(PlayerEntity) > 16.0D) {
                return false;
            } else {
                return PlayerEntity.openContainer != null;
            }
        }
    }

    public void startExecuting() {
        this.villager.getNavigator().clearPath();
    }

    public void resetTask() {
        this.villager.setCustomer(null);
    }
}