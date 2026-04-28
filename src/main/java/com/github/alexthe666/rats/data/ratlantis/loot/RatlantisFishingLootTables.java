package com.github.alexthe666.rats.data.ratlantis.loot;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.function.BiConsumer;

// PORT-STUB: see RatsChestLootTables.
public class RatlantisFishingLootTables implements LootTableSubProvider {

    public RatlantisFishingLootTables(HolderLookup.Provider registries) {
        // intentionally empty — see file header
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {
        // intentionally empty — see file header
    }
}
