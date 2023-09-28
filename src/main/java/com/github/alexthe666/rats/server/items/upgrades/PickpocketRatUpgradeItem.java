package com.github.alexthe666.rats.server.items.upgrades;

import com.github.alexthe666.rats.server.entity.ai.goal.harvest.RatPickpocketGoal;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.ChangesAIUpgrade;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.TickRatUpgrade;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.List;

public class PickpocketRatUpgradeItem extends BaseRatUpgradeItem implements ChangesAIUpgrade, TickRatUpgrade {
	public PickpocketRatUpgradeItem(Properties properties) {
		super(properties, 2, 2);
	}

	@Override
	public boolean isRatHoldingFood(TamedRat rat) {
		return false;
	}

	@Override
	public List<Goal> addNewWorkGoals(TamedRat rat) {
		return List.of(new RatPickpocketGoal(rat));
	}

	@Override
	public void tick(TamedRat rat) {
		if (rat.pickpocketCooldown > 0) {
			rat.pickpocketCooldown--;
		}
	}
}
