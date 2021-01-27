package com.github.alexthe666.rats.server.world;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.world.structure.DutchratShipStructure;
import com.github.alexthe666.rats.server.world.structure.RatlantisRuinsStructure;
import com.github.alexthe666.rats.server.world.structure.RunwayStructure;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

import java.util.Locale;


public class RatsWorldRegistry {

    public static final RatlantisSurfaceBuilder RATLANTIS_SURFACE = new RatlantisSurfaceBuilder(SurfaceBuilderConfig.field_237203_a_);
    public static Biome RATLANTIS_BIOME;
    public static final RegistryKey<DimensionSettings> RATLANTIS_KEY = RegistryKey.getOrCreateKey(Registry.NOISE_SETTINGS_KEY, new ResourceLocation("rats:ratlantis"));
    public static final Structure<NoFeatureConfig> RAT_RUINS = new RatlantisRuinsStructure(NoFeatureConfig.field_236558_a_);
    public static final Structure<NoFeatureConfig> FLYING_DUTCHRAT = new DutchratShipStructure(NoFeatureConfig.field_236558_a_);
    public static final Structure<NoFeatureConfig> RUNWAY = new RunwayStructure(NoFeatureConfig.field_236558_a_);
    public static StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> RAT_RUINS_SF;
    public static StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> FLYING_DUTCHRAT_SF;
    public static StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> RUNWAY_SF;

    public static <F extends Structure<?>> void putStructureOnAList(String nameForList, F structure) {
        Structure.NAME_STRUCTURE_BIMAP.put(nameForList.toLowerCase(Locale.ROOT), structure);
    }

    private static <FC extends IFeatureConfig, F extends Structure<FC>> StructureFeature<FC, F> func_244162_a(String p_244162_0_, StructureFeature<FC, F> p_244162_1_) {
        return WorldGenRegistries.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE, p_244162_0_, p_244162_1_);
    }

    public static void init() {
        if(RatsMod.RATLANTIS_LOADED){
            RAT_RUINS_SF = func_244162_a("rats:ratlantis_ruins_structure", RAT_RUINS.withConfiguration(NoFeatureConfig.field_236559_b_));
            FLYING_DUTCHRAT_SF = func_244162_a("rats:dutchrat_ship", FLYING_DUTCHRAT.withConfiguration(NoFeatureConfig.field_236559_b_));
            RUNWAY_SF = func_244162_a("rats:runway", RUNWAY.withConfiguration(NoFeatureConfig.field_236559_b_));
        }
    }
}
