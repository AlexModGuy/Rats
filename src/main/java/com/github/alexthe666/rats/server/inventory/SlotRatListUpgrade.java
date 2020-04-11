package com.github.alexthe666.rats.server.inventory;

import com.github.alexthe666.rats.server.items.ItemRatListUpgrade;
import com.github.alexthe666.rats.server.items.ItemRatUpgrade;
import com.github.alexthe666.rats.server.items.ItemRatUpgradeCombined;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class SlotRatListUpgrade extends Slot {
    public ItemStack upgrade;

    public SlotRatListUpgrade(IInventory inventory, ItemStack upgrade, int id, int x, int y) {
        super(inventory, id, x, y);
        this.upgrade = upgrade;
    }

    public boolean isItemValid(ItemStack stack) {
        if(upgrade.isEmpty()){
            return super.isItemValid(stack);
        }
        return upgrade.getItem() instanceof ItemRatListUpgrade || stack.getItem() instanceof ItemRatUpgrade && stack.getItem() != upgrade.getItem() && ItemRatUpgradeCombined.canCombineWithUpgrade(upgrade, stack);
    }
}
