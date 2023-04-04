package com.github.alexthe666.rats.registry.worldgen;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class RatlantisPlacedFeatureRegistry {

	public static final ResourceKey<PlacedFeature> RATGLOVE_FLOWERS = registerKey("ratglove_flowers");
	public static final ResourceKey<PlacedFeature> MARBLE_PILE = registerKey("marble_pile");
	public static final ResourceKey<PlacedFeature> SMALL_RUINS = registerKey("small_ruins");
	public static final ResourceKey<PlacedFeature> LARGE_RUINS = registerKey("large_ruins");
	public static final ResourceKey<PlacedFeature> CHEESE_ORE = registerKey("cheese_ore");
	public static final ResourceKey<PlacedFeature> GEM_ORE = registerKey("ratlantean_gem_ore");
	public static final ResourceKey<PlacedFeature> ORATCHALCUM_ORE = registerKey("oratchalcum_ore");

	public static ResourceKey<PlacedFeature> registerKey(String name) {
		return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(RatsMod.MODID, name));
	}

	public static void bootstrap(BootstapContext<PlacedFeature> context) {
		HolderGetter<ConfiguredFeature<?, ?>> features = context.lookup(Registries.CONFIGURED_FEATURE);
		context.register(RATGLOVE_FLOWERS, new PlacedFeature(features.getOrThrow(RatlantisConfiguredFeatureRegistry.RATGLOVE_FLOWERS), List.of(RarityFilter.onAverageOnceEvery(20), CountPlacement.of(5), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome())));
		context.register(MARBLE_PILE, new PlacedFeature(features.getOrThrow(RatlantisConfiguredFeatureRegistry.MARBLE_PILE), List.of(RarityFilter.onAverageOnceEvery(25), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, BiomeFilter.biome())));
		context.register(SMALL_RUINS, new PlacedFeature(features.getOrThrow(RatlantisConfiguredFeatureRegistry.SMALL_RUINS), List.of(RarityFilter.onAverageOnceEvery(20), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, BiomeFilter.biome())));
		context.register(LARGE_RUINS, new PlacedFeature(features.getOrThrow(RatlantisConfiguredFeatureRegistry.LARGE_RUINS), List.of(RarityFilter.onAverageOnceEvery(50), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, BiomeFilter.biome())));
		context.register(CHEESE_ORE, new PlacedFeature(features.getOrThrow(RatlantisConfiguredFeatureRegistry.CHEESE_ORE), List.of(HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(64)), CountPlacement.of(20), InSquarePlacement.spread())));
		context.register(GEM_ORE, new PlacedFeature(features.getOrThrow(RatlantisConfiguredFeatureRegistry.GEM_ORE), List.of(HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(32)), CountPlacement.of(2), InSquarePlacement.spread())));
		context.register(ORATCHALCUM_ORE, new PlacedFeature(features.getOrThrow(RatlantisConfiguredFeatureRegistry.ORATCHALCUM_ORE), List.of(HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(25)), CountPlacement.of(2), InSquarePlacement.spread())));
	}
}
