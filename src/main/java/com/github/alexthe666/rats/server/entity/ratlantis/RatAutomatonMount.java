package com.github.alexthe666.rats.server.entity.ratlantis;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.rats.registry.RatlantisEntityRegistry;
import com.github.alexthe666.rats.registry.RatlantisItemRegistry;
import com.github.alexthe666.rats.registry.RatsSoundRegistry;
import com.github.alexthe666.rats.server.entity.RatMountBase;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class RatAutomatonMount extends RatMountBase implements IAnimatedEntity {

	private int animationTick;
	private Animation currentAnimation;
	public static final Animation ANIMATION_MELEE = Animation.create(15);
	public static final Animation ANIMATION_RANGED = Animation.create(15);
	private boolean useRangedAttack = false;

	public RatAutomatonMount(EntityType<? extends PathfinderMob> type, Level level) {
		super(type, level);
		this.riderY = 2.85F;
		this.moveControl = new FlyingMoveControl(this, 10, false);
	}

	@Override
	public float getStepHeight() {
		return 2.0F;
	}

	@Override
	public int getAnimationTick() {
		return animationTick;
	}

	@Override
	public void setAnimationTick(int tick) {
		animationTick = tick;
	}

	@Override
	public Animation getAnimation() {
		return currentAnimation;
	}

	@Override
	public void setAnimation(Animation animation) {
		currentAnimation = animation;
	}

	@Override
	public Animation[] getAnimations() {
		return new Animation[]{ANIMATION_MELEE, ANIMATION_RANGED};
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 250.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.2D)
				.add(Attributes.FLYING_SPEED, 0.2D)
				.add(Attributes.ATTACK_DAMAGE, 5.0D)
				.add(Attributes.FOLLOW_RANGE, 32.0D)
				.add(Attributes.ARMOR, 10.0D)
				.add(Attributes.KNOCKBACK_RESISTANCE, 0.75D);
	}

	public boolean doHurtTarget(Entity entity) {
		if (this.getAnimation() == NO_ANIMATION) {
			this.setAnimation(this.useRangedAttack ? ANIMATION_MELEE : ANIMATION_RANGED);
		}
		return true;
	}

	public void aiStep() {
		super.aiStep();
		LivingEntity target = this.getTarget();
		if (target != null) {
			this.useRangedAttack = this.distanceTo(target) > 10;
		}
		if (this.useRangedAttack && this.getAnimation() != ANIMATION_RANGED && target != null && this.hasLineOfSight(target) && target.isAlive()) {
			this.setAnimation(ANIMATION_RANGED);
			this.lookAt(target, 360, 80);

		}
		if (!this.useRangedAttack && target != null && this.distanceTo(target) < 7 && this.hasLineOfSight(target) && target.isAlive()) {
			if (this.getAnimation() == NO_ANIMATION) {
				this.setAnimation(ANIMATION_MELEE);
				this.playSound(SoundEvents.PLAYER_ATTACK_SWEEP, 1.0F, 0.5F + this.getRandom().nextFloat() * 0.5F);
			}
			this.lookAt(target, 360, 80);
			if (this.getAnimation() == ANIMATION_MELEE && this.getAnimationTick() == 10) {
				target.hurt(this.damageSources().mobAttack(this), 6.0F);
				target.knockback(1.5F, this.getX() - target.getX(), this.getZ() - target.getZ());
				this.useRangedAttack = this.getRandom().nextBoolean();
			}
		}
		if (this.getLevel().isClientSide() && this.getRandom().nextDouble() < 0.5F) {
			float radius = -0.5F;
			float angle = (0.01745329251F * (this.yBodyRot));
			double extraX = (double) (radius * Mth.sin((float) (Math.PI + angle))) + getX();
			double extraZ = (double) (radius * Mth.cos(angle)) + getZ();
			double extraY = 1.25 + getY();
			this.getLevel().addParticle(ParticleTypes.END_ROD, extraX + (double) (this.getRandom().nextFloat() * 0.5F) - (double) 0.25F,
					extraY,
					extraZ + (double) (this.getRandom().nextFloat() * 0.5F) - (double) 0.25F,
					0F, -0.1F, 0F);
		}
		if (this.useRangedAttack && this.getAnimation() == ANIMATION_RANGED && this.getAnimationTick() == 6) {
			float radius = -3.4F;
			float angle = (0.01745329251F * (this.yBodyRot)) - 160F;
			double extraX = (double) (radius * Mth.sin((float) (Math.PI + angle))) + getX();
			double extraZ = (double) (radius * Mth.cos(angle)) + getZ();
			double extraY = 2.4F + getY();
			double targetRelativeX = (target == null ? this.getViewVector(1.0F).x : target.getX()) - extraX;
			double targetRelativeY = (target == null ? this.getViewVector(1.0F).y : target.getY()) - extraY;
			double targetRelativeZ = (target == null ? this.getViewVector(1.0F).z : target.getZ()) - extraZ;
			GolemBeam beam = new GolemBeam(RatlantisEntityRegistry.RATLANTEAN_AUTOMATON_BEAM.get(), this.getLevel(), this);
			beam.setPos(extraX, extraY, extraZ);
			beam.shoot(targetRelativeX, targetRelativeY, targetRelativeZ, 2.0F, 0.1F);
			this.playSound(RatsSoundRegistry.LASER.get(), 1.0F, 0.75F + this.getRandom().nextFloat() * 0.5F);
			if (!this.getLevel().isClientSide()) {
				this.getLevel().addFreshEntity(beam);
			}
			this.useRangedAttack = this.getRandom().nextBoolean();
		}
		AnimationHandler.INSTANCE.updateAnimations(this);
	}

	@Override
	public Item getUpgradeItem() {
		return RatlantisItemRegistry.RAT_UPGRADE_AUTOMATON_MOUNT.get();
	}
}
