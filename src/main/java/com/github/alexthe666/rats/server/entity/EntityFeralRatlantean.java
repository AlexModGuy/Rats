package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.rats.RatsMod;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityFeralRatlantean extends EntityMob implements IAnimatedEntity {

    private int animationTick;
    private Animation currentAnimation;
    private static final DataParameter<Boolean> TOGA = EntityDataManager.createKey(EntityFeralRatlantean.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> COLOR_VARIANT = EntityDataManager.createKey(EntityFeralRatlantean.class, DataSerializers.VARINT);
    public static final Animation ANIMATION_BITE = Animation.create(15);
    public static final Animation ANIMATION_SLASH = Animation.create(25);
    public static final Animation ANIMATION_SNIFF = Animation.create(20);

    public EntityFeralRatlantean(World worldIn) {
        super(worldIn);
        this.setSize(1.5F, 0.8F);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(TOGA, Boolean.valueOf(true));
        this.dataManager.register(COLOR_VARIANT, Integer.valueOf(0));
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        this.setAnimation(ANIMATION_SLASH);
        AnimationHandler.INSTANCE.updateAnimations(this);
    }


    public void setColorVariant(int color) {
        this.dataManager.set(COLOR_VARIANT, Integer.valueOf(color));
    }

    public int getColorVariant() {
        return Integer.valueOf(this.dataManager.get(COLOR_VARIANT).intValue());
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
}
