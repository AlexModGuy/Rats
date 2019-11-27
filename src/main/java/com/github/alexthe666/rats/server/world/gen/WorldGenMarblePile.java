package com.github.alexthe666.rats.server.world.gen;

import com.github.alexthe666.rats.server.blocks.RatsBlockRegistry;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.BlockStairs;
import net.minecraft.init.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class WorldGenMarblePile extends WorldGenerator {
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        for (int i = 0; i < 64; ++i) {
            BlockPos blockpos = position.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));

            if (RatsBlockRegistry.MARBLED_CHEESE_PILLAR.canPlaceBlockAt(worldIn, blockpos) && worldIn.getBlockState(blockpos.down()).getBlock() == Blocks.GRASS) {
                if (rand.nextInt(3) == 0) {
                    BlockPos height = new BlockPos(blockpos);
                    int h = 0;
                    worldIn.setBlockState(height.down(), RatsBlockRegistry.MARBLED_CHEESE_TILE.getDefaultState(), 2);
                    for (; h < 3 + rand.nextInt(3); h++) {
                        worldIn.setBlockState(height.up(h), RatsBlockRegistry.MARBLED_CHEESE_PILLAR.getDefaultState().with(BlockRotatedPillar.AXIS, Direction.Axis.Y), 2);
                    }
                    worldIn.setBlockState(height.up(h), RatsBlockRegistry.MARBLED_CHEESE_CHISELED.getDefaultState(), 2);
                    worldIn.setBlockState(height.up(h + 1), RatsBlockRegistry.MARBLED_CHEESE_TILE.getDefaultState(), 2);
                    for (Direction facing : Direction.HORIZONTALS) {
                        worldIn.setBlockState(height.up(h + 1).offset(facing), RatsBlockRegistry.MARBLED_CHEESE_STAIRS.getDefaultState().with(BlockStairs.FACING, facing.getOpposite()).with(BlockStairs.HALF, BlockStairs.EnumHalf.TOP), 2);
                    }
                } else if (rand.nextInt(2) == 0) {
                    Direction randFacing = Direction.HORIZONTALS[rand.nextInt(Direction.HORIZONTALS.length - 1)];
                    Direction.Axis axis = randFacing.getAxis();
                    worldIn.setBlockState(blockpos, RatsBlockRegistry.MARBLED_CHEESE_PILLAR.getDefaultState().with(BlockRotatedPillar.AXIS, axis), 2);
                    worldIn.setBlockState(blockpos.offset(randFacing), RatsBlockRegistry.MARBLED_CHEESE_PILLAR.getDefaultState().with(BlockRotatedPillar.AXIS, axis), 2);
                } else {
                    worldIn.setBlockState(blockpos, RatsBlockRegistry.MARBLED_CHEESE_PILLAR.getDefaultState().with(BlockRotatedPillar.AXIS, Direction.Axis.values()[rand.nextInt(Direction.Axis.values().length - 1)]), 2);

                }
            }
        }

        return true;
    }
}