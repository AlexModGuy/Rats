package com.github.alexthe666.rats.server.world;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

import java.util.Random;
import java.util.function.Function;

public class RatlantisSurfaceBuilder extends SurfaceBuilder<SurfaceBuilderConfig> {

    private static float height = 100;
    private static float width = 5;
    private PerlinNoise perlin1;
    private PerlinNoise perlin2;
    private boolean init = false;
    public RatlantisSurfaceBuilder(Function<Dynamic<?>, ? extends SurfaceBuilderConfig> p_i51305_1_) {
        super(p_i51305_1_);

    }

    @Override
    public void buildSurface(Random rand, IChunk chunkIn, Biome biomeIn, int chunkX, int chunkZ, int startHeight, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, long seed, SurfaceBuilderConfig config) {
        if(!init){
            init = true;
            perlin1 = new PerlinNoise(seed);
            perlin2 = new PerlinNoise(seed + 100L);
        }
        int x = chunkX << 4;
        int z = chunkZ << 4;
        for (int i = x; i < x + 16; i++) {
            for (int k = z; k < z + 16; k++) {
                float dis = perlin2.turbulence2(i / 150F, k / 150F, 10) * 300 + 200;
                float heightBase = height - (dis / (float) width) + (perlin1.turbulence2(i / 50F, k / 50F, 4F) * 5F);
                for (int y = 0; y < 256; y++) {
                    Block i4 = Blocks.AIR;
                    if (heightBase > 67) {
                        if (y < 2 + rand.nextInt(2)) {
                            i4 = Blocks.BEDROCK;
                        } else if (y < heightBase - 3) {
                            i4 = Blocks.STONE;
                        } else if (y < heightBase - 1) {
                            i4 = Blocks.DIRT;
                        } else if (y < heightBase) {
                            i4 = Blocks.GRASS;
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
                    chunkIn.setBlockState(new BlockPos(i - x, y, k - z), i4.getDefaultState(), false);
                }
            }
        }
    }
}
