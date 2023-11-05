package com.github.alexthe666.rats.server.entity;

import net.minecraft.world.item.Item;

public interface RatMount {

	Item getUpgradeItem();

	default boolean shouldTeleportWhenFarAway() {
		return true;
	}
}
