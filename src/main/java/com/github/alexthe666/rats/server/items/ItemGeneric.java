package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class ItemGeneric extends Item {

    private int textLength = 0;

    public ItemGeneric(String name) {
        super(new Item.Properties().group(RatsMod.TAB));
        this.setRegistryName(RatsMod.MODID, name);
    }

    public ItemGeneric(String name, ItemGroup group) {
        super(new Item.Properties().group(group));
        this.setRegistryName(RatsMod.MODID, name);
    }

    public ItemGeneric(String name, int textLength) {
        this(name);
        this.textLength = textLength;
    }

    public ItemGeneric(String name, ItemGroup group, int textLength) {
        super(new Item.Properties().group(group));
        this.setRegistryName(RatsMod.MODID, name);
        this.textLength = textLength;
    }


    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (textLength > 0) {
            for (int i = 0; i < textLength; i++) {
                tooltip.add(new TranslationTextComponent(this.getTranslationKey() + ".desc" + i).func_240699_a_(TextFormatting.GRAY));
            }
        }
    }

    public boolean hasEffect(ItemStack stack) {
        return super.hasEffect(stack) || this == RatsItemRegistry.PLAGUE_TOME;
    }

}
