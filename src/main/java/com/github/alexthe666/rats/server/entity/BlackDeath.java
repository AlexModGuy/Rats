package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.rats.registry.RatsEffectRegistry;
import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.registry.RatsParticleRegistry;
import com.github.alexthe666.rats.registry.RatsSoundRegistry;
import com.github.alexthe666.rats.server.entity.ai.goal.BlackDeathSummonBeastGoal;
import com.github.alexthe666.rats.server.entity.ai.goal.BlackDeathSummonCloudGoal;
import com.github.alexthe666.rats.server.entity.ai.goal.BlackDeathSummonRatGoal;
import com.github.alexthe666.rats.server.entity.ai.goal.BlackDeathSummoningGoal;
import com.github.alexthe666.rats.server.entity.rat.Rat;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

public class BlackDeath extends Monster implements PlagueLegion, RatSummoner {

	private static final EntityDataAccessor<Boolean> IS_SUMMONING = SynchedEntityData.defineId(BlackDeath.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> MELEE_ATTACKING = SynchedEntityData.defineId(BlackDeath.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Integer> RAT_COUNT = SynchedEntityData.defineId(BlackDeath.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> CLOUD_COUNT = SynchedEntityData.defineId(BlackDeath.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> BEAST_COUNT = SynchedEntityData.defineId(BlackDeath.class, EntityDataSerializers.INT);
	private final ServerBossEvent bossInfo = (new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS));
	private int summonTicks = 0;

	public BlackDeath(EntityType<? extends Monster> type, Level level) {
		super(type, level);
		this.xpReward = 50;
		((GroundPathNavigation) this.getNavigation()).setCanOpenDoors(true);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new BlackDeathSummoningGoal(this));
		this.goalSelector.addGoal(2, new BlackDeathSummonRatGoal(this));
		this.goalSelector.addGoal(3, new BlackDeathSummonCloudGoal(this));
		this.goalSelector.addGoal(4, new BlackDeathSummonBeastGoal(this));
		this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0D, true) {
			@Override
			public boolean canUse() {
				return !BlackDeath.this.isSummoning() && super.canUse();
			}

			@Override
			public boolean canContinueToUse() {
				return !BlackDeath.this.isSummoning() && super.canContinueToUse();
			}

			@Override
			public void start() {
				super.start();
				BlackDeath.this.getEntityData().set(MELEE_ATTACKING, true);
			}

			@Override
			public void stop() {
				super.stop();
				BlackDeath.this.getEntityData().set(MELEE_ATTACKING, false);
			}
		});
		this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 0.6D));
		this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
		this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, LivingEntity.class, 8.0F));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this, PlagueLegion.class).setAlertOthers());
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, true, entity -> NOT_PLAGUE.and(EntitySelector.NO_CREATIVE_OR_SPECTATOR).and(living -> living.getMobType() != MobType.UNDEAD).test(entity)));
	}

	protected void defineSynchedData() {
		super.defineSynchedData();
		this.getEntityData().define(IS_SUMMONING, false);
		this.getEntityData().define(MELEE_ATTACKING, false);
		this.getEntityData().define(RAT_COUNT, 0);
		this.getEntityData().define(CLOUD_COUNT, 0);
		this.getEntityData().define(BEAST_COUNT, 0);
	}

	@Override
	public boolean canBeAffected(MobEffectInstance instance) {
		if (instance.getEffect() == RatsEffectRegistry.PLAGUE.get()) {
			return false;
		}
		return super.canBeAffected(instance);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return RatsSoundRegistry.BLACK_DEATH_IDLE.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return RatsSoundRegistry.BLACK_DEATH_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return RatsSoundRegistry.BLACK_DEATH_DIE.get();
	}

	@Override
	public boolean isPushable() {
		return false;
	}

	@Override
	public int getAmbientSoundInterval() {
		return 40;
	}

	@Override
	public boolean removeWhenFarAway(double dist) {
		return false;
	}

	@Override
	public void remove(RemovalReason reason) {
		if (!this.isAlive()) {
			double dist = 20F;
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

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 300.0F)
				.add(Attributes.MOVEMENT_SPEED, 0.25D)
				.add(Attributes.ATTACK_DAMAGE, 4.0F)
				.add(Attributes.FOLLOW_RANGE, 32.0D)
				.add(Attributes.ARMOR, 2.0D);
	}

	@Override
	protected void customServerAiStep() {
		super.customServerAiStep();
		this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());

		if (this.summonTicks > 0 && this.getTarget() != null) {
			--this.summonTicks;
		}
	}

	@Override
	public boolean canChangeDimensions() {
		return false;
	}

	@Override
	protected boolean canRide(Entity entity) {
		return false;
	}

	public boolean isSummoning() {
		return this.getEntityData().get(IS_SUMMONING);
	}

	public void setSummoning(boolean summoning) {
		this.getEntityData().set(IS_SUMMONING, summoning);
	}

	public boolean isMeleeAttacking() {
		return this.getEntityData().get(MELEE_ATTACKING);
	}

	@Override
	public boolean encirclesSummoner() {
		return true;
	}

	@Override
	public boolean reabsorbRats() {
		return false;
	}

	@Override
	public float reabsorbedRatHealAmount() {
		return 0.0F;
	}

	@Override
	public int getRatsSummoned() {
		return this.getEntityData().get(RAT_COUNT);
	}

	@Override
	public void setRatsSummoned(int count) {
		this.getEntityData().set(RAT_COUNT, count);
	}

	@Override
	public float getRadius() {
		return (float) 5 - (float) Math.sin(this.tickCount * 0.4D) * 0.5F;
	}

	public int getCloudsSummoned() {
		return this.getEntityData().get(CLOUD_COUNT);
	}

	public void setCloudsSummoned(int count) {
		this.getEntityData().set(CLOUD_COUNT, count);
	}

	public int getBeastsSummoned() {
		return this.getEntityData().get(BEAST_COUNT);
	}

	public void setBeastsSummoned(int count) {
		this.getEntityData().set(BEAST_COUNT, count);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putInt("SummoningTicks", this.summonTicks);
		compound.putInt("RatsSummoned", this.getRatsSummoned());
		compound.putInt("CloudsSummoned", this.getCloudsSummoned());
		compound.putInt("BeastsSummoned", this.getBeastsSummoned());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		if (this.hasCustomName()) {
			this.bossInfo.setName(this.getDisplayName());
		}
		this.summonTicks = compound.getInt("SummoningTicks");
		this.setRatsSummoned(compound.getInt("RatsSummoned"));
		this.setCloudsSummoned(compound.getInt("CloudsSummoned"));
		this.setBeastsSummoned(compound.getInt("BeastsSummoned"));

	}

	public int getSummonTicks() {
		return this.summonTicks;
	}

	public void setSummonTicks(int ticks) {
		this.summonTicks = ticks;
	}

	@Override
	public void setCustomName(@Nullable Component name) {
		super.setCustomName(name);
		this.bossInfo.setName(this.getDisplayName());
	}

	@Override
	public void startSeenByPlayer(ServerPlayer player) {
		super.startSeenByPlayer(player);
		this.bossInfo.addPlayer(player);
	}

	@Override
	public void stopSeenByPlayer(ServerPlayer player) {
		super.stopSeenByPlayer(player);
		this.bossInfo.removePlayer(player);
	}

	@Override
	public void aiStep() {
		super.aiStep();
		if (this.getLevel().isClientSide() && this.isSummoning()) {
			double d0 = 0;
			double d1 = 0;
			double d2 = 0;
			float f = this.getYRot() * 0.017453292F + Mth.cos((float) this.tickCount * 0.6662F) * 0.25F;
			float f1 = Mth.cos(f);
			float f2 = Mth.sin(f);

			this.getLevel().addParticle(RatsParticleRegistry.BLACK_DEATH.get(), this.getX() + (double) f1 * 0.6D, this.getY() + 1.8D, this.getZ() + (double) f2 * 0.6D, d0, d1, d2);
			this.getLevel().addParticle(RatsParticleRegistry.BLACK_DEATH.get(), this.getX() - (double) f1 * 0.6D, this.getY() + 1.8D, this.getZ() - (double) f2 * 0.6D, d0, d1, d2);
		}
	}

	@Override
	protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance instance) {
		this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(RatsItemRegistry.PLAGUE_SCYTHE.get()));
		this.setDropChance(EquipmentSlot.MAINHAND, 0);
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficulty, MobSpawnType type, @Nullable SpawnGroupData data, @Nullable CompoundTag tag) {
		SpawnGroupData finalData = super.finalizeSpawn(accessor, difficulty, type, data, tag);
		this.populateDefaultEquipmentSlots(accessor.getRandom(), difficulty);
		this.populateDefaultEquipmentEnchantments(accessor.getRandom(), difficulty);
		return finalData;
	}
}
