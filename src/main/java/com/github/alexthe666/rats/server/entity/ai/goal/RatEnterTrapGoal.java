package com.github.alexthe666.rats.server.entity.ai.goal;

import com.github.alexthe666.rats.registry.RatsBlockRegistry;
import com.github.alexthe666.rats.server.block.RatTrapBlock;
import com.github.alexthe666.rats.server.block.entity.RatTrapBlockEntity;
import com.github.alexthe666.rats.server.entity.rat.AbstractRat;
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
	private final AbstractRat entity;

	public RatEnterTrapGoal(AbstractRat entity) {
		super(entity, 1.0F, 16);
		this.entity = entity;
	}

	public static boolean isTrap(LevelReader world, BlockPos pos) {
		BlockState block = world.getBlockState(pos.above());
		if (block.is(RatsBlockRegistry.RAT_TRAP.get())) {
			BlockEntity entity = world.getBlockEntity(pos.above());
			return entity != null && !block.getValue(RatTrapBlock.SHUT) && !((RatTrapBlockEntity) entity).getBait().isEmpty();
		}
		return false;
	}

	@Override
	public boolean canUse() {
		if (this.entity.canMove() && this.entity.getOwner() != null && !this.entity.isLeashed()) {
			if (!this.entity.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
				return false;
			} else {
				return (this.nextStartTick > 0 || ForgeEventFactory.getMobGriefingEvent(this.entity.getLevel(), this.entity)) && super.canUse();
			}
		} else {
			return false;
		}
	}

	@Override
	public boolean canContinueToUse() {
		return super.canContinueToUse() && !this.entity.isDeadInTrap() && this.entity.getItemInHand(InteractionHand.MAIN_HAND).isEmpty();
	}

	public boolean canSeeTrap() {
		BlockHitResult result = this.entity.getLevel().clip(new ClipContext(this.entity.position(), Vec3.atCenterOf(this.blockPos), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.entity));
		BlockPos pos = result.getBlockPos();
		BlockPos sidePos = result.getBlockPos().relative(result.getDirection());
		return this.entity.getLevel().isEmptyBlock(sidePos) || this.entity.getLevel().isEmptyBlock(pos);
	}

	@Override
	public void tick() {
		super.tick();
		if (this.isReachedTarget()) {
			BlockPos trapPos = this.blockPos.above();
			BlockEntity entity = this.entity.getLevel().getBlockEntity(trapPos);
			if (entity instanceof RatTrapBlockEntity trap && !trap.getBlockState().getValue(RatTrapBlock.SHUT) && !trap.getBait().isEmpty()) {
				double distance = this.entity.distanceToSqr(trapPos.getX(), trapPos.getY(), trapPos.getZ());
				if (distance < 1.0F && this.canSeeTrap() && !this.entity.isDeadInTrap()) {
					ItemStack duplicate = trap.getBait().copy();
					duplicate.setCount(1);
					if (!this.entity.getItemInHand(InteractionHand.MAIN_HAND).isEmpty() && !this.entity.getLevel().isClientSide()) {
						this.entity.spawnAtLocation(this.entity.getItemInHand(InteractionHand.MAIN_HAND), 0.0F);
					}
					this.entity.setItemInHand(InteractionHand.MAIN_HAND, duplicate);
					trap.getBait().shrink(1);
					this.entity.getLevel().sendBlockUpdated(trapPos, this.entity.getLevel().getBlockState(trapPos), this.entity.getLevel().getBlockState(trapPos), 3);
					this.entity.setFleePos(this.blockPos);
				}
			}
		}
	}


	@Override
	protected boolean isValidTarget(LevelReader level, BlockPos pos) {
		return isTrap(level, pos);
	}
}
