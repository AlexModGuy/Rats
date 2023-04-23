package com.github.alexthe666.rats.server.entity.monster;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.registry.RatsEntityRegistry;
import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.registry.RatsSoundRegistry;
import com.github.alexthe666.rats.server.entity.RatSummoner;
import com.github.alexthe666.rats.server.entity.ai.goal.PiperStrifeGoal;
import com.github.alexthe666.rats.server.entity.rat.AbstractRat;
import com.github.alexthe666.rats.server.entity.rat.Rat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.Nullable;

public class PiedPiper extends Raider implements RatSummoner {

	private static final EntityDataAccessor<Integer> RAT_COUNT = SynchedEntityData.defineId(PiedPiper.class, EntityDataSerializers.INT);
	private int ratCooldown = 0;
	private int fluteTicks = 0;

	public PiedPiper(EntityType<? extends Raider> type, Level world) {
		super(type, world);
		this.xpReward = 10;
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 20.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.3D)
				.add(Attributes.ATTACK_DAMAGE, 1.0D)
				.add(Attributes.FOLLOW_RANGE, 16.0D);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.getEntityData().define(RAT_COUNT, 0);
	}

	@Override
	public void applyRaidBuffs(int wave, boolean alwaysFalseIdk) {

	}

	@Override
	public boolean canBeLeader() {
		return false;
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(3, new PiperStrifeGoal(this, 1.0D, 15.0F));
		this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 0.6D));
		this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
		this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Mob.class, 8.0F));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this, Rat.class, Raider.class));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
	}

	@Override
	public boolean checkSpawnRules(LevelAccessor accessor, MobSpawnType type) {
		if (!accessor.getLevelData().getGameRules().getBoolean(RatsMod.SPAWN_PIPERS)) return false;
		if (type == MobSpawnType.EVENT || type == MobSpawnType.SPAWNER) return super.checkSpawnRules(accessor, type);
		int spawnRoll = RatConfig.piperSpawnDecrease;
		if (spawnRoll == 0 || accessor.getRandom().nextInt(spawnRoll) == 0) {
			return super.checkSpawnRules(accessor, type);
		}
		return false;
	}

	@Override
	public void remove(RemovalReason reason) {
		double dist = 20F;
		if (reason.shouldDestroy()) {
			for (Rat rat : this.getLevel().getEntitiesOfClass(Rat.class, new AABB(this.getX() - dist, this.getY() - dist, this.getZ() - dist, this.getX() + dist, this.getY() + dist, this.getZ() + dist))) {
				if (rat.isOwnedBy(this)) {
					rat.setTame(false);
					rat.setOwnerUUID(null);
					rat.setFleePos(rat.blockPosition());
					rat.setTarget(null);
					rat.setLastHurtByMob(null);
				}
			}
		}
		super.remove(reason);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putInt("RatsSummoned", this.getRatsSummoned());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.setRatsSummoned(compound.getInt("RatsSummoned"));
	}

	@Override
	public boolean encirclesSummoner() {
		return false;
	}

	@Override
	public boolean reabsorbRats() {
		return false;
	}

	@Override
	public float reabsorbedRatHealAmount() {
		return 0.0F;
	}

	public int getRatsSummoned() {
		return this.getEntityData().get(RAT_COUNT);
	}

	public void setRatsSummoned(int count) {
		this.getEntityData().set(RAT_COUNT, count);
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
		spawnData = super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
		this.populateDefaultEquipmentSlots(level.getRandom(), difficulty);
		this.populateDefaultEquipmentEnchantments(level.getRandom(), difficulty);
		return spawnData;
	}

	@Override
	public SoundEvent getCelebrateSound() {
		return SoundEvents.VINDICATOR_CELEBRATE;
	}

	@Override
	protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
		this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(RatsItemRegistry.RAT_FLUTE.get()));
	}

	public void summonRat() {
		if (!this.getLevel().isClientSide()) {
			if (this.getRatsSummoned() < 6 && this.ratCooldown == 0) {
				this.getLevel().broadcastEntityEvent(this, (byte) 82);
				Rat rat = new Rat(RatsEntityRegistry.RAT.get(), this.getLevel());
				ForgeEventFactory.onFinalizeSpawn(rat, (ServerLevel) this.getLevel(), this.getLevel().getCurrentDifficultyAt(this.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
				rat.copyPosition(this);
				this.getLevel().addFreshEntity(rat);
				rat.setPlagued(false);
				rat.setTame(false);
				rat.setOwnerUUID(this.getUUID());
				if (this.getTarget() != null) {
					rat.setTarget(this.getTarget());
				}
				this.setRatsSummoned(this.getRatsSummoned() + 1);
				this.playSound(RatsSoundRegistry.getFluteSound(), 0.5F, 1);
				this.ratCooldown = 150;
			}
		}
	}

	@Override
	public void aiStep() {
		super.aiStep();
		if (!this.isNoAi() && this.isHolding(RatsItemRegistry.RAT_FLUTE.get())) {
			if (this.ratCooldown > 0) {
				this.ratCooldown--;
			}
			if (this.fluteTicks % 157 == 0) {
				this.playSound(RatsSoundRegistry.PIPER_LOOP.get(), 0.5F, 1);
			}
			this.fluteTicks++;
			if (this.fluteTicks % 10 == 0) {
				this.getLevel().broadcastEntityEvent(this, (byte) 83);
			}

			if (this.getRatsSummoned() < 6 && this.ratCooldown == 0 && this.tickCount > 20) {
				this.summonRat();
			}
		}
	}

	public static boolean canPiperSpawnInLight(LevelAccessor level, BlockPos pos, RandomSource randomIn) {
		return level.getDifficulty() != Difficulty.PEACEFUL && isDarkEnoughToSpawn((ServerLevelAccessor) level, pos, randomIn);
	}

	public static boolean checkPiperSpawnRules(EntityType<? extends Mob> type, ServerLevelAccessor level, MobSpawnType reason, BlockPos pos, RandomSource random) {
		BlockPos blockpos = pos.below();
		boolean light = canPiperSpawnInLight(level, pos, random);
		return reason == MobSpawnType.SPAWNER || light && level.getBlockState(blockpos).isValidSpawn(level, blockpos, type) && random.nextFloat() < 0.25F;
	}

	@Override
	public void handleEntityEvent(byte id) {
		if (id == 82) {
			this.playEffect(0);
		} else if (id == 83) {
			this.playEffect(1);
		} else {
			super.handleEntityEvent(id);
		}
	}

	protected void playEffect(int type) {
		ParticleOptions p = ParticleTypes.NOTE;
		if (type == 1) {
			double d0 = 0.0;
			this.getLevel().addParticle(p, this.getX() + (double) (this.getRandom().nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), this.getY() + 0.5D + (this.getRandom().nextFloat() * this.getBbHeight()), this.getZ() + (double) (this.getRandom().nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), d0, 0, 0);
		} else {
			double d0 = 0.65;
			for (int i = 0; i < 9; ++i) {
				this.getLevel().addParticle(p, this.getX() + (double) (this.getRandom().nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), this.getY() + 0.5D + (this.getRandom().nextFloat() * this.getBbHeight()), this.getZ() + (double) (this.getRandom().nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), d0, 0, 0);
			}
		}
	}

	@Override
	protected void dropCustomDeathLoot(DamageSource source, int looting, boolean playerKill) {
		super.dropCustomDeathLoot(source, looting, playerKill);
		if (source.getEntity() instanceof AbstractRat) {
			if (this.getRandom().nextBoolean()) {
				this.spawnAtLocation(RatsItemRegistry.MUSIC_DISC_MICE_ON_VENUS.get(), 1);
			} else {
				this.spawnAtLocation(RatsItemRegistry.MUSIC_DISC_LIVING_MICE.get(), 1);
			}
		}
	}

	@Override
	protected SoundEvent getDeathSound() {
		return RatsSoundRegistry.PIED_PIPER_DEATH.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return RatsSoundRegistry.PIED_PIPER_HURT.get();
	}
}
