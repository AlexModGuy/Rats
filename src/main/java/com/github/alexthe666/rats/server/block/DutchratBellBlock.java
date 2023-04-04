package com.github.alexthe666.rats.server.block;

import com.github.alexthe666.rats.registry.RatlantisBlockEntityRegistry;
import com.github.alexthe666.rats.registry.RatsSoundRegistry;
import com.github.alexthe666.rats.server.block.entity.DutchratBellBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BellBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BellAttachType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class DutchratBellBlock extends BellBlock implements CustomItemRarity {

	public DutchratBellBlock(BlockBehaviour.Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(BlockStateProperties.BELL_ATTACHMENT, BellAttachType.FLOOR).setValue(POWERED, false));
	}

	@Override
	public Rarity getRarity() {
		return Rarity.RARE;
	}

	@Override
	public boolean canEntityDestroy(BlockState state, BlockGetter level, BlockPos pos, Entity entity) {
		if (level.getBlockEntity(pos) instanceof DutchratBellBlockEntity bell && !bell.canDestroyBell()) {
			return false;
		}
		return super.canEntityDestroy(state, level, pos, entity);
	}

	@Override
	public boolean onHit(Level level, BlockState state, BlockHitResult result, @Nullable Player player, boolean alwaysTrueIdk) {
		Direction direction = result.getDirection();
		BlockPos blockpos = result.getBlockPos();
		boolean flag = !alwaysTrueIdk || this.canRingFrom(state, direction, result.getLocation().y() - (double) blockpos.getY());
		if (flag && player != null) {
			boolean flag1 = this.attemptToRing(player, level, blockpos, direction);
			if (flag1) {
				player.awardStat(Stats.BELL_RING);
			}

			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean attemptToRing(@Nullable Entity entity, Level level, BlockPos pos, @Nullable Direction direction) {
		BlockEntity be = level.getBlockEntity(pos);
		if (!level.isClientSide() && be instanceof DutchratBellBlockEntity bell) {
			if (direction == null) {
				direction = level.getBlockState(pos).getValue(FACING);
			}
			if (bell.canDestroyBell()) {
				this.playRingSound(level, pos, !bell.canDestroyBell());
				bell.onHit(level, direction);
			}
			return true;
		} else {
			return false;
		}
	}

	private boolean canRingFrom(BlockState state, Direction direction, double distance) {
		if (direction.getAxis() != Direction.Axis.Y && !(distance > (double) 0.8124F)) {
			Direction realDir = state.getValue(FACING);
			BellAttachType attachment = state.getValue(BlockStateProperties.BELL_ATTACHMENT);
			return switch (attachment) {
				case FLOOR -> realDir.getAxis() == direction.getAxis();
				case SINGLE_WALL, DOUBLE_WALL -> realDir.getAxis() != direction.getAxis();
				case CEILING -> true;
			};
		} else {
			return false;
		}
	}

	private void playRingSound(Level level, BlockPos pos, boolean alreadySummoning) {
		if (!alreadySummoning) {
			if (level.isDay() || level.getCurrentDifficultyAt(pos).getDifficulty() == Difficulty.PEACEFUL) {
				level.playSound(null, pos, SoundEvents.BELL_BLOCK, SoundSource.BLOCKS, 2.0F, 1.0F);
			} else {
				level.playSound(null, pos, RatsSoundRegistry.DUTCHRAT_BELL.get(), SoundSource.BLOCKS, 2.0F, 1.0F);
			}
		}
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new DutchratBellBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, RatlantisBlockEntityRegistry.DUTCHRAT_BELL.get(), DutchratBellBlockEntity::tick);
	}

	@Override
	public boolean isPathfindable(BlockState state, BlockGetter getter, BlockPos pos, PathComputationType type) {
		return false;
	}
}
