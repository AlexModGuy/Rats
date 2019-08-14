package com.github.alexthe666.rats.server.pathfinding;

import com.github.alexthe666.rats.server.blocks.BlockRatCage;
import com.github.alexthe666.rats.server.blocks.BlockRatTube;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatTube;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AStar {
    protected List<AStarNode> confirmedList = new ArrayList<AStarNode>();
    protected List<AStarNode> shoppingList = new ArrayList<AStarNode>();

    private Map<Long, AStarNode> nodeMap = new HashMap<Long, AStarNode>();
    private AStarNode start;
    private AStarNode end;
    private int overflowLimit = 0;
    private boolean pathFound = false;
    protected boolean includeAir;
    public AStar(BlockPos startPos, BlockPos endPos, int overflowLimit, boolean includeAir) {
        start = new AStarNode(this, null, startPos, 0, endPos);
        end = new AStarNode(this, start, endPos, 0, endPos);
        this.overflowLimit = overflowLimit;
        this.includeAir = includeAir;
    }

    public BlockPos[] getPath(IBlockAccess world) {
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

    protected static boolean isRatTube(IBlockAccess world, BlockPos offset) {
        return world.getTileEntity(offset) instanceof TileEntityRatTube;
    }

    public static BlockPos getConnectedToRatTube(IBlockAccess world, BlockPos pos) {
        for(EnumFacing facing : EnumFacing.values()){
            BlockPos statePos = pos.offset(facing);
            IBlockState state = world.getBlockState(statePos);
            if(state.getBlock() instanceof BlockRatTube && state.getBlock().getMetaFromState(state) > 0){
                for(int i = 0; i < EnumFacing.values().length; i++){
                    PropertyBool bool = BlockRatTube.ALL_OPEN_PROPS[i];
                    BlockPos offsetInPos = statePos.offset(EnumFacing.values()[i]);
                    if(state.getValue(bool) && ( world.isAirBlock(offsetInPos) || world.getBlockState(offsetInPos).getBlock() instanceof BlockRatCage)){
                        return offsetInPos;
                    }
                }
            }
        }
        return null;
    }
}
