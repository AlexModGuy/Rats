package com.github.alexthe666.rats.server.inventory;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.items.ItemRatUpgrade;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class ContainerRatUpgradeJuryRigged extends Container {

    public IInventory inventory;
    private IInventory inventoryPlayer;

    public ContainerRatUpgradeJuryRigged(int id, IInventory itemInventory, PlayerInventory playerInventory) {
        super(RatsContainerRegistry.RAT_UPGRADE_JR_CONTAINER, id);
        this.inventory = itemInventory;
        this.inventoryPlayer = playerInventory;
        itemInventory.openInventory(playerInventory.player);
        int i = 18;
        this.addSlot(new SlotRatListUpgrade(inventory, RatsMod.PROXY.getRefrencedItem(), 0, 44, 18));
        this.addSlot(new SlotRatListUpgrade(inventory, RatsMod.PROXY.getRefrencedItem(), 1, 116, 18));

        for (int l = 0; l < 3; ++l) {
            for (int j1 = 0; j1 < 9; ++j1) {
                this.addSlot(new Slot(playerInventory, j1 + l * 9 + 9, 8 + j1 * 18, 31 + l * 18 + i));
            }
        }
        for (int i1 = 0; i1 < 9; ++i1) {
            this.addSlot(new Slot(playerInventory, i1, 8 + i1 * 18, 89 + i));
        }
    }

    public ContainerRatUpgradeJuryRigged(int i, PlayerInventory playerInventory) {
        this(i, new Inventory(2), playerInventory);
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

    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
             if (index != 1 && index != 0) {
                if (itemstack1.getItem() instanceof ItemRatUpgrade) {
                    if (!this.mergeItemStack(itemstack1, 0, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 2 && index < 30) {
                    if (!this.mergeItemStack(itemstack1, 30, 38, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 30 && index < 38 && !this.mergeItemStack(itemstack1, 2, 30, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 2, 38, false)) {
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
