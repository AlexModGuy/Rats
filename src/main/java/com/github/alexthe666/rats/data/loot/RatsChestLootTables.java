package com.github.alexthe666.rats.data.loot;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.registry.RatsBlockRegistry;
import com.github.alexthe666.rats.registry.RatsItemRegistry;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.function.BiConsumer;

public class RatsChestLootTables implements LootTableSubProvider {

	@Override
	public void generate(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
		consumer.accept(new ResourceLocation(RatsMod.MODID, "chest/hammocks"), LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.add(EmptyLootItem.emptyItem().setWeight(8))
						.add(LootItem.lootTableItem(RatsItemRegistry.RAT_HAMMOCKS[0].get()))
						.add(LootItem.lootTableItem(RatsItemRegistry.RAT_HAMMOCKS[1].get()))
						.add(LootItem.lootTableItem(RatsItemRegistry.RAT_HAMMOCKS[2].get()))
						.add(LootItem.lootTableItem(RatsItemRegistry.RAT_HAMMOCKS[3].get()))
						.add(LootItem.lootTableItem(RatsItemRegistry.RAT_HAMMOCKS[4].get()))
						.add(LootItem.lootTableItem(RatsItemRegistry.RAT_HAMMOCKS[5].get()))
						.add(LootItem.lootTableItem(RatsItemRegistry.RAT_HAMMOCKS[6].get()))
						.add(LootItem.lootTableItem(RatsItemRegistry.RAT_HAMMOCKS[7].get()))
						.add(LootItem.lootTableItem(RatsItemRegistry.RAT_HAMMOCKS[8].get()))
						.add(LootItem.lootTableItem(RatsItemRegistry.RAT_HAMMOCKS[9].get()))
						.add(LootItem.lootTableItem(RatsItemRegistry.RAT_HAMMOCKS[10].get()))
						.add(LootItem.lootTableItem(RatsItemRegistry.RAT_HAMMOCKS[11].get()))
						.add(LootItem.lootTableItem(RatsItemRegistry.RAT_HAMMOCKS[12].get()))
						.add(LootItem.lootTableItem(RatsItemRegistry.RAT_HAMMOCKS[13].get()))
						.add(LootItem.lootTableItem(RatsItemRegistry.RAT_HAMMOCKS[14].get()))
						.add(LootItem.lootTableItem(RatsItemRegistry.RAT_HAMMOCKS[15].get()))));

		consumer.accept(new ResourceLocation(RatsMod.MODID, "chest/igloos"), LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.add(EmptyLootItem.emptyItem().setWeight(8))
						.add(LootItem.lootTableItem(RatsItemRegistry.RAT_IGLOOS[0].get()))
						.add(LootItem.lootTableItem(RatsItemRegistry.RAT_IGLOOS[1].get()))
						.add(LootItem.lootTableItem(RatsItemRegistry.RAT_IGLOOS[2].get()))
						.add(LootItem.lootTableItem(RatsItemRegistry.RAT_IGLOOS[3].get()))
						.add(LootItem.lootTableItem(RatsItemRegistry.RAT_IGLOOS[4].get()))
						.add(LootItem.lootTableItem(RatsItemRegistry.RAT_IGLOOS[5].get()))
						.add(LootItem.lootTableItem(RatsItemRegistry.RAT_IGLOOS[6].get()))
						.add(LootItem.lootTableItem(RatsItemRegistry.RAT_IGLOOS[7].get()))
						.add(LootItem.lootTableItem(RatsItemRegistry.RAT_IGLOOS[8].get()))
						.add(LootItem.lootTableItem(RatsItemRegistry.RAT_IGLOOS[9].get()))
						.add(LootItem.lootTableItem(RatsItemRegistry.RAT_IGLOOS[10].get()))
						.add(LootItem.lootTableItem(RatsItemRegistry.RAT_IGLOOS[11].get()))
						.add(LootItem.lootTableItem(RatsItemRegistry.RAT_IGLOOS[12].get()))
						.add(LootItem.lootTableItem(RatsItemRegistry.RAT_IGLOOS[13].get()))
						.add(LootItem.lootTableItem(RatsItemRegistry.RAT_IGLOOS[14].get()))
						.add(LootItem.lootTableItem(RatsItemRegistry.RAT_IGLOOS[15].get()))));

		consumer.accept(new ResourceLocation(RatsMod.MODID, "chest/tubes"), LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.add(EmptyLootItem.emptyItem().setWeight(8))
						.add(LootItem.lootTableItem(RatsItemRegistry.RAT_TUBES[0].get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 16.0F))))
						.add(LootItem.lootTableItem(RatsItemRegistry.RAT_TUBES[1].get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 16.0F))))
						.add(LootItem.lootTableItem(RatsItemRegistry.RAT_TUBES[2].get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 16.0F))))
						.add(LootItem.lootTableItem(RatsItemRegistry.RAT_TUBES[3].get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 16.0F))))
						.add(LootItem.lootTableItem(RatsItemRegistry.RAT_TUBES[4].get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 16.0F))))
						.add(LootItem.lootTableItem(RatsItemRegistry.RAT_TUBES[5].get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 16.0F))))
						.add(LootItem.lootTableItem(RatsItemRegistry.RAT_TUBES[6].get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 16.0F))))
						.add(LootItem.lootTableItem(RatsItemRegistry.RAT_TUBES[7].get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 16.0F))))
						.add(LootItem.lootTableItem(RatsItemRegistry.RAT_TUBES[8].get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 16.0F))))
						.add(LootItem.lootTableItem(RatsItemRegistry.RAT_TUBES[9].get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 16.0F))))
						.add(LootItem.lootTableItem(RatsItemRegistry.RAT_TUBES[10].get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 16.0F))))
						.add(LootItem.lootTableItem(RatsItemRegistry.RAT_TUBES[11].get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 16.0F))))
						.add(LootItem.lootTableItem(RatsItemRegistry.RAT_TUBES[12].get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 16.0F))))
						.add(LootItem.lootTableItem(RatsItemRegistry.RAT_TUBES[13].get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 16.0F))))
						.add(LootItem.lootTableItem(RatsItemRegistry.RAT_TUBES[14].get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 16.0F))))
						.add(LootItem.lootTableItem(RatsItemRegistry.RAT_TUBES[15].get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 16.0F))))));

		consumer.accept(new ResourceLocation(RatsMod.MODID, "chest/pet_shop"), LootTable.lootTable().withPool(LootPool.lootPool()
				.setRolls(UniformGenerator.between(5.0F, 18.0F))
				.setBonusRolls(UniformGenerator.between(0.0F, 2.0F))
				.add(LootItem.lootTableItem(Items.WHEAT_SEEDS).setWeight(45).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 10.0F))))
				.add(LootItem.lootTableItem(Items.PUMPKIN_SEEDS).setWeight(35).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 10.0F))))
				.add(LootItem.lootTableItem(Items.MELON_SEEDS).setWeight(35).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 10.0F))))
				.add(LootItem.lootTableItem(Items.FEATHER).setWeight(20).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
				.add(LootItem.lootTableItem(Items.STRING).setWeight(20).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
				.add(LootItem.lootTableItem(Items.BONE).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
				.add(LootItem.lootTableItem(Items.EGG).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
				.add(LootItem.lootTableItem(Items.PORKCHOP).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
				.add(LootItem.lootTableItem(Items.BEEF).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
				.add(LootItem.lootTableItem(Items.CHICKEN).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
				.add(LootItem.lootTableItem(Items.RABBIT).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
				.add(LootItem.lootTableItem(Items.MUTTON).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
				.add(LootItem.lootTableItem(Items.SUGAR).setWeight(9).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
				.add(LootItem.lootTableItem(Items.BOOK).setWeight(30).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 8.0F))))
				.add(LootItem.lootTableItem(Items.COD).setWeight(60).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 8.0F))))
				.add(LootItem.lootTableItem(Items.FLOWER_POT).setWeight(10))
				.add(LootItem.lootTableItem(Items.CACTUS).setWeight(5))
				.add(LootItem.lootTableItem(Items.LEAD).setWeight(40))
				.add(LootItem.lootTableItem(Items.APPLE).setWeight(25).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
				.add(LootItem.lootTableItem(Items.HAY_BLOCK).setWeight(5))
				.add(LootItem.lootTableItem(Items.WHEAT).setWeight(40).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
				.add(LootItem.lootTableItem(Items.COOKIE).setWeight(45).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 16.0F))))
				.add(LootItem.lootTableItem(Items.IRON_HORSE_ARMOR).setWeight(2))
				.add(LootItem.lootTableItem(Items.GOLDEN_HORSE_ARMOR).setWeight(2))
				.add(LootItem.lootTableItem(RatsItemRegistry.RAW_RAT.get()).setWeight(20).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
				.add(LootItem.lootTableItem(RatsItemRegistry.CHEESE.get()).setWeight(20).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
				.add(LootItem.lootTableItem(RatsItemRegistry.RAT_PELT.get()).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
				.add(LootItem.lootTableItem(RatsItemRegistry.RAW_PLASTIC.get()).setWeight(35).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 6.0F))))
				.add(LootItem.lootTableItem(RatsItemRegistry.PLASTIC_WASTE.get()).setWeight(60).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 6.0F))))
				.add(LootItem.lootTableItem(RatsBlockRegistry.RAT_CAGE.get()).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
				.add(LootItem.lootTableItem(RatsItemRegistry.RAT_WATER_BOTTLE.get()).setWeight(5))
				.add(LootItem.lootTableItem(RatsItemRegistry.RAT_SEED_BOWL.get()).setWeight(5))
				.add(LootItem.lootTableItem(RatsItemRegistry.RAT_BREEDING_LANTERN.get()).setWeight(5))
				.add(LootItem.lootTableItem(RatsItemRegistry.RAT_WHEEL.get()).setWeight(5))
				.add(LootItem.lootTableItem(RatsItemRegistry.RAT_UPGRADE_BASIC.get()))
				.add(LootTableReference.lootTableReference(new ResourceLocation(RatsMod.MODID, "chest/hammocks")))
				.add(LootTableReference.lootTableReference(new ResourceLocation(RatsMod.MODID, "chest/igloos")))
				.add(LootTableReference.lootTableReference(new ResourceLocation(RatsMod.MODID, "chest/tubes")))
		));

		consumer.accept(new ResourceLocation(RatsMod.MODID, "chest/pet_shop_upstairs"), LootTable.lootTable().withPool(LootPool.lootPool()
				.setRolls(UniformGenerator.between(5.0F, 25.0F))
				.setBonusRolls(UniformGenerator.between(0.0F, 2.0F))
				.add(LootItem.lootTableItem(RatsBlockRegistry.BLOCK_OF_CHEESE.get()).setWeight(40).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
				.add(LootItem.lootTableItem(Items.WHEAT_SEEDS).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 10.0F))))
				.add(LootItem.lootTableItem(Items.PUMPKIN_SEEDS).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 10.0F))))
				.add(LootItem.lootTableItem(Items.MELON_SEEDS).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 10.0F))))
				.add(LootItem.lootTableItem(RatsItemRegistry.CHEESE.get()).setWeight(70).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 15.0F))))
				.add(LootItem.lootTableItem(RatsItemRegistry.RAW_PLASTIC.get()).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 6.0F))))
				.add(LootItem.lootTableItem(RatsItemRegistry.PLASTIC_WASTE.get()).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 6.0F))))
				.add(LootItem.lootTableItem(RatsBlockRegistry.RAT_CAGE.get()).setWeight(20).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
				.add(LootItem.lootTableItem(RatsItemRegistry.RAT_WATER_BOTTLE.get()).setWeight(25))
				.add(LootItem.lootTableItem(RatsItemRegistry.RAT_SEED_BOWL.get()).setWeight(25))
				.add(LootItem.lootTableItem(RatsItemRegistry.RAT_BREEDING_LANTERN.get()).setWeight(25))
				.add(LootItem.lootTableItem(RatsItemRegistry.RAT_WHEEL.get()).setWeight(25))
				.add(LootItem.lootTableItem(RatsItemRegistry.RAT_UPGRADE_BASIC.get()).setWeight(5))
				.add(LootTableReference.lootTableReference(new ResourceLocation(RatsMod.MODID, "chest/hammocks")).setWeight(3))
				.add(LootTableReference.lootTableReference(new ResourceLocation(RatsMod.MODID, "chest/igloos")).setWeight(3))
				.add(LootTableReference.lootTableReference(new ResourceLocation(RatsMod.MODID, "chest/tubes")).setWeight(3))
		));

		consumer.accept(new ResourceLocation(RatsMod.MODID, "chest/plague_doctor_hut"), LootTable.lootTable().withPool(LootPool.lootPool()
				.setRolls(UniformGenerator.between(3.0F, 10.0F))
				.setBonusRolls(UniformGenerator.between(0.0F, 2.0F))
				.add(LootItem.lootTableItem(Items.WHEAT_SEEDS).setWeight(45).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 10.0F))))
				.add(LootItem.lootTableItem(Items.PUMPKIN_SEEDS).setWeight(35).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 10.0F))))
				.add(LootItem.lootTableItem(Items.MELON_SEEDS).setWeight(35).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 10.0F))))
				.add(LootItem.lootTableItem(Items.FEATHER).setWeight(20).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
				.add(LootItem.lootTableItem(Items.STRING).setWeight(20).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
				.add(LootItem.lootTableItem(Items.BONE).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
				.add(LootItem.lootTableItem(Items.EGG).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
				.add(LootItem.lootTableItem(Items.PORKCHOP).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
				.add(LootItem.lootTableItem(Items.BEEF).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
				.add(LootItem.lootTableItem(Items.CHICKEN).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
				.add(LootItem.lootTableItem(Items.RABBIT).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
				.add(LootItem.lootTableItem(Items.MUTTON).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
				.add(LootItem.lootTableItem(Items.SUGAR).setWeight(9).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
				.add(LootItem.lootTableItem(Items.BOOK).setWeight(30).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 8.0F))))
				.add(LootItem.lootTableItem(RatsItemRegistry.CONTAMINATED_FOOD.get()).setWeight(60).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 8.0F))))
				.add(LootItem.lootTableItem(Items.APPLE).setWeight(25).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
				.add(LootItem.lootTableItem(Items.HAY_BLOCK).setWeight(5))
				.add(LootItem.lootTableItem(Items.WHEAT).setWeight(40).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
				.add(LootItem.lootTableItem(RatsItemRegistry.CHEESE.get()).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
				.add(LootItem.lootTableItem(RatsItemRegistry.RAT_PELT.get()).setWeight(25).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
				.add(LootItem.lootTableItem(RatsItemRegistry.HERB_BUNDLE.get()).setWeight(60).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
				.add(LootItem.lootTableItem(RatsItemRegistry.TREACLE.get()).setWeight(50).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
				.add(LootItem.lootTableItem(RatsBlockRegistry.RAT_CAGE.get()).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
				.add(LootItem.lootTableItem(RatsItemRegistry.PLAGUE_LEECH.get()).setWeight(50))
				.add(LootItem.lootTableItem(RatsItemRegistry.PLAGUE_STEW.get()).setWeight(25))
				.add(LootItem.lootTableItem(RatsItemRegistry.PURIFYING_LIQUID.get()).setWeight(15))
		));
	}
}
