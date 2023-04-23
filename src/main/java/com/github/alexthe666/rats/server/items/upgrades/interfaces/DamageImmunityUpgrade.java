package com.github.alexthe666.rats.server.items.upgrades.interfaces;

import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import net.minecraft.world.damagesource.DamageSource;

public interface DamageImmunityUpgrade {

	/**
	 * Defines if a rat is immune to certain damage sources. You can check for specific sources, damage types, or just outright ignore all damage here.
	 *
	 * @param rat    the rat currently being damaged
	 * @param source the Damage Source the rat is currently being hit by
	 * @return true if the rat should ignore the currently incoming damage, false if it should be handled as normal
	 */
	boolean isImmuneToDamageSource(TamedRat rat, DamageSource source);
}
