package com.github.alexthe666.rats.server.items.upgrades;

import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.Tags;

public class OreDoublingRatUpgradeItem extends BaseRatUpgradeItem {

	public OreDoublingRatUpgradeItem(Item.Properties properties) {
		super(properties, 2, 4);
	}

	@Override
	public boolean shouldDepositItem(TamedRat rat, ItemStack stack) {
		return !isProcessable(stack);
	}

	@Override
	public boolean isRatHoldingFood(TamedRat rat) {
		return isProcessable(rat.getMainHandItem());
	}

	public static boolean isProcessable(ItemStack stack) {
		return stack.is(Tags.Items.ORES);
	}
}
