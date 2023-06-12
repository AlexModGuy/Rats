package com.github.alexthe666.rats.server.inventory;

import com.github.alexthe666.rats.registry.RatsMenuRegistry;
import com.github.alexthe666.rats.server.inventory.container.RatUpgradeContainer;
import com.github.alexthe666.rats.server.inventory.slot.RatListUpgradeSlot;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class RatUpgradeMenu extends AbstractContainerMenu {

	public final Container inventory;

	public RatUpgradeMenu(int id, Container container, Inventory inventory, ItemStack stack) {
		super(RatsMenuRegistry.RAT_UPGRADE_CONTAINER.get(), id);
		int numRows = container.getContainerSize() / 9;
		this.inventory = container;
		container.startOpen(inventory.player);
		int i = (numRows - 4) * 18;
		for (int j = 0; j < numRows; ++j) {
			for (int k = 0; k < 9; ++k) {
				this.addSlot(new RatListUpgradeSlot(container, stack, k + j * 9, 8 + k * 18, 18 + j * 18));
			}
		}
		for (int l = 0; l < 3; ++l) {
			for (int j1 = 0; j1 < 9; ++j1) {
				this.addSlot(new Slot(inventory, j1 + l * 9 + 9, 8 + j1 * 18, 103 + l * 18 + i));
			}
		}
		for (int i1 = 0; i1 < 9; ++i1) {
			this.addSlot(new Slot(inventory, i1, 8 + i1 * 18, 161 + i));
		}
	}

	public RatUpgradeMenu(int id, Inventory inventory) {
		this(id, new SimpleContainer(27), inventory, ItemStack.EMPTY);
	}

	@Override
	public boolean stillValid(Player player) {
		return true;
	}

	@Override
	public void removed(Player player) {
		super.removed(player);
		if (this.inventory instanceof RatUpgradeContainer) {
			this.inventory.stopOpen(player);
		}
	}

	public ItemStack quickMoveStack(Player player, int index) {
		Slot slot = this.slots.get(index);
		ItemStack stack = slot.getItem();

		if (!stack.isEmpty()) {
			for (Slot cs : this.slots) {
				var destination = cs.getItem();
				if (cs instanceof RatListUpgradeSlot && ItemStack.isSameItem(destination, stack)) {
					break;
				} else if (destination.isEmpty()) {
					if (cs.mayPlace(stack)) {
						cs.set(stack.copyWithCount(1));
						this.broadcastChanges();
						break;
					}
				}
			}
		}
		return ItemStack.EMPTY;
	}

	@Override
	public void clicked(int slotIndex, int mouseButton, ClickType type, Player player) {
		if (type == ClickType.QUICK_MOVE && slotIndex > this.inventory.getContainerSize()) {
			return;
		}

		if (slotIndex >= 0 && slotIndex < this.inventory.getContainerSize() && this.slots.get(slotIndex) instanceof RatListUpgradeSlot slot) {
			ItemStack stack = this.getCarried().copyWithCount(1);

			if (stack.isEmpty()) {
				if (slotIndex < this.inventory.getContainerSize()) {
					this.inventory.removeItemNoUpdate(slotIndex);
					this.inventory.setChanged();
				}
			} else {
				if (slot.mayPlace(stack)) {
					for (Slot cs : this.slots) {
						var destination = cs.getItem();
						if (cs instanceof RatListUpgradeSlot && ItemStack.isSameItem(destination, stack)) {
							return;
						}
					}

					if (slotIndex < this.inventory.getContainerSize()) {
						this.inventory.setItem(slotIndex, stack);
						this.inventory.setChanged();
					}
				}
			}
		} else {
			super.clicked(slotIndex, mouseButton, type, player);
		}
	}
}
