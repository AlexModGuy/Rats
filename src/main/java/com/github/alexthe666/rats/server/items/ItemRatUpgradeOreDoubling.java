package com.github.alexthe666.rats.server.items;

import net.minecraft.item.ItemStack;

public class ItemRatUpgradeOreDoubling extends ItemRatUpgrade {

    public ItemRatUpgradeOreDoubling() {
        super("rat_upgrade_ore_doubling", 2, 4);
    }


    public static boolean isProcessable(ItemStack stack) {
        ItemStack one = new ItemStack(stack.getItem(), 1, stack.getMetadata());
        return RatsNuggetRegistry.ORE_TO_INGOTS.get(one) != null;
    }

    public static ItemStack getProcessedIngot(ItemStack stack) {
        ItemStack one = new ItemStack(stack.getItem(), 1, stack.getMetadata());
        return RatsNuggetRegistry.ORE_TO_INGOTS.get(one);
    }

    public static ItemStack getProcessedOre(ItemStack stack) {
        ItemStack one = new ItemStack(stack.getItem(), 1, stack.getMetadata());
        return one;
    }
}
