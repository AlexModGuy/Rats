package com.github.alexthe666.rats.data.ratlantis.loot;

import com.github.alexthe666.rats.registry.RatlantisBlockRegistry;
import com.github.alexthe666.rats.registry.RatlantisEntityRegistry;
import com.github.alexthe666.rats.registry.RatlantisItemRegistry;
import com.github.alexthe666.rats.registry.RatsItemRegistry;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithLootingCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;

import java.util.stream.Stream;

public class RatlantisEntityLootTables extends EntityLootSubProvider {
	protected RatlantisEntityLootTables() {
		super(FeatureFlags.REGISTRY.allFlags());
	}

	@Override
	public void generate() {
		this.add(RatlantisEntityRegistry.DUTCHRAT.get(),
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1.0F))
								.add(LootItem.lootTableItem(RatlantisItemRegistry.DUTCHRAT_WHEEL.get())))
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1.0F))
								.add(LootItem.lootTableItem(RatlantisItemRegistry.GHOST_PIRAT_ECTOPLASM.get())
										.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 8.0F)))
										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 2.0F))))));

		this.add(RatlantisEntityRegistry.FERAL_RATLANTEAN.get(),
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(2.0F))
								.add(LootItem.lootTableItem(RatlantisItemRegistry.FERAL_RAT_CLAW.get())
										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))
								.add(LootItem.lootTableItem(RatlantisItemRegistry.RAT_TOGA.get()))
								.add(LootItem.lootTableItem(RatsItemRegistry.RAT_PELT.get())
										.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))))
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1.0F))
								.add(LootItem.lootTableItem(RatlantisBlockRegistry.MARBLED_CHEESE_RAT_HEAD.get()))
								.when(LootItemKilledByPlayerCondition.killedByPlayer())
								.when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.05F, 0.05F))));

		this.add(RatlantisEntityRegistry.GHOST_PIRAT.get(),
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1.0F))
								.add(LootItem.lootTableItem(RatlantisItemRegistry.GHOST_PIRAT_ECTOPLASM.get())
										.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));

		this.add(RatlantisEntityRegistry.NEO_RATLANTEAN.get(),
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1.0F))
								.add(LootItem.lootTableItem(RatlantisItemRegistry.PSIONIC_RAT_BRAIN.get()))));

		this.add(RatlantisEntityRegistry.PIRAT.get(),
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1.0F))
								.add(LootItem.lootTableItem(RatsItemRegistry.RAT_PELT.get())
										.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F)))
										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))))
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1.0F))
								.add(LootItem.lootTableItem(RatsItemRegistry.RAW_RAT.get()))
								.apply(SmeltItemFunction.smelted()
										.when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, ENTITY_ON_FIRE))))
		);

		this.add(RatlantisEntityRegistry.RAT_BARON.get(), LootTable.lootTable());

		this.add(RatlantisEntityRegistry.RAT_BARON_PLANE.get(),
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1.0F))
								.add(LootItem.lootTableItem(RatlantisItemRegistry.BIPLANE_WING.get())
										.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))))
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1.0F))
								.add(LootItem.lootTableItem(RatlantisItemRegistry.ORATCHALCUM_NUGGET.get())
										.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 32.0F)))
										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 6.0F)))))
		);

		this.add(RatlantisEntityRegistry.RATFISH.get(),
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1.0F))
								.add(LootItem.lootTableItem(RatlantisItemRegistry.RATFISH.get())))
		);

		this.add(RatlantisEntityRegistry.RATLANTEAN_AUTOMATON.get(),
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1.0F))
								.add(LootItem.lootTableItem(RatlantisItemRegistry.ANCIENT_SAWBLADE.get())))
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1.0F))
								.add(LootItem.lootTableItem(RatlantisItemRegistry.ARCANE_TECHNOLOGY.get())
										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))))
		);

		this.add(RatlantisEntityRegistry.RATLANTEAN_RATBOT.get(),
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(2.0F))
								.add(LootItem.lootTableItem(Items.IRON_INGOT)
										.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F)))
										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))
								.add(LootItem.lootTableItem(RatlantisItemRegistry.ORATCHALCUM_NUGGET.get())
										.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F)))
										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))
								.add(LootItem.lootTableItem(RatlantisItemRegistry.RATBOT_BARREL.get())
										.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F)))
										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))
								.add(LootItem.lootTableItem(Items.REDSTONE)
										.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F)))
										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))))
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1.0F))
								.add(LootItem.lootTableItem(RatlantisBlockRegistry.MARBLED_CHEESE_RAT_HEAD.get()))
								.when(LootItemKilledByPlayerCondition.killedByPlayer())
								.when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.05F, 0.05F)))
		);

		this.add(RatlantisEntityRegistry.RATLANTEAN_SPIRIT.get(),
				LootTable.lootTable()
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1.0F))
								.add(LootItem.lootTableItem(RatlantisItemRegistry.RATLANTEAN_FLAME.get())
										.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F)))
										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))))
						.withPool(LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1.0F))
								.add(LootItem.lootTableItem(RatlantisBlockRegistry.MARBLED_CHEESE_RAT_HEAD.get()))
								.when(LootItemKilledByPlayerCondition.killedByPlayer())
								.when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.05F, 0.05F)))
		);

	}

	@Override
	protected Stream<EntityType<?>> getKnownEntityTypes() {
		return RatlantisEntityRegistry.ENTITIES.getEntries().stream().map(RegistryObject::get);
	}
}
