package com.github.alexthe666.rats.server.entity.ai.navigation.evaluator;

import com.github.alexthe666.rats.server.block.*;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.misc.RatUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

import java.util.EnumSet;

public class RatNodeEvaluator extends WalkNodeEvaluator {

	@Override
	public BlockPathTypes getBlockPathTypes(BlockGetter getter, int x, int y, int z, EnumSet<BlockPathTypes> nodeTypeEnum, BlockPathTypes nodeType, BlockPos pos) {
		for (int i = 0; i < this.entityWidth; ++i) {
			for (int j = 0; j < this.entityHeight; ++j) {
				for (int k = 0; k < this.entityDepth; ++k) {
					int l = i + x;
					int i1 = j + y;
					int j1 = k + z;
					BlockPathTypes pathnodetype = this.getBlockPathType(getter, l, i1, j1);
					BlockPos pos2 = new BlockPos(x, y, z);
					Block block = getter.getBlockState(pos2).getBlock();
					if (this.mob instanceof TamedRat rat) {

						if (block instanceof RatHoleBlock || block instanceof RatQuarryPlatformBlock || block instanceof RatTrapBlock || block instanceof RatCageBlock || RatUtils.isOpenRatTube(getter, new BlockPos(x, y, z))) {
							pathnodetype = BlockPathTypes.WALKABLE;
						}
						if (block instanceof SlabBlock || block instanceof FenceBlock) {
							pathnodetype = BlockPathTypes.WALKABLE;
						}

						if (pathnodetype == BlockPathTypes.DOOR_WOOD_CLOSED && this.canOpenDoors() && this.canPassDoors()) {
							pathnodetype = BlockPathTypes.WALKABLE;
						}

						if (pathnodetype == BlockPathTypes.DOOR_OPEN && !this.canPassDoors()) {
							pathnodetype = BlockPathTypes.BLOCKED;
						}

						if (i == 0 && j == 0 && k == 0) {
							nodeType = pathnodetype;
						}
						if (rat.isInCage()) {
							if (block instanceof RatCageBlock || block instanceof RatTubeBlock) {
								pathnodetype = BlockPathTypes.WALKABLE;
							} else {
								pathnodetype = BlockPathTypes.BLOCKED;
							}
						}
						nodeTypeEnum.add(pathnodetype);
					}
				}
			}
		}
		return nodeType;
	}
}