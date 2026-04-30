package com.github.alexthe666.rats.data.loot;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

import java.util.Collections;
import java.util.Set;

// Datagen note (1.21): see RatsEntityLootTables.
public class RatsBlockLootTables extends BlockLootSubProvider {

    protected RatsBlockLootTables(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        // intentionally empty — see file header
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return Collections.emptyList();
    }
}
