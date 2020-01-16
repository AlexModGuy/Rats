package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.RatsMod;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.text.NumberFormat;
import java.util.List;

public class ItemChunkyCheeseToken extends Item {

    public ItemChunkyCheeseToken() {
        super(new Item.Properties().group(RatsMod.TAB));
        this.setRegistryName(RatsMod.MODID, "chunky_cheese_token");
    }

    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        String formattedChance = NumberFormat.getNumberInstance().format(RatConfig.tokenDropRate);
        tooltip.add(new TranslationTextComponent("item.rats.chunky_cheese_token.desc0", formattedChance).applyTextStyle(TextFormatting.GRAY));
        if (!RatConfig.disableRatlantis) {
            tooltip.add(new TranslationTextComponent("item.rats.chunky_cheese_token.desc1").applyTextStyle(TextFormatting.GRAY));
        }
    }

    public Rarity getRarity(ItemStack stack) {
        return Rarity.UNCOMMON;
    }
}
