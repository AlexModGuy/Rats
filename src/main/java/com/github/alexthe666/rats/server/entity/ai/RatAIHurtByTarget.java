package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityRat;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.player.PlayerEntity;

public class RatAIHurtByTarget extends HurtByTargetGoal {

    private EntityRat rat;

    public RatAIHurtByTarget(EntityRat rat, Class<?>... excludedReinforcementTypes) {
        super(rat, excludedReinforcementTypes);
        //this.setCallsForHelp();
        this.rat = rat;
    }

    public boolean shouldExecute() {
        LivingEntity LivingEntity = this.goalOwner.getRevengeTarget();
        if (rat.isTamed() || !(LivingEntity instanceof PlayerEntity)) {
            return super.shouldExecute();
        }
        return false;
    }
}
