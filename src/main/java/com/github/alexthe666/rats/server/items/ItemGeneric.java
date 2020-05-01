package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemGeneric extends Item {

    private int textLength = 0;

    public ItemGeneric(String name) {
        super();
        this.setCreativeTab(RatsMod.TAB);
        this.setTranslationKey("rats." + name);
        this.setRegistryName(RatsMod.MODID, name);
    }

    public ItemGeneric(String name, int textLength) {
        this(name);
        this.textLength = textLength;
    }

    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return super.hasEffect(stack) || this == RatsItemRegistry.PLAGUE_TOME;
    }


    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (textLength > 0) {
            for (int i = 0; i < textLength; i++) {
                tooltip.add(I18n.format(this.getTranslationKey() + ".desc" + i));
            }
        }
    }
}
