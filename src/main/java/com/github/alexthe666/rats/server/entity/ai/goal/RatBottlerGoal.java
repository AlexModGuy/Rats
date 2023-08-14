package com.github.alexthe666.rats.server.entity.ai.goal;

import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;

public class RatBottlerGoal extends BaseRatHarvestGoal {
	public RatBottlerGoal(TamedRat rat) {
		super(rat);
	}

	@Override
	public boolean canUse() {
		if (!this.checkTheBasics(false, false) || !this.rat.getMainHandItem().is(Items.GLASS_BOTTLE)) return false;
		this.resetTarget();
		return this.getTargetBlock() != null;
	}

	@Override
	public boolean canContinueToUse() {
		return this.getTargetBlock() != null && this.rat.getMainHandItem().is(Items.GLASS_BOTTLE);
	}

	@Override
	public void tick() {
		if (this.getTargetBlock() != null) {
			BlockPos pos = this.getTargetBlock();
			BlockState block = this.rat.level().getBlockState(pos);
			this.rat.getNavigation().moveTo(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, 1.25D);
			double distance = this.rat.getRatDistanceCenterSq(this.getTargetBlock().getX(), this.getTargetBlock().getY(), this.getTargetBlock().getZ());
			if (distance < this.rat.getRatHarvestDistance(0.0D)) {
				if (block.getBlock() instanceof BeehiveBlock) {
					this.giveOrDropItem(new ItemStack(Items.HONEY_BOTTLE));
					this.rat.level().playSound(null, this.rat.blockPosition(), SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);

					if (!CampfireBlock.isSmokeyPos(this.rat.level(), pos)) {
						if (this.hiveContainsBees(this.rat.level(), pos)) {
							this.angerNearbyBees(this.rat.level(), pos);
						}

						this.releaseBeesAndResetHoneyLevel(this.rat.level(), block, pos, BeehiveBlockEntity.BeeReleaseStatus.EMERGENCY);
					} else {
						this.rat.level().setBlock(pos, block.setValue(BeehiveBlock.HONEY_LEVEL, 0), 3);
					}
					this.stop();
				} else if (block.is(Blocks.WATER_CAULDRON)) {
					LayeredCauldronBlock.lowerFillLevel(block, this.rat.level(), pos);
					this.giveOrDropItem(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER));
					this.rat.level().playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
					this.stop();
				}
			}
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

	private void giveOrDropItem(ItemStack stack) {
		this.rat.getMainHandItem().shrink(1);
		if (this.rat.getMainHandItem().isEmpty()) {
			this.rat.setItemInHand(InteractionHand.MAIN_HAND, stack);
		} else {
			this.rat.spawnAtLocation(stack);
		}
		this.rat.gameEvent(GameEvent.FLUID_PICKUP);
	}

	private void resetTarget() {
		List<BlockPos> allBlocks = new ArrayList<>();
		int RADIUS = this.rat.getRadius();
		for (BlockPos pos : BlockPos.betweenClosedStream(this.rat.getSearchCenter().offset(-RADIUS, -RADIUS, -RADIUS), this.rat.getSearchCenter().offset(RADIUS, RADIUS, RADIUS)).map(BlockPos::immutable).toList()) {
			BlockState state = this.rat.level().getBlockState(pos);
			if (state.getBlock() instanceof BeehiveBlock && state.getValue(BeehiveBlock.HONEY_LEVEL) == 5) {
				allBlocks.add(pos);
			} else if (state.is(Blocks.WATER_CAULDRON) && state.getValue(LayeredCauldronBlock.LEVEL) > 0) {
				allBlocks.add(pos);
			}
		}
		if (!allBlocks.isEmpty()) {
			allBlocks.sort(this.getTargetSorter());
			this.setTargetBlock(allBlocks.get(0));
		}
	}
}
