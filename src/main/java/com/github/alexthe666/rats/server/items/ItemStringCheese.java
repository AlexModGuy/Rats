package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

import static com.github.alexthe666.rats.server.items.ItemGenericFood.createFood;

public class ItemStringCheese extends Item {

    public ItemStringCheese() {
        super(new Item.Properties().food(createFood(2, 0.4F, true, true, false, null)).group(RatsMod.TAB));
        this.setRegistryName(RatsMod.MODID, "string_cheese");
    }

    public int getMaxItemUseDuration(ItemStack stack) {
        return 1;
    }

    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("item.rats.string_cheese.desc").applyTextStyle(TextFormatting.GRAY));
    }
}
