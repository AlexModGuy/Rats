package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityRat;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIOwnerHurtTarget;

public class RatAIOwnerHurtTarget extends EntityAIOwnerHurtTarget {

    public RatAIOwnerHurtTarget(EntityRat entityRat) {
        super(entityRat);
    }

    public boolean shouldExecute(){
        return super.shouldExecute() && ((EntityRat)taskOwner).isAttackCommand();
    }
}
