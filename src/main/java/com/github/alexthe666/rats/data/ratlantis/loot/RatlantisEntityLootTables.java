package com.github.alexthe666.rats.data.ratlantis.loot;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;

// Datagen note (1.21): see RatsEntityLootTables.
public class RatlantisEntityLootTables extends EntityLootSubProvider {

    protected RatlantisEntityLootTables(HolderLookup.Provider registries) {
        super(FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    public void generate() {
        // intentionally empty — see file header
    }
}
