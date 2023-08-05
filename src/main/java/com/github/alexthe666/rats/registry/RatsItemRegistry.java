package com.github.alexthe666.rats.registry;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.data.tags.RatsBannerPatternTags;
import com.github.alexthe666.rats.server.items.*;
import com.github.alexthe666.rats.server.items.upgrades.*;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;

@SuppressWarnings({"unchecked", "rawtypes"})
public class RatsItemRegistry {

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, RatsMod.MODID);

	//Currently unused item textures:
	//blue cheese stick
	//blueprint
	//confetti launcher
	//hardhat
	//holocards
	//rat credit card
	//rat diamond
	//rat upgrade fragment
	//rat upgrade nuke
	//rat upgrade redstone
	//rat upgrade shield
	//rat upgrade shield max
	//ratlantean arrow
	//stick of cheese

	public static final RegistryObject<Item> CHEESE = ITEMS.register("cheese", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationMod(0.5F).build())));
	public static final RegistryObject<Item> RAW_RAT = ITEMS.register("raw_rat", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationMod(0.3F).meat().build())));
	public static final RegistryObject<Item> COOKED_RAT = ITEMS.register("cooked_rat", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.6F).meat().build())));
	public static final RegistryObject<Item> RAT_PELT = ITEMS.register("rat_pelt", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> RAT_PAW = ITEMS.register("rat_paw", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> CHEESE_STICK = ITEMS.register("cheese_stick", () -> new RatStaffItem(new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> RADIUS_STICK = ITEMS.register("radius_stick", () -> new RatStaffItem(new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> PATROL_STICK = ITEMS.register("patrol_stick", () -> new RatStaffItem(new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> RAT_WHISTLE = ITEMS.register("rat_whistle", () -> new RatWhistleItem(new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> RAT_FLUTE = ITEMS.register("rat_flute", () -> new RatFluteItem(new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> GILDED_RAT_FLUTE = ITEMS.register("gilded_rat_flute", () -> new GildedRatFluteItem(new Item.Properties().durability(100)));
	public static final RegistryObject<Item> CHEF_TOQUE = ITEMS.register("chef_toque", () -> new HatItem(new Item.Properties().stacksTo(1), RatsArmorMaterialRegistry.CHEF_TOQUE, 0));
	public static final RegistryObject<Item> PIPER_HAT = ITEMS.register("piper_hat", () -> new HatItem(new Item.Properties().stacksTo(1), RatsArmorMaterialRegistry.PIPER_HAT, 1));
	public static final RegistryObject<Item> STRING_CHEESE = ITEMS.register("string_cheese", () -> new LoreTagItem(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationMod(0.4F).fast().build()), 1));
	public static final RegistryObject<Item> CREATIVE_CHEESE = ITEMS.register("creative_cheese", () -> new LoreTagItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).fireResistant(), 1, true));
	public static final RegistryObject<Item> BLUE_CHEESE = ITEMS.register("blue_cheese", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationMod(0.5F).build())));
	public static final RegistryObject<Item> NETHER_CHEESE = ITEMS.register("nether_cheese", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationMod(0.5F).build()).fireResistant()) {
		@Override
		public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
			if (!entity.fireImmune()) {
				entity.setSecondsOnFire(5);
			}
			return super.finishUsingItem(stack, level, entity);
		}
	});
	public static final RegistryObject<Item> ASSORTED_VEGETABLES = ITEMS.register("assorted_vegetables", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationMod(0.3F).build())));
	public static final RegistryObject<Item> RAT_BURGER = ITEMS.register("rat_burger", () -> new LoreTagItem(new Item.Properties().food(new FoodProperties.Builder().nutrition(8).saturationMod(1.0F).meat().build()), 1));
	public static final RegistryObject<Item> RAT_SACK = ITEMS.register("rat_sack", () -> new RatSackItem(new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> RAT_ARROW = ITEMS.register("rat_arrow", () -> new RatArrowItem(new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> RAT_CAPTURE_NET = ITEMS.register("rat_capture_net", () -> new RatCaptureNetItem(new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> FEATHERY_WING = ITEMS.register("feathery_wing", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> DRAGON_WING = ITEMS.register("dragon_wing", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> BEE_WING = ITEMS.register("bee_wing", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> PLASTIC_WASTE = ITEMS.register("plastic_waste", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> RAW_PLASTIC = ITEMS.register("raw_plastic", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> CONTAMINATED_FOOD = ITEMS.register("contaminated_food", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationMod(0.5F)
			.effect(() -> new MobEffectInstance(RatsEffectRegistry.PLAGUE.get(), 2400), 0.3F)
			.effect(() -> new MobEffectInstance(MobEffects.POISON, 2400), 0.3F)
			.effect(() -> new MobEffectInstance(MobEffects.HUNGER, 2400), 0.3F)
			.effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 2400), 0.3F)
			.effect(() -> new MobEffectInstance(MobEffects.WEAKNESS, 2400), 0.3F)
			.effect(() -> new MobEffectInstance(MobEffects.WITHER, 2400), 0.3F)
			.effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 2400), 0.3F)
			.effect(() -> new MobEffectInstance(MobEffects.UNLUCK, 2400), 1.0F)
			.build())));
	public static final RegistryObject<Item> PURIFYING_LIQUID = ITEMS.register("purifying_liquid", () -> new PurifyingLiquidItem(new Item.Properties().stacksTo(1), false));
	public static final RegistryObject<Item> CRIMSON_FLUID = ITEMS.register("crimson_liquid", () -> new PurifyingLiquidItem(new Item.Properties().stacksTo(1), true));
	public static final RegistryObject<Item> PLAGUE_ESSENCE = ITEMS.register("plague_essence", () -> new LoreTagItem(new Item.Properties().rarity(Rarity.UNCOMMON), 1));
	public static final RegistryObject<Item> PLAGUE_DOCTORATE = ITEMS.register("plague_doctorate", () -> new LoreTagItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON), 1));
	public static final RegistryObject<Item> HERB_BUNDLE = ITEMS.register("herb_bundle", () -> new PlagueHealerItem(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationMod(0.1F).build()), 0.1F));
	public static final RegistryObject<Item> TREACLE = ITEMS.register("treacle", () -> new PlagueHealerItem(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationMod(0.6F).build()), 0.25F));
	public static final RegistryObject<Item> PLAGUE_LEECH = ITEMS.register("plague_leech", () -> new PlagueLeechItem(new Item.Properties()));
	public static final RegistryObject<Item> PLAGUE_STEW = ITEMS.register("plague_stew", () -> new PlagueHealerItem(new Item.Properties().stacksTo(1).craftRemainder(Items.BOWL).food(new FoodProperties.Builder().nutrition(4).saturationMod(0.3F).build()), 1.0F));
	public static final RegistryObject<Item> RAT_SKULL = ITEMS.register("rat_skull", () -> new LoreTagItem(new Item.Properties(), 1));
	public static final RegistryObject<Item> GOLDEN_RAT_SKULL = ITEMS.register("golden_rat_skull", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> CORRUPT_RAT_SKULL = ITEMS.register("corrupt_rat_skull", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> PLAGUE_TOME = ITEMS.register("plague_tome", () -> new LoreTagItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE).fireResistant(), 2, true));
	public static final RegistryObject<Item> PLAGUE_SCYTHE = ITEMS.register("plague_scythe", () -> new PlagueScytheItem(new Item.Properties().rarity(Rarity.RARE).fireResistant()));
	public static final RegistryObject<Item> FILTH = ITEMS.register("filth", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> FILTH_CORRUPTION = ITEMS.register("filth_corruption", () -> new LoreTagItem(new Item.Properties().rarity(Rarity.RARE), 1));
	public static final RegistryObject<Item> TANGLED_RAT_TAILS = ITEMS.register("tangled_rat_tails", () -> new Item(new Item.Properties().rarity(Rarity.RARE)));
	public static final RegistryObject<Item> CHARGED_CREEPER_CHUNK = ITEMS.register("charged_creeper_chunk", () -> new LoreTagItem(new Item.Properties().rarity(Rarity.UNCOMMON), 1));
	public static final RegistryObject<Item> RATBOW_ESSENCE = ITEMS.register("ratbow_essence", () -> new LoreTagItem(new Item.Properties(), 1));
	public static final RegistryObject<Item> RAT_PAPERS = ITEMS.register("rat_papers", () -> new RatPapersItem(new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item>[] RAT_HAMMOCKS = new RegistryObject[16];
	public static final RegistryObject<Item>[] RAT_TUBES = new RegistryObject[16];
	public static final RegistryObject<Item>[] RAT_IGLOOS = new RegistryObject[16];

	static {
		for (int i = 0; i < 16; i++) {
			int finalI = i;
			RAT_TUBES[i] = ITEMS.register("rat_tube_" + DyeColor.byId(i).getName(), () -> new RatTubeItem(new Item.Properties(), DyeColor.byId(finalI)));
			RAT_IGLOOS[i] = ITEMS.register("rat_igloo_" + DyeColor.byId(i).getName(), () -> new RatIglooItem(new Item.Properties(), DyeColor.byId(finalI)));
			RAT_HAMMOCKS[i] = ITEMS.register("rat_hammock_" + DyeColor.byId(i).getName(), () -> new RatHammockItem(new Item.Properties(), DyeColor.byId(finalI)));
		}
	}

	public static final RegistryObject<Item> RAT_WATER_BOTTLE = ITEMS.register("rat_water_bottle", () -> new RatDecorationItem(new Item.Properties()));
	public static final RegistryObject<Item> RAT_SEED_BOWL = ITEMS.register("rat_seed_bowl", () -> new RatDecorationItem(new Item.Properties()));
	public static final RegistryObject<Item> RAT_BREEDING_LANTERN = ITEMS.register("rat_breeding_lantern", () -> new RatDecorationItem(new Item.Properties()));
	public static final RegistryObject<Item> RAT_WHEEL = ITEMS.register("rat_wheel", () -> new RatDecorationItem(new Item.Properties()));
	public static final RegistryObject<Item> CONFIT_BYALDI = ITEMS.register("confit_byaldi", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(50).saturationMod(1.0F).effect(() -> new MobEffectInstance(RatsEffectRegistry.CONFIT_BYALDI.get(), 2400), 1.0F).alwaysEat().build())));
	public static final RegistryObject<Item> POTATO_PANCAKE = ITEMS.register("potato_pancake", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(0.5F).alwaysEat().build())));
	public static final RegistryObject<Item> LITTLE_BLACK_SQUASH_BALLS = ITEMS.register("little_black_squash_balls", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> LITTLE_BLACK_WORM = ITEMS.register("little_black_worm", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> CENTIPEDE = ITEMS.register("centipede", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> POTATO_KNISHES = ITEMS.register("potato_knishes", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(50).saturationMod(1.0F).effect(() -> new MobEffectInstance(RatsEffectRegistry.CONFIT_BYALDI.get(), 2400), 1.0F).alwaysEat().build())));
	public static final RegistryObject<Item> TINY_COIN = ITEMS.register("tiny_coin", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> TOKEN_FRAGMENT = ITEMS.register("token_fragment", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> TOKEN_PIECE = ITEMS.register("token_piece", () -> new Item(new Item.Properties().rarity(Rarity.RARE)));
	public static final RegistryObject<Item> ARCHEOLOGIST_HAT = ITEMS.register("archeologist_hat", () -> new HatItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON), RatsArmorMaterialRegistry.GENERIC_HAT, 1));
	public static final RegistryObject<Item> FARMER_HAT = ITEMS.register("farmer_hat", () -> new HatItem(new Item.Properties().stacksTo(1), RatsArmorMaterialRegistry.FARMER_HAT, 0));
	public static final RegistryObject<Item> FISHERMAN_HAT = ITEMS.register("fisherman_hat", () -> new HatItem(new Item.Properties().stacksTo(1), RatsArmorMaterialRegistry.GENERIC_HAT, 0));
	public static final RegistryObject<Item> RAT_FEZ = ITEMS.register("rat_fez", () -> new HatItem(new Item.Properties().stacksTo(1), RatsArmorMaterialRegistry.FEZ, 2));
	public static final RegistryObject<Item> TOP_HAT = ITEMS.register("top_hat", () -> new HatItem(new Item.Properties().stacksTo(1), RatsArmorMaterialRegistry.TOP_HAT, 0));
	public static final RegistryObject<Item> SANTA_HAT = ITEMS.register("santa_hat", () -> new HatItem(new Item.Properties().stacksTo(1), RatsArmorMaterialRegistry.SANTA_HAT, 0));
	public static final RegistryObject<Item> HALO_HAT = ITEMS.register("halo_hat", () -> new HatItem(new Item.Properties().stacksTo(1), RatsArmorMaterialRegistry.HALO, 0));
	public static final RegistryObject<Item> PARTY_HAT = ITEMS.register("party_hat", () -> new PartyHatItem(new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> PIRAT_HAT = ITEMS.register("pirat_hat", () -> new HatItem(new Item.Properties().stacksTo(1), RatsArmorMaterialRegistry.GENERIC_HAT, 0));
	public static final RegistryObject<Item> RAT_KING_CROWN = ITEMS.register("rat_king_crown", () -> new HatItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE), RatsArmorMaterialRegistry.CROWN, 0));
	public static final RegistryObject<Item> PLAGUE_DOCTOR_MASK = ITEMS.register("plague_doctor_mask", () -> new HatItem(new Item.Properties().stacksTo(1), RatsArmorMaterialRegistry.PLAGUE_MASK, 1));
	public static final RegistryObject<Item> BLACK_DEATH_MASK = ITEMS.register("black_death_mask", () -> new HatItem(new Item.Properties().stacksTo(1).fireResistant().rarity(Rarity.RARE), RatsArmorMaterialRegistry.PLAGUE_MASK, 1));
	public static final RegistryObject<Item> EXTERMINATOR_HAT = ITEMS.register("exterminator_hat", () -> new HatItem(new Item.Properties().stacksTo(1), RatsArmorMaterialRegistry.GENERIC_HAT, 0));
	public static final RegistryObject<Item> MUSIC_DISC_MICE_ON_VENUS = ITEMS.register("music_disc_mice_on_venus", () -> new RecordItem(13, RatsSoundRegistry.MICE_ON_VENUS, new Item.Properties().stacksTo(1).rarity(Rarity.RARE), 280 * 20));
	public static final RegistryObject<Item> MUSIC_DISC_LIVING_MICE = ITEMS.register("music_disc_living_mice", () -> new RecordItem(13, RatsSoundRegistry.LIVING_MICE, new Item.Properties().stacksTo(1).rarity(Rarity.RARE), 188 * 20));


	public static final RegistryObject<Item> RAT_UPGRADE_BASIC = ITEMS.register("rat_upgrade_basic", () -> new BaseRatUpgradeItem(new Item.Properties()));
	public static final RegistryObject<Item> RAT_UPGRADE_CREATIVE = ITEMS.register("rat_upgrade_creative", () -> new CreativeRatUpgradeItem(new Item.Properties().fireResistant()));
	public static final RegistryObject<Item> RAT_UPGRADE_JURY_RIGGED = ITEMS.register("rat_upgrade_jury_rigged", () -> new JuryRiggedRatUpgradeItem(new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> RAT_UPGRADE_SPEED = ITEMS.register("rat_upgrade_speed", () -> new StatBoostingRatUpgradeItem(new Item.Properties(), 0, () -> Map.of(Attributes.MOVEMENT_SPEED, 0.5D)));
	public static final RegistryObject<Item> RAT_UPGRADE_PLATTER = ITEMS.register("rat_upgrade_platter", () -> new PlatterRatUpgradeItem(new Item.Properties()));
	public static final RegistryObject<Item> RAT_UPGRADE_HEALTH = ITEMS.register("rat_upgrade_health", () -> new StatBoostingRatUpgradeItem(new Item.Properties(), 0, () -> Map.of(Attributes.MAX_HEALTH, 20.0D)));
	public static final RegistryObject<Item> RAT_UPGRADE_ARMOR = ITEMS.register("rat_upgrade_armor", () -> new StatBoostingRatUpgradeItem(new Item.Properties(), 0, () -> Map.of(Attributes.ARMOR, 10.0D)));
	public static final RegistryObject<Item> RAT_UPGRADE_BOW = ITEMS.register("rat_upgrade_bow", () -> new RangedWeaponRatUpgradeItem(new Item.Properties(), false));
	public static final RegistryObject<Item> RAT_UPGRADE_CROSSBOW = ITEMS.register("rat_upgrade_crossbow", () -> new RangedWeaponRatUpgradeItem(new Item.Properties(), true));
	public static final RegistryObject<Item> RAT_UPGRADE_DEMON = ITEMS.register("rat_upgrade_demon", () -> new DemonRatUpgradeItem(new Item.Properties()));
	public static final RegistryObject<Item> RAT_UPGRADE_STRENGTH = ITEMS.register("rat_upgrade_strength", () -> new StatBoostingRatUpgradeItem(new Item.Properties(), 0, () -> Map.of(Attributes.ATTACK_DAMAGE, 5.0D)));
	public static final RegistryObject<Item> RAT_UPGRADE_WARRIOR = ITEMS.register("rat_upgrade_warrior", () -> new StatBoostingRatUpgradeItem(new Item.Properties(), 2, () -> Map.of(Attributes.MAX_HEALTH, RatConfig.warriorHealthUpgrade, Attributes.ARMOR, RatConfig.warriorArmorUpgrade, Attributes.ATTACK_DAMAGE, RatConfig.warriorDamageUpgrade)));
	public static final RegistryObject<Item> RAT_UPGRADE_GOD = ITEMS.register("rat_upgrade_god", () -> new StatBoostingRatUpgradeItem(new Item.Properties().fireResistant(), 3, () -> Map.of(Attributes.MAX_HEALTH, RatConfig.godHealthUpgrade, Attributes.ARMOR, RatConfig.godArmorUpgrade, Attributes.ATTACK_DAMAGE, RatConfig.godDamageUpgrade)));
	public static final RegistryObject<Item> RAT_UPGRADE_CHEF = ITEMS.register("rat_upgrade_chef", () -> new ChefRatUpgradeItem(new Item.Properties()));
	public static final RegistryObject<Item> RAT_UPGRADE_CRAFTING = ITEMS.register("rat_upgrade_crafting", () -> new CraftingRatUpgradeItem(new Item.Properties()));
	public static final RegistryObject<Item> RAT_UPGRADE_BLACKLIST = ITEMS.register("rat_upgrade_blacklist", () -> new RatListUpgradeItem(new Item.Properties().stacksTo(1), 0, 3));
	public static final RegistryObject<Item> RAT_UPGRADE_WHITELIST = ITEMS.register("rat_upgrade_whitelist", () -> new RatListUpgradeItem(new Item.Properties().stacksTo(1), 0, 3));
	public static final RegistryObject<Item> RAT_UPGRADE_MOB_FILTER = ITEMS.register("rat_upgrade_mob_filter", () -> new MobFilterUpgradeItem(new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> RAT_UPGRADE_REPLANTER = ITEMS.register("rat_upgrade_replanter", () -> new BaseRatUpgradeItem(new Item.Properties(), 0, 1));
	public static final RegistryObject<Item> RAT_UPGRADE_FLIGHT = ITEMS.register("rat_upgrade_flight", () -> new BaseFlightRatUpgradeItem(new Item.Properties(), 0, 3, FEATHERY_WING.get()));
	public static final RegistryObject<Item> RAT_UPGRADE_DRAGON = ITEMS.register("rat_upgrade_dragon", () -> new DragonRatUpgradeItem(new Item.Properties(), DRAGON_WING.get()));
	public static final RegistryObject<Item> RAT_UPGRADE_BEE = ITEMS.register("rat_upgrade_bee", () -> new BeeRatUpgradeItem(new Item.Properties(), BEE_WING.get()));
	public static final RegistryObject<Item> RAT_UPGRADE_AQUATIC = ITEMS.register("rat_upgrade_aquatic", () -> new AquaticRatUpgradeItem(new Item.Properties()));
	public static final RegistryObject<Item> RAT_UPGRADE_ENDER = ITEMS.register("rat_upgrade_ender", () -> new EnderRatUpgradeItem(new Item.Properties()));
	public static final RegistryObject<Item> RAT_UPGRADE_LUMBERJACK = ITEMS.register("rat_upgrade_lumberjack", () -> new LumberjackRatUpgradeItem(new Item.Properties()));
	//public static final RegistryObject<Item> RAT_UPGRADE_MINER_ORE = ITEMS.register("rat_upgrade_miner_ore", () -> new OreMinerRatUpgradeItem(new Item.Properties()));
	//public static final RegistryObject<Item> RAT_UPGRADE_MINER = ITEMS.register("rat_upgrade_miner", () -> new MinerRatUpgradeItem(new Item.Properties(), 1, 3));
	public static final RegistryObject<Item> RAT_UPGRADE_QUARRY = ITEMS.register("rat_upgrade_quarry", () -> new QuarryRatUpgradeItem(new Item.Properties()));
	public static final RegistryObject<Item> RAT_UPGRADE_FARMER = ITEMS.register("rat_upgrade_farmer", () -> new FarmerRatUpgradeItem(new Item.Properties()));
	public static final RegistryObject<Item> RAT_UPGRADE_BREEDER = ITEMS.register("rat_upgrade_breeder", () -> new BreederRatUpgradeItem(new Item.Properties()));
	public static final RegistryObject<Item> RAT_UPGRADE_FISHERMAN = ITEMS.register("rat_upgrade_fisherman", () -> new FishermanRatUpgradeItem(new Item.Properties()));
	public static final RegistryObject<Item> RAT_UPGRADE_TICK_ACCELERATOR = ITEMS.register("rat_upgrade_tick_accelerator", () -> new TickAccelRatUpgradeItem(new Item.Properties()));
	public static final RegistryObject<Item> RAT_UPGRADE_TIME_MANIPULATOR = ITEMS.register("rat_upgrade_time_manipulator", () -> new TimeManipuRatorUpgradeItem(new Item.Properties()));
	public static final RegistryObject<Item> RAT_UPGRADE_ASBESTOS = ITEMS.register("rat_upgrade_asbestos", () -> new AsbestosRatUpgradeItem(new Item.Properties()));
	public static final RegistryObject<Item> RAT_UPGRADE_UNDERWATER = ITEMS.register("rat_upgrade_underwater", () -> new UnderwaterRatUpgradeItem(new Item.Properties()));
	public static final RegistryObject<Item> RAT_UPGRADE_POISON = ITEMS.register("rat_upgrade_poison", () -> new PoisonRatUpgradeItem(new Item.Properties()));
	public static final RegistryObject<Item> RAT_UPGRADE_DAMAGE_PROTECTION = ITEMS.register("rat_upgrade_damage_protection", () -> new ProtectionRatUpgradeItem(new Item.Properties()));
	public static final RegistryObject<Item> RAT_UPGRADE_ORE_DOUBLING = ITEMS.register("rat_upgrade_ore_doubling", () -> new OreDoublingRatUpgradeItem(new Item.Properties()));
	public static final RegistryObject<Item> RAT_UPGRADE_BASIC_ENERGY = ITEMS.register("rat_upgrade_basic_energy", () -> new EnergyRatUpgradeItem(new Item.Properties(), 0, RatConfig.ratRFTransferBasic));
	public static final RegistryObject<Item> RAT_UPGRADE_ADVANCED_ENERGY = ITEMS.register("rat_upgrade_advanced_energy", () -> new EnergyRatUpgradeItem(new Item.Properties(), 1, RatConfig.ratRFTransferAdvanced));
	public static final RegistryObject<Item> RAT_UPGRADE_ELITE_ENERGY = ITEMS.register("rat_upgrade_elite_energy", () -> new EnergyRatUpgradeItem(new Item.Properties(), 2, RatConfig.ratRFTransferElite));
	public static final RegistryObject<Item> RAT_UPGRADE_EXTREME_ENERGY = ITEMS.register("rat_upgrade_extreme_energy", () -> new EnergyRatUpgradeItem(new Item.Properties().fireResistant(), 3, RatConfig.ratRFTransferExtreme));
	public static final RegistryObject<Item> RAT_UPGRADE_BUCKET = ITEMS.register("rat_upgrade_bucket", () -> new BucketRatUpgradeItem(new Item.Properties(), 0, 2, FluidType.BUCKET_VOLUME));
	public static final RegistryObject<Item> RAT_UPGRADE_BIG_BUCKET = ITEMS.register("rat_upgrade_big_bucket", () -> new BucketRatUpgradeItem(new Item.Properties(), 2, 2, FluidType.BUCKET_VOLUME * 5));
	public static final RegistryObject<Item> RAT_UPGRADE_MILKER = ITEMS.register("rat_upgrade_milker", () -> new MilkerRatUpgradeItem(new Item.Properties()));
	public static final RegistryObject<Item> RAT_UPGRADE_SHEARS = ITEMS.register("rat_upgrade_shears", () -> new ShearsRatUpgradeItem(new Item.Properties()));
	public static final RegistryObject<Item> RAT_UPGRADE_ARISTOCRAT = ITEMS.register("rat_upgrade_aristocrat", () -> new AristocratRatUpgradeItem(new Item.Properties()));
	public static final RegistryObject<Item> RAT_UPGRADE_TNT = ITEMS.register("rat_upgrade_tnt", () -> new TNTRatUpgradeItem(new Item.Properties()));
	public static final RegistryObject<Item> RAT_UPGRADE_TNT_SURVIVOR = ITEMS.register("rat_upgrade_tnt_survivor", () -> new TNTSurvivorRatUpgradeItem(new Item.Properties()));
	public static final RegistryObject<Item> RAT_UPGRADE_PLACER = ITEMS.register("rat_upgrade_placer", () -> new PlacerRatUpgradeItem(new Item.Properties()));
	public static final RegistryObject<Item> RAT_UPGRADE_VOODOO = ITEMS.register("rat_upgrade_voodoo", () -> new StatBoostingRatUpgradeItem(new Item.Properties(), 2, 0, () -> Map.of(Attributes.MAX_HEALTH, RatConfig.voodooHealthUpgrade), true));
	public static final RegistryObject<Item> RAT_UPGRADE_GEMCUTTER = ITEMS.register("rat_upgrade_gemcutter", () -> new GemcutterRatUpgradeItem(new Item.Properties()));
	public static final RegistryObject<Item> RAT_UPGRADE_ENCHANTER = ITEMS.register("rat_upgrade_enchanter", () -> new EnchanterRatUpgradeItem(new Item.Properties(), 1, 2));
	public static final RegistryObject<Item> RAT_UPGRADE_DISENCHANTER = ITEMS.register("rat_upgrade_disenchanter", () -> new EnchanterRatUpgradeItem(new Item.Properties(), 1, 1));
	public static final RegistryObject<Item> RAT_UPGRADE_CHRISTMAS = ITEMS.register("rat_upgrade_christmas", () -> new ChristmasRatUpgradeItem(new Item.Properties()));
	public static final RegistryObject<Item> RAT_UPGRADE_NO_FLUTE = ITEMS.register("rat_upgrade_no_flute", () -> new BaseRatUpgradeItem(new Item.Properties(), 0, 1));
	public static final RegistryObject<Item> RAT_UPGRADE_CARRAT = ITEMS.register("rat_upgrade_carrat", () -> new CarratRatUpgradeItem(new Item.Properties()));
	public static final RegistryObject<Item> RAT_UPGRADE_DJ = ITEMS.register("rat_upgrade_dj", () -> new DJRatUpgradeItem(new Item.Properties()));
	public static final RegistryObject<Item> RAT_UPGRADE_ANGEL = ITEMS.register("rat_upgrade_angel", () -> new BaseRatUpgradeItem(new Item.Properties(), 2, 2));
	public static final RegistryObject<Item> RAT_UPGRADE_UNDEAD = ITEMS.register("rat_upgrade_undead", () -> new UndeadRatUpgradeItem(new Item.Properties()));
	public static final RegistryObject<Item> RAT_UPGRADE_PICKPOCKET = ITEMS.register("rat_upgrade_pickpocket", () -> new PickpocketRatUpgradeItem(new Item.Properties()));
	public static final RegistryObject<Item> RAT_UPGRADE_GARDENER = ITEMS.register("rat_upgrade_gardener", () -> new RatGardenerUpgradeItem(new Item.Properties()));
	public static final RegistryObject<Item> RAT_UPGRADE_IDOL = ITEMS.register("rat_upgrade_idol", () -> new IdolRatUpgradeItem(new Item.Properties()));
	public static final RegistryObject<Item> RAT_UPGRADE_SCULKED = ITEMS.register("rat_upgrade_sculked", () -> new SculkedRatUpgradeItem(new Item.Properties()));
	public static final RegistryObject<Item> RAT_UPGRADE_BASIC_MOUNT = ITEMS.register("rat_upgrade_basic_mount", () -> new BaseRatUpgradeItem(new Item.Properties()));
	public static final RegistryObject<Item> RAT_UPGRADE_CHICKEN_MOUNT = ITEMS.register("rat_upgrade_chicken_mount", () -> new MountRatUpgradeItem<>(new Item.Properties(), 2, 3, RatsEntityRegistry.RAT_MOUNT_CHICKEN));
	public static final RegistryObject<Item> RAT_UPGRADE_GOLEM_MOUNT = ITEMS.register("rat_upgrade_golem_mount", () -> new MountRatUpgradeItem<>(new Item.Properties(), 2, 3, RatsEntityRegistry.RAT_MOUNT_GOLEM));
	public static final RegistryObject<Item> RAT_UPGRADE_STRIDER_MOUNT = ITEMS.register("rat_upgrade_strider_mount", () -> new StriderMountRatUpgradeItem<>(new Item.Properties(), 2, 3, RatsEntityRegistry.RAT_STRIDER_MOUNT));
	public static final RegistryObject<Item> RAT_UPGRADE_BEAST_MOUNT = ITEMS.register("rat_upgrade_beast_mount", () -> new MountRatUpgradeItem<>(new Item.Properties(), 2, 3, RatsEntityRegistry.RAT_MOUNT_BEAST));
	public static final RegistryObject<Item> RAT_UPGRADE_COMBINED = ITEMS.register("rat_upgrade_combined", () -> new CombinedRatUpgradeItem(new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> RAT_UPGRADE_COMBINED_CREATIVE = ITEMS.register("rat_upgrade_combined_creative", () -> new CombinedRatUpgradeItem(new Item.Properties().stacksTo(1).fireResistant()));

	public static final RegistryObject<Item> RAT_NUGGET = ITEMS.register("rat_nugget", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> RAT_NUGGET_ORE = ITEMS.register("rat_nugget_ore", () -> new OreRatNuggetItem(new Item.Properties()));
	public static final RegistryObject<Item> RAT_BANNER_PATTERN = ITEMS.register("rat_banner_pattern", () -> new BannerPatternItem(RatsBannerPatternTags.RAT_BANNER_PATTERN, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> CHEESE_BANNER_PATTERN = ITEMS.register("cheese_banner_pattern", () -> new BannerPatternItem(RatsBannerPatternTags.CHEESE_BANNER_PATTERN, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> RAC_BANNER_PATTERN = ITEMS.register("rat_and_crossbones_banner_pattern", () -> new BannerPatternItem(RatsBannerPatternTags.RAC_BANNER_PATTERN, new Item.Properties().stacksTo(1)));
}
