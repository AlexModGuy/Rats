package com.github.alexthe666.rats.server.entity.ai.goal;

import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

import java.util.EnumSet;

public class RatOwnerHurtByTargetGoal extends TargetGoal {
	private final TamedRat tameable;
	private LivingEntity attacker;
	private int timestamp;

	public RatOwnerHurtByTargetGoal(TamedRat rat) {
		super(rat, false);
		this.tameable = rat;
		this.setFlags(EnumSet.of(Flag.TARGET));
	}

	public boolean canUse() {
		if (!tameable.isAttackCommand()) {
			return false;
		}
		if (this.tameable.isTame() && !this.tameable.isInSittingPose()) {
			LivingEntity owner = this.tameable.getOwner();
			if (owner == null) {
				return false;
			} else {
				this.attacker = owner.getLastHurtByMob();
				int time = owner.getLastHurtByMobTimestamp();
				return time != this.timestamp && this.canAttack(this.attacker, TargetingConditions.DEFAULT) && this.tameable.wantsToAttack(this.attacker, owner);
			}
		} else {
			return false;
		}
	}

	public void start() {
		this.mob.setTarget(this.attacker);
		LivingEntity owner = this.tameable.getOwner();
		if (owner != null) {
			this.timestamp = owner.getLastHurtByMobTimestamp();
		}

		super.start();
	}
}
