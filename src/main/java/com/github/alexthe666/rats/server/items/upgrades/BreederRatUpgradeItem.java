package com.github.alexthe666.rats.server.items.upgrades;

import com.github.alexthe666.rats.server.entity.ai.goal.RatHarvestBreederGoal;
import com.github.alexthe666.rats.server.entity.rat.RatCommand;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.ChangesAIUpgrade;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class BreederRatUpgradeItem extends BaseRatUpgradeItem implements ChangesAIUpgrade {
	public BreederRatUpgradeItem(Properties properties) {
		super(properties, 1, 1);
	}

	@Override
	public boolean shouldDepositItem(TamedRat rat, ItemStack stack) {
		return rat.getCommand() != RatCommand.HARVEST;
	}

	@Override
	public boolean shouldCollectItem(TamedRat rat, ItemStack stack) {
		return false;
	}

	@Override
	public List<Goal> addNewWorkGoals(TamedRat rat) {
		return List.of(new RatHarvestBreederGoal(rat));
	}
}
