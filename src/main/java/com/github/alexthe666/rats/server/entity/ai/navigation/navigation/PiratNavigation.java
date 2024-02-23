package com.github.alexthe666.rats.server.entity.ai.navigation.navigation;

import com.github.alexthe666.rats.server.entity.ai.navigation.evaluator.WaterSurfaceNodeEvaluator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.phys.Vec3;

public class PiratNavigation extends PathNavigation {

	public PiratNavigation(Mob mob, Level level) {
		super(mob, level);
	}

	@Override
	protected PathFinder createPathFinder(int maxNodes) {
		this.nodeEvaluator = new WaterSurfaceNodeEvaluator();
		return new PathFinder(this.nodeEvaluator, maxNodes);
	}

	@Override
	protected Vec3 getTempMobPos() {
		return new Vec3(this.mob.getX(), this.mob.getY(0.5D), this.mob.getZ());
	}

	@Override
	protected boolean canUpdatePath() {
		return this.isInLiquid();
	}

	@Override
	protected double getGroundY(Vec3 vec) {
		return vec.y();
	}

	@Override
	protected boolean canMoveDirectly(Vec3 startVec, Vec3 endVec) {
		return isClearForMovementBetween(this.mob, startVec, endVec, false);
	}

	public boolean isStableDestination(BlockPos pos) {
		return !this.level.getBlockState(pos).is(Blocks.WATER);
	}

	@Override
	public void setCanFloat(boolean floats) {

	}
}
