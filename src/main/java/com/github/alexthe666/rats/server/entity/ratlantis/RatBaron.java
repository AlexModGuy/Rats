package com.github.alexthe666.rats.server.entity.ratlantis;

import com.github.alexthe666.rats.registry.RatlantisEntityRegistry;
import com.github.alexthe666.rats.registry.RatlantisItemRegistry;
import com.github.alexthe666.rats.registry.RatsSoundRegistry;
import com.github.alexthe666.rats.server.entity.rat.AbstractRat;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

public class RatBaron extends AbstractRat {

	private final ServerBossEvent bossInfo = new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS);

	public RatBaron(EntityType<? extends AbstractRat> type, Level level) {
		super(type, level);
	}

	protected void customServerAiStep() {
		super.customServerAiStep();
		this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
	}

	protected void registerGoals() {
		super.registerGoals();
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true) {
			@Override
			protected AABB getTargetSearchArea(double dist) {
				return this.mob.getBoundingBox().inflate(dist, 128.0D, dist);
			}
		});
	}

	public double getMyRidingOffset() {
		return 0.45D;
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 300.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.25D)
				.add(Attributes.ATTACK_DAMAGE, 7.0D)
				.add(Attributes.FOLLOW_RANGE, 64.0D);
	}

	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		if (this.hasCustomName()) {
			this.bossInfo.setName(this.getDisplayName());
		}
	}

	protected SoundEvent getHurtSound(DamageSource source) {
		return RatsSoundRegistry.RAT_HURT.get();
	}

	@Override
	protected boolean shouldDespawnInPeaceful() {
		return true;
	}

	public void setCustomName(@Nullable Component name) {
		super.setCustomName(name);
		this.bossInfo.setName(this.getDisplayName());
	}

	public void startSeenByPlayer(ServerPlayer player) {
		super.startSeenByPlayer(player);
		this.bossInfo.addPlayer(player);
	}

	public void stopSeenByPlayer(ServerPlayer player) {
		super.stopSeenByPlayer(player);
		this.bossInfo.removePlayer(player);
	}

	@Nullable
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
		spawnDataIn = super.finalizeSpawn(level, difficultyIn, reason, spawnDataIn, dataTag);
		this.setMale(this.getRandom().nextBoolean());
		this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(RatlantisItemRegistry.AVIATOR_HAT.get()));
		this.setDropChance(EquipmentSlot.HEAD, 0);
		if (!this.isPassenger()) {
			RatBaronPlane boat = new RatBaronPlane(RatlantisEntityRegistry.RAT_BARON_PLANE.get(), this.getLevel());
			boat.copyPosition(this);
			if (!this.getLevel().isClientSide()) {
				this.getLevel().addFreshEntity(boat);
			}
			this.startRiding(boat, true);
		}
		return spawnDataIn;
	}
}
