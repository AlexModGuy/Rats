package com.github.alexthe666.rats.server.entity.ai.goal;

import com.github.alexthe666.rats.server.entity.rat.Rat;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.player.Player;

import java.util.function.Predicate;

public class WildRatAvoidPlayerGoal extends AvoidEntityGoal<Player> {

	private final Rat rat;

	public WildRatAvoidPlayerGoal(Rat rat, Predicate<LivingEntity> predicate) {
		super(rat, Player.class, 10.0F, 0.8D, 1.225D, predicate);
		this.rat = rat;
	}

	@Override
	public boolean canUse() {
		return !this.rat.hasPlague() && super.canUse();
	}

	@Override
	public void tick() {
		this.mob.getNavigation().setSpeedModifier(0.6D + ((double) (50 - Math.min(this.rat.wildTrust, 50)) * 0.04D));
	}
}
