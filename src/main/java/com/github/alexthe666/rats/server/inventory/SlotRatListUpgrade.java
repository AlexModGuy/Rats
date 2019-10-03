package com.github.alexthe666.rats.server.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotRatListUpgrade extends Slot {
    public ItemStack upgrade;

    public SlotRatListUpgrade(IInventory playerInventory, ItemStack upgrade, int id, int x, int y) {
        super(playerInventory, id, x, y);
        this.upgrade = upgrade;
    }

    public boolean isItemValid(ItemStack stack) {
        return stack.getItem() != upgrade.getItem();
    }

}
