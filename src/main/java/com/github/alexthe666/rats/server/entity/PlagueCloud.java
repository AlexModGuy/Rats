package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.registry.RatsEffectRegistry;
import com.github.alexthe666.rats.registry.RatsParticleRegistry;
import com.github.alexthe666.rats.registry.RatsSoundRegistry;
import com.github.alexthe666.rats.server.events.ForgeEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class PlagueCloud extends Monster implements PlagueLegion {

	protected static final EntityDataAccessor<Optional<UUID>> OWNER_UNIQUE_ID = SynchedEntityData.defineId(PlagueCloud.class, EntityDataSerializers.OPTIONAL_UUID);

	public PlagueCloud(EntityType<? extends Monster> type, Level level) {
		super(type, level);
		this.moveControl = new PlagueCloud.AIMoveControl(this);
	}

	public boolean canBeAffected(MobEffectInstance potioneffectIn) {
		if (potioneffectIn.getEffect() == RatsEffectRegistry.PLAGUE.get()) {
			return false;
		}
		return super.canBeAffected(potioneffectIn);
	}

	protected SoundEvent getHurtSound(DamageSource source) {
		return RatsSoundRegistry.PLAGUE_CLOUD_HURT.get();
	}

	protected SoundEvent getDeathSound() {
		return RatsSoundRegistry.PLAGUE_CLOUD_DEATH.get();
	}

	public void aiStep() {
		super.aiStep();
		this.setNoGravity(true);
		double d0 = this.hurtTime > 0 ? 1 : 0;
		double d1 = 0.01D;
		double d2 = 0D;
		double x = this.getX() + (double) (this.getRandom().nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth();
		double y = this.getY() + (double) (this.getRandom().nextFloat() * this.getBbHeight()) - (double) this.getBbHeight();
		double z = this.getZ() + (double) (this.getRandom().nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth();
		float f = (this.getBbWidth() + this.getBbHeight() + this.getBbWidth()) * 0.333F + 0.5F;
		if (this.particleDistSq(x, y, z) < f * f) {
			if (this.getRandom().nextBoolean()) {
				this.getLevel().addParticle(RatsParticleRegistry.BLACK_DEATH.get(), x, y + 1.5F, z, d0, d1, d2);
			} else {
				this.getLevel().addParticle(ParticleTypes.ENTITY_EFFECT, x, y + 1.5F, z, d0, d1, d2);

			}
		}
		if (this.getOwnerId() != null && this.getOwner() != null && this.getOwner() instanceof BlackDeath death) {
			if (death.getTarget() != null && death.getTarget().isAlive()) {
				this.setTarget(death.getTarget());
			} else {
				float radius = (float) 9 - (float) Math.sin(death.tickCount * 0.4D) * 0.25F;
				int maxRatStuff = 360 / Math.max(death.getCloudsSummoned(), 1);
				int ratIndex = this.getId() % Math.max(death.getCloudsSummoned(), 1);
				float angle = (0.01745329251F * (ratIndex * maxRatStuff + tickCount * 4.1F));
				double extraX = (double) (radius * Mth.sin((float) (Math.PI + angle))) + death.getX();
				double extraZ = (double) (radius * Mth.cos(angle)) + death.getZ();
				this.getMoveControl().setWantedPosition(extraX, death.getY() + 2 + getRandom().nextInt(2), extraZ, 1.0F);
			}
		}
		if (this.getTarget() != null && !this.getTarget().isAlive()) {
			this.setTarget(null);
		}
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 10.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.25D)
				.add(Attributes.ATTACK_DAMAGE, 3.0D)
				.add(Attributes.FOLLOW_RANGE, 24.0D);
	}

	public void remove(RemovalReason reason) {
		if (!this.isAlive() && this.getOwnerId() != null && this.getOwner() != null && this.getOwner() instanceof BlackDeath death && RatConfig.bdConstantCloudSpawns) {
			death.setCloudsSummoned(death.getCloudsSummoned() - 1);
		}
		super.remove(reason);
	}

	public double particleDistSq(double toX, double toY, double toZ) {
		double d0 = this.getX() - toX;
		double d1 = this.getY() - toY;
		double d2 = this.getZ() - toZ;
		return d0 * d0 + d1 * d1 + d2 * d2;
	}

	protected void defineSynchedData() {
		super.defineSynchedData();
		this.getEntityData().define(OWNER_UNIQUE_ID, Optional.empty());
	}

	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new PlagueCloud.AIMeleeAttack(this));
		this.goalSelector.addGoal(8, new PlagueCloud.AIMoveRandom());
		this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
		this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, LivingEntity.class, 8.0F));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, false));
	}

	public boolean doHurtTarget(Entity entity) {
		boolean flag = super.doHurtTarget(entity);
		if (flag && entity instanceof LivingEntity living) {
			ForgeEvents.maybeAddAndSyncPlague(null, living, 600, 0);
		}
		return flag;
	}

	@Override
	protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
	}

	@Override
	public boolean causeFallDamage(float dist, float mult, DamageSource source) {
		return false;
	}

	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		if (this.getOwnerId() == null) {
			compound.putString("OwnerUUID", "");
		} else {
			compound.putString("OwnerUUID", this.getOwnerId().toString());
		}
	}

	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		UUID uuid;
		if (compound.hasUUID("Owner")) {
			uuid = compound.getUUID("Owner");
		} else {
			String s = compound.getString("Owner");
			uuid = OldUsersConverter.convertMobOwnerIfNecessary(this.getServer(), s);
		}

		if (uuid != null) {
			try {
				this.setOwnerId(uuid);
			} catch (Throwable ignored) {
			}
		}
	}

	@Nullable
	public UUID getOwnerId() {
		return this.getEntityData().get(OWNER_UNIQUE_ID).orElse(null);
	}

	public void setOwnerId(@Nullable UUID uuid) {
		this.getEntityData().set(OWNER_UNIQUE_ID, Optional.ofNullable(uuid));
	}

	@Nullable
	public LivingEntity getOwner() {
		try {
			UUID uuid = this.getOwnerId();
			LivingEntity player = uuid == null ? null : this.getLevel().getPlayerByUUID(uuid);
			if (player != null) {
				return player;
			} else {
				if (!this.getLevel().isClientSide()) {
					Entity entity = this.getLevel().getServer().getLevel(this.getLevel().dimension()).getEntity(uuid);
					if (entity instanceof LivingEntity) {
						return (LivingEntity) entity;
					}
				}
			}
		} catch (IllegalArgumentException var2) {
			return null;
		}
		return null;
	}

	class AIMoveControl extends MoveControl {
		public AIMoveControl(PlagueCloud cloud) {
			super(cloud);
		}

		public void tick() {
			if (this.operation == MoveControl.Operation.MOVE_TO) {
				Vec3 vec3d = new Vec3(this.getWantedX() - PlagueCloud.this.getX(), this.getWantedY() - PlagueCloud.this.getY(), this.getWantedZ() - PlagueCloud.this.getZ());
				double d0 = vec3d.length();
				double edgeLength = PlagueCloud.this.getBoundingBox().getSize();
				if (d0 < edgeLength) {
					this.operation = MoveControl.Operation.WAIT;
					PlagueCloud.this.setDeltaMovement(PlagueCloud.this.getDeltaMovement().scale(0.5D));
				} else {
					PlagueCloud.this.setDeltaMovement(PlagueCloud.this.getDeltaMovement().add(vec3d.scale(this.speedModifier * 0.1D / d0)));
					if (PlagueCloud.this.getTarget() == null) {
						Vec3 vec3d1 = PlagueCloud.this.getDeltaMovement();
						PlagueCloud.this.setYRot(-((float) Mth.atan2(vec3d1.x, vec3d1.z)) * (180F / (float) Math.PI));
					} else {
						double d4 = PlagueCloud.this.getTarget().getX() - PlagueCloud.this.getX();
						double d5 = PlagueCloud.this.getTarget().getZ() - PlagueCloud.this.getZ();
						PlagueCloud.this.setYRot(-((float) Mth.atan2(d4, d5)) * (180F / (float) Math.PI));
					}
					PlagueCloud.this.yBodyRot = PlagueCloud.this.getYRot();
				}
			}
		}
	}

	class AIMoveRandom extends Goal {

		public AIMoveRandom() {
			this.setFlags(EnumSet.of(Goal.Flag.MOVE));
		}

		public boolean canUse() {
			return !PlagueCloud.this.getMoveControl().hasWanted() && PlagueCloud.this.getRandom().nextInt(2) == 0;
		}

		public boolean canContinueToUse() {
			return false;
		}

		public void tick() {
			BlockPos blockpos =PlagueCloud.this.blockPosition();

			for (int i = 0; i < 3; ++i) {
				BlockPos blockpos1 = blockpos.offset(PlagueCloud.this.getRandom().nextInt(15) - 7, PlagueCloud.this.getRandom().nextInt(11) - 5, PlagueCloud.this.getRandom().nextInt(15) - 7);

				if (PlagueCloud.this.getLevel().isEmptyBlock(blockpos1)) {
					PlagueCloud.this.getMoveControl().setWantedPosition((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.5D, (double) blockpos1.getZ() + 0.5D, 0.25D);

					if (PlagueCloud.this.getTarget() == null) {
						PlagueCloud.this.getLookControl().setLookAt((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.5D, (double) blockpos1.getZ() + 0.5D, 180.0F, 20.0F);
					}

					break;
				}
			}
		}
	}

	class AIMeleeAttack extends Goal {
		private final PlagueCloud parentEntity;
		public int attackTimer;

		public AIMeleeAttack(PlagueCloud ghast) {
			this.parentEntity = ghast;
		}

		public boolean canUse() {
			return this.parentEntity.getTarget() != null;
		}

		public void start() {
			this.attackTimer = 0;
		}

		public void stop() {
		}

		public void tick() {
			LivingEntity living = this.parentEntity.getTarget();
			if (living != null && living.distanceToSqr(this.parentEntity) >= 2.0D || !this.parentEntity.hasLineOfSight(living)) {
				PlagueCloud.this.getMoveControl().setWantedPosition(living.getX(), living.getY() + 1.0D, living.getZ(), 0.5D);
			}
			if (living.distanceToSqr(this.parentEntity) < 5.0D) {
				++this.attackTimer;
				if (this.attackTimer == 5) {
					this.parentEntity.doHurtTarget(living);
					this.attackTimer = -10;
				}
			} else if (this.attackTimer > 0) {
				--this.attackTimer;
			}
		}
	}

	public boolean isAlliedTo(Entity entity) {
		return super.isAlliedTo(entity) || entity instanceof PlagueLegion;
	}
}
