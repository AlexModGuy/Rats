package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.misc.RatUpgradeUtils;
import com.github.alexthe666.rats.server.misc.RatUtils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class RatStriderMount extends Strider implements RatMount {

	private int checkCooldown = 20;

	public RatStriderMount(EntityType<? extends Strider> type, Level level) {
		super(type, level);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.175F).add(Attributes.FOLLOW_RANGE, 16.0D);
	}

	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		TamedRat rat = this.getRat();
		return rat != null ? rat.mobInteract(player, hand) : InteractionResult.PASS;
	}

	@Override
	public boolean isControlledByLocalInstance() {
		return false;
	}

	@Override
	public LivingEntity getControllingPassenger() {
		return this.getRat();
	}

	protected void tickDeath() {
		Vec3 vec3d = this.getDeltaMovement();
		TamedRat rat = this.getRat();
		if (rat != null) {
			rat.setMountCooldown(1000);
			rat.stopRiding();
		}
		this.setDeltaMovement(vec3d.multiply(1.0D, 0.6D, 1.0D));
		this.ambientSoundTime = 20;
		++this.deathTime;
		if (deathTime >= 5) {
			this.discard();
			for (int k = 0; k < 20; ++k) {
				double d2 = this.getRandom().nextGaussian() * 0.02D;
				double d0 = this.getRandom().nextGaussian() * 0.02D;
				double d1 = this.getRandom().nextGaussian() * 0.02D;
				this.getLevel().addParticle(ParticleTypes.EXPLOSION, this.getX() + (double) (this.getRandom().nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), this.getY() + (double) (this.getRandom().nextFloat() * this.getBbHeight()), this.getZ() + (double) (this.getRandom().nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), d2, d0, d1);
			}
		}
	}

	public boolean isAlliedTo(Entity entity) {
		return super.isAlliedTo(entity) || this.getRat() != null && this.getRat().isAlliedTo(entity);
	}

	@Nullable
	public TamedRat getRat() {
		if (!this.getPassengers().isEmpty()) {
			for (Entity entity : this.getPassengers()) {
				if (entity instanceof TamedRat) {
					return (TamedRat) entity;
				}
			}
		}
		return null;
	}

	public boolean isInvulnerableTo(DamageSource source) {
		TamedRat rat = getRat();
		if (rat != null) {
			return rat.isInvulnerableTo(source);
		}
		return super.isInvulnerableTo(source);
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
			} else {
				this.discard();
			}
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

	@Override
	public ItemStack getPickedResult(HitResult target) {
		return this.getRat() != null ? this.getRat().getPickedResult(target) : super.getPickedResult(target);
	}

	@Override
	public Item getUpgradeItem() {
		return RatsItemRegistry.RAT_UPGRADE_STRIDER_MOUNT.get();
	}
}
