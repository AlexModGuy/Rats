package com.github.alexthe666.rats.server.entity.projectile;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;

public class GolemBeam extends ArrowlikeProjectile {

	public GolemBeam(EntityType<? extends ArrowlikeProjectile> type, Level level) {
		super(type, level);
	}

	public GolemBeam(EntityType<? extends ArrowlikeProjectile> type, Level level, LivingEntity shooter) {
		super(type, shooter, level);
		this.setBaseDamage(3.5F);
	}

	@Override
	public void tick() {
		float sqrt = (float) this.getDeltaMovement().length();
		if (sqrt < 0.3F || this.inGround || this.horizontalCollision) {
			this.discard();
			Explosion explosion = this.level().explode(this.getOwner(), this.getX(), this.getY(), this.getZ(), 0.0F, Level.ExplosionInteraction.MOB);
			explosion.explode();
			explosion.finalizeExplosion(true);
		}
		super.tick();
	}

	@Override
	public boolean isInWater() {
		return false;
	}

	@Override
	public boolean isNoGravity() {
		return true;
	}

	@Override
	public boolean explodesOnHit() {
		return true;
	}
}
