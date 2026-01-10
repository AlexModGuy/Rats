package com.github.alexthe666.rats.server.entity.ai.goal;

import com.github.alexthe666.rats.server.entity.RatMount;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

import java.util.EnumSet;

/**
 * Custom follow owner goal for rats.
 * In 1.21.1, FollowOwnerGoal has private fields/methods so we implement Goal directly.
 */
public class RatFollowOwnerGoal extends Goal {
	private final TamedRat rat;
	private final double speedModifier;
	private final float startDistSq;
	private final float stopDistSq;
	private LivingEntity owner;
	private int timeToRecalcPath;
	private float oldWaterCost;

	public RatFollowOwnerGoal(TamedRat rat, double speedModifier, float startDist, float stopDist) {
		this.rat = rat;
		this.speedModifier = speedModifier;
		this.startDistSq = startDist * startDist;
		this.stopDistSq = stopDist * stopDist;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		LivingEntity livingEntity = this.rat.getOwner();
		if (livingEntity == null) {
			return false;
		} else if (livingEntity.isSpectator()) {
			return false;
		} else if (this.unableToMove()) {
			return false;
		} else if (this.rat.distanceToSqr(livingEntity) < this.startDistSq) {
			return false;
		} else if (!this.rat.canMove() || !this.rat.isFollowing()) {
			return false;
		} else {
			this.owner = livingEntity;
			return true;
		}
	}

	@Override
	public boolean canContinueToUse() {
		if (this.rat.getNavigation().isDone()) {
			return false;
		} else if (this.unableToMove()) {
			return false;
		} else {
			return this.rat.distanceToSqr(this.owner) > this.stopDistSq;
		}
	}

	public boolean unableToMove() {
		return this.rat.isOrderedToSit() || this.rat.getVehicle() instanceof Player || this.rat.isLeashed();
	}

	@Override
	public void start() {
		this.rat.getNavigation().stop();
		this.timeToRecalcPath = 0;
		this.oldWaterCost = this.rat.getPathfindingMalus(PathType.WATER);
		this.rat.setPathfindingMalus(PathType.WATER, 0.0F);
	}

	@Override
	public void stop() {
		this.owner = null;
		this.rat.getNavigation().stop();
		this.rat.setPathfindingMalus(PathType.WATER, this.oldWaterCost);
	}

	@Override
	public void tick() {
		if (this.owner == null) return;
		
		this.rat.getLookControl().setLookAt(this.owner, 10.0F, (float) this.rat.getMaxHeadXRot());
		if (this.rat.isFollowing()) {
			if (--this.timeToRecalcPath <= 0) {
				this.timeToRecalcPath = this.adjustedTickDelay(10);
				if (this.rat.distanceToSqr(this.owner) >= (this.rat.hasFlightUpgrade() ? 800.0D : 400.0D)) {
					if (!this.maybeTeleportMount()) {
						this.teleportToOwner();
					}
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

	private void teleportToOwner() {
		BlockPos blockPos = this.owner.blockPosition();

		for (int i = 0; i < 10; ++i) {
			int j = this.randomIntInclusive(-3, 3);
			int k = this.randomIntInclusive(-1, 1);
			int l = this.randomIntInclusive(-3, 3);
			boolean flag = this.maybeTeleportTo(blockPos.getX() + j, blockPos.getY() + k, blockPos.getZ() + l);
			if (flag) {
				return;
			}
		}
	}

	private boolean maybeTeleportTo(int x, int y, int z) {
		if (Math.abs((double) x - this.owner.getX()) < 2.0D && Math.abs((double) z - this.owner.getZ()) < 2.0D) {
			return false;
		} else if (!this.canTeleportTo(new BlockPos(x, y, z))) {
			return false;
		} else {
			this.rat.moveTo((double) x + 0.5D, y, (double) z + 0.5D, this.rat.getYRot(), this.rat.getXRot());
			this.rat.getNavigation().stop();
			return true;
		}
	}

	private boolean canTeleportTo(BlockPos pos) {
		PathType pathType = WalkNodeEvaluator.getPathTypeStatic(this.rat, pos);
		if (pathType != PathType.WALKABLE) {
			return false;
		} else {
			BlockState blockState = this.rat.level().getBlockState(pos.below());
			if (blockState.getBlock() instanceof LeavesBlock) {
				return false;
			} else {
				BlockPos blockPos = pos.subtract(this.rat.blockPosition());
				return this.rat.level().noCollision(this.rat, this.rat.getBoundingBox().move(blockPos));
			}
		}
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

	private boolean attemptTeleportEntity(Entity mount, int x, int y, int z) {
		if (Math.abs((double) x - this.owner.getX()) < 2.0D && Math.abs((double) z - this.owner.getZ()) < 2.0D) {
			return false;
		} else if (!this.canTeleportTo(new BlockPos(x, y, z))) {
			return false;
		} else {
			mount.moveTo((double) x + 0.5D, y, (double) z + 0.5D, this.rat.getYRot(), this.rat.getXRot());
			if (mount instanceof Mob mob) mob.getNavigation().stop();
			this.rat.getNavigation().stop();
			return true;
		}
	}

	private int randomIntInclusive(int min, int max) {
		return this.rat.getRandom().nextInt(max - min + 1) + min;
	}
}







