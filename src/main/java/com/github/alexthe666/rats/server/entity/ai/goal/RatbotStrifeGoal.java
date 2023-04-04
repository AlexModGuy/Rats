package com.github.alexthe666.rats.server.entity.ai.goal;

import com.github.alexthe666.rats.server.entity.ratlantis.RatlanteanRatbot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class RatbotStrifeGoal extends Goal {

	private final RatlanteanRatbot entity;
	private final double moveSpeedAmp;
	private final float maxAttackDistance;
	private int seeTime;
	private boolean strafingClockwise;
	private boolean strafingBackwards;
	private int strafingTime = -1;

	public RatbotStrifeGoal(RatlanteanRatbot mob, double moveSpeedAmpIn, float maxAttackDistanceIn) {
		this.entity = mob;
		this.moveSpeedAmp = moveSpeedAmpIn;
		this.maxAttackDistance = maxAttackDistanceIn * maxAttackDistanceIn;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	public boolean canUse() {
		return this.entity.getTarget() != null;
	}


	public boolean canContinueToUse() {
		return (this.canUse() || !this.entity.getNavigation().isDone());
	}

	public void stop() {
		super.stop();
		this.seeTime = 0;
		this.entity.stopUsingItem();
	}

	/**
	 * Keep ticking a continuous task that has already been started
	 */
	public void tick() {
		LivingEntity living = this.entity.getTarget();

		if (living != null) {
			double d0 = this.entity.distanceToSqr(living.getX(), living.getBoundingBox().minY, living.getZ());
			boolean flag = this.entity.getSensing().hasLineOfSight(living);
			boolean flag1 = this.seeTime > 0;

			if (flag != flag1) {
				this.seeTime = 0;
			}

			if (flag) {
				++this.seeTime;
			} else {
				--this.seeTime;
			}

			if (d0 <= (double) this.maxAttackDistance && this.seeTime >= 20) {
				this.entity.getNavigation().stop();
				++this.strafingTime;
			} else {
				this.entity.getNavigation().moveTo(living, this.moveSpeedAmp);
				this.strafingTime = -1;
			}

			if (this.strafingTime >= 20) {
				if ((double) this.entity.getRandom().nextFloat() < 0.3D) {
					this.strafingClockwise = !this.strafingClockwise;
				}

				if ((double) this.entity.getRandom().nextFloat() < 0.3D) {
					this.strafingBackwards = !this.strafingBackwards;
				}

				this.strafingTime = 0;
			}

			if (this.strafingTime > -1) {
				if (d0 > (double) (this.maxAttackDistance * 0.75F)) {
					this.strafingBackwards = false;
				} else if (d0 < (double) (this.maxAttackDistance * 0.25F)) {
					this.strafingBackwards = true;
				}

				this.entity.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
				this.entity.lookAt(living, 30.0F, 30.0F);
			} else {
				this.entity.getLookControl().setLookAt(living, 30.0F, 30.0F);
			}

			if (!flag && this.seeTime < -60) {
				this.entity.stopUsingItem();
			} else if (flag) {
				this.entity.stopUsingItem();
			}
		}
	}
}
