package com.github.alexthe666.rats.server.entity.ai.navigation.evaluator;

import com.github.alexthe666.rats.server.block.*;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.misc.RatUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

import java.util.EnumSet;

public class RatNodeEvaluator extends WalkNodeEvaluator {

	@Override
	protected BlockPathTypes evaluateBlockPathType(BlockGetter getter, BlockPos pos, BlockPathTypes types) {
		Block block = getter.getBlockState(pos).getBlock();
		if (this.mob instanceof TamedRat rat) {
			if (block instanceof RatHoleBlock || block instanceof RatTrapBlock || block instanceof RatCageBlock || RatUtils.isOpenRatTube(getter, pos)) {
				types = BlockPathTypes.WALKABLE;
			}
			if (block instanceof RatQuarryPlatformBlock) {
				types = BlockPathTypes.OPEN;
			}
			if (block instanceof SlabBlock) {
				types = BlockPathTypes.WALKABLE;
			}

			if (types == BlockPathTypes.DOOR_WOOD_CLOSED && this.canOpenDoors() && this.canPassDoors()) {
				types = BlockPathTypes.WALKABLE_DOOR;
			}

			if (types == BlockPathTypes.DOOR_OPEN && !this.canPassDoors()) {
				types = BlockPathTypes.BLOCKED;
			}

			if (types == BlockPathTypes.RAIL && !(block instanceof BaseRailBlock) && !(getter.getBlockState(pos.below()).getBlock() instanceof BaseRailBlock)) {
				types = BlockPathTypes.UNPASSABLE_RAIL;
			}

			if (rat.isInCage()) {
				if (block instanceof RatCageBlock || block instanceof RatTubeBlock) {
					types = BlockPathTypes.WALKABLE;
				} else {
					types = BlockPathTypes.BLOCKED;
				}
			}
		}
		return types;
	}
}