package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.message.MessageSyncThrownBlock;
import com.github.alexthe666.rats.server.misc.RatsSoundRegistry;
import com.google.common.base.Predicate;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

public class EntityNeoRatlantean extends MonsterEntity implements IAnimatedEntity, IRangedAttackMob, IRatlantean {

    private static final Predicate<LivingEntity> NOT_RATLANTEAN = new Predicate<LivingEntity>() {
        public boolean apply(@Nullable LivingEntity entity) {
            return entity.isAlive() && !(entity instanceof IRatlantean);
        }
    };
    private static final DataParameter<Integer> COLOR_VARIANT = EntityDataManager.createKey(EntityFeralRatlantean.class, DataSerializers.VARINT);
    private final ServerBossInfo bossInfo = (new ServerBossInfo(this.getDisplayName(), BossInfo.Color.BLUE, BossInfo.Overlay.PROGRESS));
    private int animationTick;
    private Animation currentAnimation;
    private int attackSelection = 0;
    private int summonCooldown = 0;
    private int humTicks = 0;

    public EntityNeoRatlantean(EntityType type, World worldIn) {
        super(type, worldIn);
        this.setHealth(this.getMaxHealth());
        ((GroundPathNavigator) this.getNavigator()).setCanSwim(true);
        this.experienceValue = 80;
        this.moveController = new EntityNeoRatlantean.AIMoveControl(this);
    }

    protected void updateAITasks() {
        super.updateAITasks();
        if (this.ticksExisted % 100 == 0) {
            this.heal(1);
        }
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
    }

    public boolean isNonBoss() {
        return false;
    }

    protected boolean canDespawn() {
        return false;
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(COLOR_VARIANT, Integer.valueOf(0));
    }

    public int getColorVariant() {
        return Integer.valueOf(this.dataManager.get(COLOR_VARIANT).intValue());
    }

    public void setColorVariant(int color) {
        this.dataManager.set(COLOR_VARIANT, Integer.valueOf(color));
    }

    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("ColorVariant", this.getColorVariant());
        compound.putInt("AttackSelection", attackSelection);
    }

    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.setColorVariant(compound.getInt("ColorVariant"));
        attackSelection = compound.getInt("AttackSelection");
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

    @Nullable
    public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        spawnDataIn = super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setColorVariant(this.getRNG().nextInt(4));
        return spawnDataIn;
    }

    public void tick() {
        super.tick();
        if (world.isRemote) {
            RatsMod.PROXY.addParticle("rat_lightning", this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth()) - (double) this.getWidth() / 2,
                    this.getPosY() + this.getEyeHeight() + (this.rand.nextFloat() * 0.35F),
                    this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth()) - (double) this.getWidth() / 2,
                    0.0F, 0.0F, 0.0F);
        }
        if (summonCooldown > 0) {
            summonCooldown--;
        }
    }

    public void livingTick() {
        super.livingTick();
        if (humTicks % 80 == 0) {
            this.playSound(RatsSoundRegistry.NEORATLANTEAN_LOOP, 1, 1);
        }
        humTicks++;
        if (!world.isRemote && this.getAttackTarget() != null) {
            Entity entity = this.getAttackTarget();
            if (attackSelection == 0 && summonCooldown == 0) {
                summonCooldown = 100;
                int bounds = 5;
                for (int i = 0; i < rand.nextInt(3) + 3; i++) {
                    EntityLaserPortal laserPortal = new EntityLaserPortal(RatsEntityRegistry.LASER_PORTAL, world, entity.getPosX() + this.rand.nextInt(bounds * 2) - bounds, this.getPosY() + 2, entity.getPosZ() + this.rand.nextInt(bounds * 2) - bounds, this);
                    world.addEntity(laserPortal);
                }
                resetAttacks();
            }
            if (attackSelection == 1 && summonCooldown == 0) {
                int bounds = 20;
                if(!world.isRemote){
                    ((ServerWorld)this.world).addLightningBolt(new LightningBoltEntity(this.world, entity.getPosX(), entity.getPosY(), entity.getPosZ(), false));
                    for (int i = 0; i < rand.nextInt(3) + 2; i++) {
                        ((ServerWorld)this.world).addLightningBolt(new LightningBoltEntity(this.world, entity.getPosX() + this.rand.nextInt(bounds * 2) - bounds, entity.getPosY(), entity.getPosZ() + this.rand.nextInt(bounds * 2) - bounds, false));
                    }
                }

                summonCooldown = 100;
                resetAttacks();
            }
            if (attackSelection == 2 && summonCooldown == 0) {
                int searchRange = 10;
                BlockPos ourPos = new BlockPos(this);
                List<BlockPos> listOfAll = new ArrayList<>();
                for (BlockPos pos : BlockPos.getAllInBox(ourPos.add(-searchRange, -searchRange, -searchRange), ourPos.add(searchRange, searchRange, searchRange)).map(BlockPos::toImmutable).collect(Collectors.toList())) {
                    BlockState state = world.getBlockState(pos);
                    if (!world.isAirBlock(pos) && canPickupBlock(state)) {
                        listOfAll.add(pos);
                    }
                }
                boolean flag = false;
                if (listOfAll.size() > 0) {
                    BlockPos pos = listOfAll.get(rand.nextInt(listOfAll.size()));
                    EntityThrownBlock thrownBlock = new EntityThrownBlock(RatsEntityRegistry.THROWN_BLOCK, world, world.getBlockState(pos), this);
                    thrownBlock.setPosition(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
                    if (!world.isRemote) {
                        world.addEntity(thrownBlock);
                    }
                    RatsMod.sendMSGToAll(new MessageSyncThrownBlock(thrownBlock.getEntityId(), pos.toLong()));
                    world.setBlockState(pos, Blocks.AIR.getDefaultState());
                    summonCooldown = 40;
                }
                resetAttacks();
            }
            if (attackSelection == 3 && summonCooldown == 0) {
                this.getAttackTarget().addPotionEffect(new EffectInstance(Effects.GLOWING, 200));
                this.getAttackTarget().addPotionEffect(new EffectInstance(Effects.WITHER, 200));
                this.getAttackTarget().addPotionEffect(new EffectInstance(Effects.LEVITATION, 200));
                summonCooldown = 40;
                resetAttacks();
            }
        }
    }

    public void resetAttacks() {
        attackSelection = rand.nextInt(4);
    }

    public boolean canPickupBlock(BlockState state) {
        return WitherEntity.canDestroyBlock(state);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new EntityNeoRatlantean.AIFollowPrey(this));
        this.goalSelector.addGoal(2, new EntityNeoRatlantean.AIMoveRandom());
        this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(7, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 0, false, false, NOT_RATLANTEAN));
    }

    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(RatConfig.neoRatlanteanHealth);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(1.0D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(RatConfig.neoRatlanteanAttack);
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(128.0D);
        this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(0.0D);
    }

    public void fall(float distance, float damageMultiplier) {

    }

    protected void updateFallState(double y, boolean onGroundIn, BlockState state, BlockPos pos) {

    }

    @Override
    public int getAnimationTick() {
        return 0;
    }

    @Override
    public void setAnimationTick(int tick) {

    }

    @Override
    public Animation getAnimation() {
        return null;
    }

    @Override
    public void setAnimation(Animation animation) {

    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[0];
    }

    @Override
    public void attackEntityWithRangedAttack(LivingEntity target, float distanceFactor) {

    }

    protected SoundEvent getAmbientSound() {
        return RatsSoundRegistry.NEORATLANTEAN_IDLE;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return RatsSoundRegistry.NEORATLANTEAN_HURT;
    }

    protected SoundEvent getDeathSound() {
        return RatsSoundRegistry.NEORATLANTEAN_DIE;
    }

    public int getTalkInterval() {
        return 10;
    }

    class AIMoveControl extends MovementController {
        public AIMoveControl(EntityNeoRatlantean vex) {
            super(vex);
        }

        public void tick() {
            if (this.action == MovementController.Action.MOVE_TO) {
                Vector3d vec3d = new Vector3d(this.getX() - EntityNeoRatlantean.this.getPosX(), this.getY() - EntityNeoRatlantean.this.getPosY(), this.getZ() - EntityNeoRatlantean.this.getPosZ());
                double d0 = vec3d.length();
                double edgeLength = EntityNeoRatlantean.this.getBoundingBox().getAverageEdgeLength();
                if (d0 < edgeLength) {
                    this.action = MovementController.Action.WAIT;
                    EntityNeoRatlantean.this.setMotion(EntityNeoRatlantean.this.getMotion().scale(0.5D));
                } else {
                    EntityNeoRatlantean.this.setMotion(EntityNeoRatlantean.this.getMotion().add(vec3d.scale(this.speed * 0.1D / d0)));
                    if (EntityNeoRatlantean.this.getAttackTarget() == null) {
                        Vector3d vec3d1 = EntityNeoRatlantean.this.getMotion();
                        EntityNeoRatlantean.this.rotationYaw = -((float)MathHelper.atan2(vec3d1.x, vec3d1.z)) * (180F / (float)Math.PI);
                        EntityNeoRatlantean.this.renderYawOffset = EntityNeoRatlantean.this.rotationYaw;
                    } else {
                        double d4 = EntityNeoRatlantean.this.getAttackTarget().getPosX() - EntityNeoRatlantean.this.getPosX();
                        double d5 = EntityNeoRatlantean.this.getAttackTarget().getPosZ() - EntityNeoRatlantean.this.getPosZ();
                        EntityNeoRatlantean.this.rotationYaw = -((float) MathHelper.atan2(d4, d5)) * (180F / (float) Math.PI);
                        EntityNeoRatlantean.this.renderYawOffset = EntityNeoRatlantean.this.rotationYaw;
                    }
                }
            }
        }
    }

    public class AIFollowPrey extends Goal {
        private final EntityNeoRatlantean parentEntity;
        public int attackTimer;
        private double followDist;

        public AIFollowPrey(EntityNeoRatlantean ghast) {
            this.parentEntity = ghast;
        }

        public boolean shouldExecute() {
            followDist = EntityNeoRatlantean.this.getBoundingBox().getAverageEdgeLength();
            LivingEntity LivingEntity = this.parentEntity.getAttackTarget();
            double maxFollow = followDist * 5;
            return LivingEntity != null && (LivingEntity.getDistance(this.parentEntity) >= maxFollow || !this.parentEntity.canEntityBeSeen(LivingEntity));
        }

        public void startExecuting() {
            this.attackTimer = 0;
        }

        public void resetTask() {
        }

        public void tick() {
            LivingEntity LivingEntity = this.parentEntity.getAttackTarget();
            double maxFollow = followDist * 5;
            if (LivingEntity.getDistance(this.parentEntity) >= maxFollow || !this.parentEntity.canEntityBeSeen(LivingEntity)) {

                EntityNeoRatlantean.this.moveController.setMoveTo(LivingEntity.getPosX() + rand.nextInt(10) - 20, LivingEntity.getPosY() + 3, LivingEntity.getPosZ() + rand.nextInt(10) - 20, 1D);
            }
        }
    }

    public class AIMoveRandom extends Goal {

        public AIMoveRandom() {
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean shouldExecute() {
            return !EntityNeoRatlantean.this.moveController.isUpdating() && EntityNeoRatlantean.this.rand.nextInt(2) == 0;
        }

        public boolean shouldContinueExecuting() {
            return false;
        }

        public void tick() {
            BlockPos blockpos = new BlockPos(EntityNeoRatlantean.this);

            for (int i = 0; i < 3; ++i) {
                BlockPos blockpos1 = blockpos.add(EntityNeoRatlantean.this.rand.nextInt(15) - 7, EntityNeoRatlantean.this.rand.nextInt(11) - 5, EntityNeoRatlantean.this.rand.nextInt(15) - 7);

                if (EntityNeoRatlantean.this.world.isAirBlock(blockpos1)) {
                    EntityNeoRatlantean.this.moveController.setMoveTo((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.5D, (double) blockpos1.getZ() + 0.5D, 1D);

                    if (EntityNeoRatlantean.this.getAttackTarget() == null) {
                        EntityNeoRatlantean.this.getLookController().setLookPosition((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.5D, (double) blockpos1.getZ() + 0.5D, 180.0F, 20.0F);
                    }

                    break;
                }
            }
        }
    }
}
