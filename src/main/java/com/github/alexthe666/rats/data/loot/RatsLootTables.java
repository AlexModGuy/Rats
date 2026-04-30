package com.github.alexthe666.rats.data.loot;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

// Datagen note (1.21): 1.20.5+ changed LootTableProvider to take CompletableFuture<HolderLookup
// .Provider> and SubProviderEntry now wraps a Function<HolderLookup.Provider,
// LootTableSubProvider>. Sub-providers updated separately; this is the wiring shell.
public class RatsLootTables extends LootTableProvider {

    public RatsLootTables(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, Set.of(), List.of(
                new SubProviderEntry(RatsBlockLootTables::new, LootContextParamSets.BLOCK),
                new SubProviderEntry(RatsChestLootTables::new, LootContextParamSets.CHEST),
                new SubProviderEntry(RatsEntityLootTables::new, LootContextParamSets.ENTITY),
                new SubProviderEntry(RatsGameplayLootTables::new, LootContextParamSets.GIFT)
        ), registries);
    }
}
