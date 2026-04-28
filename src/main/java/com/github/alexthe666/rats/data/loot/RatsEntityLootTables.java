package com.github.alexthe666.rats.data.loot;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;

// PORT-STUB: 1.20.5+ added HolderLookup.Provider to EntityLootSubProvider's
// ctor and reworked LootingEnchantFunction -> EnchantedCountIncreaseFunction
// + LootItemRandomChanceWithLootingCondition -> ...EnchantedBonusCondition,
// and NumberProvider/UniformGenerator now wraps via .Provider. Pre-generated
// JSON in src/generated/resources/data/rats/loot_tables/entities/ is the
// runtime source of truth; this generator is a no-op until the next data pass.
public class RatsEntityLootTables extends EntityLootSubProvider {

    protected RatsEntityLootTables(HolderLookup.Provider registries) {
        super(FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    public void generate() {
        // intentionally empty — see file header
    }
}
