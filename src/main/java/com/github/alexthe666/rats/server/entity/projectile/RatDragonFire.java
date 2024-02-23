package com.github.alexthe666.rats.server.entity.projectile;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class RatDragonFire extends Fireball {

	public RatDragonFire(EntityType<? extends Fireball> type, Level level) {
		super(type, level);
	}

	public RatDragonFire(EntityType<? extends Fireball> type, LivingEntity shooter, Level level, double accelX, double accelY, double accelZ) {
		super(type, shooter, accelX, accelY, accelZ, level);
	}

	@Override
	public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
		Vec3 vec3d = (new Vec3(x, y, z)).normalize().add(this.random.nextGaussian() * (double) 0.0075F * (double) inaccuracy, this.random.nextGaussian() * (double) 0.0075F * (double) inaccuracy, this.random.nextGaussian() * (double) 0.0075F * (double) inaccuracy).scale(velocity);
		this.setDeltaMovement(vec3d);
		double f = vec3d.horizontalDistanceSqr();
		this.setYRot((float) (Mth.atan2(vec3d.x, vec3d.z) * (double) (180F / (float) Math.PI)));
		this.setXRot((float) (Mth.atan2(vec3d.y, f) * (double) (180F / (float) Math.PI)));
		this.yRotO = this.getYRot();
		this.xRotO = this.getXRot();
	}

	@Override
	protected boolean shouldBurn() {
		return false;
	}

	@Override
	public void tick() {
		super.tick();
		if (this.level().isClientSide()) {
			this.level().addParticle(ParticleTypes.FLAME, this.getX() + (double) (this.random.nextFloat() * this.getBbWidth() * 2F) - (double) this.getBbWidth(),
					this.getY() + (double) (this.random.nextFloat() * this.getBbHeight()),
					this.getZ() + (double) (this.random.nextFloat() * this.getBbWidth() * 2F) - (double) this.getBbWidth(), 0, 0, 0);
		}
		if (tickCount > 200) {
			this.discard();
		}
	}

	@Override
	protected void onHitBlock(BlockHitResult result) {
		super.onHitBlock(result);
		if (!this.level().isClientSide()) {
			this.discard();
		}
	}

	@Override
	protected void onHitEntity(EntityHitResult result) {
		Entity entity = result.getEntity();
		if (!entity.fireImmune()) {
			entity.setSecondsOnFire(10);
			boolean flag = entity.hurt(this.damageSources().fireball(this, this.getOwner()), 5.0F);
			if (flag && this.getOwner() instanceof LivingEntity living) {
				this.doEnchantDamageEffects(living, entity);
			}

			if (!this.level().isClientSide()) {
				this.discard();
			}
		}
	}

	@Override
	protected boolean canHitEntity(Entity entity) {
		return super.canHitEntity(entity) && (this.getOwner() == null || !this.isAlliedToOwner(entity));
	}

	private boolean isAlliedToOwner(Entity entity) {
		if (this.getOwner() instanceof OwnableEntity ownable && ownable.getOwner() == entity) return true;
		return entity instanceof OwnableEntity ownable && ownable.getOwner() == entity;
	}

	@Override
	public boolean canBeCollidedWith() {
		return false;
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		return false;
	}
}
