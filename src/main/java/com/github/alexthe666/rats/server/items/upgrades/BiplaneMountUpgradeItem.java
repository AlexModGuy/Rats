package com.github.alexthe666.rats.server.items.upgrades;

import com.github.alexthe666.rats.registry.RatlantisEntityRegistry;
import com.github.alexthe666.rats.server.entity.mount.RatBiplaneMount;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;

public class BiplaneMountUpgradeItem extends MountRatUpgradeItem<RatBiplaneMount> {
	public BiplaneMountUpgradeItem(Properties properties) {
		super(properties, 2, 2, RatlantisEntityRegistry.RAT_MOUNT_BIPLANE);
	}

	@Override
	public boolean canFly(TamedRat rat) {
		return true;
	}
}







