package com.github.alexthe666.rats.server.entity.monster.boss;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.registry.RatlantisEntityRegistry;
import com.github.alexthe666.rats.registry.RatsSoundRegistry;
import com.github.alexthe666.rats.server.entity.AdjustsRatTail;
import com.github.alexthe666.rats.server.entity.Plane;
import com.github.alexthe666.rats.server.entity.ai.navigation.control.PlaneMoveControl;
import com.github.alexthe666.rats.server.entity.projectile.RattlingGunBullet;
import com.github.alexthe666.rats.server.entity.rat.AbstractRat;
import com.github.alexthe666.rats.server.misc.PlaneRotationUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.ListTag;
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

public class RatBaronPlane extends Mob implements Plane, AdjustsRatTail {

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

	public RatBaronPlane(EntityType<? extends Mob> type, Level level) {
		super(type, level);
		this.moveControl = new PlaneMoveControl<>(this);
		this.xpReward = 55;
		this.noPhysics = true;
		if (level.isClientSide()) {
			this.roll_buffer = new PlaneRotationUtil();
			this.pitch_buffer = new PlaneRotationUtil();
		}
	}

	public float getPlanePitch() {
		return this.getEntityData().get(PLANE_PITCH);
	}

	public void setPlanePitch(float pitch) {
		this.getEntityData().set(PLANE_PITCH, pitch);
	}

	public void incrementPlanePitch(float pitch) {
		this.getEntityData().set(PLANE_PITCH, this.getPlanePitch() + pitch);
	}

	public void decrementPlanePitch(float pitch) {
		this.getEntityData().set(PLANE_PITCH, this.getPlanePitch() - pitch);
	}

	public boolean isAlliedTo(Entity entity) {
		return super.isAlliedTo(entity) || entity instanceof RatBaron;
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
		if (!this.isNoAi() && !this.isVehicle() && !this.level().isClientSide()) {
			this.hurt(this.damageSources().magic(), Float.MAX_VALUE);
		}
		if (!this.onGround() && this.getDeltaMovement().y() < 0.0D) {
			this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, 0.6D, 1.0D));
		}
		this.setDeltaMovement(this.getDeltaMovement().x(), this.getDeltaMovement().y() + 0.08D, this.getDeltaMovement().z());
		if (this.getTarget() != null && this.startPreyVec != null && this.startAttackVec != null) {
			float distX = (float) (this.startPreyVec.x() - this.startAttackVec.x());
			float distY = 1.5F;
			float distZ = (float) (this.startPreyVec.z() - this.startAttackVec.z());
			this.setFlightTarget(new Vec3(this.getTarget().getX() + distX, this.level().getHeightmapPos(Heightmap.Types.WORLD_SURFACE, this.getRestrictCenter()).getY() + 5 + this.getRandom().nextInt(16) + distY, this.getTarget().getZ() + distZ));
			if (this.distanceToSqr(this.getFlightTarget().x(), this.getFlightTarget().y(), this.getFlightTarget().z()) < 100) {
				this.setFlightTarget(new Vec3(this.getTarget().getX() - distX, this.level().getHeightmapPos(Heightmap.Types.WORLD_SURFACE, this.getRestrictCenter()).getY() + 5 + this.getRandom().nextInt(16) + distY, this.getTarget().getZ() - distZ));
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
					RattlingGunBullet bullet = new RattlingGunBullet(RatlantisEntityRegistry.RATTLING_GUN_BULLET.get(), this.level(), this);
					bullet.setPos(extraX, extraY, extraZ);
					bullet.setBaseDamage(RatConfig.ratBaronBulletDamage);

					double d0 = target.getX() - this.getX();
					double d1 = target.getY(0.3333333333333333D) - bullet.getY();
					double d2 = target.getZ() - this.getZ();
					double d3 = Math.sqrt(d0 * d0 + d2 * d2);
					bullet.shoot(d0, d1 + d3 * (double)0.2F, d2, 3.2F, 1.4F);
					this.playSound(RatsSoundRegistry.BIPLANE_SHOOT.get(), 3.0F, 2.3F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
					if (!this.level().isClientSide()) {
						this.level().addFreshEntity(bullet);
					}
				}
			}
			this.setFiring(true);
		}
		if (this.getTarget() == null || this.getFlightTarget() == null || this.distanceToSqr(this.getFlightTarget().x(), this.getFlightTarget().y(), this.getFlightTarget().z()) < 9 || !this.level().isEmptyBlock(BlockPos.containing(this.getFlightTarget()))) {
			if (this.escortPosition == null) {
				this.escortPosition = this.level().getHeightmapPos(Heightmap.Types.WORLD_SURFACE, this.getRestrictCenter()).above(RatConfig.ratBaronYFlight + this.getRandom().nextInt(10));
			}
			this.setFlightTarget(this.getBlockInViewEscort());
		}
		if (this.level().isClientSide()) {
			if (!this.onGround()) {
				this.roll_buffer.calculateChainFlapBuffer(40, 20, 0.5F, 0.5F, this);
				this.pitch_buffer.calculateChainWaveBuffer(40, 10, 0.5F, 0.5F, this);
			}
		}
		if (!this.onGround()) {
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
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		if (this.getRestrictCenter() != BlockPos.ZERO) {
			BlockPos home = this.getRestrictCenter();
			tag.put("Home", this.makeDoubleList(home.getX(), home.getY(), home.getZ()));
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		if (tag.contains("Home", 9)) {
			ListTag nbttaglist = tag.getList("Home", 6);
			int hx = (int) nbttaglist.getDouble(0);
			int hy = (int) nbttaglist.getDouble(1);
			int hz = (int) nbttaglist.getDouble(2);
			this.restrictTo(new BlockPos(hx, hy, hz), 16);
		}
	}

	private ListTag makeDoubleList(double... pNumbers) {
		ListTag listtag = new ListTag();
		for (double d0 : pNumbers) {
			listtag.add(DoubleTag.valueOf(d0));
		}

		return listtag;
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
		if (this.getControllingPassenger() != null) {
			return this.getControllingPassenger().hurt(source, amount);
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

	public void positionRider(Entity passenger, Entity.MoveFunction callback) {
		super.positionRider(passenger, callback);
		float radius = 0.35F;
		float angle = (0.01745329251F * this.yBodyRot);
		double extraX = radius * Mth.sin((float) (Math.PI + angle));
		double extraZ = radius * Mth.cos(angle);
		double extraY = 1.35F;
		callback.accept(passenger, this.getX() + extraX, this.getY() + extraY, this.getZ() + extraZ);
		if (passenger instanceof LivingEntity living) {
			living.yBodyRot = this.yBodyRot;
			passenger.setYRot(this.yBodyRot);
			living.yHeadRot = this.yBodyRot;
		}
	}

	@Nullable
	public Vec3 getBlockInViewEscort() {
		BlockPos escortPos = this.getEscortPosition();
		BlockPos ground = this.level().getHeightmapPos(Heightmap.Types.WORLD_SURFACE, escortPos);
		int distFromGround = escortPos.getY() - ground.getY();
		int fromHome = 32;
		for (int i = 0; i < 10; i++) {
			BlockPos pos = new BlockPos(escortPos.getX() + this.getRandom().nextInt(fromHome) - fromHome / 2,
					(distFromGround > 16 ? ground.getY() + 16 : escortPos.getY() + 10 + this.getRandom().nextInt(16)),
					(escortPos.getZ() + this.getRandom().nextInt(fromHome) - fromHome / 2));
			if (this.canBlockPosBeSeen(pos) && this.distanceToSqr(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) > 6) {
				return new Vec3(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
			}
		}
		return null;
	}

	public boolean canBlockPosBeSeen(BlockPos pos) {
		Vec3 vec3d = new Vec3(this.getX(), this.getEyeY(), this.getZ());
		Vec3 vec3d1 = new Vec3(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
		return this.level().clip(new ClipContext(vec3d, vec3d1, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() == HitResult.Type.MISS;
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

	@Override
	public void adjustRatTailRotation(AbstractRat rat, AdvancedModelBox upperTail, AdvancedModelBox lowerTail) {
		this.progressRotation(upperTail, rat.sitProgress, 1.3F, 0.0F, 0.0F, 20.0F);
		this.progressRotation(lowerTail, rat.sitProgress, -0.2F, 0.0F, 0.0F, 20.0F);
	}
}
