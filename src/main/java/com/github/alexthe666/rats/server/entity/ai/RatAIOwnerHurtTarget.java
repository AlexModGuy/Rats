package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityRat;
import net.minecraft.entity.ai.goal.OwnerHurtTargetGoal;

public class RatAIOwnerHurtTarget extends OwnerHurtTargetGoal {

    public RatAIOwnerHurtTarget(EntityRat entityRat) {
        super(entityRat);
    }

    public boolean shouldExecute(){
        return super.shouldExecute() && ((EntityRat)goalOwner).isAttackCommand();
    }
}
