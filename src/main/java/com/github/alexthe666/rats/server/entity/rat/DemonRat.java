package com.github.alexthe666.rats.server.entity.rat;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import org.jetbrains.annotations.Nullable;

public class DemonRat extends AbstractRat implements Enemy {

	public DemonRat(EntityType<? extends AbstractRat> type, Level level) {
		super(type, level);
		this.xpReward = 5;
		this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 16.0F);
		this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, -1.0F);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
		this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
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
		if (this.getLevel().isClientSide() && this.getRandom().nextFloat() < 0.5F) {
			double d2 = this.getRandom().nextGaussian() * 0.02D;
			double d0 = this.getRandom().nextGaussian() * 0.02D;
			double d1 = this.getRandom().nextGaussian() * 0.02D;
			this.getLevel().addParticle(ParticleTypes.FLAME, this.getX() + (double) (this.getRandom().nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), this.getY(), this.getZ() + (double) (this.getRandom().nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), d0, d1, d2);
		}
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
		this.setMale(this.getRandom().nextBoolean());
		return data;
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
