package com.github.alexthe666.rats.server.items.upgrades;

import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.items.LoreTagItem;
import com.github.alexthe666.rats.server.misc.RatUtils;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

public class BaseRatUpgradeItem extends LoreTagItem {
	private int rarity = 0;

	public BaseRatUpgradeItem(Item.Properties properties) {
		super(properties, 1);
	}

	public BaseRatUpgradeItem(Item.Properties properties, int rarity, int textLength) {
		super(properties, textLength);
		this.rarity = rarity;
	}

	public Rarity getRarity(ItemStack stack) {
		if (rarity != 0 && rarity != 4) {
			return Rarity.values()[rarity];
		}
		return super.getRarity(stack);
	}

	public boolean isFoil(ItemStack stack) {
		return this.rarity >= 3 || super.isFoil(stack);
	}

	public boolean isRatHoldingFood(TamedRat rat) {
		return RatUtils.isRatFood(rat.getMainHandItem());
	}

	public boolean playIdleAnimation(TamedRat rat) {
		return true;
	}

	public boolean canFly(TamedRat rat) {
		return false;
	}

	/**
	 * True if the rat should deposit an item into containers instead of holding onto it.
	 */
	public boolean shouldDepositItem(TamedRat rat, ItemStack stack) {
		return true;
	}

	/**
	 * True if the rat should pick up the specified item off the ground.
	 */
	public boolean shouldCollectItem(TamedRat rat, ItemStack stack) {
		return true;
	}

}
