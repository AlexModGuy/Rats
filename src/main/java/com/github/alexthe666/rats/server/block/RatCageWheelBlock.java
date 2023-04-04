package com.github.alexthe666.rats.server.block;

import com.github.alexthe666.rats.registry.RatsBlockEntityRegistry;
import com.github.alexthe666.rats.server.block.entity.RatCageWheelBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class RatCageWheelBlock extends RatCageDecoratedBlock {

	public RatCageWheelBlock(Properties properties) {
		super(properties);
	}

	@Nullable
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new RatCageWheelBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, RatsBlockEntityRegistry.RAT_CAGE_WHEEL.get(), RatCageWheelBlockEntity::tick);
	}
}
