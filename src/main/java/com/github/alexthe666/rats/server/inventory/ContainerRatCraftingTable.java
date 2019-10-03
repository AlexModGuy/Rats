package com.github.alexthe666.rats.server.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceOutput;

public class ContainerRatCraftingTable extends SyncedFieldContainer {

    private final IInventory tileRatCraftingTable;
    private int cookTime;

    public ContainerRatCraftingTable(IInventory inv, EntityPlayer player) {
        super(inv);
        this.tileRatCraftingTable = inv;
        this.addSlotToContainer(new Slot(tileRatCraftingTable, 0, 35, 20));
        this.addSlotToContainer(new SlotFurnaceOutput(player, tileRatCraftingTable, 1, 124, 20));
        for (int k = 2; k < 11; ++k) {
            this.addSlotToContainer(new Slot(tileRatCraftingTable, k, k * 18 - 28, 100));
        }
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 129 + i * 18));
            }
        }
        for (int k = 0; k < 9; ++k) {
            this.addSlotToContainer(new Slot(player.inventory, k, 8 + k * 18, 187));
        }
    }

    public boolean canInteractWith(EntityPlayer playerIn) {
        return this.tileRatCraftingTable.isUsableByPlayer(playerIn);
    }

    /*public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
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
}
