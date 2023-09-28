package com.github.alexthe666.rats.server.world;

import com.github.alexthe666.rats.registry.worldgen.RatlantisConfiguredFeatureRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractMegaTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;

public class PiratTreeGrower extends AbstractMegaTreeGrower {
	@Nullable
	@Override
	protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean bees) {
		return RatlantisConfiguredFeatureRegistry.GHOST_PIRAT_TREE;
	}

	@Nullable
	@Override
	protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredMegaFeature(RandomSource random) {
		return RatlantisConfiguredFeatureRegistry.LARGE_GHOST_PIRAT_TREE;
	}
}
