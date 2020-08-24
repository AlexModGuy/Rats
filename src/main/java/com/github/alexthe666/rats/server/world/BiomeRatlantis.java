package com.github.alexthe666.rats.server.world;

import com.github.alexthe666.rats.server.blocks.RatsBlockRegistry;
import com.github.alexthe666.rats.server.entity.RatsEntityRegistry;
import com.github.alexthe666.rats.server.world.structure.RatlantisStructureRegistry;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeAmbience;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.biome.MoodSoundAmbience;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.structure.MineshaftConfig;
import net.minecraft.world.gen.feature.structure.MineshaftStructure;
import net.minecraft.world.gen.feature.structure.ShipwreckConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.placement.*;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BiomeRatlantis {
    //extends
}/* Biome {

    public static final SurfaceBuilderConfig SURFACE_BUILDER_CONFIG = new SurfaceBuilderConfig(Blocks.GRASS.getDefaultState(), Blocks.DIRT.getDefaultState(), Blocks.SAND.getDefaultState());
    public static final BlockClusterFeatureConfig RATGLOVE_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(RatsBlockRegistry.RATGLOVE_FLOWER.getDefaultState()), new SimpleBlockPlacer())).tries(64).build();


    public BiomeRatlantis() {
        super((new Biome.Builder()).surfaceBuilder(RatsWorldRegistry.RATLANTIS_SURFACE, SURFACE_BUILDER_CONFIG).precipitation(Biome.RainType.RAIN).category(Category.OCEAN).scale(0.1F).temperature(0.55F).downfall(0.5F).depth(1).func_235097_a_((new BiomeAmbience.Builder()).func_235246_b_(4445678).func_235248_c_(270131).func_235239_a_(12638463).func_235243_a_(MoodSoundAmbience.field_235027_b_).func_235238_a_()).parent((String)null));
        this.func_235063_a_(DefaultBiomeFeatures.field_235176_n_);
        this.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, RatsBlockRegistry.CHEESE_ORE.getDefaultState(), 5)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(20, 0, 0, 64))));
        this.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, RatsBlockRegistry.RATLANTEAN_GEM_ORE.getDefaultState(), 4)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(2, 0, 0, 32))));
        this.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, RatsBlockRegistry.ORATCHALCUM_ORE.getDefaultState(), 4)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(3, 0, 0, 25))));
        this.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.SIMPLE_RANDOM_SELECTOR.withConfiguration(new SingleRandomFeature(ImmutableList.of(Feature.CORAL_TREE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG), Feature.CORAL_CLAW.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG), Feature.CORAL_MUSHROOM.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG)))).withPlacement(Placement.TOP_SOLID_HEIGHTMAP_NOISE_BIASED.configure(new TopSolidWithNoiseConfig(20, 400.0D, 0.0D, Heightmap.Type.OCEAN_FLOOR_WG))));
        DefaultBiomeFeatures.addTallSeagrassSparse(this);
        this.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.SEA_PICKLE.withConfiguration(new CountConfig(20)).withPlacement(Placement.CHANCE_TOP_SOLID_HEIGHTMAP.configure(new ChanceConfig(16))));
        this.func_235063_a_(RatsWorldRegistry.RAT_RUINS.func_236391_a_( new NoFeatureConfig()));
        this.func_235063_a_(RatsWorldRegistry.FLYING_DUTCHRAT.func_236391_a_( new NoFeatureConfig()));
        this.func_235063_a_(RatsWorldRegistry.RUNWAY.func_236391_a_( new NoFeatureConfig()));
        this.func_235063_a_(RatsWorldRegistry.RAT_RUINS.func_236391_a_(new NoFeatureConfig()));
        this.func_235063_a_( RatsWorldRegistry.FLYING_DUTCHRAT.func_236391_a_(new NoFeatureConfig()));
        this.func_235063_a_( RatsWorldRegistry.RUNWAY.func_236391_a_(new NoFeatureConfig()));
        this.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(RATGLOVE_CONFIG));
        this.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, RatlantisStructureRegistry.MARBLE_PILE.withConfiguration(new NoFeatureConfig()));
        this.flowers.add(Feature.RANDOM_PATCH.withConfiguration(RATGLOVE_CONFIG));
        DefaultBiomeFeatures.addCarvers(this);
        DefaultBiomeFeatures.addLakes(this);
        DefaultBiomeFeatures.addMonsterRooms(this);
        DefaultBiomeFeatures.addStoneVariants(this);
        DefaultBiomeFeatures.addOres(this);
        DefaultBiomeFeatures.addSedimentDisks(this);
        DefaultBiomeFeatures.addBamboo(this);
        DefaultBiomeFeatures.addExtraDefaultFlowers(this);
        DefaultBiomeFeatures.addJungleGrass(this);
        DefaultBiomeFeatures.addMushrooms(this);
        DefaultBiomeFeatures.addReedsAndPumpkins(this);
        DefaultBiomeFeatures.addSprings(this);
        DefaultBiomeFeatures.addJunglePlants(this);
        DefaultBiomeFeatures.addSprings(this);
        DefaultBiomeFeatures.addFreezeTopLayer(this);
        DefaultBiomeFeatures.addLakes(this);
        DefaultBiomeFeatures.addMonsterRooms(this);
        DefaultBiomeFeatures.addStoneVariants(this);
        DefaultBiomeFeatures.addOres(this);
        DefaultBiomeFeatures.addSedimentDisks(this);
        DefaultBiomeFeatures.addBamboo(this);
        DefaultBiomeFeatures.addJungleTreeForest(this);
        DefaultBiomeFeatures.addExtraDefaultFlowers(this);
        DefaultBiomeFeatures.addJungleGrass(this);
        DefaultBiomeFeatures.addMushrooms(this);
        DefaultBiomeFeatures.addReedsAndPumpkins(this);
        DefaultBiomeFeatures.addSprings(this);
        DefaultBiomeFeatures.addJunglePlants(this);
        DefaultBiomeFeatures.addFreezeTopLayer(this);
        this.addSpawn(EntityClassification.WATER_CREATURE, new Biome.SpawnListEntry(EntityType.PUFFERFISH, 15, 1, 3));
        this.addSpawn(EntityClassification.WATER_CREATURE, new Biome.SpawnListEntry(RatsEntityRegistry.RATFISH, 45, 8, 8));
        this.addSpawn(EntityClassification.WATER_CREATURE, new Biome.SpawnListEntry(EntityType.DOLPHIN, 2, 1, 2));
        this.addSpawn(EntityClassification.CREATURE, new Biome.SpawnListEntry(EntityType.PARROT, 60, 1, 2));
        this.addSpawn(EntityClassification.CREATURE, new Biome.SpawnListEntry(EntityType.PANDA, 12, 1, 2));
        this.addSpawn(EntityClassification.MONSTER, new Biome.SpawnListEntry(RatsEntityRegistry.FERAL_RATLANTEAN, 50, 1, 3));
        this.addSpawn(EntityClassification.MONSTER, new Biome.SpawnListEntry(RatsEntityRegistry.PIRAT, 20, 1, 1));
        this.addSpawn(EntityClassification.MONSTER, new Biome.SpawnListEntry(RatsEntityRegistry.RATLANTEAN_SPIRIT, 50, 1, 1));
        this.addSpawn(EntityClassification.MONSTER, new Biome.SpawnListEntry(RatsEntityRegistry.GHOST_PIRAT, 75, 1, 5));
        this.addSpawn(EntityClassification.MONSTER, new Biome.SpawnListEntry(RatsEntityRegistry.RATLANTEAN_RATBOT, 15, 1, 5));
        this.setRegistryName("rats:ratlantis_biome");
        //this.addFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION, Biome.createDecoratedFeature(RAT_RUINS, IFeatureConfig.NO_FEATURE_CONFIG, Placement.CHANCE_PASSTHROUGH, new ChanceConfig(64)));
        //this.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, Biome.createDecoratedFeature(AQUADUCT, IFeatureConfig.NO_FEATURE_CONFIG, Placement.CHANCE_PASSTHROUGH, new ChanceConfig(128)));
        //addFlower(RatsBlockRegistry.RATGLOVE_FLOWER.getDefaultState(), 30);
    }


    @OnlyIn(Dist.CLIENT)
    public int getSkyColor() {
        return 0XFFC62A;
    }

    @OnlyIn(Dist.CLIENT)
    public int getGrassColor(double posX, double posZ) {
        return 0X7CC900;
    }

    @OnlyIn(Dist.CLIENT)
    public int getFoliageColor() {
        return 0X26DD00;
    }
}*/
