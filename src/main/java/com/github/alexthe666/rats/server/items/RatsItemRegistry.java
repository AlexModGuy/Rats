package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.citadel.server.item.CustomArmorMaterial;
import com.github.alexthe666.citadel.server.item.CustomToolMaterial;
import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.RatsEntityRegistry;
import com.github.alexthe666.rats.server.misc.RatsSoundRegistry;
import com.github.alexthe666.rats.server.recipes.RatsRecipeRegistry;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.DyeColor;
import net.minecraft.item.FishBucketItem;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.lang.reflect.Field;

@Mod.EventBusSubscriber(modid = RatsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RatsItemRegistry {

    public static CustomArmorMaterial CHEF_TOQUE_ARMOR_MATERIAL = new CustomArmorMaterial("ChefToque", 200, new int[]{1, 1, 1, 1}, 100, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0);
    public static CustomArmorMaterial HAT_ARMOR_MATERIAL = new CustomArmorMaterial("Hat", 200, new int[]{1, 1, 1, 1}, 100, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0);
    public static CustomArmorMaterial PLAGUE_MASK_MATERIAL = new CustomArmorMaterial("PlagueMaskk", 100, new int[]{3, 1, 1, 1}, 100, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1);
    public static CustomToolMaterial PIRAT_CUTLASS_MATERIAL = new CustomToolMaterial("PiratCutlass", 2, 300, 5.0F, 6.5F, 30);
    public static CustomToolMaterial BAGHNAKHS_MATERIAL = new CustomToolMaterial("BaghHakhs", 1, 500, 2.0F, 3.5F, 15);
    public static CustomToolMaterial PLAGUE_SCYTHE_MATERIAL = new CustomToolMaterial("PlagueScythe", 2, 1500, 5.0F, 12F, 30);

    public static final Item CHEESE = new ItemGenericFood(3, 0.5F, true, false, false, "cheese");

    public static final Item RAW_RAT = new ItemGenericFood(3, 0.3F, true, false, false, "raw_rat");

    public static final Item COOKED_RAT = new ItemGenericFood(5, 0.6F, true, false, false, "cooked_rat");

    public static final Item RAT_PELT = new ItemGeneric("rat_pelt");

    public static final Item CHEESE_STICK = new ItemCheeseStick();

    public static final Item CHEF_TOQUE = new ItemChefToque();

    public static final Item RAT_FLUTE = new ItemRatFlute();

    public static final Item PIPER_HAT = new ItemPiperHat();

    public static final Item STRING_CHEESE = new ItemStringCheese();

    public static final Item CREATIVE_CHEESE = new ItemCreativeCheese();

    public static final Item ASSORTED_VEGETABLES = new ItemGenericFood(7, 0.3F, false, false, false, "assorted_vegetables");

    public static final Item RAT_BURGER = new ItemGenericFood(8, 1.0F, true, false, false, "rat_burger");

    public static final Item RAT_SACK = new ItemRatSack();

    public static final Item RAT_ARROW = new ItemRatArrow();

    public static final Item RAT_CAPTURE_NET = new ItemRatCaptureNet();

    public static final Item FEATHERY_WING = new ItemGeneric("feathery_wing");

    public static final Item DRAGON_WING = new ItemGeneric("dragon_wing");

    public static final Item PLASTIC_WASTE = new ItemGeneric("plastic_waste");

    public static final Item RAW_PLASTIC = new ItemGeneric("raw_plastic");

    public static final Item CONTAMINATED_FOOD = new ItemGenericFood(2, 0.5F, false, false, false, "contaminated_food");

    public static final Item PURIFYING_LIQUID = new ItemPurifyingLiquid();

    public static final Item HERB_BUNDLE = new ItemPlagueHealer(1, 0.1F, "herb_bundle", 0.1F);

    public static final Item TREACLE = new ItemPlagueHealer(2, 0.6F, "treacle", 0.25F);

    public static final Item PLAGUE_LEECH = new ItemPlagueLeech();

    public static final Item PLAGUE_STEW = new ItemPlagueHealer(4, 0.3F, "plague_stew", 1.0F);

    public static final Item PLAGUE_SCYTHE = new ItemPlagueScythe();

    public static final Item CHARGED_CREEPER_CHUNK = new ItemGeneric("charged_creeper_chunk", 1);

    public static final Item[] RAT_IGLOOS = new Item[16];

    public static final Item RAT_WATER_BOTTLE = new ItemRatDecoration("rat_water_bottle");

    public static final Item RAT_SEED_BOWL = new ItemRatDecoration("rat_seed_bowl");

    public static final Item[] RAT_HAMMOCKS = new Item[16];

    public static final Item RAT_BREEDING_LANTERN = new ItemRatDecoration("rat_breeding_lantern");

    public static final Item CONFIT_BYALDI = new ItemGenericFood(100, 10F, false, false, true, "confit_byaldi");

    public static final Item POTATO_PANCAKE = new ItemGenericFood(4, 0.5F, false, false, true, "potato_pancake");

    public static final Item LITTLE_BLACK_SQUASH_BALLS = new ItemGeneric("little_black_squash_balls");

    public static final Item LITTLE_BLACK_WORM = new ItemGeneric("little_black_worm");

    public static final Item CENTIPEDE = new ItemGeneric("centipede");

    public static final Item POTATO_KNISHES = new ItemGenericFood(100, 10F, false, false, true, "potato_kinishes");

    public static final Item TINY_COIN = new ItemGeneric("tiny_coin");

    public static final Item TOKEN_FRAGMENT = new ItemGeneric("token_fragment");

    public static final Item TOKEN_PIECE = new ItemGeneric("token_piece");

    public static final Item CHUNKY_CHEESE_TOKEN = new ItemChunkyCheeseToken();

    public static final Item ARCHEOLOGIST_HAT = new ItemHat("archeologist_hat", 3);

    public static final Item FARMER_HAT = new ItemHat("farmer_hat", 4);

    public static final Item FISHERMAN_HAT = new ItemHat("fisherman_hat", 4);

    public static final Item RAT_FEZ = new ItemHat("rat_fez", 6);

    public static final Item TOP_HAT = new ItemHat("top_hat", 7);

    public static final Item SANTA_HAT = new ItemHat("santa_hat", 8);

    public static final Item HALO_HAT = new ItemHat("halo_hat", 10);

    public static final Item PLAGUE_DOCTOR_MASK = new ItemHat(PLAGUE_MASK_MATERIAL, "plague_doctor_mask", 5);

    public static final Item BLACK_DEATH_MASK = new ItemHat(PLAGUE_MASK_MATERIAL, "black_death_mask", 5);

    public static final Item PLAGUE_ESSENCE = new ItemGeneric("plague_essence", 1);

    public static final Item PLAGUE_DOCTORATE = new ItemGeneric("plague_doctorate", 1);

    public static final Item RAT_TOGA = new ItemGeneric("rat_toga", 2);

    public static final Item RATGLOVE_PETALS = new ItemGeneric("ratglove_petals");

    public static final Item FERAL_RAT_CLAW = new ItemGeneric("feral_rat_claw");

    public static final Item FERAL_BAGH_NAKHS = new ItemBaghNakhs();

    public static final Item GEM_OF_RATLANTIS = new ItemGeneric("gem_of_ratlantis");

    public static final Item ARCANE_TECHNOLOGY = new ItemGeneric("arcane_technology", 2);

    public static final Item ANCIENT_SAWBLADE = new ItemGeneric("ancient_sawblade");

    public static final Item RATLANTEAN_FLAME = new ItemRatlanteanFlame();

    public static final Item VIAL_OF_SENTIENCE = new ItemVialOfSentience();

    public static final Item PSIONIC_RAT_BRAIN = new ItemGeneric("psionic_rat_brain", 2);

    public static final Item PIRAT_HAT = new ItemHat("pirat_hat", 2);

    public static final Item PIRAT_CUTLASS = new ItemPiratCutlass(false);

    public static final Item CHEESE_CANNONBALL = new ItemGeneric("cheese_cannonball", 1);

    public static final Item GHOST_PIRAT_HAT = new ItemHat("ghost_pirat_hat", 9);

    public static final Item GHOST_PIRAT_ECTOPLASM = new ItemGeneric("ghost_pirat_ectoplasm");

    public static final Item GHOST_PIRAT_CUTLASS = new ItemPiratCutlass(true);

    public static final Item DUTCHRAT_WHEEL = new ItemGeneric("dutchrat_wheel");

    public static final Item RATFISH = new ItemGenericFood(1, 0.35F, false, false, false, "ratfish");

    public static final Item RATFISH_BUCKET = new FishBucketItem(RatsEntityRegistry.RATFISH, Fluids.WATER, (new Item.Properties()).maxStackSize(1).group(RatsMod.TAB)).setRegistryName("ratfish_bucket");

    public static final Item RAT_DIAMOND = new ItemGeneric("rat_diamond");

    public static final Item MUSIC_DISC_MICE_ON_VENUS = new ItemRatDisc("music_disc_mice_on_venus", RatsSoundRegistry.MICE_ON_VENUS);

    public static final Item MUSIC_DISC_LIVING_MICE = new ItemRatDisc("music_disc_living_mice", RatsSoundRegistry.LIVING_MICE);

    public static final Item IDOL_OF_RATLANTIS = new ItemGeneric("idol_of_ratlantis", 1);

    public static final Item RAT_UPGRADE_BASIC = new ItemRatUpgrade("rat_upgrade_basic");

    public static final Item RAT_UPGRADE_CREATIVE = new ItemRatUpgrade("rat_upgrade_creative", 3, 0);

    public static final Item RAT_UPGRADE_JURY_RIGGED = new ItemRatUpgradeJuryRigged("rat_upgrade_jury_rigged");

    public static final Item RAT_UPGRADE_SPEED = new ItemRatUpgrade("rat_upgrade_speed");

    public static final Item RAT_UPGRADE_PLATTER = new ItemRatUpgrade("rat_upgrade_platter");

    public static final Item RAT_UPGRADE_HEALTH = new ItemRatUpgrade("rat_upgrade_health");

    public static final Item RAT_UPGRADE_ARMOR = new ItemRatUpgrade("rat_upgrade_armor");

    public static final Item RAT_UPGRADE_STRENGTH = new ItemRatUpgrade("rat_upgrade_strength");

    public static final Item RAT_UPGRADE_WARRIOR = new ItemRatUpgrade("rat_upgrade_warrior", 2, 3);

    public static final Item RAT_UPGRADE_GOD = new ItemRatUpgrade("rat_upgrade_god", 3, 3);

    public static final Item RAT_UPGRADE_CHEF = new ItemRatUpgrade("rat_upgrade_chef", 2, 2);

    public static final Item RAT_UPGRADE_CRAFTING = new ItemRatUpgrade("rat_upgrade_crafting", 0, 2);

    public static final Item RAT_UPGRADE_BLACKLIST = new ItemRatListUpgrade("rat_upgrade_blacklist", 0, 3, false);

    public static final Item RAT_UPGRADE_WHITELIST = new ItemRatListUpgrade("rat_upgrade_whitelist", 0, 3, true);

    public static final Item RAT_UPGRADE_FLIGHT = new ItemRatUpgrade("rat_upgrade_flight", 0, 3);

    public static final Item RAT_UPGRADE_DRAGON = new ItemRatUpgrade("rat_upgrade_dragon", 1, 5);

    public static final Item RAT_UPGRADE_AQUATIC = new ItemRatUpgrade("rat_upgrade_aquatic", 0, 3);

    public static final Item RAT_UPGRADE_ENDER = new ItemRatUpgrade("rat_upgrade_ender", 1, 3);

    public static final Item RAT_UPGRADE_LUMBERJACK = new ItemRatUpgrade("rat_upgrade_lumberjack", 1, 1);

    public static final Item RAT_UPGRADE_MINER = new ItemRatListUpgrade("rat_upgrade_miner", 1, 3, true);

    public static final Item RAT_UPGRADE_FARMER = new ItemRatUpgrade("rat_upgrade_farmer", 1, 3);

    public static final Item RAT_UPGRADE_BREEDER = new ItemRatUpgrade("rat_upgrade_breeder", 1, 2);

    public static final Item RAT_UPGRADE_FISHERMAN = new ItemRatUpgrade("rat_upgrade_fisherman", 1, 2);

    public static final Item RAT_UPGRADE_ASBESTOS = new ItemRatUpgrade("rat_upgrade_asbestos", 0, 4);

    public static final Item RAT_UPGRADE_UNDERWATER = new ItemRatUpgrade("rat_upgrade_underwater", 0, 3);

    public static final Item RAT_UPGRADE_POISON = new ItemRatUpgrade("rat_upgrade_poison", 0, 4);

    public static final Item RAT_UPGRADE_DAMAGE_PROTECTION = new ItemRatUpgrade("rat_upgrade_damage_protection", 2, 4);

    public static final Item RAT_UPGRADE_ORE_DOUBLING = new ItemRatUpgradeOreDoubling();

    public static final Item RAT_UPGRADE_BASIC_ENERGY = new ItemRatUpgradeEnergy("rat_upgrade_basic_energy", 0, 2);

    public static final Item RAT_UPGRADE_ADVANCED_ENERGY = new ItemRatUpgradeEnergy("rat_upgrade_advanced_energy", 1, 2);

    public static final Item RAT_UPGRADE_ELITE_ENERGY = new ItemRatUpgradeEnergy("rat_upgrade_elite_energy", 2, 2);

    public static final Item RAT_UPGRADE_EXTREME_ENERGY = new ItemRatUpgradeEnergy("rat_upgrade_extreme_energy", 3, 2);

    public static final Item RAT_UPGRADE_BUCKET = new ItemRatUpgradeBucket("rat_upgrade_bucket", 0, 2);

    public static final Item RAT_UPGRADE_BIG_BUCKET = new ItemRatUpgradeBucket("rat_upgrade_big_bucket", 2, 2);

    public static final Item RAT_UPGRADE_MILKER = new ItemRatUpgradeBucket("rat_upgrade_milker", 0, 3);

    public static final Item RAT_UPGRADE_SHEARS = new ItemRatUpgradeBucket("rat_upgrade_shears", 0, 1);

    public static final Item RAT_UPGRADE_ARISTOCRAT = new ItemRatUpgrade("rat_upgrade_aristocrat", 1, 1);

    public static final Item RAT_UPGRADE_TNT = new ItemRatUpgrade("rat_upgrade_tnt", 1, 2);

    public static final Item RAT_UPGRADE_TNT_SURVIVOR = new ItemRatUpgrade("rat_upgrade_tnt_survivor", 2, 4);

    public static final Item RAT_UPGRADE_PLACER = new ItemRatUpgrade("rat_upgrade_placer", 2, 2);

    public static final Item RAT_UPGRADE_VOODOO = new ItemRatUpgrade("rat_upgrade_voodoo", 2, 4);

    public static final Item RAT_UPGRADE_GEMCUTTER = new ItemRatUpgrade("rat_upgrade_gemcutter", 1, 1);

    public static final Item RAT_UPGRADE_ENCHANTER = new ItemRatUpgrade("rat_upgrade_enchanter", 1, 2);

    public static final Item RAT_UPGRADE_DISENCHANTER = new ItemRatUpgrade("rat_upgrade_disenchanter", 1, 1);

    public static final Item RAT_UPGRADE_CHRISTMAS = new ItemRatUpgrade("rat_upgrade_christmas", 2, 3);

    public static final Item RAT_UPGRADE_NO_FLUTE = new ItemRatUpgrade("rat_upgrade_no_flute", 0, 1);

    public static final Item RAT_UPGRADE_CARRAT = new ItemRatUpgrade("rat_upgrade_carrat", 1, 1);

    public static final Item RAT_UPGRADE_ANGEL = new ItemRatUpgrade("rat_upgrade_angel", 2, 2);

    public static final Item RAT_UPGRADE_ARCHEOLOGIST = new ItemRatUpgrade("rat_upgrade_archeologist", 2, 1);

    public static final Item RAT_UPGRADE_BASIC_RATLANTEAN = new ItemRatUpgrade("rat_upgrade_basic_ratlantean");

    public static final Item RAT_UPGRADE_FERAL_BITE = new ItemRatUpgrade("rat_upgrade_feral_bite");

    public static final Item RAT_UPGRADE_BUCCANEER = new ItemRatUpgrade("rat_upgrade_buccaneer", 1, 2);

    public static final Item RAT_UPGRADE_RATINATOR = new ItemRatUpgrade("rat_upgrade_ratinator", 2, 3);

    public static final Item RAT_UPGRADE_PSYCHIC = new ItemRatUpgrade("rat_upgrade_psychic", 2, 3);

    public static final Item RAT_UPGRADE_ETHEREAL = new ItemRatUpgrade("rat_upgrade_ethereal", 1, 2);

    public static final Item RAT_UPGRADE_NONBELIEVER = new ItemRatUpgrade("rat_upgrade_nonbeliever", 4, 4);

    public static final Item RAT_UPGRADE_COMBINED = new ItemRatUpgradeCombined("rat_upgrade_combined");

    public static final Item RAT_UPGRADE_COMBINED_CREATIVE = new ItemRatUpgradeCombined("rat_upgrade_combined_creative");

    public static final Item RAT_NUGGET = new ItemGeneric("rat_nugget");

    public static final Item RAT_NUGGET_ORE = new ItemRatNuggetOre();

    static {
        for (int i = 0; i < 16; i++) {
            RAT_IGLOOS[i] = new ItemRatIgloo(DyeColor.byId(i));
        }
    }

    static {
        for (int i = 0; i < 16; i++) {
            RAT_HAMMOCKS[i] = new ItemRatHammock(DyeColor.byId(i));
        }
    }

    @SubscribeEvent
    public static void registerItem(RegistryEvent.Register<Item> event) {
        try {
            for (Field f : RatsItemRegistry.class.getDeclaredFields()) {
                Object obj = f.get(null);
                if (obj instanceof Item && ((Item) obj).getRegistryName() != null) {
                    if ((obj != RatsItemRegistry.PLASTIC_WASTE && obj != RatsItemRegistry.RAW_PLASTIC) || !RatConfig.disablePlastic) {
                        event.getRegistry().register((Item) obj);
                    }
                } else if (obj instanceof Item[]) {
                    for (Item item : (Item[]) obj) {
                        event.getRegistry().register((item));

                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        RatsUpgradeConflictRegistry.init();
    }
}
