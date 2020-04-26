package com.github.alexthe666.rats.server.world;

import com.github.alexthe666.rats.server.world.structure.DutchratShipStructure;
import com.github.alexthe666.rats.server.world.structure.RatlantisAquaductStructure;
import com.github.alexthe666.rats.server.world.structure.RatlantisRuinsStructure;
import com.github.alexthe666.rats.server.world.structure.RatlantisStructureRegistry;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.registries.ForgeRegistries;


public class RatsWorldRegistry {

    public static final RatlantisSurfaceBuilder RATLANTIS_SURFACE = new RatlantisSurfaceBuilder(SurfaceBuilderConfig::deserialize);
    public static final ModDimension RATLANTIS_DIM = new RatlantisModDimension(RatlantisDimension::new).setRegistryName("rats:ratlantis");
    public static DimensionType RATLANTIS_DIMENSION_TYPE = DimensionManager.registerOrGetDimension(new ResourceLocation("rats:ratlantis"), RATLANTIS_DIM, null, true);
    public static Biome RATLANTIS_BIOME;
    public static final Structure<NoFeatureConfig> RAT_RUINS = new RatlantisRuinsStructure(NoFeatureConfig::deserialize);
    public static final Structure<NoFeatureConfig> FLYING_DUTCHRAT = new DutchratShipStructure(NoFeatureConfig::deserialize);
    public static final Structure<NoFeatureConfig> RATLANTIS_AQUADUCTS = new RatlantisAquaductStructure(NoFeatureConfig::deserialize);

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
