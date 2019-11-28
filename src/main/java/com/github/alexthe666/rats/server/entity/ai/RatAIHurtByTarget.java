package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityRat;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.player.PlayerEntity;

public class RatAIHurtByTarget extends EntityAIHurtByTarget {

    private EntityRat rat;

    public RatAIHurtByTarget(EntityRat rat, boolean entityCallsForHelpIn, Class<?>... excludedReinforcementTypes) {
        super(rat, entityCallsForHelpIn, excludedReinforcementTypes);
        this.rat = rat;
    }

    public boolean shouldExecute() {
        LivingEntity LivingEntity = this.taskOwner.getRevengeTarget();
        if (rat.isTamed() || !(LivingEntity instanceof PlayerEntity)) {
            return super.shouldExecute();
        }
        return false;
    }
}
