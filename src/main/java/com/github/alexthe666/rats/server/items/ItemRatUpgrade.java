package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemRatUpgrade extends Item {
    private int rarity = 0;
    private int textLength = 0;

    public ItemRatUpgrade(String name, int stacksize) {
        super(new Item.Properties().group(RatsMod.TAB_UPGRADES).maxStackSize(stacksize));
        this.setRegistryName(RatsMod.MODID, name);
    }

    public ItemRatUpgrade(String name, int stacksize, int rarity, int textLength) {
        this(name, stacksize);
        this.rarity = rarity;
        this.textLength = textLength;
    }

    public ItemRatUpgrade(String name) {
        this(name, 64);
    }

    public ItemRatUpgrade(String name, int rarity, int textLength) {
        this(name, 64,rarity, textLength);
    }

    public Rarity getRarity(ItemStack stack) {
        if (rarity != 0 && rarity != 4) {
            return Rarity.values()[rarity];
        }
        return super.getRarity(stack);
    }

    public boolean hasEffect(ItemStack stack) {
        return rarity >= 3 || super.hasEffect(stack);
    }


    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (textLength > 0) {
            for (int i = 0; i < textLength; i++) {
                tooltip.add(new TranslationTextComponent(this.getTranslationKey() + i + ".desc").applyTextStyle(TextFormatting.GRAY));

            }
        } else {
            tooltip.add(new TranslationTextComponent(this.getTranslationKey() + ".desc").applyTextStyle(TextFormatting.GRAY));
        }
    }
}
