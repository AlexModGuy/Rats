package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.ratlantis.EntityRatlantisArrow;
import com.github.alexthe666.rats.server.entity.ratlantis.RatlantisEntityRegistry;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemRatlantisBow extends BowItem {

    public ItemRatlantisBow() {
        super((new Item.Properties()).maxDamage(1500).group(RatsMod.getRatlantisTab()));
        this.setRegistryName(RatsMod.MODID, "ratlantis_bow");
    }

    public int getUseDuration(ItemStack stack) {
        return 36000;
    }

    @Override
    public AbstractArrowEntity customArrow(AbstractArrowEntity arrow) {
        EntityRatlantisArrow ratlantisArrow = new EntityRatlantisArrow(RatlantisEntityRegistry.RATLANTIS_ARROW, arrow.world);
        ratlantisArrow.copyDataFromOld(arrow);
        return ratlantisArrow;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslationTextComponent("item.rats.ratlantis_bow0.desc").mergeStyle(TextFormatting.YELLOW));
        tooltip.add(new TranslationTextComponent("item.rats.ratlantis_bow1.desc").mergeStyle(TextFormatting.GRAY));
    }

}
