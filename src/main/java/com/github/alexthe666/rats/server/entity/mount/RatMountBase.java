package com.github.alexthe666.rats.server.entity.mount;

import com.github.alexthe666.rats.server.entity.AdjustsRatTail;
import com.github.alexthe666.rats.server.entity.RatMount;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public abstract class RatMountBase extends PathfinderMob implements RatMount, AdjustsRatTail {

	protected double riderY;
	protected double riderXZ;
	private int checkCooldown;

	protected RatMountBase(EntityType<? extends PathfinderMob> type, Level level) {
		super(type, level);
		this.getNavigation().setCanFloat(true);
		this.riderY = 1.1D;
		this.riderXZ = 0.0D;
		this.checkCooldown = 20;
	}

	@Override
	protected boolean shouldPassengersInheritMalus() {
		return true;
	}

	@Override
	protected void updateControlFlags() {
		boolean flag = this.getRat() == null;
		boolean flag1 = !(this.getVehicle() instanceof Boat);
		this.goalSelector.setControlFlag(Goal.Flag.MOVE, flag);
		this.goalSelector.setControlFlag(Goal.Flag.JUMP, flag && flag1);
		this.goalSelector.setControlFlag(Goal.Flag.LOOK, flag);
		this.goalSelector.setControlFlag(Goal.Flag.TARGET, flag);
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
		if (this.getFirstPassenger() instanceof TamedRat rat) {
			return rat;
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
	public double getPassengersRidingOffset() {
		return this.riderY;
	}

	@Override
	public void positionRider(Entity passenger, Entity.MoveFunction callback) {
		super.positionRider(passenger, callback);
		if (this.hasPassenger(passenger)) {
			float radius = (float) this.riderXZ;
			float angle = (0.01745329251F * this.yBodyRot);
			double extraX = radius * Mth.sin((float) (Math.PI + angle));
			double extraZ = radius * Mth.cos(angle);
			double extraY = this.getY() + this.getPassengersRidingOffset() + passenger.getMyRidingOffset();
			callback.accept(passenger, this.getX() + extraX, extraY, this.getZ() + extraZ);
			if (passenger instanceof LivingEntity living) {
				living.yBodyRot = this.getYRot();
				living.setYHeadRot(this.getYRot());
				living.setXRot(this.getXRot());
				living.setYRot(this.getYRot());
			}
		}
	}

	@Override
	public void aiStep() {
		super.aiStep();
		TamedRat rat = this.getRat();
		if (this.checkCooldown > 0) {
			this.checkCooldown--;
		} else {
			if (rat != null) {
				this.setTarget(rat.getTarget());
				this.getLookControl().setLookAt(rat.getLookControl().getWantedX(), rat.getLookControl().getWantedY(), rat.getLookControl().getWantedZ());
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
		this.setDeltaMovement(vec3d.multiply(1.0D, 0.6D, 1.0D));
		this.ambientSoundTime = 20;
		++this.deathTime;
		if (deathTime >= 5 && !this.level().isClientSide()) {
			if (rat != null) {
				rat.setMountCooldown(1000);
				rat.stopRiding();
			}
			this.remove(Entity.RemovalReason.KILLED);
		}
	}

	@Override
	public void remove(RemovalReason reason) {
		TamedRat rat = this.getRat();
		if (reason.shouldDestroy() && !this.level().isClientSide() && rat != null) {
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
