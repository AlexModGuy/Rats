package com.github.alexthe666.rats.server.entity.ratlantis;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.rats.RatlantisConfig;
import com.github.alexthe666.rats.server.misc.RatsSoundRegistry;
import com.google.common.base.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraftforge.fluids.IFluidBlock;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class EntityRatlanteanAutomaton extends MonsterEntity implements IAnimatedEntity, IRangedAttackMob, IRatlantean {

    public static final Animation ANIMATION_MELEE = Animation.create(15);
    public static final Animation ANIMATION_RANGED = Animation.create(15);
    private static final Predicate<LivingEntity> NOT_RATLANTEAN = new Predicate<LivingEntity>() {
        public boolean apply(@Nullable LivingEntity entity) {
            return entity.isAlive() && !(entity instanceof IRatlantean);
        }
    };
    private final ServerBossInfo bossInfo = (new ServerBossInfo(this.getDisplayName(), BossInfo.Color.YELLOW, BossInfo.Overlay.PROGRESS));
    private int blockBreakCounter;
    private int animationTick;
    private boolean useRangedAttack = false;
    private Animation currentAnimation;

    public EntityRatlanteanAutomaton(EntityType type, World worldIn) {
        super(type, worldIn);
        this.setHealth(this.getMaxHealth());
        ((GroundPathNavigator) this.getNavigator()).setCanSwim(true);
        this.experienceValue = 50;
        this.stepHeight = 2;
        this.moveController = new EntityRatlanteanAutomaton.AIMoveControl(this);
    }

    public static boolean canDestroyBlock(Block blockIn) {
        return blockIn != Blocks.BEDROCK && blockIn != Blocks.END_PORTAL && blockIn != Blocks.END_PORTAL_FRAME && blockIn != Blocks.COMMAND_BLOCK && blockIn != Blocks.REPEATING_COMMAND_BLOCK && blockIn != Blocks.CHAIN_COMMAND_BLOCK && blockIn != Blocks.BARRIER && blockIn != Blocks.STRUCTURE_BLOCK && blockIn != Blocks.STRUCTURE_VOID && blockIn != Blocks.PISTON_HEAD && blockIn != Blocks.END_GATEWAY;
    }

    public boolean canDespawn(double distanceToClosestPlayer) {
        return true;
    }

    public static AttributeModifierMap.MutableAttribute func_234290_eH_() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.MAX_HEALTH, RatlantisConfig.ratlanteanAutomatonHealth)        //HEALTH
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.8D)                //SPEED
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, RatlantisConfig.ratlanteanAutomatonAttack)       //ATTACK
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 128.0D)               //FOLLOW RANGE
                .createMutableAttribute(Attributes.ARMOR, 10.0D);
    }


    public CreatureAttribute getCreatureAttribute() {
        return CreatureAttribute.UNDEFINED;
    }

    protected boolean canBeRidden(Entity entityIn) {
        return false;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new EntityRatlanteanAutomaton.AIFollowPrey(this));
        this.goalSelector.addGoal(2, new EntityRatlanteanAutomaton.AIMoveRandom());
        this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(7, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 0, false, false, NOT_RATLANTEAN));
    }

    protected void updateAITasks() {
        super.updateAITasks();
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
        if (this.blockBreakCounter > 0) {
            --this.blockBreakCounter;
            if (this.blockBreakCounter == 0 && net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this)) {
                int i1 = MathHelper.floor(this.getPosY());
                int l1 = MathHelper.floor(this.getPosX());
                int i2 = MathHelper.floor(this.getPosZ());
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
                            if (!(block instanceof IFluidBlock) && canDestroyBlock(block) && !block.isAir(BlockState, this.world, blockpos) && block.canEntityDestroy(BlockState, world, blockpos, this) && net.minecraftforge.event.ForgeEventFactory.onEntityDestroyBlock(this, blockpos, BlockState)) {
                                flag = this.world.destroyBlock(blockpos, true) || flag;
                            }
                        }
                    }
                }

                if (flag) {
                    this.world.playEvent(null, 1022, new BlockPos(this.getPositionVec()), 0);
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
        if (this.isInvulnerableTo(source)) {
            return false;
        } else if (source != DamageSource.DROWN && !(source.getTrueSource() instanceof EntityRatlanteanAutomaton)) {
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

    protected SoundEvent getAmbientSound() {
        return RatsSoundRegistry.RATLANTEAN_AUTOMATON_IDLE;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return RatsSoundRegistry.RATLANTEAN_AUTOMATON_HURT;
    }

    protected SoundEvent getDeathSound() {
        return RatsSoundRegistry.RATLANTEAN_AUTOMATON_DIE;
    }

    public void livingTick() {
        super.livingTick();
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
                this.getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE));
                this.getAttackTarget().applyKnockback(1.5F, this.getPosX() - this.getAttackTarget().getPosX(), this.getPosZ() - this.getAttackTarget().getPosZ());
                this.useRangedAttack = rand.nextBoolean();
            }
        }
        if (world.isRemote && rand.nextDouble() < 0.5F) {
            float radius = -0.5F;
            float angle = (0.01745329251F * (this.renderYawOffset));
            double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle))) + getPosX();
            double extraZ = (double) (radius * MathHelper.cos(angle)) + getPosZ();
            double extraY = 1.25 + getPosY();
            world.addParticle(ParticleTypes.END_ROD, extraX + (double) (this.rand.nextFloat() * 0.5F) - (double) 0.25F,
                    extraY,
                    extraZ + (double) (this.rand.nextFloat() * 0.5F) - (double) 0.25F,
                    0F, -0.1F, 0F);
        }
        if (this.useRangedAttack && this.getAnimation() == ANIMATION_RANGED && this.getAnimationTick() == 6) {
            float radius = -3.8F;
            float angle = (0.01745329251F * (this.renderYawOffset)) - 160F;
            double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle))) + getPosX();
            double extraZ = (double) (radius * MathHelper.cos(angle)) + getPosZ();
            double extraY = 2.4F + getPosY();
            double targetRelativeX = (this.getAttackTarget() == null ? this.getLook(1.0F).x : this.getAttackTarget().getPosX()) - extraX;
            double targetRelativeY = (this.getAttackTarget() == null ? this.getLook(1.0F).y : this.getAttackTarget().getPosY()) - extraY;
            double targetRelativeZ = (this.getAttackTarget() == null ? this.getLook(1.0F).z : this.getAttackTarget().getPosZ()) - extraZ;
            EntityGolemBeam beam = new EntityGolemBeam(RatlantisEntityRegistry.RATLANTEAN_AUTOMATON_BEAM, world, this);
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
        public AIMoveControl(EntityRatlanteanAutomaton vex) {
            super(vex);
        }

        public void tick() {
            if (this.action == MovementController.Action.MOVE_TO) {
                Vector3d vec3d = new Vector3d(this.getX() - EntityRatlanteanAutomaton.this.getPosX(), this.getY() - EntityRatlanteanAutomaton.this.getPosY(), this.getZ() - EntityRatlanteanAutomaton.this.getPosZ());
                double d0 = vec3d.length();
                double edgeLength = EntityRatlanteanAutomaton.this.getBoundingBox().getAverageEdgeLength();
                if (d0 < edgeLength) {
                    this.action = MovementController.Action.WAIT;
                    EntityRatlanteanAutomaton.this.setMotion(EntityRatlanteanAutomaton.this.getMotion().scale(0.5D));
                } else {
                    EntityRatlanteanAutomaton.this.setMotion(EntityRatlanteanAutomaton.this.getMotion().add(vec3d.scale(this.speed * 0.1D / d0)));
                    if (EntityRatlanteanAutomaton.this.getAttackTarget() == null) {
                        Vector3d vec3d1 = EntityRatlanteanAutomaton.this.getMotion();
                        EntityRatlanteanAutomaton.this.rotationYaw = -((float)MathHelper.atan2(vec3d1.x, vec3d1.z)) * (180F / (float)Math.PI);
                        EntityRatlanteanAutomaton.this.renderYawOffset = EntityRatlanteanAutomaton.this.rotationYaw;
                    } else {
                        double d4 = EntityRatlanteanAutomaton.this.getAttackTarget().getPosX() - EntityRatlanteanAutomaton.this.getPosX();
                        double d5 = EntityRatlanteanAutomaton.this.getAttackTarget().getPosZ() - EntityRatlanteanAutomaton.this.getPosZ();
                        EntityRatlanteanAutomaton.this.rotationYaw = -((float) MathHelper.atan2(d4, d5)) * (180F / (float) Math.PI);
                        EntityRatlanteanAutomaton.this.renderYawOffset = EntityRatlanteanAutomaton.this.rotationYaw;
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
            return !EntityRatlanteanAutomaton.this.moveController.isUpdating() && EntityRatlanteanAutomaton.this.rand.nextInt(2) == 0;
        }

        public boolean shouldContinueExecuting() {
            return false;
        }

        public void tick() {
            BlockPos blockpos = new BlockPos(EntityRatlanteanAutomaton.this.getPositionVec());

            for (int i = 0; i < 3; ++i) {
                BlockPos blockpos1 = blockpos.add(EntityRatlanteanAutomaton.this.rand.nextInt(15) - 7, EntityRatlanteanAutomaton.this.rand.nextInt(11) - 5, EntityRatlanteanAutomaton.this.rand.nextInt(15) - 7);

                if (EntityRatlanteanAutomaton.this.world.isAirBlock(blockpos1)) {
                    EntityRatlanteanAutomaton.this.moveController.setMoveTo((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.5D, (double) blockpos1.getZ() + 0.5D, 1D);

                    if (EntityRatlanteanAutomaton.this.getAttackTarget() == null) {
                        EntityRatlanteanAutomaton.this.getLookController().setLookPosition((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.5D, (double) blockpos1.getZ() + 0.5D, 180.0F, 20.0F);
                    }

                    break;
                }
            }
        }
    }

    class AIFollowPrey extends Goal {
        private final EntityRatlanteanAutomaton parentEntity;
        public int attackTimer;
        private double followDist;

        public AIFollowPrey(EntityRatlanteanAutomaton ghast) {
            this.parentEntity = ghast;
        }

        public boolean shouldExecute() {
            followDist = EntityRatlanteanAutomaton.this.getBoundingBox().getAverageEdgeLength();
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
                EntityRatlanteanAutomaton.this.moveController.setMoveTo(LivingEntity.getPosX(), LivingEntity.getPosY() + 1, LivingEntity.getPosZ(), 1D);
            }
        }
    }
}
