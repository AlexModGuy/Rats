package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.IPlagueLegion;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;

import javax.annotation.Nullable;

public class BlackDeathAITargetNonPlagued extends EntityAINearestAttackableTarget {

    public BlackDeathAITargetNonPlagued(EntityCreature creature, Class<LivingEntity> LivingEntityClass, boolean b) {
        super(creature, LivingEntityClass, b);
    }

    protected boolean isSuitableTarget(@Nullable LivingEntity target, boolean includeInvincibles) {
        if (super.isSuitableTarget(target, includeInvincibles)) {
            if (target instanceof IPlagueLegion) {
                return false;
            }
            return !(target instanceof EntityRat) || !((EntityRat) target).hasPlague();
        }
        return false;
    }
}
