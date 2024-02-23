package com.github.alexthe666.rats.data.ratlantis.loot;

import com.github.alexthe666.rats.registry.RatlantisBlockRegistry;
import com.github.alexthe666.rats.registry.RatlantisItemRegistry;
import com.github.alexthe666.rats.registry.RatsItemRegistry;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;
import java.util.stream.Collectors;

public class RatlantisBlockLootTables extends BlockLootSubProvider {
	protected RatlantisBlockLootTables() {
		super(Set.of(), FeatureFlags.REGISTRY.allFlags());
	}

	@Override
	protected void generate() {
		this.dropSelf(RatlantisBlockRegistry.AIR_RAID_SIREN.get());
		this.dropSelf(RatlantisBlockRegistry.BLACK_MARBLED_CHEESE.get());
		this.dropSelf(RatlantisBlockRegistry.BRAIN_BLOCK.get());
		this.add(RatlantisBlockRegistry.CHEESE_ORE.get(), createSilkTouchDispatchTable(RatlantisBlockRegistry.CHEESE_ORE.get(), applyExplosionDecay(RatlantisBlockRegistry.CHEESE_ORE.get(), LootItem.lootTableItem(RatsItemRegistry.CHEESE.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 6.0F))).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE)))));
		this.dropSelf(RatlantisBlockRegistry.CHUNKY_CHEESE_TOKEN.get());
		this.dropSelf(RatlantisBlockRegistry.COMPRESSED_RAT.get());
		this.dropSelf(RatlantisBlockRegistry.DUTCHRAT_BELL.get());
		this.dropSelf(RatlantisBlockRegistry.MARBLED_CHEESE.get());
		this.dropSelf(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK.get());
		this.add(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_SLAB.get(), createSlabItemTable(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_SLAB.get()));
		this.dropSelf(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_STAIRS.get());
		this.dropSelf(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_CHISELED.get());
		this.dropSelf(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_CRACKED.get());
		this.add(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_CRACKED_SLAB.get(), createSlabItemTable(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_CRACKED_SLAB.get()));
		this.dropSelf(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_CRACKED_STAIRS.get());
		this.dropSelf(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_MOSSY.get());
		this.add(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_MOSSY_SLAB.get(), createSlabItemTable(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_MOSSY_SLAB.get()));
		this.dropSelf(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_MOSSY_STAIRS.get());
		this.dropSelf(RatlantisBlockRegistry.MARBLED_CHEESE_CHISELED.get());
		this.dropSelf(RatlantisBlockRegistry.MARBLED_CHEESE_DIRT.get());
		this.dropSelf(RatlantisBlockRegistry.MARBLED_CHEESE_GOLEM_CORE.get());
		this.add(RatlantisBlockRegistry.MARBLED_CHEESE_GRASS.get(), createSingleItemTableWithSilkTouch(RatlantisBlockRegistry.MARBLED_CHEESE_GRASS.get(), RatlantisBlockRegistry.MARBLED_CHEESE_DIRT.get()));
		this.dropSelf(RatlantisBlockRegistry.MARBLED_CHEESE_PILLAR.get());
		this.dropSelf(RatlantisBlockRegistry.MARBLED_CHEESE_RAT_HEAD.get());
		this.add(RatlantisBlockRegistry.MARBLED_CHEESE_SLAB.get(), createSlabItemTable(RatlantisBlockRegistry.MARBLED_CHEESE_SLAB.get()));
		this.dropSelf(RatlantisBlockRegistry.MARBLED_CHEESE_STAIRS.get());
		this.dropSelf(RatlantisBlockRegistry.MARBLED_CHEESE_TILE.get());
		this.dropSelf(RatlantisBlockRegistry.ORATCHALCUM_BLOCK.get());
		this.add(RatlantisBlockRegistry.ORATCHALCUM_ORE.get(), createOreDrop(RatlantisBlockRegistry.ORATCHALCUM_ORE.get(), RatlantisItemRegistry.RAW_ORATCHALCUM.get()));
		this.dropSelf(RatlantisBlockRegistry.PIRAT_BUTTON.get());
		this.add(RatlantisBlockRegistry.PIRAT_DOOR.get(), createDoorTable(RatlantisBlockRegistry.PIRAT_DOOR.get()));
		this.dropSelf(RatlantisBlockRegistry.PIRAT_FENCE.get());
		this.dropSelf(RatlantisBlockRegistry.PIRAT_FENCE_GATE.get());
		this.dropSelf(RatlantisBlockRegistry.PIRAT_HANGING_SIGN.get());
		this.add(RatlantisBlockRegistry.PIRAT_LEAVES.get(), createSilkTouchOrShearsDispatchTable(RatlantisBlockRegistry.PIRAT_LEAVES.get(), this.applyExplosionCondition(RatlantisBlockRegistry.PIRAT_LEAVES.get(), LootItem.lootTableItem(RatlantisBlockRegistry.PIRAT_SAPLING.get()))
				.when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, 0.025F, 0.027777778F, 0.03125F, 0.041666668F, 0.1F)))
				.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
						.when(MatchTool.toolMatches(ItemPredicate.Builder.item().of(Tags.Items.SHEARS)).or(HAS_SILK_TOUCH).invert())
						.add(this.applyExplosionCondition(RatlantisBlockRegistry.PIRAT_LEAVES.get(), LootItem.lootTableItem(RatlantisItemRegistry.GHOST_PIRAT_ECTOPLASM.get()))
								.when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, 0.005F, 0.0055555557F, 0.00625F, 0.008333334F, 0.025F)))));
		this.dropSelf(RatlantisBlockRegistry.PIRAT_LOG.get());
		this.dropSelf(RatlantisBlockRegistry.PIRAT_PLANKS.get());
		this.dropSelf(RatlantisBlockRegistry.PIRAT_PRESSURE_PLATE.get());
		this.dropSelf(RatlantisBlockRegistry.PIRAT_SAPLING.get());
		this.dropPottedContents(RatlantisBlockRegistry.POTTED_PIRAT_SAPLING.get());
		this.dropSelf(RatlantisBlockRegistry.PIRAT_SIGN.get());
		this.add(RatlantisBlockRegistry.PIRAT_SLAB.get(), createSlabItemTable(RatlantisBlockRegistry.PIRAT_SLAB.get()));
		this.dropSelf(RatlantisBlockRegistry.PIRAT_STAIRS.get());
		this.dropSelf(RatlantisBlockRegistry.PIRAT_TRAPDOOR.get());
		this.dropSelf(RatlantisBlockRegistry.PIRAT_WOOD.get());
		this.dropSelf(RatlantisBlockRegistry.RATGLOVE_FLOWER.get());
		this.dropPottedContents(RatlantisBlockRegistry.POTTED_RATGLOVE_FLOWER.get());
		this.add(RatlantisBlockRegistry.RATLANTEAN_GEM_ORE.get(), createOreDrop(RatlantisBlockRegistry.RATLANTEAN_GEM_ORE.get(), RatlantisItemRegistry.GEM_OF_RATLANTIS.get()));
		this.add(RatlantisBlockRegistry.RATLANTIS_PORTAL.get(), LootTable.lootTable());
		this.dropSelf(RatlantisBlockRegistry.RATLANTIS_REACTOR.get());
		this.dropSelf(RatlantisBlockRegistry.RATLANTIS_UPGRADE_BLOCK.get());
		this.dropSelf(RatlantisBlockRegistry.STRIPPED_PIRAT_LOG.get());
		this.dropSelf(RatlantisBlockRegistry.STRIPPED_PIRAT_WOOD.get());
	}

	@Override
	protected Iterable<Block> getKnownBlocks() {
		return RatlantisBlockRegistry.BLOCKS.getEntries().stream().map(RegistryObject::get).collect(Collectors.toList());
	}
}
