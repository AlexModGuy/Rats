package com.github.alexthe666.rats.data.loot;

import com.github.alexthe666.rats.registry.RatsBlockRegistry;
import com.github.alexthe666.rats.registry.RatsItemRegistry;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;
import java.util.stream.Collectors;

public class RatsBlockLootTables extends BlockLootSubProvider {

	protected RatsBlockLootTables() {
		super(Set.of(), FeatureFlags.REGISTRY.allFlags());
	}

	@Override
	protected void generate() {
		this.dropSelf(RatsBlockRegistry.AUTO_CURDLER.get());
		this.dropSelf(RatsBlockRegistry.BLOCK_OF_BLUE_CHEESE.get());
		this.dropSelf(RatsBlockRegistry.BLOCK_OF_NETHER_CHEESE.get());
		this.dropSelf(RatsBlockRegistry.BLOCK_OF_CHEESE.get());
		this.dropCauldronStuff(RatsBlockRegistry.BLUE_CHEESE_CAULDRON.get(), RatsBlockRegistry.BLOCK_OF_BLUE_CHEESE.get());
		this.dropCauldronStuff(RatsBlockRegistry.CHEESE_CAULDRON.get(), RatsBlockRegistry.BLOCK_OF_CHEESE.get());
		this.dropOther(RatsBlockRegistry.MILK_CAULDRON.get(), Items.CAULDRON);
		this.dropCauldronStuff(RatsBlockRegistry.NETHER_CHEESE_CAULDRON.get(), RatsBlockRegistry.BLOCK_OF_NETHER_CHEESE.get());
		this.dropSelf(RatsBlockRegistry.COMPRESSED_GARBAGE.get());
		this.dropSelf(RatsBlockRegistry.CURSED_GARBAGE.get());
		this.dropSelf(RatsBlockRegistry.DYE_SPONGE.get());
		this.dropSelf(RatsBlockRegistry.FISH_BARREL.get());
		this.add(RatsBlockRegistry.GARBAGE_PILE.get(), createSilkTouchDispatchTable(RatsBlockRegistry.GARBAGE_PILE.get(), applyExplosionCondition(RatsBlockRegistry.GARBAGE_PILE.get(), LootItem.lootTableItem(RatsItemRegistry.PLASTIC_WASTE.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))).when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, 0.1F, 0.14285715F, 0.25F, 1.0F)).otherwise(LootItem.lootTableItem(RatsBlockRegistry.GARBAGE_PILE.get())))));
		this.dropSelf(RatsBlockRegistry.JACK_O_RATERN.get());
		this.dropSelf(RatsBlockRegistry.MANHOLE.get());
		this.dropSelf(RatsBlockRegistry.MARBLED_CHEESE_RAW.get());
		this.dropSelf(RatsBlockRegistry.PIED_GARBAGE.get());
		this.dropSelf(RatsBlockRegistry.PIED_WOOL.get());
		this.dropSelf(RatsBlockRegistry.PURIFIED_GARBAGE.get());
		this.dropSelf(RatsBlockRegistry.RAT_ATTRACTOR.get());
		this.dropSelf(RatsBlockRegistry.RAT_CAGE.get());
		this.dropOther(RatsBlockRegistry.RAT_CAGE_BREEDING_LANTERN.get(), RatsBlockRegistry.RAT_CAGE.get());
		this.dropOther(RatsBlockRegistry.RAT_CAGE_DECORATED.get(), RatsBlockRegistry.RAT_CAGE.get());
		this.dropOther(RatsBlockRegistry.RAT_CAGE_WHEEL.get(), RatsBlockRegistry.RAT_CAGE.get());
		this.dropSelf(RatsBlockRegistry.RAT_CRAFTING_TABLE.get());
		this.add(RatsBlockRegistry.RAT_HOLE.get(), LootTable.lootTable());
		this.dropSelf(RatsBlockRegistry.RAT_QUARRY.get());
		this.add(RatsBlockRegistry.RAT_QUARRY_PLATFORM.get(), LootTable.lootTable());
		this.dropSelf(RatsBlockRegistry.RAT_TRAP.get());
		this.add(RatsBlockRegistry.RAT_TUBE_COLOR.get(), LootTable.lootTable());
		this.dropSelf(RatsBlockRegistry.RAT_UPGRADE_BLOCK.get());
		this.dropSelf(RatsBlockRegistry.TRASH_CAN.get());
		this.dropSelf(RatsBlockRegistry.UPGRADE_COMBINER.get());
		this.dropSelf(RatsBlockRegistry.UPGRADE_SEPARATOR.get());
	}

	protected void dropCauldronStuff(Block cauldron, ItemLike contents) {
		this.add(cauldron, LootTable.lootTable()
				.withPool(this.applyExplosionCondition(contents, LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(contents))))
				.withPool(this.applyExplosionCondition(Blocks.CAULDRON, LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(Blocks.CAULDRON))))
		);
	}

	@Override
	protected Iterable<Block> getKnownBlocks() {
		return RatsBlockRegistry.BLOCKS.getEntries().stream().map(RegistryObject::get).collect(Collectors.toList());
	}
}
