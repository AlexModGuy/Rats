package com.github.alexthe666.rats.server.entity.projectile;

import com.github.alexthe666.rats.registry.RatlantisEntityRegistry;
import com.github.alexthe666.rats.registry.RatlantisItemRegistry;
import com.github.alexthe666.rats.registry.RatsParticleRegistry;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class RatlanteanFlame extends Fireball {

	public RatlanteanFlame(EntityType<? extends Fireball> type, Level level) {
		super(type, level);
	}

	public RatlanteanFlame(Level level, LivingEntity shooter, double accelX, double accelY, double accelZ) {
		super(RatlantisEntityRegistry.RATLANTEAN_FLAME.get(), shooter.getX(), shooter.getY() + shooter.getBbHeight() * 0.5D, shooter.getZ(), accelX, accelY, accelZ, level);
		this.setOwner(shooter);
	}

	public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
		Vec3 vec3d = (new Vec3(x, y, z)).normalize().add(this.random.nextGaussian() * (double) 0.0075F * (double) inaccuracy, this.random.nextGaussian() * (double) 0.0075F * (double) inaccuracy, this.random.nextGaussian() * (double) 0.0075F * (double) inaccuracy).scale(velocity);
		this.setDeltaMovement(vec3d);
		double f = vec3d.horizontalDistanceSqr();
		this.setYRot((float) (Mth.atan2(vec3d.x, vec3d.z) * (double) (180F / (float) Math.PI)));
		this.setXRot((float) (Mth.atan2(vec3d.y, f) * (double) (180F / (float) Math.PI)));
		this.yRotO = this.getYRot();
		this.xRotO = this.getXRot();
	}

	public void shoot(Entity shooter, float pitch, float yaw, float velocity, float inaccuracy) {
		float f = -Mth.sin(yaw * ((float) Math.PI / 180F)) * Mth.cos(pitch * ((float) Math.PI / 180F));
		float f1 = -Mth.sin(pitch * ((float) Math.PI / 180F));
		float f2 = Mth.cos(yaw * ((float) Math.PI / 180F)) * Mth.cos(pitch * ((float) Math.PI / 180F));
		this.shoot(f, f1, f2, velocity, inaccuracy);
		this.setDeltaMovement(this.getDeltaMovement().add(shooter.getDeltaMovement().x, shooter.isOnGround() ? 0.0D : shooter.getDeltaMovement().y, shooter.getDeltaMovement().z));
	}

	public void tick() {
		super.tick();
		if (this.getLevel().isClientSide()) {
			this.getLevel().addParticle(RatsParticleRegistry.RAT_GHOST.get(),
					this.getX() + (double) (this.random.nextFloat() * this.getBbWidth() * 2F) - (double) this.getBbWidth(),
					this.getY() + (double) (this.random.nextFloat() * this.getBbHeight()),
					this.getZ() + (double) (this.random.nextFloat() * this.getBbWidth() * 2F) - (double) this.getBbWidth(),
					0.92F, 0.82, 0.0F);
		}
		if (this.isInWater()) {
			this.clearFire();
			this.discard();
		}
	}

	protected void onHit(HitResult result) {
		super.onHit(result);
		this.discard();
	}

	@Override
	protected void onHitEntity(EntityHitResult result) {
		if (this.getOwner() != null) {
			if (!result.getEntity().fireImmune()) {
				result.getEntity().setSecondsOnFire(10);
			}
			boolean flag = result.getEntity().hurt(this.damageSources().fireball(this, this.getOwner()), 2.0F);
			if (flag) {
				this.doEnchantDamageEffects((LivingEntity) this.getOwner(), result.getEntity());
			}
		}
	}

	@Override
	public boolean isAttackable() {
		return false;
	}

	@Override
	public ItemStack getItem() {
		return new ItemStack(RatlantisItemRegistry.RATLANTEAN_FLAME.get());
	}
}
