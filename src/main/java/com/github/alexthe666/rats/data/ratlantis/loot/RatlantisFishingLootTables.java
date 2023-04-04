package com.github.alexthe666.rats.data.ratlantis.loot;

import com.github.alexthe666.rats.registry.RatlantisItemRegistry;
import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.registry.RatsLootRegistry;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.FishingHookPredicate;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.functions.EnchantWithLevelsFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.function.BiConsumer;

public class RatlantisFishingLootTables implements LootTableSubProvider {
	@Override
	public void generate(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
		consumer.accept(RatsLootRegistry.RATLANTIS_FISH, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootTableReference.lootTableReference(RatsLootRegistry.RATLANTIS_FISHING_JUNK)
								.setWeight(10)
								.setQuality(-2))
						.add(LootTableReference.lootTableReference(RatsLootRegistry.RATLANTIS_FISHING_TREASURE)
								.setWeight(5)
								.setQuality(2)
								.when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().subPredicate(FishingHookPredicate.inOpenWater(true)))))
						.add(LootTableReference.lootTableReference(RatsLootRegistry.RATLANTIS_FISHING_FISH)
								.setWeight(85)
								.setQuality(-1))));

		consumer.accept(RatsLootRegistry.RATLANTIS_FISHING_FISH, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(RatlantisItemRegistry.RATFISH.get()).setWeight(20))
						.add(LootItem.lootTableItem(Items.PUFFERFISH).setWeight(12))
						.add(LootItem.lootTableItem(Items.TROPICAL_FISH).setWeight(5))));

		consumer.accept(RatsLootRegistry.RATLANTIS_FISHING_JUNK, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(RatlantisItemRegistry.RAT_TOGA.get()).setWeight(20))
						.add(LootItem.lootTableItem(RatsItemRegistry.RAT_PELT.get()).setWeight(15))
						.add(LootItem.lootTableItem(RatsItemRegistry.RAW_RAT.get()).setWeight(14))
						.add(LootItem.lootTableItem(RatsItemRegistry.PLASTIC_WASTE.get()).setWeight(18))
						.add(LootItem.lootTableItem(RatlantisItemRegistry.FERAL_RAT_CLAW.get()).setWeight(6))
						.add(LootItem.lootTableItem(RatsItemRegistry.FILTH.get()).setWeight(5))
						.add(LootItem.lootTableItem(RatsItemRegistry.TREACLE.get()).setWeight(2))
						.add(LootItem.lootTableItem(Items.BOWL).setWeight(7))
						.add(LootItem.lootTableItem(Items.BUCKET).setWeight(4))
						.add(LootItem.lootTableItem(Items.SEAGRASS).setWeight(16))
						.add(LootItem.lootTableItem(Items.FLINT).setWeight(9))
				));

		consumer.accept(RatsLootRegistry.RATLANTIS_FISHING_TREASURE, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(RatlantisItemRegistry.ORATCHALCUM_NUGGET.get()).setWeight(5))
						.add(LootItem.lootTableItem(RatlantisItemRegistry.GEM_OF_RATLANTIS.get()).setWeight(12))
						.add(LootItem.lootTableItem(RatlantisItemRegistry.GHOST_PIRAT_ECTOPLASM.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 5.0F))).setWeight(15))
						.add(LootItem.lootTableItem(RatsItemRegistry.TANGLED_RAT_TAILS.get()))
						.add(LootItem.lootTableItem(RatsItemRegistry.PLAGUE_ESSENCE.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))).setWeight(10))
						.add(LootItem.lootTableItem(RatsItemRegistry.PLAGUE_TOME.get()).setWeight(2))
						.add(LootItem.lootTableItem(Items.EMERALD).setWeight(13))
						.add(LootItem.lootTableItem(Items.DIAMOND).setWeight(4))
						.add(LootItem.lootTableItem(Items.BOOK).apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(30.0F)).allowTreasure()).setWeight(3))
						.add(LootItem.lootTableItem(Items.SADDLE).setWeight(7))
						.add(LootItem.lootTableItem(Items.TRIDENT).setWeight(5))
						.add(LootItem.lootTableItem(Items.EXPERIENCE_BOTTLE).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 14.0F))).setWeight(7))
				));
	}
}
