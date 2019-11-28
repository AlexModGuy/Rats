package com.github.alexthe666.rats.server.inventory;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerRatUpgrade extends SyncedFieldContainer {

    private ItemStack stack;
    private IInventory inventory;
    private IInventory inventoryPlayer;

    public ContainerRatUpgrade(PlayerEntity player, InventoryPlayer playerInventory, InventoryRatUpgrade itemInventory) {
        super(itemInventory);
        int numRows = itemInventory.getSizeInventory() / 9;
        this.inventory = itemInventory;
        this.inventoryPlayer = playerInventory;
        itemInventory.openInventory(player);
        this.stack = itemInventory.upgradeStack;
        int i = (numRows - 4) * 18;
        for (int j = 0; j < numRows; ++j) {
            for (int k = 0; k < 9; ++k) {
                this.addSlotToContainer(new SlotRatListUpgrade(inventory, itemInventory.upgradeStack, k + j * 9, 8 + k * 18, 18 + j * 18));
            }
        }
        for (int l = 0; l < 3; ++l) {
            for (int j1 = 0; j1 < 9; ++j1) {
                this.addSlotToContainer(new Slot(player.inventory, j1 + l * 9 + 9, 8 + j1 * 18, 103 + l * 18 + i));
            }
        }
        for (int i1 = 0; i1 < 9; ++i1) {
            this.addSlotToContainer(new Slot(player.inventory, i1, 8 + i1 * 18, 161 + i));
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    @Override
    public void onContainerClosed(PlayerEntity playerIn) {
        super.onContainerClosed(playerIn);
        this.inventory.closeInventory(playerIn);
    }

}
