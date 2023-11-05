package com.github.alexthe666.rats.server.items.upgrades;

import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.items.OreRatNuggetItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.Tags;

public class OreDoublingRatUpgradeItem extends BaseRatUpgradeItem {

	public OreDoublingRatUpgradeItem(Item.Properties properties) {
		super(properties, 2, 4);
	}

	@Override
	public boolean shouldDepositItem(TamedRat rat, ItemStack stack) {
		return !isProcessable(rat.level(), stack);
	}

	@Override
	public boolean isRatHoldingFood(TamedRat rat) {
		return isProcessable(rat.level(), rat.getMainHandItem());
	}

	public static boolean isProcessable(Level level, ItemStack stack) {
		return stack.is(Tags.Items.ORES) && !OreRatNuggetItem.getIngot(level, stack).isEmpty();
	}
}
