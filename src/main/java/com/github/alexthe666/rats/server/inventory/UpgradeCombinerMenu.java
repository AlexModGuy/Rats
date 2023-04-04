package com.github.alexthe666.rats.server.inventory;

import com.github.alexthe666.rats.registry.RatsMenuRegistry;
import com.github.alexthe666.rats.server.block.entity.UpgradeCombinerBlockEntity;
import com.github.alexthe666.rats.server.items.upgrades.BaseRatUpgradeItem;
import com.github.alexthe666.rats.server.items.upgrades.CombinedRatUpgradeItem;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;

public class UpgradeCombinerMenu extends AbstractContainerMenu {

	public final Container container;
	public final ContainerData data;

	public UpgradeCombinerMenu(int id, Inventory playerInventory) {
		this(id, new SimpleContainer(4), playerInventory, new SimpleContainerData(4));
	}

	public UpgradeCombinerMenu(int id, Container tileInventory, Inventory playerInventory, ContainerData data) {
		super(RatsMenuRegistry.UPGRADE_COMBINER_CONTAINER.get(), id);
		this.container = tileInventory;
		this.data = data;
		this.addSlot(new Slot(this.container, 0, 20, 35));
		this.addSlot(new Slot(this.container, 1, 44, 57));
		this.addSlot(new Slot(this.container, 2, 69, 35));
		this.addSlot(new FurnaceResultSlot(playerInventory.player, this.container, 3, 129, 35));
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}
		for (int k = 0; k < 9; ++k) {
			this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
		}
		this.addDataSlots(data);
	}

	@Override
	public boolean stillValid(Player player) {
		return this.container.stillValid(player);
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
			if (index == 2) {
				if (!this.moveItemStackTo(itemstack1, 3, 38, true)) {
					return ItemStack.EMPTY;
				}

				slot.onQuickCraft(itemstack1, itemstack);
			} else if (index != 1 && index != 0) {
				if (itemstack1.getItem() instanceof CombinedRatUpgradeItem) {
					if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
						return ItemStack.EMPTY;
					}
				} else if (UpgradeCombinerBlockEntity.getItemBurnTime(itemstack1) > 0) {
					if (!this.moveItemStackTo(itemstack1, 1, 2, false)) {
						return ItemStack.EMPTY;
					}
				} else if (itemstack1.getItem() instanceof BaseRatUpgradeItem) {
					if (!this.moveItemStackTo(itemstack1, 2, 3, false)) {
						return ItemStack.EMPTY;
					}
				} else if (index >= 3 && index < 30) {
					if (!this.moveItemStackTo(itemstack1, 30, 38, false)) {
						return ItemStack.EMPTY;
					}
				} else if (index >= 30 && index < 38 && !this.moveItemStackTo(itemstack1, 3, 30, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(itemstack1, 3, 38, false)) {
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

	public int getCookProgressScaled() {
		int i = this.data.get(0);
		int j = this.data.get(1);
		return j != 0 && i != 0 ? i * 24 / j : 0;
	}

	public int getBurnLeftScaled() {
		int i = this.data.get(3);
		if (i == 0) {
			i = 200;
		}

		return this.data.get(2) * 13 / i;
	}
}
