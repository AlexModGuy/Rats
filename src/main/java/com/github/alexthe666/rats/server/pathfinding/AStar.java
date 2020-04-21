package com.github.alexthe666.rats.server.pathfinding;

import com.github.alexthe666.rats.server.blocks.BlockRatCage;
import com.github.alexthe666.rats.server.blocks.BlockRatTube;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatTube;
import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AStar {
    protected List<AStarNode> confirmedList = new ArrayList<AStarNode>();
    protected List<AStarNode> shoppingList = new ArrayList<AStarNode>();
    protected boolean includeAir;
    private Map<Long, AStarNode> nodeMap = new HashMap<Long, AStarNode>();
    private AStarNode start;
    private AStarNode end;
    private int overflowLimit = 0;
    private boolean pathFound = false;

    public AStar(BlockPos startPos, BlockPos endPos, int overflowLimit, boolean includeAir) {
        start = new AStarNode(this, null, startPos, 0, endPos);
        end = new AStarNode(this, start, endPos, 0, endPos);
        this.overflowLimit = overflowLimit;
        this.includeAir = includeAir;
    }

    protected static boolean isRatTube(IWorldReader world, BlockPos offset) {
        return world.getTileEntity(offset) instanceof TileEntityRatTube;
    }

    public static BlockPos getConnectedToRatTube(IBlockReader world, BlockPos pos) {
        for (Direction facing : Direction.values()) {
            BlockPos statePos = pos.offset(facing);
            BlockState state = world.getBlockState(statePos);
            if (state.getBlock() instanceof BlockRatTube) {
                for (int i = 0; i < Direction.values().length; i++) {
                    BooleanProperty bool = BlockRatTube.ALL_OPEN_PROPS[i];
                    BlockPos offsetInPos = statePos.offset(Direction.values()[i]);
                    if (state.get(bool) && (world.getBlockState(offsetInPos).isAir(world, offsetInPos) || world.getBlockState(offsetInPos).getBlock() instanceof BlockRatCage)) {
                        return offsetInPos;
                    }
                }
            }
        }
        return null;
    }

    public BlockPos[] getPath(IWorldReader world) {
        shoppingList.add(start);
        while (confirmedList.size() < overflowLimit && !pathFound && shoppingList.size() > 0) {
            AStarNode n = shoppingList.get(0);
            for (AStarNode nt : shoppingList) {
                if (nt.getCost() < n.getCost()) {
                    n = nt;
                }
            }
            if (n.calcCost < 1) {
                pathFound = true;
                end = n;
                break;
            }
            n.generateReachablePos(world);
            shoppingList.remove(n);
            confirmedList.add(n);
        }
        if (!pathFound) {
            int length = confirmedList.size();
            BlockPos[] locations = new BlockPos[length];
            for (int i = 0; i < length; i++) {
                locations[i] = confirmedList.get(i).getPos();
            }
            return locations;
        }
        int length = 1;
        AStarNode n = end;
        while (n.start != null) {
            n = n.start;
            length++;
        }
        BlockPos[] locations = new BlockPos[length];
        n = end;
        for (int i = length - 1; i > 0; i--) {
            locations[i] = n.getPos();
            n = n.start;
        }
        locations[0] = start.getPos();
        return locations;
    }

    public AStarNode getNodeFromMap(BlockPos pos) {
        Long toLong = pos.toLong();
        if (nodeMap.get(toLong) == null) {
            AStarNode node = new AStarNode(this, null, pos, 0, end.getPos());
            nodeMap.put(pos.toLong(), node);
            return node;
        } else {
            return nodeMap.get(toLong);
        }
    }
}
