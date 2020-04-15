package com.github.alexthe666.rats.server.inventory;

import com.github.alexthe666.rats.server.items.ItemRatCombinedUpgrade;
import com.github.alexthe666.rats.server.items.ItemRatUpgradeJuryRigged;
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
        System.out.println(stack);
        return stack.getItem() != upgrade.getItem() && !(stack.getItem() instanceof ItemRatCombinedUpgrade) && !(stack.getItem() instanceof ItemRatUpgradeJuryRigged);
    }

}
