package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.blocks.BlockRatCage;
import com.github.alexthe666.rats.server.blocks.BlockRatCageDecorated;
import com.github.alexthe666.rats.server.blocks.ICustomRendered;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemRatDecoration extends Item implements IRatCageDecoration {

    public ItemRatDecoration(String name) {
        this.setCreativeTab(RatsMod.TAB);
        this.setTranslationKey("rats." + name);
        this.setRegistryName(RatsMod.MODID, name);
    }

    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format("item.rats.cage_decoration.desc"));
    }

    @Override
    public boolean canStay(World world, BlockPos pos, BlockRatCage cageBlock) {
        if(this == RatsItemRegistry.RAT_WATER_BOTTLE){
            if(cageBlock instanceof BlockRatCageDecorated && world.getBlockState(pos).getBlock() instanceof BlockRatCageDecorated){
                EnumFacing facing = world.getBlockState(pos).getValue(BlockRatCageDecorated.FACING);
                return cageBlock.canFenceConnectTo(world, pos, facing) == 0;
            }
            return true;
        }
        if(this == RatsItemRegistry.RAT_SEED_BOWL) {
            return cageBlock.canFenceConnectTo(world, pos, EnumFacing.DOWN) != 1;
        }
        if(this == RatsItemRegistry.RAT_BREEDING_LANTERN) {
            return cageBlock.canFenceConnectTo(world, pos, EnumFacing.UP) == 0;
        }
        return false;
    }
}
