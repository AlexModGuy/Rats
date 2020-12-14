package com.github.alexthe666.rats.server.pathfinding;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public interface IPassabilityNavigator {

    int maxSearchNodes();

    boolean isBlockPassable(BlockState state, BlockPos pos, BlockPos entityPos);
}
