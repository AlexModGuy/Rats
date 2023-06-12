package com.github.alexthe666.rats.server.entity.monster.boss;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.registry.RatsEffectRegistry;
import com.github.alexthe666.rats.registry.RatsEntityRegistry;
import com.github.alexthe666.rats.registry.RatsSoundRegistry;
import com.github.alexthe666.rats.server.entity.projectile.RatShot;
import com.github.alexthe666.rats.server.entity.RatSummoner;
import com.github.alexthe666.rats.registry.RatVariantRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

public class RatKing extends Monster implements RatSummoner {
	public static final int RAT_COUNT = 15;
	public static final float RAT_ANGLE = 360F / RAT_COUNT;
	private static final EntityDataAccessor<String> RAT_COLORS = SynchedEntityData.defineId(RatKing.class, EntityDataSerializers.STRING);
	private static final EntityDataAccessor<Integer> SUMMONED_RATS = SynchedEntityData.defineId(RatKing.class, EntityDataSerializers.INT);

	public RatKing(EntityType<? extends Monster> type, Level level) {
		super(type, level);
		this.xpReward = 40;
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new FloatGoal(this));
		this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, true));
		this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.getEntityData().define(RAT_COLORS, "000000000000000");
		this.getEntityData().define(SUMMONED_RATS, 0);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 200.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.2D)
				.add(Attributes.ATTACK_DAMAGE, 2.0D)
				.add(Attributes.FOLLOW_RANGE, 32.0D)
				.add(Attributes.KNOCKBACK_RESISTANCE, 0.75D);
	}

	@Override
	public void aiStep() {
		super.aiStep();
		LivingEntity target = this.getTarget();
		if (!this.level().isClientSide() && target != null & this.tickCount % 15 == 0 && this.getRatsSummoned() < RatConfig.ratKingMaxRatSpawns) {
			RatShot shot = new RatShot(RatsEntityRegistry.RAT_SHOT.get(), this.level(), this);
			shot.setColorVariant(RatVariantRegistry.getRandomVariant(this.getRandom(), false));
			double extraX = this.getX();
			double extraZ = this.getZ();
			double extraY = 0.2 + this.getY();
			double d0 = target.getEyeY() - (double) 1.2F;
			double d1 = target.getX() - extraX;
			double d3 = target.getZ() - extraZ;
			double d2 = d0 - extraY;
			float f = Mth.sqrt((float) (d1 * d1 + d3 * d3)) * 1;
			float velocity = Math.min(Mth.sqrt((float) (d1 * d1 + d3 * d3)) * 0.06F, 1.4F);
			shot.setPos(extraX, extraY, extraZ);
			shot.shoot(d1, d2 + (double) f, d3, velocity, 0);
			this.playSound(RatsSoundRegistry.RAT_KING_SHOOT.get(), 3.0F, 2.3F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
			if (!this.level().isClientSide()) {
				this.level().addFreshEntity(shot);
			}
		}
	}

	@Override
	public void playAmbientSound() {
		SoundEvent soundevent = this.getAmbientSound();
		if (soundevent != null) {
			for (int i = 0; i < 3 + this.getRandom().nextInt(3); i++) {
				this.playSound(soundevent, this.getSoundVolume(), this.getVoicePitch());
			}
		}
	}

	@Override
	public int getAmbientSoundInterval() {
		return 60;
	}

	@Override
	protected void playHurtSound(DamageSource source) {
		SoundEvent soundevent = this.getHurtSound(source);
		if (soundevent != null) {
			for (int i = 0; i < 3 + this.getRandom().nextInt(3); i++) {
				this.playSound(soundevent, this.getSoundVolume(), this.getVoicePitch());
			}
		}
	}

	@Override
	public void die(DamageSource source) {
		if (net.minecraftforge.common.ForgeHooks.onLivingDeath(this, source)) return;
		if (!this.isRemoved() && !this.dead) {
			Entity entity = source.getEntity();
			LivingEntity livingentity = this.getKillCredit();
			if (this.deathScore >= 0 && livingentity != null) {
				livingentity.awardKillScore(this, this.deathScore, source);
			}

			this.dead = true;
			this.getCombatTracker().recheckStatus();
			if (this.level() instanceof ServerLevel server) {
				if (entity == null || entity.killedEntity(server, this)) {
					this.gameEvent(GameEvent.ENTITY_DIE);
					//this.dropAllDeathLoot(source);
					this.createWitherRose(livingentity);
				}

				this.level().broadcastEntityEvent(this, (byte) 3);
			}

			this.setPose(Pose.DYING);
		}
	}

	@Override
	protected void tickDeath() {
		++this.deathTime;
		if (this.deathTime >= 100 && !this.level().isClientSide() && !this.isRemoved()) {
			this.level().broadcastEntityEvent(this, (byte) 60);
			this.remove(Entity.RemovalReason.KILLED);
			if (this.getLastDamageSource() != null) {
				this.dropAllDeathLoot(this.getLastDamageSource());
			} else {
				this.dropAllDeathLoot(this.damageSources().generic());
			}
		}
	}

	//add a death check, so we can properly store the last damage source during our long death animation
	@Nullable
	@Override
	public DamageSource getLastDamageSource() {
		if (!this.isDeadOrDying()) {
			if (this.level().getGameTime() - this.lastDamageStamp > 40L) {
				this.lastDamageSource = null;
			}
		}

		return this.lastDamageSource;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return RatsSoundRegistry.RAT_IDLE.get();
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
	@Nullable
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
		spawnData = super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
		for (int i = 0; i < RAT_COUNT; i++) {
			int color = this.getRandom().nextInt(4);
			this.setRatColors(i, color);
		}
		return spawnData;
	}

	@Override
	public boolean removeWhenFarAway(double dist) {
		return false;
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putString("RatColors", this.getRatColorsString());
		compound.putInt("RatsSummoned", this.getRatsSummoned());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.setRatsSummoned(compound.getInt("RatsSummoned"));
		this.setRatColorsString(compound.getString("RatColors"));
	}

	private void setRatColorsString(String str) {
		this.getEntityData().set(RAT_COLORS, str);
	}

	private String getRatColorsString() {
		return this.getEntityData().get(RAT_COLORS);
	}

	public void setRatColors(int index, int color) {
		String ratColors = getRatColorsString();
		if (ratColors.length() < RAT_COUNT - 1) {
			ratColors = "000000000000000";
		}
		String before = ratColors.substring(0, index);
		String after = ratColors.substring(index, RAT_COUNT - 1);
		String newStr = before + color + after;
		setRatColorsString(newStr);
	}

	public int getRatColors(int index) {
		String ratColors = getRatColorsString();
		if (ratColors.length() < RAT_COUNT - 1) {
			ratColors = "000000000000000";
		}
		char c = ratColors.charAt(index);
		return Integer.parseInt(String.valueOf(c));
	}

	@Override
	public boolean canChangeDimensions() {
		return false;
	}

	@Override
	protected boolean canRide(Entity entity) {
		return false;
	}

	@Override
	public boolean encirclesSummoner() {
		return true;
	}

	@Override
	public boolean reabsorbRats() {
		return RatConfig.ratKingReabsorbsRats && this.getRatsSummoned() > 5;
	}

	@Override
	public float reabsorbedRatHealAmount() {
		return (float) RatConfig.ratKingReabsorbHealRate;
	}

	@Override
	public int getRatsSummoned() {
		return this.getEntityData().get(SUMMONED_RATS);
	}

	@Override
	public void setRatsSummoned(int count) {
		this.getEntityData().set(SUMMONED_RATS, count);
	}

	@Override
	public float getRadius() {
		return 3.0F;
	}

	public BlockPos getLightPosition() {
		BlockPos pos = this.blockPosition();
		if (!this.level().getBlockState(pos).canOcclude()) {
			return pos.above();
		}
		return pos;
	}
}
