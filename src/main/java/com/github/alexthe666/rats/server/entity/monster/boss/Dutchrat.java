package com.github.alexthe666.rats.server.entity.monster.boss;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.registry.RatlantisBlockRegistry;
import com.github.alexthe666.rats.registry.RatlantisEntityRegistry;
import com.github.alexthe666.rats.registry.RatlantisItemRegistry;
import com.github.alexthe666.rats.registry.RatsSoundRegistry;
import com.github.alexthe666.rats.server.entity.projectile.DutchratSword;
import com.github.alexthe666.rats.server.entity.Pirats;
import com.github.alexthe666.rats.server.entity.Ratlanteans;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class Dutchrat extends Monster implements PowerableMob, IAnimatedEntity, Ratlanteans, Pirats {

	private int animationTick;
	private boolean useRangedAttack = false;
	private int ticksSinceThrownSword = 0;
	private Animation currentAnimation;
	public static final Animation ANIMATION_SLASH = Animation.create(25);
	public static final Animation ANIMATION_STAB = Animation.create(17);
	public static final Animation ANIMATION_THROW = Animation.create(15);
	public static final Animation ANIMATION_SPEAK = Animation.create(10);
	private static final EntityDataAccessor<Boolean> THROWN_SWORD = SynchedEntityData.defineId(Dutchrat.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Integer> BELL_SPAWN_TICKS = SynchedEntityData.defineId(Dutchrat.class, EntityDataSerializers.INT);
	private final ServerBossEvent bossInfo = new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.GREEN, BossEvent.BossBarOverlay.PROGRESS);

	public Dutchrat(EntityType<? extends Monster> type, Level level) {
		super(type, level);
		this.xpReward = 60;
		this.moveControl = new Dutchrat.DutchratMoveControl(this);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 300.0F)
				.add(Attributes.MOVEMENT_SPEED, 0.4D)
				.add(Attributes.ATTACK_DAMAGE, 8.0F)
				.add(Attributes.FOLLOW_RANGE, 32.0D)
				.add(Attributes.ARMOR, 10.0D);
	}

	protected void customServerAiStep() {
		if (this.getBellSummonTicks() > 0) {
			if (!this.getLevel().getBlockState(this.getRestrictCenter()).is(RatlantisBlockRegistry.DUTCHRAT_BELL.get())) {
				this.discard();
			}
			int k1 = this.getBellSummonTicks() - 1;
			this.bossInfo.setProgress(1.0F - (float) k1 / 160.0F);
			this.getEntityData().set(BELL_SPAWN_TICKS, k1);
		} else {
			super.customServerAiStep();
			this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
		}
	}

	protected void registerGoals() {
		this.goalSelector.addGoal(1, new Dutchrat.SummonedByBellGoal());
		this.goalSelector.addGoal(1, new Dutchrat.FollowTargetGoal(this));
		this.goalSelector.addGoal(2, new Dutchrat.RandomlyMoveGoal());
		this.goalSelector.addGoal(4, new MoveTowardsRestrictionGoal(this, 1.25D));
		this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, false, false) {
			@Override
			public boolean canContinueToUse() {
				return super.canContinueToUse() && Dutchrat.this.getTarget() != null && Dutchrat.this.isWithinRestriction(Dutchrat.this.getTarget().blockPosition());
			}
		});
	}


	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.getEntityData().define(THROWN_SWORD, false);
		this.getEntityData().define(BELL_SPAWN_TICKS, 0);
	}

	public void setThrownSword(boolean sword) {
		this.getEntityData().set(THROWN_SWORD, sword);
	}

	public boolean hasThrownSword() {
		return this.getEntityData().get(THROWN_SWORD);
	}

	public void setBellSummoned() {
		this.getEntityData().set(BELL_SPAWN_TICKS, 160);
	}

	public int getBellSummonTicks() {
		return this.getEntityData().get(BELL_SPAWN_TICKS);
	}

	@Override
	public boolean canChangeDimensions() {
		return false;
	}

	@Override
	protected boolean canRide(Entity entity) {
		return false;
	}

	@Override
	public boolean isNoGravity() {
		return true;
	}

	@Override
	public void aiStep() {
		super.aiStep();
		this.noPhysics = true;
		if (this.getAnimation() == ANIMATION_THROW && this.getAnimationTick() > 7) {
			if (!this.hasThrownSword()) {
				float radius = -1.5F;
				float angle = (0.01745329251F * (this.yBodyRot)) - 230F;
				double extraX = (double) (radius * Mth.sin((float) (Math.PI + angle))) + getX();
				double extraZ = (double) (radius * Mth.cos(angle)) + getZ();
				double extraY = 1.7F + getY();
				DutchratSword sword = new DutchratSword(RatlantisEntityRegistry.DUTCHRAT_SWORD.get(), this.getLevel(), extraX, extraY, extraZ, this);
				this.getLevel().addFreshEntity(sword);
				this.useRangedAttack = this.getRandom().nextInt(RatConfig.dutchratSwordThrowChance) == 0;
			}
			this.setThrownSword(true);
		}
		if (!this.getLevel().isClientSide()) {
			if (this.hasThrownSword()) {
				this.ticksSinceThrownSword++;
			}
			if (this.ticksSinceThrownSword > 60) {
				this.setThrownSword(false);
				this.ticksSinceThrownSword = 0;
			}
		}
		if (this.useRangedAttack && this.getAnimation() != ANIMATION_THROW && !this.hasThrownSword() && this.getTarget() != null && this.hasLineOfSight(this.getTarget())) {
			this.setAnimation(ANIMATION_THROW);
			this.lookAt(this.getTarget(), 360, 80);
		}
		if (!this.useRangedAttack && this.getTarget() != null && !this.hasThrownSword() && this.distanceTo(this.getTarget()) < 7 && this.hasLineOfSight(this.getTarget())) {
			if (this.getAnimation() == NO_ANIMATION) {
				this.setAnimation(this.getRandom().nextBoolean() ? ANIMATION_SLASH : ANIMATION_STAB);
			}
			this.lookAt(this.getTarget(), 360, 80);
			if (this.getAnimation() == ANIMATION_SLASH && (this.getAnimationTick() == 10 || this.getAnimationTick() == 20)) {
				this.getTarget().hurt(this.damageSources().mobAttack(this), (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE));
				this.getTarget().knockback(1.5F, this.getX() - this.getTarget().getX(), this.getZ() - this.getTarget().getZ());
				this.useRangedAttack = this.getRandom().nextInt(RatConfig.dutchratSwordThrowChance) == 0;
			}
			if (this.getAnimation() == ANIMATION_STAB && this.getAnimationTick() == 10) {
				this.getTarget().hurt(this.damageSources().mobAttack(this), (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE));
				this.getTarget().knockback(1.5F, this.getTarget().getX() - this.getX(), this.getTarget().getZ() - this.getZ());
				this.useRangedAttack = this.getRandom().nextInt(RatConfig.dutchratSwordThrowChance) == 0;
			}
		}
		AnimationHandler.INSTANCE.updateAnimations(this);
	}

	@Override
	public boolean removeWhenFarAway(double dist) {
		return false;
	}

	@Override
	public void checkDespawn() {
		if (this.getLevel().getDifficulty() == Difficulty.PEACEFUL && this.getBellSummonTicks() <= 0) {
			if (this.hasRestriction()) {
				this.getLevel().setBlockAndUpdate(this.getRestrictCenter(), RatlantisBlockRegistry.DUTCHRAT_BELL.get().defaultBlockState());
			}
			this.discard();
		} else {
			super.checkDespawn();
		}
	}

	@Nullable
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficulty, MobSpawnType type, @Nullable SpawnGroupData data, @Nullable CompoundTag tag) {
		data = super.finalizeSpawn(accessor, difficulty, type, data, tag);
		this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(RatlantisItemRegistry.GHOST_PIRAT_CUTLASS.get()));
		this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(RatlantisItemRegistry.GHOST_PIRAT_HAT.get()));
		return data;
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (this.getBellSummonTicks() > 0 && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
			return false;
		} else {
			return super.hurt(source, amount);
		}
	}

	@Override
	public int getAnimationTick() {
		return this.animationTick;
	}

	@Override
	public void setAnimationTick(int tick) {
		this.animationTick = tick;
	}

	@Override
	public Animation getAnimation() {
		return this.currentAnimation;
	}

	@Override
	public void setAnimation(Animation animation) {
		this.currentAnimation = animation;
	}

	@Override
	public Animation[] getAnimations() {
		return new Animation[]{ANIMATION_SLASH, ANIMATION_STAB, ANIMATION_THROW, ANIMATION_SPEAK};
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		if (this.getRestrictCenter() != BlockPos.ZERO) {
			BlockPos home = this.getRestrictCenter();
			tag.put("Home", this.makeDoubleList(home.getX(), home.getY(), home.getZ()));
		}
		tag.putInt("Invul", this.getBellSummonTicks());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		if (this.hasCustomName()) {
			this.bossInfo.setName(this.getDisplayName());
		}
		if (tag.contains("Home", 9)) {
			ListTag nbttaglist = tag.getList("Home", 6);
			int hx = (int) nbttaglist.getDouble(0);
			int hy = (int) nbttaglist.getDouble(1);
			int hz = (int) nbttaglist.getDouble(2);
			this.restrictTo(new BlockPos(hx, hy, hz), RatConfig.dutchratRestrictionRadius);
		}
		this.getEntityData().set(BELL_SPAWN_TICKS, tag.getInt("Invul"));
	}

	private ListTag makeDoubleList(double... pNumbers) {
		ListTag listtag = new ListTag();
		for (double d0 : pNumbers) {
			listtag.add(DoubleTag.valueOf(d0));
		}

		return listtag;
	}

	@Override
	public void setCustomName(@Nullable Component name) {
		super.setCustomName(name);
		this.bossInfo.setName(this.getDisplayName());
	}

	@Override
	public void startSeenByPlayer(ServerPlayer player) {
		super.startSeenByPlayer(player);
		this.bossInfo.addPlayer(player);
	}

	@Override
	public void stopSeenByPlayer(ServerPlayer player) {
		super.stopSeenByPlayer(player);
		this.bossInfo.removePlayer(player);
	}

	@Override
	public void playAmbientSound() {
		if (!this.getLevel().isClientSide()) {
			if (this.getAnimation() == NO_ANIMATION) {
				this.setAnimation(ANIMATION_SPEAK);
			}
			super.playAmbientSound();
		}
	}

	@Override
	protected void playHurtSound(DamageSource source) {
		if (this.getAnimation() == NO_ANIMATION && !this.getLevel().isClientSide()) {
			this.setAnimation(ANIMATION_SPEAK);
		}
		super.playHurtSound(source);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return this.getBellSummonTicks() > 0 ? null : RatsSoundRegistry.DUTCHRAT_IDLE.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return RatsSoundRegistry.DUTCHRAT_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return RatsSoundRegistry.DUTCHRAT_DIE.get();
	}

	@Override
	public boolean isWithinRestriction(BlockPos pos) {
		if (this.getRestrictRadius() == -1) {
			return true;
		} else {
			int distX = Math.abs(this.getRestrictCenter().getX() - pos.getX());
			int distY = Math.abs(this.getRestrictCenter().getY() - pos.getY());
			int distZ = Math.abs(this.getRestrictCenter().getZ() - pos.getZ());

			return distX <= 20 && distY <= 10 && distZ <= 20;
		}
	}

	@Override
	public boolean isPowered() {
		return this.getBellSummonTicks() > 0;
	}

	class DutchratMoveControl extends MoveControl {
		public DutchratMoveControl(Dutchrat dutchrat) {
			super(dutchrat);
		}

		public void tick() {
			if (this.operation == MoveControl.Operation.MOVE_TO) {
				Vec3 vec3d = new Vec3(this.getWantedX() - Dutchrat.this.getX(), this.getWantedY() - Dutchrat.this.getY(), this.getWantedZ() - Dutchrat.this.getZ());
				double d0 = vec3d.length();
				double edgeLength = Dutchrat.this.getBoundingBox().getSize();
				if (d0 < edgeLength) {
					this.operation = MoveControl.Operation.WAIT;
					Dutchrat.this.setDeltaMovement(Dutchrat.this.getDeltaMovement().scale(0.5D));
				} else {
					Dutchrat.this.setDeltaMovement(Dutchrat.this.getDeltaMovement().add(vec3d.scale(this.speedModifier * 0.1D / d0)));
					if (Dutchrat.this.getTarget() == null) {
						Vec3 vec3d1 = Dutchrat.this.getDeltaMovement();
						Dutchrat.this.setYRot(-((float) Mth.atan2(vec3d1.x(), vec3d1.z())) * (180F / (float) Math.PI));
					} else {
						double d4 = Dutchrat.this.getTarget().getX() - Dutchrat.this.getX();
						double d5 = Dutchrat.this.getTarget().getZ() - Dutchrat.this.getZ();
						Dutchrat.this.setYRot(-((float) Mth.atan2(d4, d5)) * (180F / (float) Math.PI));
					}
					Dutchrat.this.yBodyRot = Dutchrat.this.getYRot();
				}
			}
		}
	}

	class SummonedByBellGoal extends Goal {
		public SummonedByBellGoal() {
			this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP, Goal.Flag.LOOK));
		}

		public boolean canUse() {
			return Dutchrat.this.getBellSummonTicks() > 0;
		}
	}

	class RandomlyMoveGoal extends Goal {

		public RandomlyMoveGoal() {
			this.setFlags(EnumSet.of(Goal.Flag.MOVE));
		}

		public boolean canUse() {
			return !Dutchrat.this.getMoveControl().hasWanted() && Dutchrat.this.getRandom().nextInt(2) == 0;
		}

		public boolean canContinueToUse() {
			return false;
		}

		public void tick() {
			BlockPos blockpos = Dutchrat.this.blockPosition();
			if (Dutchrat.this.hasRestriction()) {
				blockpos = Dutchrat.this.getRestrictCenter();
			}
			for (int i = 0; i < 3; ++i) {
				BlockPos blockpos1 = blockpos.offset(Dutchrat.this.getRandom().nextInt(15) - 7, Math.min(Dutchrat.this.getRandom().nextInt(10) - 5, 10), Dutchrat.this.getRandom().nextInt(15) - 7);

				if (Dutchrat.this.getLevel().isEmptyBlock(blockpos1)) {
					Dutchrat.this.getMoveControl().setWantedPosition((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.5D, (double) blockpos1.getZ() + 0.5D, 1D);

					if (Dutchrat.this.getTarget() == null) {
						Dutchrat.this.getLookControl().setLookAt((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.5D, (double) blockpos1.getZ() + 0.5D, 180.0F, 20.0F);
					}

					break;
				}
			}
		}
	}

	class FollowTargetGoal extends Goal {
		private final Dutchrat dutchrat;
		private double followDist;

		public FollowTargetGoal(Dutchrat dutchrat) {
			this.dutchrat = dutchrat;
		}

		public boolean canUse() {
			this.followDist = Dutchrat.this.getBoundingBox().getSize();
			LivingEntity living = this.dutchrat.getTarget();
			double maxFollow = this.dutchrat.useRangedAttack ? 5 * this.followDist : this.followDist;
			return living != null && Dutchrat.this.isWithinRestriction(living.blockPosition()) && (living.distanceTo(this.dutchrat) >= maxFollow || !this.dutchrat.hasLineOfSight(living));
		}

		public void tick() {
			LivingEntity living = this.dutchrat.getTarget();
			double maxFollow = this.dutchrat.useRangedAttack ? 5 * this.followDist : this.followDist;
			if (living != null && (living.distanceTo(this.dutchrat) >= maxFollow || !this.dutchrat.hasLineOfSight(living))) {
				if (Dutchrat.this.hasThrownSword()) {
					BlockPos blockpos = Dutchrat.this.blockPosition();
					if (Dutchrat.this.hasRestriction()) {
						blockpos = Dutchrat.this.getRestrictCenter();
					}
					BlockPos blockpos1 = blockpos.offset(Dutchrat.this.getRandom().nextInt(6) - 12, 0, Dutchrat.this.getRandom().nextInt(6) - 12);
					Dutchrat.this.getMoveControl().setWantedPosition(blockpos1.getX(), blockpos1.getY(), blockpos1.getZ(), 1.0D);
				} else {
					Dutchrat.this.getMoveControl().setWantedPosition(living.getX(), living.getY() + 4, living.getZ(), 1.0D);
				}
			}
		}
	}
}
