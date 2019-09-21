package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.RatUtils;
import com.github.alexthe666.rats.server.pathfinding.AStar;
import com.google.common.collect.Sets;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RatPathFinder extends PathFinder {

    private final PathHeap path = new PathHeap();
    private final Set<PathPoint> closedSet = Sets.newHashSet();
    private final PathPoint[] pathOptions = new PathPoint[32];
    private final NodeProcessor nodeProcessor;
    private EntityRat rat;

    public RatPathFinder(NodeProcessor processor, EntityRat rat) {
        super(processor);
        this.rat = rat;
        this.nodeProcessor = processor;
    }

    @Nullable
    public Path findPath(IBlockAccess worldIn, EntityLiving entitylivingIn, Entity targetEntity, float maxDistance) {
        return this.findPath(worldIn, entitylivingIn, targetEntity.posX, targetEntity.getEntityBoundingBox().minY, targetEntity.posZ, maxDistance);
    }

    @Nullable
    public Path findPath(IBlockAccess worldIn, EntityLiving entitylivingIn, BlockPos targetPos, float maxDistance) {
        return this.findPath(worldIn, entitylivingIn, (double) ((float) targetPos.getX() + 0.5F), (double) ((float) targetPos.getY() + 0.5F), (double) ((float) targetPos.getZ() + 0.5F), maxDistance);
    }

    @Nullable
    private Path findPath(IBlockAccess worldIn, EntityLiving entitylivingIn, double x, double y, double z, float maxDistance) {
        this.path.clearPath();
        this.nodeProcessor.init(worldIn, entitylivingIn);
        PathPoint pathpoint = this.nodeProcessor.getStart();
        PathPoint pathpoint1 = this.nodeProcessor.getPathPointToCoords(x, y, z);
        Path path = this.findPath(worldIn, pathpoint, pathpoint1, maxDistance);
        this.nodeProcessor.postProcess();
        return path;
    }

    @Nullable
    private Path findPath(IBlockAccess worldIn, PathPoint pathFrom, PathPoint pathTo, float maxDistance) {
        Path vanillaPath = findVanillaPath(pathFrom, pathTo, maxDistance);
        BlockPos startPos = new BlockPos(pathFrom.x, pathFrom.y, pathFrom.z);
        BlockPos endPos = new BlockPos(pathTo.x, pathTo.y, pathTo.z);
        List<BlockPos> openTubes = new ArrayList<>();
        PathPoint tubePathEnd = pathFrom;
        Path tubePath = null;
        if (rat.getDistanceSqToCenter(endPos) > 10 && !rat.isInCage() && rat.isTamed()) {
            for (BlockPos pos : BlockPos.getAllInBox(startPos.add(-10, -10, -10), startPos.add(10, 10, 10))) {
                if (RatUtils.isOpenRatTube(worldIn, rat, pos)) {
                    openTubes.add(pos);
                }
            }
            for (BlockPos pos : openTubes) {
                BlockPos tubeOffset = RatUtils.offsetTubeEntrance(worldIn, pos);
                AStar aStar = new AStar(pos, endPos, 1000, true);
                BlockPos[] pathBlocks = aStar.getPath(worldIn);
                if (pathBlocks.length > 1) {
                    Path path = findTubePath(worldIn, pathFrom, tubeOffset, pos, maxDistance);
                    if (path != null && path.getFinalPathPoint() != null) {
                        tubePath = path;
                        tubePathEnd = new PathPoint(pathBlocks[pathBlocks.length - 1].getX(), pathBlocks[pathBlocks.length - 1].getY(), pathBlocks[pathBlocks.length - 1].getZ());
                        break;
                    }
                }
            }
        }
        if (tubePath != null && rat.shouldBeSuckedIntoTube()) {
            return tubePath;
        }
        return vanillaPath;
    }

    public Path findTubePath(IBlockAccess worldIn, PathPoint pathFrom, BlockPos tubePos, BlockPos tubeActualPos, float maxDistance) {
        PathPoint pathTube = new PathPoint(tubePos.getX(), tubePos.getY(), tubePos.getZ());
        PathPoint pathInTube = new PathPoint(tubeActualPos.getX(), tubeActualPos.getY(), tubeActualPos.getZ());
        pathFrom.totalPathDistance = 0.0F;
        pathFrom.distanceToNext = pathFrom.distanceManhattan(pathTube);
        pathFrom.distanceToTarget = pathFrom.distanceToNext;
        Path path = new Path(new PathPoint[]{pathFrom, pathTube, pathInTube});
        return path;
    }


    private Path createPath(PathPoint start, PathPoint end) {
        int i = 1;

        for (PathPoint pathpoint = end; pathpoint.previous != null; pathpoint = pathpoint.previous) {
            ++i;
        }

        PathPoint[] apathpoint = new PathPoint[i];
        PathPoint pathpoint1 = end;
        --i;

        for (apathpoint[i] = end; pathpoint1.previous != null; apathpoint[i] = pathpoint1) {
            pathpoint1 = pathpoint1.previous;
            --i;
        }

        return new Path(apathpoint);
    }


    @Nullable
    private Path findVanillaPath(PathPoint pathFrom, PathPoint pathTo, float maxDistance) {
        pathFrom.totalPathDistance = 0.0F;
        pathFrom.distanceToNext = pathFrom.distanceManhattan(pathTo);
        pathFrom.distanceToTarget = pathFrom.distanceToNext;
        this.path.clearPath();
        this.closedSet.clear();
        this.path.addPoint(pathFrom);
        PathPoint pathpoint = pathFrom;
        int i = 0;

        while (!this.path.isPathEmpty()) {
            ++i;

            if (i >= 200) {
                break;
            }

            PathPoint pathpoint1 = this.path.dequeue();

            if (pathpoint1.equals(pathTo)) {
                pathpoint = pathTo;
                break;
            }

            if (pathpoint1.distanceManhattan(pathTo) < pathpoint.distanceManhattan(pathTo)) {
                pathpoint = pathpoint1;
            }

            pathpoint1.visited = true;
            int j = this.nodeProcessor.findPathOptions(this.pathOptions, pathpoint1, pathTo, maxDistance);

            for (int k = 0; k < j; ++k) {
                PathPoint pathpoint2 = this.pathOptions[k];
                float f = pathpoint1.distanceManhattan(pathpoint2);
                pathpoint2.distanceFromOrigin = pathpoint1.distanceFromOrigin + f;
                pathpoint2.cost = f + pathpoint2.costMalus;
                float f1 = pathpoint1.totalPathDistance + pathpoint2.cost;

                if (pathpoint2.distanceFromOrigin < maxDistance && (!pathpoint2.isAssigned() || f1 < pathpoint2.totalPathDistance)) {
                    pathpoint2.previous = pathpoint1;
                    pathpoint2.totalPathDistance = f1;
                    pathpoint2.distanceToNext = pathpoint2.distanceManhattan(pathTo) + pathpoint2.costMalus;

                    if (pathpoint2.isAssigned()) {
                        this.path.changeDistance(pathpoint2, pathpoint2.totalPathDistance + pathpoint2.distanceToNext);
                    } else {
                        pathpoint2.distanceToTarget = pathpoint2.totalPathDistance + pathpoint2.distanceToNext;
                        this.path.addPoint(pathpoint2);
                    }
                }
            }
        }

        if (pathpoint == pathFrom) {
            return null;
        } else {
            Path path = this.createPath(pathFrom, pathpoint);
            return path;
        }
    }
}
