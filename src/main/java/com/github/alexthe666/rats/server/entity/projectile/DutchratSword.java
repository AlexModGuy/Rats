package com.github.alexthe666.rats.server.entity.projectile;

import com.github.alexthe666.rats.registry.RatsParticleRegistry;
import com.github.alexthe666.rats.server.entity.monster.GhostPirat;
import com.github.alexthe666.rats.server.entity.monster.boss.Dutchrat;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

public class DutchratSword extends ThrowableProjectile {

	@Nullable
	private Entity target;

	public DutchratSword(EntityType<? extends ThrowableProjectile> type, Level level) {
		super(type, level);
	}

	public DutchratSword(EntityType<? extends ThrowableProjectile> type, Level level, LivingEntity creator) {
		super(type, creator, level);
	}

	@Override
	public boolean canCollideWith(Entity entity) {
		if (!entity.isSpectator() && entity.isAlive() && entity.canBeCollidedWith()) {
			Entity shooter = this.getOwner();
			return shooter == null || !entity.isPassengerOfSameVehicle(shooter);
		} else {
			return false;
		}
	}

	@Override
	protected void defineSynchedData() {

	}

	@Override
	public void tick() {
		super.tick();
		if (this.tickCount > 300) {
			this.discard();
		}

		if (this.level().isClientSide()) {
			this.level().addParticle(RatsParticleRegistry.PIRAT_GHOST.get(),
					this.getX() + (double) (this.random.nextFloat() * this.getBbWidth()) - (double) this.getBbWidth() / 2,
					this.getY() + (double) (this.random.nextFloat() * this.getBbHeight()),
					this.getZ() + (double) (this.random.nextFloat() * this.getBbWidth()) - (double) this.getBbWidth() / 2,
					0.0F, 0.0F, 0.0F);
		}
	}

	@Override
	protected void onHit(HitResult result) {
		super.onHit(result);
		this.discard();
	}

	@Override
	protected void onHitEntity(EntityHitResult result) {
		super.onHitEntity(result);
		DamageSource source = this.damageSources().magic();
		if (this.getOwner() instanceof LivingEntity living) {
			source = this.damageSources().mobAttack(living);
		}
		if (result.getEntity() instanceof Dutchrat || result.getEntity() instanceof GhostPirat || result.getEntity() == this.getOwner()) {
			return;
		}

		if (!this.level().isClientSide()) {
			result.getEntity().hurt(source, this.getOwner() instanceof LivingEntity living ? (float) living.getAttributeValue(Attributes.ATTACK_DAMAGE) : 5.0F);
			this.level().explode(this.getOwner(), this.getX(), this.getY(), this.getZ(), 2.0F, Level.ExplosionInteraction.NONE);
		}
	}

	@Override
	public PushReaction getPistonPushReaction() {
		return PushReaction.IGNORE;
	}

	@Override
	protected float getGravity() {
		return 0.0f;
	}
}
