package com.github.alexthe666.rats.server.world;

import com.github.alexthe666.rats.server.blocks.RatsBlockRegistry;
import com.github.alexthe666.rats.server.entity.RatsEntityRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BiomeRatlantis extends Biome {

    public static final SurfaceBuilderConfig SURFACE_BUILDER_CONFIG = new SurfaceBuilderConfig(Blocks.GRASS.getDefaultState(), Blocks.DIRT.getDefaultState(), Blocks.SAND.getDefaultState());


    public BiomeRatlantis() {
        super((new Biome.Builder()).surfaceBuilder(RatsWorldRegistry.RATLANTIS_SURFACE, SURFACE_BUILDER_CONFIG).precipitation(Biome.RainType.RAIN).category(Category.OCEAN).scale(0.1F).temperature(0.55F).downfall(0.5F).waterColor(4445678).waterFogColor(270131).parent((String)null));
        this.setRegistryName("ratlantis");
        DefaultBiomeFeatures.addCarvers(this);
        DefaultBiomeFeatures.addStructures(this);
        DefaultBiomeFeatures.addLakes(this);
        DefaultBiomeFeatures.addMonsterRooms(this);
        DefaultBiomeFeatures.addStoneVariants(this);
        DefaultBiomeFeatures.addOres(this);
        DefaultBiomeFeatures.addSedimentDisks(this);
        DefaultBiomeFeatures.addBamboo(this);
        DefaultBiomeFeatures.func_222323_C(this);
        DefaultBiomeFeatures.addExtraDefaultFlowers(this);
        DefaultBiomeFeatures.addJungleGrass(this);
        DefaultBiomeFeatures.addMushrooms(this);
        DefaultBiomeFeatures.addReedsAndPumpkins(this);
        DefaultBiomeFeatures.addSprings(this);
        DefaultBiomeFeatures.addJunglePlants(this);
        DefaultBiomeFeatures.addFreezeTopLayer(this);
        this.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(Feature.ORE, new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, RatsBlockRegistry.BLOCK_OF_CHEESE.getDefaultState(), 9), Placement.COUNT_RANGE, new CountRangeConfig(6, 0, 0, 100)));
        this.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(Feature.ORE, new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, RatsBlockRegistry.MARBLED_CHEESE_RAW.getDefaultState(), 9), Placement.COUNT_RANGE, new CountRangeConfig(6, 0, 0, 100)));
        this.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(Feature.ORE, new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, Blocks.EMERALD_ORE.getDefaultState(), 3), Placement.COUNT_RANGE, new CountRangeConfig(1, 0, 0, 10)));
        this.addSpawn(EntityClassification.CREATURE, new Biome.SpawnListEntry(EntityType.PARROT, 60, 1, 2));
        this.addSpawn(EntityClassification.CREATURE, new Biome.SpawnListEntry(EntityType.PANDA, 12, 1, 2));
        this.addSpawn(EntityClassification.MONSTER, new Biome.SpawnListEntry(RatsEntityRegistry.FERAL_RATLANTEAN, 50, 1, 3));
        this.addSpawn(EntityClassification.MONSTER, new Biome.SpawnListEntry(RatsEntityRegistry.PIRAT, 20, 1, 1));
        this.addSpawn(EntityClassification.MONSTER, new Biome.SpawnListEntry(RatsEntityRegistry.RATLANTEAN_SPIRIT, 50, 1, 1));
        //addFlower(RatsBlockRegistry.RATGLOVE_FLOWER.getDefaultState(), 30);
    }

    @OnlyIn(Dist.CLIENT)
    public int getSkyColorByTemp(float currentTemperature) {
        return 0XFFC62A;
    }
}
