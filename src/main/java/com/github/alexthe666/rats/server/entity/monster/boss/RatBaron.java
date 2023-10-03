package com.github.alexthe666.rats.server.entity.monster.boss;

import com.github.alexthe666.rats.registry.RatlantisBlockRegistry;
import com.github.alexthe666.rats.registry.RatlantisEntityRegistry;
import com.github.alexthe666.rats.registry.RatlantisItemRegistry;
import com.github.alexthe666.rats.registry.RatsSoundRegistry;
import com.github.alexthe666.rats.server.entity.rat.AbstractRat;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

public class RatBaron extends AbstractRat implements Enemy {

	private final ServerBossEvent bossInfo = new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS);

	public RatBaron(EntityType<? extends AbstractRat> type, Level level) {
		super(type, level);
		this.setGuaranteedDrop(EquipmentSlot.HEAD);
	}

	@Override
	protected void customServerAiStep() {
		super.customServerAiStep();
		this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true) {
			@Override
			protected AABB getTargetSearchArea(double dist) {
				return this.mob.getBoundingBox().inflate(dist, 128.0D, dist);
			}
		});
	}

	@Override
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

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		if (this.getRestrictCenter() != BlockPos.ZERO) {
			BlockPos home = this.getRestrictCenter();
			tag.put("Home", this.makeDoubleList(home.getX(), home.getY(), home.getZ()));
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		if (this.hasCustomName()) {
			this.bossInfo.setName(this.getDisplayName());
		}
		if (tag.contains("Home", 9)) {
			ListTag nbttaglist = tag.getList("Home", 6);
			int hx = (int) nbttaglist.getDouble(0);
			int hy = (int) nbttaglist.getDouble(1);
			int hz = (int) nbttaglist.getDouble(2);
			this.restrictTo(new BlockPos(hx, hy, hz), 16);
		}
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return RatsSoundRegistry.RAT_HURT.get();
	}

	@Override
	protected boolean shouldDespawnInPeaceful() {
		return true;
	}

	@Override
	public boolean removeWhenFarAway(double dist) {
		return false;
	}

	@Override
	public void checkDespawn() {
		if (this.level().getDifficulty() == Difficulty.PEACEFUL) {
			if (this.hasRestriction()) {
				this.level().setBlockAndUpdate(this.getRestrictCenter(), RatlantisBlockRegistry.AIR_RAID_SIREN.get().defaultBlockState());
			}
			this.discard();
		} else {
			super.checkDespawn();
		}
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

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficulty, MobSpawnType type, @Nullable SpawnGroupData data, @Nullable CompoundTag tag) {
		data = super.finalizeSpawn(accessor, difficulty, type, data, tag);
		this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(RatlantisItemRegistry.AVIATOR_HAT.get()));
		this.setDropChance(EquipmentSlot.HEAD, 0);
		if (type != MobSpawnType.MOB_SUMMONED) {
			this.restrictTo(this.blockPosition(), 16);
		}
		if (!this.isPassenger()) {
			RatBaronPlane plane = new RatBaronPlane(RatlantisEntityRegistry.RAT_BARON_PLANE.get(), this.level());
			plane.copyPosition(this);
			plane.restrictTo(this.blockPosition(), 16);
			if (!this.level().isClientSide()) {
				this.level().addFreshEntity(plane);
			}
			this.startRiding(plane, true);
		}
		return data;
	}

	@Override
	public boolean canChangeDimensions() {
		return false;
	}

	@Override
	public boolean isVisuallySitting() {
		return true;
	}
}
