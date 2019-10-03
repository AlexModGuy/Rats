package com.github.alexthe666.rats.server.pathfinding;

import com.github.alexthe666.rats.server.blocks.BlockRatCage;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class AStarNode {
    private static final float H = 1;
    public AStarNode start;
    protected double calcCost = -1;
    private AStar aStar;
    private BlockPos pos;
    private BlockPos end;
    private double baseCost;

    public AStarNode(AStar aStar, AStarNode start, BlockPos pos, double cost, BlockPos end) {
        this.aStar = aStar;
        this.start = start;
        this.pos = pos;
        this.baseCost = cost;
        this.end = end;
    }

    public double getCost() {
        if (calcCost == -1) {
            calcCost = pos.distanceSq(end);
        }
        return baseCost + H * calcCost;
    }

    public void generateReachablePos(IBlockAccess world) {
        boolean flag = false;
        for (EnumFacing facing : EnumFacing.values()) {
            BlockPos offset = pos.offset(facing);
            BlockPos offPos;
            if (AStar.isRatTube(world, offset) || world.getBlockState(offset).getBlock() instanceof BlockRatCage) {
                travel(offset, baseCost + 1);
                flag = true;
            } else if ((offPos = AStar.getConnectedToRatTube(world, offset)) != null) {
                travel(offPos, baseCost + 10);
                flag = true;
            }
        }
        if (aStar.includeAir) {
            for (int i = -1; i < 1; i++) {
                for (int j = -1; j < 1; j++) {
                    for (int k = -1; k < 1; k++) {
                        BlockPos offset = pos.add(i, j, k);
                        if (world.isAirBlock(offset) && world.isSideSolid(offset.down(), EnumFacing.UP, false)) {
                            travel(offset, baseCost + 50);
                        }
                    }
                }
            }
        }
    }

    private void travel(BlockPos offset, double cost) {
        AStarNode nt = aStar.getNodeFromMap(offset);
        if (nt.start == null && nt != start) {
            nt.baseCost = cost;
            nt.start = this;
            aStar.shoppingList.add(nt);
            return;
        }
        if (nt.baseCost > cost) {
            nt.baseCost = cost;
            nt.start = this;
        }
    }

    public BlockPos getPos() {
        return pos;
    }
}
