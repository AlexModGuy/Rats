package com.github.alexthe666.rats.server.entity.ai.goal;

import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.misc.RatUpgradeUtils;
import net.minecraft.world.entity.ai.goal.FloatGoal;

public class RatFloatGoal extends FloatGoal {

	private final TamedRat rat;

	public RatFloatGoal(TamedRat rat) {
		super(rat);
		this.rat = rat;
	}

	public boolean canUse() {
		return super.canUse() && !RatUpgradeUtils.hasUpgrade(this.rat, RatsItemRegistry.RAT_UPGRADE_AQUATIC.get());
	}

}
