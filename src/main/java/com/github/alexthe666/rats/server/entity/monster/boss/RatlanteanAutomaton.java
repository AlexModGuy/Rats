package com.github.alexthe666.rats.server.entity.monster.boss;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.registry.RatlantisEntityRegistry;
import com.github.alexthe666.rats.registry.RatsSoundRegistry;
import com.github.alexthe666.rats.server.entity.projectile.GolemBeam;
import com.github.alexthe666.rats.server.entity.Ratlanteans;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;

public class RatlanteanAutomaton extends Monster implements IAnimatedEntity, RangedAttackMob, Ratlanteans {

	public static final Animation ANIMATION_MELEE = Animation.create(15);
	public static final Animation ANIMATION_RANGED = Animation.create(15);
	private final ServerBossEvent bossInfo = (new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.YELLOW, BossEvent.BossBarOverlay.PROGRESS));
	private int blockBreakCounter;
	private int animationTick;
	private boolean useRangedAttack = false;
	private Animation currentAnimation;

	public RatlanteanAutomaton(EntityType<? extends Monster> type, Level level) {
		super(type, level);
		this.getNavigation().setCanFloat(true);
		this.xpReward = 50;
		this.moveControl = new FlyingMoveControl(this, 10, false);
	}

	@Override
	public float getStepHeight() {
		return 2.0F;
	}

	public boolean canDestroyBlock(BlockState state, BlockPos pos) {
		return !state.is(BlockTags.WITHER_IMMUNE) && state.getDestroySpeed(this.getLevel(), pos) >= 0.0F;
	}

	public boolean removeWhenFarAway(double distanceToClosestPlayer) {
		return true;
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 400.0F)
				.add(Attributes.MOVEMENT_SPEED, 0.8D)
				.add(Attributes.FLYING_SPEED, 0.8D)
				.add(Attributes.ATTACK_DAMAGE, 5.0F)
				.add(Attributes.FOLLOW_RANGE, 32.0D)
				.add(Attributes.ARMOR, 10.0D)
				.add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
	}

	@Override
	protected boolean canRide(Entity entity) {
		return false;
	}

	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new AIFollowPrey(this));
		this.goalSelector.addGoal(5, new WaterAvoidingRandomFlyingGoal(this, 1.0D));
		this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, false));
	}

	protected void customServerAiStep() {
		super.customServerAiStep();
		this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
		if (this.blockBreakCounter > 0) {
			--this.blockBreakCounter;

			if (this.blockBreakCounter == 0 && ForgeEventFactory.getMobGriefingEvent(this.getLevel(), this)) {
				int i1 = Mth.floor(this.getY());
				int l1 = Mth.floor(this.getX());
				int i2 = Mth.floor(this.getZ());
				boolean flag = false;

				for (int k2 = -2; k2 <= 2; ++k2) {
					for (int l2 = -2; l2 <= 2; ++l2) {
						for (int j = 1; j <= 3; ++j) {
							int i3 = l1 + k2;
							int k = i1 + j;
							int l = i2 + l2;
							BlockPos blockpos = new BlockPos(i3, k, l);
							BlockState state = this.getLevel().getBlockState(blockpos);
							Block block = state.getBlock();
							if (!(block instanceof LiquidBlock) && this.canDestroyBlock(state, blockpos) && !state.isAir() && block.canEntityDestroy(state, this.getLevel(), blockpos, this) && ForgeEventFactory.onEntityDestroyBlock(this, blockpos, state)) {
								flag = this.getLevel().destroyBlock(blockpos, true) || flag;
							}
						}
					}
				}

				if (flag) {
					this.getLevel().levelEvent(null, 1022, this.blockPosition(), 0);
				}
			}
		}
	}

	public boolean canChangeDimensions() {
		return false;
	}

	public boolean doHurtTarget(Entity entity) {
		if (this.getAnimation() == NO_ANIMATION) {
			this.setAnimation(this.useRangedAttack ? ANIMATION_MELEE : ANIMATION_RANGED);
		}
		return true;
	}

	public boolean hurt(DamageSource source, float amount) {
		if (this.isInvulnerableTo(source)) {
			return false;
		} else if (!(source.getEntity() instanceof RatlanteanAutomaton)) {
			if (this.blockBreakCounter <= 0) {
				this.blockBreakCounter = 20;
			}
			return super.hurt(source, amount);
		} else {
			return false;
		}
	}

	@Override
	public boolean canDrownInFluidType(FluidType type) {
		return false;
	}

	@Override
	public void performRangedAttack(LivingEntity target, float distanceFactor) {
		this.useRangedAttack = true;
		if (this.getAnimation() == NO_ANIMATION) {
			this.setAnimation(ANIMATION_RANGED);
		}
	}

	protected SoundEvent getAmbientSound() {
		return RatsSoundRegistry.RATLANTEAN_AUTOMATON_IDLE.get();
	}

	protected SoundEvent getHurtSound(DamageSource source) {
		return RatsSoundRegistry.RATLANTEAN_AUTOMATON_HURT.get();
	}

	protected SoundEvent getDeathSound() {
		return RatsSoundRegistry.RATLANTEAN_AUTOMATON_DIE.get();
	}

	public void aiStep() {
		super.aiStep();
		if (!this.isOnGround() && this.getDeltaMovement().y() < 0.0D) {
			this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, 0.75D, 1.0D));
		}
		if (this.getTarget() != null && !this.getTarget().isAlive()) {
			this.setTarget(null);
		}
		if (this.getTarget() != null) {
			this.useRangedAttack = this.distanceTo(this.getTarget()) > RatConfig.automatonRangedDistance;
		}
		if (this.useRangedAttack && this.getAnimation() != ANIMATION_RANGED && this.getTarget() != null && this.hasLineOfSight(this.getTarget())) {
			this.setAnimation(ANIMATION_RANGED);
			this.lookAt(this.getTarget(), 360, 80);

		}
		if (!this.useRangedAttack && this.getTarget() != null && this.distanceTo(this.getTarget()) < RatConfig.automatonMeleeDistance && this.hasLineOfSight(this.getTarget())) {
			if (this.getAnimation() == NO_ANIMATION) {
				this.setAnimation(ANIMATION_MELEE);
				this.playSound(SoundEvents.PLAYER_ATTACK_SWEEP, 1.0F, 0.5F + this.getRandom().nextFloat() * 0.5F);
			}
			this.lookAt(this.getTarget(), 360, 80);
			if (this.getAnimation() == ANIMATION_MELEE && this.getAnimationTick() == 10) {
				this.getTarget().hurt(this.damageSources().mobAttack(this), (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE));
				this.getTarget().knockback(1.5F, this.getX() - this.getTarget().getX(), this.getZ() - this.getTarget().getZ());
				this.useRangedAttack = this.getRandom().nextInt(RatConfig.automatonShootChance) == 0;
			}
		}
		if (this.getLevel().isClientSide() && this.getRandom().nextDouble() < 0.5F) {
			float radius = -0.5F;
			float angle = (0.01745329251F * (this.yBodyRot));
			double extraX = (double) (radius * Mth.sin((float) (Math.PI + angle))) + this.getX();
			double extraZ = (double) (radius * Mth.cos(angle)) + this.getZ();
			double extraY = 0.75F + this.getY();
			this.getLevel().addParticle(ParticleTypes.END_ROD, extraX + (double) (this.getRandom().nextFloat() * 0.5F) - (double) 0.25F,
					extraY,
					extraZ + (double) (this.getRandom().nextFloat() * 0.5F) - (double) 0.25F,
					0F, -0.15F, 0F);
		}
		if (this.useRangedAttack && this.getAnimation() == ANIMATION_RANGED && this.getAnimationTick() == 6) {
			float radius = -3.8F;
			float angle = (0.01745329251F * (this.yBodyRot)) - 160F;
			double extraX = (double) (radius * Mth.sin((float) (Math.PI + angle))) + getX();
			double extraZ = (double) (radius * Mth.cos(angle)) + getZ();
			double extraY = 2.4F + getY();
			double targetRelativeX = (this.getTarget() == null ? this.getViewVector(1.0F).x : this.getTarget().getX()) - extraX;
			double targetRelativeY = (this.getTarget() == null ? this.getViewVector(1.0F).y : this.getTarget().getY()) - extraY;
			double targetRelativeZ = (this.getTarget() == null ? this.getViewVector(1.0F).z : this.getTarget().getZ()) - extraZ;
			GolemBeam beam = new GolemBeam(RatlantisEntityRegistry.RATLANTEAN_AUTOMATON_BEAM.get(), this.getLevel(), this);
			beam.setPos(extraX, extraY, extraZ);
			beam.shoot(targetRelativeX, targetRelativeY, targetRelativeZ, 2.0F, 0.1F);
			this.playSound(RatsSoundRegistry.LASER.get(), 1.0F, 0.75F + this.getRandom().nextFloat() * 0.5F);
			if (!this.getLevel().isClientSide()) {
				this.getLevel().addFreshEntity(beam);
			}
			this.useRangedAttack = this.getRandom().nextBoolean();
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

	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		if (this.hasCustomName()) {
			this.bossInfo.setName(this.getDisplayName());
		}
	}

	public void setCustomName(@Nullable Component name) {
		super.setCustomName(name);
		this.bossInfo.setName(this.getDisplayName());
	}

	public void startSeenByPlayer(ServerPlayer player) {
		super.startSeenByPlayer(player);
		this.bossInfo.addPlayer(player);
	}

	public void stopSeenByPlayer(ServerPlayer player) {
		super.stopSeenByPlayer(player);
		this.bossInfo.removePlayer(player);
	}

	class AIFollowPrey extends Goal {
		private final RatlanteanAutomaton parentEntity;
		private double followDist;

		public AIFollowPrey(RatlanteanAutomaton ghast) {
			this.parentEntity = ghast;
		}

		public boolean canUse() {
			followDist = RatlanteanAutomaton.this.getBoundingBox().getSize();
			LivingEntity LivingEntity = this.parentEntity.getTarget();
			double maxFollow = this.parentEntity.useRangedAttack ? 5 * followDist : followDist;
			return LivingEntity != null && (LivingEntity.distanceTo(this.parentEntity) >= maxFollow || !this.parentEntity.hasLineOfSight(LivingEntity));
		}

		public void stop() {
		}

		public void tick() {
			LivingEntity LivingEntity = this.parentEntity.getTarget();
			double maxFollow = this.parentEntity.useRangedAttack ? 5 * followDist : followDist;
			if (LivingEntity.distanceTo(this.parentEntity) >= maxFollow || !this.parentEntity.hasLineOfSight(LivingEntity)) {
				RatlanteanAutomaton.this.getMoveControl().setWantedPosition(LivingEntity.getX(), LivingEntity.getY() + 1, LivingEntity.getZ(), 1D);
			}
		}
	}
}
