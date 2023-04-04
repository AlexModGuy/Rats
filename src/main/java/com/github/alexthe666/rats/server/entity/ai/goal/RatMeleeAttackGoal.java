package com.github.alexthe666.rats.server.entity.ai.goal;

import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class RatMeleeAttackGoal extends MeleeAttackGoal implements RatWorkGoal {
	private final TamedRat rat;

	public RatMeleeAttackGoal(TamedRat rat, double speed, boolean memory) {
		super(rat, speed, memory);
		this.rat = rat;
	}

	public boolean canUse() {
		return !this.rat.isInCage() && super.canUse();
	}

	protected double getAttackReachSqr(LivingEntity attackTarget) {
		if (this.rat.isRidingSpecialMount()) {
			Entity entity = this.rat.getVehicle();
			if (entity != null) {
				return entity.getBbWidth() * 2.0F * entity.getBbWidth() * 2.0F + attackTarget.getBbWidth();
			}
		}
		return 1.5D;
	}

	@Override
	public TaskType getRatTaskType() {
		return TaskType.ATTACK;
	}
}