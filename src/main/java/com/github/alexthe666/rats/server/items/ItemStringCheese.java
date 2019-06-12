package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

public class ItemStringCheese extends ItemFood {

    public ItemStringCheese() {
        super(2, 0.4F, true);
        this.setCreativeTab(RatsMod.TAB);
        this.setTranslationKey("rats.string_cheese");
        this.setRegistryName(RatsMod.MODID, "string_cheese");
    }

    public int getMaxItemUseDuration(ItemStack stack){
        return 1;
    }

}
