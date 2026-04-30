package com.github.alexthe666.rats.server.entity.ai.goal;

import com.github.alexthe666.rats.server.entity.RatMount;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

import java.util.EnumSet;

public class RatFollowOwnerGoal extends FollowOwnerGoal {
	private final TamedRat rat;
	private final double speedModifier;
	private int timeToRecalcPath;

	public RatFollowOwnerGoal(TamedRat rat, double speedModifier, float startDist, float stopDist) {
		super(rat, speedModifier, startDist, stopDist);
		this.rat = rat;
		this.speedModifier = speedModifier;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		// 1.21: FollowOwnerGoal.unableToMove() was inlined; we replicate the equivalent gating here.
		if (this.rat.isOrderedToSit() || this.rat.isLeashed() || this.rat.getVehicle() instanceof Player) {
			return false;
		}
		return this.rat.canMove() && this.rat.isFollowing() && super.canUse();
	}

	@Override
	public void start() {
		super.start();
		this.rat.getNavigation().stop();
		this.timeToRecalcPath = 0;
	}

	@Override
	public void tick() {
		// If the rat is riding a custom mount (RatMount) and the owner is far away, hop the whole
		// mount to the owner instead of letting vanilla teleport just the passenger off the saddle.
		if (--this.timeToRecalcPath <= 0) {
			this.timeToRecalcPath = this.adjustedTickDelay(10);
			LivingEntity owner = this.rat.getOwner();
			if (owner != null && this.rat.distanceToSqr(owner) > 144.0D && this.rat.getVehicle() instanceof RatMount mount && mount.shouldTeleportWhenFarAway()) {
				if (this.maybeTeleportMount(owner)) {
					return;
				}
			}
		}
		super.tick();
	}

	private boolean maybeTeleportMount(LivingEntity owner) {
		BlockPos anchor = owner.blockPosition();
		for (int i = 0; i < 10; i++) {
			int dx = this.randomIntInclusive(-3, 3);
			int dy = this.randomIntInclusive(-1, 1);
			int dz = this.randomIntInclusive(-3, 3);
			if (this.attemptTeleportEntity(this.rat.getVehicle(), anchor.getX() + dx, anchor.getY() + dy, anchor.getZ() + dz)) {
				return true;
			}
		}
		return false;
	}

	private boolean attemptTeleportEntity(Entity mount, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		if (!this.canTeleportTo(pos)) return false;
		mount.moveTo(x + 0.5D, y, z + 0.5D, mount.getYRot(), mount.getXRot());
		this.rat.getNavigation().stop();
		return true;
	}

	private boolean canTeleportTo(BlockPos pos) {
		PathType type = WalkNodeEvaluator.getPathTypeStatic(this.rat, pos);
		if (type != PathType.WALKABLE) return false;
		BlockPos below = pos.below();
		net.minecraft.world.level.block.state.BlockState belowState = this.rat.level().getBlockState(below);
		if (!belowState.isFaceSturdy(this.rat.level(), below, net.minecraft.core.Direction.UP)) return false;
		if (belowState.is(BlockTags.LEAVES)) return false;
		return this.rat.level().noCollision(this.rat, this.rat.getBoundingBox().move(pos.subtract(this.rat.blockPosition())));
	}

	private int randomIntInclusive(int min, int max) {
		return this.rat.getRandom().nextInt(max - min + 1) + min;
	}
}
