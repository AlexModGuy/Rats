package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.IPlagueLegion;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;

import javax.annotation.Nullable;

public class BlackDeathAITargetNonPlagued extends EntityAINearestAttackableTarget {

    public BlackDeathAITargetNonPlagued(EntityCreature creature, Class<EntityLivingBase> entityLivingBaseClass, boolean b) {
        super(creature, entityLivingBaseClass, b);
    }

    protected boolean isSuitableTarget(@Nullable EntityLivingBase target, boolean includeInvincibles) {
        if (super.isSuitableTarget(target, includeInvincibles)) {
            if (target instanceof IPlagueLegion) {
                return false;
            }
            return !(target instanceof EntityRat) || !((EntityRat) target).hasPlague();
        }
        return false;
    }
}
