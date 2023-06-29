package com.github.alexthe666.rats.server.items.upgrades;

import com.github.alexthe666.rats.server.entity.ai.goal.RatGardenerGoal;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.ChangesAIUpgrade;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.List;

public class RatGardenerUpgradeItem extends BaseRatUpgradeItem implements ChangesAIUpgrade {
	public RatGardenerUpgradeItem(Properties properties) {
		super(properties, 1, 1);
	}

	@Override
	public List<Goal> addNewWorkGoals(TamedRat rat) {
		return List.of(new RatGardenerGoal(rat));
	}
}
