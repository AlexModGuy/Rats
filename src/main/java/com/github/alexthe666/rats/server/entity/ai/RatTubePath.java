package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.RatUtils;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class RatTubePath {
    public BlockPos start;
    public BlockPos end;
    public final int overflow;
    public int nodes;
    public List<BlockPos> list = new ArrayList<>();

    public RatTubePath(BlockPos start, BlockPos end, int overflow) {
        this.start = start;
        this.end = end;
        this.overflow = overflow;
    }

    public void generatePath(World world){
        if(!RatUtils.isRatTube(world, start)){
            return;
        }

    }

    private void addToPath(World world, BlockPos pos){
        for(EnumFacing facing : EnumFacing.values()){
            if(RatUtils.isRatTube(world, pos.offset(facing))){
                list.add(pos.offset(facing));
                nodes++;
            }
        }
    }


}
