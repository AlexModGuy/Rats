package com.github.alexthe666.rats.data.ratlantis.loot;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

// Datagen note (1.21): see RatsLootTables.
public class RatlantisLootTables extends LootTableProvider {

    public RatlantisLootTables(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, Set.of(), List.of(
                new SubProviderEntry(RatlantisBlockLootTables::new, LootContextParamSets.BLOCK),
                new SubProviderEntry(RatlantisChestLootTables::new, LootContextParamSets.CHEST),
                new SubProviderEntry(RatlantisEntityLootTables::new, LootContextParamSets.ENTITY),
                new SubProviderEntry(RatlantisFishingLootTables::new, LootContextParamSets.FISHING)
        ), registries);
    }
}
