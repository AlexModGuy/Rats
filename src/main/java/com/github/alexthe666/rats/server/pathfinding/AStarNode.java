package com.github.alexthe666.rats.server.pathfinding;

import com.github.alexthe666.rats.server.entity.tile.TileEntityRatTube;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AStarNode {
    private static final float H = 2;
    private AStar aStar;

    public AStarNode start;
    private BlockPos pos;
    private BlockPos end;
    private double baseCost;
    protected double calcCost = -1;

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

    public void generateReachablePos(World world) {
        for (EnumFacing facing : EnumFacing.values()) {
            BlockPos offset = pos.offset(facing);
            if (AStar.isRatTube(world, offset)) {
                travel(offset, baseCost + 1);
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
