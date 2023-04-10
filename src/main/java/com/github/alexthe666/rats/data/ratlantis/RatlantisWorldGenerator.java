package com.github.alexthe666.rats.data.ratlantis;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.registry.RatlantisTrimRegistry;
import com.github.alexthe666.rats.registry.worldgen.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;

public class RatlantisWorldGenerator extends DatapackBuiltinEntriesProvider {

	public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
			.add(Registries.BIOME, RatlantisBiomeRegistry::bootstrap)
			.add(Registries.CONFIGURED_FEATURE, RatlantisConfiguredFeatureRegistry::bootstrap)
			.add(Registries.CONFIGURED_CARVER, RatlantisDimensionRegistry::bootstrapCarver)
			.add(Registries.DIMENSION_TYPE, RatlantisDimensionRegistry::bootstrapType)
			.add(Registries.LEVEL_STEM, RatlantisDimensionRegistry::bootstrapLevelStem)
			.add(Registries.NOISE_SETTINGS, RatlantisDimensionRegistry::bootstrapNoise)
			.add(Registries.PLACED_FEATURE, RatlantisPlacedFeatureRegistry::bootstrap)
			.add(Registries.PROCESSOR_LIST, RatlantisStructureRegistry::bootstrapProcessors)
			.add(Registries.STRUCTURE, RatlantisStructureRegistry::bootstrapStructures)
			.add(Registries.STRUCTURE_SET, RatlantisStructureRegistry::bootstrapSets)
			.add(Registries.TEMPLATE_POOL, RatlantisStructureRegistry::bootstrapPools)
			.add(Registries.TRIM_MATERIAL, RatlantisTrimRegistry::bootstrap);

	public RatlantisWorldGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, provider, BUILDER, Collections.singleton(RatsMod.MODID));
	}
}
