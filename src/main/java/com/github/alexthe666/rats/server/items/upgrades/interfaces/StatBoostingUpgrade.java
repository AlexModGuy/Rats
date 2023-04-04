package com.github.alexthe666.rats.server.items.upgrades.interfaces;

import net.minecraft.world.entity.ai.attributes.Attribute;

import java.util.Map;

public interface StatBoostingUpgrade {

	/**
	 * Applies attribute boosts to a rat with this upgrade. <br>
	 * You can change any attribute, such as its health or armor with this. <br>
	 * Each map entry consists of the attribute to modify and the value to modify it with. The value you define is additive, meaning it will add (or subtract!) from the rat's default attribute value.
	 * @return a map of attribute changes to make
	 */
	Map<Attribute, Double> getAttributeBoosts();

	/**
	 * Allows the rat to regenerate 1 health every 1.5 seconds if true.
	 * @return if the rat should regenerate health with this upgrade
	 */
	default boolean regeneratesHealth() {
		return false;
	}
}
