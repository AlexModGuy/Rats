package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.blocks.BlockRatCage;
import com.github.alexthe666.rats.server.blocks.BlockRatCageDecorated;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemRatDecoration extends Item implements IRatCageDecoration {

    public ItemRatDecoration(String name) {
        super(new Item.Properties().group(RatsMod.TAB));
        this.setRegistryName(RatsMod.MODID, name);
    }

    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("item.rats.cage_decoration.desc").applyTextStyle(TextFormatting.GRAY));
    }

    @Override
    public boolean canStay(World world, BlockPos pos, BlockRatCage cageBlock) {
        if (this == RatsItemRegistry.RAT_WATER_BOTTLE) {
            if (cageBlock instanceof BlockRatCageDecorated && world.getBlockState(pos).getBlock() instanceof BlockRatCageDecorated) {
                Direction facing = world.getBlockState(pos).get(BlockRatCageDecorated.FACING);
                return cageBlock.canFenceConnectTo(world.getBlockState(pos.offset(facing)), false, facing) == 0;
            }
            return true;
        }
        if (this == RatsItemRegistry.RAT_SEED_BOWL) {
            return cageBlock.canFenceConnectTo(world.getBlockState(pos.down()), false, Direction.DOWN) != 1;
        }
        if (this == RatsItemRegistry.RAT_BREEDING_LANTERN) {
            return cageBlock.canFenceConnectTo(world.getBlockState(pos.up()), false, Direction.UP) == 0;
        }
        return false;
    }
}
