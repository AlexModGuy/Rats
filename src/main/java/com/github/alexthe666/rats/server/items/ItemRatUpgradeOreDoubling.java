package com.github.alexthe666.rats.server.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Tags;

public class ItemRatUpgradeOreDoubling extends ItemRatUpgrade {

    public ItemRatUpgradeOreDoubling() {
        super("rat_upgrade_ore_doubling", 2, 4);

    }


    public static boolean isProcessable(ItemStack stack) {
        boolean b = Tags.Blocks.ORES.getAllElements().contains(Block.getBlockFromItem(stack.getItem()));
        return b;
    }

    public static ItemStack getProcessedIngot(ItemStack stack) {
        ItemStack one = new ItemStack(stack.getItem(), 1);
        return one;
    }

    public static ItemStack getProcessedOre(ItemStack stack) {
        ItemStack one = new ItemStack(stack.getItem(), 1);
        return one;
    }
}
