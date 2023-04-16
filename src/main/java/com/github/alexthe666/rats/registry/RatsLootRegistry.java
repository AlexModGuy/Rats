package com.github.alexthe666.rats.registry;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.loot.*;
import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RatsLootRegistry {

	public static final ResourceLocation CHRISTMAS_GIFTS = new ResourceLocation(RatsMod.MODID, "christmas_rat_gifts");
	public static final ResourceLocation PET_SHOP_HOTV = new ResourceLocation(RatsMod.MODID, "gameplay/hero_of_the_village/pet_shop_owner");
	public static final ResourceLocation RATLANTIS_RAT_EXCLUSIVE_DROPS = new ResourceLocation(RatsMod.MODID, "gameplay/ratlantis_exclusive_rat_loot");

	public static final ResourceLocation RATLANTIS_FISH = new ResourceLocation(RatsMod.MODID, "gameplay/fishing/ratlantis");
	public static final ResourceLocation RATLANTIS_FISHING_JUNK = new ResourceLocation(RatsMod.MODID, "gameplay/fishing/ratlantis_junk");
	public static final ResourceLocation RATLANTIS_FISHING_TREASURE = new ResourceLocation(RatsMod.MODID, "gameplay/fishing/ratlantis_treasure");
	public static final ResourceLocation RATLANTIS_FISHING_FISH = new ResourceLocation(RatsMod.MODID, "gameplay/fishing/ratlantis_fish");


	public static final DeferredRegister<LootItemConditionType> CONDITIONS = DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, RatsMod.MODID);
	public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, RatsMod.MODID);

	public static final RegistryObject<LootItemConditionType> RATLANTIS_LOADED = CONDITIONS.register("ratlantis_loaded", () -> new LootItemConditionType(new RatlantisLoadedLootCondition.ConditionSerializer()));
	public static final RegistryObject<LootItemConditionType> KILLER_HAS_UPGRADE = CONDITIONS.register("killer_has_upgrade", () -> new LootItemConditionType(new RatKilledAndHasUpgradeCondition.RatSerializer()));
	public static final RegistryObject<LootItemConditionType> RAT_HAS_PLAGUE = CONDITIONS.register("rat_has_plague", () -> new LootItemConditionType(new RatHasPlagueCondition.RatSerializer()));
	public static final RegistryObject<LootItemConditionType> HAS_TOGA_AND_IN_RATLANTIS = CONDITIONS.register("has_toga_and_in_ratlantis", () -> new LootItemConditionType(new RatHasTogaInRatlantisCondition.ConditionSerializer()));
	public static final RegistryObject<Codec<GenericAddItemLootModifier>> ADD_ITEM = LOOT_MODIFIERS.register("add_item", () -> GenericAddItemLootModifier.CODEC);
}
