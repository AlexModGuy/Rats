package com.github.alexthe666.rats.server.world;

import com.github.alexthe666.rats.server.blocks.RatsBlockRegistry;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.provider.SingleBiomeProvider;
import net.minecraft.world.biome.provider.SingleBiomeProviderSettings;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.ChunkGeneratorType;
import net.minecraft.world.gen.OverworldGenSettings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ForgeHooksClient;

import javax.annotation.Nullable;
import java.util.Random;

public class RatlantisDimension extends Dimension {
    public static final BlockPos SPAWN = new BlockPos(0, 110, 0);

    public RatlantisDimension(final World worldIn, final DimensionType dimension) {
        super(worldIn, dimension);
    }

    @Override
    public ChunkGenerator<?> createChunkGenerator() {
        OverworldGenSettings settings = ChunkGeneratorType.SURFACE.createSettings();
        settings.setDefaultBlock(RatsBlockRegistry.MARBLED_CHEESE.getDefaultState());
        settings.setDefaultBlock(RatsBlockRegistry.MARBLED_CHEESE.getDefaultState());
        SingleBiomeProviderSettings providerSettings = new SingleBiomeProviderSettings();
        providerSettings.setBiome(RatsWorldRegistry.RATLANTIS_BIOME);
        return new ChunkProviderRatlantis(this.world, new SingleBiomeProvider(providerSettings), settings);
     }

    @Override
    public float calculateCelestialAngle(long worldTime, float partialTicks) {
        double d0 = MathHelper.frac((double)worldTime / 24000.0D - 0.25D);
        double d1 = 0.5D - Math.cos(d0 * Math.PI) / 2.0D;
        return (float)(d0 * 2.0D + d1) / 3.0F;
    }

    @Override @Nullable
    @OnlyIn(Dist.CLIENT)
    public float[] calcSunriseSunsetColors(final float p_76560_1_, final float p_76560_2_) {
        return null;
    }

    @Override @OnlyIn(Dist.CLIENT)
    public Vec3d getFogColor(final float p_76562_1_, final float p_76562_2_) {
        float f = MathHelper.clamp(MathHelper.cos(p_76562_1_ * ((float) Math.PI * 2F)) * 2.0F + 0.5F, 0, 1F);
        float bright = 1.15F * f;
        float f1 = 1F;
        float f2 = 0.98F;
        float f3 = 0.79F;
        return new Vec3d((double) f1 * bright, (double) f2 * bright, (double) f3 * bright);
    }

    @Override @OnlyIn(Dist.CLIENT)
    public Vec3d getSkyColor(final BlockPos pos, final float flo) {
        final float f = this.world.getCelestialAngle(flo);
        float f2 = MathHelper.cos(f * 6.2831855f) * 2.0f + 0.5f;
        f2 = MathHelper.clamp(f2, 0.0f, 1.0f);
        final int i = ForgeHooksClient.getSkyBlendColour(this.world, pos);
        float f3 = (i >> 16 & 0xFF) / 255.0f;
        float f4 = (i >> 8 & 0xFF) / 255.0f;
        float f5 = (i & 0xFF) / 255.0f;
        f3 *= f2;
        f4 *= f2;
        f5 *= f2;
        return new Vec3d((double) f3, (double) f4, (double) f5);
    }

    @Override
    public boolean hasSkyLight() {
        return true;
    }

    @Override @OnlyIn(Dist.CLIENT)
    public boolean isSkyColored() {
        return true;
    }

    @Override
    public boolean canRespawnHere() {
        return true;
    }

    @Override
    public boolean isSurfaceWorld() {
        return true;
    }

    @Override @OnlyIn(Dist.CLIENT)
    public float getCloudHeight() {
        return 128.0f;
    }

    @Override @Nullable
    public BlockPos findSpawn(final ChunkPos p_206920_1_, final boolean p_206920_2_) {
        final Random random = new Random(this.world.getSeed());
        final BlockPos blockpos = new BlockPos(p_206920_1_.getXStart() + random.nextInt(15), 0,
                p_206920_1_.getZEnd() + random.nextInt(15));
        return this.world.getGroundAboveSeaLevel(blockpos).getMaterial().blocksMovement() ? blockpos : null;
    }

    @Override
    public BlockPos getSpawnCoordinate() {
        return SPAWN;
    }

    @Override @Nullable
    public BlockPos findSpawn(final int p_206921_1_, final int p_206921_2_, final boolean p_206921_3_) {
        return this.findSpawn(new ChunkPos(p_206921_1_ >> 4, p_206921_2_ >> 4), p_206921_3_);
    }

    @Override @OnlyIn(Dist.CLIENT)
    public boolean doesXZShowFog(final int p_76568_1_, final int p_76568_2_) {
        return false;
    }

    @Override
    public void onWorldSave() {
        final CompoundNBT compoundnbt = new CompoundNBT();
        this.world.getWorldInfo().setDimensionData(this.world.getDimension().getType(), compoundnbt);
    }
}