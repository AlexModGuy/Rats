package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.misc.RatsSoundRegistry;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class RatsItemRegistry {

    public static ItemArmor.ArmorMaterial CHEF_TOQUE_ARMOR_MATERIAL = EnumHelper.addArmorMaterial("ChefToque", "rats:cheftoque", 200, new int[]{1, 1, 1, 1}, 100, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0);
    public static ItemArmor.ArmorMaterial HAT_ARMOR_MATERIAL = EnumHelper.addArmorMaterial("PiratHat", "rats:pirat_hat", 200, new int[]{1, 1, 1, 1}, 100, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0);
    public static ItemArmor.ArmorMaterial PLAGUE_MASK_MATERIAL = EnumHelper.addArmorMaterial("PlagueMask", "rats:plague_mask", 100, new int[]{3, 1, 1, 1}, 100, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1);
    public static Item.ToolMaterial PIRAT_CUTLASS_MATERIAL = EnumHelper.addToolMaterial("PiratCutlass", 2, 300, 5.0F, 6.5F, 30);
    public static Item.ToolMaterial BAGHNAKHS_MATERIAL = EnumHelper.addToolMaterial("BaghHakhs", 1, 500, 2.0F, 3.5F, 15);
    public static Item.ToolMaterial PLAGUE_SCYTHE_MATERIAL = EnumHelper.addToolMaterial("PlagueScythe", 2, 1500, 5.0F, 12F, 30);

    @GameRegistry.ObjectHolder(RatsMod.MODID + ":cheese")
    public static Item CHEESE = new ItemGenericFood(3, 0.5F, true, "cheese");

    @GameRegistry.ObjectHolder(RatsMod.MODID + ":raw_rat")
    public static Item RAW_RAT = new ItemGenericFood(3, 0.3F, true, "raw_rat").setPotionEffect(new PotionEffect(MobEffects.HUNGER, 200, 0), 1);

    @GameRegistry.ObjectHolder(RatsMod.MODID + ":cooked_rat")
    public static Item COOKED_RAT = new ItemGenericFood(5, 0.6F, true, "cooked_rat");

    @GameRegistry.ObjectHolder(RatsMod.MODID + ":rat_pelt")
    public static Item RAT_PELT = new ItemGeneric("rat_pelt");

    @GameRegistry.ObjectHolder(RatsMod.MODID + ":cheese_stick")
    public static Item CHEESE_STICK = new ItemCheeseStick();

    @GameRegistry.ObjectHolder(RatsMod.MODID + ":chef_toque")
    public static Item CHEF_TOQUE = new ItemChefToque();

    @GameRegistry.ObjectHolder(RatsMod.MODID + ":rat_flute")
    public static Item RAT_FLUTE = new ItemRatFlute();

    @GameRegistry.ObjectHolder(RatsMod.MODID + ":piper_hat")
    public static Item PIPER_HAT = new ItemPiperHat();

    @GameRegistry.ObjectHolder(RatsMod.MODID + ":string_cheese")
    public static Item STRING_CHEESE = new ItemStringCheese();

    @GameRegistry.ObjectHolder(RatsMod.MODID + ":creative_cheese")
    public static Item CREATIVE_CHEESE = new ItemCreativeCheese();

    @GameRegistry.ObjectHolder(RatsMod.MODID + ":assorted_vegetables")
    public static Item ASSORTED_VEGETABLES = new ItemGenericFood(7, 0.3F, false, "assorted_vegetables");

    @GameRegistry.ObjectHolder(RatsMod.MODID + ":rat_burger")
    public static Item RAT_BURGER = new ItemGenericFood(8, 1.0F, true, "rat_burger");

    @GameRegistry.ObjectHolder(RatsMod.MODID + ":rat_sack")
    public static Item RAT_SACK = new ItemRatSack();

    @GameRegistry.ObjectHolder(RatsMod.MODID + ":rat_capture_net")
    public static Item RAT_CAPTURE_NET = new ItemRatCaptureNet();

    @GameRegistry.ObjectHolder(RatsMod.MODID + ":feathery_wing")
    public static Item FEATHERY_WING = new ItemGeneric("feathery_wing");

    @GameRegistry.ObjectHolder(RatsMod.MODID + ":plastic_waste")
    public static Item PLASTIC_WASTE = new ItemGeneric("plastic_waste");

    @GameRegistry.ObjectHolder(RatsMod.MODID + ":raw_plastic")
    public static Item RAW_PLASTIC = new ItemGeneric("raw_plastic");

    @GameRegistry.ObjectHolder(RatsMod.MODID + ":contaminated_food")
    public static Item CONTAMINATED_FOOD = new ItemGenericFood(2, 0.5F, false, "contaminated_food");

    @GameRegistry.ObjectHolder(RatsMod.MODID + ":purifying_liquid")
    public static Item PURIFYING_LIQUID = new ItemPurifyingLiquid();

    @GameRegistry.ObjectHolder(RatsMod.MODID + ":herb_bundle")
    public static Item HERB_BUNDLE = new ItemPlagueHealer(1, 0.1F, "herb_bundle", 0.1F);

    @GameRegistry.ObjectHolder(RatsMod.MODID + ":treacle")
    public static Item TREACLE = new ItemPlagueHealer(2, 0.6F, "treacle", 0.25F);

    @GameRegistry.ObjectHolder(RatsMod.MODID + ":plague_leech")
    public static Item PLAGUE_LEECH = new ItemPlagueLeech();

    @GameRegistry.ObjectHolder(RatsMod.MODID + ":plague_stew")
    public static Item PLAGUE_STEW = new ItemPlagueHealer(4, 0.3F, "plague_stew", 1.0F);

    @GameRegistry.ObjectHolder(RatsMod.MODID + ":plague_scythe")
    public static Item PLAGUE_SCYTHE = new ItemPlagueScythe();
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":charged_creeper_chunk")
    public static Item CHARGED_CREEPER_CHUNK = new ItemGeneric("charged_creeper_chunk", 1);
    public static Item[] RAT_IGLOOS = new Item[16];
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":rat_water_bottle")
    public static Item RAT_WATER_BOTTLE = new ItemRatDecoration("rat_water_bottle");
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":rat_seed_bowl")
    public static Item RAT_SEED_BOWL = new ItemRatDecoration("rat_seed_bowl");
    public static Item[] RAT_HAMMOCKS = new Item[16];
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":rat_breeding_lantern")
    public static Item RAT_BREEDING_LANTERN = new ItemRatDecoration("rat_breeding_lantern");
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":confit_byaldi")
    public static Item CONFIT_BYALDI = new ItemGenericFood(100, 10F, false, "confit_byaldi").setPotionEffect(new PotionEffect(RatsMod.CONFIT_BYALDI_POTION, 1800, 0), 1);
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":tiny_coin")
    public static Item TINY_COIN = new ItemGeneric("tiny_coin");
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":token_fragment")
    public static Item TOKEN_FRAGMENT = new ItemGeneric("token_fragment");
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":token_piece")
    public static Item TOKEN_PIECE = new ItemGeneric("token_piece");
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":chunky_cheese_token")
    public static Item CHUNKY_CHEESE_TOKEN = new ItemChunkyCheeseToken();
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":archeologist_hat")
    public static Item ARCHEOLOGIST_HAT = new ItemHat("archeologist_hat", 3);
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":farmer_hat")
    public static Item FARMER_HAT = new ItemHat("farmer_hat", 4);
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":fisherman_hat")
    public static Item FISHERMAN_HAT = new ItemHat("fisherman_hat", 4);
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":rat_fez")
    public static Item RAT_FEZ = new ItemHat("rat_fez", 6);
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":top_hat")
    public static Item TOP_HAT = new ItemHat("top_hat", 7);
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":plague_doctor_mask")
    public static Item PLAGUE_DOCTOR_MASK = new ItemHat(PLAGUE_MASK_MATERIAL, "plague_doctor_mask", 5);
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":black_death_mask")
    public static Item BLACK_DEATH_MASK = new ItemHat(PLAGUE_MASK_MATERIAL, "black_death_mask", 5);
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":plague_essence")
    public static Item PLAGUE_ESSENCE = new ItemGeneric("plague_essence", 1);
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":plague_doctorate")
    public static Item PLAGUE_DOCTORATE = new ItemGeneric("plague_doctorate", 1);
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":rat_toga")
    public static Item RAT_TOGA = new ItemGeneric("rat_toga", 2);
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":ratglove_petals")
    public static Item RATGLOVE_PETALS = new ItemGeneric("ratglove_petals");
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":feral_rat_claw")
    public static Item FERAL_RAT_CLAW = new ItemGeneric("feral_rat_claw");
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":feral_bagh_nakhs")
    public static Item FERAL_BAGH_NAKHS = new ItemBaghNakhs();
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":gem_of_ratlantis")
    public static Item GEM_OF_RATLANTIS = new ItemGeneric("gem_of_ratlantis");
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":arcane_technology")
    public static Item ARCANE_TECHNOLOGY = new ItemGeneric("arcane_technology", 2);
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":ratlantean_flame")
    public static Item RATLANTEAN_FLAME = new ItemRatlanteanFlame();
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":vial_of_sentience")
    public static Item VIAL_OF_SENTIENCE = new ItemVialOfSentience();
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":psionic_rat_brain")
    public static Item PSIONIC_RAT_BRAIN = new ItemGeneric("psionic_rat_brain", 2);
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":pirat_hat")
    public static Item PIRAT_HAT = new ItemHat("pirat_hat", 2);
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":pirat_cutlass")
    public static Item PIRAT_CUTLASS = new ItemPiratCutlass();
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":cheese_cannonball")
    public static Item CHEESE_CANNONBALL = new ItemGeneric("cheese_cannonball", 1);
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":music_disc_mice_on_venus")
    public static Item MUSIC_DISC_MICE_ON_VENUS = new ItemRatDisc("music_disc_mice_on_venus", "mice_on_venus", RatsSoundRegistry.MICE_ON_VENUS);
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":music_disc_living_mice")
    public static Item MUSIC_DISC_LIVING_MICE = new ItemRatDisc("music_disc_living_mice", "living_mice", RatsSoundRegistry.LIVING_MICE);
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":idol_of_ratlantis")
    public static Item IDOL_OF_RATLANTIS = new ItemGeneric("idol_of_ratlantis", 1);
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":rat_upgrade_basic")
    public static Item RAT_UPGRADE_BASIC = new ItemRatUpgrade("rat_upgrade_basic");
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":rat_upgrade_speed")
    public static Item RAT_UPGRADE_SPEED = new ItemRatUpgrade("rat_upgrade_speed");
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":rat_upgrade_platter")
    public static Item RAT_UPGRADE_PLATTER = new ItemRatUpgrade("rat_upgrade_platter");
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":rat_upgrade_health")
    public static Item RAT_UPGRADE_HEALTH = new ItemRatUpgrade("rat_upgrade_health");
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":rat_upgrade_armor")
    public static Item RAT_UPGRADE_ARMOR = new ItemRatUpgrade("rat_upgrade_armor");
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":rat_upgrade_strength")
    public static Item RAT_UPGRADE_STRENGTH = new ItemRatUpgrade("rat_upgrade_strength");
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":rat_upgrade_warrior")
    public static Item RAT_UPGRADE_WARRIOR = new ItemRatUpgrade("rat_upgrade_warrior", 2, 3);
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":rat_upgrade_god")
    public static Item RAT_UPGRADE_GOD = new ItemRatUpgrade("rat_upgrade_god", 3, 3);
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":rat_upgrade_chef")
    public static Item RAT_UPGRADE_CHEF = new ItemRatUpgrade("rat_upgrade_chef", 2, 2);
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":rat_upgrade_crafting")
    public static Item RAT_UPGRADE_CRAFTING = new ItemRatUpgrade("rat_upgrade_crafting", 0, 2);
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":rat_upgrade_blacklist")
    public static Item RAT_UPGRADE_BLACKLIST = new ItemRatListUpgrade("rat_upgrade_blacklist", 0, 3, false);
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":rat_upgrade_whitelist")
    public static Item RAT_UPGRADE_WHITELIST = new ItemRatListUpgrade("rat_upgrade_whitelist", 0, 3, true);
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":rat_upgrade_flight")
    public static Item RAT_UPGRADE_FLIGHT = new ItemRatUpgrade("rat_upgrade_flight", 0, 3);
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":rat_upgrade_ender")
    public static Item RAT_UPGRADE_ENDER = new ItemRatUpgrade("rat_upgrade_ender", 1, 3);
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":rat_upgrade_lumberjack")
    public static Item RAT_UPGRADE_LUMBERJACK = new ItemRatUpgrade("rat_upgrade_lumberjack", 1, 1);
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":rat_upgrade_miner")
    public static Item RAT_UPGRADE_MINER = new ItemRatListUpgrade("rat_upgrade_miner", 1, 3, true);
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":rat_upgrade_farmer")
    public static Item RAT_UPGRADE_FARMER = new ItemRatUpgrade("rat_upgrade_farmer", 1, 2);
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":rat_upgrade_fisherman")
    public static Item RAT_UPGRADE_FISHERMAN = new ItemRatUpgrade("rat_upgrade_fisherman", 1, 2);
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":rat_upgrade_asbestos")
    public static Item RAT_UPGRADE_ASBESTOS = new ItemRatUpgrade("rat_upgrade_asbestos", 0, 4);
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":rat_upgrade_underwater")
    public static Item RAT_UPGRADE_UNDERWATER = new ItemRatUpgrade("rat_upgrade_underwater", 0, 3);
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":rat_upgrade_poison")
    public static Item RAT_UPGRADE_POISON = new ItemRatUpgrade("rat_upgrade_poison", 0, 4);
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":rat_upgrade_damage_protection")
    public static Item RAT_UPGRADE_DAMAGE_PROTECTION = new ItemRatUpgrade("rat_upgrade_damage_protection", 2, 4);
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":rat_upgrade_ore_doubling")
    public static Item RAT_UPGRADE_ORE_DOUBLING = new ItemRatUpgradeOreDoubling();
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":rat_upgrade_basic_energy")
    public static Item RAT_UPGRADE_BASIC_ENERGY = new ItemRatUpgradeEnergy("rat_upgrade_basic_energy", 0, 2);
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":rat_upgrade_advanced_energy")
    public static Item RAT_UPGRADE_ADVANCED_ENERGY = new ItemRatUpgradeEnergy("rat_upgrade_advanced_energy", 1, 2);
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":rat_upgrade_elite_energy")
    public static Item RAT_UPGRADE_ELITE_ENERGY = new ItemRatUpgradeEnergy("rat_upgrade_elite_energy", 2, 2);
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":rat_upgrade_extreme_energy")
    public static Item RAT_UPGRADE_EXTREME_ENERGY = new ItemRatUpgradeEnergy("rat_upgrade_extreme_energy", 3, 2);
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":rat_upgrade_bucket")
    public static Item RAT_UPGRADE_BUCKET = new ItemRatUpgradeBucket("rat_upgrade_bucket", 0, 2);
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":rat_upgrade_big_bucket")
    public static Item RAT_UPGRADE_BIG_BUCKET = new ItemRatUpgradeBucket("rat_upgrade_big_bucket", 2, 2);
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":rat_upgrade_milker")
    public static Item RAT_UPGRADE_MILKER = new ItemRatUpgradeBucket("rat_upgrade_milker", 0, 3);
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":rat_upgrade_aristocrat")
    public static Item RAT_UPGRADE_ARISTOCRAT = new ItemRatUpgrade("rat_upgrade_aristocrat", 1, 1);
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":rat_upgrade_archeologist")
    public static Item RAT_UPGRADE_ARCHEOLOGIST = new ItemRatUpgrade("rat_upgrade_archeologist", 2, 1);
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":rat_upgrade_basic_ratlantean")
    public static Item RAT_UPGRADE_BASIC_RATLANTEAN = new ItemRatUpgrade("rat_upgrade_basic_ratlantean");
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":rat_upgrade_feral_bite")
    public static Item RAT_UPGRADE_FERAL_BITE = new ItemRatUpgrade("rat_upgrade_feral_bite");
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":rat_upgrade_buccaneer")
    public static Item RAT_UPGRADE_BUCCANEER = new ItemRatUpgrade("rat_upgrade_buccaneer", 1, 2);
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":rat_upgrade_ratinator")
    public static Item RAT_UPGRADE_RATINATOR = new ItemRatUpgrade("rat_upgrade_ratinator", 2, 3);
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":rat_upgrade_psychic")
    public static Item RAT_UPGRADE_PSYCHIC = new ItemRatUpgrade("rat_upgrade_psychic", 2, 3);
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":rat_upgrade_nonbeliever")
    public static Item RAT_UPGRADE_NONBELIEVER = new ItemRatUpgrade("rat_upgrade_nonbeliever", 4, 4);
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":rat_upgrade_combined")
    public static Item RAT_UPGRADE_COMBINED = new ItemRatCombinedUpgrade("rat_upgrade_combined");
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":rat_upgrade_combined_creative")
    public static Item RAT_UPGRADE_COMBINED_CREATIVE = new ItemRatCombinedUpgrade("rat_upgrade_combined_creative");
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":rat_nugget")
    public static Item RAT_NUGGET = new ItemGeneric("rat_nugget");
    @GameRegistry.ObjectHolder(RatsMod.MODID + ":rat_nugget_ore")
    public static Item RAT_NUGGET_ORE = new ItemRatNuggetOre();
    static {
        for (int i = 0; i < 16; i++) {
            RAT_IGLOOS[i] = new ItemRatIgloo(EnumDyeColor.byMetadata(i));
        }
    }

    static {
        for (int i = 0; i < 16; i++) {
            RAT_HAMMOCKS[i] = new ItemRatHammock(EnumDyeColor.byMetadata(i));
        }
    }

}
