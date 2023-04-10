package com.github.alexthe666.rats.server.entity.ai.goal;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.server.entity.rat.DiggingRat;
import com.github.alexthe666.rats.server.misc.RatPathingHelper;
import com.github.alexthe666.rats.server.misc.RatUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.List;

public class RatRaidCropsGoal extends RatMoveToBlockGoal {
	private final DiggingRat rat;

	public RatRaidCropsGoal(DiggingRat rat) {
		super(rat, 1.0F, 16);
		this.rat = rat;
	}

	@Override
	public boolean canUse() {
		if (!this.rat.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
			return false;
		}
		if (!this.rat.canMove() || this.rat.raidCooldown > 0 || !RatConfig.ratsBreakCrops || this.rat.getOwner() != null) {
			return false;
		}

		if (this.nextStartTick <= 0) {
			if (!ForgeEventFactory.getMobGriefingEvent(this.rat.getLevel(), this.rat)) {
				return false;
			}
		}
		return super.canUse();
	}

	@Override
	public boolean canContinueToUse() {
		return super.canContinueToUse() && this.rat.getItemInHand(InteractionHand.MAIN_HAND).isEmpty();
	}

	@Override
	public void tick() {
		super.tick();
		if (this.isReachedTarget()) {
			BlockPos cropsPos = this.blockPos.above();
			BlockState block = this.rat.getLevel().getBlockState(cropsPos);
			double distance = this.rat.distanceToSqr(cropsPos.getX(), cropsPos.getY(), cropsPos.getZ());
			if (distance < 3.5F) {
				LootContext.Builder loot = new LootContext.Builder((ServerLevel) this.rat.getLevel()).withParameter(LootContextParams.TOOL, ItemStack.EMPTY).withParameter(LootContextParams.ORIGIN, this.rat.position()).withRandom(this.rat.getRandom()).withLuck(1.0F);
				List<ItemStack> drops = block.getBlock().getDrops(block, loot);
				if (drops.isEmpty()) {
					this.stop();
					return;
				} else {
					int count = 0;
					for (ItemStack stack : drops) {
						if (count == 0) {
							ItemStack duplicate = stack.copy();
							duplicate.setCount(1);
							if (!this.rat.getItemInHand(InteractionHand.MAIN_HAND).isEmpty() && !this.rat.getLevel().isClientSide()) {
								this.rat.spawnAtLocation(this.rat.getItemInHand(InteractionHand.MAIN_HAND), 0.0F);
							}
							this.rat.setItemInHand(InteractionHand.MAIN_HAND, duplicate);
							drops.remove(stack);
						}
						count++;
					}
					this.rat.getLevel().destroyBlock(cropsPos, false);
					drops.forEach(stack -> this.rat.spawnAtLocation(stack, 0.0F));
				}
				this.rat.setFleePos(cropsPos);
				this.rat.raidCooldown = 200;
			}
		}
	}

	@Override
	protected boolean isValidTarget(LevelReader reader, BlockPos pos) {
		BlockState state = reader.getBlockState(pos);
		BlockState cropState = reader.getBlockState(pos.above());
		if (RatUtils.canRatBreakBlock(this.rat.getLevel(), pos.above(), this.rat)) {
			if (state.getBlock() instanceof FarmBlock) {
				if (!RatPathingHelper.canSeeOrDigToBlock(this.rat, pos.above())) return false;
				if (cropState.getBlock() instanceof CropBlock crop) return crop.isMaxAge(cropState);
				return cropState.is(BlockTags.CROPS) && !(cropState.getBlock() instanceof StemBlock);
			}
		}
		return false;
	}
}
