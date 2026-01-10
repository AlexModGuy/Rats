package com.github.alexthe666.rats.data;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.registry.RatsBannerPatternRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class BannerPatternGenerator extends DatapackBuiltinEntriesProvider {

	private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
			.add(Registries.BANNER_PATTERN, RatsBannerPatternRegistry::bootstrap);

	public BannerPatternGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, provider, BUILDER, Set.of(RatsMod.MODID));
	}

	@Override
	public String getName() {
		return "Rats Banner Pattern Registries";
	}
}
