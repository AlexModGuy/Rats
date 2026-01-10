package com.github.alexthe666.rats.server.entity.ai.navigation.evaluator;

import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.*;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

import java.util.Map;
import javax.annotation.Nullable;

public class WaterSurfaceNodeEvaluator extends NodeEvaluator {

	private final Long2ObjectMap<PathType> pathTypesByPosCache = new Long2ObjectOpenHashMap<>();

	@Override
	public void prepare(PathNavigationRegion region, Mob mob) {
		super.prepare(region, mob);
		this.pathTypesByPosCache.clear();
	}

	@Override
	public void done() {
		super.done();
		this.pathTypesByPosCache.clear();
	}

	@Override
	public Node getStart() {
		return this.getNode(Mth.floor(this.mob.getBoundingBox().minX), Mth.floor(this.mob.getBoundingBox().maxY), Mth.floor(this.mob.getBoundingBox().minZ));
	}

	@Override
	public Target getTarget(double x, double y, double z) {
		return this.getTargetNodeAt(x, y, z);
	}

	@Override
	public int getNeighbors(Node[] nodeArray, Node currentNode) {
		int i = 0;
		Map<Direction, Node> map = Maps.newEnumMap(Direction.class);

		for (Direction direction : Direction.values()) {
			Node node = this.findAcceptedNode(currentNode.x + direction.getStepX(), currentNode.y + direction.getStepY(), currentNode.z + direction.getStepZ());
			map.put(direction, node);
			if (this.isNodeValid(node)) {
				nodeArray[i++] = node;
			}
		}

		for (Direction direction1 : Direction.Plane.HORIZONTAL) {
			Direction direction2 = direction1.getClockWise();
			Node node1 = this.findAcceptedNode(currentNode.x + direction1.getStepX() + direction2.getStepX(), currentNode.y, currentNode.z + direction1.getStepZ() + direction2.getStepZ());
			if (this.isDiagonalNodeValid(node1, map.get(direction1), map.get(direction2))) {
				nodeArray[i++] = node1;
			}
		}

		return i;
	}

	protected boolean isNodeValid(@Nullable Node node) {
		return node != null && !node.closed;
	}

	protected boolean isDiagonalNodeValid(@Nullable Node node, @Nullable Node p_192965_, @Nullable Node p_192966_) {
		return this.isNodeValid(node) && p_192965_ != null && p_192965_.costMalus >= 0.0F && p_192966_ != null && p_192966_.costMalus >= 0.0F;
	}

	@Nullable
	protected Node findAcceptedNode(int x, int y, int z) {
		Node node = null;
		PathType blockpathtypes = this.getCachedBlockType(x, y, z);
		if (blockpathtypes == PathType.WATER) {
			float f = this.mob.getPathfindingMalus(blockpathtypes);
			if (f >= 0.0F) {
				node = this.getNode(x, y, z);
				node.type = blockpathtypes;
				node.costMalus = Math.max(node.costMalus, f);
				if (!this.currentContext.level().getFluidState(new BlockPos(x, y, z)).isEmpty() && this.currentContext.level().getFluidState(new BlockPos(x, y + 1, z)).isEmpty()) {
					node.costMalus += 8.0F;
				}
			}
		}

		return node;
	}

	protected PathType getCachedBlockType(int x, int y, int z) {
		return this.pathTypesByPosCache.computeIfAbsent(BlockPos.asLong(x, y, z), type -> this.getPathType(this.currentContext, x, y, z));
	}

	@Override
	public PathType getPathType(PathfindingContext context, int x, int y, int z) {
		return this.getPathTypeOfMob(context, x, y, z, this.mob);
	}

	@Override
	public PathType getPathTypeOfMob(PathfindingContext context, int x, int y, int z, Mob mob) {
		BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

		for (int i = x; i < x + this.entityWidth; ++i) {
			for (int j = y; j < y + this.entityHeight; ++j) {
				for (int k = z; k < z + this.entityDepth; ++k) {
					FluidState fluidstate = context.level().getFluidState(mutablePos.set(i, j, k));
					BlockState blockstate = context.getBlockState(mutablePos.set(i, j, k));
					if (fluidstate.isEmpty() && blockstate.isPathfindable(PathComputationType.WATER) && blockstate.isAir()) {
						return PathType.WATER;
					}

					if (!fluidstate.is(Fluids.WATER)) {
						return PathType.OPEN;
					}
				}
			}
		}

		BlockState currentState = context.getBlockState(mutablePos);
		BlockState aboveState = context.getBlockState(mutablePos.above());
		return currentState.isPathfindable(PathComputationType.WATER) && aboveState.isAir() ? PathType.WATER : PathType.OPEN;
	}
}







