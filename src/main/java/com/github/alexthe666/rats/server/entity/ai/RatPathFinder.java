package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.RatUtils;
import com.github.alexthe666.rats.server.pathfinding.AStar;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.pathfinding.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//TODO: Rewrite entire pathfinding code for 1.14
public class RatPathFinder extends PathFinder {

    private final PathHeap path = new PathHeap();
    private final Set<PathPoint> closedSet = Sets.newHashSet();
    private final PathPoint[] pathOptions = new PathPoint[32];
    private final NodeProcessor nodeProcessor;
    private EntityRat rat;
    private int range;

    public RatPathFinder(NodeProcessor processor, int range, EntityRat rat) {
        super(processor, range);
        this.rat = rat;
        this.range = range;
        this.nodeProcessor = processor;
    }

    @Nullable
    public Path func_224775_a(IWorldReader worldIn, MobEntity LivingEntityIn, Entity targetEntity, float maxDistance) {
        return this.findPath(worldIn, LivingEntityIn, targetEntity.posX, targetEntity.getBoundingBox().minY, targetEntity.posZ, maxDistance);
    }

    @Nullable
    public Path findPath(IWorldReader worldIn, MobEntity LivingEntityIn, BlockPos targetPos, float maxDistance) {
        return this.findPath(worldIn, LivingEntityIn, (double) ((float) targetPos.getX() + 0.5F), (double) ((float) targetPos.getY() + 0.5F), (double) ((float) targetPos.getZ() + 0.5F), maxDistance);
    }

    @Nullable
    private Path findPath(IWorldReader worldIn, MobEntity LivingEntityIn, double x, double y, double z, float maxDistance) {
        this.path.clearPath();
        this.nodeProcessor.init(worldIn, LivingEntityIn);
        PathPoint pathpoint = this.nodeProcessor.getStart();
        PathPoint pathpoint1 = this.nodeProcessor.func_224768_a(x, y, z);
        Path path = this.findPath(worldIn, pathpoint, pathpoint1, maxDistance);
        this.nodeProcessor.postProcess();
        return path;
    }

    @Nullable
    private Path findPath(IWorldReader worldIn, PathPoint pathFrom, PathPoint pathTo, float maxDistance) {
        BlockPos startPos = new BlockPos(pathFrom.x, pathFrom.y, pathFrom.z);
        BlockPos endPos = new BlockPos(pathTo.x, pathTo.y, pathTo.z);
        List<BlockPos> openTubes = new ArrayList<>();
        PathPoint tubePathEnd = pathFrom;
        Path tubePath = null;
        if (rat.getDistanceSq(endPos.getX(), endPos.getY(), endPos.getZ()) > 10 && !rat.isInCage() && rat.isTamed()) {
            for (BlockPos pos : BlockPos.getAllInBox(startPos.add(-10, -10, -10), startPos.add(10, 10, 10)).map(BlockPos::toImmutable).collect(Collectors.toList())) {
                if (RatUtils.isOpenRatTube(worldIn, rat, pos)) {
                    openTubes.add(pos);
                }
            }
            for (BlockPos pos : openTubes) {
                BlockPos tubeOffset = pos;
                boolean inside = true;
                if (rat.getDistanceSq(endPos.getX(), endPos.getY(), endPos.getZ()) > 0.6F) {
                    inside = false;
                    tubeOffset = RatUtils.offsetTubeEntrance(worldIn, pos);
                }
                AStar aStar = new AStar(pos, endPos, 1000, true);
                BlockPos[] pathBlocks = aStar.getPath(worldIn);
                if (pathBlocks.length > 1) {
                    Path path = null;
                    BlockPos finalAStarPath = pathBlocks[pathBlocks.length - 1];
                    if (finalAStarPath != null && finalAStarPath.distanceSq(endPos) < rat.getDistanceSq(endPos.getX(), endPos.getY(), endPos.getZ())) {
                        if (inside) {
                            path = findAlreadyTubePath(worldIn, pathFrom, pos, maxDistance);
                        } else {
                            path = findTubePath(worldIn, pathFrom, tubeOffset, pos, maxDistance);
                        }
                    }
                    if (path != null && path.getFinalPathPoint() != null) {
                        tubePath = path;
                        tubePathEnd = new PathPoint(pathBlocks[pathBlocks.length - 1].getX(), pathBlocks[pathBlocks.length - 1].getY(), pathBlocks[pathBlocks.length - 1].getZ());
                        break;
                    }
                }
            }
        }
        if (tubePath != null) {
            return tubePath;
        }
        //IWorldReader worldIn, PathPoint pathFrom, PathPoint pathTo, float maxDistance
        return null;//findVanillaPath(worldIn, rat, pathFrom, pathTo, maxDistance);
    }

    public Path findTubePath(IWorldReader worldIn, PathPoint pathFrom, BlockPos tubePos, BlockPos tubeActualPos, float maxDistance) {
        PathPoint pathTube = new PathPoint(tubePos.getX(), tubePos.getY(), tubePos.getZ());
        PathPoint pathInTube = new PathPoint(tubeActualPos.getX(), tubeActualPos.getY(), tubeActualPos.getZ());
        pathFrom.totalPathDistance = 0.0F;
        pathFrom.distanceToNext = pathFrom.distanceTo(pathTube);
        pathFrom.distanceToTarget = pathFrom.distanceToNext;
        List<PathPoint> points = new ArrayList<>();
        points.add(pathFrom);
        points.add(pathTube);
        points.add(pathInTube);
        Path path = new Path(points, tubePos, false);
        return path;
    }

    public Path findAlreadyTubePath(IWorldReader worldIn, PathPoint pathFrom, BlockPos tubeActualPos, float maxDistance) {
        PathPoint pathInTube = new PathPoint(tubeActualPos.getX(), tubeActualPos.getY(), tubeActualPos.getZ());
        pathFrom.totalPathDistance = 0.0F;
        pathFrom.distanceToNext = pathFrom.distanceTo(pathInTube);
        pathFrom.distanceToTarget = pathFrom.distanceToNext;
        List<PathPoint> points = new ArrayList<>();
        points.add(pathFrom);
        points.add(pathInTube);
        Path path = new Path(points, tubeActualPos, false);
        return path;
    }


    @Nullable
    public Path findVanillaPath(IWorldReader p_224775_1_, MobEntity p_224775_2_, Set<BlockPos> p_224775_3_, float p_224775_4_, int p_224775_5_) {
        this.path.clearPath();
        this.nodeProcessor.init(p_224775_1_, p_224775_2_);
        PathPoint pathpoint = this.nodeProcessor.getStart();
        Map<FlaggedPathPoint, BlockPos> map = p_224775_3_.stream().collect(Collectors.toMap((p_224782_1_) -> {
            return this.nodeProcessor.func_224768_a((double)p_224782_1_.getX(), (double)p_224782_1_.getY(), (double)p_224782_1_.getZ());
        }, Function.identity()));
        Path path = this.func_224779_a(pathpoint, map, p_224775_4_, p_224775_5_);
        this.nodeProcessor.postProcess();
        return path;
    }

    private Path createPath(PathPoint start, PathPoint end) {
        List<PathPoint> list = Lists.newArrayList();
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
            list.add(0, pathpoint1);
        }

        return new Path(list, new BlockPos(end.x, end.y, end.z), false);
    }

    private Path func_224780_a(PathPoint p_224780_1_, BlockPos p_224780_2_, boolean p_224780_3_) {
        List<PathPoint> list = Lists.newArrayList();
        PathPoint pathpoint = p_224780_1_;
        list.add(0, p_224780_1_);

        while(pathpoint.previous != null) {
            pathpoint = pathpoint.previous;
            list.add(0, pathpoint);
        }

        return new Path(list, p_224780_2_, p_224780_3_);
    }


    @Nullable
    private Path func_224779_a(PathPoint p_224779_1_, Map<FlaggedPathPoint, BlockPos> p_224779_2_, float p_224779_3_, int p_224779_4_) {
        Set<FlaggedPathPoint> set = p_224779_2_.keySet();
        p_224779_1_.totalPathDistance = 0.0F;
        p_224779_1_.distanceToNext = this.func_224776_a(p_224779_1_, set);
        p_224779_1_.distanceToTarget = p_224779_1_.distanceToNext;
        this.path.clearPath();
        this.closedSet.clear();
        this.path.addPoint(p_224779_1_);
        int i = 0;

        while (!this.path.isPathEmpty()) {
            ++i;
            if (i >= this.range) {
                break;
            }

            PathPoint pathpoint = this.path.dequeue();
            pathpoint.visited = true;
            set.stream().filter((p_224781_2_) -> {
                return pathpoint.func_224757_c(p_224781_2_) <= (float) p_224779_4_;
            }).forEach(FlaggedPathPoint::func_224764_e);
            if (set.stream().anyMatch(FlaggedPathPoint::func_224762_f)) {
                break;
            }

            if (!(pathpoint.distanceTo(p_224779_1_) >= p_224779_3_)) {
                int j = this.nodeProcessor.func_222859_a(this.pathOptions, pathpoint);

                for (int k = 0; k < j; ++k) {
                    PathPoint pathpoint1 = this.pathOptions[k];
                    float f = pathpoint.distanceTo(pathpoint1);
                    pathpoint1.field_222861_j = pathpoint.field_222861_j + f;
                    float f1 = pathpoint.totalPathDistance + f + pathpoint1.costMalus;
                    if (pathpoint1.field_222861_j < p_224779_3_ && (!pathpoint1.isAssigned() || f1 < pathpoint1.totalPathDistance)) {
                        pathpoint1.previous = pathpoint;
                        pathpoint1.totalPathDistance = f1;
                        pathpoint1.distanceToNext = this.func_224776_a(pathpoint1, set) * 1.5F;
                        if (pathpoint1.isAssigned()) {
                            this.path.changeDistance(pathpoint1, pathpoint1.totalPathDistance + pathpoint1.distanceToNext);
                        } else {
                            pathpoint1.distanceToTarget = pathpoint1.totalPathDistance + pathpoint1.distanceToNext;
                            this.path.addPoint(pathpoint1);
                        }
                    }
                }
            }
        }

        Stream<Path> stream;
        if (set.stream().anyMatch(FlaggedPathPoint::func_224762_f)) {
            stream = set.stream().filter(FlaggedPathPoint::func_224762_f).map((p_224778_2_) -> {
                return this.func_224780_a(p_224778_2_.func_224763_d(), p_224779_2_.get(p_224778_2_), true);
            }).sorted(Comparator.comparingInt(Path::getCurrentPathLength));
        } else {
            stream = set.stream().map((p_224777_2_) -> {
                return this.func_224780_a(p_224777_2_.func_224763_d(), p_224779_2_.get(p_224777_2_), false);
            }).sorted(Comparator.comparingDouble(Path::func_224769_l).thenComparingInt(Path::getCurrentPathLength));
        }

        Optional<Path> optional = stream.findFirst();
        if (!optional.isPresent()) {
            return null;
        } else {
            Path path = optional.get();
            return path;
        }
    }

    private float func_224776_a(PathPoint p_224776_1_, Set<FlaggedPathPoint> p_224776_2_) {
        float f = Float.MAX_VALUE;

        for(FlaggedPathPoint flaggedpathpoint : p_224776_2_) {
            float f1 = p_224776_1_.distanceTo(flaggedpathpoint);
            flaggedpathpoint.func_224761_a(f1, p_224776_1_);
            f = Math.min(f1, f);
        }

        return f;
    }
}
