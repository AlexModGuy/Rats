package com.github.alexthe666.rats.server.entity.ai.navigation.evaluator;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.PathfindingContext;
import net.minecraft.world.level.pathfinder.Target;

// PORT-STUB: 1.21 collapsed BlockGetter/Mob path-type lookups into PathfindingContext.
// NodeEvaluator now exposes only `getPathType(PathfindingContext, int, int, int)`; the old
// `level`/`mob` fields and `getBlockPathType(...)` overloads are gone. The water-surface logic that
// boosted cost when crossing a water-air boundary needs reimplementation against PathfindingContext.
// For now we provide a non-functional stub so the surface-water rat upgrade does not break compile;
// rats will fall back to default WaterBoundPathNavigation behaviour.
public class WaterSurfaceNodeEvaluator extends NodeEvaluator {

	@Override
	public void prepare(PathNavigationRegion region, Mob mob) {
		super.prepare(region, mob);
	}

	@Override
	public void done() {
		super.done();
	}

	@Override
	public Node getStart() {
		return new Node(0, 0, 0);
	}

	@Override
	public Target getTarget(double x, double y, double z) {
		return new Target(new Node((int) x, (int) y, (int) z));
	}

	@Override
	public int getNeighbors(Node[] nodeArray, Node currentNode) {
		return 0;
	}

	@Override
	public PathType getPathType(PathfindingContext context, int x, int y, int z) {
		return PathType.WATER;
	}

	@Override
	public PathType getPathTypeOfMob(PathfindingContext context, int x, int y, int z, Mob mob) {
		return PathType.WATER;
	}
}
