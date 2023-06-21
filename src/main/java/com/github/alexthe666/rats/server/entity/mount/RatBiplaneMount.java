package com.github.alexthe666.rats.server.entity.mount;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.rats.registry.RatlantisEntityRegistry;
import com.github.alexthe666.rats.registry.RatlantisItemRegistry;
import com.github.alexthe666.rats.registry.RatsSoundRegistry;
import com.github.alexthe666.rats.server.entity.Plane;
import com.github.alexthe666.rats.server.entity.ai.navigation.control.PlaneMoveControl;
import com.github.alexthe666.rats.server.entity.projectile.RattlingGunBullet;
import com.github.alexthe666.rats.server.entity.rat.AbstractRat;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.misc.PlaneRotationUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class RatBiplaneMount extends RatMountBase implements Plane {

	public float prevPlanePitch;
	private static final EntityDataAccessor<Boolean> FIRING = SynchedEntityData.defineId(RatBiplaneMount.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Float> PLANE_PITCH = SynchedEntityData.defineId(RatBiplaneMount.class, EntityDataSerializers.FLOAT);
	public PlaneRotationUtil roll_buffer;
	public PlaneRotationUtil pitch_buffer;
	@Nullable
	private Vec3 flightTarget;
	private int soundLoopCounter = 0;

	public RatBiplaneMount(EntityType<? extends PathfinderMob> type, Level level) {
		super(type, level);
		this.riderY = 1.35F;
		this.riderXZ = -0.35F;
		this.moveControl = new PlaneMoveControl<>(this);
		if (this.level().isClientSide()) {
			this.roll_buffer = new PlaneRotationUtil();
			this.pitch_buffer = new PlaneRotationUtil();
		}
	}

	@Override
	public float getStepHeight() {
		return 1.0F;
	}

	@Override
	public void positionRider(Entity passenger, Entity.MoveFunction callback) {
		super.positionRider(passenger, callback);
		float radius = 0.35F;
		float angle = (0.01745329251F * this.yBodyRot);
		double extraX = radius * Mth.sin((float) (Math.PI + angle));
		double extraZ = radius * Mth.cos(angle);
		double extraY = 1.35F;
		callback.accept(passenger, this.getX() + extraX, this.getY() + extraY, this.getZ() + extraZ);
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
				.add(Attributes.MAX_HEALTH, 300.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.35D)
				.add(Attributes.ATTACK_DAMAGE, 1.0D)
				.add(Attributes.FOLLOW_RANGE, 32.0D);
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

		this.prevPlanePitch = this.getPlanePitch();
		if (!this.isVehicle() && !this.level().isClientSide()) {
			this.hurt(this.damageSources().drown(), 1000);
		}
		if (!this.onGround() && this.getDeltaMovement().y < 0.0D) {
			this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, 0.6D, 1.0D));
		}
		TamedRat rat = this.getRat();
		double up = 0.08D;
		if (rat != null && !rat.canMove()) {
			up = 0;
		}
		this.setDeltaMovement(this.getDeltaMovement().x(), this.getDeltaMovement().y() + up, this.getDeltaMovement().z());

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
					RattlingGunBullet cannonball = new RattlingGunBullet(RatlantisEntityRegistry.RATTLING_GUN_BULLET.get(), this.level(), this);
					cannonball.setPos(extraX, extraY, extraZ);
					cannonball.setBaseDamage(0.5F);
					cannonball.shoot(d1, d2, d3, velocity, 1.4F);
					this.playSound(RatsSoundRegistry.BIPLANE_SHOOT.get(), 3.0F, 2.3F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
					if (!this.level().isClientSide()) {
						this.level().addFreshEntity(cannonball);
					}
				}
			}
			this.setFiring(true);
		}

		if (this.getFlightTarget() == null || this.distanceToSqr(this.getFlightTarget().x(), this.getFlightTarget().y(), this.getFlightTarget().z()) < 20 || rat != null && !rat.canMove()) {
			this.setFlightTarget(null);
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

	@Override
	public @Nullable Vec3 getFlightTarget() {
		return this.flightTarget;
	}

	@Override
	public void setFlightTarget(@Nullable Vec3 target) {
		this.flightTarget = target;
	}

	@Override
	public void adjustRatTailRotation(AbstractRat rat, AdvancedModelBox upperTail, AdvancedModelBox lowerTail) {
		this.progressRotation(upperTail, rat.sitProgress, 1.3F, 0.0F, 0.0F, 20.0F);
		this.progressRotation(lowerTail, rat.sitProgress, -0.2F, 0.0F, 0.0F, 20.0F);
	}
}
