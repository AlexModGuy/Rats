package com.github.alexthe666.rats.server.inventory;

import com.github.alexthe666.rats.registry.RatsMenuRegistry;
import com.github.alexthe666.rats.server.inventory.slot.JuryRiggedUpgradeSlot;
import com.github.alexthe666.rats.server.items.upgrades.BaseRatUpgradeItem;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class JuryRiggedRatUpgradeMenu extends AbstractContainerMenu {

	public final Container inventory;

	public JuryRiggedRatUpgradeMenu(int id, Container itemInventory, Inventory inventory, ItemStack stack) {
		super(RatsMenuRegistry.RAT_UPGRADE_JR_CONTAINER.get(), id);
		this.inventory = itemInventory;
		itemInventory.startOpen(inventory.player);
		int i = 18;
		this.addSlot(new JuryRiggedUpgradeSlot(itemInventory, stack, 0, 44, 18));
		this.addSlot(new JuryRiggedUpgradeSlot(itemInventory, stack, 1, 116, 18));

		for (int l = 0; l < 3; ++l) {
			for (int j1 = 0; j1 < 9; ++j1) {
				this.addSlot(new Slot(inventory, j1 + l * 9 + 9, 8 + j1 * 18, 31 + l * 18 + i));
			}
		}
		for (int i1 = 0; i1 < 9; ++i1) {
			this.addSlot(new Slot(inventory, i1, 8 + i1 * 18, 89 + i));
		}
	}

	public JuryRiggedRatUpgradeMenu(int id, Inventory inventory) {
		this(id, new SimpleContainer(2), inventory, ItemStack.EMPTY);
	}


	@Override
	public boolean stillValid(Player player) {
		return true;
	}

	@Override
	public void removed(Player player) {
		super.removed(player);
		this.inventory.stopOpen(player);
	}

	public ItemStack quickMoveStack(Player player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
			if (index != 1 && index != 0) {
				if (itemstack1.getItem() instanceof BaseRatUpgradeItem) {
					if (!this.moveItemStackTo(itemstack1, 0, 2, false)) {
						return ItemStack.EMPTY;
					}
				} else if (index >= 2 && index < 30) {
					if (!this.moveItemStackTo(itemstack1, 30, 38, false)) {
						return ItemStack.EMPTY;
					}
				} else if (index >= 30 && index < 38 && !this.moveItemStackTo(itemstack1, 2, 30, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(itemstack1, 2, 38, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(player, itemstack1);
		}

		return itemstack;
	}
}
