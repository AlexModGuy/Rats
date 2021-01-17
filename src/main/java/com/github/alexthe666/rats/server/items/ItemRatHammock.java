package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.blocks.BlockRatCage;
import com.github.alexthe666.rats.server.blocks.ICustomRendered;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class ItemRatHammock extends Item implements ICustomRendered, IRatCageDecoration {
    public DyeColor color;

    public ItemRatHammock(DyeColor color) {
        super(new Item.Properties().group(RatsMod.TAB));
        this.setRegistryName(RatsMod.MODID, "rat_hammock_" + color.getTranslationKey());
        this.color = color;
    }

    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("item.rats.cage_decoration.desc").mergeStyle(TextFormatting.GRAY));
    }

    @Override
    public boolean canStay(World world, BlockPos pos, BlockRatCage cageBlock) {
        return cageBlock.canFenceConnectTo(world.getBlockState(pos.offset(Direction.UP)), false, Direction.UP) != 1;
    }
}