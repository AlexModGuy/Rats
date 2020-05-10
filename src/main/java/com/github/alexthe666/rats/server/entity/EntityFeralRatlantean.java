package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.rats.server.misc.RatsSoundRegistry;
import com.google.common.base.Predicate;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class EntityFeralRatlantean extends MonsterEntity implements IAnimatedEntity, IRatlantean {

    public static final Animation ANIMATION_BITE = Animation.create(15);
    public static final Animation ANIMATION_SLASH = Animation.create(25);
    public static final Animation ANIMATION_SNIFF = Animation.create(20);
    private static final DataParameter<Boolean> TOGA = EntityDataManager.createKey(EntityFeralRatlantean.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> COLOR_VARIANT = EntityDataManager.createKey(EntityFeralRatlantean.class, DataSerializers.VARINT);
    private static final Predicate<LivingEntity> NOT_RATLANTEAN = new Predicate<LivingEntity>() {
        public boolean apply(@Nullable LivingEntity entity) {
            return entity.isAlive() && !(entity instanceof IRatlantean);
        }
    };
    private int animationTick;
    private Animation currentAnimation;

    public EntityFeralRatlantean(EntityType type, World worldIn) {
        super(type, worldIn);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(7, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        if(!(this instanceof EntityPlagueBeast)){
            this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 0, false, false, NOT_RATLANTEAN));
        }
    }

    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(80.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0D);
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
        this.dataManager.register(TOGA, Boolean.valueOf(true));
        this.dataManager.register(COLOR_VARIANT, Integer.valueOf(0));
    }

    public void livingTick() {
        super.livingTick();
        AnimationHandler.INSTANCE.updateAnimations(this);
        if (this.getAttackTarget() != null && this.getDistance(this.getAttackTarget()) < 3 && this.canEntityBeSeen(this.getAttackTarget())) {
            if (this.getAnimation() == NO_ANIMATION) {
                this.setAnimation(rand.nextBoolean() ? ANIMATION_BITE : ANIMATION_SLASH);
            }
            this.faceEntity(this.getAttackTarget(), 360, 80);
            if (this.getAnimation() == ANIMATION_BITE && (this.getAnimationTick() > 8 && this.getAnimationTick() < 12)) {
                doExtraEffect(this.getAttackTarget());
                this.getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), (float) this.getAttributes().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).getValue());
                this.getAttackTarget().knockBack(this.getAttackTarget(), 0.25F, this.getPosX() - this.getAttackTarget().getPosX(), this.getPosZ() - this.getAttackTarget().getPosZ());
            }
            if (this.getAnimation() == ANIMATION_SLASH && (this.getAnimationTick() == 8 || this.getAnimationTick() == 16)) {
                doExtraEffect(this.getAttackTarget());
                this.getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), (float) this.getAttributes().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).getValue());
                this.getAttackTarget().knockBack(this.getAttackTarget(), 0.25F, this.getPosX() - this.getAttackTarget().getPosX(), this.getPosZ() - this.getAttackTarget().getPosZ());
            }
        }
        if (!world.isRemote && this.getAttackTarget() == null && this.rand.nextInt(150) == 0 && this.getAnimation() == NO_ANIMATION) {
            this.setAnimation(ANIMATION_SNIFF);
        }
    }

    public boolean doExtraEffect(LivingEntity target) {
        return false;
    }

    public int getColorVariant() {
        return Integer.valueOf(this.dataManager.get(COLOR_VARIANT).intValue());
    }

    public void setColorVariant(int color) {
        this.dataManager.set(COLOR_VARIANT, Integer.valueOf(color));
    }

    public void setToga(boolean plague) {
        this.dataManager.set(TOGA, Boolean.valueOf(plague));
    }

    public boolean hasToga() {
        return this.dataManager.get(TOGA).booleanValue();
    }

    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("ColorVariant", this.getColorVariant());
        compound.putBoolean("Toga", this.hasToga());
    }

    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.setToga(compound.getBoolean("Toga"));
        this.setColorVariant(compound.getInt("ColorVariant"));
    }

    @Nullable
    public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        spawnDataIn = super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setColorVariant(this.getRNG().nextInt(4));
        this.setToga(true);
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

    public static boolean canSpawn(EntityType<? extends MobEntity> entityType, IWorld world, SpawnReason reason, BlockPos pos, Random rand) {
        return rand.nextInt(8) == 0 && canSpawnAtPos(world, pos) && MobEntity.canSpawnOn(entityType, world, reason, pos, rand);
    }

    private static boolean canSpawnAtPos(IWorld world, BlockPos pos) {
        BlockState down = world.getBlockState(pos.down());
        return !BlockTags.getCollection().getOrCreate(RatUtils.PIRAT_ONLY_BLOCKS).contains(down.getBlock());
    }
}
