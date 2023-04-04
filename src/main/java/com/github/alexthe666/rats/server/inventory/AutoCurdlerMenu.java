package com.github.alexthe666.rats.server.inventory;

import com.github.alexthe666.rats.registry.RatsMenuRegistry;
import com.github.alexthe666.rats.server.block.entity.AutoCurdlerBlockEntity;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;

public class AutoCurdlerMenu extends AbstractContainerMenu {

	public final Container container;
	public final ContainerData data;

	public AutoCurdlerMenu(int id, Container inv, Inventory playerInventory, ContainerData data) {
		super(RatsMenuRegistry.AUTO_CURDLER_CONTAINER.get(), id);
		this.container = inv;
		this.data = data;
		this.addSlot(new Slot(this.container, 0, 8, 35) {
			@Override
			public int getMaxStackSize(ItemStack stack) {
				return 1;
			}
		});
		this.addSlot(new FurnaceResultSlot(playerInventory.player, this.container, 1, 129, 35));

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

	public AutoCurdlerMenu(int i, Inventory playerInventory) {
		this(i, new SimpleContainer(2), playerInventory, new SimpleContainerData(4));
	}

	@Override
	public boolean stillValid(Player player) {
		return this.container.stillValid(player);
	}

	public int getCookProgressionScaled() {
		int i = this.data.get(0);
		int j = this.data.get(1);
		return j != 0 && i != 0 ? i * 50 / j : 0;
	}

	public int getFluidAmount() {
		return this.data.get(2);
	}

	public int getTankCapacity() {
		return this.data.get(3);
	}

	public ItemStack quickMoveStack(Player player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
			if (index == 2) {
				if (!this.moveItemStackTo(itemstack1, 3, 38, true)) {
					return ItemStack.EMPTY;
				}

				slot.onQuickCraft(itemstack1, itemstack);
			} else if (index != 1 && index != 0) {
				if (AutoCurdlerBlockEntity.isMilk(itemstack1)) {
					if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
						return ItemStack.EMPTY;
					}
				} else if (index >= 1 && index < 30) {
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
}
