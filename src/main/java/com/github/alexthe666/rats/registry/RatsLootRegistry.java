package com.github.alexthe666.rats.registry;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.loot.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import com.mojang.serialization.MapCodec;

public class RatsLootRegistry {

	public static final ResourceLocation CHRISTMAS_GIFTS = ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "christmas_rat_gifts");
	public static final ResourceKey<LootTable> CHRISTMAS_GIFTS_KEY = ResourceKey.create(Registries.LOOT_TABLE, CHRISTMAS_GIFTS);
	public static final ResourceLocation PET_SHOP_HOTV = ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "gameplay/hero_of_the_village/pet_shop_owner");
	public static final ResourceKey<LootTable> PET_SHOP_HOTV_KEY = ResourceKey.create(Registries.LOOT_TABLE, PET_SHOP_HOTV);
	public static final ResourceLocation RATLANTIS_RAT_EXCLUSIVE_DROPS = ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "gameplay/ratlantis_exclusive_rat_loot");
	public static final ResourceKey<LootTable> RATLANTIS_RAT_EXCLUSIVE_DROPS_KEY = ResourceKey.create(Registries.LOOT_TABLE, RATLANTIS_RAT_EXCLUSIVE_DROPS);

	public static final ResourceLocation RATLANTIS_FISH = ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "gameplay/fishing/ratlantis");
	public static final ResourceKey<LootTable> RATLANTIS_FISH_KEY = ResourceKey.create(Registries.LOOT_TABLE, RATLANTIS_FISH);
	public static final ResourceLocation RATLANTIS_FISHING_JUNK = ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "gameplay/fishing/ratlantis_junk");
	public static final ResourceKey<LootTable> RATLANTIS_FISHING_JUNK_KEY = ResourceKey.create(Registries.LOOT_TABLE, RATLANTIS_FISHING_JUNK);
	public static final ResourceLocation RATLANTIS_FISHING_TREASURE = ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "gameplay/fishing/ratlantis_treasure");
	public static final ResourceKey<LootTable> RATLANTIS_FISHING_TREASURE_KEY = ResourceKey.create(Registries.LOOT_TABLE, RATLANTIS_FISHING_TREASURE);
	public static final ResourceLocation RATLANTIS_FISHING_FISH = ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "gameplay/fishing/ratlantis_fish");
	public static final ResourceKey<LootTable> RATLANTIS_FISHING_FISH_KEY = ResourceKey.create(Registries.LOOT_TABLE, RATLANTIS_FISHING_FISH);


	public static final DeferredRegister<LootItemConditionType> CONDITIONS = DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, RatsMod.MODID);
	public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, RatsMod.MODID);

	public static final DeferredHolder<LootItemConditionType, LootItemConditionType> RATLANTIS_LOADED = CONDITIONS.register("ratlantis_loaded", () -> new LootItemConditionType(RatlantisLoadedLootCondition.CODEC));
	public static final DeferredHolder<LootItemConditionType, LootItemConditionType> KILLER_HAS_UPGRADE = CONDITIONS.register("killer_has_upgrade", () -> new LootItemConditionType(RatKilledAndHasUpgradeCondition.CODEC));
	public static final DeferredHolder<LootItemConditionType, LootItemConditionType> RAT_HAS_PLAGUE = CONDITIONS.register("rat_has_plague", () -> new LootItemConditionType(RatHasPlagueCondition.CODEC));
	public static final DeferredHolder<LootItemConditionType, LootItemConditionType> HAS_TOGA_AND_IN_RATLANTIS = CONDITIONS.register("has_toga_and_in_ratlantis", () -> new LootItemConditionType(RatHasTogaInRatlantisCondition.CODEC));
	public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<GenericAddItemLootModifier>> ADD_ITEM = LOOT_MODIFIERS.register("add_item", () -> GenericAddItemLootModifier.CODEC);
}







