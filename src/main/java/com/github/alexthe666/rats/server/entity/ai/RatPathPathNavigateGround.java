package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.blocks.BlockRatTube;
import com.github.alexthe666.rats.server.entity.EntityRat;
import net.minecraft.entity.Entity;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RatPathPathNavigateGround extends PathNavigateGround {

    public RatPathPathNavigateGround(EntityRat entitylivingIn, World worldIn) {
        super(entitylivingIn, worldIn);
    }

    protected PathFinder getPathFinder() {
        this.nodeProcessor = new RatWalkNodeProcessor();
        this.nodeProcessor.setCanEnterDoors(true);
        this.nodeProcessor.setCanSwim(true);
        return new RatPathFinder(this.nodeProcessor, (EntityRat)entity);
    }

    public boolean canEntityStandOnPos(BlockPos pos) {
        return this.world.getBlockState(pos.down()).isFullBlock() || this.world.getBlockState(pos).getBlock() instanceof BlockRatTube;
    }
}
