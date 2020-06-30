package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.EntityRatlantisArrow;
import com.github.alexthe666.rats.server.entity.RatsEntityRegistry;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemRatlantisBow extends BowItem {

    public ItemRatlantisBow() {
        super((new Item.Properties()).maxDamage(1500).group(RatsMod.TAB));
        this.setRegistryName(RatsMod.MODID, "ratlantis_bow");
    }

    public int getUseDuration(ItemStack stack) {
        return 36000;
    }

    @Override
    public AbstractArrowEntity customeArrow(AbstractArrowEntity arrow) {
        EntityRatlantisArrow ratlantisArrow = new EntityRatlantisArrow(RatsEntityRegistry.RATLANTIS_ARROW, arrow.world);
        ratlantisArrow.copyDataFromOld(arrow);
        return ratlantisArrow;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslationTextComponent("item.rats.ratlantis_bow0.desc").func_240699_a_(TextFormatting.YELLOW));
        tooltip.add(new TranslationTextComponent("item.rats.ratlantis_bow1.desc").func_240699_a_(TextFormatting.GRAY));
    }

}
