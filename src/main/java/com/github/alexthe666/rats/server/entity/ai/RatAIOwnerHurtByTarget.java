package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityRat;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIOwnerHurtByTarget;
import net.minecraft.entity.ai.EntityAITarget;

public class RatAIOwnerHurtByTarget extends EntityAITarget {

    EntityRat tameable;
    EntityLivingBase attacker;
    private int timestamp;

    public RatAIOwnerHurtByTarget(EntityRat theDefendingTameableIn) {
        super(theDefendingTameableIn, false);
        this.tameable = theDefendingTameableIn;
        this.setMutexBits(1);
    }

    public boolean shouldExecute() {
        if (!this.tameable.isAttackCommand()) {
            return false;
        }
        if (!this.tameable.isTamed() && !this.tameable.isOwnerMonster()) {
            return false;
        } else {
            EntityLivingBase entitylivingbase = this.tameable.getOwner();
            if(this.tameable.isOwnerMonster()){
                entitylivingbase = this.tameable.getOwnerMonster();
            }
            if (entitylivingbase == null) {
                return false;
            } else {
                this.attacker = entitylivingbase.getRevengeTarget();
                int i = entitylivingbase.getRevengeTimer();
                return i != this.timestamp && this.isSuitableTarget(this.attacker, false) && this.tameable.shouldAttackEntity(this.attacker, entitylivingbase);
            }
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        this.taskOwner.setAttackTarget(this.attacker);
        EntityLivingBase entitylivingbase = this.tameable.getOwner();

        if (entitylivingbase != null) {
            this.timestamp = entitylivingbase.getRevengeTimer();
        }

        super.startExecuting();
    }
}
