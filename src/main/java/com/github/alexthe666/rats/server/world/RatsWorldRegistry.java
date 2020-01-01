package com.github.alexthe666.rats.server.world;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(RatsMod.MODID)
public class RatsWorldRegistry {

    public static final RatlantisSurfaceBuilder RATLANTIS_SURFACE = new RatlantisSurfaceBuilder(SurfaceBuilderConfig::deserialize);
    public static final ModDimension RATLANTIS_DIM = new RatlantisModDimension(RatlantisDimension::new).setRegistryName("rats:ratlantis");
    public static DimensionType RATLANTIS_DIMENSION_TYPE;
    public static Biome RATLANTIS_BIOME;

    static {
        RATLANTIS_SURFACE.setRegistryName("rats:ratlantis_surface");
    }

    public static void register() {

    }
}
