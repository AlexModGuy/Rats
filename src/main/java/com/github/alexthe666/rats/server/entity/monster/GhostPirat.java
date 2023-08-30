package com.github.alexthe666.rats.server.entity.monster;

import com.github.alexthe666.rats.data.ratlantis.tags.RatlantisBlockTags;
import com.github.alexthe666.rats.registry.RatlantisItemRegistry;
import com.github.alexthe666.rats.server.entity.rat.AbstractRat;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

public class GhostPirat extends AbstractRat implements Enemy {

	public GhostPirat(EntityType<? extends AbstractRat> type, Level level) {
		super(type, level);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 20.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.35D)
				.add(Attributes.ATTACK_DAMAGE, 0.0D)
				.add(Attributes.FOLLOW_RANGE, 16.0D);
	}

	public static boolean checkGhostPiratSpawnRules(EntityType<? extends Mob> entityType, LevelAccessor world, MobSpawnType reason, BlockPos pos, RandomSource rand) {
		return canSpawnAtPos(world, pos) || reason == MobSpawnType.SPAWNER;
	}

	private static boolean canSpawnAtPos(LevelAccessor world, BlockPos pos) {
		return world.getBlockState(pos.below()).is(RatlantisBlockTags.PIRAT_ONLY_BLOCKS);
	}

	protected void registerGoals() {
		super.registerGoals();
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true, false));
	}

	@Override
	protected boolean shouldDespawnInPeaceful() {
		return true;
	}

	public double getMyRidingOffset() {
		return 0.45D;
	}

	@Nullable
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData data, @Nullable CompoundTag tag) {
		data = super.finalizeSpawn(level, difficulty, reason, data, tag);
		this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(RatlantisItemRegistry.GHOST_PIRAT_CUTLASS.get()));
		this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(RatlantisItemRegistry.GHOST_PIRAT_HAT.get()));
		return data;
	}

	public boolean isTame() {
		return false;
	}
}
