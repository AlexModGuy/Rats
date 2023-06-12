package com.github.alexthe666.rats.server.entity.ai.goal;

import com.github.alexthe666.rats.server.entity.rat.Rat;
import com.github.alexthe666.rats.server.entity.misc.PlagueDoctor;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

import java.util.EnumSet;

public class WildRatDefendPlagueDoctorGoal extends TargetGoal {
	private final Rat rat;
	private LivingEntity ownerLastHurtBy;
	private int timestamp;

	public WildRatDefendPlagueDoctorGoal(Rat rat) {
		super(rat, false);
		this.rat = rat;
		this.setFlags(EnumSet.of(Goal.Flag.TARGET));
	}

	public boolean canUse() {
		if (this.rat.hasPlague() || !this.rat.isLeashed() || this.rat.level().getCurrentDifficultyAt(this.rat.blockPosition()).getDifficulty() == Difficulty.PEACEFUL) {
			return false;
		} else {
			Entity entity = this.rat.getLeashHolder();
			if (!(entity instanceof PlagueDoctor doctor)) {
				return false;
			} else {
				this.ownerLastHurtBy = doctor.getLastHurtByMob();
				int i = doctor.getLastHurtByMobTimestamp();
				return i != this.timestamp && this.canAttack(this.ownerLastHurtBy, TargetingConditions.DEFAULT);
			}
		}
	}

	public void start() {
		this.mob.setTarget(this.ownerLastHurtBy);
		Entity entity = this.rat.getLeashHolder();
		if (entity instanceof PlagueDoctor doctor) {
			this.timestamp = doctor.getLastHurtByMobTimestamp();
		}

		super.start();
	}
}
