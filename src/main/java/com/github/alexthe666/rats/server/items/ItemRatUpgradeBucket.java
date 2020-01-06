package com.github.alexthe666.rats.server.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

public class ItemRatUpgradeBucket extends ItemRatUpgrade {

    public ItemRatUpgradeBucket(String name, int rarity, int textLength) {
        super(name, rarity, textLength);
    }

    public static ItemStack getBucketFromFluid(FluidStack ingredient) {
        if(ingredient == null || ingredient.isEmpty()){
            return new ItemStack(Items.BUCKET);
        }
        ItemStack filledBucket = FluidUtil.getFilledBucket(ingredient);
        return filledBucket;
    }

    public static int getMbTransferRate(Item item) {
        if (item == RatsItemRegistry.RAT_UPGRADE_BUCKET || item == RatsItemRegistry.RAT_UPGRADE_MILKER) {
            return FluidAttributes.BUCKET_VOLUME;
        }
        if (item == RatsItemRegistry.RAT_UPGRADE_BIG_BUCKET) {
            return FluidAttributes.BUCKET_VOLUME * 5;
        }
        return 0;
    }
}
