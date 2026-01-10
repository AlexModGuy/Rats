package com.github.alexthe666.rats.data.loot;

import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.registry.RatsLootRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class RatsLootTables extends LootTableProvider {

	public RatsLootTables(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, Set.of(), List.of(
				new SubProviderEntry(RatsBlockLootTables::new, LootContextParamSets.BLOCK),
				new SubProviderEntry(RatsChestLootTables::new, LootContextParamSets.CHEST),
				new SubProviderEntry(RatsEntityLootTables::new, LootContextParamSets.ENTITY),
				new SubProviderEntry(RatsGameplayLootTables::new, LootContextParamSets.GIFT),
				new SubProviderEntry(RatsSpecialLootTables::new, LootContextParamSets.EMPTY)
		), registries);
	}

	@Override
	protected void validate(WritableRegistry<LootTable> writableRegistry, ValidationContext validationContext, ProblemReporter.Collector problemReporter) {
	}

	private static class RatsSpecialLootTables implements LootTableSubProvider {

		private final HolderLookup.Provider registries;

		public RatsSpecialLootTables(HolderLookup.Provider provider) {
			this.registries = provider;
		}

		@Override
		public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> builder) {
			builder.accept(RatsLootRegistry.RATLANTIS_RAT_EXCLUSIVE_DROPS_KEY, LootTable.lootTable()
					.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
							.add(LootItem.lootTableItem(Items.DIAMOND).setWeight(5))
							.add(LootItem.lootTableItem(RatsItemRegistry.CHEESE.get()).setWeight(30).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
							.add(LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(15))
							.add(LootItem.lootTableItem(Items.PRISMARINE_CRYSTALS).setWeight(15))
							.add(LootItem.lootTableItem(Items.BOOK).setWeight(15))
							.add(EmptyLootItem.emptyItem().setWeight(100))));
		}
	}
}







