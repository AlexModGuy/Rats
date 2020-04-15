package com.github.alexthe666.rats.server.inventory;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerRatUpgradeJuryRigged  extends SyncedFieldContainer {

    private ItemStack stack;
    private IInventory inventory;
    private IInventory inventoryPlayer;

    public ContainerRatUpgradeJuryRigged(EntityPlayer player, InventoryPlayer playerInventory, InventoryRatUpgrade itemInventory) {
        super(itemInventory);
        int numRows = itemInventory.getSizeInventory() / 9;
        this.inventory = itemInventory;
        this.inventoryPlayer = playerInventory;
        itemInventory.openInventory(player);
        this.stack = itemInventory.upgradeStack;
        this.addSlotToContainer(new SlotRatListUpgrade(inventory, itemInventory.upgradeStack, 0, 44, 18));
        this.addSlotToContainer(new SlotRatListUpgrade(inventory, itemInventory.upgradeStack, 1, 116, 18));
        int i = (numRows - 4) * 18;
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
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        this.inventory.closeInventory(playerIn);
    }

}
