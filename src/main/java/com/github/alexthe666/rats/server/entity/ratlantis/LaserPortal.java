package com.github.alexthe666.rats.server.entity.ratlantis;

import com.github.alexthe666.rats.registry.RatlantisEntityRegistry;
import com.github.alexthe666.rats.registry.RatsParticleRegistry;
import com.github.alexthe666.rats.registry.RatsSoundRegistry;
import com.github.alexthe666.rats.server.entity.rat.AbstractRat;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.PushReaction;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Predicate;

public class LaserPortal extends Entity {
	public static final Predicate<Entity> MONSTER_NOT_RAT = entity -> !(entity instanceof AbstractRat) && entity instanceof Enemy;
	public float scaleOfPortal;
	public float scaleOfPortalPrev;
	@Nullable
	private LivingEntity creator;
	@Nullable
	private Entity facingTarget;
	private UUID ownerUniqueId;

	public LaserPortal(EntityType<? extends Entity> type, Level level) {
		super(type, level);
	}

	public LaserPortal(EntityType<? extends Entity> type, Level level, double x, double y, double z, LivingEntity creator) {
		this(type, level);
		this.setPos(x, y, z);
		this.setCreator(creator);
	}

	public void tick() {
		super.tick();
		if (this.tickCount > 300 || this.getLevel().getCurrentDifficultyAt(this.blockPosition()).getDifficulty() == Difficulty.PEACEFUL) {
			this.discard();
		}
		if (this.tickCount < 250 && this.scaleOfPortal < 1.0F) {
			this.scaleOfPortal += 0.05F;
		}
		if (tickCount > 250 && this.scaleOfPortal > 0.0F) {
			this.scaleOfPortal -= 0.05F;
		}
		if (this.tickCount % 50 == 0) {
			this.tryFiring();
		} else {
			this.faceTarget();
		}
		if (this.getLevel().isClientSide() && this.scaleOfPortal >= 0.5F) {
			this.getLevel().addParticle(RatsParticleRegistry.LIGHTNING.get(),
					this.getX() + (double) (this.random.nextFloat() * this.getBbWidth()) - (double) this.getBbWidth() / 2,
					this.getY() + (double) (this.random.nextFloat() * this.getBbHeight()),
					this.getZ() + (double) (this.random.nextFloat() * this.getBbWidth()) - (double) this.getBbWidth() / 2,
					0.0F, 0.0F, 0.0F);
		}
		this.scaleOfPortalPrev = this.scaleOfPortal;
	}


	private void faceTarget() {
		if (this.facingTarget == null || (this.getCreator() != null && this.getCreator() instanceof Mob mob && (mob.getTarget() == null || !this.facingTarget.is(mob.getTarget())))) {
			if (this.getCreator() != null && this.getCreator() instanceof Mob mob) {
				LivingEntity target = mob.getTarget();
				if (target == null && this.getCreator() instanceof Monster) {
					target = this.getLevel().getNearestPlayer(this, 30);
				}
				this.facingTarget = target;
			}
		}
		if (this.facingTarget != null) {
			double d0 = this.getX() - this.facingTarget.getX();
			double d2 = this.getZ() - this.facingTarget.getZ();
			float f = (float) (Mth.atan2(d2, d0) * (180D / Math.PI)) - 90.0F;
			this.setYRot(f % 360);
		}
	}

	private void tryFiring() {
		if (this.getCreator() != null && this.getCreator() instanceof Mob) {
			LivingEntity target = ((Mob) this.getCreator()).getTarget();
			if (target == null && this.getCreator() instanceof Monster) {
				target = this.getLevel().getNearestPlayer(this, 30);
			}
			if (target == null && this.getCreator() instanceof TamedRat) {
				LivingEntity closest = null;
				for (Entity entity : this.getLevel().getEntities(this.getCreator(), this.getBoundingBox().inflate(40, 10, 40), MONSTER_NOT_RAT)) {
					if (entity instanceof LivingEntity && (closest == null || entity.distanceToSqr(this) < closest.distanceToSqr(this))) {
						closest = (LivingEntity) entity;
					}
				}
				target = closest;
			}
			if (target != null) {
				double d0 = this.getX() - target.getX();
				double d2 = this.getZ() - target.getZ();
				float f = (float) (Mth.atan2(d2, d0) * (180D / Math.PI)) - 90.0F;
				this.setYRot(f % 360);
				double targetRelativeX = target.getX() - this.getX();
				double targetRelativeY = target.getY() + target.getBbHeight() / 2 - this.getY() - 1.0F;
				double targetRelativeZ = target.getZ() - this.getZ();
				LaserBeam beam = new LaserBeam(RatlantisEntityRegistry.LASER_BEAM.get(), this.getLevel(), this.getCreator());
				this.playSound(RatsSoundRegistry.LASER.get(), 1.0F, 0.75F + this.random.nextFloat() * 0.5F);
				beam.setPos(this.getX(), this.getY() + 1.0F, this.getZ());
				beam.shoot(targetRelativeX, targetRelativeY, targetRelativeZ, 2.0F, 0.4F);
				if (!this.getLevel().isClientSide()) {
					this.getLevel().addFreshEntity(beam);
				}
			}
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
