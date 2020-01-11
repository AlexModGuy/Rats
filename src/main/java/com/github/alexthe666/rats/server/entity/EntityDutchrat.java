package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import com.google.common.base.Predicate;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.*;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class EntityDutchrat extends MonsterEntity implements IAnimatedEntity, IRangedAttackMob, IRatlantean, IPirat{

    private int animationTick;
    private boolean useRangedAttack = false;
    private int ticksSinceThrownSword = 0;
    private Animation currentAnimation;
    public static final Animation ANIMATION_SLASH = Animation.create(25);
    public static final Animation ANIMATION_STAB = Animation.create(17);
    public static final Animation ANIMATION_THROW = Animation.create(15);
    public static final Animation ANIMATION_SPEAK = Animation.create(10);
    private static final DataParameter<Boolean> THROWN_SWORD = EntityDataManager.createKey(EntityDutchrat.class, DataSerializers.BOOLEAN);
    private static final Predicate<LivingEntity> NOT_RATLANTEAN = new Predicate<LivingEntity>() {
        public boolean apply(@Nullable LivingEntity entity) {
            return entity.isAlive() && !(entity instanceof IRatlantean);
        }
    };
    private final ServerBossInfo bossInfo = (new ServerBossInfo(this.getDisplayName(), BossInfo.Color.GREEN, BossInfo.Overlay.PROGRESS));

    public EntityDutchrat(EntityType<? extends MonsterEntity> type, World world) {
        super(type, world);
        this.moveController = new EntityDutchrat.AIMoveControl(this);
    }

    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(RatConfig.dutchratHealth);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(RatConfig.dutchratAttack);
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(128.0D);
        this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(10.0D);
    }

    protected void updateAITasks() {
        super.updateAITasks();
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new EntityDutchrat.AIFollowPrey(this));
        this.goalSelector.addGoal(2, new EntityDutchrat.AIMoveRandom());
        this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(7, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 0, false, false, NOT_RATLANTEAN));
    }


    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(THROWN_SWORD, Boolean.valueOf(false));
    }

    public void setThrownSword(boolean sword) {
        this.dataManager.set(THROWN_SWORD, Boolean.valueOf(sword));
    }

    public boolean hasThrownSword() {
        return this.dataManager.get(THROWN_SWORD).booleanValue();
    }

    public boolean hasNoGravity() {
        return true;
    }

    public void livingTick() {
        super.livingTick();
        this.noClip = true;
        if(this.getAnimation() == ANIMATION_THROW && this.getAnimationTick() > 7){
            if(!this.hasThrownSword()){
                float radius = -1.5F;
                float angle = (0.01745329251F * (this.renderYawOffset)) - 230F;
                double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle))) + posX;
                double extraZ = (double) (radius * MathHelper.cos(angle)) + posZ;
                double extraY = 1.7F + posY;
                EntityDutchratSword sword = new EntityDutchratSword(RatsEntityRegistry.DUTCHRAT_SWORD, world, extraX, extraY, extraZ, this);
                world.addEntity(sword);
            }
            this.setThrownSword(true);
        }
        if(!world.isRemote){
            if(this.hasThrownSword()){
                ticksSinceThrownSword++;
            }
            if(ticksSinceThrownSword > 60){
                this.setThrownSword(false);
                this.ticksSinceThrownSword = 0;
            }
        }
        if (this.getAttackTarget() != null) {
            this.useRangedAttack = this.getDistance(this.getAttackTarget()) > 10;
        }
        if (this.useRangedAttack && this.getAnimation() != ANIMATION_THROW  && !this.hasThrownSword() && this.getAttackTarget() != null && this.canEntityBeSeen(this.getAttackTarget())) {
            this.setAnimation(ANIMATION_THROW);
            this.faceEntity(this.getAttackTarget(), 360, 80);
        }
        if (!this.useRangedAttack && this.getAttackTarget() != null && !this.hasThrownSword() && this.getDistance(this.getAttackTarget()) < 7 && this.canEntityBeSeen(this.getAttackTarget())) {
            if (this.getAnimation() == NO_ANIMATION) {
                this.setAnimation(rand.nextBoolean() ? ANIMATION_SLASH : ANIMATION_STAB);
            }
            this.faceEntity(this.getAttackTarget(), 360, 80);
            if (this.getAnimation() == ANIMATION_SLASH && (this.getAnimationTick() == 10 || this.getAnimationTick() == 20)) {
                this.getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), (float) this.getAttributes().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).getValue());
                this.getAttackTarget().knockBack(this.getAttackTarget(), 1.5F, this.posX - this.getAttackTarget().posX, this.posZ - this.getAttackTarget().posZ);
                this.useRangedAttack = rand.nextBoolean();
            }
            if (this.getAnimation() == ANIMATION_STAB && (this.getAnimationTick() == 10)) {
                this.getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), (float) this.getAttributes().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).getValue());
                this.getAttackTarget().knockBack(this.getAttackTarget(), 1.5F, this.getAttackTarget().posX - this.posX, this.getAttackTarget().posZ - this.posZ);
                this.useRangedAttack = rand.nextBoolean();
            }
        }
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

    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        if (this.hasCustomName()) {
            this.bossInfo.setName(this.getDisplayName());
        }
    }

    public void setCustomName(ITextComponent name) {
        super.setCustomName(name);
        this.bossInfo.setName(this.getDisplayName());
    }


    public void addTrackingPlayer(ServerPlayerEntity player) {
        super.addTrackingPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    public void removeTrackingPlayer(ServerPlayerEntity player) {
        super.removeTrackingPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    class AIMoveControl extends MovementController {
        public AIMoveControl(EntityDutchrat vex) {
            super(vex);
        }

        public void tick() {
            if (this.action == MovementController.Action.MOVE_TO) {
                Vec3d vec3d = new Vec3d(this.posX - EntityDutchrat.this.posX, this.posY - EntityDutchrat.this.posY, this.posZ - EntityDutchrat.this.posZ);
                double d0 = vec3d.length();
                double edgeLength = EntityDutchrat.this.getBoundingBox().getAverageEdgeLength();
                if (d0 < edgeLength) {
                    this.action = MovementController.Action.WAIT;
                    EntityDutchrat.this.setMotion(EntityDutchrat.this.getMotion().scale(0.5D));
                } else {
                    EntityDutchrat.this.setMotion(EntityDutchrat.this.getMotion().add(vec3d.scale(this.speed * 0.1D / d0)));
                    if (EntityDutchrat.this.getAttackTarget() == null) {
                        Vec3d vec3d1 = EntityDutchrat.this.getMotion();
                        EntityDutchrat.this.rotationYaw = -((float)MathHelper.atan2(vec3d1.x, vec3d1.z)) * (180F / (float)Math.PI);
                        EntityDutchrat.this.renderYawOffset = EntityDutchrat.this.rotationYaw;
                    } else {
                        double d4 = EntityDutchrat.this.getAttackTarget().posX - EntityDutchrat.this.posX;
                        double d5 = EntityDutchrat.this.getAttackTarget().posZ - EntityDutchrat.this.posZ;
                        EntityDutchrat.this.rotationYaw = -((float) MathHelper.atan2(d4, d5)) * (180F / (float) Math.PI);
                        EntityDutchrat.this.renderYawOffset = EntityDutchrat.this.rotationYaw;
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
            return !EntityDutchrat.this.moveController.isUpdating() && EntityDutchrat.this.rand.nextInt(2) == 0;
        }

        public boolean shouldContinueExecuting() {
            return false;
        }

        public void tick() {
            BlockPos blockpos = new BlockPos(EntityDutchrat.this);
            if(EntityDutchrat.this.detachHome()){
                blockpos = EntityDutchrat.this.getHomePosition();
            }
            for (int i = 0; i < 3; ++i) {
                BlockPos blockpos1 = blockpos.add(EntityDutchrat.this.rand.nextInt(15) - 7, Math.min(EntityDutchrat.this.rand.nextInt(10) - 5, 10), EntityDutchrat.this.rand.nextInt(15) - 7);

                if (EntityDutchrat.this.world.isAirBlock(blockpos1)) {
                    EntityDutchrat.this.moveController.setMoveTo((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.5D, (double) blockpos1.getZ() + 0.5D, 1D);

                    if (EntityDutchrat.this.getAttackTarget() == null) {
                        EntityDutchrat.this.getLookController().setLookPosition((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.5D, (double) blockpos1.getZ() + 0.5D, 180.0F, 20.0F);
                    }

                    break;
                }
            }
        }
    }

    class AIFollowPrey extends Goal {
        private final EntityDutchrat parentEntity;
        public int attackTimer;
        private double followDist;

        public AIFollowPrey(EntityDutchrat ghast) {
            this.parentEntity = ghast;
        }

        public boolean shouldExecute() {
            followDist = EntityDutchrat.this.getBoundingBox().getAverageEdgeLength();
            LivingEntity LivingEntity = this.parentEntity.getAttackTarget();
            double maxFollow = this.parentEntity.useRangedAttack ? 5 * followDist : followDist;
            return LivingEntity != null && (LivingEntity.getDistance(this.parentEntity) >= maxFollow || !this.parentEntity.canEntityBeSeen(LivingEntity));
        }

        public void startExecuting() {
            this.attackTimer = 0;
        }

        public void resetTask() {
        }

        public void tick() {
            LivingEntity LivingEntity = this.parentEntity.getAttackTarget();
            double maxFollow = this.parentEntity.useRangedAttack ? 5 * followDist : followDist;
            if (LivingEntity.getDistance(this.parentEntity) >= maxFollow || !this.parentEntity.canEntityBeSeen(LivingEntity)) {
               if(EntityDutchrat.this.hasThrownSword()){
                   BlockPos blockpos = new BlockPos(EntityDutchrat.this);
                   if(EntityDutchrat.this.detachHome()){
                       blockpos = EntityDutchrat.this.getHomePosition();
                   }
                   BlockPos blockpos1 = blockpos.add(EntityDutchrat.this.rand.nextInt(15) - 7, 0, EntityDutchrat.this.rand.nextInt(15) - 7);
                   EntityDutchrat.this.moveController.setMoveTo(blockpos1.getX(), blockpos1.getY(), blockpos1.getZ(), 1D);
               }else{
                   EntityDutchrat.this.moveController.setMoveTo(LivingEntity.posX, LivingEntity.posY + 1, LivingEntity.posZ, 1D);
               }
            }
        }
    }
}
