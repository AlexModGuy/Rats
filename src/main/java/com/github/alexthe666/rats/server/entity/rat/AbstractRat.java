package com.github.alexthe666.rats.server.entity.rat;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.registry.RatlantisItemRegistry;
import com.github.alexthe666.rats.registry.RatsEffectRegistry;
import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.registry.RatsSoundRegistry;
import com.github.alexthe666.rats.server.entity.*;
import com.github.alexthe666.rats.server.entity.ai.goal.*;
import com.github.alexthe666.rats.server.entity.ai.navigation.control.RatMoveControl;
import com.github.alexthe666.rats.server.entity.ratlantis.RatBaronPlane;
import com.github.alexthe666.rats.server.entity.ratlantis.RatBiplaneMount;
import com.github.alexthe666.rats.server.entity.ratlantis.RattlingGun;
import com.github.alexthe666.rats.server.misc.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

public abstract class AbstractRat extends TamableAnimal implements IAnimatedEntity {

	public static final Animation ANIMATION_EAT = Animation.create(10);
	public static final Animation ANIMATION_IDLE_SCRATCH = Animation.create(25);
	public static final Animation ANIMATION_IDLE_SNIFF = Animation.create(20);
	public static final Animation ANIMATION_DANCE = Animation.create(35);

	private static final EntityDataAccessor<Boolean> IS_MALE = SynchedEntityData.defineId(AbstractRat.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(AbstractRat.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> SLEEPING = SynchedEntityData.defineId(AbstractRat.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<String> COLOR_VARIANT = SynchedEntityData.defineId(AbstractRat.class, EntityDataSerializers.STRING);
	private static final EntityDataAccessor<Boolean> DEAD_IN_TRAP = SynchedEntityData.defineId(AbstractRat.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Optional<BlockPos>> FLEE_POS = SynchedEntityData.defineId(AbstractRat.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);

	public float sitProgress;
	public float deadInTrapProgress;
	public float sleepProgress;
	private int animationTick;
	protected int eatingTicks = 0;
	public int raidCooldown = 1200;
	private Animation currentAnimation;
	private RatStatus status = RatStatus.IDLE;

	protected AbstractRat(EntityType<? extends TamableAnimal> type, Level level) {
		super(type, level);
		this.moveControl = new RatMoveControl(this);
		this.setPathfindingMalus(BlockPathTypes.RAIL, 0.0F);
		this.setPathfindingMalus(BlockPathTypes.UNPASSABLE_RAIL, 0.0F);
	}

	@Override
	public float getStepHeight() {
		return 1.75F;
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 8.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.35D)
				.add(Attributes.FLYING_SPEED, 0.35D)
				.add(Attributes.ATTACK_DAMAGE, 1.0D)
				.add(Attributes.FOLLOW_RANGE, 12.0D);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new RatEnterTrapGoal(this));
		this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Ocelot.class, 10.0F, 0.8D, 1.5D, entity -> !entity.isSteppingCarefully()));
		this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Cat.class, 10.0F, 0.8D, 1.5D, entity -> !entity.isSteppingCarefully()));
		this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Fox.class, 10.0F, 0.8D, 1.3D));
		this.goalSelector.addGoal(3, new FleeSunGoal(this, 1.5D));
		this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.25D, true));
		this.goalSelector.addGoal(6, new RatFleePositionGoal(this));
		this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.225D));
		this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, LivingEntity.class, 6.0F));
		this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(0, new WildRatTargetFoodGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this, Cat.class, Ocelot.class, Fox.class));
		this.targetSelector.addGoal(2, new OwnerHurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
	}

	@Override
	public void aiStep() {
		super.aiStep();
		this.setRatStatus(RatStatus.IDLE);
		if (this.getTarget() != null && !this.getTarget().isAlive()) {
			this.setTarget(null);
		}

		if (this.raidCooldown > 0 && !this.hasFleePos()) {
			this.raidCooldown--;
		}

		if (this.isMoving()) {
			this.setRatStatus(RatStatus.MOVING);
		}
		if (!this.isNoAi()) {
			if (!this.getLevel().isClientSide() && this.getRatStatus() == RatStatus.IDLE && this.getItemInHand(InteractionHand.MAIN_HAND).isEmpty() && this.getAnimation() == NO_ANIMATION && this.getRandom().nextInt(350) == 0 && this.shouldPlayIdleAnimations()) {
				this.setAnimation(this.getRandom().nextBoolean() ? ANIMATION_IDLE_SNIFF : ANIMATION_IDLE_SCRATCH);
			}
		}
		if (this.getAnimation() == ANIMATION_EAT && this.isHoldingFood()) {
			this.eatingTicks++;
			this.eatItem(this.getItemInHand(InteractionHand.MAIN_HAND));
			if (this.eatingTicks >= 40) {
				this.onItemEaten();
				int healAmount = 1;
				if (this.getItemInHand(InteractionHand.MAIN_HAND).getItem().isEdible()) {
					healAmount = Objects.requireNonNull(this.getItemInHand(InteractionHand.MAIN_HAND).getItem().getFoodProperties(this.getItemInHand(InteractionHand.MAIN_HAND), this)).getNutrition();
				}
				this.heal(healAmount);
				this.eatingTicks = 0;
			}
		}

		if (!this.getLevel().isClientSide()) {
			if (this.isEating()) {
				this.setAnimation(ANIMATION_EAT);
				this.setRatStatus(RatStatus.EATING);
			}
		}
		AnimationHandler.INSTANCE.updateAnimations(this);
		float sitInc = this.getAnimation() == ANIMATION_IDLE_SCRATCH || this.getAnimation() == ANIMATION_IDLE_SNIFF ? 5.0F : 1.0F;
		if (this.sleepProgress <= 0.0F) {
			if ((this.isVisuallySitting() || this.isHoldingItemInHands()) && this.sitProgress < 20.0F) {
				this.sitProgress += sitInc;
			} else if (!this.isVisuallySitting() && !this.isHoldingItemInHands() && this.sitProgress > 0.0F) {
				this.sitProgress -= sitInc;
			}
		}
		if (this.isDeadInTrap()) {
			this.sitProgress = 0.0F;
			if (this.deadInTrapProgress < 5.0F) {
				this.deadInTrapProgress += 1.0F;
			}
		}
		if (this.getLevel().getBlockState(this.blockPosition()).is(BlockTags.BEDS) && this.isOrderedToSit()) {
			this.sitProgress = 0.0F;
			this.getEntityData().set(SLEEPING, true);
			this.refreshDimensions();
			if (this.sleepProgress < 20.0F) {
				this.sleepProgress += 1.0F;
			}
		} else {
			this.getEntityData().set(SLEEPING, false);
			this.refreshDimensions();
			if (this.sleepProgress > 0.0F) {
				this.sleepProgress -= 1.0F;
			}
		}
		if (!this.getLevel().isClientSide() && this.getOwner() instanceof RatSummoner summoner && this.getOwner() instanceof Mob mob) {
			if (mob.getTarget() != null) {
				this.setTarget(mob.getTarget());
			}
			if (summoner.reabsorbRats()) {
				if (this.getTarget() == null || !this.getTarget().isAlive()) {
					this.getNavigation().moveTo(mob, 1.225F);
				}
				if (this.distanceToSqr(mob) < mob.getBbWidth()) {
					this.discard();
					mob.heal(summoner.reabsorbedRatHealAmount());
					summoner.setRatsSummoned(summoner.getRatsSummoned() - 1);
				}
			} else if (summoner.encirclesSummoner()) {
				if (this.getTarget() == null || !this.getTarget().isAlive()) {
					float radius = summoner.getRadius();
					int maxRatStuff = 360 / Math.max(summoner.getRatsSummoned(), 1);
					int ratIndex = this.getId() % Math.max(summoner.getRatsSummoned(), 1);
					float angle = (0.01745329251F * (ratIndex * maxRatStuff + this.tickCount * 4.1F));
					double extraX = (double) (radius * Mth.sin((float) (Math.PI + angle))) + mob.getX();
					double extraZ = (double) (radius * Mth.cos(angle)) + mob.getZ();
					BlockPos runToPos = BlockPos.containing(extraX, mob.getY(), extraZ);
					int steps = 0;
					while (this.getLevel().getBlockState(runToPos).isSolidRender(getLevel(), runToPos) && steps < 10) {
						runToPos = runToPos.above();
						steps++;
					}
					this.getNavigation().moveTo(extraX, runToPos.getY(), extraZ, 1.225F);
				}
			}
		}
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.getEntityData().define(IS_MALE, false);
		this.getEntityData().define(SITTING, false);
		this.getEntityData().define(SLEEPING, false);
		this.getEntityData().define(COLOR_VARIANT, RatVariants.getVariantId(RatVariants.getRandomVariant(this.getRandom(), false)));
		this.getEntityData().define(DEAD_IN_TRAP, false);
		this.getEntityData().define(FLEE_POS, Optional.empty());
	}

	@Override
	public Animation[] getAnimations() {
		return new Animation[]{ANIMATION_EAT, ANIMATION_IDLE_SCRATCH, ANIMATION_IDLE_SNIFF, ANIMATION_DANCE};
	}

	@Override
	public void handleEntityEvent(byte id) {
		if (id == 82) {
			this.playEffect(0);
		} else if (id == 83) {
			this.playEffect(1);
		} else if (id == 84) {
			this.playEffect(2);
		} else if (id == 101) {
			this.playEffect(3);
		} else if (id == 126) {
			this.playEffect(5);
		} else {
			super.handleEntityEvent(id);
		}
	}

	protected void playEffect(int type) {
		if (type == 3) {
			for (int j = 0; j < 5; ++j) {
				double d6 = (double) (j) / 5D;
				float f = (this.getRandom().nextFloat() - 0.5F) * 0.2F;
				float f1 = (this.getRandom().nextFloat() - 0.5F) * 0.2F;
				float f2 = (this.getRandom().nextFloat() - 0.5F) * 0.2F;
				double d3 = this.xo + (this.getX() - this.xo) * d6 + (getRandom().nextDouble() - 0.5D) * this.getBbWidth() * 2.0D;
				double d4 = this.yo + (this.getY() - this.yo) * d6 + getRandom().nextDouble() * (double) this.getBbHeight();
				double d5 = this.zo + (this.getZ() - this.zo) * d6 + (getRandom().nextDouble() - 0.5D) * this.getBbWidth() * 2.0D;
				getLevel().addParticle(ParticleTypes.SPLASH, d3, d4, d5, f, f1, f2);
			}
		} else if (type == 2) {
			for (int j = 0; j < 5; ++j) {
				double d6 = (double) (j) / 5D;
				float f = (this.getRandom().nextFloat() - 0.5F) * 0.2F;
				float f1 = (this.getRandom().nextFloat() - 0.5F) * 0.2F;
				float f2 = (this.getRandom().nextFloat() - 0.5F) * 0.2F;
				double d3 = this.xo + (this.getX() - this.xo) * d6 + (getRandom().nextDouble() - 0.5D) * this.getBbWidth() * 2.0D;
				double d4 = this.yo + (this.getY() - this.yo) * d6 + getRandom().nextDouble() * (double) this.getBbHeight();
				double d5 = this.zo + (this.getZ() - this.zo) * d6 + (getRandom().nextDouble() - 0.5D) * this.getBbWidth() * 2.0D;
				getLevel().addParticle(ParticleTypes.PORTAL, d3, d4, d5, f, f1, f2);
			}
		} else {
			ParticleOptions p = ParticleTypes.SMOKE;

			if (type == 1) {
				p = ParticleTypes.HEART;
			}
			if (type == 5) {
				p = ParticleTypes.ITEM_SNOWBALL;
			}
			for (int i = 0; i < 9; ++i) {
				double d0 = this.getRandom().nextGaussian() * 0.02D;
				double d1 = this.getRandom().nextGaussian() * 0.02D;
				double d2 = this.getRandom().nextGaussian() * 0.02D;
				this.getLevel().addParticle(p, this.getX() + (double) (this.getRandom().nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), this.getY() + 0.5D + (this.getRandom().nextFloat() * this.getBbHeight()), this.getZ() + (double) (this.getRandom().nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), d0, d1, d2);
			}
		}
	}

	public boolean isMale() {
		return this.getEntityData().get(IS_MALE);
	}

	public void setMale(boolean male) {
		this.getEntityData().set(IS_MALE, male);
	}

	private RatStatus getRatStatus() {
		return this.status;
	}

	protected void setRatStatus(RatStatus status) {
		if (this.status.canBeOverriden(this)) {
			this.status = status;
		}
	}

	@Override
	public boolean canBeAffected(MobEffectInstance instance) {
		return instance.getEffect() != RatsEffectRegistry.PLAGUE.get() && super.canBeAffected(instance);
	}

	public RatVariant getColorVariant() {
		return RatVariants.getVariant(this.getEntityData().get(COLOR_VARIANT));
	}

	public void setColorVariant(RatVariant variant) {
		this.getEntityData().set(COLOR_VARIANT, RatVariants.getVariantId(variant));
	}

	public boolean canMove() {
		return this.getAnimation() == NO_ANIMATION && !this.isBaby() && !this.isOrderedToSit();
	}

	public boolean isEating() {
		return this.isHoldingFood() && (this.getRandom().nextInt(20) == 0 || this.eatingTicks > 0) && this.sleepProgress <= 0.0F;
	}

	public void onItemEaten() {
		this.getItemInHand(InteractionHand.MAIN_HAND).shrink(1);
	}

	public boolean isMoving() {
		return Math.abs(this.getDeltaMovement().x()) >= 0.05D || Math.abs(this.getDeltaMovement().z()) >= 0.05D;
	}

	public boolean isHoldingFood() {
		return !this.getItemInHand(InteractionHand.MAIN_HAND).isEmpty() && RatUtils.isRatFood(this.getItemInHand(InteractionHand.MAIN_HAND));
	}

	public boolean shouldPlayIdleAnimations() {
		return !this.isDeadInTrap() && this.isAlive() && this.getAnimation() != Rat.ANIMATION_EAT && !this.getEntityData().get(SLEEPING);
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
	}

	public boolean isDeadInTrap() {
		return this.getEntityData().get(DEAD_IN_TRAP);
	}

	public void setKilledInTrap() {
		this.getEntityData().set(DEAD_IN_TRAP, true);
		this.hurt(this.damageSources().inWall(), Float.MAX_VALUE);
	}

	public boolean hasFleePos() {
		return this.getEntityData().get(FLEE_POS).isPresent();
	}

	@Nullable
	public BlockPos getFleePos() {
		return this.getEntityData().get(FLEE_POS).orElse(null);
	}

	public void setFleePos(@Nullable BlockPos pos) {
		this.getEntityData().set(FLEE_POS, Optional.ofNullable(pos));
	}

	@Override
	public boolean isOrderedToSit() {
		return this.getEntityData().get(SITTING);
	}

	@Override
	public void setOrderedToSit(boolean sit) {
		this.getEntityData().set(SITTING, sit);
		this.refreshDimensions();
	}

	protected boolean isVisuallySitting() {
		return this.isPassenger() || this.getEntityData().get(SITTING) || this.isEating() || this.getAnimation() == ANIMATION_IDLE_SCRATCH || this.getAnimation() == ANIMATION_IDLE_SNIFF;
	}

	public boolean isHoldingItemInHands() {
		return !this.hasFleePos() && this.isEating() && this.sleepProgress <= 0.0F;
	}

	@Override
	public boolean checkSpawnRules(LevelAccessor accessor, MobSpawnType type) {
		return accessor.getLevelData().getGameRules().getBoolean(RatsMod.SPAWN_RATS) && super.checkSpawnRules(accessor, type);
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficulty, MobSpawnType type, @Nullable SpawnGroupData data, @Nullable CompoundTag tag) {
		data = super.finalizeSpawn(accessor, difficulty, type, data, tag);
		this.setColorVariant(RatVariants.getRandomVariant(this.getRandom(), false));
		this.setMale(this.getRandom().nextBoolean());
		if (this.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
			if (RatsDateFetcher.isHalloweenSeason() && this.getRandom().nextFloat() <= 0.25F) {
				this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.PUMPKIN));
				this.setGuaranteedDrop(EquipmentSlot.HEAD);
			}
			if (RatsDateFetcher.isChristmasSeason() && this.getRandom().nextFloat() <= (RatsDateFetcher.isChristmasDay() ? 1.0F : 0.25F)) {
				this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(RatsItemRegistry.SANTA_HAT.get()));
				this.setGuaranteedDrop(EquipmentSlot.HEAD);
			}
			if (RatsDateFetcher.isChristmasDay() && this.getRandom().nextFloat() <= 0.75F) {
				this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(RatsItemRegistry.TINY_COIN.get()));
				this.setGuaranteedDrop(EquipmentSlot.MAINHAND);
			}
			if ((RatsDateFetcher.isNewYearsEve() && this.getRandom().nextFloat() <= 0.25F) || RatsDateFetcher.isAlexsBDay() || RatsDateFetcher.isGizmosBDay()) {
				ItemStack stack = new ItemStack(RatsItemRegistry.PARTY_HAT.get());
				((DyeableLeatherItem)stack.getItem()).setColor(stack, (int) (this.getRandom().nextFloat() * 0xFFFFFF));
				this.setItemSlot(EquipmentSlot.HEAD, stack);
				this.setGuaranteedDrop(EquipmentSlot.HEAD);
			}
		}
		return data;
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putBoolean("IsMale", this.isMale());
		tag.putBoolean("Sitting", this.isOrderedToSit());
		tag.putString("ColorVariant", RatVariants.getVariantId(this.getColorVariant()));
		tag.putInt("RaidCooldown", this.raidCooldown);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		this.setMale(tag.getBoolean("IsMale"));
		this.setOrderedToSit(tag.getBoolean("Sitting"));
		if (tag.contains("ColorVariant", Tag.TAG_INT)) {
			this.setColorVariant(RatUtils.convertOldRatVariant(tag.getInt("ColorVariant")));
			RatsMod.LOGGER.debug("Converted Rat variant for Rat {} from {} to {}.", this.getUUID(), tag.getInt("ColorVariant"), RatVariants.RAT_VARIANT_REGISTRY.get().getKey(RatUtils.convertOldRatVariant(tag.getInt("ColorVariant"))).toString());
		} else if (tag.contains("ColorVariant", Tag.TAG_STRING)) {
			this.setColorVariant(RatVariants.getVariant(tag.getString("ColorVariant")));
		}
		this.raidCooldown = tag.getInt("RaidCooldown");
	}

	@Nullable
	@Override
	public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mob) {
		return null;
	}

	public boolean isDead() {
		return this.dead;
	}

	@Override
	protected void tickDeath() {
		++this.deathTime;
		int maxDeathTime = this.isDeadInTrap() ? 60 : 20;
		if (this.deathTime >= maxDeathTime) {
			this.level.broadcastEntityEvent(this, (byte)60);
			this.handleBeforeRemoval();
			this.remove(RemovalReason.KILLED);
		}
	}

	@Override
	protected void dropEquipment() {
		for (EquipmentSlot slot : EquipmentSlot.values()) {
			if (!this.getItemBySlot(slot).isEmpty()) {
				this.spawnAtLocation(this.getItemBySlot(slot));
			}
		}
	}

	protected void handleBeforeRemoval() {

	}

	@Override
	public boolean removeWhenFarAway(double dist) {
		return RatConfig.ratsSpawnLikeMonsters;
	}

	@Override
	protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
		return this.isOrderedToSit() && this.sleepProgress <= 0.0F ? super.getStandingEyeHeight(pose, dimensions) : dimensions.height * 0.5F;
	}

	@Override
	public void remove(RemovalReason reason) {
		if (reason.shouldDestroy()) {
			if (!this.isAlive() && this.getOwner() instanceof RatSummoner summoner) {
				if (summoner instanceof BlackDeath && !RatConfig.bdConstantRatSpawns) {
					super.remove(reason);
					return;
				}
				if (summoner instanceof RatKing && !RatConfig.ratKingConstantRatSpawns) {
					super.remove(reason);
					return;
				}
				summoner.setRatsSummoned(summoner.getRatsSummoned() - 1);
				this.setOwnerUUID(null);
			}
		}
		super.remove(reason);
	}

	@Nullable
	@Override
	protected SoundEvent getAmbientSound() {
		if (this.getHealth() <= this.getMaxHealth() / 2D || this.isBaby()) {
			return RatsSoundRegistry.RAT_IDLE.get();
		}
		return super.getAmbientSound();
	}

	@Nullable
	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return RatsSoundRegistry.RAT_HURT.get();
	}

	@Nullable
	@Override
	protected SoundEvent getDeathSound() {
		return RatsSoundRegistry.RAT_DIE.get();
	}

	@Override
	public void rideTick() {
		Entity entity = this.getVehicle();
		if (entity != null) {
			if (!entity.isAlive() || entity instanceof LivingEntity living && living.getHealth() <= 0.0F) {
				this.stopRiding();
			} else {
				this.setDeltaMovement(0, 0, 0);
				this.tick();
				if (this.isPassenger()) {
					if (this.getVehicle() instanceof RatMountBase || this.getVehicle() instanceof RatBaronPlane || this.getVehicle() instanceof RattlingGun) {
						this.setYRot(this.yBodyRot);
						super.rideTick();
					} else {
						this.updateRiding(entity);
					}
				}
			}
		}
	}

	public void updateRiding(Entity riding) {
		if (riding.isVehicle() && riding instanceof Player player) {
			int i = riding.getPassengers().indexOf(this);
			float radius = (i == 0 ? 0F : 0.4F) + (player.isFallFlying() ? 2 : 0);
			float angle = (0.01745329251F * player.yBodyRot + (i == 2 ? -92.5F : i == 1 ? 92.5F : 0));
			double extraX = radius * Mth.sin((float) (Math.PI + angle));
			double extraZ = radius * Mth.cos(angle);
			double extraY = (riding.isCrouching() ? 1.1D : 1.4D);
			this.setYRot(player.yHeadRot);
			this.yHeadRot = player.yHeadRot;
			this.yRotO = player.yHeadRot;
			this.setPos(riding.getX() + extraX, riding.getY() + extraY, riding.getZ() + extraZ);
			if (player.isFallFlying()) {
				this.stopRiding();
			}
		}
	}

	@Override
	public void ejectPassengers() {

	}

	protected void eatItem(ItemStack stack) {
		if (!stack.isEmpty()) {
			if (stack.getUseAnimation() == UseAnim.DRINK) {
				this.playSound(RatsSoundRegistry.RAT_DRINK.get(), 0.5F, this.getLevel().getRandom().nextFloat() * 0.1F + 0.9F);
			}
			if (RatUtils.isRatFood(stack) || (this instanceof TamedRat rat && RatUpgradeUtils.hasUpgrade(rat, RatsItemRegistry.RAT_UPGRADE_ORE_DOUBLING.get()))) {
				for (int i = 0; i < 3; ++i) {
					Vec3 vec3d = new Vec3(((double) this.getRandom().nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
					vec3d = vec3d.xRot(-this.getXRot() * 0.017453292F);
					vec3d = vec3d.yRot(-this.getYRot() * 0.017453292F);
					double d0 = (double) (-this.getRandom().nextFloat()) * 0.6D - 0.3D;
					Vec3 vec3d1 = new Vec3(((double) this.getRandom().nextFloat() - 0.5D) * 0.3D, d0, 0.1D);
					vec3d1 = vec3d1.xRot(-this.getXRot() * 0.017453292F);
					vec3d1 = vec3d1.yRot(-this.getYRot() * 0.017453292F);
					vec3d1 = vec3d1.add(this.getX(), this.getY() + 0.25D, this.getZ());
					this.getLevel().addParticle(new ItemParticleOption(ParticleTypes.ITEM, stack), vec3d1.x(), vec3d1.y(), vec3d1.z(), vec3d.x(), vec3d.y() + 0.05D, vec3d.z());
				}
				this.playSound(RatsSoundRegistry.RAT_EAT.get(), 0.25F + 0.25F * (float) this.getRandom().nextInt(2), (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.2F + 1.3F);
			}
		}
	}

	public boolean isValidLightLevel() {
		BlockPos blockpos = BlockPos.containing(this.getX(), this.getBoundingBox().minY, this.getZ());
		if (this.getLevel().getBrightness(LightLayer.SKY, blockpos) > this.getRandom().nextInt(32)) {
			return false;
		} else {
			int i = this.getLevel().isThundering() ? this.getLevel().getMaxLocalRawBrightness(blockpos, 10) : this.getLevel().getMaxLocalRawBrightness(blockpos);
			return i <= this.getRandom().nextInt(8);
		}
	}

	protected static boolean isValidLightLevel(LevelAccessor accessor, RandomSource random, BlockPos pos) {
		if (accessor.getBrightness(LightLayer.SKY, pos) > random.nextInt(32)) {
			return false;
		} else {
			int i = accessor.getLevelData().isThundering() ? accessor.getMaxLocalRawBrightness(pos, 10) : accessor.getMaxLocalRawBrightness(pos);
			return i <= random.nextInt(8);
		}
	}

	public int getTailBehaviorForMount() {
		if (this instanceof TamedRat rat) {
			if (RatUpgradeUtils.hasUpgrade(rat, RatsItemRegistry.RAT_UPGRADE_GOLEM_MOUNT.get()) && rat.isRidingSpecialMount()) {
				return 1;
			}
			if (RatUpgradeUtils.hasUpgrade(rat, RatsItemRegistry.RAT_UPGRADE_CHICKEN_MOUNT.get()) && rat.isRidingSpecialMount()) {
				return 2;
			}
			if (RatUpgradeUtils.hasUpgrade(rat, RatsItemRegistry.RAT_UPGRADE_BEAST_MOUNT.get()) && rat.isRidingSpecialMount()) {
				return 3;
			}
			if (RatUpgradeUtils.hasUpgrade(rat, RatlantisItemRegistry.RAT_UPGRADE_AUTOMATON_MOUNT.get()) && rat.isRidingSpecialMount()) {
				return 4;
			}
		}
		if (this.isPassenger() && this.getVehicle() instanceof RattlingGun) {
			return 5;
		}
		if (this.isPassenger() && this.getVehicle() instanceof RatBaronPlane || this.getVehicle() instanceof RatBiplaneMount) {
			return 3;
		}
		if (this.getVehicle() instanceof Strider) {
			return 2;
		}
		return 0;//normal (down + riding)
	}

	protected ListTag makeDoubleList(double... pNumbers) {
		ListTag listtag = new ListTag();
		for (double d0 : pNumbers) {
			listtag.add(DoubleTag.valueOf(d0));
		}

		return listtag;
	}

	public BlockPos getLightPosition() {
		BlockPos pos = this.blockPosition();
		if (!this.getLevel().getBlockState(pos).canOcclude()) {
			return pos.above();
		}
		return pos;
	}

	@Override
	protected void spawnSprintParticle() {

	}
}
