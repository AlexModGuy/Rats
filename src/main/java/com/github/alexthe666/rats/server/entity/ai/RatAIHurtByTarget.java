package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityRat;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.player.EntityPlayer;

public class RatAIHurtByTarget extends EntityAIHurtByTarget {

    private EntityRat rat;

    public RatAIHurtByTarget(EntityRat rat, boolean entityCallsForHelpIn, Class<?>... excludedReinforcementTypes) {
        super(rat, entityCallsForHelpIn, excludedReinforcementTypes);
        this.rat = rat;
    }

    public boolean shouldExecute() {
        EntityLivingBase entitylivingbase = this.taskOwner.getRevengeTarget();
        if(rat.isTamed() || !(entitylivingbase instanceof EntityPlayer)){
            return super.shouldExecute();
        }
        return false;
    }
}
