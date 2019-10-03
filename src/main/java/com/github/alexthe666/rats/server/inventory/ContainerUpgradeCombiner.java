package com.github.alexthe666.rats.server.inventory;

import com.github.alexthe666.rats.server.entity.tile.TileEntityUpgradeCombiner;
import com.github.alexthe666.rats.server.items.ItemRatCombinedUpgrade;
import com.github.alexthe666.rats.server.items.ItemRatUpgrade;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceOutput;
import net.minecraft.item.ItemStack;

public class ContainerUpgradeCombiner extends SyncedFieldContainer {

    private final IInventory tileRatCraftingTable;
    private int cookTime;

    public ContainerUpgradeCombiner(IInventory inv, EntityPlayer player) {
        super(inv);
        this.tileRatCraftingTable = inv;
        this.addSlotToContainer(new Slot(tileRatCraftingTable, 0, 20, 35));
        this.addSlotToContainer(new Slot(tileRatCraftingTable, 1, 44, 57));
        this.addSlotToContainer(new Slot(tileRatCraftingTable, 2, 69, 35));
        this.addSlotToContainer(new SlotFurnaceOutput(player, tileRatCraftingTable, 3, 129, 35));

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int k = 0; k < 9; ++k) {
            this.addSlotToContainer(new Slot(player.inventory, k, 8 + k * 18, 142));
        }
    }

    public boolean canInteractWith(EntityPlayer playerIn) {
        return this.tileRatCraftingTable.isUsableByPlayer(playerIn);
    }

    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index == 2) {
                if (!this.mergeItemStack(itemstack1, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(itemstack1, itemstack);
            } else if (index != 1 && index != 0) {
                if (itemstack1.getItem() instanceof ItemRatCombinedUpgrade) {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (itemstack1.getItem() instanceof ItemRatUpgrade) {
                    if (!this.mergeItemStack(itemstack1, 2, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (TileEntityUpgradeCombiner.getItemBurnTime(itemstack1) > 0) {
                    if (!this.mergeItemStack(itemstack1, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 3 && index < 30) {
                    if (!this.mergeItemStack(itemstack1, 30, 39, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 30 && index < 39 && !this.mergeItemStack(itemstack1, 4, 30, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 4, 39, false)) {
                return ItemStack.EMPTY;
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
    }
}
