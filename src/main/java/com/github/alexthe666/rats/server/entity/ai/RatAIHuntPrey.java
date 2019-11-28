package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityRat;
import com.google.common.base.Predicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.util.EnumHand;

public class RatAIHuntPrey<T extends LivingEntity> extends EntityAINearestAttackableTarget<LivingEntity> {
    private final EntityRat rat;

    public RatAIHuntPrey(EntityRat entityIn, Predicate<? super LivingEntity> targetSelector) {
        super(entityIn, LivingEntity.class, 10, true, false, targetSelector);
        this.rat = entityIn;
    }

    public boolean shouldExecute() {
        return !rat.isInCage() && rat.shouldHunt() && rat.getHeldItem(EnumHand.MAIN_HAND).isEmpty() && super.shouldExecute();
    }

    public boolean shouldContinueExecuting() {
        return rat.shouldHunt() && super.shouldContinueExecuting();
    }
}