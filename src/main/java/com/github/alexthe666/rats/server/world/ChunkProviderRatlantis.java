package com.github.alexthe666.rats.server.world;

import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.NoiseChunkGenerator;
import net.minecraft.world.gen.OctavesNoiseGenerator;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.spawner.WorldEntitySpawner;

public class ChunkProviderRatlantis extends NoiseChunkGenerator {
    private final OctavesNoiseGenerator depthNoise;
    private static final float[] field_222576_h = Util.make(new float[25], (p_222575_0_) -> {
        for(int i = -2; i <= 2; ++i) {
            for(int j = -2; j <= 2; ++j) {
                float f = 10.0F / MathHelper.sqrt((float)(i * i + j * j) + 0.2F);
                p_222575_0_[i + 2 + (j + 2) * 5] = f;
            }
        }

    });

    public ChunkProviderRatlantis(IWorld world, BiomeProvider provider, GenerationSettings settings) {
        super(world, provider, 4, 8, 256, settings, true);
        this.depthNoise = new OctavesNoiseGenerator(this.randomSeed, 16);
    }

    protected void func_222548_a(double[] p_222548_1_, int p_222548_2_, int p_222548_3_) {
        this.func_222546_a(p_222548_1_, p_222548_2_, p_222548_3_, (double)684.412F, (double)684.412F, 8.555149841308594D, 4.277574920654297D, 3, -10);
    }

    protected double func_222545_a(double p_222545_1_, double p_222545_3_, int p_222545_5_) {
        double lvt_6_1_ = 8.5D;
        double lvt_8_1_ = ((double)p_222545_5_ - (8.5D + p_222545_1_ * 8.5D / 8.0D * 4.0D)) * 12.0D * 128.0D / 256.0D / p_222545_3_;
        if (lvt_8_1_ < 0.0D) {
            lvt_8_1_ *= 4.0D;
        }
        return lvt_8_1_;
    }

    protected double[] func_222549_a(int p_222549_1_, int p_222549_2_) {
        double[] lvt_3_1_ = new double[2];
        float lvt_4_1_ = 0.0F;
        float lvt_5_1_ = 0.0F;
        float lvt_6_1_ = 0.0F;
        int lvt_7_1_ = 1;
        float lvt_8_1_ = this.biomeProvider.func_222366_b(p_222549_1_, p_222549_2_).getDepth();

        for(int lvt_9_1_ = -2; lvt_9_1_ <= 2; ++lvt_9_1_) {
            for(int lvt_10_1_ = -2; lvt_10_1_ <= 2; ++lvt_10_1_) {
                Biome lvt_11_1_ = this.biomeProvider.func_222366_b(p_222549_1_ + lvt_9_1_, p_222549_2_ + lvt_10_1_);
                float lvt_12_1_ = lvt_11_1_.getDepth();
                float lvt_13_1_ = lvt_11_1_.getScale();
                float lvt_14_1_ = field_222576_h[lvt_9_1_ + 2 + (lvt_10_1_ + 2) * 5] / (lvt_12_1_ + 2.0F);
                if (lvt_11_1_.getDepth() > lvt_8_1_) {
                    lvt_14_1_ /= 2.0F;
                }

                lvt_4_1_ += lvt_13_1_ * lvt_14_1_;
                lvt_5_1_ += lvt_12_1_ * lvt_14_1_;
                lvt_6_1_ += lvt_14_1_;
            }
        }

        lvt_4_1_ /= lvt_6_1_;
        lvt_5_1_ /= lvt_6_1_;
        lvt_4_1_ = lvt_4_1_ * 0.9F + 0.1F;
        lvt_5_1_ = (lvt_5_1_ * 4.0F - 1.0F) / 8.0F;
        lvt_3_1_[0] = (double)lvt_5_1_ + this.func_222574_c(p_222549_1_, p_222549_2_);
        lvt_3_1_[1] = (double)lvt_4_1_;
        return lvt_3_1_;
    }

    public void spawnMobs(WorldGenRegion p_202093_1_) {
        int lvt_2_1_ = p_202093_1_.getMainChunkX();
        int lvt_3_1_ = p_202093_1_.getMainChunkZ();
        Biome lvt_4_1_ = p_202093_1_.getChunk(lvt_2_1_, lvt_3_1_).getBiomes()[0];
        SharedSeedRandom lvt_5_1_ = new SharedSeedRandom();
        lvt_5_1_.setDecorationSeed(p_202093_1_.getSeed(), lvt_2_1_ << 4, lvt_3_1_ << 4);
        WorldEntitySpawner.performWorldGenSpawning(p_202093_1_, lvt_4_1_, lvt_2_1_, lvt_3_1_, lvt_5_1_);
    }

    private double func_222574_c(int p_222574_1_, int p_222574_2_) {
        double lvt_3_1_ = this.depthNoise.func_215462_a((double)(p_222574_1_ * 200), 10.0D, (double)(p_222574_2_ * 200), 1.0D, 0.0D, true) / 8000.0D;
        if (lvt_3_1_ < 0.0D) {
            lvt_3_1_ = -lvt_3_1_ * 0.3D;
        }

        lvt_3_1_ = lvt_3_1_ * 3.0D - 2.0D;
        if (lvt_3_1_ < 0.0D) {
            lvt_3_1_ /= 28.0D;
        } else {
            if (lvt_3_1_ > 1.0D) {
                lvt_3_1_ = 1.0D;
            }

            lvt_3_1_ /= 40.0D;
        }

        return lvt_3_1_;
    }

    public int getGroundHeight() {
        return this.world.getSeaLevel() + 1;
    }

}
