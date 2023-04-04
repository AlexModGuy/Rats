package com.github.alexthe666.rats.server.block.entity;

import com.github.alexthe666.rats.registry.RatlantisBlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class RatlanteanAutomatonHeadBlockEntity extends BlockEntity {

	public int tickCount;

	public RatlanteanAutomatonHeadBlockEntity(BlockPos pos, BlockState state) {
		super(RatlantisBlockEntityRegistry.AUTOMATON_HEAD.get(), pos, state);
	}

	public static void tick(Level level, BlockPos pos, BlockState state, RatlanteanAutomatonHeadBlockEntity te) {
		te.tickCount++;
	}
}
