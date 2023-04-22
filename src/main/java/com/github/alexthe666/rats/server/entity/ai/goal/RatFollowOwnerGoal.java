package com.github.alexthe666.rats.server.entity.ai.goal;

import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;

public class RatFollowOwnerGoal extends FollowOwnerGoal {
	private final TamedRat rat;
	private final double speedModifier;
	private int timeToRecalcPath;

	public RatFollowOwnerGoal(TamedRat rat, double speedModifier, float startDist, float stopDist) {
		super(rat, speedModifier, startDist, stopDist, false);
		this.rat = rat;
		this.speedModifier = speedModifier;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		return this.rat.canMove() && this.rat.isFollowing() && super.canUse();
	}

	@Override
	public boolean unableToMove() {
		return this.rat.isOrderedToSit() || this.rat.getVehicle() instanceof Player || this.rat.isLeashed();
	}

	@Override
	public void start() {
		super.start();
		this.rat.getNavigation().stop();
		this.timeToRecalcPath = 0;
	}

	@Override
	public void tick() {
		this.rat.getLookControl().setLookAt(this.owner, 10.0F, (float) this.rat.getMaxHeadXRot());
		if (this.rat.isFollowing()) {
			if (--this.timeToRecalcPath <= 0) {
				this.timeToRecalcPath = this.adjustedTickDelay(10);
				if (this.rat.distanceToSqr(this.owner) >= (this.rat.hasFlightUpgrade() ? 800.0D : 400.0D)) {
					this.teleportToOwner();
				} else {
					if (this.rat.hasFlightUpgrade()) {
						this.rat.getNavigation().moveTo(this.owner.getX(), this.owner.getY() + 2.5D, this.owner.getZ(), this.speedModifier);
						this.rat.setFlying(true);
					} else {
						this.rat.getNavigation().moveTo(this.owner, this.speedModifier);
					}
				}
			}
		}
	}
}