package com.github.alexthe666.rats.server.world.gen;

import net.minecraft.block.BlockVine;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class WorldGenBetterVine extends WorldGenerator
{
    public boolean generate(World worldIn, Random rand, BlockPos position)
    {
        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL.facings()){
            if (Blocks.VINE.canPlaceBlockOnSide(worldIn, position, enumfacing)){
                IBlockState iblockstate = Blocks.VINE.getDefaultState().withProperty(BlockVine.NORTH, Boolean.valueOf(enumfacing == EnumFacing.NORTH)).withProperty(BlockVine.EAST, Boolean.valueOf(enumfacing == EnumFacing.EAST)).withProperty(BlockVine.SOUTH, Boolean.valueOf(enumfacing == EnumFacing.SOUTH)).withProperty(BlockVine.WEST, Boolean.valueOf(enumfacing == EnumFacing.WEST));
                worldIn.setBlockState(position, iblockstate, 2);
                break;
            }
        }
        return true;
    }
}