package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class BlockMarbledCheeseGrass extends GrassBlock implements IGrowable {
    protected BlockMarbledCheeseGrass() {
        super(Block.Properties.create(Material.ORGANIC).sound(SoundType.PLANT).hardnessAndResistance(0.6F, 0));
        this.setRegistryName(RatsMod.MODID, "marbled_cheese_grass");
    }


    public void tick(BlockState state, World worldIn, BlockPos pos, Random rand) {
        if (!worldIn.isRemote) {
            if (!worldIn.isAreaLoaded(pos, 3)) return;
            if (worldIn.getLight(pos.up()) < 4 && worldIn.getBlockState(pos.up()).getOpacity(worldIn, pos.up()) > 2) {
                worldIn.setBlockState(pos, RatsBlockRegistry.MARBLED_CHEESE_DIRT.getDefaultState());
            } else {
                if (worldIn.getLight(pos.up()) >= 9) {
                    for (int i = 0; i < 4; ++i) {
                        BlockPos blockpos = pos.add(rand.nextInt(3) - 1, rand.nextInt(5) - 3, rand.nextInt(3) - 1);

                        if (blockpos.getY() >= 0 && blockpos.getY() < 256 && !worldIn.isBlockLoaded(blockpos)) {
                            return;
                        }

                        BlockState BlockState = worldIn.getBlockState(blockpos.up());
                        BlockState BlockState1 = worldIn.getBlockState(blockpos);

                        if (BlockState1.getBlock() == Blocks.DIRT && worldIn.getLight(blockpos.up()) >= 4 && BlockState.getOpacity(worldIn, pos.up()) <= 2) {
                            worldIn.setBlockState(blockpos, Blocks.GRASS.getDefaultState());
                        }
                    }
                }
            }
        }
    }

    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }
}
