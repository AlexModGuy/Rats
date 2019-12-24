package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.blocks.BlockRatCage;
import com.github.alexthe666.rats.server.blocks.BlockRatTube;
import com.github.alexthe666.rats.server.entity.EntityRat;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RatTubePathNavigate extends GroundPathNavigator {
    public BlockPos targetPosition;

    public RatTubePathNavigate(MobEntity LivingEntityIn, World worldIn) {
        super(LivingEntityIn, worldIn);
    }

    protected PathFinder getPathFinder() {
        this.nodeProcessor = new RatTubeNodeProcessor();
        this.nodeProcessor.setCanEnterDoors(true);
        return new RatTubePathFinder(this.nodeProcessor, 64, (EntityRat) entity);
    }


    public Path getPathToPos(BlockPos pos, int idk) {
        this.targetPosition = pos;
        return super.getPathToPos(pos, idk);
    }

    public Path getPathToLivingEntity(Entity entityIn, int idk) {
        this.targetPosition = new BlockPos(entityIn);
        return super.getPathToEntityLiving(entityIn, idk);
    }

    public boolean tryMoveToLivingEntity(Entity entityIn, double speedIn) {
        Path path = this.getPathToLivingEntity(entityIn, 0);
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

    public void tick() {
        ((EntityRat) entity).tubeTarget = targetPosition;
        super.tick();
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