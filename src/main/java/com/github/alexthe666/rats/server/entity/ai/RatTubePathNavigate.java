package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.blocks.BlockRatCage;
import com.github.alexthe666.rats.server.blocks.BlockRatTube;
import com.github.alexthe666.rats.server.entity.EntityRat;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RatTubePathNavigate extends PathNavigateGround {
    public BlockPos targetPosition;

    public RatTubePathNavigate(LivingEntity LivingEntityIn, World worldIn) {
        super(LivingEntityIn, worldIn);
    }

    protected PathFinder getPathFinder() {
        this.nodeProcessor = new RatTubeNodeProcessor();
        this.nodeProcessor.setCanEnterDoors(true);
        return new RatTubePathFinder(this.nodeProcessor, (EntityRat) entity);
    }


    public Path getPathToPos(BlockPos pos) {
        this.targetPosition = pos;
        return super.getPathToPos(pos);
    }

    public Path getPathToLivingEntity(Entity entityIn) {
        this.targetPosition = new BlockPos(entityIn);
        return super.getPathToLivingEntity(entityIn);
    }

    public boolean tryMoveToLivingEntity(Entity entityIn, double speedIn) {
        Path path = this.getPathToLivingEntity(entityIn);
        if (path != null) {
            return this.setPath(path, speedIn);
        } else {
            this.targetPosition = new BlockPos(entityIn);
            this.speed = speedIn;
            return true;
        }
    }

    public void clearPath() {
        super.clearPath();
    }

    public void onUpdateNavigation() {
        ((EntityRat) entity).tubeTarget = targetPosition;
        super.onUpdateNavigation();
    }

    protected boolean canNavigate() {
        return ((EntityRat) entity).inTube();
    }

    protected void pathFollow() {
        super.pathFollow();
    }

    public boolean canEntityStandOnPos(BlockPos pos) {
        return this.world.getBlockState(pos).getBlock() instanceof BlockRatTube || this.world.getBlockState(pos).getBlock() instanceof BlockRatCage;
    }
}