package com.github.alexthe666.rats.server.entity.projectile;

import com.github.alexthe666.rats.registry.RatlantisItemRegistry;
import com.github.alexthe666.rats.registry.RatsItemRegistry;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class CheeseCannonball extends ThrowableProjectile implements ItemSupplier {

	public CheeseCannonball(EntityType<? extends ThrowableProjectile> type, Level level) {
		super(type, level);
	}

	public CheeseCannonball(EntityType<? extends ThrowableProjectile> type, Level level, LivingEntity thrower) {
		super(type, thrower, level);
	}

	@Override
	protected void defineSynchedData() {

	}

	public void handleEntityEvent(byte id) {
		if (id == 3) {
			for (int i = 0; i < 18; ++i) {
				this.getLevel().addParticle(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(RatsItemRegistry.CHEESE.get())), this.getX(), this.getY(), this.getZ(), ((double) this.random.nextFloat() - 0.5D) * 0.08D, ((double) this.random.nextFloat() - 0.5D) * 0.08D, ((double) this.random.nextFloat() - 0.5D) * 0.08D);
			}
		}
	}

	protected void onHit(HitResult result) {
		if (result instanceof EntityHitResult hitResult && this.getOwner() != null && this.getOwner().isAlliedTo(hitResult.getEntity())) {
			return;
		}
		super.onHit(result);
		if (!this.getLevel().isClientSide()) {
			Explosion explosion = new Explosion(this.getLevel(), this.getOwner() == null ? this : this.getOwner(), this.getX(), this.getY(), this.getZ(), 1.0F, false, Explosion.BlockInteraction.KEEP);
			explosion.explode();
			explosion.finalizeExplosion(true);
			this.discard();
		}
	}

	@Override
	protected void onHitEntity(EntityHitResult result) {
		if ((this.getOwner() == null || !result.getEntity().isAlliedTo(this.getOwner())) && result.getEntity() instanceof LivingEntity) {
			result.getEntity().hurt(this.damageSources().thrown(this, this.getOwner()), 8.0F);
		}
	}

	@Override
	public ItemStack getItem() {
		return new ItemStack(RatlantisItemRegistry.CHEESE_CANNONBALL.get());
	}
}