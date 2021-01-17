package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityRat;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.TargetGoal;

import java.util.EnumSet;

public class RatAIOwnerHurtByTarget  extends TargetGoal {
    private final EntityRat tameable;
    private LivingEntity attacker;
    private int timestamp;

    public RatAIOwnerHurtByTarget(EntityRat p_i1667_1_) {
        super(p_i1667_1_, false);
        this.tameable = p_i1667_1_;
        this.setMutexFlags(EnumSet.of(Flag.TARGET));
    }

    public boolean shouldExecute() {
        if(!tameable.isAttackCommand()){
            return false;
        }
        if ((this.tameable.isTamed() || this.tameable.wasTamedByMonster()) && !this.tameable.isSitting()) {
            LivingEntity lvt_1_1_ = this.tameable.getOwner();
            if(lvt_1_1_ == null){
                lvt_1_1_ = this.tameable.getMonsterOwner();
            }
            if (lvt_1_1_ == null) {
                return false;
            } else {
                this.attacker = lvt_1_1_.getRevengeTarget();
                int lvt_2_1_ = lvt_1_1_.getRevengeTimer();
                return lvt_2_1_ != this.timestamp && this.isSuitableTarget(this.attacker, EntityPredicate.DEFAULT) && this.tameable.shouldAttackEntity(this.attacker, lvt_1_1_);
            }
        } else {
            return false;
        }
    }

    public void startExecuting() {
        this.goalOwner.setAttackTarget(this.attacker);
        LivingEntity lvt_1_1_ = this.tameable.getOwner();
        if(lvt_1_1_ == null){
            lvt_1_1_ = this.tameable.getMonsterOwner();
        }
        if (lvt_1_1_ != null) {
            this.timestamp = lvt_1_1_.getRevengeTimer();
        }

        super.startExecuting();
    }
}
