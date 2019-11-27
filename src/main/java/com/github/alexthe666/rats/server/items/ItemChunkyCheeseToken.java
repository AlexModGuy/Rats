package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.text.NumberFormat;
import java.util.List;

public class ItemChunkyCheeseToken extends Item {

    public ItemChunkyCheeseToken() {
        super();
        this.setCreativeTab(RatsMod.TAB);
        this.setTranslationKey("rats.chunky_cheese_token");
        this.setRegistryName(RatsMod.MODID, "chunky_cheese_token");
    }


    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        String formattedChance = NumberFormat.getNumberInstance().format(RatConfig.tokenDropRate);
        tooltip.add(I18n.format("item.rats.chunky_cheese_token.desc0", formattedChance));
        if(!RatConfig.disableRatlantis){
            tooltip.add(I18n.format("item.rats.chunky_cheese_token.desc1"));
        }
    }

    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.UNCOMMON;
    }
}
