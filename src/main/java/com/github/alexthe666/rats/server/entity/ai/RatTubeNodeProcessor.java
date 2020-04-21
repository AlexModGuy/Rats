package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.blocks.BlockRatCage;
import com.github.alexthe666.rats.server.blocks.BlockRatHole;
import com.github.alexthe666.rats.server.blocks.BlockRatTube;
import com.github.alexthe666.rats.server.pathfinding.AStar;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import java.util.EnumSet;

public class RatTubeNodeProcessor extends WalkNodeProcessor {

    public PathNodeType getPathNodeType(IBlockReader p_193577_1_, int x, int y, int z, int xSize, int ySize, int zSize, boolean canOpenDoorsIn, boolean canEnterDoorsIn, EnumSet<PathNodeType> nodeTypeEnum, PathNodeType nodeType, BlockPos pos) {
        for (int i = 0; i < xSize; ++i) {
            for (int j = 0; j < ySize; ++j) {
                for (int k = 0; k < zSize; ++k) {
                    int l = i + x;
                    int i1 = j + y;
                    int j1 = k + z;
                    PathNodeType pathnodetype = this.getPathNodeType(p_193577_1_, l, i1, j1);
                    Block block = p_193577_1_.getBlockState(new BlockPos(l, i1, j1)).getBlock();
                    if (block instanceof BlockRatTube || block instanceof BlockRatCage || block instanceof BlockRatHole) {
                        pathnodetype = PathNodeType.WALKABLE;
                    } else {
                        if (block instanceof AirBlock && AStar.getConnectedToRatTube(p_193577_1_, new BlockPos(l, i1, j1)) != null) {
                            pathnodetype = PathNodeType.WALKABLE;
                        }else{
                            pathnodetype = PathNodeType.BLOCKED;
                        }
                    }
                    if (i == 0 && j == 0 && k == 0) {
                        nodeType = pathnodetype;
                    }
                    nodeTypeEnum.add(pathnodetype);
                }
            }
        }

        return nodeType;
    }
}
