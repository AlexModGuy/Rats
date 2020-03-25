package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityPirat;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.google.common.base.Predicate;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.util.EnumHand;

public class RatAIHuntAnimals<T extends EntityLivingBase> extends EntityAINearestAttackableTarget<EntityLivingBase> {
    private final EntityRat rat;

    public RatAIHuntAnimals(EntityRat entityIn, Predicate<? super EntityLivingBase> targetSelector) {
        super(entityIn, EntityLivingBase.class, 10, true, false, targetSelector);
        this.rat = entityIn;
    }

    public boolean shouldExecute() {
        return !rat.isInCage() && shouldHunt() && ((EntityRat)taskOwner).isAttackCommand() && rat.getHeldItem(EnumHand.MAIN_HAND).isEmpty() && super.shouldExecute();
    }

    public boolean shouldContinueExecuting() {
        return shouldHunt() && super.shouldContinueExecuting();
    }

    private boolean shouldHunt(){
        return rat.shouldHuntAnimals() || rat.shouldHuntMonsters();
    }
}