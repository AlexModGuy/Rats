package com.github.alexthe666.rats.server.entity.ai.goal;

import com.github.alexthe666.rats.data.tags.RatsBlockTags;
import com.github.alexthe666.rats.registry.RatsBlockRegistry;
import com.github.alexthe666.rats.server.block.entity.RatQuarryBlockEntity;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

public class RatQuarryGoal extends BaseRatHarvestGoal {
	private final TamedRat rat;
	private int breakingTime;
	private int previousBreakProgress;
	private BlockState prevMiningState = null;
	private boolean buildStairs = false;

	public RatQuarryGoal(TamedRat rat) {
		super(rat);
		this.rat = rat;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		if (this.rat.getDepositPos().isEmpty() || !this.rat.getDepositPos().get().dimension().equals(this.rat.getLevel().dimension()) || !this.rat.getLevel().getBlockState(this.rat.getDepositPos().get().pos()).is(RatsBlockRegistry.RAT_QUARRY.get()) || !this.checkTheBasics(true, true)) {
			return false;
		}

		this.resetTarget();
		return this.getTargetBlock() != null && this.canRatHoldTargetedBlock(this.rat.getLevel(), this.getTargetBlock());
	}

	private void resetTarget() {
		List<BlockPos> allBlocks = new ArrayList<>();
		GlobalPos quarryPos = this.rat.getDepositPos().orElse(null);
		if (quarryPos != null && this.rat.getLevel().getBlockEntity(quarryPos.pos()) instanceof RatQuarryBlockEntity quarry) {
			int RADIUS = quarry.getRadius();
			for (BlockPos pos : BlockPos.betweenClosedStream(quarryPos.pos().offset(-RADIUS, -1, -RADIUS), quarryPos.pos().offset(RADIUS, -quarryPos.pos().getY() - 1, RADIUS)).map(BlockPos::immutable).toList()) {
				if ((!this.rat.getLevel().isEmptyBlock(pos) && this.doesListContainBlock(this.rat.getLevel(), pos)) || this.rat.getLevel().getFluidState(pos).isSource()) {
					if (this.canMineBlock(pos)) {
						allBlocks.add(pos);
					}
				}
			}
			if (!allBlocks.isEmpty()) {
				allBlocks.sort(new QuarryBlockSorter(this.rat));
				this.setTargetBlock(allBlocks.get(allBlocks.size() - 1));
				BlockPos stairs = quarry.getNextPosForStairs();
				if (stairs.getY() >= this.getTargetBlock().getY() && this.rat.getLevel().getBlockState(stairs).isAir()) {
					this.buildStairs = true;
					this.setTargetBlock(stairs);
				}
			}
		}

	}

	private boolean doesListContainBlock(Level level, BlockPos pos) {
		BlockState state = level.getBlockState(pos);
		ItemStack getStack = state.getBlock().getCloneItemStack(level, pos, state);
		return this.rat.canRatPickupItem(getStack);
	}

	private boolean canRatHoldTargetedBlock(Level level, BlockPos pos) {
		if (this.rat.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) return true;
		BlockState state = level.getBlockState(pos);
		LootContext.Builder loot = new LootContext.Builder((ServerLevel) level).withParameter(LootContextParams.TOOL, ItemStack.EMPTY).withParameter(LootContextParams.ORIGIN, this.rat.position()).withRandom(this.rat.getRandom()).withLuck(1.0F);
		List<ItemStack> drops = state.getBlock().getDrops(state, loot);
		return drops.stream().map(ItemStack::getItem).toList().contains(this.rat.getItemInHand(InteractionHand.MAIN_HAND).getItem());
	}

	@Override
	public boolean canContinueToUse() {
		return this.checkTheBasics(false, true) && this.getTargetBlock() != null;
	}

	public void stop() {
		super.stop();
		this.rat.crafting = false;
		this.buildStairs = false;
	}

	@Override
	public void tick() {
		if (this.getTargetBlock() != null) {
			BlockPos rayPos = this.getOffsetFromTarget(this.rat.getLevel(), this.getTargetBlock());
			if (!this.buildStairs && this.rat.getLevel().getFluidState(this.getTargetBlock()).isEmpty() && this.rat.getLevel().isEmptyBlock(this.getTargetBlock())) {
				this.stop();
				return;
			}
			if (this.rat.isInLava() && this.rat.getLevel().getBlockState(this.rat.blockPosition().above()).isAir()) {
				this.rat.getLevel().setBlockAndUpdate(this.rat.blockPosition().above(), Blocks.WATER.defaultBlockState());
				this.rat.heal(15.0F);
				this.rat.getJumpControl().jump();
			}

			this.rat.getNavigation().moveTo(rayPos.getX(), rayPos.getY(), rayPos.getZ(), 1.0F);

			double distance = this.rat.getRatDistanceCenterSq(this.getTargetBlock().getX() + 0.5D, this.getTargetBlock().getY() + 0.5D, this.getTargetBlock().getZ() + 0.5D);
			if (!this.rat.getMoveControl().hasWanted() && (this.rat.isOnGround() || this.rat.isInWater() || this.rat.isInLava() || this.rat.isRidingSpecialMount())) {
				BlockState block = this.rat.getLevel().getBlockState(this.getTargetBlock());
				SoundType soundType = block.getBlock().getSoundType(block, this.rat.getLevel(), this.getTargetBlock(), null);
				if (this.buildStairs) {
					if (distance < this.rat.getRatHarvestDistance(0.0D)) {
						this.rat.getLevel().setBlockAndUpdate(this.getTargetBlock(), RatsBlockRegistry.RAT_QUARRY_PLATFORM.get().defaultBlockState());
						this.rat.getLevel().broadcastEntityEvent(this.rat, (byte) 86);
						this.prevMiningState = block;
						this.stop();
					}
				} else {
					if (block.getMaterial() != Material.AIR) {
						if (distance < this.rat.getRatHarvestDistance(0.0D)) {
							this.rat.getNavigation().stop();
							if (block.getFluidState().isSource()) {
								BlockState replace = Blocks.COBBLESTONE.defaultBlockState();
								if (block.getFluidState().is(FluidTags.LAVA)) {
									replace = Blocks.OBSIDIAN.defaultBlockState();
								}
								this.rat.getLevel().setBlockAndUpdate(this.getTargetBlock(), replace);
								this.rat.setPos(this.rat.getX(), this.rat.getY() + 1F, this.rat.getZ());
								this.rat.heal(2.0F);
								this.stop();
							}
							this.rat.getLevel().broadcastEntityEvent(this.rat, (byte) 85);
							this.rat.crafting = true;
							if (block == prevMiningState) {
								this.rat.getLevel().broadcastEntityEvent(this.rat, (byte) 85);
								this.rat.crafting = true;
							} else {
								this.rat.getLevel().broadcastEntityEvent(this.rat, (byte) 86);
								this.rat.crafting = false;
							}
							if (distance < this.rat.getRatHarvestDistance(-2.0D)) {
								this.rat.setDeltaMovement(Vec3.ZERO);
							}
							this.breakingTime++;
							int hardness = (int) (block.getDestroySpeed(this.rat.getLevel(), this.getTargetBlock()) * 10);
							int i = (int) ((float) this.breakingTime / hardness * 10.0F);
							if (this.breakingTime % 5 == 0) {
								this.rat.playSound(soundType.getHitSound(), soundType.getVolume() + 1, soundType.getPitch());
							}
							if (i != this.previousBreakProgress) {
								this.rat.getLevel().destroyBlockProgress(this.rat.getId(), this.getTargetBlock(), i);
								this.previousBreakProgress = i;
							}
							if (this.breakingTime >= hardness) {
								this.rat.getLevel().broadcastEntityEvent(this.rat, (byte) 86);
								this.rat.playSound(soundType.getBreakSound(), soundType.getVolume(), soundType.getPitch());
								this.rat.playSound(SoundEvents.ITEM_PICKUP, 1, 1F);
								this.breakingTime = 0;
								this.previousBreakProgress = -1;
								this.rat.getLevel().destroyBlock(this.getTargetBlock(), true);
								this.stop();
							}
							this.prevMiningState = block;
						}
					}
				}
			}
		}
	}

	private BlockPos getOffsetFromTarget(Level level, BlockPos initialPos) {
		for (Direction direction : Direction.values()) {
			if (level.getBlockState(initialPos.relative(direction)).isAir()) {
				return initialPos.relative(direction);
			}
		}
		return initialPos;
	}

	private boolean canMineBlock(BlockPos rayPos) {
		BlockState state = this.rat.getLevel().getBlockState(rayPos);
		return !state.is(RatsBlockTags.QUARRY_IGNORABLES) && this.doesListContainBlock(this.rat.getLevel(), rayPos);
	}

	public record QuarryBlockSorter(TamedRat rat) implements Comparator<BlockPos> {

		@Override
		public int compare(BlockPos pos1, BlockPos pos2) {
			FluidState state1 = this.rat().getLevel().getFluidState(pos2);
			FluidState state2 = this.rat().getLevel().getFluidState(pos1);
			double distance1 = pos2.getY();
			double distance2 = pos1.getY();
			if (state1.isSource() && !state2.isSource()) {
				return 1;
			}
			if (state2.isSource() && !state1.isSource()) {
				return -1;
			}
			return Double.compare(distance2, distance1);
		}
	}
}
