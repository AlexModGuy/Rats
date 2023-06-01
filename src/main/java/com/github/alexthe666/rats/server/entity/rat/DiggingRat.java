package com.github.alexthe666.rats.server.entity.rat;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.data.tags.RatsBlockTags;
import com.github.alexthe666.rats.registry.RatsBlockRegistry;
import com.github.alexthe666.rats.registry.RatsSoundRegistry;
import com.github.alexthe666.rats.server.block.RatHoleBlock;
import com.github.alexthe666.rats.server.block.entity.RatHoleBlockEntity;
import com.github.alexthe666.rats.server.entity.ai.goal.RatRaidChestsGoal;
import com.github.alexthe666.rats.server.entity.ai.goal.RatRaidCropsGoal;
import com.github.alexthe666.rats.server.misc.RatPathingHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.Nullable;

public abstract class DiggingRat extends AbstractRat {

	public int breakingTime;
	public int previousBreakProgress = -1;
	private BlockPos diggingPos = null;
	private int digCooldown = 0;

	protected DiggingRat(EntityType<? extends TamableAnimal> type, Level level) {
		super(type, level);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(4, new RatRaidChestsGoal(this));
		this.goalSelector.addGoal(4, new RatRaidCropsGoal(this));
	}

	@Override
	public void aiStep() {
		super.aiStep();

		if (this.canDigThroughBlocks() && ForgeEventFactory.getMobGriefingEvent(this.getLevel(), this)) {
			if (this.getOwner() == null && this.getNavigation().isDone() && this.digCooldown-- <= 0 && RatConfig.ratsDigBlocks) {
				this.findDigTarget();
				this.digTarget();
			}
		}
	}

	@Override
	public boolean canMove() {
		return super.canMove() && this.diggingPos == null;
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putInt("DigCooldown", this.digCooldown);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		this.digCooldown = tag.getInt("DigCooldown");
	}

	public boolean isOnDiggingCooldown() {
		return this.digCooldown > 0;
	}

	private void digTarget() {
		if (this.diggingPos != null) {
			++this.breakingTime;
			this.getLookControl().setLookAt(this.diggingPos.getCenter());
			int i = (int) ((float) this.breakingTime / 160.0F * 10.0F);
			if (this.getNavigation().getPath() != null) {
				this.getNavigation().stop();
			}
			this.setDeltaMovement(0, 0, 0);
			if (this.breakingTime % 40 == 0) {
				this.playSound(RatsSoundRegistry.RAT_DIG.get(), this.getSoundVolume(), this.getVoicePitch());
			}
			if (i != this.previousBreakProgress) {
				this.getLevel().destroyBlockProgress(this.getId(), this.diggingPos, i);
				this.previousBreakProgress = i;
			}

			if (this.breakingTime == 160) {
				this.breakingTime = 0;
				this.previousBreakProgress = -1;
				BlockState prevState = this.getLevel().getBlockState(this.diggingPos);
				this.getLevel().setBlockAndUpdate(this.diggingPos, RatsBlockRegistry.RAT_HOLE.get().defaultBlockState());
				for (Direction direction : Direction.Plane.HORIZONTAL) {
					boolean empty = this.getLevel().isEmptyBlock(this.diggingPos.relative(direction));
					this.getLevel().getBlockState(this.diggingPos).setValue(RatHoleBlock.PROPERTY_BY_DIRECTION.get(direction), empty);
				}
				if (this.getLevel().getBlockState(this.diggingPos).is(RatsBlockRegistry.RAT_HOLE.get())) {
					BlockEntity be = this.getLevel().getBlockEntity(this.diggingPos);
					if (be instanceof RatHoleBlockEntity hole) {
						hole.setImitatedBlockState(prevState);
					}
				}

				this.digCooldown = 3000;
				this.diggingPos = null;
			}
			if (this.diggingPos != null && this.distanceToSqr(this.diggingPos.getX(), this.diggingPos.getY(), this.diggingPos.getZ()) > 2F) {
				this.breakingTime = 0;
				this.previousBreakProgress = -1;
				this.getLevel().destroyBlockProgress(this.getId(), this.diggingPos, 0);
				this.diggingPos = null;
			}
		} else {
			this.breakingTime = 0;
			this.previousBreakProgress = -1;
		}
	}

	private void findDigTarget() {
		if (this.getNavigation().getTargetPos() != null) {
			BlockPos digPos = this.rayTraceBlockPos(this.getNavigation().getTargetPos());
			if (digPos != null && this.distanceToSqr(digPos.getX(), digPos.getY(), digPos.getZ()) < 2) {
				if (this.getLevel().getBlockEntity(digPos) == null || this.getLevel().getBlockEntity(digPos) instanceof RatHoleBlockEntity) {
					if (this.canDigBlock(this.getLevel(), digPos) && digPos.getY() == (int) Math.round(this.getY())) {
						this.diggingPos = digPos;
					}
				}
			}
		}
	}

	@Nullable
	public BlockPos rayTraceBlockPos(BlockPos targetPos) {
		BlockHitResult result = RatPathingHelper.clipWithConditions(this.getLevel(), new ClipContext(this.position(), Vec3.atCenterOf(targetPos), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this), false);
		BlockPos pos = result.getBlockPos();
		BlockPos sidePos = result.getBlockPos().relative(result.getDirection());
		if (!this.getLevel().isEmptyBlock(sidePos)) {
			return sidePos;
		} else if (!this.getLevel().isEmptyBlock(pos)) {
			return pos;
		} else {
			return null;
		}
	}

	public boolean canDigThroughBlocks() {
		return true;
	}

	private boolean canDigBlock(Level level, BlockPos pos) {
		BlockState state = level.getBlockState(pos);
		return (state.is(RatsBlockTags.DIGGABLE_BLOCKS) && state.isSolidRender(level, pos));
	}

	@Override
	public boolean shouldPlayIdleAnimations() {
		return this.diggingPos == null && super.shouldPlayIdleAnimations();
	}
}
