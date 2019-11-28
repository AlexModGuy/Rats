package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityPlagueDoctor;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.PlayerEntity;

public class PlagueDoctorAILookAtTradePlayer extends EntityAIWatchClosest {
    private final EntityPlagueDoctor villager;

    public PlagueDoctorAILookAtTradePlayer(EntityPlagueDoctor villagerIn) {
        super(villagerIn, PlayerEntity.class, 8.0F);
        this.villager = villagerIn;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        if (this.villager.isTrading()) {
            this.closestEntity = this.villager.getCustomer();
            return true;
        } else {
            return false;
        }
    }
}