package com.github.alexthe666.rats.server.inventory.slot;

import com.github.alexthe666.rats.server.items.RatListUpgradeItem;
import com.github.alexthe666.rats.server.items.upgrades.BaseRatUpgradeItem;
import com.github.alexthe666.rats.server.items.upgrades.CombinedRatUpgradeItem;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.CombinedUpgrade;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public class RatListUpgradeSlot extends GhostItemSlot {
	public final ItemStack upgrade;

	public RatListUpgradeSlot(Container inventory, ItemStack upgrade, int id, int x, int y) {
		super(inventory, id, x, y);
		this.upgrade = upgrade;
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		if (this.upgrade.isEmpty()) {
			return super.mayPlace(stack);
		}
		return this.upgrade.getItem() instanceof RatListUpgradeItem || (stack.getItem() instanceof CombinedUpgrade && !stack.is(this.upgrade.getItem()) && CombinedRatUpgradeItem.canCombineWithUpgrade(this.upgrade, stack));
	}
}
