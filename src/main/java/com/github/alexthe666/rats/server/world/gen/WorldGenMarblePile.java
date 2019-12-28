package com.github.alexthe666.rats.server.world.gen;

import com.github.alexthe666.rats.server.blocks.RatsBlockRegistry;
import com.mojang.datafixers.Dynamic;
import net.minecraft.block.Blocks;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.state.properties.Half;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;
import java.util.function.Function;

public class WorldGenMarblePile extends Feature<NoFeatureConfig> {

    private static final Direction[] HORIZONTALS = new Direction[]{Direction.NORTH, Direction.EAST, Direction.WEST, Direction.SOUTH};

    public WorldGenMarblePile(Function<Dynamic<?>, ? extends NoFeatureConfig> p_i49873_1_) {
        super(p_i49873_1_);
    }

    @Override
    public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        for (int i = 0; i < 64; ++i) {
            BlockPos blockpos = pos.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));

            if (worldIn.getBlockState(blockpos.down()).getBlock() == Blocks.GRASS) {
                if (rand.nextInt(3) == 0) {
                    BlockPos height = new BlockPos(blockpos);
                    int h = 0;
                    worldIn.setBlockState(height.down(), RatsBlockRegistry.MARBLED_CHEESE_TILE.getDefaultState(), 2);
                    for (; h < 3 + rand.nextInt(3); h++) {
                        worldIn.setBlockState(height.up(h), RatsBlockRegistry.MARBLED_CHEESE_PILLAR.getDefaultState().with(RotatedPillarBlock.AXIS, Direction.Axis.Y), 2);
                    }
                    worldIn.setBlockState(height.up(h), RatsBlockRegistry.MARBLED_CHEESE_CHISELED.getDefaultState(), 2);
                    worldIn.setBlockState(height.up(h + 1), RatsBlockRegistry.MARBLED_CHEESE_TILE.getDefaultState(), 2);
                    for (Direction facing : HORIZONTALS) {
                        worldIn.setBlockState(height.up(h + 1).offset(facing), RatsBlockRegistry.MARBLED_CHEESE_STAIRS.getDefaultState().with(StairsBlock.FACING, facing.getOpposite()).with(StairsBlock.HALF, Half.TOP), 2);
                    }
                } else if (rand.nextInt(2) == 0) {
                    Direction randFacing = HORIZONTALS[rand.nextInt(HORIZONTALS.length - 1)];
                    Direction.Axis axis = randFacing.getAxis();
                    worldIn.setBlockState(blockpos, RatsBlockRegistry.MARBLED_CHEESE_PILLAR.getDefaultState().with(RotatedPillarBlock.AXIS, axis), 2);
                    worldIn.setBlockState(blockpos.offset(randFacing), RatsBlockRegistry.MARBLED_CHEESE_PILLAR.getDefaultState().with(RotatedPillarBlock.AXIS, axis), 2);
                } else {
                    worldIn.setBlockState(blockpos, RatsBlockRegistry.MARBLED_CHEESE_PILLAR.getDefaultState().with(RotatedPillarBlock.AXIS, Direction.Axis.values()[rand.nextInt(Direction.Axis.values().length - 1)]), 2);

                }
            }
        }

        return true;
    }
}