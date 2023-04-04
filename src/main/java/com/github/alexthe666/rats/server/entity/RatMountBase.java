package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.misc.RatUpgradeUtils;
import com.github.alexthe666.rats.server.misc.RatUtils;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public abstract class RatMountBase extends Mob implements RatMount {

	protected double riderY;
	protected double riderXZ;
	private int checkCooldown = 20;

	protected RatMountBase(EntityType<? extends Mob> type, Level level) {
		super(type, level);
		this.riderY = 1.1D;
		this.riderXZ = 0.0D;
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
	}

	@Override
	public boolean canBeAffected(MobEffectInstance instance) {
		return this.getRat() != null ? this.getRat().canBeAffected(instance) : super.canBeAffected(instance);
	}

	@Override
	public boolean isCurrentlyGlowing() {
		return super.isCurrentlyGlowing() || (this.getRat() != null && this.getRat().isCurrentlyGlowing());
	}

	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		TamedRat rat = this.getRat();
		return rat != null ? rat.mobInteract(player, hand) : InteractionResult.PASS;
	}

	@Override
	public LivingEntity getControllingPassenger() {
		return this.getRat();
	}

	@Nullable
	public TamedRat getRat() {
		if (!this.getPassengers().isEmpty()) {
			for (Entity entity : this.getPassengers()) {
				if (entity instanceof TamedRat rat) {
					return rat;
				}
			}
		}
		return null;
	}

	public boolean isInvulnerableTo(DamageSource source) {
		if (source.is(DamageTypes.IN_WALL)) return true;
		TamedRat rat = this.getRat();
		if (rat != null) {
			return rat.isInvulnerableTo(source);
		}
		return super.isInvulnerableTo(source);
	}

	@Override
	public void positionRider(Entity passenger) {
		super.positionRider(passenger);
		if (this.hasPassenger(passenger)) {
			float radius = (float) riderXZ;
			float angle = (0.01745329251F * this.yBodyRot);
			double extraX = radius * Mth.sin((float) (Math.PI + angle));
			double extraZ = radius * Mth.cos(angle);
			double extraY = riderY;
			passenger.setPos(this.getX() + extraX, this.getY() + extraY, this.getZ() + extraZ);
			if (passenger instanceof LivingEntity) {
				((LivingEntity) passenger).yBodyRot = this.yBodyRot;
			}
		}
	}

	public void tick() {
		super.tick();
		TamedRat rat = this.getRat();
		if (this.getTarget() != null && this.isAlliedTo(this.getTarget())) {
			this.setTarget(null);
		}
		if (this.getTarget() != null && this.getTarget().getId() == this.getId()) {
			this.setTarget(null);
		}
		if (this.getTarget() != null && RatUtils.isRidingOrBeingRiddenBy(this.getTarget(), this)) {
			this.setTarget(null);
		}
		if (this.checkCooldown > 0) {
			this.checkCooldown--;
		} else {
			if (rat != null) {
				if (RatUpgradeUtils.hasUpgrade(rat, this.getUpgradeItem())) {
					if (this.getTarget() != null && rat.getTarget() != this.getTarget()) {
						this.setTarget(null);
					}
					if (rat.getTarget() != null && rat.getTarget() != this) {
						this.setTarget(rat.getTarget());
					}
					if (this.getLastHurtByMob() != null && rat.getLastHurtByMob() != this && !this.isAlliedTo(this.getLastHurtByMob())) {
						rat.setLastHurtByMob(this.getLastHurtByMob());
						rat.setTarget(this.getLastHurtByMob());
					}
				}
				rat.setYRot(this.getYRot());
				rat.yRotO = this.getYRot();
				rat.setYHeadRot(this.getYRot());
				rat.yHeadRotO = this.getYRot();
			} else {
				this.discard();
			}
		}
	}

	@Override
	public boolean canBeSeenAsEnemy() {
		if (this.getRat() != null) {
			return this.getRat().canBeSeenAsEnemy();
		}
		return super.canBeSeenAsEnemy();
	}

	@Override
	protected void tickDeath() {
		Vec3 vec3d = this.getDeltaMovement();
		TamedRat rat = this.getRat();
		if (rat != null) {
			rat.setMountCooldown(1000);
		}
		this.setDeltaMovement(vec3d.multiply(1.0D, 0.6D, 1.0D));
		this.ambientSoundTime = 20;
		++this.deathTime;
		if (deathTime >= 5 && !this.getLevel().isClientSide()) {
			this.remove(Entity.RemovalReason.KILLED);
		}
	}

	@Override
	public void remove(RemovalReason reason) {
		TamedRat rat = this.getRat();
		if (rat != null) {
			rat.stopRiding();
		}
		super.remove(reason);
	}

	public boolean isAlliedTo(Entity entity) {
		return super.isAlliedTo(entity) || this.getRat() != null && this.getRat().isAlliedTo(entity);
	}

	@Override
	public ItemStack getPickedResult(HitResult target) {
		return this.getRat() != null ? this.getRat().getPickedResult(target) : super.getPickedResult(target);
	}
}
