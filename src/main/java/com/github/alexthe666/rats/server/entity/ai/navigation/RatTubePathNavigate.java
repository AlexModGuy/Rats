package com.github.alexthe666.rats.server.entity.ai.navigation;

import com.github.alexthe666.rats.server.blocks.BlockRatCage;
import com.github.alexthe666.rats.server.blocks.BlockRatTube;
import com.github.alexthe666.rats.server.entity.EntityRat;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RatTubePathNavigate extends GroundPathNavigator {
    public BlockPos targetPosition;
    private Path prevPath;
    public RatTubePathNavigate(MobEntity LivingEntityIn, World worldIn) {
        super(LivingEntityIn, worldIn);
    }

    public Path getPathToPos(BlockPos pos, int idk) {
        this.targetPosition = pos;
        return generatePath();
    }

    protected PathFinder getPathFinder(int p_179679_1_) {
        this.nodeProcessor = new RatWalkNodeProcessor();
        this.nodeProcessor.setCanEnterDoors(true);
        this.nodeProcessor.setCanSwim(true);
        return new PathFinder(this.nodeProcessor, p_179679_1_);
    }

    public Path getPathToLivingEntity(Entity entityIn, int idk) {
        this.targetPosition = new BlockPos(entityIn.getPositionVec());
        return generatePath();
    }

    public boolean tryMoveToEntityLiving(Entity entityIn, double speedIn) {
        Path path = this.getPathToLivingEntity(entityIn, 1);
        if (path != null) {
            return this.setPath(path, speedIn);
        } else {
            this.targetPosition = new BlockPos(entityIn.getPositionVec());
            this.speed = speedIn;
            return true;
        }
    }

    private Path generatePath(){
        if(prevPath == null || prevPath.isFinished() || prevPath.getFinalPathPoint() != null && prevPath.getFinalPathPoint().x != targetPosition.getX() && prevPath.getFinalPathPoint().y != targetPosition.getY() && prevPath.getFinalPathPoint().y != targetPosition.getZ()) {
            BlockPos startPos = new BlockPos(this.getEntityPosition());
        }
        return prevPath;
    }

    public void tick() {
        ((EntityRat) entity).tubeTarget = targetPosition;
        super.tick();
    }

    protected boolean canNavigate() {
        return ((EntityRat) entity).inTube();
    }

    public boolean canEntityStandOnPos(BlockPos pos) {
        return this.world.getBlockState(pos).getBlock() instanceof BlockRatTube || this.world.getBlockState(pos).getBlock() instanceof BlockRatCage;
    }
}