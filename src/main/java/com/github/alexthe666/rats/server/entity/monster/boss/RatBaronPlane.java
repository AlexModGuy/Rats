package com.github.alexthe666.rats.server.entity.monster.boss;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.registry.RatlantisEntityRegistry;
import com.github.alexthe666.rats.registry.RatsSoundRegistry;
import com.github.alexthe666.rats.server.entity.Plane;
import com.github.alexthe666.rats.server.entity.ai.navigation.control.PlaneMoveControl;
import com.github.alexthe666.rats.server.entity.projectile.RattlingGunBullet;
import com.github.alexthe666.rats.server.entity.rat.AbstractRat;
import com.github.alexthe666.rats.server.misc.PlaneRotationUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class RatBaronPlane extends Mob implements Plane {

	private static final EntityDataAccessor<Boolean> FIRING = SynchedEntityData.defineId(RatBaronPlane.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Float> PLANE_PITCH = SynchedEntityData.defineId(RatBaronPlane.class, EntityDataSerializers.FLOAT);
	public PlaneRotationUtil roll_buffer;
	public PlaneRotationUtil pitch_buffer;
	@Nullable
	private Vec3 flightTarget;
	private Vec3 startAttackVec;
	private Vec3 startPreyVec;
	private LivingEntity prevAttackTarget = null;
	private BlockPos escortPosition = null;
	private int soundLoopCounter = 0;
	public float prevPlanePitch;

	public RatBaronPlane(EntityType<? extends Mob> type, Level world) {
		super(type, world);
		this.moveControl = new PlaneMoveControl<>(this);
		this.xpReward = 55;
		if (world.isClientSide()) {
			this.roll_buffer = new PlaneRotationUtil();
			this.pitch_buffer = new PlaneRotationUtil();
		}
	}

	public float getPlanePitch() {
		return getEntityData().get(PLANE_PITCH);
	}

	public void setPlanePitch(float pitch) {
		getEntityData().set(PLANE_PITCH, pitch);
	}

	public void incrementPlanePitch(float pitch) {
		getEntityData().set(PLANE_PITCH, getPlanePitch() + pitch);
	}

	public void decrementPlanePitch(float pitch) {
		getEntityData().set(PLANE_PITCH, getPlanePitch() - pitch);
	}

	public boolean isAlliedTo(Entity entity) {
		return super.isAlliedTo(entity) || entity instanceof RatBaron;
	}

	protected void registerGoals() {
		super.registerGoals();
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
	}

	public void setTarget(@Nullable LivingEntity living) {
		super.setTarget(living);
		if (living != null) {
			if (this.prevAttackTarget != living) {
				this.startPreyVec = new Vec3(living.getX(), living.getY(), living.getZ());
				this.startAttackVec = new Vec3(this.getX(), this.getY(), this.getZ());
			}
			this.prevAttackTarget = living;
		}
	}

	public void tick() {
		super.tick();
		if (this.soundLoopCounter == 0) {
			this.playSound(RatsSoundRegistry.BIPLANE_LOOP.get(), 10, 1);
		}
		this.soundLoopCounter++;
		if (this.soundLoopCounter > 90) {
			this.soundLoopCounter = 0;
		}
		for (Entity passenger : this.getPassengers()) {
			if (passenger instanceof AbstractRat rat) {
				this.setTarget(rat.getTarget());
			}
		}
		this.prevPlanePitch = this.getPlanePitch();
		if (!this.isNoAi() && !this.isVehicle() && !this.getLevel().isClientSide()) {
			this.hurt(this.damageSources().magic(), Float.MAX_VALUE);
		}
		if (!this.isOnGround() && this.getDeltaMovement().y() < 0.0D) {
			this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, 0.6D, 1.0D));
		}
		this.setDeltaMovement(this.getDeltaMovement().x(), this.getDeltaMovement().y() + 0.08D, this.getDeltaMovement().z());
		if (this.getTarget() != null && this.startPreyVec != null && this.startAttackVec != null) {
			float distX = (float) (this.startPreyVec.x() - this.startAttackVec.x());
			float distY = 1.5F;
			float distZ = (float) (this.startPreyVec.z() - this.startAttackVec.z());
			this.setFlightTarget(new Vec3(this.getTarget().getX() + distX, this.getTarget().getY() + distY, this.getTarget().getZ() + distZ));
			if (this.distanceToSqr(this.getFlightTarget().x(), this.getFlightTarget().y(), this.getFlightTarget().z()) < 100) {
				this.setFlightTarget(new Vec3(this.getTarget().getX() - distX, this.getTarget().getY() + distY, this.getTarget().getZ() - distZ));
			}
		}
		if (this.getTarget() != null && this.hasLineOfSight(this.getTarget())) {
			Entity target = this.getTarget();
			if (this.tickCount % RatConfig.ratBaronShootFrequency == 0) {
				for (int i = 0; i < 2; i++) {
					boolean left = i == 0;
					float radius = 1.15F;
					float angle = (0.01745329251F * this.yBodyRot + (left ? 75 : -75));
					double extraX = this.getX() + radius * Mth.sin((float) (Math.PI + angle));
					double extraZ = this.getZ() + radius * Mth.cos(angle);
					double extraY = this.getY() + 1.35F;
					double d0 = target.getY() + (double) target.getEyeHeight() / 2;
					double d1 = target.getX() - extraX;
					double d3 = target.getZ() - extraZ;
					double d2 = d0 - extraY;
					float velocity = 3.2F;
					RattlingGunBullet bullet = new RattlingGunBullet(RatlantisEntityRegistry.RATTLING_GUN_BULLET.get(), this.getLevel(), this);
					bullet.setPos(extraX, extraY, extraZ);
					bullet.setBaseDamage(RatConfig.ratBaronBulletDamage);

					bullet.shoot(d1, d2, d3, velocity, 1.4F);
					this.playSound(RatsSoundRegistry.BIPLANE_SHOOT.get(), 3.0F, 2.3F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
					if (!this.getLevel().isClientSide()) {
						this.getLevel().addFreshEntity(bullet);
					}
				}
			}
			this.setFiring(true);
		}
		if (this.getTarget() == null || this.getFlightTarget() == null || this.distanceToSqr(this.getFlightTarget().x(), this.getFlightTarget().y(), this.getFlightTarget().z()) < 9 || !this.getLevel().isEmptyBlock(BlockPos.containing(this.getFlightTarget()))) {
			if (this.escortPosition == null) {
				this.escortPosition = this.getLevel().getHeightmapPos(Heightmap.Types.WORLD_SURFACE, this.blockPosition()).above(RatConfig.ratBaronYFlight + this.getRandom().nextInt(10));
			}
			this.setFlightTarget(this.getBlockInViewEscort());
		}
		if (this.getLevel().isClientSide()) {
			if (!this.onGround) {
				this.roll_buffer.calculateChainFlapBuffer(40, 20, 0.5F, 0.5F, this);
				this.pitch_buffer.calculateChainWaveBuffer(40, 10, 0.5F, 0.5F, this);
			}
		}
		if (!this.onGround) {
			double ydist = this.yo - this.getY();//down 0.4 up -0.38
			float planeDist = (float) ((Math.abs(this.getDeltaMovement().x()) + Math.abs(this.getDeltaMovement().z())) * 6F);
			this.incrementPlanePitch((float) (ydist) * 10);

			this.setPlanePitch(Mth.clamp(this.getPlanePitch(), -60, 40));
			float plateau = 2;
			if (this.getPlanePitch() > plateau) {
				//down
				//this.motionY -= 0.2D;
				this.decrementPlanePitch(planeDist * Math.abs(this.getPlanePitch()) / 90);
			}
			if (this.getPlanePitch() < -plateau) {//-2
				//up
				this.incrementPlanePitch(planeDist * Math.abs(this.getPlanePitch()) / 90);
			}
			if (this.getPlanePitch() > 2F) {
				this.decrementPlanePitch(1);
			} else if (this.getPlanePitch() < -2F) {
				this.incrementPlanePitch(1);
			}
		} else {
			this.setPlanePitch(0);
		}
	}

	@Override
	protected boolean shouldDespawnInPeaceful() {
		return true;
	}

	@Override
	public boolean removeWhenFarAway(double dist) {
		return false;
	}

	public boolean hurt(DamageSource source, float amount) {
		if (this.isVehicle() && !this.getPassengers().isEmpty()) {
			for (Entity passenger : this.getPassengers()) {
				passenger.hurt(source, amount);
			}
			return super.hurt(source, 0.0F);
		} else {
			return super.hurt(source, amount);
		}
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 10.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.35D)
				.add(Attributes.ATTACK_DAMAGE, 1.0D)
				.add(Attributes.FOLLOW_RANGE, 128.0D);
	}

	public void positionRider(Entity passenger) {
		super.positionRider(passenger);
		float radius = 0.35F;
		float angle = (0.01745329251F * this.yBodyRot);
		double extraX = radius * Mth.sin((float) (Math.PI + angle));
		double extraZ = radius * Mth.cos(angle);
		double extraY = 1.35F;
		passenger.setPos(this.getX() + extraX, this.getY() + extraY, this.getZ() + extraZ);
		if (passenger instanceof LivingEntity living) {
			living.yBodyRot = this.yBodyRot;
			passenger.setYRot(this.yBodyRot);
			living.yHeadRot = this.yBodyRot;
		}
	}

	@Nullable
	public Vec3 getBlockInViewEscort() {
		BlockPos escortPos = this.getEscortPosition();
		BlockPos ground = this.getLevel().getHeightmapPos(Heightmap.Types.WORLD_SURFACE, escortPos);
		int distFromGround = escortPos.getY() - ground.getY();
		int fromHome = 30;
		for (int i = 0; i < 10; i++) {
			BlockPos pos = new BlockPos(escortPos.getX() + this.getRandom().nextInt(fromHome) - fromHome / 2,
					(distFromGround > 16 ? escortPos.getY() : escortPos.getY() + 5 + this.getRandom().nextInt(26)),
					(escortPos.getZ() + this.getRandom().nextInt(fromHome) - fromHome / 2));
			if (canBlockPosBeSeen(pos) && this.distanceToSqr(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) > 6) {
				return new Vec3(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
			}
		}
		return null;
	}

	public boolean canBlockPosBeSeen(BlockPos pos) {
		Vec3 vec3d = new Vec3(this.getX(), this.getEyeY(), this.getZ());
		Vec3 vec3d1 = new Vec3(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
		return this.getLevel().clip(new ClipContext(vec3d, vec3d1, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() == HitResult.Type.MISS;
	}

	private BlockPos getEscortPosition() {
		return this.escortPosition;
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.getEntityData().define(FIRING, false);
		this.getEntityData().define(PLANE_PITCH, 0F);
	}

	public boolean isFiring() {
		return this.getEntityData().get(FIRING);
	}

	public void setFiring(boolean male) {
		this.getEntityData().set(FIRING, male);
	}

	@Override
	public @Nullable Vec3 getFlightTarget() {
		return this.flightTarget;
	}

	@Override
	public void setFlightTarget(@Nullable Vec3 target) {
		this.flightTarget = target;
	}

	@Nullable
	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return RatsSoundRegistry.BIPLANE_HURT.get();
	}

	@Nullable
	@Override
	protected SoundEvent getDeathSound() {
		return RatsSoundRegistry.BIPLANE_DEATH.get();
	}

	@Override
	public boolean canChangeDimensions() {
		return false;
	}

	@Override
	protected boolean canRide(Entity entity) {
		return false;
	}
}
