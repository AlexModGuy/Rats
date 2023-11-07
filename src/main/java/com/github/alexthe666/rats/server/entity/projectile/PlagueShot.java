package com.github.alexthe666.rats.server.entity.projectile;

import com.github.alexthe666.rats.registry.RatsEffectRegistry;
import com.github.alexthe666.rats.registry.RatsParticleRegistry;
import com.github.alexthe666.rats.server.events.ForgeEvents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class PlagueShot extends ArrowlikeProjectile {

	public PlagueShot(EntityType<? extends ArrowlikeProjectile> type, Level level) {
		super(type, level);
		this.setBaseDamage(6F);
	}

	public PlagueShot(EntityType<? extends ArrowlikeProjectile> type, Level level, LivingEntity shooter, double dmg) {
		super(type, shooter, level);
		this.setBaseDamage(dmg);
	}

	@Override
	public boolean isInWater() {
		return false;
	}

	@Override
	public void tick() {
		float sqrt = Mth.sqrt((float) (this.getDeltaMovement().x() * this.getDeltaMovement().x() + this.getDeltaMovement().z() * this.getDeltaMovement().z()));
		if (!this.shouldRender(this.getX(), this.getY(), this.getZ()) || (sqrt < 0.1F || this.inGround || this.horizontalCollision) && this.tickCount > 5) {
			this.discard();
		}
		double d0 = 0;
		double d1 = 0.01D;
		double d2 = 0D;
		double x = this.getX() + (double) (this.random.nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth();
		double y = this.getY() + (double) (this.random.nextFloat() * this.getBbHeight()) - (double) this.getBbHeight();
		double z = this.getZ() + (double) (this.random.nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth();
		float f = (this.getBbWidth() + this.getBbHeight() + this.getBbWidth()) * 0.333F + 0.5F;
		if (this.particleDistSq(x, y, z) < f * f) {
			if (this.random.nextBoolean()) {
				this.level().addParticle(RatsParticleRegistry.BLACK_DEATH.get(), x, y + 0.5D, z, d0, d1, d2);
			} else {
				this.level().addParticle(ParticleTypes.ENTITY_EFFECT, x, y + 0.5D, z, d0, d1, d2);

			}
		}
		super.tick();
	}

	public double particleDistSq(double toX, double toY, double toZ) {
		double d0 = this.getX() - toX;
		double d1 = this.getY() - toY;
		double d2 = this.getZ() - toZ;
		return d0 * d0 + d1 * d1 + d2 * d2;
	}

	@Override
	protected void onHitEntity(EntityHitResult result) {
		super.onHitEntity(result);
		if (result.getEntity() instanceof LivingEntity living) {
			if (this.getOwner() == null || !living.is(this.getOwner())) {
				living.addEffect(new MobEffectInstance(RatsEffectRegistry.PLAGUE.get(), 1200));
			}
		}
	}

	@Override
	public boolean isNoGravity() {
		return true;
	}

	@Override
	public boolean explodesOnHit() {
		return false;
	}
}
