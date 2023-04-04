package com.github.alexthe666.rats.server.block.entity;

import com.github.alexthe666.rats.registry.RatlantisBlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class RatlantisReactorBlockEntity extends BlockEntity {

	public RatlantisReactorBlockEntity(BlockPos pos, BlockState state) {
		super(RatlantisBlockEntityRegistry.RATLANTIS_REACTOR.get(), pos, state);
	}
}
