package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockMarbledCheeseGrass extends Block implements IGrowable {
    protected BlockMarbledCheeseGrass() {
        super(Material.GRASS);
        this.setTickRandomly(true);
        this.setCreativeTab(RatsMod.TAB);
        this.setSoundType(SoundType.PLANT);
        this.setHardness(0.6F);
        this.setTranslationKey("rats.marbled_cheese_grass");
        this.setRegistryName(RatsMod.MODID, "marbled_cheese_grass");
    }


    public void updateTick(World worldIn, BlockPos pos, BlockState state, Random rand) {
        if (!worldIn.isRemote) {
            if (!worldIn.isAreaLoaded(pos, 3)) return;
            if (worldIn.getLightFromNeighbors(pos.up()) < 4 && worldIn.getBlockState(pos.up()).getLightOpacity(worldIn, pos.up()) > 2) {
                worldIn.setBlockState(pos, RatsBlockRegistry.MARBLED_CHEESE_DIRT.getDefaultState());
            } else {
                if (worldIn.getLightFromNeighbors(pos.up()) >= 9) {
                    for (int i = 0; i < 4; ++i) {
                        BlockPos blockpos = pos.add(rand.nextInt(3) - 1, rand.nextInt(5) - 3, rand.nextInt(3) - 1);

                        if (blockpos.getY() >= 0 && blockpos.getY() < 256 && !worldIn.isBlockLoaded(blockpos)) {
                            return;
                        }

                        BlockState BlockState = worldIn.getBlockState(blockpos.up());
                        BlockState BlockState1 = worldIn.getBlockState(blockpos);

                        if (BlockState1.getBlock() == Blocks.DIRT && BlockState1.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.DIRT && worldIn.getLightFromNeighbors(blockpos.up()) >= 4 && BlockState.getLightOpacity(worldIn, pos.up()) <= 2) {
                            worldIn.setBlockState(blockpos, Blocks.GRASS.getDefaultState());
                        }
                    }
                }
            }
        }
    }

    public Item getItemDropped(BlockState state, Random rand, int fortune) {
        return Blocks.DIRT.getItemDropped(Blocks.DIRT.getDefaultState().with(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT), rand, fortune);
    }

    public boolean canGrow(World worldIn, BlockPos pos, BlockState state, boolean isClient) {
        return true;
    }

    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, BlockState state) {
        return true;
    }

    public void grow(World worldIn, Random rand, BlockPos pos, BlockState state) {
        BlockPos blockpos = pos.up();

        for (int i = 0; i < 128; ++i) {
            BlockPos blockpos1 = blockpos;
            int j = 0;

            while (true) {
                if (j >= i / 16) {
                    if (worldIn.isAirBlock(blockpos1)) {
                        if (rand.nextInt(8) == 0) {
                            worldIn.getBiome(blockpos1).plantFlower(worldIn, rand, blockpos1);
                        } else {
                            BlockState BlockState1 = Blocks.TALLGRASS.getDefaultState().with(BlockTallGrass.TYPE, BlockTallGrass.EnumType.GRASS);

                            if (Blocks.TALLGRASS.canBlockStay(worldIn, blockpos1, BlockState1)) {
                                worldIn.setBlockState(blockpos1, BlockState1, 3);
                            }
                        }
                    }

                    break;
                }

                blockpos1 = blockpos1.add(rand.nextInt(3) - 1, (rand.nextInt(3) - 1) * rand.nextInt(3) / 2, rand.nextInt(3) - 1);

                if (worldIn.getBlockState(blockpos1.down()).getBlock() != Blocks.GRASS || worldIn.getBlockState(blockpos1).isNormalCube()) {
                    break;
                }

                ++j;
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }
}
