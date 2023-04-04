package com.github.alexthe666.rats.server.items.upgrades;

import com.github.alexthe666.rats.server.entity.ai.goal.RatPlaceGoal;
import com.github.alexthe666.rats.server.entity.rat.RatCommand;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.ChangesAIUpgrade;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.DamageImmunityUpgrade;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class PlacerRatUpgradeItem extends BaseRatUpgradeItem implements ChangesAIUpgrade, DamageImmunityUpgrade {
	public PlacerRatUpgradeItem(Properties properties) {
		super(properties, 2, 2);
	}

	@Override
	public List<Goal> addNewWorkGoals(TamedRat rat) {
		return List.of(new RatPlaceGoal(rat));
	}

	@Override
	public boolean shouldDepositItem(TamedRat rat, ItemStack stack) {
		return rat.getCommand() != RatCommand.HARVEST;
	}

	@Override
	public boolean isRatHoldingFood(TamedRat rat) {
		return !(rat.getMainHandItem().getItem() instanceof BlockItem);
	}

	@Override
	public boolean isImmuneToDamageSource(TamedRat rat, DamageSource source) {
		return source.is(DamageTypeTags.IS_FALL) || source.is(DamageTypes.IN_WALL);
	}
}
