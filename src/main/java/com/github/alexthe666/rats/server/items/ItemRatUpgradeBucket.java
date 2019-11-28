package com.github.alexthe666.rats.server.items;

import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

public class ItemRatUpgradeBucket extends ItemRatUpgrade {

    public ItemRatUpgradeBucket(String name, int rarity, int textLength) {
        super(name, rarity, textLength);
    }

    public static ItemStack getBucketFromFluid(FluidStack ingredient) {
        net.minecraftforge.fluids.Fluid fluid = ingredient.getFluid();
        ItemStack filledBucket = FluidUtil.getFilledBucket(ingredient);
        //TODO: Forge needs to finish its fluid implementation
        return new ItemStack(Items.BUCKET);
    }

    public static int getMbTransferRate(Item item) {
        if (item == RatsItemRegistry.RAT_UPGRADE_BUCKET || item == RatsItemRegistry.RAT_UPGRADE_MILKER) {
            return Fluid.BUCKET_VOLUME;
        }
        if (item == RatsItemRegistry.RAT_UPGRADE_BIG_BUCKET) {
            return Fluid.BUCKET_VOLUME * 5;
        }
        return 0;
    }
}
