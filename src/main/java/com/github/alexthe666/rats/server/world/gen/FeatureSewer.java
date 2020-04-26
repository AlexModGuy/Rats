package com.github.alexthe666.rats.server.world.gen;

import com.github.alexthe666.rats.server.blocks.BlockGenericTrapDoor;
import com.github.alexthe666.rats.server.blocks.RatsBlockRegistry;
import com.mojang.datafixers.Dynamic;
import net.minecraft.block.*;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.state.properties.Half;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FeatureSewer extends Feature<NoFeatureConfig> {

    private static final Direction[] HORIZONTALS = new Direction[]{Direction.NORTH, Direction.EAST, Direction.WEST, Direction.SOUTH};

    public FeatureSewer(Function<Dynamic<?>, ? extends NoFeatureConfig> p_i49873_1_) {
        super(p_i49873_1_);
    }

    @Override
    public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config) {
       /* if(rand.nextFloat() > 0.05){
            return false;
        }
        pos = worldIn.getHeight(Heightmap.Type.OCEAN_FLOOR_WG, pos);
        Direction facing = HORIZONTALS[rand.nextInt(3)];
        for(BlockPos surfaceEntrances : BlockPos.getAllInBox(pos.add(1, 0, 1), pos.add(1, 0, 1)).collect(Collectors.toSet())){
            worldIn.setBlockState(surfaceEntrances, getRandomSewerBlock(rand, 0), 2);
        }
        worldIn.setBlockState(pos.up(), RatsBlockRegistry.MANHOLE.getDefaultState().with(BlockGenericTrapDoor.HORIZONTAL_FACING, facing), 2);
        worldIn.setBlockState(pos.down(), Blocks.LADDER.getDefaultState().with(LadderBlock.FACING, facing), 2);
        int depth = 15;
        for(int i = 0; i < depth; i++){
            BlockPos downPos = pos.down(i);
            worldIn.setBlockState(downPos, Blocks.LADDER.getDefaultState().with(LadderBlock.FACING, facing), 2);
            worldIn.setBlockState(downPos.east(), getRandomSewerBlock(rand, 0.5F), 2);
            worldIn.setBlockState(downPos.west(), getRandomSewerBlock(rand, 0.5F), 2);
            worldIn.setBlockState(downPos.north(), getRandomSewerBlock(rand, 0.5F), 2);
            worldIn.setBlockState(downPos.south(), getRandomSewerBlock(rand, 0.5F), 2);
        }*/
        return false;
    }

    private BlockState getRandomSewerBlock(Random random, double threshold){
        return  random.nextFloat() < threshold ? Blocks.COBBLESTONE.getDefaultState() : Blocks.MOSSY_COBBLESTONE.getDefaultState();
    }
}