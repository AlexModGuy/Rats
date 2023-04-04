package com.github.alexthe666.rats.server.entity.ratlantis;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.rats.data.ratlantis.tags.RatlantisBlockTags;
import com.github.alexthe666.rats.registry.RatsSoundRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

public class FeralRatlantean extends Monster implements IAnimatedEntity, Ratlanteans {

	public static final Animation ANIMATION_BITE = Animation.create(15);
	public static final Animation ANIMATION_SLASH = Animation.create(25);
	public static final Animation ANIMATION_SNIFF = Animation.create(20);
	private static final EntityDataAccessor<Boolean> TOGA = SynchedEntityData.defineId(FeralRatlantean.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Integer> COLOR_VARIANT = SynchedEntityData.defineId(FeralRatlantean.class, EntityDataSerializers.INT);
	private int animationTick;
	private Animation currentAnimation;

	public FeralRatlantean(EntityType<? extends Monster> type, Level level) {
		super(type, level);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new FloatGoal(this));
		this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, true));
		this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, false, false));
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 80.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.5D)
				.add(Attributes.ATTACK_DAMAGE, 5.0D)
				.add(Attributes.FOLLOW_RANGE, 16.0D)
				.add(Attributes.ARMOR, 4.0D);
	}

	@Override
	public boolean doHurtTarget(Entity entity) {
		if (this.getAnimation() == NO_ANIMATION) {
			this.setAnimation(this.getRandom().nextBoolean() ? ANIMATION_SLASH : ANIMATION_BITE);
		}
		return true;
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.getEntityData().define(TOGA, true);
		this.getEntityData().define(COLOR_VARIANT, 0);
	}

	@Override
	public void aiStep() {
		super.aiStep();
		AnimationHandler.INSTANCE.updateAnimations(this);
		if (!this.getLevel().isClientSide()) {
			if (this.getTarget() != null && this.distanceToSqr(this.getTarget()) < 3.0D && this.hasLineOfSight(this.getTarget())) {
				if (this.getTarget().hurt(this.damageSources().mobAttack(this), (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE))) {
					if (this.getAnimation() == NO_ANIMATION) {
						this.setAnimation(this.getRandom().nextBoolean() ? ANIMATION_BITE : ANIMATION_SLASH);
					}
					this.lookAt(this.getTarget(), 360, 80);
					this.doExtraEffect(this.getTarget());
					this.getTarget().knockback(0.25F, this.getX() - this.getTarget().getX(), this.getZ() - this.getTarget().getZ());
				}
			}
			if (!this.isNoAi()) {
				if (this.getTarget() == null && this.getRandom().nextInt(150) == 0 && this.getAnimation() == NO_ANIMATION) {
					this.setAnimation(ANIMATION_SNIFF);
				}
			}
		}
	}

	@Override
	public EntityDimensions getDimensions(Pose pose) {
		if (this.getAnimation() == ANIMATION_SLASH || this.getAnimation() == ANIMATION_SNIFF) {
			return EntityDimensions.fixed(1.85F, 2.25F);
		}
		return super.getDimensions(pose);
	}

	public void doExtraEffect(LivingEntity target) {
	}

	public int getColorVariant() {
		return this.getEntityData().get(COLOR_VARIANT);
	}

	public void setColorVariant(int color) {
		this.getEntityData().set(COLOR_VARIANT, color);
	}

	public void setToga(boolean plague) {
		this.getEntityData().set(TOGA, plague);
	}

	public boolean hasToga() {
		return this.getEntityData().get(TOGA);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putInt("ColorVariant", this.getColorVariant());
		compound.putBoolean("Toga", this.hasToga());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.setToga(compound.getBoolean("Toga"));
		this.setColorVariant(compound.getInt("ColorVariant"));
	}

	@Nullable
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
		spawnDataIn = super.finalizeSpawn(level, difficultyIn, reason, spawnDataIn, dataTag);
		this.setColorVariant(this.getRandom().nextInt(4));
		this.setToga(true);
		return spawnDataIn;
	}

	@Override
	public int getAnimationTick() {
		return this.animationTick;
	}

	@Override
	public void setAnimationTick(int tick) {
		this.animationTick = tick;
	}

	@Override
	public Animation getAnimation() {
		return this.currentAnimation;
	}

	@Override
	public void setAnimation(Animation animation) {
		this.currentAnimation = animation;
		this.refreshDimensions();
	}

	@Override
	public Animation[] getAnimations() {
		return new Animation[]{ANIMATION_BITE, ANIMATION_SLASH, ANIMATION_SNIFF};
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return RatsSoundRegistry.RAT_BEAST_GROWL.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return RatsSoundRegistry.RAT_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return RatsSoundRegistry.RAT_DIE.get();
	}

	@Override
	public float getVoicePitch() {
		return super.getVoicePitch() * 0.4F;
	}

	public static boolean checkRatlanteanSpawnRules(EntityType<? extends Mob> entityType, LevelAccessor world, MobSpawnType reason, BlockPos pos, RandomSource rand) {
		return rand.nextInt(8) == 0 && canSpawnAtPos(world, pos) && Mob.checkMobSpawnRules(entityType, world, reason, pos, rand);
	}

	private static boolean canSpawnAtPos(LevelAccessor world, BlockPos pos) {
		return !world.getBlockState(pos.below()).is(RatlantisBlockTags.PIRAT_ONLY_BLOCKS);
	}
}
