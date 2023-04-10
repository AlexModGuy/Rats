package com.github.alexthe666.rats.server.entity.ai.goal;

import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class RatHarvestFarmerGoal extends BaseRatHarvestGoal {
	private final TamedRat rat;

	public RatHarvestFarmerGoal(TamedRat rat) {
		super(rat);
		this.rat = rat;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		if (!this.checkTheBasics(this.rat.getDepositPos().isPresent(), false) && !this.holdingSeeds() && !this.holdingBlock() && !this.holdingBonemeal()) {
			return false;
		}
		this.resetTarget();
		return this.getTargetBlock() != null;
	}

	private boolean holdingSeeds() {
		ItemStack stack = this.rat.getItemInHand(InteractionHand.MAIN_HAND);
		return !stack.isEmpty() && stack.is(Tags.Items.SEEDS);
	}

	private boolean holdingBonemeal() {
		ItemStack stack = this.rat.getItemInHand(InteractionHand.MAIN_HAND);
		return !stack.isEmpty() && stack.is(Items.BONE_MEAL);
	}

	private boolean holdingBlock() {
		ItemStack stack = this.rat.getItemInHand(InteractionHand.MAIN_HAND);
		return !stack.isEmpty() && stack.getItem() instanceof BlockItem;
	}

	@Override
	public boolean canContinueToUse() {
		return this.checkTheBasics(false, false) && this.getTargetBlock() != null && (this.holdingSeeds() || this.holdingBonemeal() || this.holdingBlock());
	}

	@Override
	public void tick() {
		if (this.getTargetBlock() != null) {
			if (this.holdingSeeds()) {
				BlockState block = this.rat.getLevel().getBlockState(this.getTargetBlock());
				this.rat.getNavigation().moveTo(this.getTargetBlock().getX() + 0.5D, this.getTargetBlock().getY(), this.getTargetBlock().getZ() + 0.5D, 1.25D);
				if (block.getBlock().isFertile(block, this.rat.getLevel(), this.getTargetBlock()) && this.rat.getLevel().isEmptyBlock(this.getTargetBlock().above())) {
					double distance = this.rat.getRatDistanceCenterSq(this.getTargetBlock().getX(), this.getTargetBlock().getY(), this.getTargetBlock().getZ());
					if (distance < this.rat.getRatHarvestDistance(0.0F)) {
						if (this.holdingSeeds()) {
							ItemStack seedStack = this.rat.getItemInHand(InteractionHand.MAIN_HAND).copy();
							seedStack.setCount(1);
							this.rat.getItemInHand(InteractionHand.MAIN_HAND).shrink(1);
							if (seedStack.getItem() instanceof ItemNameBlockItem item) {
								this.rat.getLevel().setBlockAndUpdate(this.getTargetBlock().above(), item.getBlock().defaultBlockState());
							}
						}
						this.stop();
						return;
					}
				} else {
					this.stop();
					return;
				}
			}
			if (this.holdingBonemeal()) {
				BlockState block = this.rat.getLevel().getBlockState(this.getTargetBlock());
				this.rat.getNavigation().moveTo(this.getTargetBlock().getX() + 0.5D, this.getTargetBlock().getY(), this.getTargetBlock().getZ() + 0.5D, 1.25D);
				if (this.canPlantBeBonemealed(this.getTargetBlock(), block)) {
					double distance = this.rat.getRatDistanceCenterSq(this.getTargetBlock().getX(), this.getTargetBlock().getY(), this.getTargetBlock().getZ());
					if (distance < 4.5F) {
						if (this.holdingBonemeal()) {
							this.rat.getItemInHand(InteractionHand.MAIN_HAND).shrink(1);
							if (block.getBlock() instanceof BonemealableBlock bonemealable) {
								if (bonemealable.isValidBonemealTarget(this.rat.getLevel(), this.getTargetBlock(), block, this.rat.getLevel().isClientSide())) {
									if (!this.rat.getLevel().isClientSide()) {
										this.rat.getLevel().levelEvent(2005, this.getTargetBlock(), 0);
										this.rat.getLevel().playSound(null, this.getTargetBlock(), SoundEvents.BONE_MEAL_USE, SoundSource.BLOCKS);
										bonemealable.performBonemeal((ServerLevel) this.rat.getLevel(), this.rat.getLevel().getRandom(), this.getTargetBlock(), block);
									}
								}
							}
						}
						this.stop();
					}
				} else {
					this.stop();
				}
			} else if (this.holdingBlock()) {
				BlockItem itemBlock = ((BlockItem) this.rat.getItemInHand(InteractionHand.MAIN_HAND).getItem());
				this.rat.getNavigation().moveTo(this.getTargetBlock().getX() + 0.5D, this.getTargetBlock().getY(), this.getTargetBlock().getZ() + 0.5D, 1.25D);
				if (this.rat.getLevel().isEmptyBlock(this.getTargetBlock().above())) {
					double distance = this.rat.getRatDistanceCenterSq(this.getTargetBlock().getX(), this.getTargetBlock().getY(), this.getTargetBlock().getZ());
					if (distance < 4.5F) {
						if (this.holdingBlock()) {
							BlockHitResult raytrace = this.rat.getLevel().clip(new ClipContext(new Vec3(this.getTargetBlock().getX(), this.getTargetBlock().getY(), this.getTargetBlock().getZ()), new Vec3(this.getTargetBlock().getX(), this.getTargetBlock().getY(), this.getTargetBlock().getZ()), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.rat));
							BlockPlaceContext itemusecontext = new BlockPlaceContext(this.rat.getLevel(), null, InteractionHand.MAIN_HAND, this.rat.getItemInHand(InteractionHand.MAIN_HAND), raytrace);
							BlockState state = itemBlock.getBlock().getStateForPlacement(new BlockPlaceContext(itemusecontext));
							this.rat.getItemInHand(InteractionHand.MAIN_HAND).shrink(1);
							this.rat.getLevel().setBlockAndUpdate(this.getTargetBlock(), state);
							if (this.rat.isInWall()) {
								this.rat.setPos(this.rat.getX(), this.rat.getY() + 1.5D, this.rat.getZ());
							}
							SoundType placeSound = state.getBlock().getSoundType(state, this.rat.getLevel(), this.getTargetBlock(), this.rat);
							this.rat.playSound(placeSound.getPlaceSound(), (placeSound.getVolume() + 1.0F) / 2.0F, placeSound.getPitch() * 0.8F);
						}
						this.stop();
					}
				} else {
					this.stop();
				}
			}

		}
	}

	private void resetTarget() {
		int RADIUS = this.rat.getRadius();
		if (this.holdingBonemeal()) {
			List<BlockPos> allBlocks = new ArrayList<>();
			for (BlockPos pos : BlockPos.betweenClosedStream(this.rat.getSearchCenter().offset(-RADIUS, -RADIUS, -RADIUS), this.rat.getSearchCenter().offset(RADIUS, RADIUS, RADIUS)).map(BlockPos::immutable).toList()) {
				if (this.canPlantBeBonemealed(pos, this.rat.getLevel().getBlockState(pos))) {
					allBlocks.add(pos);
				}
			}
			if (!allBlocks.isEmpty()) {
				allBlocks.sort(this.getTargetSorter());
				this.setTargetBlock(allBlocks.get(0));
			}
		} else if (this.holdingSeeds()) {
			List<BlockPos> allBlocks = new ArrayList<>();
			for (BlockPos pos : BlockPos.betweenClosedStream(this.rat.getSearchCenter().offset(-RADIUS, -RADIUS, -RADIUS), this.rat.getSearchCenter().offset(RADIUS, RADIUS, RADIUS)).map(BlockPos::immutable).toList()) {
				if (this.rat.getLevel().getBlockState(pos).getBlock().isFertile(this.rat.getLevel().getBlockState(pos), this.rat.getLevel(), pos) && this.rat.getLevel().isEmptyBlock(pos.above())) {
					allBlocks.add(pos);
				}
			}
			if (!allBlocks.isEmpty()) {
				allBlocks.sort(this.getTargetSorter());
				this.setTargetBlock(allBlocks.get(0));
			}
		} else if (this.holdingBlock()) {
			List<BlockPos> allBlocks = new ArrayList<>();
			Block block = null;
			if (!this.rat.getItemInHand(InteractionHand.MAIN_HAND).isEmpty() && this.rat.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof BlockItem item) {
				block = item.getBlock();
			}
			if (block == null) return;
			for (BlockPos pos : BlockPos.betweenClosedStream(this.rat.getSearchCenter().offset(-RADIUS, -RADIUS, -RADIUS), this.rat.getSearchCenter().offset(RADIUS, RADIUS, RADIUS)).map(BlockPos::immutable).toList()) {
				//dont allow placing on points of interest (on the tops of transport positions or the home point)
				if (this.rat.getDepositPos().isPresent() && pos.equals(this.rat.getDepositPos().get().pos().above())) continue;
				if (this.rat.getPickupPos().isPresent() && pos.equals(this.rat.getPickupPos().get().pos().above())) continue;
				if (this.rat.getHomePoint().isPresent() && (pos.equals(this.rat.getHomePoint().get().pos()) || pos.equals(this.rat.getHomePoint().get().pos().above()))) continue;

				if (block.canSurvive(block.defaultBlockState(), this.rat.getLevel(), pos) && this.rat.getLevel().isEmptyBlock(pos.above()) && this.rat.getLevel().isEmptyBlock(pos)) {
					allBlocks.add(pos);
				}
			}
			if (!allBlocks.isEmpty()) {
				allBlocks.sort(this.getTargetSorter());
				this.setTargetBlock(allBlocks.get(0));
			}
		}

	}

	private boolean canPlantBeBonemealed(BlockPos pos, BlockState state) {
		if (state.getBlock() instanceof BonemealableBlock bonemealable && state.is(BlockTags.BEE_GROWABLES)) {
			if (bonemealable.isValidBonemealTarget(this.rat.getLevel(), pos, state, this.rat.getLevel().isClientSide())) {
				return bonemealable.isBonemealSuccess(this.rat.getLevel(), this.rat.getLevel().getRandom(), pos, state);
			}
		}
		return false;
	}
}
