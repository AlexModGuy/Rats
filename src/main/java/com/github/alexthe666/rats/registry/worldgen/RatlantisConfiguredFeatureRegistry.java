package com.github.alexthe666.rats.registry.worldgen;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.registry.RatlantisBlockRegistry;
import com.github.alexthe666.rats.server.world.ThickBranchingTrunkPlacer;
import com.github.alexthe666.rats.server.world.RatlantisRuinConfiguration;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.CherryFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.Map;

public class RatlantisConfiguredFeatureRegistry {
	public static final ResourceKey<ConfiguredFeature<?, ?>> GHOST_PIRAT_TREE = registerKey("ghost_pirat_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> LARGE_GHOST_PIRAT_TREE = registerKey("large_ghost_pirat_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> RATGLOVE_FLOWERS = registerKey("ratglove_flowers");
	public static final ResourceKey<ConfiguredFeature<?, ?>> MARBLE_PILE = registerKey("marble_pile");
	public static final ResourceKey<ConfiguredFeature<?, ?>> SMALL_RUINS = registerKey("small_ruins");
	public static final ResourceKey<ConfiguredFeature<?, ?>> LARGE_RUINS = registerKey("large_ruins");
	public static final ResourceKey<ConfiguredFeature<?, ?>> CHEESE_ORE = registerKey("cheese_ore");
	public static final ResourceKey<ConfiguredFeature<?, ?>> GEM_ORE = registerKey("ratlantean_gem_ore");
	public static final ResourceKey<ConfiguredFeature<?, ?>> ORATCHALCUM_ORE = registerKey("oratchalcum_ore");

	public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
		return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(RatsMod.MODID, name));
	}

	public static final TreeConfiguration GHOST_PIRAT_TREE_CONFIG = new TreeConfiguration.TreeConfigurationBuilder(
			BlockStateProvider.simple(RatlantisBlockRegistry.PIRAT_LOG.get()),
			new ThickBranchingTrunkPlacer(5, 1, 1, 1, 1, 2),
			BlockStateProvider.simple(RatlantisBlockRegistry.PIRAT_LEAVES.get()),
			new CherryFoliagePlacer(ConstantInt.of(4), ConstantInt.of(0), ConstantInt.of(5), 0.25F, 0.5F, 0.5F, 0.75F),
			new TwoLayersFeatureSize(1, 0, 2)).ignoreVines().build();

	public static final TreeConfiguration LARGE_GHOST_PIRAT_TREE_CONFIG = new TreeConfiguration.TreeConfigurationBuilder(
			BlockStateProvider.simple(RatlantisBlockRegistry.PIRAT_LOG.get()),
			new ThickBranchingTrunkPlacer(8, 1, 1, 2, 2, 4),
			BlockStateProvider.simple(RatlantisBlockRegistry.PIRAT_LEAVES.get()),
			new CherryFoliagePlacer(ConstantInt.of(4), ConstantInt.of(0), ConstantInt.of(5), 0.25F, 0.5F, 0.5F, 0.75F),
			new TwoLayersFeatureSize(1, 0, 2)).ignoreVines().build();

	public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
		HolderGetter<StructureProcessorList> processors = context.lookup(Registries.PROCESSOR_LIST);
		context.register(GHOST_PIRAT_TREE, new ConfiguredFeature<>(Feature.TREE, GHOST_PIRAT_TREE_CONFIG));
		context.register(LARGE_GHOST_PIRAT_TREE, new ConfiguredFeature<>(Feature.TREE, LARGE_GHOST_PIRAT_TREE_CONFIG));
		context.register(RATGLOVE_FLOWERS, new ConfiguredFeature<>(Feature.FLOWER, new RandomPatchConfiguration(64, 7, 3, PlacementUtils.filtered(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(RatlantisBlockRegistry.RATGLOVE_FLOWER.get())), BlockPredicate.ONLY_IN_AIR_PREDICATE))));
		context.register(MARBLE_PILE, new ConfiguredFeature<>(RatlantisFeatureRegistry.MARBLE_PILE.get(), NoneFeatureConfiguration.INSTANCE));
		context.register(SMALL_RUINS, new ConfiguredFeature<>(RatlantisFeatureRegistry.RATLANTIS_RUIN.get(), new RatlantisRuinConfiguration(Map.of(ruinLocation("cheese_statuette"), 0.35F, ruinLocation("marble_giant_cheese"), 0.1F, ruinLocation("marble_hut"), 0.35F, ruinLocation("marble_pillar_collection"), 0.2F, ruinLocation("marble_pillar_leaning"), 0.25F, ruinLocation("marble_rat_head"), 0.15F, ruinLocation("marble_rat_lincoln"), 0.12F, ruinLocation("marble_small_aquaduct"), 0.3F, ruinLocation("marble_thin_tower"), 0.4F, ruinLocation("marble_tower"), 0.25F), ruinLocation("marble_pillar"), processors.getOrThrow(RatlantisStructureRegistry.RUIN_RUINS))));
		context.register(LARGE_RUINS, new ConfiguredFeature<>(RatlantisFeatureRegistry.RATLANTIS_RUIN.get(), new RatlantisRuinConfiguration(Map.of(ruinLocation("marble_forum"), 0.2F, ruinLocation("marble_palace"), 0.15F, ruinLocation("marble_rat_colossus"), 0.05F, ruinLocation("marble_rat_sphinx"), 0.1F, ruinLocation("marble_temple"), 0.2F), ruinLocation("marble_large_aquaduct"), processors.getOrThrow(RatlantisStructureRegistry.RUIN_RUINS))));
		context.register(CHEESE_ORE, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), RatlantisBlockRegistry.CHEESE_ORE.get().defaultBlockState(), 5)));
		context.register(GEM_ORE, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), RatlantisBlockRegistry.RATLANTEAN_GEM_ORE.get().defaultBlockState(), 4)));
		context.register(ORATCHALCUM_ORE, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), RatlantisBlockRegistry.ORATCHALCUM_ORE.get().defaultBlockState(), 3)));
	}

	private static ResourceLocation ruinLocation(String name) {
		return new ResourceLocation(RatsMod.MODID, name);
	}
}
