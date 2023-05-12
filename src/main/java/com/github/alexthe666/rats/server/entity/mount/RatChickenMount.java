package com.github.alexthe666.rats.server.entity.mount;

import com.github.alexthe666.rats.registry.RatsItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class RatChickenMount extends RatMountBase {

	public float wingRotation;
	public float destPos;
	public float oFlapSpeed;
	public float oFlap;
	public float wingRotDelta = 1.0F;

	public RatChickenMount(EntityType<? extends PathfinderMob> type, Level level) {
		super(type, level);
		this.riderY = 0.7F;
		this.riderXZ = 0.1F;
	}

	@Override
	public float getStepHeight() {
		return 1.0F;
	}

	@Override
	protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
		return this.isBaby() ? dimensions.height * 0.85F : dimensions.height * 0.92F;
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 4.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.3D);
	}

	public boolean doHurtTarget(Entity entity) {
		return entity.hurt(this.damageSources().mobAttack(this), (float) (1 + this.getRandom().nextInt(2)));
	}

	public void aiStep() {
		super.aiStep();
		this.oFlap = this.wingRotation;
		this.oFlapSpeed = this.destPos;
		this.destPos = (float) ((double) this.destPos + (double) (this.isOnGround() ? -1 : 4) * 0.3D);
		this.destPos = Mth.clamp(this.destPos, 0.0F, 1.0F);
		if (!this.isOnGround() && this.wingRotDelta < 1.0F) {
			this.wingRotDelta = 1.0F;
		}

		this.wingRotDelta = (float) ((double) this.wingRotDelta * 0.9D);
		Vec3 vec3d = this.getDeltaMovement();
		if (!this.isOnGround() && vec3d.y < 0.0D) {
			this.setDeltaMovement(vec3d.multiply(1.0D, 0.6D, 1.0D));
		}

		this.wingRotation += this.wingRotDelta * 2.0F;
	}

	protected SoundEvent getAmbientSound() {
		return SoundEvents.CHICKEN_AMBIENT;
	}

	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.CHICKEN_HURT;
	}

	protected SoundEvent getDeathSound() {
		return SoundEvents.CHICKEN_DEATH;
	}

	protected void playStepSound(BlockPos pos, BlockState state) {
		this.playSound(SoundEvents.CHICKEN_STEP, 0.15F, 1.0F);
	}

	@Override
	public Item getUpgradeItem() {
		return RatsItemRegistry.RAT_UPGRADE_CHICKEN_MOUNT.get();
	}
}
