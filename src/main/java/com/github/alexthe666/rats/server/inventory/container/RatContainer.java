package com.github.alexthe666.rats.server.inventory.container;

import com.github.alexthe666.rats.server.entity.rat.InventoryRat;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

public class RatContainer extends SimpleContainer {

	private final InventoryRat rat;

	public RatContainer(InventoryRat rat, int size) {
		super(size);
		this.rat = rat;
	}

	public InventoryRat getRat() {
		return this.rat;
	}

	@Override
	public void setItem(int index, ItemStack stack) {
		super.setItem(index, stack);
		if (index >= 3 && this.rat instanceof TamedRat tamed && tamed.isAlive() && !tamed.getLevel().isClientSide()) {
			tamed.onUpgradeChanged();
		}
	}
}
