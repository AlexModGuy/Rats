package com.github.alexthe666.rats.server.entity.ai.goal.harvest;

import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.IForgeShearable;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class RatUseShearsGoal extends BaseRatHarvestGoal {
	private static final ItemStack SHEAR_STACK = new ItemStack(Items.SHEARS);
	private final TamedRat rat;
	private final Predicate<LivingEntity> SHEAR_PREDICATE = entity -> entity instanceof IForgeShearable && ((IForgeShearable) entity).isShearable(SHEAR_STACK, entity.level(), entity.blockPosition());

	public RatUseShearsGoal(TamedRat rat) {
		super(rat);
		this.rat = rat;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		if (!super.canUse() || !this.checkTheBasics(this.rat.getDepositPos().isPresent(), this.rat.getDepositPos().isPresent())) {
			return false;
		}
		this.resetTarget();
		return this.getTargetEntity() != null || this.getTargetBlock() != null;
	}

	@Override
	public boolean canContinueToUse() {
		return this.getTargetEntity() != null || this.getTargetBlock() != null;
	}

	@Override
	public void tick() {
		if (this.getTargetEntity() != null && this.getTargetEntity().isAlive() && this.rat.getMainHandItem().isEmpty()) {
			this.rat.getNavigation().moveTo(this.getTargetEntity(), 1.25D);
			if (this.rat.distanceToSqr(this.getTargetEntity()) < this.rat.getRatHarvestDistance(0.0D)) {
				if (this.getTargetEntity() instanceof IForgeShearable shearable) {
					List<ItemStack> drops = shearable.onSheared(null, SHEAR_STACK, this.rat.level(), this.getTargetEntity().blockPosition(), 0);
					this.rat.gameEvent(GameEvent.ENTITY_INTERACT);
					for (ItemStack stack : drops) {
						this.getTargetEntity().spawnAtLocation(stack, 0.0F);
					}
				}
				this.stop();
			}
		} else if (this.getTargetBlock() != null && this.rat.getMainHandItem().isEmpty()) {
			this.rat.getNavigation().moveTo(this.getTargetBlock().getX() + 0.5D, this.getTargetBlock().getY(), this.getTargetBlock().getZ() + 0.5D, 1.25D);
			if (this.rat.getRatDistanceCenterSq(this.getTargetBlock().getX(), this.getTargetBlock().getY(), this.getTargetBlock().getZ()) < this.rat.getRatHarvestDistance(0.0D)) {
				BlockState state = this.rat.level().getBlockState(this.getTargetBlock());
				this.rat.level().playSound(null, this.rat.blockPosition(), SoundEvents.BEEHIVE_SHEAR, SoundSource.BLOCKS, 1.0F, 1.0F);
				BeehiveBlock.dropHoneycomb(this.rat.level(), this.getTargetBlock());
				this.rat.gameEvent(GameEvent.SHEAR);

				if (!CampfireBlock.isSmokeyPos(this.rat.level(), this.getTargetBlock())) {
					if (this.hiveContainsBees(this.rat.level(), this.getTargetBlock())) {
						this.angerNearbyBees(this.rat.level(), this.getTargetBlock());
					}

					this.releaseBeesAndResetHoneyLevel(this.rat.level(), state, this.getTargetBlock(), BeehiveBlockEntity.BeeReleaseStatus.EMERGENCY);
				} else {
					this.rat.level().setBlock(this.getTargetBlock(), state.setValue(BeehiveBlock.HONEY_LEVEL, 0), 3);
				}
				this.stop();
			}
		} else {
			this.stop();
		}
	}

	private void angerNearbyBees(Level level, BlockPos pos) {
		List<Bee> list = level.getEntitiesOfClass(Bee.class, (new AABB(pos)).inflate(8.0D, 6.0D, 8.0D));
		if (!list.isEmpty()) {
			for (Bee bee : list) {
				if (bee.getTarget() == null) {
					bee.setTarget(this.rat);
				}
			}
		}

	}

	private boolean hiveContainsBees(Level level, BlockPos pos) {
		BlockEntity blockentity = level.getBlockEntity(pos);
		if (blockentity instanceof BeehiveBlockEntity beehiveblockentity) {
			return !beehiveblockentity.isEmpty();
		} else {
			return false;
		}
	}

	public void releaseBeesAndResetHoneyLevel(Level level, BlockState state, BlockPos pos, BeehiveBlockEntity.BeeReleaseStatus status) {
		level.setBlock(pos, state.setValue(BeehiveBlock.HONEY_LEVEL, 0), 3);
		BlockEntity blockentity = level.getBlockEntity(pos);
		if (blockentity instanceof BeehiveBlockEntity beehive) {
			List<Entity> list = beehive.releaseAllOccupants(state, status);
			for (Entity entity : list) {
				if (entity instanceof Bee bee) {
					if (this.rat.position().distanceToSqr(entity.position()) <= 16.0D) {
						if (!beehive.isSedated()) {
							bee.setTarget(this.rat);
						} else {
							bee.setStayOutOfHiveCountdown(400);
						}
					}
				}
			}
		}
	}

	private void resetTarget() {
		int radius = this.rat.getRadius();
		AABB bb = new AABB(-radius, -radius, -radius, radius, radius, radius).move(this.rat.getSearchCenter());
		List<LivingEntity> list = this.rat.level().getEntitiesOfClass(LivingEntity.class, bb, SHEAR_PREDICATE);
		LivingEntity closestSheep = null;
		for (LivingEntity base : list) {
			if (closestSheep == null || base.distanceToSqr(this.rat) < closestSheep.distanceToSqr(this.rat)) {
				closestSheep = base;
			}
		}
		if (closestSheep != null) {
			this.setTargetEntity(closestSheep);
			return;
		}

		List<BlockPos> allBlocks = new ArrayList<>();
		int RADIUS = this.rat.getRadius();
		for (BlockPos pos : BlockPos.betweenClosedStream(this.rat.getSearchCenter().offset(-RADIUS, -RADIUS, -RADIUS), this.rat.getSearchCenter().offset(RADIUS, RADIUS, RADIUS)).map(BlockPos::immutable).toList()) {
			if ((this.rat.level().getBlockState(pos).getBlock() instanceof BeehiveBlock && this.rat.level().getBlockState(pos).getValue(BeehiveBlock.HONEY_LEVEL) == 5)) {
				allBlocks.add(pos);
			}
		}
		if (!allBlocks.isEmpty()) {
			allBlocks.sort(this.getTargetSorter());
			this.setTargetBlock(allBlocks.get(0));
		}
	}
}
