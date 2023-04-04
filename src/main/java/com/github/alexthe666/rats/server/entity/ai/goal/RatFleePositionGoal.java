package com.github.alexthe666.rats.server.entity.ai.goal;

import com.github.alexthe666.rats.server.entity.rat.AbstractRat;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class RatFleePositionGoal extends Goal {
	private final AbstractRat rat;
	private Path path;

	public RatFleePositionGoal(AbstractRat rat) {
		this.rat = rat;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		if (this.rat.hasFleePos()) {
			Vec3 vec3d = DefaultRandomPos.getPosAway(this.rat, 32, 7, new Vec3(this.rat.getFleePos().getX(), this.rat.getFleePos().getY(), this.rat.getFleePos().getZ()).add(0.5D, 0.5D, 0.5D));
			if (vec3d == null) {
				return false;
			} else if (this.rat.getFleePos().distToCenterSqr(vec3d.x, vec3d.y, vec3d.z) < this.rat.distanceToSqr(this.rat.getFleePos().getX(), this.rat.getFleePos().getY(), this.rat.getFleePos().getZ())) {
				return false;
			} else {
				this.path = this.rat.getNavigation().createPath(BlockPos.containing(vec3d), 0);
				return this.path != null;
			}
		} else {
			return false;
		}
	}

	public boolean canContinueToUse() {
		return !this.rat.getNavigation().isDone() && this.rat.hasFleePos();
	}

	public void start() {
		this.rat.getNavigation().moveTo(this.path, 1.225D);
	}

	public void stop() {
		this.path = null;
		this.rat.setFleePos(null);
	}
}
