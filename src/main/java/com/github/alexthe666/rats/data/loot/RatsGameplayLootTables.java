package com.github.alexthe666.rats.data.loot;

import com.github.alexthe666.rats.registry.RatsBlockRegistry;
import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.registry.RatsLootRegistry;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.function.BiConsumer;

public class RatsGameplayLootTables implements LootTableSubProvider {
	@Override
	public void generate(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
		consumer.accept(RatsLootRegistry.PET_SHOP_HOTV, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(RatsItemRegistry.CHEESE.get()))
						.add(LootItem.lootTableItem(RatsItemRegistry.TINY_COIN.get()))
						.add(LootItem.lootTableItem(RatsItemRegistry.RAW_PLASTIC.get()))
						.add(LootItem.lootTableItem(RatsBlockRegistry.RAT_CAGE.get()))));

		consumer.accept(RatsLootRegistry.CHRISTMAS_GIFTS, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(Items.COAL).setWeight(20))
						.add(LootItem.lootTableItem(Items.COAL_BLOCK).setWeight(2))
						.add(LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(2))
						.add(LootItem.lootTableItem(Items.GOLD_NUGGET).setWeight(10))
						.add(LootItem.lootTableItem(Items.EMERALD).setWeight(5))
						.add(LootItem.lootTableItem(Items.DIAMOND).setWeight(1))
						.add(LootItem.lootTableItem(Items.CAKE).setWeight(15))
						.add(LootItem.lootTableItem(Items.COOKIE).setWeight(25))
						.add(LootItem.lootTableItem(Items.MILK_BUCKET).setWeight(10))
						.add(LootItem.lootTableItem(Items.BEETROOT).setWeight(5))
						.add(LootItem.lootTableItem(Items.BREAD).setWeight(5))
						.add(LootItem.lootTableItem(Items.GOLDEN_CARROT).setWeight(5))
						.add(LootItem.lootTableItem(Items.BOOK).setWeight(20))
						.add(LootItem.lootTableItem(Items.PUMPKIN_PIE).setWeight(10))
						.add(LootItem.lootTableItem(Items.NAME_TAG).setWeight(10))
						.add(LootItem.lootTableItem(Items.SUGAR).setWeight(15))
						.add(LootItem.lootTableItem(RatsItemRegistry.STRING_CHEESE.get()).setWeight(15))
						.add(LootItem.lootTableItem(RatsItemRegistry.POTATO_PANCAKE.get()).setWeight(3))
						.add(LootItem.lootTableItem(RatsItemRegistry.TREACLE.get()).setWeight(3))
						.add(LootItem.lootTableItem(RatsItemRegistry.TINY_COIN.get()).setWeight(25))
						.add(LootItem.lootTableItem(RatsItemRegistry.TOKEN_FRAGMENT.get()).setWeight(3))
						.add(LootItem.lootTableItem(RatsItemRegistry.SANTA_HAT.get()).setWeight(8))
						.add(LootItem.lootTableItem(RatsItemRegistry.PIRAT_HAT.get()).setWeight(8))
						.add(LootItem.lootTableItem(RatsItemRegistry.TOP_HAT.get()).setWeight(8))
						.add(LootItem.lootTableItem(RatsItemRegistry.FISHERMAN_HAT.get()).setWeight(8))
						.add(LootItem.lootTableItem(RatsItemRegistry.RAT_FEZ.get()).setWeight(8))
						.add(LootItem.lootTableItem(RatsItemRegistry.ASSORTED_VEGETABLES.get()).setWeight(8))));
	}
}
