package com.github.alexthe666.rats.server.world;

import com.github.alexthe666.rats.server.world.structure.DutchratShipStructure;
import com.github.alexthe666.rats.server.world.structure.RatlantisAquaductStructure;
import com.github.alexthe666.rats.server.world.structure.RatlantisRuinsStructure;
import com.github.alexthe666.rats.server.world.structure.RunwayStructure;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;


public class RatsWorldRegistry {

    public static final RatlantisSurfaceBuilder RATLANTIS_SURFACE = new RatlantisSurfaceBuilder(SurfaceBuilderConfig.field_237203_a_);
    //public static final ModDimension RATLANTIS_DIM = new RatlantisModDimension(RatlantisDimension::new).setRegistryName("rats:ratlantis");
    //public static DimensionType RATLANTIS_DIMENSION_TYPE = DimensionManager.registerOrGetDimension(new ResourceLocation("rats:ratlantis"), RATLANTIS_DIM, null, true);
    public static Biome RATLANTIS_BIOME;
    public static final Structure<NoFeatureConfig> RAT_RUINS = new RatlantisRuinsStructure(NoFeatureConfig.field_236558_a_);
    public static final Structure<NoFeatureConfig> FLYING_DUTCHRAT = new DutchratShipStructure(NoFeatureConfig.field_236558_a_);
    public static final Structure<NoFeatureConfig> RATLANTIS_AQUADUCTS = new RatlantisAquaductStructure(NoFeatureConfig.field_236558_a_);
    public static final Structure<NoFeatureConfig> RUNWAY = new RunwayStructure(NoFeatureConfig.field_236558_a_);

    static {
        RATLANTIS_SURFACE.setRegistryName("rats:ratlantis_surface");
    }

    public static void init() {
       /* for(Biome biome : ForgeRegistries.BIOMES.getValues()){
            if(!BiomeDictionary.hasType(biome, BiomeDictionary.Type.WATER)){
                biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, RatlantisStructureRegistry.SEWER.withConfiguration(new NoFeatureConfig()));
            }
        }*/

    }
}
