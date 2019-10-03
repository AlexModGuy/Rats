package com.github.alexthe666.rats.server.world;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WorldProviderRatlantis extends WorldProvider {

    @Override
    public void init() {
        this.hasSkyLight = true;
        this.biomeProvider = new BiomeProviderSingle(RatsWorldRegistry.RATLANTIS_BIOME);
        this.nether = false;
    }

    @Override
    public DimensionType getDimensionType() {
        return RatsWorldRegistry.RATLANTIS_DIM;
    }

    @SideOnly(Side.CLIENT)
    public Vec3d getFogColor(float p_76562_1_, float p_76562_2_) {
        float f = MathHelper.clamp(MathHelper.cos(p_76562_1_ * ((float) Math.PI * 2F)) * 2.0F + 0.5F, 0, 1F);
        float bright = 1.15F * f;
        float f1 = 1F;
        float f2 = 0.98F;
        float f3 = 0.79F;
        return new Vec3d((double) f1 * bright, (double) f2 * bright, (double) f3 * bright);
    }

    @Override
    public IChunkGenerator createChunkGenerator() {
        return new ChunkProviderRatlantis(this.world, this.world.getSeed());
    }

    @Override
    public float calculateCelestialAngle(long worldTime, float partialTicks) {
        return super.calculateCelestialAngle(worldTime, partialTicks);
    }

    @Override
    public boolean canRespawnHere() {
        return true;
    }

    @Override
    public boolean isSurfaceWorld() {
        return true;
    }
}
