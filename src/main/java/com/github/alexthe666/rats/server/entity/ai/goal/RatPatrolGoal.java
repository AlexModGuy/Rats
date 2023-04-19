package com.github.alexthe666.rats.server.entity.ai.goal;

import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

public class RatPatrolGoal extends Goal {

	private final TamedRat rat;
	private GlobalPos nextNode;
	private int nodeIndex = 0;

	public RatPatrolGoal(TamedRat rat) {
		this.rat = rat;
	}

	@Override
	public boolean canUse() {
		return this.rat.isPatrolCommand() && !this.rat.getPatrolNodes().isEmpty() && (this.rat.getTarget() == null || !this.rat.getTarget().isAlive());
	}

	public void tick() {
		if ((this.nextNode == null || this.nextNode.dimension().equals(this.rat.getLevel().dimension())) && this.nodeIndex >= 0 && this.nodeIndex < this.rat.getPatrolNodes().size()) {
			this.nextNode = this.rat.getPatrolNodes().get(this.nodeIndex);
		}

		if (this.nextNode != null && this.rat.distanceToSqr(Vec3.atCenterOf(this.nextNode.pos())) <= this.rat.getRatHarvestDistance(0.0D)) {
			++this.nodeIndex;
			this.nextNode = null;
			if (this.nodeIndex > this.rat.getPatrolNodes().size() - 1) {
				this.nodeIndex = 0;
			}

			try {
				this.nextNode = this.rat.getPatrolNodes().get(this.nodeIndex);
			} catch (Exception var2) {
				this.nodeIndex = 0;
			}
		} else if (this.nextNode != null) {
			this.rat.getNavigation().moveTo((double) ((float) this.nextNode.pos().getX()) + 0.5D, this.nextNode.pos().getY() + 1, (double) ((float) this.nextNode.pos().getZ()) + 0.5D, 1.0D);
		}

	}
}
