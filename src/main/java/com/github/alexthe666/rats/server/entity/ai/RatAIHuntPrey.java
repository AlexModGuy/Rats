package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityPirat;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.google.common.base.Predicate;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.util.EnumHand;

public class RatAIHuntPrey<T extends EntityLivingBase> extends EntityAINearestAttackableTarget<EntityLivingBase> {
    private final EntityRat rat;

    public RatAIHuntPrey(EntityRat entityIn, Predicate<? super EntityLivingBase> targetSelector) {
        super(entityIn, EntityLivingBase.class, 5, true, false, targetSelector);
        this.rat = entityIn;
    }

    public boolean shouldExecute() {
        if(this.rat instanceof EntityPirat){
            return super.shouldExecute();
        }
        return (this.rat.isTamed() || this.rat.hasPlague()) && !rat.isInCage() && rat.shouldHunt() && rat.getHeldItem(EnumHand.MAIN_HAND).isEmpty() && super.shouldExecute();
    }

    public boolean shouldContinueExecuting() {
        return rat.shouldHunt() && super.shouldContinueExecuting();
    }
}