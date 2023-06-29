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
		if (rat.tickCount % 5 == 0) {
			RatUtils.accelerateTick(rat.level(), rat.blockPosition().above(), rat.level().getRandom().nextInt(40), rat.level().getRandom().nextInt(10));
			RatUtils.accelerateTick(rat.level(), rat.blockPosition(), rat.level().getRandom().nextInt(40), rat.level().getRandom().nextInt(10));
			RatUtils.accelerateTick(rat.level(), rat.blockPosition().below(), rat.level().getRandom().nextInt(40), rat.level().getRandom().nextInt(10));
		}
	}
}
