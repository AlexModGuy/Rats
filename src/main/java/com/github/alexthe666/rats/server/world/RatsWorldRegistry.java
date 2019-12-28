package com.github.alexthe666.rats.server.world;

import com.github.alexthe666.rats.RatConfig;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.surfacebuilders.DefaultSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ModDimension;

public class RatsWorldRegistry {

    public static final SurfaceBuilder<SurfaceBuilderConfig> RATLANTIS_SURFACE = new DefaultSurfaceBuilder(SurfaceBuilderConfig::deserialize);
    public static ModDimension RATLANTIS_DIM = new RatlantisModDimension(RatlantisDimension::new).setRegistryName("rats:ratlantis");
    public static DimensionType RATLANTIS_DIMENSION_TYPE;
    public static Biome RATLANTIS_BIOME = new BiomeRatlantis();

    static {
        RATLANTIS_SURFACE.setRegistryName("rats:ratlantis_surface");
    }

    public static void register() {
        if (!RatConfig.disableRatlantis) {
            RATLANTIS_DIMENSION_TYPE = DimensionManager.registerDimension(new ResourceLocation("rats:ratlantis"), RATLANTIS_DIM, null, true);
        }
        StructureRatRoadPieces.registerVillagePieces();
    }
}
