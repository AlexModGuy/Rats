package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import com.github.alexthe666.rats.server.misc.RatsSoundRegistry;
import net.minecraft.entity.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityRatBeastMount extends EntityRatMountBase implements IAnimatedEntity {

    public static final Animation ANIMATION_BITE = Animation.create(15);
    public static final Animation ANIMATION_SLASH = Animation.create(25);
    public static final Animation ANIMATION_SNIFF = Animation.create(20);
    private static final DataParameter<Integer> COLOR_VARIANT = EntityDataManager.createKey(EntityFeralRatlantean.class, DataSerializers.VARINT);
    private int animationTick;
    private Animation currentAnimation;

    protected EntityRatBeastMount(EntityType<? extends MobEntity> type, World worldIn) {
        super(type, worldIn);
        this.stepHeight = 1.0F;

        this.upgrade = RatsItemRegistry.RAT_UPGRADE_BEAST_MOUNT;
    }

    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(80.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.4D);
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64.0D);
        this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(4.0D);
        this.getAttribute(SWIM_SPEED).setBaseValue(1.0D);
    }

    public boolean attackEntityAsMob(Entity entityIn) {
        if (this.getAnimation() == NO_ANIMATION) {
            this.setAnimation(rand.nextBoolean() ? ANIMATION_SLASH : ANIMATION_BITE);
        }
        return true;
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(COLOR_VARIANT, Integer.valueOf(0));
    }


    public void livingTick() {
        super.livingTick();
        AnimationHandler.INSTANCE.updateAnimations(this);
        float idleSpeed = 0.3F;
        float idleDegree = 0.015F;
        float walkSpeed = 0.4F;
        float walkDegree = 0.1F;
        float bob = -(float)(Math.sin((double)(ticksExisted * idleSpeed)) * (double)idleDegree - (double)(idleDegree));
        float bob_walk = -(float)(Math.sin((double)(limbSwing * walkSpeed))  * limbSwingAmount * (double)walkDegree - (double)(walkDegree));
        this.riderY = 0.8F + bob + bob_walk;
        this.riderXZ = 0.1F;
        if(this.getAnimation() == ANIMATION_SNIFF || this.getAnimation() == ANIMATION_SLASH){
            float max = 0.75F;
            float jumpAdd = max;
            if(this.getAnimationTick() < 5){
                jumpAdd = this.getAnimationTick() * (max / 3F);
            }
            if(this.getAnimationTick() > 15){
                jumpAdd = (20 - this.getAnimationTick()) * (max / 10F);
            }
            this.riderY += jumpAdd;
        }
        if (this.getAttackTarget() != null && this.getDistance(this.getAttackTarget()) < 5 && this.canEntityBeSeen(this.getAttackTarget()) && this.getAttackTarget().isAlive()) {
            this.faceEntity(this.getAttackTarget(), 360, 80);
            if (this.getAnimation() == ANIMATION_BITE && (this.getAnimationTick() > 8 && this.getAnimationTick() < 12)) {
                this.getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), 5);
                this.getAttackTarget().knockBack(this.getAttackTarget(), 0.25F, this.getPosX() - this.getAttackTarget().getPosX(), this.getPosZ() - this.getAttackTarget().getPosZ());
            }
            if (this.getAnimation() == ANIMATION_SLASH && (this.getAnimationTick() == 8 || this.getAnimationTick() == 16)) {
                this.getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), 5);
                this.getAttackTarget().knockBack(this.getAttackTarget(), 0.25F, this.getPosX() - this.getAttackTarget().getPosX(), this.getPosZ() - this.getAttackTarget().getPosZ());
            }
        }
        if (!world.isRemote && this.getAttackTarget() == null && this.rand.nextInt(150) == 0 && this.getAnimation() == NO_ANIMATION) {
            this.setAnimation(ANIMATION_SNIFF);
        }
    }

    public int getColorVariant() {
        return Integer.valueOf(this.getDataManager().get(COLOR_VARIANT).intValue());
    }

    public void setColorVariant(int color) {
        this.dataManager.set(COLOR_VARIANT, Integer.valueOf(color));
    }

    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("ColorVariant", this.getColorVariant());
    }

    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.setColorVariant(compound.getInt("ColorVariant"));
    }

    @Nullable
    public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        spawnDataIn = super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setColorVariant(this.getRNG().nextInt(4));
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
        return new Animation[]{ANIMATION_BITE, ANIMATION_SLASH, ANIMATION_SNIFF};
    }

    protected SoundEvent getAmbientSound() {
        return RatsSoundRegistry.RAT_PLAGUE;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return RatsSoundRegistry.RAT_HURT;
    }

    protected SoundEvent getDeathSound() {
        return RatsSoundRegistry.RAT_DIE;
    }

    protected float getSoundPitch() {
        return super.getSoundPitch() * 0.4F;
    }

}
