package com.github.alexthe666.rats.data.loot;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class RatsLootTables extends LootTableProvider {

	public RatsLootTables(PackOutput output) {
		super(output, Set.of(), List.of(
				new SubProviderEntry(RatsBlockLootTables::new, LootContextParamSets.BLOCK),
				new SubProviderEntry(RatsChestLootTables::new, LootContextParamSets.CHEST),
				new SubProviderEntry(RatsEntityLootTables::new, LootContextParamSets.ENTITY),
				new SubProviderEntry(RatsGameplayLootTables::new, LootContextParamSets.GIFT)
		));
	}

	@Override
	protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationtracker) {
	}
}
