package com.github.alexthe666.rats.server.entity.ai.goal;

import com.github.alexthe666.rats.server.entity.monster.Pirat;
import com.github.alexthe666.rats.server.misc.RatUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class PiratWanderGoal extends Goal {
	private final Pirat rat;
	private double xPosition;
	private double yPosition;
	private double zPosition;
	private final double speed;
	private final int executionChance;
	private boolean mustUpdate;

	public PiratWanderGoal(Pirat pirat, double speedModifer) {
		this(pirat, speedModifer, 20);
	}

	public PiratWanderGoal(Pirat pirat, double speedModifer, int chance) {
		this.rat = pirat;
		this.speed = speedModifer;
		this.executionChance = chance;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		if (!this.mustUpdate) {
			if (this.rat.getRandom().nextInt(executionChance) != 0) {
				return false;
			}
		}
		Vec3 vec3d = null; // RatUtils.generateRandomWaterPos(this.rat, 10, 5, null, true);
		if (vec3d == null) {
			return false;
		} else {
			BlockPos water = RatUtils.findLowestWater(BlockPos.containing(vec3d), this.rat);
			this.xPosition = water.getX();
			this.yPosition = water.getY();
			this.zPosition = water.getZ();
			this.mustUpdate = false;
			return true;
		}
	}

	@Override
	public boolean canContinueToUse() {
		return !this.rat.getNavigation().isDone();
	}

	@Override
	public void start() {
		this.rat.getNavigation().moveTo(this.xPosition, this.yPosition, this.zPosition, this.speed);
	}
}
