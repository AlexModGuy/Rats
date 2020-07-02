package com.github.alexthe666.rats.server.world;

import com.github.alexthe666.rats.server.world.structure.*;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.DimensionType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.FuzzedBiomeMagnifier;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

import java.util.Locale;
import java.util.OptionalLong;


public class RatsWorldRegistry {

    public static final RatlantisSurfaceBuilder RATLANTIS_SURFACE = new RatlantisSurfaceBuilder(SurfaceBuilderConfig.field_237203_a_);
    public static Biome RATLANTIS_BIOME;
    public static final Structure<NoFeatureConfig> RAT_RUINS = new RatlantisRuinsStructure(NoFeatureConfig.field_236558_a_);
    public static final Structure<NoFeatureConfig> FLYING_DUTCHRAT = new DutchratShipStructure(NoFeatureConfig.field_236558_a_);
    public static final Structure<NoFeatureConfig> RUNWAY = new RunwayStructure(NoFeatureConfig.field_236558_a_);

    public static <F extends Structure<?>> void putStructureOnAList(String nameForList, F structure) {
        Structure.field_236365_a_.put(nameForList.toLowerCase(Locale.ROOT), structure);
    }

    public static void init() {
    }
}
