package com.github.alexthe666.rats.server.entity.ai.navigation.evaluator;

import com.github.alexthe666.rats.server.block.*;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.misc.RatUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.pathfinder.PathfindingContext;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

public class RatNodeEvaluator extends WalkNodeEvaluator {

	@Override
	public PathType getPathType(PathfindingContext context, int x, int y, int z) {
		PathType types = super.getPathType(context, x, y, z);
		BlockPos pos = new BlockPos(x, y, z);
		Block block = context.getBlockState(pos).getBlock();
		
		if (this.mob instanceof TamedRat rat) {
			if (block instanceof RatHoleBlock || block instanceof RatTrapBlock || block instanceof RatCageBlock || RatUtils.isOpenRatTube(context.level(), pos)) {
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

			if (types == PathType.RAIL && !(block instanceof BaseRailBlock) && !(context.getBlockState(pos.below()).getBlock() instanceof BaseRailBlock)) {
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






