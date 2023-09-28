package com.github.alexthe666.rats.server.items.upgrades;

import com.github.alexthe666.rats.server.entity.ai.goal.harvest.RatBottlerGoal;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.ChangesAIUpgrade;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;

public class BottlerRatUpgradeItem extends BaseRatUpgradeItem implements ChangesAIUpgrade {
	public BottlerRatUpgradeItem(Properties properties) {
		super(properties, 1, 1);
	}

	@Override
	public boolean shouldDepositItem(TamedRat rat, ItemStack stack) {
		return !stack.is(Items.GLASS_BOTTLE);
	}

	@Override
	public boolean isRatHoldingFood(TamedRat rat) {
		return !rat.getMainHandItem().is(Items.HONEY_BOTTLE) && !rat.getMainHandItem().is(Items.POTION) && super.isRatHoldingFood(rat);
	}

	@Override
	public List<Goal> addNewWorkGoals(TamedRat rat) {
		return List.of(new RatBottlerGoal(rat));
	}
}
