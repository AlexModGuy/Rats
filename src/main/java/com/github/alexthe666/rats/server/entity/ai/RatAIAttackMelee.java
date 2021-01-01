package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.pathfinding.RatAdvancedPathNavigate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;

public class RatAIAttackMelee extends MeleeAttackGoal {
    private EntityRat rat;
    private double speed;

    public RatAIAttackMelee(EntityRat rat, double speed, boolean memory) {
        super(rat, speed, memory);
        this.rat = rat;
        this.speed = speed;
    }

    public void tick() {
        super.tick();
        if(rat.getNavigator() instanceof RatAdvancedPathNavigate && rat.getAttackTarget() != null){
            ((RatAdvancedPathNavigate)rat.getNavigator()).moveToLivingEntity(rat.getAttackTarget(), speed);
        }
        //this.attackTick = 0;
    }

    public boolean shouldExecute() {
        return !rat.isInCage() && super.shouldExecute();
    }

    protected double getAttackReachSqr(LivingEntity attackTarget) {
        if(rat.isRidingSpecialMount()){
            Entity entity = rat.getRidingEntity();
            if(entity != null){
                return (double)(entity.getWidth() * 2.0F * entity.getWidth() * 2.0F + attackTarget.getWidth());
            }
        }
        return 1.5D;
    }
}