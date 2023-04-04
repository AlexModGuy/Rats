package com.github.alexthe666.rats.server.entity.ai.goal;

import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.server.entity.PiedPiper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class PiperStrifeGoal extends Goal {

	private final PiedPiper piper;
	private final double speedModifier;
	private final float maxAttackDistance;
	private int seeTime;
	private boolean strafingClockwise;
	private boolean strafingBackwards;
	private int strafingTime = -1;

	public PiperStrifeGoal(PiedPiper mob, double speedModifier, float attackDistance) {
		this.piper = mob;
		this.speedModifier = speedModifier;
		this.maxAttackDistance = attackDistance * attackDistance;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		return this.piper.getTarget() != null && this.piper.isHolding(RatsItemRegistry.RAT_FLUTE.get());
	}

	@Override
	public boolean canContinueToUse() {
		return (this.canUse() || !this.piper.getNavigation().isDone()) && this.piper.isHolding(RatsItemRegistry.RAT_FLUTE.get());
	}

	@Override
	public void stop() {
		super.stop();
		this.seeTime = 0;
		this.piper.stopUsingItem();
	}

	@Override
	public void tick() {
		LivingEntity living = this.piper.getTarget();

		if (living != null) {
			double d0 = this.piper.distanceToSqr(living.getX(), living.getBoundingBox().minY, living.getZ());
			boolean flag = this.piper.getSensing().hasLineOfSight(living);
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
				this.piper.getNavigation().stop();
				++this.strafingTime;
			} else {
				this.piper.getNavigation().moveTo(living, this.speedModifier);
				this.strafingTime = -1;
			}

			if (this.strafingTime >= 20) {
				if ((double) this.piper.getRandom().nextFloat() < 0.3D) {
					this.strafingClockwise = !this.strafingClockwise;
				}

				if ((double) this.piper.getRandom().nextFloat() < 0.3D) {
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

				this.piper.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
				this.piper.lookAt(living, 30.0F, 30.0F);
			} else {
				this.piper.getLookControl().setLookAt(living, 30.0F, 30.0F);
			}

			if (!flag && this.seeTime < -60) {
				this.piper.stopUsingItem();
			} else if (flag) {
				this.piper.stopUsingItem();
				this.piper.summonRat();
			}
		}
	}
}
