package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.blocks.BlockRatCage;
import com.github.alexthe666.rats.server.blocks.ICustomRendered;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemRatIgloo extends Item implements ICustomRendered, IRatCageDecoration {
    public EnumDyeColor color;

    public ItemRatIgloo(EnumDyeColor color) {
        this.setCreativeTab(RatsMod.TAB);
        this.setTranslationKey("rats.rat_igloo_" + color.getName());
        this.setRegistryName(RatsMod.MODID, "rat_igloo_" + color.getName());
        this.color = color;
    }

    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format("item.rats.cage_decoration.desc"));
    }

    @Override
    public boolean canStay(World world, BlockPos pos, BlockRatCage cageBlock) {
        return cageBlock.canFenceConnectTo(world, pos, Direction.DOWN) != 1;
    }
}
