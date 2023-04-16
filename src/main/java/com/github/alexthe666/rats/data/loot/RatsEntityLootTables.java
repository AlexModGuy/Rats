package com.github.alexthe666.rats.data.loot;

import com.github.alexthe666.rats.registry.*;
import com.github.alexthe666.rats.server.loot.RatHasPlagueCondition;
import com.github.alexthe666.rats.server.loot.RatHasTogaInRatlantisCondition;
import com.github.alexthe666.rats.server.loot.RatlantisLoadedLootCondition;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithLootingCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;

import java.util.stream.Stream;

public class RatsEntityLootTables extends EntityLootSubProvider {

	protected RatsEntityLootTables() {
		super(FeatureFlags.REGISTRY.allFlags());
	}

	@Override
	public void generate() {
		this.add(RatsEntityRegistry.BLACK_DEATH.get(),
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1.0F))
								.add(LootItem.lootTableItem(RatsItemRegistry.TOKEN_PIECE.get())
										.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))))
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1.0F))
								.add(LootItem.lootTableItem(RatsItemRegistry.HERB_BUNDLE.get())
										.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 5.0F)))
										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))))
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1.0F))
								.add(LootItem.lootTableItem(RatsItemRegistry.BLACK_DEATH_MASK.get())
										.apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))))
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1.0F))
								.add(LootItem.lootTableItem(RatsItemRegistry.PLAGUE_SCYTHE.get())
										.apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))))
		);

		this.add(RatsEntityRegistry.DEMON_RAT.get(),
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1.0F))
								.add(LootItem.lootTableItem(RatsItemRegistry.COOKED_RAT.get())
										.when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.65F, 0.2F))))
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1.0F))
								.add(LootItem.lootTableItem(RatsItemRegistry.RAT_SKULL.get())
										.when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.05F, 0.05F))))
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1.0F))
								.add(LootItem.lootTableItem(RatsItemRegistry.NETHER_CHEESE.get()))
								.add(LootItem.lootTableItem(Items.MAGMA_CREAM))
								.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
								.when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.5F, 0.1F)))
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1.0F))
								.add(LootItem.lootTableItem(RatsItemRegistry.RAT_PAW.get()))
								.when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.1F, 0.02F)))
		);
		this.add(RatsEntityRegistry.PIED_PIPER.get(),
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1.0F))
								.add(LootItem.lootTableItem(RatsBlockRegistry.PIED_WOOL.get())
										.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))))
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1.0F))
								.add(LootItem.lootTableItem(Items.FEATHER)))
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1.0F))
								.add(LootItem.lootTableItem(RatsItemRegistry.PIPER_HAT.get())
										.when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.09F, 0.05F))))
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1.0F))
								.add(LootItem.lootTableItem(RatsItemRegistry.TOKEN_FRAGMENT.get())
										.when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.05F, 0.05F))))
		);

		this.add(RatsEntityRegistry.PLAGUE_BEAST.get(),
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(2.0F))
								.add(LootItem.lootTableItem(RatsItemRegistry.FILTH.get())
										.when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.5F, 0.2F)))
								.add(LootItem.lootTableItem(RatsItemRegistry.PLAGUE_LEECH.get())
										.when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.5F, 0.2F)))
								.add(LootItem.lootTableItem(RatsItemRegistry.RAT_PELT.get())
										.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))))
		);

		this.add(RatsEntityRegistry.PLAGUE_CLOUD.get(), LootTable.lootTable());

		this.add(RatsEntityRegistry.PLAGUE_DOCTOR.get(),
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(2.0F))
								.add(LootItem.lootTableItem(RatsItemRegistry.CONTAMINATED_FOOD.get())
										.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))
								.add(LootItem.lootTableItem(RatsItemRegistry.HERB_BUNDLE.get())
										.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
								.add(LootItem.lootTableItem(RatsItemRegistry.PLAGUE_LEECH.get())
										.when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.5F, 0.2F)))
								.add(LootItem.lootTableItem(Items.ROTTEN_FLESH)
										.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))
										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))))
		);

		this.add(RatsEntityRegistry.RAT.get(),
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1.0F))
								.add(LootItem.lootTableItem(RatsItemRegistry.RAT_PELT.get())
										.when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.25F, 0.25F))))
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1.0F))
								.add(LootItem.lootTableItem(RatsItemRegistry.RAW_RAT.get()))
								.apply(SmeltItemFunction.smelted()
										.when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, ENTITY_ON_FIRE))))
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1.0F))
								.add(LootItem.lootTableItem(RatsItemRegistry.RAT_SKULL.get())
										.when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.05F, 0.05F))))
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1.0F))
								.add(LootItem.lootTableItem(RatsItemRegistry.RAT_PAW.get())
										.when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.1F, 0.02F))))
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1.0F))
								.add(LootItem.lootTableItem(RatlantisBlockRegistry.CHUNKY_CHEESE_TOKEN.get())
										.when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.0001F, 0.0001F))
										.when(RatlantisLoadedLootCondition::new)))
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1.0F))
								.add(LootItem.lootTableItem(RatsItemRegistry.PLAGUE_ESSENCE.get())
										.when(RatHasPlagueCondition.hasPlague())
										.when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.05F, 0.01F))))
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1.0F))
								.add(LootItem.lootTableItem(RatsItemRegistry.PLAGUE_TOME.get())
										.when(RatHasPlagueCondition.hasPlague())
										.when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.0035F, 0.001F))))
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1.0F))
								.add(LootTableReference.lootTableReference(RatsLootRegistry.RATLANTIS_RAT_EXCLUSIVE_DROPS)
										.when(RatHasTogaInRatlantisCondition::new))
								.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))
		);

		this.add(RatsEntityRegistry.RAT_KING.get(),
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1.0F))
								.add(LootItem.lootTableItem(RatsItemRegistry.TOKEN_PIECE.get())
										.when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.5F, 0.25F))))
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1.0F))
								.add(LootItem.lootTableItem(RatsItemRegistry.FILTH.get())
										.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))))
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1.0F))
								.add(LootItem.lootTableItem(RatsItemRegistry.RAT_KING_CROWN.get())))
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1.0F))
								.add(LootItem.lootTableItem(RatsItemRegistry.TANGLED_RAT_TAILS.get())))
		);

		this.add(RatsEntityRegistry.TAMED_RAT.get(), LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootTableReference.lootTableReference(RatsEntityRegistry.RAT.get().getDefaultLootTable())))
		);
	}

	@Override
	protected Stream<EntityType<?>> getKnownEntityTypes() {
		return RatsEntityRegistry.ENTITIES.getEntries().stream().map(RegistryObject::get);
	}
}
