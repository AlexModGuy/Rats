package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

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

    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format("item.rats.string_cheese.desc"));
    }
}
