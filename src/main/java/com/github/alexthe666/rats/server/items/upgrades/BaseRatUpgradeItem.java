package com.github.alexthe666.rats.server.items.upgrades;

import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.items.LoreTagItem;
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
		return true;
	}

	public boolean playIdleAnimation(TamedRat rat) {
		return true;
	}

	public boolean canFly(TamedRat rat) {
		return false;
	}

	/**
	 * True if the rat should deposit or cook an item instead of holding it.
	 */
	public boolean shouldDepositItem(TamedRat rat, ItemStack stack) {
		return true;
	}

}
