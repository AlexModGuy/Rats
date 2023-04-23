package com.github.alexthe666.rats.server.entity.ai.goal;

import com.github.alexthe666.rats.server.entity.monster.boss.BlackDeath;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class BlackDeathSummoningGoal extends Goal {

	private final BlackDeath death;

	public BlackDeathSummoningGoal(BlackDeath death) {
		this.death = death;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		return this.death.getSummonTicks() > 0;
	}

	public void start() {
		super.start();
		this.death.getNavigation().stop();
	}

	public void stop() {
		super.stop();
		this.death.setSummoning(false);
	}

	public void tick() {
		if (this.death.getTarget() != null) {
			this.death.getLookControl().setLookAt(this.death.getTarget(), (float) this.death.getMaxHeadYRot(), (float) this.death.getMaxHeadXRot());
		}

	}
}
