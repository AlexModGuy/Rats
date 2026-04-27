package com.github.alexthe666.rats.server.entity.ai.navigation.evaluator;

import com.github.alexthe666.rats.server.block.*;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.misc.RatUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

public class RatNodeEvaluator extends WalkNodeEvaluator {

	@Override
	protected PathType evaluateBlockPathType(BlockGetter getter, BlockPos pos, PathType types) {
		Block block = getter.getBlockState(pos).getBlock();
		if (this.mob instanceof TamedRat rat) {
			if (block instanceof RatHoleBlock || block instanceof RatTrapBlock || block instanceof RatCageBlock || RatUtils.isOpenRatTube(getter, pos)) {
				types = PathType.WALKABLE;
			}
			if (block instanceof RatQuarryPlatformBlock) {
				types = PathType.OPEN;
			}
			if (block instanceof SlabBlock) {
				types = PathType.WALKABLE;
			}

			if (types == PathType.DOOR_WOOD_CLOSED && this.canOpenDoors() && this.canPassDoors()) {
				types = PathType.WALKABLE_DOOR;
			}

			if (types == PathType.DOOR_OPEN && !this.canPassDoors()) {
				types = PathType.BLOCKED;
			}

			if (types == PathType.RAIL && !(block instanceof BaseRailBlock) && !(getter.getBlockState(pos.below()).getBlock() instanceof BaseRailBlock)) {
				types = PathType.UNPASSABLE_RAIL;
			}

			if (rat.isInCage()) {
				if (block instanceof RatCageBlock || block instanceof RatTubeBlock) {
					types = PathType.WALKABLE;
				} else {
					types = PathType.BLOCKED;
				}
			}
		}
		return types;
	}
}