package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.rats.server.misc.RatsSoundRegistry;
import com.google.common.base.Predicate;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

import javax.annotation.Nullable;

public class EntityFeralRatlantean extends EntityMob implements IAnimatedEntity, IRatlantean {

    public static final Animation ANIMATION_BITE = Animation.create(15);
    public static final Animation ANIMATION_SLASH = Animation.create(25);
    public static final Animation ANIMATION_SNIFF = Animation.create(20);
    public static final ResourceLocation LOOT = LootTableList.register(new ResourceLocation("rats", "feral_ratlantean"));
    private static final DataParameter<Boolean> TOGA = EntityDataManager.createKey(EntityFeralRatlantean.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> COLOR_VARIANT = EntityDataManager.createKey(EntityFeralRatlantean.class, DataSerializers.VARINT);
    private static final Predicate<EntityLivingBase> NOT_RATLANTEAN = new Predicate<EntityLivingBase>() {
        public boolean apply(@Nullable EntityLivingBase entity) {
            return entity.isEntityAlive() && !(entity instanceof IRatlantean);
        }
    };
    private int animationTick;
    private Animation currentAnimation;

    public EntityFeralRatlantean(World worldIn) {
        super(worldIn);
        this.setSize(1.85F, 1.2F);
    }

    protected void initEntityAI() {
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIAttackMelee(this, 1.0D, true));
        this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityLiving.class, 0, false, false, NOT_RATLANTEAN));
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(80.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(4.0D);
    }

    public boolean attackEntityAsMob(Entity entityIn) {
        if (this.getAnimation() == NO_ANIMATION) {
            this.setAnimation(rand.nextBoolean() ? ANIMATION_SLASH : ANIMATION_BITE);
        }
        return true;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(TOGA, Boolean.valueOf(true));
        this.dataManager.register(COLOR_VARIANT, Integer.valueOf(0));
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        AnimationHandler.INSTANCE.updateAnimations(this);
        if (this.getAttackTarget() != null && this.getDistance(this.getAttackTarget()) < 4 && this.canEntityBeSeen(this.getAttackTarget())) {
            if (this.getAnimation() == NO_ANIMATION) {
                this.setAnimation(rand.nextBoolean() ? ANIMATION_BITE : ANIMATION_SLASH);
            }
            this.faceEntity(this.getAttackTarget(), 360, 80);
            if (this.getAnimation() == ANIMATION_BITE && (this.getAnimationTick() > 8 && this.getAnimationTick() < 12)) {
                doExtraEffect(this.getAttackTarget());
                this.getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), (float) this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue());
                this.getAttackTarget().knockBack(this.getAttackTarget(), 0.25F, this.posX - this.getAttackTarget().posX, this.posZ - this.getAttackTarget().posZ);
            }
            if (this.getAnimation() == ANIMATION_SLASH && (this.getAnimationTick() == 8 || this.getAnimationTick() == 16)) {
                doExtraEffect(this.getAttackTarget());
                this.getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), (float) this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue());
                this.getAttackTarget().knockBack(this.getAttackTarget(), 0.25F, this.posX - this.getAttackTarget().posX, this.posZ - this.getAttackTarget().posZ);
            }
        }
        if (!world.isRemote && this.getAttackTarget() == null && this.rand.nextInt(150) == 0 && this.getAnimation() == NO_ANIMATION) {
            this.setAnimation(ANIMATION_SNIFF);
        }
    }

    public boolean doExtraEffect(EntityLivingBase target){
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

    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("ColorVariant", this.getColorVariant());
        compound.setBoolean("Toga", this.hasToga());
    }

    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setToga(compound.getBoolean("Toga"));
        this.setColorVariant(compound.getInteger("ColorVariant"));
    }

    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        this.setColorVariant(this.getRNG().nextInt(4));
        this.setToga(true);
        return livingdata;
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

    protected float getSoundVolume()
    {
        return 0.7F;
    }

    @Nullable
    protected ResourceLocation getLootTable() {
        return LOOT;
    }
}
