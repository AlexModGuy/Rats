package com.github.alexthe666.rats.server.entity.ai.goal;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.misc.RatUpgradeUtils;
import com.github.alexthe666.rats.server.misc.RatUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class RatHarvestCropsGoal extends BaseRatHarvestGoal {
	private final TamedRat rat;


	public RatHarvestCropsGoal(TamedRat rat) {
		super(rat);
		this.rat = rat;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		if (!this.checkTheBasics(this.rat.getDepositPos().isPresent(), this.rat.getDepositPos().isPresent())) {
			return false;
		}
		this.resetTarget();
		return this.getTargetBlock() != null;
	}

	private void resetTarget() {
		List<BlockPos> allBlocks = new ArrayList<>();
		int RADIUS = this.rat.getRadius();
		for (BlockPos pos : BlockPos.betweenClosedStream(this.rat.getSearchCenter().offset(-RADIUS, -RADIUS, -RADIUS), this.rat.getSearchCenter().offset(RADIUS, RADIUS, RADIUS)).map(BlockPos::immutable).toList()) {
			BlockState state = this.rat.getLevel().getBlockState(pos);
			if (state.is(BlockTags.CROPS)) {
				if (state.getBlock() instanceof CropBlock crop && !crop.isMaxAge(state)) continue;
				if (!(state.getBlock() instanceof StemBlock) && !(state.getBlock() instanceof AttachedStemBlock)) {
					if (RatUtils.canRatBreakBlock(this.rat.getLevel(), pos, this.rat)) {
						allBlocks.add(pos);
					}
				}
			}
		}
		if (!allBlocks.isEmpty()) {
			allBlocks.sort(this.getTargetSorter());
			this.setTargetBlock(allBlocks.get(0));
		}
	}

	@Override
	public boolean canContinueToUse() {
		return this.checkTheBasics(false, false) && this.getTargetBlock() != null && this.rat.getItemInHand(InteractionHand.MAIN_HAND).isEmpty();
	}

	@Override
	public void tick() {
		if (this.getTargetBlock() != null) {
			BlockState block = this.rat.getLevel().getBlockState(this.getTargetBlock());
			this.rat.getNavigation().moveTo(this.getTargetBlock().getX() + 0.5D, this.getTargetBlock().getY(), this.getTargetBlock().getZ() + 0.5D, 1.25D);
			if (block.getBlock() instanceof BushBlock || block.getMaterial() == Material.VEGETABLE) {
				if (block.getBlock() instanceof CropBlock crop && !crop.isMaxAge(block)) {
					this.setTargetBlock(null);
					this.stop();
					return;
				}
				double distance = this.rat.getRatDistanceCenterSq(this.getTargetBlock().getX(), this.getTargetBlock().getY(), this.getTargetBlock().getZ());
				if (distance < this.rat.getRatHarvestDistance(0.0D)) {
					this.rat.getLevel().destroyBlock(this.getTargetBlock(), true);
					if ((!RatConfig.ratsBreakBlockOnHarvest || RatUpgradeUtils.hasUpgrade(this.rat, RatsItemRegistry.RAT_UPGRADE_REPLANTER.get()))) {
						if (block.getBlock() instanceof BonemealableBlock) {
							this.rat.getLevel().setBlockAndUpdate(this.getTargetBlock(), block.getBlock().defaultBlockState());
						}
					}
					this.stop();
				}
			}
		}
	}
}
