package com.github.alexthe666.rats.server.entity.ratlantis;

import com.github.alexthe666.rats.registry.RatsParticleRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

public class DutchratSword extends Entity {
	@Nullable
	private LivingEntity creator;
	@Nullable
	private Entity target;
	private UUID ownerUniqueId;

	public DutchratSword(EntityType<? extends Entity> type, Level level) {
		super(type, level);
	}

	public DutchratSword(EntityType<? extends Entity> type, Level level, double x, double y, double z) {
		this(type, level);
		this.setPos(x, y, z);
	}

	public DutchratSword(EntityType<? extends Entity> type, Level level, double x, double y, double z, LivingEntity creator) {
		this(type, level);
		this.setPos(x, y, z);
		this.setCreator(creator);
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

	@Nullable
	public Entity getOwner() {
		if (this.creator != null && this.getLevel() instanceof ServerLevel server) {
			return server.getEntity(this.ownerUniqueId);
		}
		return null;
	}

	public void tick() {
		super.tick();
		this.noPhysics = true;
		if (this.tickCount > 300) {
			this.discard();
		}
		if (this.tickCount > 5) {
			HitResult raytraceresult = ProjectileUtil.getHitResult(this, this::canCollideWith);
			this.onHit(raytraceresult);
		}

		if (this.getLevel().isClientSide()) {
			this.getLevel().addParticle(RatsParticleRegistry.RAT_GHOST.get(),
					this.getX() + (double) (this.random.nextFloat() * this.getBbWidth()) - (double) this.getBbWidth() / 2,
					this.getY() + (double) (this.random.nextFloat() * this.getBbHeight()),
					this.getZ() + (double) (this.random.nextFloat() * this.getBbWidth()) - (double) this.getBbWidth() / 2,
					0.0F, 0.0F, 0.0F);
		}

		if (target == null || this.getCreator() != null && !this.target.is(Objects.requireNonNull(((Mob) this.getCreator()).getTarget()))) {
			if (this.getCreator() != null && this.getCreator() instanceof Mob) {
				LivingEntity target = ((Mob) this.getCreator()).getTarget();
				if (target == null && this.getCreator() instanceof Monster) {
					target = this.getLevel().getNearestPlayer(this, 30);
				}
				this.target = target;
			}
		}
		if (this.target != null) {
			double d0 = this.target.getX() - this.getX();
			double d1 = this.target.getY() - this.getY();
			double d2 = this.target.getZ() - this.getZ();
			double d3 = Mth.sqrt((float) (d0 * d0 + d1 * d1 + d2 * d2));
			double speed = 0.1D;
			this.setDeltaMovement(this.getDeltaMovement().add(d0 / d3 * speed, d1 / d3 * speed, d2 / d3 * speed));
			float f = (float) (Mth.atan2(d2, d0) * (180D / Math.PI)) - 90.0F;
			this.setYRot(f % 360);
			this.yRotO = f % 360;

		}
		this.move(MoverType.SELF, this.getDeltaMovement());
	}

	private void onHit(HitResult raytraceresult) {
		DamageSource source = this.damageSources().magic();
		if (this.getCreator() != null) {
			source = this.damageSources().mobAttack(this.getCreator());
		}
		boolean hit = false;
		for (Entity hitMobs : this.getLevel().getEntities(this, this.getBoundingBox().inflate(1.0F, 1.0F, 1.0F))) {
			if (!(hitMobs instanceof Pirats)) {
				hitMobs.hurt(source, (float) (this.creator != null ? creator.getAttributeValue(Attributes.ATTACK_DAMAGE) : 5.0D));
				hit = true;
			}
		}
		if (hit) {
			this.getLevel().explode(this.getCreator(), this.getX(), this.getY(), this.getZ(), 2.0F, Level.ExplosionInteraction.NONE);
			this.discard();
		}
	}

	@Nullable
	public LivingEntity getCreator() {
		if (this.creator == null && this.ownerUniqueId != null && this.getLevel() instanceof ServerLevel) {
			Entity entity = ((ServerLevel) this.getLevel()).getEntity(this.ownerUniqueId);
			if (entity instanceof LivingEntity) {
				this.creator = (LivingEntity) entity;
			}
		}

		return this.creator;
	}

	public void setCreator(@Nullable LivingEntity ownerIn) {
		this.creator = ownerIn;
		this.ownerUniqueId = ownerIn == null ? null : ownerIn.getUUID();
	}

	protected void readAdditionalSaveData(CompoundTag compound) {
		this.tickCount = compound.getInt("Age");
		if (compound.hasUUID("OwnerUUID")) {
			this.ownerUniqueId = compound.getUUID("OwnerUUID");
		}
	}

	protected void addAdditionalSaveData(CompoundTag compound) {
		compound.putInt("Age", this.tickCount);

		if (this.ownerUniqueId != null) {
			compound.putUUID("OwnerUUID", this.ownerUniqueId);
		}
	}

	public PushReaction getPistonPushReaction() {
		return PushReaction.IGNORE;
	}

	@Override
	protected void defineSynchedData() {

	}
}
