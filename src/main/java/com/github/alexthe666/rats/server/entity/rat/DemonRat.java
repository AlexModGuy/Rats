package com.github.alexthe666.rats.server.entity.rat;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import org.jetbrains.annotations.Nullable;

public class DemonRat extends AbstractRat implements Enemy {

	public static final EntityDataAccessor<Boolean> SOUL_VARIANT = SynchedEntityData.defineId(DemonRat.class, EntityDataSerializers.BOOLEAN);

	public DemonRat(EntityType<? extends AbstractRat> type, Level level) {
		super(type, level);
		this.xpReward = 5;
		this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 16.0F);
		this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, -1.0F);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.getEntityData().define(SOUL_VARIANT, false);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 20.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.35D)
				.add(Attributes.ATTACK_DAMAGE, 2.0D)
				.add(Attributes.FOLLOW_RANGE, 24.0D);
	}

	public static boolean canDemonRatSpawnOn(EntityType<? extends Mob> type, LevelAccessor level, MobSpawnType reason, BlockPos pos, RandomSource random) {
		BlockPos blockpos = pos.below();
		return (level.getDifficulty() != Difficulty.PEACEFUL && !level.getBlockState(blockpos).is(Blocks.NETHER_WART_BLOCK) && level.getBlockState(blockpos).isValidSpawn(level, pos, type)) || reason == MobSpawnType.SPAWNER;
	}

	@Override
	public boolean checkSpawnRules(LevelAccessor accessor, MobSpawnType type) {
		if (type == MobSpawnType.EVENT || type == MobSpawnType.SPAWNER) return super.checkSpawnRules(accessor, type);
		return accessor.getRandom().nextInt(5) == 0;
	}

	@Override
	public boolean isInvulnerableTo(DamageSource source) {
		if (source.is(DamageTypeTags.IS_FIRE)) {
			return true;
		}
		return super.isInvulnerableTo(source);
	}

	@Override
	public boolean doHurtTarget(Entity entity) {
		if (this.getRandom().nextInt(3) == 0) {
			entity.setSecondsOnFire(5);
		}
		return super.doHurtTarget(entity);
	}

	@Override
	public void aiStep() {
		super.aiStep();
		if (this.level().isClientSide() && this.getRandom().nextFloat() < 0.15F) {
			double d2 = this.getRandom().nextGaussian() * 0.0075D;
			double d0 = this.getRandom().nextGaussian() * 0.0075D;
			double d1 = this.getRandom().nextGaussian() * 0.0075D;
			this.level().addParticle(this.isSoulVariant() ? ParticleTypes.SOUL_FIRE_FLAME : ParticleTypes.FLAME, this.getRandomX(0.5D), this.getRandomY() - 0.25D, this.getRandomZ(0.5D), d0, d1, d2);
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putBoolean("SoulVariant", this.isSoulVariant());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		this.setSoulVariant(tag.getBoolean("SoulVariant"));
	}

	@Override
	protected boolean shouldDespawnInPeaceful() {
		return true;
	}

	@Override
	public double getMyRidingOffset() {
		return 0.45D;
	}

	@Nullable
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficulty, MobSpawnType type, @Nullable SpawnGroupData data, @Nullable CompoundTag tag) {
		data = super.finalizeSpawn(accessor, difficulty, type, data, tag);
		this.setSoulVariant(accessor.getBiome(this.blockPosition()).is(Biomes.SOUL_SAND_VALLEY));
		return data;
	}

	public boolean isSoulVariant() {
		return this.getEntityData().get(SOUL_VARIANT);
	}

	public void setSoulVariant(boolean soul) {
		this.getEntityData().set(SOUL_VARIANT, soul);
	}

	@Override
	public boolean isBaby() {
		return false;
	}

	@Override
	public boolean isTame() {
		return false;
	}

	@Override
	protected boolean isVisuallySitting() {
		return false;
	}

	@Override
	public boolean isHoldingItemInHands() {
		return false;
	}
}
