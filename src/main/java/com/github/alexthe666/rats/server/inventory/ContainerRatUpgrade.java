package com.github.alexthe666.rats.server.inventory;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class ContainerRatUpgrade extends Container {

    public IInventory inventory;
    private IInventory inventoryPlayer;

    public ContainerRatUpgrade(int id, IInventory itemInventory, PlayerInventory playerInventory) {
        super(RatsContainerRegistry.RAT_UPGRADE_CONTAINER, id);
        int numRows = itemInventory.getSizeInventory() / 9;
        this.inventory = itemInventory;
        this.inventoryPlayer = playerInventory;
        itemInventory.openInventory(playerInventory.player);
        int i = (numRows - 4) * 18;
        for (int j = 0; j < numRows; ++j) {
            for (int k = 0; k < 9; ++k) {
                this.addSlot(new SlotRatListUpgrade(inventory, RatsMod.PROXY.getRefrencedItem(), k + j * 9, 8 + k * 18, 18 + j * 18));
            }
        }
        for (int l = 0; l < 3; ++l) {
            for (int j1 = 0; j1 < 9; ++j1) {
                this.addSlot(new Slot(playerInventory, j1 + l * 9 + 9, 8 + j1 * 18, 103 + l * 18 + i));
            }
        }
        for (int i1 = 0; i1 < 9; ++i1) {
            this.addSlot(new Slot(playerInventory, i1, 8 + i1 * 18, 161 + i));
        }
    }

    public ContainerRatUpgrade(int i, PlayerInventory playerInventory) {
        this(i, new Inventory(27), playerInventory);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    @Override
    public void onContainerClosed(PlayerEntity playerIn) {
        super.onContainerClosed(playerIn);
        if(inventory instanceof InventoryRatUpgrade){
            this.inventory.closeInventory(playerIn);
        }
    }

    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index < 36) {
                if (!this.mergeItemStack(itemstack1, 36, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, 36, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }
}
