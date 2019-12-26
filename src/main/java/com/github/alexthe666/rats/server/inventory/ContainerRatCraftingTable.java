package com.github.alexthe666.rats.server.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.FurnaceResultSlot;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;

public class ContainerRatCraftingTable extends Container {

    public final IInventory tileRatCraftingTable;
    public IIntArray vars;

    public ContainerRatCraftingTable(int id, IInventory tileInventory, PlayerInventory playerInventory, IIntArray vars) {
        super(RatsContainerRegistry.RAT_CRAFTING_TABLE_CONTAINER, id);
        this.tileRatCraftingTable = tileInventory;
        this.vars = vars;
        this.addSlot(new Slot(tileRatCraftingTable, 0, 35, 20));
        this.addSlot(new FurnaceResultSlot(playerInventory.player, tileRatCraftingTable, 1, 124, 20));
        for (int k = 2; k < 11; ++k) {
            this.addSlot(new Slot(tileRatCraftingTable, k, k * 18 - 28, 100));
        }
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 129 + i * 18));
            }
        }
        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 187));
        }
    }

    public ContainerRatCraftingTable(int i, PlayerInventory playerInventory) {
        this(i, new Inventory(11), playerInventory, new IntArray(4));
    }

    public boolean canInteractWith(PlayerEntity playerIn) {
        return this.tileRatCraftingTable.isUsableByPlayer(playerIn);
    }

    /*public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index == 0) {
                if (!this.mergeItemStack(itemstack1, 11, 39, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(itemstack1, itemstack);
            } else if (index == 1) {
                if (!this.mergeItemStack(itemstack1, 11, 39, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(itemstack1, itemstack);
            } else if (index < 11) {
                if (!this.mergeItemStack(itemstack1, 11, 39, false)) {
                    return ItemStack.EMPTY;
                }
            }else {
                if (!this.mergeItemStack(itemstack1, 2, 11, false)) {
                    return ItemStack.EMPTY;
                }
            }
            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }*/


    public int getCookProgressScaled(int pixels) {
        int i = vars.get(0);
        int j = 200;
        return j != 0 && i != 0 ? i * pixels / j : 0;
    }
}
