package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.blocks.ICustomRendered;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;

public class ItemRatIgloo extends Item implements ICustomRendered {

    public ItemRatIgloo(EnumDyeColor color) {
        this.setCreativeTab(RatsMod.TAB);
        this.setTranslationKey("rats.rat_igloo_" + color.getName());
        this.setRegistryName(RatsMod.MODID, "rat_igloo_" + color.getName());
    }
}
