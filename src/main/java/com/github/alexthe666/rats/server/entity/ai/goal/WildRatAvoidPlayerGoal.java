package com.github.alexthe666.rats.server.entity.ai.goal;

import com.github.alexthe666.rats.server.entity.rat.Rat;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;

import java.util.function.Predicate;

public class WildRatAvoidPlayerGoal extends AvoidEntityGoal<Player> {

	private final Rat rat;

	public WildRatAvoidPlayerGoal(Rat rat, Predicate<LivingEntity> predicate) {
		super(rat, Player.class, 10.0F, 1.225D, 1.5D, predicate);
		this.rat = rat;
		this.avoidEntityTargeting = TargetingConditions.forNonCombat().range(10.0F).selector(predicate);
	}

	@Override
	public boolean canUse() {
		return !this.rat.hasPlague() && this.rat.getTarget() == null && this.rat.getNavigation().isDone() && super.canUse();
	}

	@Override
	public void tick() {
		double speed = 0.75D + (Math.max(100 - this.rat.wildTrust, 0) * 0.01D);
		this.mob.getNavigation().setSpeedModifier(speed);
	}
}
