package com.github.alexthe666.rats.data.ratlantis.loot;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class RatlantisLootTables extends LootTableProvider {

	public RatlantisLootTables(PackOutput output) {
		super(output, Set.of(), List.of(
				new SubProviderEntry(RatlantisBlockLootTables::new, LootContextParamSets.BLOCK),
				new SubProviderEntry(RatlantisChestLootTables::new, LootContextParamSets.CHEST),
				new SubProviderEntry(RatlantisEntityLootTables::new, LootContextParamSets.ENTITY),
				new SubProviderEntry(RatlantisFishingLootTables::new, LootContextParamSets.FISHING)
		));
	}

	@Override
	protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationtracker) {
	}
}
