package com.github.alexthe666.rats.server.entity.ratlantis;

import com.github.alexthe666.rats.registry.RatlantisEntityRegistry;
import com.github.alexthe666.rats.registry.RatlantisItemRegistry;
import com.github.alexthe666.rats.registry.RatsSoundRegistry;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class RattlingGun extends Entity {
	private static final EntityDataAccessor<Boolean> FIRING = SynchedEntityData.defineId(RattlingGun.class, EntityDataSerializers.BOOLEAN);
	private boolean prevFire;
	private int fireCooldown = 0;

	private boolean markedDead;
	private int damageTaken;

	public RattlingGun(EntityType<? extends Entity> type, Level level) {
		super(type, level);
		this.refreshDimensions();
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag tag) {

	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {

	}

	@Override
	public LivingEntity getControllingPassenger() {
		if (!this.getPassengers().isEmpty()) {
			for (Entity entity : this.getPassengers()) {
				if (entity instanceof TamedRat rat) {
					return rat;
				}
			}
		}
		return null;
	}

	public double getMyRidingOffset() {
		return 0.45D;
	}

	public void positionRider(Entity passenger) {
		super.positionRider(passenger);
		float radius = 0.9F;
		float angle = (0.01745329251F * (this.getYRot() + 150.0F));
		double extraX = radius * Mth.sin((float) (Math.PI + angle));
		double extraZ = radius * Mth.cos(angle);
		double extraY = 1.3D;
		passenger.setPos(this.getX() + extraX, this.getY() + extraY, this.getZ() + extraZ);
	}

	@Override
	protected void defineSynchedData() {
		this.getEntityData().define(FIRING, false);
	}

	public boolean isFiring() {
		return this.getEntityData().get(FIRING);
	}

	public void setFiring(boolean firing) {
		this.getEntityData().set(FIRING, firing);
	}

	@Override
	public boolean isPickable() {
		return true;
	}

//	protected void tickDeath() {
//		++this.deathTime;
//		Vec3 vec3d = this.getDeltaMovement();
//		this.setDeltaMovement(vec3d.multiply(1.0D, 0.6D, 1.0D));
//		this.ambientSoundTime = 20;
//		if (this.deathTime >= 40) {
//			this.discard();
//			for (int k = 0; k < 20; ++k) {
//				double d2 = this.random.nextGaussian() * 0.02D;
//				double d0 = this.random.nextGaussian() * 0.02D;
//				double d1 = this.random.nextGaussian() * 0.02D;
//				this.getLevel().addParticle(ParticleTypes.EXPLOSION, this.getX() + (double) (this.random.nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), this.getY() + (double) (this.random.nextFloat() * this.getBbHeight()), this.getZ() + (double) (this.random.nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), d2, d0, d1);
//			}
//		}
//	}

	public void tick() {
		super.tick();
		LivingEntity passenger = this.getControllingPassenger();
		if (this.getVehicle() != null) {
			if (!this.getVehicle().isPassenger()) {
				this.getVehicle().startRiding(this, true);
			}
		}
		if (!this.getLevel().isClientSide()) {
			if (this.prevFire != this.isFiring()) {
				this.fireCooldown = 4;
			}
			if (this.isFiring() && this.fireCooldown == 0) {
				this.setFiring(false);
			}
			if (this.fireCooldown > 0) {
				this.fireCooldown--;
			}
			this.prevFire = this.isFiring();
			if (passenger != null) {
				this.setYRot(passenger.getYRot());
				this.yRotO = passenger.getYRot();
				if (passenger instanceof TamedRat rat) {
					if (rat.getTarget() != null && rat.getTarget().isAlive() && rat.getTarget().getId() != this.getId()) {
						this.shoot(rat);
					}
				}
			}
			this.setRot(this.getYRot(), this.getXRot());
		}

		this.checkInsideBlocks();
	}

	@Override
	public InteractionResult interact(Player player, InteractionHand hand) {
		if (this.getControllingPassenger() == null) {
			if (!player.getPassengers().isEmpty()) {
				boolean flag = false;
				for (Entity entity : player.getPassengers()) {
					if (entity instanceof TamedRat) {
						flag = true;
						entity.startRiding(this, false);
						break;
					}
				}
				return flag ? InteractionResult.SUCCESS : InteractionResult.PASS;
			} else {
				if (player.isShiftKeyDown()) {
					this.spawnAtLocation(RatlantisItemRegistry.RATTLING_GUN.get());
					this.discard();
					return InteractionResult.SUCCESS;
				}
			}
		} else {
			Entity passenger = this.getControllingPassenger();
			if (passenger instanceof TamedRat rat) {
				if (rat.isOwnedBy(player)) {
					passenger.stopRiding();
					passenger.startRiding(player);
					return InteractionResult.SUCCESS;
				}
			}
		}
		return InteractionResult.PASS;
	}

	public void shoot(TamedRat rat) {
		LivingEntity target = rat.getTarget();
		if (target != null) {
			{
				double d0 = target.getX() - this.getX();
				double d2 = target.getZ() - this.getZ();
				float f = (float) (Mth.atan2(d2, d0) * (180D / Math.PI)) - 90.0F;
				this.setYRot(f % 360);
				rat.yBodyRot = f % 360;
				rat.setYRot(f % 360);
				rat.yHeadRot = f % 360;
			}
			RattlingGunBullet bullet = new RattlingGunBullet(RatlantisEntityRegistry.RATTLING_GUN_BULLET.get(), this.getLevel(), rat);
			float radius = 1.6F;
			float angle = (0.01745329251F * (this.getYRot()));
			double extraX = (double) (radius * Mth.sin((float) (Math.PI + angle))) + getX() + this.random.nextFloat() * 0.2F - 0.1;
			double extraZ = (double) (radius * Mth.cos(angle)) + getZ() + this.random.nextFloat() * 0.2F - 0.1;
			double extraY = 1.35 + getY() + this.random.nextFloat() * 0.1F - 0.05;
			double d0 = target.getY() + (double) target.getEyeHeight() / 2;
			double d1 = target.getX() - extraX;
			double d3 = target.getZ() - extraZ;
			double d2 = d0 - extraY;
			float velocity = 2.2F;
			bullet.setPos(extraX, extraY, extraZ);
			bullet.shoot(d1, d2, d3, velocity, 0.4F);
			this.playSound(RatsSoundRegistry.RATTLING_GUN_SHOOT.get(), 0.5F, 2.3F / (this.random.nextFloat() * 0.4F + 0.8F));
			if (!this.getLevel().isClientSide()) {
				this.getLevel().addFreshEntity(bullet);
			}
			this.setFiring(true);
		}
	}
}
