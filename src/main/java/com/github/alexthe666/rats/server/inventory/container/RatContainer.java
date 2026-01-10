package com.github.alexthe666.rats.server.inventory.container;

import com.github.alexthe666.rats.server.entity.rat.InventoryRat;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.EquipmentListenerUpgrade;
import com.github.alexthe666.rats.server.misc.RatUpgradeUtils;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EquipmentSlot;
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
		if (index < 3 && this.rat instanceof TamedRat tamed) {
			ItemStack oldStack = this.getItem(index);
			if (!ItemStack.isSameItem(oldStack, stack)) {
				RatUpgradeUtils.forEachUpgrade(tamed, item -> item instanceof EquipmentListenerUpgrade, (upgradeStack, upgradeSlot) ->
					((EquipmentListenerUpgrade) upgradeStack.getItem()).onItemChanged(tamed, this.getIndexedSlot(index), oldStack, stack));
			}
		}
		super.setItem(index, stack);
		if (index >= 3 && this.rat instanceof TamedRat tamed && tamed.isAlive() && !tamed.level().isClientSide()) {
			tamed.onUpgradeChanged();
		}
	}

	private EquipmentSlot getIndexedSlot(int index) {
		return switch (index) {
			case 1 -> EquipmentSlot.HEAD;
			case 2 -> EquipmentSlot.OFFHAND;
			default -> EquipmentSlot.MAINHAND;
		};
	}
}







