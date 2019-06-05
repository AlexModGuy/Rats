package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.item.ItemFood;

public class ItemCheese extends ItemFood {

    public ItemCheese() {
        super(3, 0.6F, false);
        this.setCreativeTab(RatsMod.TAB);
        this.setTranslationKey("rats.cheese");
        this.setRegistryName(RatsMod.MODID, "cheese");
    }
}
