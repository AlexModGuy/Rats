package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.misc.RatsSoundRegistry;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.EnumSet;
import java.util.Random;

public class EntityRatlanteanSpirit extends MonsterEntity implements IAnimatedEntity, IRatlantean {

    public static final Animation ANIMATION_ATTACK = Animation.create(10);
    private int animationTick;
    private Animation currentAnimation;

    public EntityRatlanteanSpirit(EntityType type, World worldIn) {
        super(type, worldIn);
        this.moveController = new EntityRatlanteanSpirit.AIMoveControl(this);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new EntityRatlanteanSpirit.AIFireballAttack(this));
        this.goalSelector.addGoal(8, new EntityRatlanteanSpirit.AIMoveRandom());
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, LivingEntity.class, 8.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[0]));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, PlayerEntity.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, IronGolemEntity.class, false));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal(this, AbstractVillagerEntity.class, false));
    }

    public static AttributeModifierMap.MutableAttribute func_234290_eH_() {
        return MobEntity.func_233666_p_()
                .func_233815_a_(Attributes.field_233818_a_, 20.0D)            //HEALTH
                .func_233815_a_(Attributes.field_233821_d_, 0.15D)           //SPEED
                .func_233815_a_(Attributes.field_233823_f_, 4.0D)            //ATTACK
                .func_233815_a_(Attributes.field_233819_b_, 64.0D)         //FOLLOW RANGE
                .func_233815_a_(Attributes.field_233826_i_, 0);         //ARMOR
    }


    public void move(MoverType typeIn, Vector3d pos) {
        super.move(typeIn, pos);
        this.doBlockCollisions();
    }

    public void tick() {
        this.noClip = true;
        super.tick();
        this.noClip = false;
        this.setNoGravity(true);
        AnimationHandler.INSTANCE.updateAnimations(this);
        if (world.isRemote) {
            RatsMod.PROXY.addParticle("rat_ghost", this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth() * 2F) - (double) this.getWidth(),
                    this.getPosY() + (double) (this.rand.nextFloat() * this.getHeight()),
                    this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth() * 2F) - (double) this.getWidth(),
                    0.92F, 0.82, 0.0F);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public int getBrightnessForRender() {
        return 15728880;
    }

    public float getBrightness() {
        return 1.0F;
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
        return new Animation[]{ANIMATION_ATTACK};
    }
    
    protected SoundEvent getAmbientSound() {
        return RatsSoundRegistry.RATLANTEAN_SPIRIT_IDLE;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return RatsSoundRegistry.RATLANTEAN_SPIRIT_HURT;
    }

    protected SoundEvent getDeathSound() {
        return RatsSoundRegistry.RATLANTEAN_SPIRIT_DIE;
    }

    class AIMoveControl extends MovementController {
        public AIMoveControl(EntityRatlanteanSpirit vex) {
            super(vex);
        }

        public void tick() {
            if (this.action == MovementController.Action.MOVE_TO) {
                Vector3d vec3d = new Vector3d(this.getX() - EntityRatlanteanSpirit.this.getPosX(), this.getY() - EntityRatlanteanSpirit.this.getPosY(), this.getZ() - EntityRatlanteanSpirit.this.getPosZ());
                double d0 = vec3d.length();
                double edgeLength = EntityRatlanteanSpirit.this.getBoundingBox().getAverageEdgeLength();
                if (d0 < edgeLength) {
                    this.action = MovementController.Action.WAIT;
                    EntityRatlanteanSpirit.this.setMotion(EntityRatlanteanSpirit.this.getMotion().scale(0.5D));
                } else {
                    EntityRatlanteanSpirit.this.setMotion(EntityRatlanteanSpirit.this.getMotion().add(vec3d.scale(this.speed * 0.1D / d0)));
                    if (EntityRatlanteanSpirit.this.getAttackTarget() == null) {
                        Vector3d vec3d1 = EntityRatlanteanSpirit.this.getMotion();
                        EntityRatlanteanSpirit.this.rotationYaw = -((float)MathHelper.atan2(vec3d1.x, vec3d1.z)) * (180F / (float)Math.PI);
                        EntityRatlanteanSpirit.this.renderYawOffset = EntityRatlanteanSpirit.this.rotationYaw;
                    } else {
                        double d4 = EntityRatlanteanSpirit.this.getAttackTarget().getPosX() - EntityRatlanteanSpirit.this.getPosX();
                        double d5 = EntityRatlanteanSpirit.this.getAttackTarget().getPosZ() - EntityRatlanteanSpirit.this.getPosZ();
                        EntityRatlanteanSpirit.this.rotationYaw = -((float) MathHelper.atan2(d4, d5)) * (180F / (float) Math.PI);
                        EntityRatlanteanSpirit.this.renderYawOffset = EntityRatlanteanSpirit.this.rotationYaw;
                    }
                }
            }
        }
    }

    class AIMoveRandom extends Goal {

        public AIMoveRandom() {
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean shouldExecute() {
            return !EntityRatlanteanSpirit.this.moveController.isUpdating() && EntityRatlanteanSpirit.this.rand.nextInt(2) == 0;
        }

        public boolean shouldContinueExecuting() {
            return false;
        }

        public void tick() {
            BlockPos blockpos = new BlockPos(EntityRatlanteanSpirit.this.getPositionVec());

            for (int i = 0; i < 3; ++i) {
                BlockPos blockpos1 = blockpos.add(EntityRatlanteanSpirit.this.rand.nextInt(15) - 7, EntityRatlanteanSpirit.this.rand.nextInt(11) - 5, EntityRatlanteanSpirit.this.rand.nextInt(15) - 7);

                if (EntityRatlanteanSpirit.this.world.isAirBlock(blockpos1)) {
                    EntityRatlanteanSpirit.this.moveController.setMoveTo((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.5D, (double) blockpos1.getZ() + 0.5D, 0.25D);

                    if (EntityRatlanteanSpirit.this.getAttackTarget() == null) {
                        EntityRatlanteanSpirit.this.getLookController().setLookPosition((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.5D, (double) blockpos1.getZ() + 0.5D, 180.0F, 20.0F);
                    }

                    break;
                }
            }
        }
    }

    class AIFireballAttack extends Goal {
        private final EntityRatlanteanSpirit parentEntity;
        public int attackTimer;

        public AIFireballAttack(EntityRatlanteanSpirit ghast) {
            this.parentEntity = ghast;
        }

        public boolean shouldExecute() {
            return this.parentEntity.getAttackTarget() != null;
        }

        public void startExecuting() {
            this.attackTimer = 0;
        }

        public void resetTask() {
        }

        public void tick() {
            LivingEntity LivingEntity = this.parentEntity.getAttackTarget();
            double d0 = 64.0D;
            if (LivingEntity.getDistanceSq(this.parentEntity) >= 4096.0D || !this.parentEntity.canEntityBeSeen(LivingEntity)) {

                EntityRatlanteanSpirit.this.moveController.setMoveTo(LivingEntity.getPosX(), LivingEntity.getPosY(), LivingEntity.getPosZ(), 0.5D);

            }
            if (LivingEntity.getDistanceSq(this.parentEntity) < 4096.0D) {
                World world = this.parentEntity.world;
                ++this.attackTimer;

                if (this.attackTimer == 20) {
                    double d1 = 4.0D;
                    double d2 = LivingEntity.getPosX() - (this.parentEntity.getPosX());
                    double d3 = LivingEntity.getPosY() + (double) (LivingEntity.getHeight()) - (this.parentEntity.getPosY() + (double) (this.parentEntity.getHeight() / 2.0F));
                    double d4 = LivingEntity.getPosZ() - (this.parentEntity.getPosZ());
                    world.playEvent(null, 1016, new BlockPos(this.parentEntity.getPositionVec()), 0);
                    EntityRatlanteanFlame entitylargefireball = new EntityRatlanteanFlame(world, this.parentEntity, d2, d3, d4);
                    entitylargefireball.setPosition(this.parentEntity.getPosX(), this.parentEntity.getPosY() + (double) (this.parentEntity.getHeight() / 2.0F), this.parentEntity.getPosZ());
                    world.addEntity(entitylargefireball);
                    this.attackTimer = -10;
                }
            } else if (this.attackTimer > 0) {
                --this.attackTimer;
            }
        }
    }

    public static boolean func_223315_a(EntityType<? extends MobEntity> p_223315_0_, IWorld p_223315_1_, SpawnReason p_223315_2_, BlockPos p_223315_3_, Random p_223315_4_) {
        BlockPos blockpos = p_223315_3_.down();
        return p_223315_2_ == SpawnReason.SPAWNER || p_223315_1_.getBlockState(blockpos).canEntitySpawn(p_223315_1_, blockpos, p_223315_0_) && p_223315_4_.nextInt(5) == 0;
    }
}
