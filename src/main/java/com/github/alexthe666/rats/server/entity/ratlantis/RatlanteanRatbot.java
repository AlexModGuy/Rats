package com.github.alexthe666.rats.server.entity.ratlantis;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.rats.data.ratlantis.tags.RatlantisBlockTags;
import com.github.alexthe666.rats.registry.RatlantisEntityRegistry;
import com.github.alexthe666.rats.registry.RatsSoundRegistry;
import com.github.alexthe666.rats.server.entity.ai.goal.RatbotStrifeGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class RatlanteanRatbot extends Monster implements IAnimatedEntity, Ratlanteans {

	public static final Animation ANIMATION_SHOOT = Animation.create(15);
	private int animationTick;
	private Animation currentAnimation;
	public int walkTick;
	public int prevWalkTick;
	private int rangedAttackCooldownLaser = 0;

	public RatlanteanRatbot(EntityType<? extends Monster> type, Level level) {
		super(type, level);
	}

	protected void registerGoals() {
		this.goalSelector.addGoal(1, new FloatGoal(this));
		this.goalSelector.addGoal(2, new RatbotStrifeGoal(this, 1.0D, 15.0F));
		this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this, RatlanteanRatbot.class));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, false, false));
	}


	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 40.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.15D)
				.add(Attributes.ATTACK_DAMAGE, 5.0D)
				.add(Attributes.FOLLOW_RANGE, 24.0D)
				.add(Attributes.ARMOR, 7.0D);
	}


	public boolean doHurtTarget(Entity entity) {
		if (this.getAnimation() == NO_ANIMATION) {
			this.setAnimation(ANIMATION_SHOOT);
		}
		return true;
	}

	public void aiStep() {
		super.aiStep();
		this.prevWalkTick = this.walkTick;
		if (rangedAttackCooldownLaser == 0 && this.getTarget() != null && this.hasLineOfSight(this.getTarget())) {
			rangedAttackCooldownLaser = 40;
			float radius = 1.1F;
			float angle = (0.01745329251F * (this.yBodyRot));
			double extraX = (double) (radius * Mth.sin((float) (Math.PI + angle))) + getX();
			double extraZ = (double) (radius * Mth.cos(angle)) + getZ();
			double extraY = 0.4 + getY();
			double targetRelativeX = this.getTarget().getX() - extraX;
			double targetRelativeY = this.getTarget().getY() + this.getTarget().getBbHeight() / 2 - extraY;
			double targetRelativeZ = this.getTarget().getZ() - extraZ;
			this.playSound(RatsSoundRegistry.LASER.get(), 1.0F, 0.75F + this.random.nextFloat() * 0.5F);
			LaserBeam beam = new LaserBeam(RatlantisEntityRegistry.LASER_BEAM.get(), this.getLevel(), this);
			beam.setRGB(1.0F, 0.0F, 0.0F);
			beam.setBaseDamage(2.0F);
			beam.setPos(extraX, extraY, extraZ);
			beam.shoot(targetRelativeX, targetRelativeY, targetRelativeZ, 2.0F, 0.4F);
			if (!this.getLevel().isClientSide()) {
				this.getLevel().addFreshEntity(beam);
			}
		}
		AnimationHandler.INSTANCE.updateAnimations(this);
		if (this.walkAnimation.speed() > 0.1F) {
			this.walkTick++;
		}
		if (rangedAttackCooldownLaser > 0) {
			rangedAttackCooldownLaser--;
		}
	}

	@Nullable
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
		spawnDataIn = super.finalizeSpawn(level, difficultyIn, reason, spawnDataIn, dataTag);
		return spawnDataIn;
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
		return new Animation[]{ANIMATION_SHOOT};
	}

	protected SoundEvent getAmbientSound() {
		return RatsSoundRegistry.RATLANTEAN_RATBOT_IDLE.get();
	}

	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return RatsSoundRegistry.RATBOT_HURT.get();
	}

	protected SoundEvent getDeathSound() {
		return RatsSoundRegistry.RATBOT_DEATH.get();
	}

	protected void playStepSound(BlockPos pos, BlockState blockIn) {
		this.playSound(SoundEvents.IRON_GOLEM_STEP, 1.0F, 1.0F);
	}

	public static boolean checkBotSpawnRule(EntityType<? extends Mob> entityType, LevelAccessor world, MobSpawnType reason, BlockPos pos, RandomSource rand) {
		return rand.nextInt(8) == 0 && canSpawnAtPos(world, pos) && Mob.checkMobSpawnRules(entityType, world, reason, pos, rand);
	}

	private static boolean canSpawnAtPos(LevelAccessor world, BlockPos pos) {
		return !world.getBlockState(pos.below()).is(RatlantisBlockTags.PIRAT_ONLY_BLOCKS);
	}
}
