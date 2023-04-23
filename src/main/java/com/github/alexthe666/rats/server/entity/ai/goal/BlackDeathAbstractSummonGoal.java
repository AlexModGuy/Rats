package com.github.alexthe666.rats.server.entity.ai.goal;

import com.github.alexthe666.rats.server.entity.monster.boss.BlackDeath;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public abstract class BlackDeathAbstractSummonGoal extends Goal {

	protected final BlackDeath death;
	protected int nextAttackTickCount;

	public BlackDeathAbstractSummonGoal(BlackDeath death) {
		this.death = death;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));

	}

	@Override
	public boolean canUse() {
		if (this.hasSummonedEnough()) return false;
		LivingEntity livingentity = this.death.getTarget();
		if (livingentity != null && livingentity.isAlive()) {
			if (this.death.isSummoning()) {
				return false;
			} else {
				return this.death.tickCount >= this.nextAttackTickCount;
			}
		} else {
			return false;
		}
	}

	@Override
	public boolean canContinueToUse() {
		LivingEntity livingentity = this.death.getTarget();
		return livingentity != null && livingentity.isAlive();
	}

	@Override
	public void start() {
		this.death.setSummonTicks(20);
		this.nextAttackTickCount = this.death.tickCount + this.getAttackCooldown();
		this.death.setSummoning(true);
	}

	@Override
	public void tick() {
		this.summonEntity();
	}

	public abstract int getAttackCooldown();

	public abstract void summonEntity();

	public abstract boolean hasSummonedEnough();
}
