package com.github.alexthe666.rats.server.entity.ai.goal.harvest;

import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.misc.RatUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class RatPlaceGoal extends BaseRatHarvestGoal {
	private final TamedRat rat;

	public RatPlaceGoal(TamedRat rat) {
		super(rat);
		this.rat = rat;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		if (!super.canUse() || !this.checkTheBasics(false, false) || !this.holdingBlock()) {
			return false;
		}
		this.resetTarget();
		return this.getTargetBlock() != null;
	}

	private boolean holdingBlock() {
		ItemStack stack = this.rat.getItemInHand(InteractionHand.MAIN_HAND);
		return !stack.isEmpty() && stack.getItem() instanceof BlockItem;
	}

	@Override
	public boolean canContinueToUse() {
		return this.getTargetBlock() != null && this.holdingBlock() && this.rat.level().getBlockState(this.getTargetBlock()).canBeReplaced();
	}

	@Override
	public void tick() {
		if (this.getTargetBlock() != null) {
			if (this.holdingBlock()) {
				ItemStack stack = this.rat.getItemInHand(InteractionHand.MAIN_HAND);
				BlockItem blockItem = (BlockItem) stack.getItem();
				BlockState block = this.rat.level().getBlockState(this.getTargetBlock());
				BlockPos moveToPos = this.getTargetBlock();
				this.rat.getNavigation().moveTo(moveToPos.getX() + 0.5D, moveToPos.getY(), moveToPos.getZ() + 0.5D, 1.25D);
				if (block.getBlock().canSurvive(block, this.rat.level(), this.getTargetBlock()) && this.rat.level().isEmptyBlock(this.getTargetBlock().above()) && this.rat.level().getBlockState(this.getTargetBlock()).canBeReplaced()) {
					double distance = this.rat.getRatDistanceCenterSq(this.getTargetBlock().getX(), this.getTargetBlock().getY(), this.getTargetBlock().getZ());
					if (distance < this.rat.getRatHarvestDistance(0.0D)) {
						ItemStack seedStack = this.rat.getItemInHand(InteractionHand.MAIN_HAND).copy();
						seedStack.setCount(1);
						this.rat.getItemInHand(InteractionHand.MAIN_HAND).shrink(1);
						BlockHitResult raytrace = this.rat.level().clip(new ClipContext(new Vec3(this.getTargetBlock().getX(), this.getTargetBlock().getY(), this.getTargetBlock().getZ()), new Vec3(this.getTargetBlock().getX(), this.getTargetBlock().getY(), this.getTargetBlock().getZ()), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.rat));
						BlockPlaceContext itemusecontext = new BlockPlaceContext(this.rat.level(), null, InteractionHand.MAIN_HAND, this.rat.getItemInHand(InteractionHand.MAIN_HAND), raytrace);
						BlockState BlockState1 = blockItem.getBlock().getStateForPlacement(new BlockPlaceContext(itemusecontext));
						this.rat.level().setBlockAndUpdate(this.getTargetBlock(), BlockState1);
						if (this.rat.isInWall()) {
							this.rat.setPos(this.rat.getX(), this.rat.getY() + 1, this.rat.getZ());
						}
						SoundType placeSound = BlockState1.getBlock().getSoundType(BlockState1, this.rat.level(), this.getTargetBlock(), this.rat);
						this.rat.playSound(placeSound.getPlaceSound(), (placeSound.getVolume() + 1.0F) / 2.0F, placeSound.getPitch() * 0.8F);
						this.setTargetBlock(null);
						this.stop();
					}
				} else {
					this.setTargetBlock(null);
					this.stop();
				}
			}
		}
	}

	private void resetTarget() {
		BlockPos newTarget = null;
		if (this.rat.getHomePoint().isPresent()) {
			newTarget = this.rat.getHomePoint().get().pos();
			if (!this.rat.level().getBlockState(newTarget).canBeReplaced()) {
				newTarget = newTarget.above();
			}
		}
		if (newTarget != null && RatUtils.canRatPlaceBlock(this.rat.level(), newTarget, this.rat)) {
			this.setTargetBlock(newTarget);
		}
	}
}
