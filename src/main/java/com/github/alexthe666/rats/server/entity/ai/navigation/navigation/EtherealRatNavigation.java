package com.github.alexthe666.rats.server.entity.ai.navigation.navigation;

import com.github.alexthe666.rats.server.entity.ai.navigation.evaluator.RatNodeEvaluator;
import com.github.alexthe666.rats.server.entity.rat.AbstractRat;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathFinder;

public class EtherealRatNavigation extends FlyingPathNavigation {

	public final AbstractRat rat;

	public EtherealRatNavigation(AbstractRat entity, Level level) {
		super(entity, level);
		this.rat = entity;
	}

	protected PathFinder createPathFinder(int nodes) {
		this.nodeEvaluator = new RatNodeEvaluator();
		this.nodeEvaluator.setCanPassDoors(true);
		this.nodeEvaluator.setCanFloat(true);
		return new PathFinder(this.nodeEvaluator, nodes);
	}

	@Override
	public boolean moveTo(Entity entity, double speed) {
		this.rat.getMoveControl().setWantedPosition(entity.getX(), entity.getY() + entity.getBbHeight(), entity.getZ(), speed);
		Path path = this.createPath(entity, 0);
		return path != null && this.moveTo(path, speed);
	}

	@Override
	public boolean moveTo(double x, double y, double z, double speed) {
		this.rat.getMoveControl().setWantedPosition(x, y, z, speed);
		return this.moveTo(this.createPath(BlockPos.containing(x, y, z), 0), speed);
	}
}
