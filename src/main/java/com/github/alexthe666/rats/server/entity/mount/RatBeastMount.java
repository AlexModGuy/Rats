package com.github.alexthe666.rats.server.entity.mount;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.registry.RatsSoundRegistry;
import com.github.alexthe666.rats.server.entity.rat.AbstractRat;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

public class RatBeastMount extends RatMountBase implements IAnimatedEntity {

	public static final Animation ANIMATION_BITE = Animation.create(15);
	public static final Animation ANIMATION_SLASH = Animation.create(25);
	public static final Animation ANIMATION_SNIFF = Animation.create(20);
	private static final EntityDataAccessor<Integer> COLOR_VARIANT = SynchedEntityData.defineId(RatBeastMount.class, EntityDataSerializers.INT);
	private int animationTick;
	private Animation currentAnimation;

	public RatBeastMount(EntityType<? extends PathfinderMob> type, Level level) {
		super(type, level);
		this.setMaxUpStep(1.0F);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 80.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.4D)
				.add(Attributes.ATTACK_DAMAGE, 1.0D)
				.add(Attributes.FOLLOW_RANGE, 16.0D)
				.add(Attributes.ARMOR, 5.0D);
	}


	public boolean doHurtTarget(Entity entity) {
		if (this.getAnimation() == NO_ANIMATION) {
			this.setAnimation(this.getRandom().nextBoolean() ? ANIMATION_SLASH : ANIMATION_BITE);
		}
		return true;
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.getEntityData().define(COLOR_VARIANT, 0);
	}


	public void aiStep() {
		super.aiStep();
		AnimationHandler.INSTANCE.updateAnimations(this);
		float idleSpeed = 0.3F;
		float idleDegree = 0.015F;
		float walkSpeed = 0.4F;
		float walkDegree = 0.1F;
		float bob = -(float) (Math.sin(tickCount * idleSpeed) * (double) idleDegree - (double) (idleDegree));
		float bob_walk = -(float) (Math.sin(this.walkAnimation.position() * walkSpeed) * this.walkAnimation.speed() * (double) walkDegree - (double) (walkDegree));
		this.riderY = 0.8F + bob + bob_walk;
		this.riderXZ = 0.1F;
		if (this.getAnimation() == ANIMATION_SNIFF || this.getAnimation() == ANIMATION_SLASH) {
			float max = 0.75F;
			float jumpAdd = max;
			if (this.getAnimationTick() < 5) {
				jumpAdd = this.getAnimationTick() * (max / 3F);
			}
			if (this.getAnimationTick() > 15) {
				jumpAdd = (20 - this.getAnimationTick()) * (max / 10F);
			}
			this.riderY += jumpAdd;
		}
		if (this.getTarget() != null && this.distanceTo(this.getTarget()) < 5 && this.hasLineOfSight(this.getTarget()) && this.getTarget().isAlive()) {
			this.lookAt(this.getTarget(), 360, 80);
			if (this.getAnimation() == ANIMATION_BITE && (this.getAnimationTick() > 8 && this.getAnimationTick() < 12)) {
				this.getTarget().hurt(this.damageSources().mobAttack(this), 5);
				this.getTarget().knockback(0.25F, this.getX() - this.getTarget().getX(), this.getZ() - this.getTarget().getZ());
			}
			if (this.getAnimation() == ANIMATION_SLASH && (this.getAnimationTick() == 8 || this.getAnimationTick() == 16)) {
				this.getTarget().hurt(this.damageSources().mobAttack(this), 5);
				this.getTarget().knockback(0.25F, this.getX() - this.getTarget().getX(), this.getZ() - this.getTarget().getZ());
			}
		}
		if (!this.level().isClientSide() && this.getTarget() == null && this.getRandom().nextInt(150) == 0 && this.getAnimation() == NO_ANIMATION) {
			this.setAnimation(ANIMATION_SNIFF);
		}
	}

	@Override
	public EntityDimensions getDimensions(Pose pose) {
		if (this.getAnimation() == ANIMATION_SLASH || this.getAnimation() == ANIMATION_SNIFF) {
			return EntityDimensions.fixed(1.85F, 2.25F);
		}
		return super.getDimensions(pose);
	}

	public int getColorVariant() {
		return this.getEntityData().get(COLOR_VARIANT);
	}

	public void setColorVariant(int color) {
		this.getEntityData().set(COLOR_VARIANT, color);
	}

	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putInt("ColorVariant", this.getColorVariant());
	}

	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.setColorVariant(compound.getInt("ColorVariant"));
	}

	@Override
	protected int calculateFallDamage(float dist, float multiplier) {
		return super.calculateFallDamage(dist, multiplier) - 5;
	}

	@Nullable
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
		spawnData = super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
		this.setColorVariant(this.getRandom().nextInt(4));
		return spawnData;
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
		this.refreshDimensions();
	}

	@Override
	public Animation[] getAnimations() {
		return new Animation[]{ANIMATION_BITE, ANIMATION_SLASH, ANIMATION_SNIFF};
	}

	protected SoundEvent getAmbientSound() {
		return RatsSoundRegistry.RAT_BEAST_GROWL.get();
	}

	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return RatsSoundRegistry.RAT_HURT.get();
	}

	protected SoundEvent getDeathSound() {
		return RatsSoundRegistry.RAT_DIE.get();
	}

	public float getVoicePitch() {
		return super.getVoicePitch() * 0.4F;
	}

	@Override
	public Item getUpgradeItem() {
		return RatsItemRegistry.RAT_UPGRADE_BEAST_MOUNT.get();
	}

	@Override
	public void adjustRatTailRotation(AbstractRat rat, AdvancedModelBox upperTail, AdvancedModelBox lowerTail) {
		this.progressRotation(upperTail, rat.sitProgress, 1.3F, 0.0F, 0.0F, 20.0F);
		this.progressRotation(lowerTail, rat.sitProgress, -0.2F, 0.0F, 0.0F, 20.0F);
	}
}
