package com.github.alexthe666.rats.server.entity.ai.goal;

import com.github.alexthe666.rats.server.entity.RatMount;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;

public class RatFollowOwnerGoal extends FollowOwnerGoal {
	private final TamedRat rat;
	private final double speedModifier;
	private int timeToRecalcPath;

	public RatFollowOwnerGoal(TamedRat rat, double speedModifier, float startDist, float stopDist) {
		// 1.21: FollowOwnerGoal(TamableAnimal, double, float, float).
		super(rat, speedModifier, startDist, stopDist);
		this.rat = rat;
		this.speedModifier = speedModifier;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		return this.rat.canMove() && this.rat.isFollowing() && super.canUse();
	}

	// PORT-STUB: 1.21 FollowOwnerGoal.unableToMove() removed; sit/leash/ride checks must be done in canUse().
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
		// PORT-STUB: 1.21 owner field removed; teleportToOwner is now private. Delegate movement loop to the parent class.
		super.tick();
	}

	private boolean maybeTeleportMount() {
		if (this.rat.getVehicle() instanceof RatMount mount && mount.shouldTeleportWhenFarAway()) {
			BlockPos blockpos = this.owner.blockPosition();

			for (int i = 0; i < 10; ++i) {
				int j = this.randomIntInclusive(-3, 3);
				int k = this.randomIntInclusive(-1, 1);
				int l = this.randomIntInclusive(-3, 3);
				boolean flag = this.attemptTeleportEntity(this.rat.getVehicle(), blockpos.getX() + j, blockpos.getY() + k, blockpos.getZ() + l);
				if (flag) {
					return true;
				}
			}
		}
		return false;
	}

	@SuppressWarnings("unused")
	private boolean attemptTeleportEntity(Entity mount, int x, int y, int z) {
		// PORT-STUB: 1.21 FollowOwnerGoal.canTeleportTo / owner are private; mount-teleport detour is disabled.
		return false;
	}

	private int randomIntInclusive(int min, int max) {
		return this.rat.getRandom().nextInt(max - min + 1) + min;
	}
}
