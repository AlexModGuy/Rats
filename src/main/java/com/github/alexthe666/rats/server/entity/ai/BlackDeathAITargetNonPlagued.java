package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.EntityRatKing;
import com.github.alexthe666.rats.server.entity.IPlagueLegion;
import com.google.common.base.Predicate;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;

import javax.annotation.Nullable;

public class BlackDeathAITargetNonPlagued extends NearestAttackableTargetGoal {

    private static final Predicate<LivingEntity> NOT_PLAGUE = new Predicate<LivingEntity>() {
        public boolean apply(@Nullable LivingEntity entity) {
            return entity.isAlive() && !(entity instanceof IPlagueLegion) && !(entity instanceof EntityRat && ((EntityRat) entity).hasPlague());
        }
    };

    public BlackDeathAITargetNonPlagued(CreatureEntity creature, Class<LivingEntity> LivingEntityClass, boolean b) {
        super(creature, LivingEntityClass, 10, b, false, NOT_PLAGUE);
    }

    protected boolean isSuitableTarget(@Nullable LivingEntity potentialTarget, EntityPredicate targetPredicate) {
        if (super.isSuitableTarget(potentialTarget, targetPredicate)) {
            if (potentialTarget instanceof IPlagueLegion) {
                return false;
            }
            return !(potentialTarget instanceof EntityRat) || !((EntityRat) potentialTarget).hasPlague();
        }
        return false;
    }
}
