package com.github.alexthe666.rats.server.entity.ai.navigation.navigation;

import com.github.alexthe666.rats.server.entity.ai.navigation.evaluator.RatNodeEvaluator;
import com.github.alexthe666.rats.server.entity.rat.Rat;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathFinder;

public class RatNavigation extends GroundPathNavigation {
	public RatNavigation(Mob mob, Level level) {
		super(mob, level);
	}

	@Override
	protected PathFinder createPathFinder(int nodes) {
		this.nodeEvaluator = new RatNodeEvaluator();
		this.nodeEvaluator.setCanPassDoors(true);
		this.nodeEvaluator.setCanOpenDoors(true);
		this.nodeEvaluator.setCanFloat(true);
		return new PathFinder(this.nodeEvaluator, nodes);
	}
}
