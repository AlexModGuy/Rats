package com.github.alexthe666.rats.data;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.server.loot.GenericAddItemLootModifier;
import com.github.alexthe666.rats.server.loot.RatKilledAndHasUpgradeCondition;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;

public class RatsLootModifierGenerator extends GlobalLootModifierProvider {
	public RatsLootModifierGenerator(PackOutput output) {
		super(output, RatsMod.MODID);
	}

	@Override
	protected void start() {
		this.add("add_cat_gifts", new GenericAddItemLootModifier(new LootItemCondition[]{LootTableIdCondition.builder(BuiltInLootTables.CAT_MORNING_GIFT).build(), LootItemRandomChanceCondition.randomChance(0.3F).build()}, true, new ItemStack(RatsItemRegistry.RAW_RAT.get()), new ItemStack(RatsItemRegistry.TINY_COIN.get())));
		this.add("add_creeper_chunks", new GenericAddItemLootModifier(new LootItemCondition[]{LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().nbt(new NbtPredicate(this.createCreeperTag()))).build(), LootTableIdCondition.builder(EntityType.CREEPER.getDefaultLootTable()).build()}, false, new ItemStack(RatsItemRegistry.CHARGED_CREEPER_CHUNK.get())));
		this.add("add_tiny_coins", new GenericAddItemLootModifier(new LootItemCondition[]{RatKilledAndHasUpgradeCondition.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_ARISTOCRAT.get()).build()}, false, new ItemStack(RatsItemRegistry.TINY_COIN.get())));
	}

	private CompoundTag createCreeperTag() {
		CompoundTag tag = new CompoundTag();
		tag.putBoolean("powered", true);
		return tag;
	}
}
