package com.github.alexthe666.rats.data.ratlantis.loot;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.registry.RatlantisBlockRegistry;
import com.github.alexthe666.rats.registry.RatlantisItemRegistry;
import com.github.alexthe666.rats.registry.RatsBlockRegistry;
import com.github.alexthe666.rats.registry.RatsItemRegistry;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.function.BiConsumer;

public class RatlantisChestLootTables implements LootTableSubProvider {
	@Override
	public void generate(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
		consumer.accept(new ResourceLocation(RatsMod.MODID, "chest/dutchrat_ship"), LootTable.lootTable().withPool(LootPool.lootPool()
				.setRolls(UniformGenerator.between(3.0F, 10.0F))
				.setBonusRolls(UniformGenerator.between(0.0F, 2.0F))
				.add(LootItem.lootTableItem(RatlantisItemRegistry.ORATCHALCUM_NUGGET.get()).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 5.0F))))
				.add(LootItem.lootTableItem(RatlantisBlockRegistry.PIRAT_PLANKS.get()).setWeight(45).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 10.0F))))
				.add(LootItem.lootTableItem(RatsBlockRegistry.MARBLED_CHEESE_RAW.get()).setWeight(35).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 10.0F))))
				.add(LootItem.lootTableItem(RatlantisBlockRegistry.MARBLED_CHEESE_RAT_HEAD.get()).setWeight(5))
				.add(LootItem.lootTableItem(RatlantisItemRegistry.MILITARY_HAT.get()).setWeight(3))
				.add(LootItem.lootTableItem(RatsBlockRegistry.FISH_BARREL.get()).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
				.add(LootItem.lootTableItem(RatsItemRegistry.CONTAMINATED_FOOD.get()).setWeight(20).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
				.add(LootItem.lootTableItem(Items.BONE).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
				.add(LootItem.lootTableItem(Items.FIRE_CHARGE).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
				.add(LootItem.lootTableItem(RatlantisItemRegistry.CHEESE_CANNONBALL.get()).setWeight(25).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
				.add(LootItem.lootTableItem(Items.NAUTILUS_SHELL).setWeight(5))
				.add(LootItem.lootTableItem(Items.TRIDENT))
				.add(LootItem.lootTableItem(Items.PHANTOM_MEMBRANE).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
				.add(LootItem.lootTableItem(RatlantisItemRegistry.GHOST_PIRAT_ECTOPLASM.get()).setWeight(60).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
				.add(LootItem.lootTableItem(RatsItemRegistry.PIRAT_HAT.get()).setWeight(9))
				.add(LootItem.lootTableItem(RatlantisItemRegistry.PIRAT_CUTLASS.get()).setWeight(9))
				.add(LootItem.lootTableItem(Items.BOOK).setWeight(30).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 8.0F))))
				.add(LootItem.lootTableItem(RatlantisItemRegistry.GHOST_PIRAT_HAT.get()).setWeight(10))
				.add(LootItem.lootTableItem(RatlantisItemRegistry.GHOST_PIRAT_CUTLASS.get()).setWeight(2))
				.add(LootItem.lootTableItem(RatlantisItemRegistry.RATFISH.get()).setWeight(20).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 5.0F))))
				.add(LootItem.lootTableItem(RatlantisItemRegistry.RAT_TOGA.get()).setWeight(5))
				.add(LootItem.lootTableItem(Items.WHEAT).setWeight(45).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
				.add(LootItem.lootTableItem(RatsItemRegistry.CHEESE.get()).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
				.add(LootItem.lootTableItem(RatsItemRegistry.RAT_PELT.get()).setWeight(25).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
				.add(LootItem.lootTableItem(RatsItemRegistry.TREACLE.get()).setWeight(50).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
				.add(LootItem.lootTableItem(RatsItemRegistry.PLAGUE_LEECH.get()).setWeight(50))
				.add(LootItem.lootTableItem(RatsItemRegistry.PLAGUE_STEW.get()).setWeight(25))
				.add(LootItem.lootTableItem(RatsItemRegistry.TINY_COIN.get()).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(6.0F, 39.0F))))
		));
	}
}
