package com.github.alexthe666.rats.server.entity.ai.navigation.evaluator;

import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.*;

import javax.annotation.Nullable;
import java.util.Map;

public class WaterSurfaceNodeEvaluator extends NodeEvaluator {

	private final Long2ObjectMap<BlockPathTypes> pathTypesByPosCache = new Long2ObjectOpenHashMap<>();

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
	public Target getGoal(double x, double y, double z) {
		return this.getTargetFromNode(this.getNode(Mth.floor(x), Mth.floor(y), Mth.floor(z)));
	}

	@Override
	public int getNeighbors(Node[] nodeArray, Node currentNode) {
		int i = 0;
		Map<Direction, Node> map = Maps.newEnumMap(Direction.class);

		for(Direction direction : Direction.values()) {
			Node node = this.findAcceptedNode(currentNode.x + direction.getStepX(), currentNode.y + direction.getStepY(), currentNode.z + direction.getStepZ());
			map.put(direction, node);
			if (this.isNodeValid(node)) {
				nodeArray[i++] = node;
			}
		}

		for(Direction direction1 : Direction.Plane.HORIZONTAL) {
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
		BlockPathTypes blockpathtypes = this.getCachedBlockType(x, y, z);
		if (blockpathtypes == BlockPathTypes.WATER) {
			float f = this.mob.getPathfindingMalus(blockpathtypes);
			if (f >= 0.0F) {
				node = this.getNode(x, y, z);
				node.type = blockpathtypes;
				node.costMalus = Math.max(node.costMalus, f);
				if (!this.level.getFluidState(new BlockPos(x, y, z)).isEmpty() && this.level.getFluidState(new BlockPos(x, y + 1, z)).isEmpty()) {
					node.costMalus += 8.0F;
				}
			}
		}

		return node;
	}

	protected BlockPathTypes getCachedBlockType(int x, int y, int z) {
		return this.pathTypesByPosCache.computeIfAbsent(BlockPos.asLong(x, y, z), type -> this.getBlockPathType(this.level, x, y, z));
	}

	@Override
	public BlockPathTypes getBlockPathType(BlockGetter getter, int x, int y, int z) {
		return this.getBlockPathType(getter, x, y, z, this.mob);
	}

	@Override
	public BlockPathTypes getBlockPathType(BlockGetter getter, int x, int y, int z, Mob mob) {
		BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

		for(int i = x; i < x + this.entityWidth; ++i) {
			for(int j = y; j < y + this.entityHeight; ++j) {
				for(int k = z; k < z + this.entityDepth; ++k) {
					FluidState fluidstate = getter.getFluidState(mutablePos.set(i, j, k));
					BlockState blockstate = getter.getBlockState(mutablePos.set(i, j, k));
					if (fluidstate.isEmpty() && blockstate.isPathfindable(getter, mutablePos.below(), PathComputationType.WATER) && blockstate.isAir()) {
						return BlockPathTypes.WATER;
					}

					if (!fluidstate.is(Fluids.WATER)) {
						return BlockPathTypes.OPEN;
					}
				}
			}
		}

		BlockState currentState = getter.getBlockState(mutablePos);
		BlockState aboveState = getter.getBlockState(mutablePos.above());
		return currentState.isPathfindable(getter, mutablePos, PathComputationType.WATER) && aboveState.isAir() ? BlockPathTypes.WATER : BlockPathTypes.OPEN;
	}
}
