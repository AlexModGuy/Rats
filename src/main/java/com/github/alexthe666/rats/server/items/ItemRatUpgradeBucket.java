package com.github.alexthe666.rats.server.items;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

public class ItemRatUpgradeBucket extends ItemRatUpgrade {

    public ItemRatUpgradeBucket(String name, int rarity, int textLength) {
        super(name, rarity, textLength);
    }

    public static ItemStack getBucketFromFluid(FluidStack ingredient) {
        if (ingredient == null) {
            return new ItemStack(Items.BUCKET);
        }
        if (ingredient.getFluid() == FluidRegistry.WATER) {
            return new ItemStack(Items.WATER_BUCKET);
        } else if (ingredient.getFluid() == FluidRegistry.LAVA) {
            return new ItemStack(Items.LAVA_BUCKET);
        } else if (ingredient.getFluid().getName().equals("milk")) {
            return new ItemStack(Items.MILK_BUCKET);
        } else if (FluidRegistry.isUniversalBucketEnabled()) {
            ItemStack filledBucket = FluidUtil.getFilledBucket(ingredient);
            FluidStack fluidContained = FluidUtil.getFluidContained(filledBucket);
            if (fluidContained != null && fluidContained.isFluidEqual(ingredient)) {
                return filledBucket;
            }
        }
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
