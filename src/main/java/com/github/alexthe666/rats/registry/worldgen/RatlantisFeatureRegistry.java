package com.github.alexthe666.rats.registry.worldgen;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.world.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Supplier;

public class RatlantisFeatureRegistry {
	// 1.21: NeoForgeRegistries.WORLD_CARVERS / FEATURES → use vanilla BuiltInRegistries.CARVER / FEATURE.
	public static final DeferredRegister<WorldCarver<?>> CARVERS = DeferredRegister.create(net.minecraft.core.registries.BuiltInRegistries.CARVER, RatsMod.MODID);
	public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(net.minecraft.core.registries.BuiltInRegistries.FEATURE, RatsMod.MODID);
	public static final DeferredRegister<StructureProcessorType<?>> PROCESSORS = DeferredRegister.create(Registries.STRUCTURE_PROCESSOR, RatsMod.MODID);
	public static final DeferredRegister<TrunkPlacerType<?>> TRUNK_PLACERS = DeferredRegister.create(Registries.TRUNK_PLACER_TYPE, RatsMod.MODID);

	public static final DeferredHolder<WorldCarver<?>, WorldCarver<CaveCarverConfiguration>> RATLANTIS_CAVES = CARVERS.register("ratlantis_caves", () -> new RatsCaveCarver(CaveCarverConfiguration.CODEC));

	public static final DeferredHolder<Feature<?>, Feature<RatlantisRuinConfiguration>> RATLANTIS_RUIN = FEATURES.register("ratlantis_ruin", () -> new RatlantisRuinFeature(RatlantisRuinConfiguration.CODEC));
	public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> MARBLE_PILE = FEATURES.register("marble_pile", () -> new MarblePileFeature(NoneFeatureConfiguration.CODEC));

	public static final DeferredHolder<StructureProcessorType<?>, StructureProcessorType<CopyInputStateRuleProcessor>> COPY_STATE = registerProcessor("copy_state", () -> () -> CopyInputStateRuleProcessor.CODEC);
	public static final DeferredHolder<TrunkPlacerType<?>, TrunkPlacerType<ThickBranchingTrunkPlacer>> THICK_BASE_BRANCHING_TRUNK_PLACER = TRUNK_PLACERS.register("thick_branching_trunk_placer", () -> new TrunkPlacerType<ThickBranchingTrunkPlacer>(ThickBranchingTrunkPlacer.CODEC));

	private static <P extends StructureProcessor> DeferredHolder<StructureProcessorType<?>, StructureProcessorType<P>> registerProcessor(String name, Supplier<StructureProcessorType<P>> factory) {
		return PROCESSORS.register(name, factory);
	}
}
