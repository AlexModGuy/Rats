package com.github.alexthe666.rats.server.inventory.slot;

import com.github.alexthe666.rats.server.items.RatListUpgradeItem;
import com.github.alexthe666.rats.server.items.upgrades.BaseRatUpgradeItem;
import com.github.alexthe666.rats.server.items.upgrades.CombinedRatUpgradeItem;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class JuryRiggedUpgradeSlot extends Slot {
	public final ItemStack upgrade;

	public JuryRiggedUpgradeSlot(Container inventory, ItemStack upgrade, int id, int x, int y) {
		super(inventory, id, x, y);
		this.upgrade = upgrade;
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		if (this.upgrade.isEmpty()) {
			return super.mayPlace(stack);
		}
		return this.upgrade.getItem() instanceof RatListUpgradeItem || stack.getItem() instanceof BaseRatUpgradeItem && !stack.is(this.upgrade.getItem()) && CombinedRatUpgradeItem.canCombineWithUpgrade(this.upgrade, stack);
	}
}
