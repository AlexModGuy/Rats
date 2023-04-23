package com.github.alexthe666.rats.server.items.upgrades;

import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.TickRatUpgrade;
import com.github.alexthe666.rats.server.misc.RatUtils;

public class TickAccelRatUpgradeItem extends BaseRatUpgradeItem implements TickRatUpgrade {
	public TickAccelRatUpgradeItem(Properties properties) {
		super(properties, 1, 2);
	}

	@Override
	public void tick(TamedRat rat) {
		RatUtils.accelerateTick(rat.getLevel(), rat.blockPosition().above());
		RatUtils.accelerateTick(rat.getLevel(), rat.blockPosition());
		RatUtils.accelerateTick(rat.getLevel(), rat.blockPosition().below());
	}
}
