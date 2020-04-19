package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.IPlagueLegion;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;

import javax.annotation.Nullable;

public class BlackDeathAITargetNonPlagued extends NearestAttackableTargetGoal {

    public BlackDeathAITargetNonPlagued(CreatureEntity creature, Class<LivingEntity> LivingEntityClass, boolean b) {
        super(creature, LivingEntityClass, b);
    }

    protected boolean isSuitableTarget(@Nullable LivingEntity potentialTarget, EntityPredicate targetPredicate) {
        if (super.isSuitableTarget(target, targetPredicate)) {
            if (target instanceof IPlagueLegion) {
                return false;
            }
            return !(target instanceof EntityRat) || !((EntityRat) target).hasPlague();
        }
        return false;
    }
}
