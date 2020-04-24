package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.blocks.BlockRatCage;
import com.github.alexthe666.rats.server.blocks.BlockRatHole;
import com.github.alexthe666.rats.server.blocks.BlockRatTrap;
import com.github.alexthe666.rats.server.blocks.BlockRatTube;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.RatUtils;
import net.minecraft.block.Block;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import java.util.EnumSet;

public class RatWalkNodeProcessor extends WalkNodeProcessor {

    @Override
    public PathNodeType getPathNodeType(IBlockReader p_193577_1_, int x, int y, int z, int xSize, int ySize, int zSize, boolean canOpenDoorsIn, boolean canEnterDoorsIn, EnumSet<PathNodeType> nodeTypeEnum, PathNodeType nodeType, BlockPos pos) {
        for (int i = 0; i < xSize; ++i) {
            for (int j = 0; j < ySize; ++j) {
                for (int k = 0; k < zSize; ++k) {
                    int l = i + x;
                    int i1 = j + y;
                    int j1 = k + z;
                    PathNodeType pathnodetype = this.getPathNodeType(p_193577_1_, l, i1, j1);
                    Block block = p_193577_1_.getBlockState(new BlockPos(x, y, z)).getBlock();
                    boolean flag = false;
                    if (entity != null) {
                        if (entity instanceof EntityRat && ((EntityRat) entity).isInCage()) {
                            flag = block instanceof BlockRatTube;
                        }
                    }
                    if (block instanceof BlockRatHole || block instanceof BlockRatTrap || block instanceof BlockRatCage || RatUtils.isOpenRatTube(p_193577_1_, ((EntityRat) entity), new BlockPos(x, y, z)) || flag) {
                        pathnodetype = PathNodeType.WALKABLE;
                    }
                    if (pathnodetype == PathNodeType.DOOR_WOOD_CLOSED && canOpenDoorsIn && canEnterDoorsIn) {
                        pathnodetype = PathNodeType.WALKABLE;
                    }

                    if (pathnodetype == PathNodeType.DOOR_OPEN && !canEnterDoorsIn) {
                        pathnodetype = PathNodeType.BLOCKED;
                    }

                    if (i == 0 && j == 0 && k == 0) {
                        nodeType = pathnodetype;
                    }
                    if(block instanceof BlockRatHole && entity != null && ((EntityRat) entity).isInCage()){
                        pathnodetype = PathNodeType.WALKABLE;
                    }
                    nodeTypeEnum.add(pathnodetype);
                }
            }
        }

        return nodeType;
    }


}
