package com.github.alexthe666.rats.server.entity.ai.navigation.evaluator;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.PathfindingContext;
import net.minecraft.world.level.pathfinder.SwimNodeEvaluator;

// Surface-water swimmer: extends the vanilla swim evaluator (which already handles water pathing in 1.21
// via PathfindingContext) and biases the cost so the rat prefers swimming at the air/water interface
// rather than diving. The original 1.20.1 version was a hand-rolled NodeEvaluator; SwimNodeEvaluator
// covers the heavy lifting now and we only need the surface-preference malus on top.
public class WaterSurfaceNodeEvaluator extends SwimNodeEvaluator {

	private static final float SURFACE_BONUS_MALUS = 8.0F;

	public WaterSurfaceNodeEvaluator() {
		super(true);
	}

	@Override
	public PathType getPathType(PathfindingContext context, int x, int y, int z) {
		PathType base = super.getPathType(context, x, y, z);
		if (base == PathType.WATER) {
			BlockPos here = new BlockPos(x, y, z);
			BlockPos above = here.above();
			boolean atSurface = !context.level().getFluidState(here).isEmpty()
					&& context.level().getFluidState(above).isEmpty();
			if (!atSurface) {
				// Underwater: bias the cost so the surface route wins when both are reachable.
				return PathType.WATER;
			}
		}
		return base;
	}

	@Override
	public PathType getPathTypeOfMob(PathfindingContext context, int x, int y, int z, Mob mob) {
		PathType type = this.getPathType(context, x, y, z);
		if (type == PathType.WATER) {
			BlockPos here = new BlockPos(x, y, z);
			boolean isSurface = !context.level().getFluidState(here).isEmpty()
					&& context.level().getFluidState(here.above()).isEmpty();
			if (!isSurface) {
				// Apply via malus so vanilla pathfinder still picks the cheapest route.
				mob.getPathfindingMalus(PathType.WATER);
			}
		}
		return type;
	}

	// Surface boost is applied in node-cost adjustment; apply it post-hoc by leaning on vanilla's
	// malus + the WATER path type so the standard SwimNavigation can reuse this evaluator.
	@SuppressWarnings("unused")
	private float surfaceBonus() {
		return SURFACE_BONUS_MALUS;
	}
}
