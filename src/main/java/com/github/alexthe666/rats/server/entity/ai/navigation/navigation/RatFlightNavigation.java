package com.github.alexthe666.rats.server.entity.ai.navigation.navigation;

import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;

public class RatFlightNavigation extends GroundPathNavigation {

	public final TamedRat rat;

	public RatFlightNavigation(TamedRat rat, Level level) {
		super(rat, level);
		this.rat = rat;
	}

	public void tick() {
		this.tick++;
	}

	public boolean moveTo(double x, double y, double z, double speed) {
		this.rat.getMoveControl().setWantedPosition(x, y + 3.0F, z, speed);
		return true;
	}

	public boolean moveTo(Entity entity, double speed) {
		this.rat.getMoveControl().setWantedPosition(entity.getX(), entity.getY() + 3.0F, entity.getZ(), speed);
		return true;
	}
}
