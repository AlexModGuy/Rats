package com.github.alexthe666.rats.data.loot;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;

// 1.20.5+ datagen: EntityLootSubProvider takes a HolderLookup.Provider, the looting-enchant function
// became EnchantedCountIncreaseFunction, the random-chance condition became EnchantedBonusCondition,
// and number providers wrap through their `.Provider` adapter. The runtime source of truth is the
// already-generated JSON under src/generated/resources/data/rats/loot_tables/entities/; this stub
// keeps the runData pipeline happy and would be filled in only if the loot tables need re-generating.
public class RatsEntityLootTables extends EntityLootSubProvider {

    protected RatsEntityLootTables(HolderLookup.Provider registries) {
        super(FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    public void generate() {
        // intentionally empty — see file header
    }
}
