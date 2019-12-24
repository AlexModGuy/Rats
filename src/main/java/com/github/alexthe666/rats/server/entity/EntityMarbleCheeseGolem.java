package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.rats.server.misc.RatsSoundRegistry;
import com.google.common.base.Predicate;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

import javax.annotation.Nullable;

public class EntityMarbleCheeseGolem extends MobEntity implements IAnimatedEntity, IRangedAttackMob, IRatlantean {

    public static final Animation ANIMATION_MELEE = Animation.create(15);
    public static final Animation ANIMATION_RANGED = Animation.create(15);
    public static final ResourceLocation LOOT = LootTableList.register(new ResourceLocation("rats", "marbled_cheese_golem"));
    private static final Predicate<LivingEntity> NOT_RATLANTEAN = new Predicate<LivingEntity>() {
        public boolean apply(@Nullable LivingEntity entity) {
            return entity.isEntityAlive() && !(entity instanceof IRatlantean);
        }
    };
    private final BossInfoServer bossInfo = (new BossInfoServer(this.getDisplayName(), BossInfo.Color.GREEN, BossInfo.Overlay.PROGRESS));
    private int blockBreakCounter;
    private int animationTick;
    private boolean useRangedAttack = false;
    private Animation currentAnimation;

    public EntityMarbleCheeseGolem(EntityType type, World worldIn) {
        super(type, worldIn);
        this.setHealth(this.getMaxHealth());
        this.setSize(2F, 3.5F);
        this.isImmuneToFire = true;
        ((PathNavigateGround) this.getNavigator()).setCanSwim(true);
        this.experienceValue = 50;
        this.stepHeight = 2;
        this.moveHelper = new EntityMarbleCheeseGolem.AIMoveControl(this);
    }

    public static boolean canDestroyBlock(Block blockIn) {
        return blockIn != Blocks.BEDROCK && blockIn != Blocks.END_PORTAL && blockIn != Blocks.END_PORTAL_FRAME && blockIn != Blocks.COMMAND_BLOCK && blockIn != Blocks.REPEATING_COMMAND_BLOCK && blockIn != Blocks.CHAIN_COMMAND_BLOCK && blockIn != Blocks.BARRIER && blockIn != Blocks.STRUCTURE_BLOCK && blockIn != Blocks.STRUCTURE_VOID && blockIn != Blocks.PISTON_EXTENSION && blockIn != Blocks.END_GATEWAY;
    }

    protected boolean canDespawn() {
        return false;
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(RatConfig.ratlanteanAutomatonHealth);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.8D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(RatConfig.ratlanteanAutomatonAttack);
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(128.0D);
        this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(10.0D);
    }

    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEFINED;
    }

    protected boolean canBeRidden(Entity entityIn) {
        return false;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new EntityMarbleCheeseGolem.AIFollowPrey(this));
        this.goalSelector.addGoal(2, new EntityMarbleCheeseGolem.AIMoveRandom());
        this.goalSelector.addGoal(5, new EntityAIWanderAvoidWater(this, 1.0D));
        this.goalSelector.addGoal(6, new EntityAIWatchClosest(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(7, new EntityAILookIdle(this));
        this.targetSelector.addGoal(1, new EntityAIHurtByTarget(this, false));
        this.targetSelector.addGoal(2, new EntityAINearestAttackableTarget(this, LivingEntity.class, 0, false, false, NOT_RATLANTEAN));
    }

    protected void updateAIgoalSelector() {
        super.updateAIgoalSelector();
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
        if (this.blockBreakCounter > 0) {
            --this.blockBreakCounter;
            if (this.blockBreakCounter == 0 && net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this)) {
                int i1 = MathHelper.floor(this.posY);
                int l1 = MathHelper.floor(this.posX);
                int i2 = MathHelper.floor(this.posZ);
                boolean flag = false;

                for (int k2 = -2; k2 <= 2; ++k2) {
                    for (int l2 = -2; l2 <= 2; ++l2) {
                        for (int j = 1; j <= 3; ++j) {
                            int i3 = l1 + k2;
                            int k = i1 + j;
                            int l = i2 + l2;
                            BlockPos blockpos = new BlockPos(i3, k, l);
                            BlockState BlockState = this.world.getBlockState(blockpos);
                            Block block = BlockState.getBlock();
                            if (!(block instanceof BlockLiquid) && canDestroyBlock(block) && !block.isAir(BlockState, this.world, blockpos) && block.canEntityDestroy(BlockState, world, blockpos, this) && net.minecraftforge.event.ForgeEventFactory.onEntityDestroyBlock(this, blockpos, BlockState)) {
                                flag = this.world.destroyBlock(blockpos, true) || flag;
                            }
                        }
                    }
                }

                if (flag) {
                    this.world.playEvent(null, 1022, new BlockPos(this), 0);
                }
            }
        }
    }

    public boolean isNonBoss() {
        return false;
    }

    public boolean attackEntityAsMob(Entity entityIn) {
        if (this.getAnimation() == NO_ANIMATION) {
            this.setAnimation(this.useRangedAttack ? ANIMATION_MELEE : ANIMATION_RANGED);
        }
        return true;
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        } else if (source != DamageSource.DROWN && !(source.getTrueSource() instanceof EntityMarbleCheeseGolem)) {
            if (this.blockBreakCounter <= 0) {
                this.blockBreakCounter = 20;
            }
            return super.attackEntityFrom(source, amount);
        } else {
            return false;
        }
    }

    @Override
    public void attackEntityWithRangedAttack(LivingEntity target, float distanceFactor) {
        this.useRangedAttack = true;
        if (this.getAnimation() == NO_ANIMATION) {
            this.setAnimation(ANIMATION_RANGED);
        }
    }

    @Override
    public void setSwingingArms(boolean swingingArms) {

    }

    protected SoundEvent getAmbientSound() {
        return RatsSoundRegistry.RATLANTEAN_AUTOMATON_IDLE;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return RatsSoundRegistry.RATLANTEAN_AUTOMATON_HURT;
    }

    protected SoundEvent getDeathSound() {
        return RatsSoundRegistry.RATLANTEAN_AUTOMATON_DIE;
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (this.blockBreakCounter <= 0) {
            this.blockBreakCounter = 20;
        }
        if (this.getAttackTarget() != null) {
            this.useRangedAttack = this.getDistance(this.getAttackTarget()) > 10;
        }
        if (this.useRangedAttack && this.getAnimation() != ANIMATION_RANGED && this.getAttackTarget() != null && this.canEntityBeSeen(this.getAttackTarget())) {
            this.setAnimation(ANIMATION_RANGED);
            this.faceEntity(this.getAttackTarget(), 360, 80);

        }
        if (!this.useRangedAttack && this.getAttackTarget() != null && this.getDistance(this.getAttackTarget()) < 7 && this.canEntityBeSeen(this.getAttackTarget())) {
            if (this.getAnimation() == NO_ANIMATION) {
                this.setAnimation(ANIMATION_MELEE);
                this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0F, 0.5F + rand.nextFloat() * 0.5F);
            }
            this.faceEntity(this.getAttackTarget(), 360, 80);
            if (this.getAnimation() == ANIMATION_MELEE && this.getAnimationTick() == 10) {
                this.getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), (float) this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue());
                this.getAttackTarget().knockBack(this.getAttackTarget(), 1.5F, this.posX - this.getAttackTarget().posX, this.posZ - this.getAttackTarget().posZ);
                this.useRangedAttack = rand.nextBoolean();
            }
        }
        if (world.isRemote && rand.nextDouble() < 0.5F) {
            float radius = -0.5F;
            float angle = (0.01745329251F * (this.renderYawOffset));
            double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle))) + posX;
            double extraZ = (double) (radius * MathHelper.cos(angle)) + posZ;
            double extraY = 1.25 + posY;
            world.addParticle(EnumParticleTypes.END_ROD, extraX + (double) (this.rand.nextFloat() * 0.5F) - (double) 0.25F,
                    extraY,
                    extraZ + (double) (this.rand.nextFloat() * 0.5F) - (double) 0.25F,
                    0F, -0.1F, 0F);
        }
        if (this.useRangedAttack && this.getAnimation() == ANIMATION_RANGED && this.getAnimationTick() == 6) {
            float radius = -3.8F;
            float angle = (0.01745329251F * (this.renderYawOffset)) - 160F;
            double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle))) + posX;
            double extraZ = (double) (radius * MathHelper.cos(angle)) + posZ;
            double extraY = 2.4F + posY;
            double targetRelativeX = (this.getAttackTarget() == null ? this.getLook(1.0F).x : this.getAttackTarget().posX) - extraX;
            double targetRelativeY = (this.getAttackTarget() == null ? this.getLook(1.0F).y : this.getAttackTarget().posY) - extraY;
            double targetRelativeZ = (this.getAttackTarget() == null ? this.getLook(1.0F).z : this.getAttackTarget().posZ) - extraZ;
            EntityGolemBeam beam = new EntityGolemBeam(world, this);
            beam.setPosition(extraX, extraY, extraZ);
            beam.shoot(targetRelativeX, targetRelativeY, targetRelativeZ, 2.0F, 0.1F);
            this.playSound(RatsSoundRegistry.LASER, 1.0F, 0.75F + rand.nextFloat() * 0.5F);
            if (!world.isRemote) {
                world.addEntity(beam);
            }
            this.useRangedAttack = rand.nextBoolean();
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
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
        return new Animation[]{ANIMATION_MELEE, ANIMATION_RANGED};
    }

    public void readEntityFromNBT(CompoundNBT compound) {
        super.readEntityFromNBT(compound);
        if (this.hasCustomName()) {
            this.bossInfo.setName(this.getDisplayName());
        }
    }

    public void setCustomNameTag(String name) {
        super.setCustomNameTag(name);
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
    protected ResourceLocation getLootTable() {
        return LOOT;
    }

    class AIMoveControl extends EntityMoveHelper {
        public AIMoveControl(EntityMarbleCheeseGolem vex) {
            super(vex);
        }

        public void onUpdateMoveHelper() {
            if (this.action == EntityMoveHelper.Action.MOVE_TO) {
                double d0 = this.posX - EntityMarbleCheeseGolem.this.posX;
                double d1 = this.posY - EntityMarbleCheeseGolem.this.posY;
                double d2 = this.posZ - EntityMarbleCheeseGolem.this.posZ;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                d3 = (double) MathHelper.sqrt(d3);

                if (d3 < EntityMarbleCheeseGolem.this.getBoundingBox().getAverageEdgeLength()) {
                    this.action = EntityMoveHelper.Action.WAIT;
                    EntityMarbleCheeseGolem.this.motionX *= 0.5D;
                    EntityMarbleCheeseGolem.this.motionY *= 0.5D;
                    EntityMarbleCheeseGolem.this.motionZ *= 0.5D;
                } else {
                    EntityMarbleCheeseGolem.this.motionX += d0 / d3 * 0.05D * this.speed;
                    EntityMarbleCheeseGolem.this.motionY += d1 / d3 * 0.05D * this.speed;
                    EntityMarbleCheeseGolem.this.motionZ += d2 / d3 * 0.05D * this.speed;

                    if (EntityMarbleCheeseGolem.this.getAttackTarget() == null) {
                        EntityMarbleCheeseGolem.this.rotationYaw = -((float) MathHelper.atan2(EntityMarbleCheeseGolem.this.motionX, EntityMarbleCheeseGolem.this.motionZ)) * (180F / (float) Math.PI);
                        EntityMarbleCheeseGolem.this.renderYawOffset = EntityMarbleCheeseGolem.this.rotationYaw;
                    } else {
                        double d4 = EntityMarbleCheeseGolem.this.getAttackTarget().posX - EntityMarbleCheeseGolem.this.posX;
                        double d5 = EntityMarbleCheeseGolem.this.getAttackTarget().posZ - EntityMarbleCheeseGolem.this.posZ;
                        EntityMarbleCheeseGolem.this.rotationYaw = -((float) MathHelper.atan2(d4, d5)) * (180F / (float) Math.PI);
                        EntityMarbleCheeseGolem.this.renderYawOffset = EntityMarbleCheeseGolem.this.rotationYaw;
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
            return !EntityMarbleCheeseGolem.this.getMoveHelper().isUpdating() && EntityMarbleCheeseGolem.this.rand.nextInt(2) == 0;
        }

        public boolean shouldContinueExecuting() {
            return false;
        }

        public void updateTask() {
            BlockPos blockpos = new BlockPos(EntityMarbleCheeseGolem.this);

            for (int i = 0; i < 3; ++i) {
                BlockPos blockpos1 = blockpos.add(EntityMarbleCheeseGolem.this.rand.nextInt(15) - 7, EntityMarbleCheeseGolem.this.rand.nextInt(11) - 5, EntityMarbleCheeseGolem.this.rand.nextInt(15) - 7);

                if (EntityMarbleCheeseGolem.this.world.isAirBlock(blockpos1)) {
                    EntityMarbleCheeseGolem.this.moveHelper.setMoveTo((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.5D, (double) blockpos1.getZ() + 0.5D, 1D);

                    if (EntityMarbleCheeseGolem.this.getAttackTarget() == null) {
                        EntityMarbleCheeseGolem.this.getLookHelper().setLookPosition((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.5D, (double) blockpos1.getZ() + 0.5D, 180.0F, 20.0F);
                    }

                    break;
                }
            }
        }
    }

    class AIFollowPrey extends Goal {
        private final EntityMarbleCheeseGolem parentEntity;
        public int attackTimer;
        private double followDist;

        public AIFollowPrey(EntityMarbleCheeseGolem ghast) {
            this.parentEntity = ghast;
        }

        public boolean shouldExecute() {
            followDist = EntityMarbleCheeseGolem.this.getBoundingBox().getAverageEdgeLength();
            LivingEntity LivingEntity = this.parentEntity.getAttackTarget();
            double maxFollow = this.parentEntity.useRangedAttack ? 5 * followDist : followDist;
            return LivingEntity != null && (LivingEntity.getDistance(this.parentEntity) >= maxFollow || !this.parentEntity.canEntityBeSeen(LivingEntity));
        }

        public void startExecuting() {
            this.attackTimer = 0;
        }

        public void resetTask() {
        }

        public void updateTask() {
            LivingEntity LivingEntity = this.parentEntity.getAttackTarget();
            double maxFollow = this.parentEntity.useRangedAttack ? 5 * followDist : followDist;
            if (LivingEntity.getDistance(this.parentEntity) >= maxFollow || !this.parentEntity.canEntityBeSeen(LivingEntity)) {
                EntityMarbleCheeseGolem.this.moveHelper.setMoveTo(LivingEntity.posX, LivingEntity.posY + 1, LivingEntity.posZ, 1D);
            }
        }
    }
}
