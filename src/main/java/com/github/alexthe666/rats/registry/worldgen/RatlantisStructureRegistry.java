package com.github.alexthe666.rats.registry.worldgen;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.data.ratlantis.tags.RatlantisBiomeTags;
import com.github.alexthe666.rats.registry.RatlantisBlockRegistry;
import com.github.alexthe666.rats.registry.RatlantisEntityRegistry;
import com.github.alexthe666.rats.server.world.CopyInputStateRuleProcessor;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
import net.minecraft.world.level.levelgen.structure.templatesystem.AlwaysTrueTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.ProcessorRule;
import net.minecraft.world.level.levelgen.structure.templatesystem.RandomBlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

import java.util.List;
import java.util.Map;

public class RatlantisStructureRegistry {

	public static final ResourceKey<StructureTemplatePool> BARON_RUNWAY_START = registerPoolKey("baron_runway");
	public static final ResourceKey<StructureTemplatePool> DUTCHRAT_SHIP_START = registerPoolKey("dutchrat_ship");

	public static final ResourceKey<Structure> BARON_RUNWAY = registerStructureKey("baron_runway");
	public static final ResourceKey<Structure> DUTCHRAT_SHIP = registerStructureKey("dutchrat_ship");

	public static final ResourceKey<StructureSet> BARON_RUNWAY_SET = registerSetKey("baron_runway");
	public static final ResourceKey<StructureSet> DUTCHRAT_SHIP_SET = registerSetKey("dutchrat_ship");

	public static final ResourceKey<StructureProcessorList> RUIN_RUINS = registerProcessorKey("ruin_ruins");

	public static void bootstrapStructures(BootstapContext<Structure> context) {
		HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
		HolderGetter<StructureTemplatePool> pools = context.lookup(Registries.TEMPLATE_POOL);

		context.register(BARON_RUNWAY, new JigsawStructure(new Structure.StructureSettings(biomes.getOrThrow(RatlantisBiomeTags.BARON_RUNWAY_SPAWNS), Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.BEARD_THIN), pools.getOrThrow(BARON_RUNWAY_START), 1, ConstantHeight.of(VerticalAnchor.absolute(0)), false, Heightmap.Types.OCEAN_FLOOR_WG));
		context.register(DUTCHRAT_SHIP, new JigsawStructure(new Structure.StructureSettings(biomes.getOrThrow(RatlantisBiomeTags.DUTCHRAT_SHIP_SPAWNS), Map.of(MobCategory.MONSTER, new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, WeightedRandomList.create(new MobSpawnSettings.SpawnerData(RatlantisEntityRegistry.GHOST_PIRAT.get(), 1, 1, 1)))), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE), pools.getOrThrow(DUTCHRAT_SHIP_START), 1, ConstantHeight.of(VerticalAnchor.absolute(50)), false, Heightmap.Types.WORLD_SURFACE_WG));
	}

	public static void bootstrapPools(BootstapContext<StructureTemplatePool> context) {
		Holder<StructureTemplatePool> emptyPool = context.lookup(Registries.TEMPLATE_POOL).getOrThrow(Pools.EMPTY);

		context.register(BARON_RUNWAY_START, new StructureTemplatePool(emptyPool, ImmutableList.of(Pair.of(StructurePoolElement.single(new ResourceLocation(RatsMod.MODID, "baron_runway").toString()), 1)), StructureTemplatePool.Projection.TERRAIN_MATCHING));
		context.register(DUTCHRAT_SHIP_START, new StructureTemplatePool(emptyPool, ImmutableList.of(Pair.of(StructurePoolElement.single(new ResourceLocation(RatsMod.MODID, "dutchrat_ship").toString()), 1)), StructureTemplatePool.Projection.RIGID));
	}

	public static void bootstrapSets(BootstapContext<StructureSet> context) {
		HolderGetter<Structure> structures = context.lookup(Registries.STRUCTURE);
		context.register(BARON_RUNWAY_SET, new StructureSet(structures.getOrThrow(BARON_RUNWAY), new RandomSpreadStructurePlacement(100, 70, RandomSpreadType.LINEAR, 8008135)));
		context.register(DUTCHRAT_SHIP_SET, new StructureSet(structures.getOrThrow(DUTCHRAT_SHIP), new RandomSpreadStructurePlacement(75, 50, RandomSpreadType.LINEAR, 7177135)));
	}

	public static void bootstrapProcessors(BootstapContext<StructureProcessorList> context) {
		context.register(RUIN_RUINS, new StructureProcessorList(List.of(
				new CopyInputStateRuleProcessor(List.of(
						new ProcessorRule(
								new RandomBlockMatchTest(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK.get(), 0.3F),
								AlwaysTrueTest.INSTANCE,
								RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_CRACKED.get().defaultBlockState()),
						new ProcessorRule(
								new RandomBlockMatchTest(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK.get(), 0.35F),
								AlwaysTrueTest.INSTANCE,
								RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_MOSSY.get().defaultBlockState()),
						new ProcessorRule(
								new RandomBlockMatchTest(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_SLAB.get(), 0.35F),
								AlwaysTrueTest.INSTANCE,
								RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_CRACKED_SLAB.get().defaultBlockState()),
						new ProcessorRule(
								new RandomBlockMatchTest(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_SLAB.get(), 0.3F),
								AlwaysTrueTest.INSTANCE,
								RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_MOSSY_SLAB.get().defaultBlockState()),
						new ProcessorRule(
								new RandomBlockMatchTest(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_STAIRS.get(), 0.35F),
								AlwaysTrueTest.INSTANCE,
								RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_CRACKED_STAIRS.get().defaultBlockState()),
						new ProcessorRule(
								new RandomBlockMatchTest(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_STAIRS.get(), 0.3F),
								AlwaysTrueTest.INSTANCE,
								RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_MOSSY_STAIRS.get().defaultBlockState()),
						new ProcessorRule(
								new RandomBlockMatchTest(Blocks.GRASS, 0.2F),
								AlwaysTrueTest.INSTANCE,
								RatlantisBlockRegistry.MARBLED_CHEESE_GRASS.get().defaultBlockState()),
						new ProcessorRule(
								new RandomBlockMatchTest(Blocks.GRASS, 0.15F),
								AlwaysTrueTest.INSTANCE,
								RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_MOSSY.get().defaultBlockState()),
						new ProcessorRule(
								new RandomBlockMatchTest(Blocks.GRASS, 0.05F),
								AlwaysTrueTest.INSTANCE,
								RatlantisBlockRegistry.MARBLED_CHEESE_BRICK.get().defaultBlockState()),
						new ProcessorRule(
								new RandomBlockMatchTest(Blocks.GRASS, 0.05F),
								AlwaysTrueTest.INSTANCE,
								RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_CRACKED.get().defaultBlockState()),
						new ProcessorRule(
								new RandomBlockMatchTest(Blocks.GRASS, 0.01F),
								AlwaysTrueTest.INSTANCE,
								RatlantisBlockRegistry.MARBLED_CHEESE_TILE.get().defaultBlockState()),
						new ProcessorRule(
								new RandomBlockMatchTest(Blocks.DIRT, 0.1F),
								AlwaysTrueTest.INSTANCE,
								RatlantisBlockRegistry.MARBLED_CHEESE_DIRT.get().defaultBlockState())
				)))));
	}

	private static ResourceKey<Structure> registerStructureKey(String name) {
		return ResourceKey.create(Registries.STRUCTURE, new ResourceLocation(RatsMod.MODID, name));
	}

	private static ResourceKey<StructureTemplatePool> registerPoolKey(String name) {
		return ResourceKey.create(Registries.TEMPLATE_POOL, new ResourceLocation(RatsMod.MODID, name));
	}

	private static ResourceKey<StructureSet> registerSetKey(String name) {
		return ResourceKey.create(Registries.STRUCTURE_SET, new ResourceLocation(RatsMod.MODID, name));
	}

	private static ResourceKey<StructureProcessorList> registerProcessorKey(String name) {
		return ResourceKey.create(Registries.PROCESSOR_LIST, new ResourceLocation(RatsMod.MODID, name));
	}
}
