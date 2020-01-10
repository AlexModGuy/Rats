package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.entity.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.Hand;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityDutchrat extends MonsterEntity implements IAnimatedEntity, IRangedAttackMob, IRatlantean, IPirat{

    private int animationTick;
    private boolean useRangedAttack = false;
    private Animation currentAnimation;
    public static final Animation ANIMATION_SLASH = Animation.create(25);
    public static final Animation ANIMATION_STAB = Animation.create(25);
    public static final Animation ANIMATION_THROW = Animation.create(25);
    public static final Animation ANIMATION_SPEAK = Animation.create(10);
    private static final DataParameter<Boolean> THROWN_SWORD = EntityDataManager.createKey(EntityDutchrat.class, DataSerializers.BOOLEAN);

    protected EntityDutchrat(EntityType<? extends MonsterEntity> type, World world) {
        super(type, world);
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(THROWN_SWORD, Boolean.valueOf(false));
    }

    public void setToga(boolean sword) {
        this.dataManager.set(THROWN_SWORD, Boolean.valueOf(sword));
    }

    public boolean hasToga() {
        return this.dataManager.get(THROWN_SWORD).booleanValue();
    }


    public void livingTick() {
        super.livingTick();
        this.setAnimation(ANIMATION_THROW);
        AnimationHandler.INSTANCE.updateAnimations(this);
    }


    @Nullable
    public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        spawnDataIn = super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setHeldItem(Hand.MAIN_HAND, new ItemStack(RatsItemRegistry.GHOST_PIRAT_CUTLASS));
        this.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(RatsItemRegistry.GHOST_PIRAT_HAT));
        return spawnDataIn;
    }

    @Override
    public int getAnimationTick() {
        return animationTick;
    }

    @Override
    public void setAnimationTick(int tick) {
        animationTick = tick;
    }

    @Override
    public Animation getAnimation() {
        return currentAnimation;
    }

    @Override
    public void setAnimation(Animation animation) {
        currentAnimation = animation;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{ANIMATION_SLASH, ANIMATION_STAB, ANIMATION_THROW, ANIMATION_SPEAK};
    }

    @Override
    public void attackEntityWithRangedAttack(LivingEntity target, float distanceFactor) {

    }
}
