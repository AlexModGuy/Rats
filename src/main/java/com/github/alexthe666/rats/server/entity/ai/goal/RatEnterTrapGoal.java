package com.github.alexthe666.rats.server.entity.ai.goal;

import com.github.alexthe666.rats.registry.RatsBlockRegistry;
import com.github.alexthe666.rats.server.block.RatTrapBlock;
import com.github.alexthe666.rats.server.block.entity.RatTrapBlockEntity;
import com.github.alexthe666.rats.server.entity.rat.Rat;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;

public class RatEnterTrapGoal extends RatMoveToBlockGoal {
	private final Rat rat;

	public RatEnterTrapGoal(Rat rat) {
		super(rat, 1.0F, 16);
		this.rat = rat;
	}

	public static boolean isTrap(LevelReader world, BlockPos pos) {
		BlockState block = world.getBlockState(pos.above());
		if (block.is(RatsBlockRegistry.RAT_TRAP.get())) {
			BlockEntity rat = world.getBlockEntity(pos.above());
			return rat != null && !block.getValue(RatTrapBlock.SHUT) && !((RatTrapBlockEntity) rat).getBait().isEmpty();
		}
		return false;
	}

	@Override
	public boolean canUse() {
		if (this.rat.canMove() && this.rat.getOwner() == null && !this.rat.isLeashed()) {
			if (!this.rat.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
				return false;
			} else {
				return (this.nextStartTick > 0 || ForgeEventFactory.getMobGriefingEvent(this.rat.getLevel(), this.rat)) && super.canUse();
			}
		} else {
			return false;
		}
	}

	@Override
	public boolean canContinueToUse() {
		return super.canContinueToUse() && !this.rat.isDeadInTrap() && this.rat.getItemInHand(InteractionHand.MAIN_HAND).isEmpty();
	}

	public boolean canSeeTrap() {
		BlockHitResult result = this.rat.getLevel().clip(new ClipContext(this.rat.position(), Vec3.atCenterOf(this.blockPos), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.rat));
		BlockPos pos = result.getBlockPos();
		BlockPos sidePos = result.getBlockPos().relative(result.getDirection());
		return this.rat.getLevel().isEmptyBlock(sidePos) || this.rat.getLevel().isEmptyBlock(pos);
	}

	@Override
	public void tick() {
		super.tick();
		if (this.isReachedTarget()) {
			BlockPos trapPos = this.blockPos.above();
			BlockEntity rat = this.rat.getLevel().getBlockEntity(trapPos);
			if (rat instanceof RatTrapBlockEntity trap && !trap.getBlockState().getValue(RatTrapBlock.SHUT) && !trap.getBait().isEmpty()) {
				double distance = this.rat.distanceToSqr(trapPos.getX(), trapPos.getY(), trapPos.getZ());
				if (distance < 1.0F && this.canSeeTrap() && !this.rat.isDeadInTrap()) {
					ItemStack duplicate = trap.getBait().copy();
					duplicate.setCount(1);
					if (!this.rat.getItemInHand(InteractionHand.MAIN_HAND).isEmpty() && !this.rat.getLevel().isClientSide()) {
						this.rat.spawnAtLocation(this.rat.getItemInHand(InteractionHand.MAIN_HAND), 0.0F);
					}
					this.rat.setItemInHand(InteractionHand.MAIN_HAND, duplicate);
					trap.getBait().shrink(1);
					this.rat.getLevel().sendBlockUpdated(trapPos, this.rat.getLevel().getBlockState(trapPos), this.rat.getLevel().getBlockState(trapPos), 3);
					this.rat.setFleePos(this.blockPos);
				}
			}
		}
	}


	@Override
	protected boolean isValidTarget(LevelReader level, BlockPos pos) {
		return isTrap(level, pos);
	}
}
