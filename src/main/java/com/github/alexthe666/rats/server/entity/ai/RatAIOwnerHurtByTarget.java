package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityRat;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIOwnerHurtByTarget;

public class RatAIOwnerHurtByTarget extends EntityAIOwnerHurtByTarget {

    public RatAIOwnerHurtByTarget(EntityRat entityRat) {
        super(entityRat);
    }

    public boolean shouldExecute(){
        return super.shouldExecute() && ((EntityRat)taskOwner).isAttackCommand();
    }
}
