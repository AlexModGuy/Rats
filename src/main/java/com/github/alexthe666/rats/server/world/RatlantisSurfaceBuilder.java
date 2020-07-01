package com.github.alexthe666.rats.server.world;

import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

import java.util.Random;

public class RatlantisSurfaceBuilder extends SurfaceBuilder<SurfaceBuilderConfig> {

    private static final float HEIGHT = 100;
    private static final float WIDTH = 5;
    private PerlinNoise perlin1 = new PerlinNoise(100);
    private PerlinNoise perlin2 = new PerlinNoise(200);
    private long prevSeed = -1;

    public RatlantisSurfaceBuilder(Codec<SurfaceBuilderConfig> p_i232127_1_) {
        super(p_i232127_1_);
    }


    public void setSeed(long seed) {
        super.setSeed(seed);
    }

    @Override
    public void buildSurface(Random rand, IChunk chunkIn, Biome biomeIn, int chunkX, int chunkZ, int startHeight, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, long seed, SurfaceBuilderConfig config) {
        if(prevSeed != seed){
            perlin1 = new PerlinNoise(rand.nextLong());
            perlin2 = new PerlinNoise(rand.nextLong());
            prevSeed = seed;
        }
        int i = chunkX;
        int k = chunkZ;
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();
        float dis = perlin2.turbulence2(i / 150F, k / 150F, 10) * 300 + 200;
        float heightBase = HEIGHT - (dis / WIDTH) + (perlin1.turbulence2(i / 50F, k / 50F, 4F) * 5F);
        for (int y = 0; y < 256; y++) {
            blockpos$mutable.setPos(i, y, k);
            Block i4 = Blocks.AIR;
            if (heightBase > 67) {
                if (y < 2 + rand.nextInt(2)) {
                    i4 = Blocks.BEDROCK;
                } else if (y < heightBase - 3) {
                    i4 = Blocks.STONE;
                } else if (y < heightBase - 2) {
                    i4 = Blocks.DIRT;
                } else if (y < heightBase - 1) {
                    i4 = Blocks.GRASS_BLOCK;
                }
            } else {
                if (y < 2 + rand.nextInt(2)) {
                    i4 = Blocks.BEDROCK;
                } else if (y < heightBase - 6 + rand.nextInt(3)) {
                    i4 = Blocks.STONE;
                } else if (y < heightBase - 3) {
                    i4 = Blocks.SANDSTONE;
                } else if (y < heightBase) {
                    i4 = Blocks.SAND;
                } else if (y <= 64) {
                    i4 = Blocks.WATER;
                }
            }
            chunkIn.setBlockState(blockpos$mutable, i4.getDefaultState(), false);
        }
    }
}
