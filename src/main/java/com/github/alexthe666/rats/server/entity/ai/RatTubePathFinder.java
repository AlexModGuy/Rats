package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.pathfinding.AStar;
import com.google.common.collect.Sets;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.pathfinding.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nullable;
import java.util.Set;

public class RatTubePathFinder extends PathFinder {

    private final PathHeap path = new PathHeap();
    private final Set<PathPoint> closedSet = Sets.newHashSet();
    private final PathPoint[] pathOptions = new PathPoint[32];
    private final NodeProcessor nodeProcessor;
    private EntityRat rat;

    public RatTubePathFinder(NodeProcessor processor, EntityRat rat) {
        super(processor);
        this.nodeProcessor = processor;
        this.rat = rat;
    }

    @Nullable
    public Path findPath(IBlockAccess worldIn, LivingEntity LivingEntityIn, Entity targetEntity, float maxDistance) {
        return this.findPath(worldIn, LivingEntityIn, targetEntity.posX, targetEntity.getEntityBoundingBox().minY, targetEntity.posZ, maxDistance);
    }

    @Nullable
    public Path findPath(IBlockAccess worldIn, LivingEntity LivingEntityIn, BlockPos targetPos, float maxDistance) {
        return this.findPath(worldIn, LivingEntityIn, (double) ((float) targetPos.getX() + 0.5F), (double) ((float) targetPos.getY() + 0.5F), (double) ((float) targetPos.getZ() + 0.5F), maxDistance);
    }

    @Nullable
    private Path findPath(IBlockAccess worldIn, LivingEntity LivingEntityIn, double x, double y, double z, float maxDistance) {
        this.path.clearPath();
        this.nodeProcessor.init(worldIn, LivingEntityIn);
        PathPoint pathpoint = this.nodeProcessor.getStart();
        PathPoint pathpoint1 = this.nodeProcessor.getPathPointToCoords(x, y, z);
        Path path = this.findPath(worldIn, pathpoint, pathpoint1, maxDistance);
        this.nodeProcessor.postProcess();
        return path;
    }

    @Nullable
    private Path findPath(IBlockAccess worldIn, PathPoint pathFrom, PathPoint pathTo, float maxDistance) {
        BlockPos startPos = new BlockPos(pathFrom.x, pathFrom.y, pathFrom.z);
        BlockPos endPos = new BlockPos(pathTo.x, pathTo.y, pathTo.z);

        AStar aStar = new AStar(startPos, endPos, 1000, false);
        BlockPos[] pathBlocks = aStar.getPath(worldIn);
        PathPoint[] fromPos = new PathPoint[pathBlocks.length - 1];
        for (int i = 1; i < pathBlocks.length; i++) {
            fromPos[i - 1] = new PathPoint(pathBlocks[i].getX(), pathBlocks[i].getY(), pathBlocks[i].getZ());
        }
        /*
        Random random =     new Random();
        if(pathBlocks.length > 0)
        rat.world.setBlockState(pathBlocks[pathBlocks.length-1].down(), random.nextBoolean() ? Blocks.DIAMOND_BLOCK.getDefaultState() : Blocks.GOLD_BLOCK.getDefaultState());
        */
        return new Path(fromPos);
    }

}
