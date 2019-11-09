package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemRatUpgrade extends ItemGeneric {
    private int rarity = 0;
    private int textLength = 0;

    public ItemRatUpgrade(String name) {
        super(name);
        this.setCreativeTab(RatsMod.TAB_UPGRADES);
    }

    public ItemRatUpgrade(String name, int rarity, int textLength) {
        this(name);
        this.rarity = rarity;
        this.textLength = textLength;
        this.setCreativeTab(RatsMod.TAB_UPGRADES);
    }

    public EnumRarity getRarity(ItemStack stack) {
        if (rarity != 0 && rarity != 4) {
            return EnumRarity.values()[rarity];
        }
        return super.getRarity(stack);
    }

    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return rarity >= 3 || super.hasEffect(stack);
    }


    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (textLength > 0) {
            for (int i = 0; i < textLength; i++) {
                tooltip.add(I18n.format(this.getTranslationKey() + i + ".desc"));

            }
        } else {
            tooltip.add(I18n.format(this.getTranslationKey() + ".desc"));
        }
    }
}
