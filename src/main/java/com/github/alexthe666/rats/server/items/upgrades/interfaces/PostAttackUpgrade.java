package com.github.alexthe666.rats.server.items.upgrades.interfaces;

import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import net.minecraft.world.entity.LivingEntity;

public interface PostAttackUpgrade {
	/**
	 * Allows a rat with this upgrade to do some special handling after it attacks. <br>
	 * You can set the target on fire, give them a potion effect, or change anything about them with this. <br>
	 * You can also change something about the attacking rat if you want to. <br>
	 * @param rat the rat currently attacking its target
	 * @param target the victim of the rat's attack
	 */
	void afterHit(TamedRat rat, LivingEntity target);
}
