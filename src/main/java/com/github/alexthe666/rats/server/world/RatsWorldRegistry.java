package com.github.alexthe666.rats.server.world;

import com.github.alexthe666.rats.server.world.structure.DutchratShipStructure;
import com.github.alexthe666.rats.server.world.structure.RatlantisAquaductStructure;
import com.github.alexthe666.rats.server.world.structure.RatlantisRuinsStructure;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.minecraftforge.common.ModDimension;


public class RatsWorldRegistry {

    public static final RatlantisSurfaceBuilder RATLANTIS_SURFACE = new RatlantisSurfaceBuilder(SurfaceBuilderConfig::deserialize);
    public static final ModDimension RATLANTIS_DIM = new RatlantisModDimension(RatlantisDimension::new).setRegistryName("rats:ratlantis");
    public static DimensionType RATLANTIS_DIMENSION_TYPE;
    public static Biome RATLANTIS_BIOME;
    public static final Structure<NoFeatureConfig> RAT_RUINS = new RatlantisRuinsStructure(NoFeatureConfig::deserialize);
    public static final Structure<NoFeatureConfig> FLYING_DUTCHRAT = new DutchratShipStructure(NoFeatureConfig::deserialize);
    public static final Structure<NoFeatureConfig> RATLANTIS_AQUADUCTS = new RatlantisAquaductStructure(NoFeatureConfig::deserialize);

    static {
        RATLANTIS_SURFACE.setRegistryName("rats:ratlantis_surface");
    }
}
