package com.github.alexthe666.rats.registry;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.data.tags.RatsBannerPatternTags;
import com.github.alexthe666.rats.server.items.*;
import com.github.alexthe666.rats.server.items.upgrades.*;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RatlantisItemRegistry {

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, RatsMod.MODID);

	public static final RegistryObject<Item> RAS_BANNER_PATTERN = ITEMS.register("rat_and_sickle_banner_pattern", () -> new BannerPatternItem(RatsBannerPatternTags.RAS_BANNER_PATTERN, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> RATLANTIS_RAT_SKULL = ITEMS.register("ratlantis_rat_skull", () -> new Item(new Item.Properties().rarity(RatsMod.RATLANTIS_SPECIAL).fireResistant()));
	public static final RegistryObject<Item> AVIATOR_HAT = ITEMS.register("aviator_hat", () -> new HatItem(new Item.Properties(), RatsArmorMaterialRegistry.GENERIC_HAT, 0));
	public static final RegistryObject<Item> RAT_TOGA = ITEMS.register("rat_toga", () -> new LoreTagItem(new Item.Properties(), 2));
	public static final RegistryObject<Item> RATGLOVE_PETALS = ITEMS.register("ratglove_petals", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> FERAL_RAT_CLAW = ITEMS.register("feral_rat_claw", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> FERAL_BAGH_NAKHS = ITEMS.register("feral_bagh_nakhs", () -> new BaghNakhsItem(new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> GEM_OF_RATLANTIS = ITEMS.register("gem_of_ratlantis", () -> new Item(new Item.Properties().rarity(RatsMod.RATLANTIS_SPECIAL)));
	public static final RegistryObject<Item> ORATCHALCUM_INGOT = ITEMS.register("oratchalcum_ingot", () -> new Item(new Item.Properties().fireResistant()));
	public static final RegistryObject<Item> RAW_ORATCHALCUM = ITEMS.register("raw_oratchalcum", () -> new Item(new Item.Properties().fireResistant()));
	public static final RegistryObject<Item> ORATCHALCUM_NUGGET = ITEMS.register("oratchalcum_nugget", () -> new Item(new Item.Properties().fireResistant()));
	public static final RegistryObject<ArmorItem> RATLANTIS_HELMET = ITEMS.register("ratlantis_helmet", () -> new RatlantisArmorItem(RatsArmorMaterialRegistry.RATLANTIS, ArmorItem.Type.HELMET, new Item.Properties().stacksTo(1).fireResistant().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<ArmorItem> RATLANTIS_CHESTPLATE = ITEMS.register("ratlantis_chestplate", () -> new RatlantisArmorItem(RatsArmorMaterialRegistry.RATLANTIS, ArmorItem.Type.CHESTPLATE, new Item.Properties().stacksTo(1).fireResistant().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<ArmorItem> RATLANTIS_LEGGINGS = ITEMS.register("ratlantis_leggings", () -> new RatlantisArmorItem(RatsArmorMaterialRegistry.RATLANTIS, ArmorItem.Type.LEGGINGS, new Item.Properties().stacksTo(1).fireResistant().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<ArmorItem> RATLANTIS_BOOTS = ITEMS.register("ratlantis_boots", () -> new RatlantisArmorItem(RatsArmorMaterialRegistry.RATLANTIS, ArmorItem.Type.BOOTS, new Item.Properties().stacksTo(1).fireResistant().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> RATLANTIS_SWORD = ITEMS.register("ratlantis_sword", () -> new RatlantisSwordItem(new Item.Properties().rarity(Rarity.UNCOMMON).fireResistant()));
	public static final RegistryObject<Item> RATLANTIS_PICKAXE = ITEMS.register("ratlantis_pickaxe", () -> new RatlantisToolItem.Pickaxe(new Item.Properties().rarity(Rarity.UNCOMMON).fireResistant()));
	public static final RegistryObject<Item> RATLANTIS_AXE = ITEMS.register("ratlantis_axe", () -> new RatlantisToolItem.Axe(new Item.Properties().rarity(Rarity.UNCOMMON).fireResistant()));
	public static final RegistryObject<Item> RATLANTIS_SHOVEL = ITEMS.register("ratlantis_shovel", () -> new RatlantisToolItem.Shovel(new Item.Properties().rarity(Rarity.UNCOMMON).fireResistant()));
	public static final RegistryObject<Item> RATLANTIS_HOE = ITEMS.register("ratlantis_hoe", () -> new RatlantisToolItem.Hoe(new Item.Properties().rarity(Rarity.UNCOMMON).fireResistant()));
	public static final RegistryObject<Item> RATLANTIS_BOW = ITEMS.register("ratlantis_bow", () -> new RatlantisBowItem(new Item.Properties().durability(1500).rarity(Rarity.UNCOMMON).fireResistant()));
	public static final RegistryObject<Item> ARCANE_TECHNOLOGY = ITEMS.register("arcane_technology", () -> new LoreTagItem(new Item.Properties().rarity(Rarity.RARE).fireResistant(), 2));
	public static final RegistryObject<Item> ANCIENT_SAWBLADE = ITEMS.register("ancient_sawblade", () -> new Item(new Item.Properties().rarity(Rarity.RARE).fireResistant()));
	public static final RegistryObject<Item> RATLANTEAN_FLAME = ITEMS.register("ratlantean_flame", () -> new RatlanteanFlameItem(new Item.Properties()));
	public static final RegistryObject<Item> VIAL_OF_SENTIENCE = ITEMS.register("vial_of_sentience", () -> new VialOfSentienceItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> PSIONIC_RAT_BRAIN = ITEMS.register("psionic_rat_brain", () -> new LoreTagItem(new Item.Properties().rarity(Rarity.RARE).fireResistant(), 2));
	public static final RegistryObject<Item> PIRAT_CUTLASS = ITEMS.register("pirat_cutlass", () -> new PiratCutlassItem(new Item.Properties(), false));
	public static final RegistryObject<Item> CHEESE_CANNONBALL = ITEMS.register("cheese_cannonball", () -> new LoreTagItem(new Item.Properties(), 1));
	public static final RegistryObject<Item> GHOST_PIRAT_HAT = ITEMS.register("ghost_pirat_hat", () -> new HatItem(new Item.Properties().stacksTo(1).fireResistant(), RatsArmorMaterialRegistry.GHOST_HAT, 0));
	public static final RegistryObject<Item> GHOST_PIRAT_ECTOPLASM = ITEMS.register("ghost_pirat_ectoplasm", () -> new Item(new Item.Properties().fireResistant()));
	public static final RegistryObject<Item> GHOST_PIRAT_CUTLASS = ITEMS.register("ghost_pirat_cutlass", () -> new PiratCutlassItem(new Item.Properties().fireResistant(), true));
	public static final RegistryObject<Item> DUTCHRAT_WHEEL = ITEMS.register("dutchrat_wheel", () -> new Item(new Item.Properties().rarity(Rarity.RARE).fireResistant()));
	public static final RegistryObject<Item> MILITARY_HAT = ITEMS.register("military_hat", () -> new HatItem(new Item.Properties().stacksTo(1), RatsArmorMaterialRegistry.GENERIC_HAT, 0));
	public static final RegistryObject<Item> BIPLANE_WING = ITEMS.register("biplane_wing", () -> new Item(new Item.Properties().rarity(Rarity.RARE).fireResistant()));
	public static final RegistryObject<Item> RATFISH = ITEMS.register("ratfish", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationMod(0.35F).build())));
	public static final RegistryObject<Item> RATFISH_BUCKET = ITEMS.register("ratfish_bucket", () -> new MobBucketItem(RatlantisEntityRegistry.RATFISH, () -> Fluids.WATER, () -> SoundEvents.BUCKET_EMPTY_FISH, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
	public static final RegistryObject<Item> RATBOT_BARREL = ITEMS.register("ratbot_barrel", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> CHARGED_RATBOT_BARREL = ITEMS.register("charged_ratbot_barrel", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> RATTLING_GUN = ITEMS.register("rattling_gun", () -> new RattlingGunItem(new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> IDOL_OF_RATLANTIS = ITEMS.register("idol_of_ratlantis", () -> new LoreTagItem(new Item.Properties().rarity(RatsMod.RATLANTIS_SPECIAL).fireResistant(), 1));

	public static final RegistryObject<Item> RAT_UPGRADE_ARCHEOLOGIST = ITEMS.register("rat_upgrade_archeologist", () -> new ArcheologistRatUpgradeItem(new Item.Properties()));
	public static final RegistryObject<Item> RAT_UPGRADE_AUTOMATON_MOUNT = ITEMS.register("rat_upgrade_automaton_mount", () -> new MountRatUpgradeItem<>(new Item.Properties(), 2, 3, RatlantisEntityRegistry.RAT_MOUNT_AUTOMATON));
	public static final RegistryObject<Item> RAT_UPGRADE_BIPLANE_MOUNT = ITEMS.register("rat_upgrade_biplane_mount", () -> new MountRatUpgradeItem<>(new Item.Properties(), 2, 3, RatlantisEntityRegistry.RAT_MOUNT_BIPLANE));
	public static final RegistryObject<Item> RAT_UPGRADE_BASIC_RATLANTEAN = ITEMS.register("rat_upgrade_basic_ratlantean", () -> new BaseRatUpgradeItem(new Item.Properties()));
	public static final RegistryObject<Item> RAT_UPGRADE_FERAL_BITE = ITEMS.register("rat_upgrade_feral_bite", () -> new FeralBiteRatUpgradeItem(new Item.Properties()));
	public static final RegistryObject<Item> RAT_UPGRADE_BUCCANEER = ITEMS.register("rat_upgrade_buccaneer", () -> new BuccaneerRatUpgradeItem(new Item.Properties()));
	public static final RegistryObject<Item> RAT_UPGRADE_RATINATOR = ITEMS.register("rat_upgrade_ratinator", () -> new RatinatorRatUpgradeItem(new Item.Properties()));
	public static final RegistryObject<Item> RAT_UPGRADE_PSYCHIC = ITEMS.register("rat_upgrade_psychic", () -> new PsychicRatUpgradeItem(new Item.Properties().fireResistant()));
	public static final RegistryObject<Item> RAT_UPGRADE_ETHEREAL = ITEMS.register("rat_upgrade_ethereal", () -> new EtherealRatUpgradeItem(new Item.Properties().fireResistant()));
	public static final RegistryObject<Item> RAT_UPGRADE_NONBELIEVER = ITEMS.register("rat_upgrade_nonbeliever", () -> new NonbelieverRatUpgradeItem(new Item.Properties().fireResistant()));
}
