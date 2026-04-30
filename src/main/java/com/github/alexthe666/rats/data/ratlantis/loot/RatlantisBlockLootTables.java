package com.github.alexthe666.rats.data.ratlantis.loot;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;

import java.util.Set;

// Datagen note (1.21): same situation as RatsEntityLootTables.
public class RatlantisBlockLootTables extends BlockLootSubProvider {

    protected RatlantisBlockLootTables(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        // intentionally empty — see file header
    }

    @Override
    protected Iterable<net.minecraft.world.level.block.Block> getKnownBlocks() {
        return java.util.Collections.emptyList();
    }
}
