package com.github.alexthe666.rats.server.world;

import com.github.alexthe666.rats.server.blocks.RatsBlockRegistry;
import com.github.alexthe666.rats.server.entity.RatsEntityRegistry;
import com.github.alexthe666.rats.server.world.structure.RatlantisStructureRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.structure.MineshaftConfig;
import net.minecraft.world.gen.feature.structure.MineshaftStructure;
import net.minecraft.world.gen.feature.structure.ShipwreckConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.placement.*;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BiomeRatlantis extends Biome {

    public static final SurfaceBuilderConfig SURFACE_BUILDER_CONFIG = new SurfaceBuilderConfig(Blocks.GRASS.getDefaultState(), Blocks.DIRT.getDefaultState(), Blocks.SAND.getDefaultState());

    public BiomeRatlantis() {
        super((new Biome.Builder()).surfaceBuilder(RatsWorldRegistry.RATLANTIS_SURFACE, SURFACE_BUILDER_CONFIG).precipitation(Biome.RainType.RAIN).category(Category.OCEAN).scale(0.1F).temperature(0.55F).downfall(0.5F).waterColor(4445678).depth(1).waterFogColor(270131).parent((String)null));
        this.addStructure(Feature.MINESHAFT.withConfiguration(new MineshaftConfig(0.004D, MineshaftStructure.Type.NORMAL)));
        this.addStructure(Feature.SHIPWRECK.withConfiguration( new ShipwreckConfig(false)));
        this.addStructure(Feature.SHIPWRECK.withConfiguration(new ShipwreckConfig(false)));
        this.addStructure(RatsWorldRegistry.RAT_RUINS.withConfiguration( new NoFeatureConfig()));
        this.addStructure(RatsWorldRegistry.FLYING_DUTCHRAT.withConfiguration( new NoFeatureConfig()));
        this.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, RatsWorldRegistry.RAT_RUINS.withConfiguration(new NoFeatureConfig()));
        this.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, RatsWorldRegistry.FLYING_DUTCHRAT.withConfiguration(new NoFeatureConfig()));
        this.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, RatlantisStructureRegistry.MARBLE_PILE.withConfiguration(new NoFeatureConfig()));
        //this.addStructure(RatsWorldRegistry.RATLANTIS_AQUADUCTS, new NoFeatureConfig());
        //this.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, Biome.createDecoratedFeature(RatsWorldRegistry.RATLANTIS_AQUADUCTS, new NoFeatureConfig(), Placement.NOPE, IPlacementConfig.NO_PLACEMENT_CONFIG));
        DefaultBiomeFeatures.addCarvers(this);
        DefaultBiomeFeatures.addStructures(this);
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
        DefaultBiomeFeatures.func_222309_aj(this);
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
        this.setRegistryName("ratlantis_biome");
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
}
