package com.github.alexthe666.rats.server.inventory;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.tile.TileEntityUpgradeCombiner;
import com.github.alexthe666.rats.server.items.ItemRatUpgradeCombined;
import com.github.alexthe666.rats.server.items.ItemRatUpgrade;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.FurnaceResultSlot;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;

public class ContainerUpgradeCombiner extends Container {

    public final IInventory tileRatCraftingTable;
    private int cookTime;
    private IIntArray vars;
    public ContainerUpgradeCombiner(int id, IInventory tileInventory, PlayerInventory playerInventory, IIntArray vars) {
        super(RatsContainerRegistry.UPGRADE_COMBINER_CONTAINER, id);
        this.tileRatCraftingTable = tileInventory;
        this.vars = vars;
        this.addSlot(new Slot(tileRatCraftingTable, 0, 20, 35));
        this.addSlot(new Slot(tileRatCraftingTable, 1, 44, 57));
        this.addSlot(new Slot(tileRatCraftingTable, 2, 69, 35));
        this.addSlot(new FurnaceResultSlot(playerInventory.player, tileRatCraftingTable, 3, 129, 35));
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }

    public ContainerUpgradeCombiner(int i, PlayerInventory playerInventory) {
        this(i, new Inventory(4), playerInventory, new IntArray(4));
    }

    public boolean canInteractWith(PlayerEntity playerIn) {
        return this.tileRatCraftingTable.isUsableByPlayer(playerIn);
    }

    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index == 2) {
                if (!this.mergeItemStack(itemstack1, 3, 38, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else if (index != 1 && index != 0) {
                if (itemstack1.getItem() instanceof ItemRatUpgradeCombined) {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (TileEntityUpgradeCombiner.getItemBurnTime(itemstack1) > 0) {
                    if (!this.mergeItemStack(itemstack1, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (itemstack1.getItem() instanceof ItemRatUpgrade) {
                    if (!this.mergeItemStack(itemstack1, 2, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 3 && index < 30) {
                    if (!this.mergeItemStack(itemstack1, 30, 38, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 30 && index < 38 && !this.mergeItemStack(itemstack1, 3, 30, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 3, 38, false)) {
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

    public int getCookProgressScaled(int pixels) {
        int i = ((TileEntityUpgradeCombiner) RatsMod.PROXY.getRefrencedTE()).cookTime;
        int j = ((TileEntityUpgradeCombiner) RatsMod.PROXY.getRefrencedTE()).totalCookTime;
        return j != 0 && i != 0 ? i * pixels / j : 0;
    }

    public int getBurnLeftScaled(int pixels) {
        int i = ((TileEntityUpgradeCombiner) RatsMod.PROXY.getRefrencedTE()).currentItemBurnTime;
        if (i == 0) {
            i = 200;
        }
        return ((TileEntityUpgradeCombiner) RatsMod.PROXY.getRefrencedTE()).furnaceBurnTime * pixels / i;
    }
}
