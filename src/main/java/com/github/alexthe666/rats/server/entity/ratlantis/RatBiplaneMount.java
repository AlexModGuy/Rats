package com.github.alexthe666.rats.server.entity.ratlantis;

import com.github.alexthe666.rats.registry.RatlantisEntityRegistry;
import com.github.alexthe666.rats.registry.RatlantisItemRegistry;
import com.github.alexthe666.rats.registry.RatsSoundRegistry;
import com.github.alexthe666.rats.server.entity.RatMountBase;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
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
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class RatBiplaneMount extends RatMountBase {

	public float prevPlanePitch;
	private static final EntityDataAccessor<Boolean> FIRING = SynchedEntityData.defineId(RatBiplaneMount.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Float> PLANE_PITCH = SynchedEntityData.defineId(RatBiplaneMount.class, EntityDataSerializers.FLOAT);
	public PlaneBuffer roll_buffer;
	public PlaneBuffer pitch_buffer;
	@Nullable
	private Vec3 flightTarget;
	private int soundLoopCounter = 0;

	public RatBiplaneMount(EntityType<? extends Mob> type, Level level) {
		super(type, level);
		this.riderY = 1.35F;
		this.riderXZ = -0.35F;
		this.moveControl = new FlightMoveHelper(this);
		if (this.getLevel().isClientSide()) {
			roll_buffer = new PlaneBuffer();
			pitch_buffer = new PlaneBuffer();
		}
	}

	@Override
	public float getStepHeight() {
		return 1.0F;
	}

	public void positionRider(Entity passenger) {
		super.positionRider(passenger);
		float radius = 0.35F;
		float angle = (0.01745329251F * this.yBodyRot);
		double extraX = radius * Mth.sin((float) (Math.PI + angle));
		double extraZ = radius * Mth.cos(angle);
		double extraY = 1.35F;
		passenger.setPos(this.getX() + extraX, this.getY() + extraY, this.getZ() + extraZ);
		if (passenger instanceof LivingEntity) {
			((LivingEntity) passenger).yBodyRot = this.yBodyRot;
			passenger.setYRot(this.yBodyRot);
			((LivingEntity) passenger).yHeadRot = this.yBodyRot;
		}
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.getEntityData().define(FIRING, false);
		this.getEntityData().define(PLANE_PITCH, 0F);
	}

	@Override
	protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {

	}

	@Override
	public boolean causeFallDamage(float dist, float mult, DamageSource source) {
		return false;
	}

	public boolean removeWhenFarAway(double dist) {
		return false;
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

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 300.0D)        //HEALTH
				.add(Attributes.MOVEMENT_SPEED, 0.35D)                //SPEED
				.add(Attributes.ATTACK_DAMAGE, 1.0D)       //ATTACK
				.add(Attributes.FOLLOW_RANGE, 128.0D);
	}

	public void tick() {
		super.tick();
		if (soundLoopCounter == 0) {
			this.playSound(RatsSoundRegistry.BIPLANE_LOOP.get(), 10, 1);
		}
		soundLoopCounter++;
		if (soundLoopCounter > 90) {
			soundLoopCounter = 0;
		}

		this.prevPlanePitch = this.getPlanePitch();
		if (!this.isVehicle() && !this.getLevel().isClientSide()) {
			this.hurt(this.damageSources().drown(), 1000);
		}
		if (!this.isOnGround() && this.getDeltaMovement().y < 0.0D) {
			this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, 0.6D, 1.0D));
		}
		TamedRat rat = this.getRat();
		double up = 0.08D;
		if (rat != null && !rat.canMove()) {
			up = 0;
		}
		this.setDeltaMovement(this.getDeltaMovement().x, this.getDeltaMovement().y + up, this.getDeltaMovement().z);

		if (this.getTarget() != null && this.hasLineOfSight(this.getTarget())) {
			Entity target = this.getTarget();
			if (this.tickCount % 2 == 0) {
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
					RattlingGunBullet cannonball = new RattlingGunBullet(RatlantisEntityRegistry.RATTLING_GUN_BULLET.get(), this.getLevel(), this);
					cannonball.setPos(extraX, extraY, extraZ);
					cannonball.setBaseDamage(0.5F);
					cannonball.shoot(d1, d2, d3, velocity, 1.4F);
					this.playSound(RatsSoundRegistry.BIPLANE_SHOOT.get(), 3.0F, 2.3F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
					if (!this.getLevel().isClientSide()) {
						this.getLevel().addFreshEntity(cannonball);
					}
				}
			}
			this.setFiring(true);
		}

		if (this.flightTarget == null || this.distanceToSqr(flightTarget.x, flightTarget.y, flightTarget.z) < 20 || rat != null && !rat.canMove()) {
			flightTarget = null;
		}
		if (this.getLevel().isClientSide()) {
			if (!onGround) {
				roll_buffer.calculateChainFlapBuffer(40, 20, 0.5F, 0.5F, this);
				pitch_buffer.calculateChainWaveBuffer(40, 10, 0.5F, 0.5F, this);
			}
		}
		if (!onGround) {
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


	public boolean isFiring() {
		return this.getEntityData().get(FIRING);
	}

	public void setFiring(boolean firing) {
		this.getEntityData().set(FIRING, firing);
	}


	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return RatsSoundRegistry.BIPLANE_HURT.get();
	}

	protected SoundEvent getDeathSound() {
		return RatsSoundRegistry.BIPLANE_DEATH.get();
	}

	@Override
	public Item getUpgradeItem() {
		return RatlantisItemRegistry.RAT_UPGRADE_BIPLANE_MOUNT.get();
	}

	protected static class FlightMoveHelper extends MoveControl {

		private final RatBiplaneMount plane;

		protected FlightMoveHelper(RatBiplaneMount planeBase) {
			super(planeBase);
			this.plane = planeBase;
		}

		public static float approach(float number, float max, float min) {
			min = Math.abs(min);
			return number < max ? Mth.clamp(number + min, number, max) : Mth.clamp(number - min, max, number);
		}

		public static float approachDegrees(float number, float max, float min) {
			float add = Mth.wrapDegrees(max - number);
			return approach(number, number + add, min);
		}

		public static float degreesDifferenceAbs(float f1, float f2) {
			return Math.abs(Mth.wrapDegrees(f2 - f1));
		}

		public void tick() {
			if (plane.horizontalCollision) {
				plane.setYRot(plane.getYRot() + 180.0F);
				this.speedModifier = 0.1F;
				plane.flightTarget = null;
				return;
			}
			if (plane.getRat() != null) {
				if (!plane.getRat().canMove()) {
					return;
				}
			}
			if (plane.flightTarget == null && this.hasWanted()) {
				plane.flightTarget = new Vec3(this.getWantedX(), this.getWantedY(), this.getWantedZ());
			}
			if (plane.flightTarget != null) {
				float distX = (float) (plane.flightTarget.x - plane.getX());
				float distY = (float) (plane.flightTarget.y - plane.getY());
				float distZ = (float) (plane.flightTarget.z - plane.getZ());
				double planeDist = Mth.sqrt(distX * distX + distZ * distZ);
				double yDistMod = 1.0D - (double) Mth.abs(distY * 0.7F) / planeDist;
				distX = (float) ((double) distX * yDistMod);
				distZ = (float) ((double) distZ * yDistMod);
				planeDist = Mth.sqrt(distX * distX + distZ * distZ);
				double dist = Mth.sqrt(distX * distX + distZ * distZ + distY * distY);
				if (dist > 1.0F) {
					float yawCopy = plane.getYRot();
					float atan = (float) Mth.atan2(distZ, distX);
					float yawTurn = Mth.wrapDegrees(plane.getYRot() + 90);
					float yawTurnAtan = Mth.wrapDegrees(atan * 57.295776F);
					plane.setYRot(approachDegrees(yawTurn, yawTurnAtan, 4.0F) - 90.0F);
					plane.yBodyRot = plane.getYRot();
					if (degreesDifferenceAbs(yawCopy, plane.getYRot()) < 3.0F) {
						this.speedModifier = approach((float) this.speedModifier, 1.2F, 0.005F * (1.2F / (float) this.speedModifier));
					} else {
						this.speedModifier = approach((float) this.speedModifier, 0.2F, 0.025F);
						if (dist < 100D && plane.getTarget() != null) {
							this.speedModifier = this.speedModifier * (dist / 100D);
						}
					}
					float finPitch = (float) (-(Mth.atan2(-distY, planeDist) * 57.2957763671875D));
					plane.setXRot(finPitch);
					float yawTurnHead = plane.getYRot() + 90.0F;
					double lvt_16_1_ = this.speedModifier * Mth.cos(yawTurnHead * 0.017453292F) * Math.abs((double) distX / dist);
					double lvt_18_1_ = this.speedModifier * Mth.sin(yawTurnHead * 0.017453292F) * Math.abs((double) distZ / dist);
					double lvt_20_1_ = this.speedModifier * Mth.sin(finPitch * 0.017453292F) * Math.abs((double) distY / dist);
					plane.setDeltaMovement(plane.getDeltaMovement().add(lvt_16_1_ * 0.4D, lvt_20_1_ * 0.4D, lvt_18_1_ * 0.4D));
				}
			}
		}
	}
}
