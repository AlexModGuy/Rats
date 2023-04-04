package com.github.alexthe666.rats.server.entity.ai.navigation.navigation;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.level.Level;

public class PiratNavigation extends WaterBoundPathNavigation {

	public PiratNavigation(Mob mob, Level level) {
		super(mob, level);
	}

	@Override
	protected boolean isInLiquid() {
		return this.mob.isInWaterRainOrBubble() || this.mob.isInLava() || this.mob.isPassenger();
	}
}