package com.github.alexthe666.rats.server.entity.ai.goal;

import com.github.alexthe666.rats.RatConfig;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;

public abstract class RatMoveToBlockGoal extends MoveToBlockGoal {

	public RatMoveToBlockGoal(PathfinderMob creature, double speedModifier, int searchRadius) {
		super(creature, speedModifier, searchRadius);
	}

	@Override
	public double acceptedDistance() {
		return 2.0D;
	}

	@Override
	protected int nextStartTick(PathfinderMob mob) {
		return reducedTickDelay(RatConfig.ratUpdateDelay + this.mob.getRandom().nextInt(RatConfig.ratUpdateDelay));
	}
}