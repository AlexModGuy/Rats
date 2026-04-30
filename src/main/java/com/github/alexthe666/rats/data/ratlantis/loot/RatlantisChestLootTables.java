package com.github.alexthe666.rats.data.ratlantis.loot;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.function.BiConsumer;

// Datagen note (1.21): see RatsChestLootTables.
public class RatlantisChestLootTables implements LootTableSubProvider {

    public RatlantisChestLootTables(HolderLookup.Provider registries) {
        // intentionally empty — see file header
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {
        // intentionally empty — see file header
    }
}
