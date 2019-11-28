package com.github.alexthe666.rats.server.world.gen;

import net.minecraft.block.BlockVine;
import net.minecraft.block.state.BlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class WorldGenBetterVine extends WorldGenerator {
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        for (Direction Direction : Direction.Plane.HORIZONTAL.facings()) {
            if (Blocks.VINE.canPlaceBlockOnSide(worldIn, position, Direction)) {
                BlockState BlockState = Blocks.VINE.getDefaultState().with(BlockVine.NORTH, Boolean.valueOf(Direction == net.minecraft.util.Direction.NORTH)).with(BlockVine.EAST, Boolean.valueOf(Direction == net.minecraft.util.Direction.EAST)).with(BlockVine.SOUTH, Boolean.valueOf(Direction == net.minecraft.util.Direction.SOUTH)).with(BlockVine.WEST, Boolean.valueOf(Direction == net.minecraft.util.Direction.WEST));
                worldIn.setBlockState(position, BlockState, 2);
                break;
            }
        }
        return true;
    }
}