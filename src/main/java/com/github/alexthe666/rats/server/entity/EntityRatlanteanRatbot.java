package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.rats.server.entity.ai.RatbotAIStrife;
import com.github.alexthe666.rats.server.misc.RatsSoundRegistry;
import com.google.common.base.Predicate;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class EntityRatlanteanRatbot extends MonsterEntity implements IAnimatedEntity, IRatlantean {

    public static final Animation ANIMATION_SHOOT = Animation.create(15);
    private static final Predicate<LivingEntity> NOT_RATLANTEAN = new Predicate<LivingEntity>() {
        public boolean apply(@Nullable LivingEntity entity) {
            return entity.isAlive() && !(entity instanceof IRatlantean);
        }
    };
    private int animationTick;
    private Animation currentAnimation;
    public int walkTick;
    public int prevWalkTick;
    private int rangedAttackCooldownLaser = 0;

    public EntityRatlanteanRatbot(EntityType type, World worldIn) {
        super(type, worldIn);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(2, new RatbotAIStrife(this, 1.0D, 20, 15.0F));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(7, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, EntityRatlanteanRatbot.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 0, false, false, NOT_RATLANTEAN));
    }


    public static AttributeModifierMap.MutableAttribute func_234290_eH_() {
        return MobEntity.func_233666_p_()
                .func_233815_a_(Attributes.field_233818_a_, 40.0D)            //HEALTH
                .func_233815_a_(Attributes.field_233821_d_, 0.15D)           //SPEED
                .func_233815_a_(Attributes.field_233823_f_, 5.0D)            //ATTACK
                .func_233815_a_(Attributes.field_233819_b_, 64.0D)         //FOLLOW RANGE
                .func_233815_a_(Attributes.field_233826_i_, 7.0D);         //ARMOR
    }


    public boolean attackEntityAsMob(Entity entityIn) {
        if (this.getAnimation() == NO_ANIMATION) {
            this.setAnimation(ANIMATION_SHOOT);
        }
        return true;
    }

    @Override
    protected void registerData() {
        super.registerData();
    }

    public void livingTick() {
        super.livingTick();
        this.prevWalkTick = this.walkTick;
        if (rangedAttackCooldownLaser == 0 && this.getAttackTarget() != null && this.canEntityBeSeen(this.getAttackTarget())) {
            rangedAttackCooldownLaser = 40;
            float radius = 1.1F;
            float angle = (0.01745329251F * (this.renderYawOffset));
            double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle))) + getPosX();
            double extraZ = (double) (radius * MathHelper.cos(angle)) + getPosZ();
            double extraY = 0.4 + getPosY();
            double targetRelativeX = this.getAttackTarget().getPosX() - extraX;
            double targetRelativeY = this.getAttackTarget().getPosY() + this.getAttackTarget().getHeight() / 2 - extraY;
            double targetRelativeZ = this.getAttackTarget().getPosZ() - extraZ;
            this.playSound(RatsSoundRegistry.LASER, 1.0F, 0.75F + rand.nextFloat() * 0.5F);
            EntityLaserBeam beam = new EntityLaserBeam(RatsEntityRegistry.LASER_BEAM, world, this);
            beam.setRGB(1.0F, 0.0F, 0.0F);
            beam.setDamage(2.0F);
            beam.setPosition(extraX, extraY, extraZ);
            beam.shoot(targetRelativeX, targetRelativeY, targetRelativeZ, 2.0F, 0.4F);
            if (!world.isRemote) {
                world.addEntity(beam);
            }
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
        if(limbSwingAmount > 0.1F){
            this.walkTick++;
        }
        if (rangedAttackCooldownLaser > 0) {
            rangedAttackCooldownLaser--;
        }
    }

    public boolean doExtraEffect(LivingEntity target) {
        return false;
    }

    @Nullable
    public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        spawnDataIn = super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
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
        return new Animation[]{ANIMATION_SHOOT};
    }

    protected SoundEvent getAmbientSound() {
        return RatsSoundRegistry.RATLANTEAN_RATBOT_IDLE;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_IRON_GOLEM_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_IRON_GOLEM_DEATH;
    }

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.ENTITY_IRON_GOLEM_STEP, 1.0F, 1.0F);
    }

    public static boolean canSpawn(EntityType<? extends MobEntity> entityType, IWorld world, SpawnReason reason, BlockPos pos, Random rand) {
        return rand.nextInt(8) == 0 && canSpawnAtPos(world, pos) && MobEntity.canSpawnOn(entityType, world, reason, pos, rand);
    }

    private static boolean canSpawnAtPos(IWorld world, BlockPos pos) {
        BlockState down = world.getBlockState(pos.down());
        return !BlockTags.getCollection().get(RatUtils.PIRAT_ONLY_BLOCKS).func_230235_a_(down.getBlock());
    }
}
