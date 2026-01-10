package com.github.alexthe666.rats.server.world;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.registry.worldgen.RatlantisConfiguredFeatureRegistry;
import net.minecraft.world.level.block.grower.TreeGrower;

import java.util.Optional;

public class PiratTreeGrower {
	public static final TreeGrower PIRAT_TREE_GROWER = new TreeGrower(
		RatsMod.MODID + ":pirat",
		0.1F, // mega tree chance
		Optional.of(RatlantisConfiguredFeatureRegistry.LARGE_GHOST_PIRAT_TREE),
		Optional.empty(),
		Optional.of(RatlantisConfiguredFeatureRegistry.GHOST_PIRAT_TREE),
		Optional.empty(),
		Optional.empty(),
		Optional.empty()
	);
}







