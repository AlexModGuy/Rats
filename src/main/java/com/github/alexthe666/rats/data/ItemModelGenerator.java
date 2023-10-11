package com.github.alexthe666.rats.data;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.registry.RatlantisBlockRegistry;
import com.github.alexthe666.rats.registry.RatlantisItemRegistry;
import com.github.alexthe666.rats.registry.RatsBlockRegistry;
import com.github.alexthe666.rats.registry.RatsItemRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.loaders.ItemLayerModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ItemModelGenerator extends ItemModelProvider {

	public ItemModelGenerator(PackOutput output, ExistingFileHelper helper) {
		super(output, RatsMod.MODID, helper);
	}

	@Override
	protected void registerModels() {

		for (Item i : ForgeRegistries.ITEMS.getValues()) {
			if (i instanceof SpawnEggItem && Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(i)).getNamespace().equals(RatsMod.MODID)) {
				this.getBuilder(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(i)).getPath())
						.parent(this.getExistingFile(new ResourceLocation("item/template_spawn_egg")));
			}
		}

		this.singleTex(RatsItemRegistry.ARCHEOLOGIST_HAT);
		this.singleTex(RatsItemRegistry.ASSORTED_VEGETABLES);
		this.singleTex(RatsItemRegistry.BLACK_DEATH_MASK);
		this.toBlock(RatsBlockRegistry.BLOCK_OF_BLUE_CHEESE.get());
		this.toBlock(RatsBlockRegistry.BLOCK_OF_CHEESE.get());
		this.toBlock(RatsBlockRegistry.BLOCK_OF_NETHER_CHEESE.get());
		this.singleTex(RatsItemRegistry.BLUE_CHEESE);
		this.toBlock(RatsBlockRegistry.BLUE_CHEESE_CAULDRON.get());
		this.toBlock(RatsBlockRegistry.CHEESE_CAULDRON.get());
		this.toBlock(RatsBlockRegistry.MILK_CAULDRON.get());
		this.toBlock(RatsBlockRegistry.NETHER_CHEESE_CAULDRON.get());
		this.singleTex(RatsItemRegistry.CENTIPEDE);
		this.fullbrightSingleTex(RatsItemRegistry.CHARGED_CREEPER_CHUNK);
		this.singleTex(RatsItemRegistry.CHEESE);
		this.generated(RatsItemRegistry.CHEESE_BANNER_PATTERN.getId().getPath(), false, new ResourceLocation("item/creeper_banner_pattern"));
		this.singleTexTool(RatsItemRegistry.CHEESE_STICK);
		this.singleTex(RatsItemRegistry.CHEF_TOQUE);
		this.toBlock(RatsBlockRegistry.COMPRESSED_GARBAGE.get());
		this.singleTex(RatsItemRegistry.CONFIT_BYALDI);
		this.singleTex(RatsItemRegistry.CONTAMINATED_FOOD);
		this.singleTex(RatsItemRegistry.COOKED_RAT);
		this.singleTex(RatsItemRegistry.CORRUPT_RAT_SKULL);
		this.fullbrightSingleTex(RatsItemRegistry.CREATIVE_CHEESE);
		this.singleTex(RatsItemRegistry.CRIMSON_FLUID);
		this.toBlock(RatsBlockRegistry.CURSED_GARBAGE.get());
		this.singleTex(RatsItemRegistry.DRAGON_WING);
		this.toBlock(RatsBlockRegistry.DYE_SPONGE.get());
		this.singleTex(RatsItemRegistry.EXTERMINATOR_HAT);
		this.singleTex(RatsItemRegistry.FARMER_HAT);
		this.singleTex(RatsItemRegistry.FEATHERY_WING);
		this.singleTex(RatsItemRegistry.FILTH);
		this.singleTex(RatsItemRegistry.FILTH_CORRUPTION);
		this.toBlock(RatsBlockRegistry.FISH_BARREL.get());
		this.singleTex(RatsItemRegistry.FISHERMAN_HAT);
		this.toBlockModel(RatsBlockRegistry.GARBAGE_PILE.get(), this.blockPrefix("garbage_0"));
		this.buildItem(RatsItemRegistry.GILDED_RAT_FLUTE.getId().getPath(), "rats:item/rat_flute", false, null, this.itemPrefix("gilded_rat_flute"));
		this.singleTex(RatsItemRegistry.GOLDEN_RAT_SKULL);
		this.singleTex(RatsItemRegistry.HALO_HAT);
		this.singleTex(RatsItemRegistry.HERB_BUNDLE);
		this.toBlock(RatsBlockRegistry.JACK_O_RATERN.get());
		this.singleTex(RatsItemRegistry.LITTLE_BLACK_SQUASH_BALLS);
		this.singleTex(RatsItemRegistry.LITTLE_BLACK_WORM);
		this.toBlockModel(RatsBlockRegistry.MANHOLE.get(), this.blockPrefix("manhole_bottom"));
		this.toBlock(RatsBlockRegistry.MARBLED_CHEESE_RAW.get());
		this.singleTex(RatsItemRegistry.MUSIC_DISC_LIVING_MICE);
		this.singleTex(RatsItemRegistry.MUSIC_DISC_MICE_ON_VENUS);
		this.fullbrightSingleTex(RatsItemRegistry.NETHER_CHEESE);
		this.singleTexTool(RatsItemRegistry.PATROL_STICK);
		this.toBlock(RatsBlockRegistry.PIED_GARBAGE.get());
		this.toBlock(RatsBlockRegistry.PIED_WOOL.get());
		this.singleTex(RatsItemRegistry.PIPER_HAT);
		this.singleTex(RatsItemRegistry.PIRAT_HAT);
		this.singleTex(RatsItemRegistry.PLAGUE_DOCTOR_MASK);
		this.singleTex(RatsItemRegistry.PLAGUE_DOCTORATE);
		this.singleTex(RatsItemRegistry.PLAGUE_ESSENCE);
		this.singleTex(RatsItemRegistry.PLAGUE_LEECH);
		this.singleTexTool(RatsItemRegistry.PLAGUE_SCYTHE);
		this.singleTex(RatsItemRegistry.PLAGUE_STEW);
		this.fullbrightSingleTex(RatsItemRegistry.PLAGUE_TOME);
		this.singleTex(RatsItemRegistry.PLASTIC_WASTE);
		this.singleTex(RatsItemRegistry.POTATO_KNISHES);
		this.singleTex(RatsItemRegistry.POTATO_PANCAKE);
		this.toBlock(RatsBlockRegistry.PURIFIED_GARBAGE.get());
		this.singleTex(RatsItemRegistry.PURIFYING_LIQUID);
		this.singleTexTool(RatsItemRegistry.RADIUS_STICK);
		this.generated(RatsItemRegistry.RAC_BANNER_PATTERN.getId().getPath(), false, new ResourceLocation("item/creeper_banner_pattern"));
		this.singleTex(RatsItemRegistry.RAT_ARROW);
		this.generated(RatsBlockRegistry.RAT_ATTRACTOR.getId().getPath(), false, this.itemPrefix("rat_attractor"));
		this.generated(RatsItemRegistry.RAT_BANNER_PATTERN.getId().getPath(), false, new ResourceLocation("item/creeper_banner_pattern"));
		this.singleTex(RatsItemRegistry.RAT_BREEDING_LANTERN);
		this.singleTex(RatsItemRegistry.RAT_BURGER);
		this.toBlockModel(RatsBlockRegistry.RAT_CAGE.get(), this.blockPrefix("rat_cage_item"));
		this.toBlockModel(RatsBlockRegistry.RAT_CAGE_BREEDING_LANTERN.get(), this.blockPrefix("rat_cage_item"));
		this.toBlockModel(RatsBlockRegistry.RAT_CAGE_DECORATED.get(), this.blockPrefix("rat_cage_item"));
		this.toBlockModel(RatsBlockRegistry.RAT_CAGE_WHEEL.get(), this.blockPrefix("rat_cage_item"));
		this.singleTex(RatsItemRegistry.RAT_CAPTURE_NET);
		this.toBlock(RatsBlockRegistry.RAT_CRAFTING_TABLE.get());
		this.singleTex(RatsItemRegistry.RAT_FEZ);
		this.singleTex(RatsItemRegistry.RAT_KING_CROWN);
		this.singleTex(RatsItemRegistry.RAT_NUGGET);
		this.generated(RatsItemRegistry.RAT_NUGGET_ORE.getId().getPath(), false, this.itemPrefix("rat_nugget_ore"), this.itemPrefix("rat_nugget_overlay"));
		this.singleTex(RatsItemRegistry.RAT_PAPERS);
		this.singleTex(RatsItemRegistry.RAT_PAW);
		this.singleTex(RatsItemRegistry.RAT_PELT);
		this.toBlock(RatsBlockRegistry.RAT_UPGRADE_BLOCK.get());
		this.toBlock(RatsBlockRegistry.RAT_QUARRY.get());
		this.toBlock(RatsBlockRegistry.RAT_QUARRY_PLATFORM.get());

		ModelFile sack1 = this.generated("rat_sack_1", false, this.itemPrefix("rat_sack_1"));
		ModelFile sack2 = this.generated("rat_sack_2", false, this.itemPrefix("rat_sack_2"));
		ModelFile sack3 = this.generated("rat_sack_3", false, this.itemPrefix("rat_sack_3"));
		singleTex(RatsItemRegistry.RAT_SACK)
				.override().predicate(ResourceLocation.tryParse("rat_count"), 1).model(sack1).end()
				.override().predicate(ResourceLocation.tryParse("rat_count"), 2).model(sack2).end()
				.override().predicate(ResourceLocation.tryParse("rat_count"), 3).model(sack3).end();

		this.singleTex(RatsItemRegistry.RAT_SEED_BOWL);
		this.singleTex(RatsItemRegistry.RAT_SKULL);
		this.generated(RatsBlockRegistry.RAT_TUBE_COLOR.getId().getPath(), false, this.itemPrefix("rat_tube"));
		this.singleTex(RatsItemRegistry.RAT_WATER_BOTTLE);
		this.singleTex(RatsItemRegistry.RAT_WHEEL);
		this.singleTex(RatsItemRegistry.RAT_WHISTLE);
		ResourceLocation special = new ResourceLocation(RatsMod.MODID, "special");
		this.fullbrightSingleTex(RatsItemRegistry.RATBOW_ESSENCE)
				.override().predicate(special, 1).model(this.generated("ratbow_essence_aro", true, this.itemPrefix("ratbow_essence_aro"))).end()
				.override().predicate(special, 2).model(this.generated("ratbow_essence_ace", true, this.itemPrefix("ratbow_essence_ace"))).end()
				.override().predicate(special, 3).model(this.generated("ratbow_essence_bi", true, this.itemPrefix("ratbow_essence_bi"))).end()
				.override().predicate(special, 4).model(this.generated("ratbow_essence_gay", true, this.itemPrefix("ratbow_essence_gay"))).end()
				.override().predicate(special, 5).model(this.generated("ratbow_essence_genderfluid", true, this.itemPrefix("ratbow_essence_genderfluid"))).end()
				.override().predicate(special, 6).model(this.generated("ratbow_essence_enby", true, this.itemPrefix("ratbow_essence_enby"))).end()
				.override().predicate(special, 7).model(this.generated("ratbow_essence_lesbian", true, this.itemPrefix("ratbow_essence_lesbian"))).end()
				.override().predicate(special, 8).model(this.generated("ratbow_essence_pan", true, this.itemPrefix("ratbow_essence_pan"))).end()
				.override().predicate(special, 9).model(this.generated("ratbow_essence_trans", true, this.itemPrefix("ratbow_essence_trans"))).end();
		this.singleTex(RatsItemRegistry.RAW_PLASTIC);
		this.singleTex(RatsItemRegistry.RAW_RAT);
		this.singleTex(RatsItemRegistry.SANTA_HAT);
		this.singleTex(RatsItemRegistry.STRING_CHEESE);
		this.singleTex(RatsItemRegistry.TANGLED_RAT_TAILS);
		this.singleTex(RatsItemRegistry.TINY_COIN);
		this.singleTex(RatsItemRegistry.TOKEN_FRAGMENT);
		this.singleTex(RatsItemRegistry.TOKEN_PIECE);
		this.singleTex(RatsItemRegistry.TOP_HAT);
		this.singleTex(RatsItemRegistry.TREACLE);
		this.toBlock(RatsBlockRegistry.UPGRADE_COMBINER.get());
		this.toBlock(RatsBlockRegistry.UPGRADE_SEPARATOR.get());

		for (DyeColor color : DyeColor.values()) {
			this.generated("rat_hammock_" + color.getName(), false, this.itemPrefix("rat_hammock_0"), this.itemPrefix("rat_hammock_1"));
			this.generated("rat_igloo_" + color.getName(), false, this.itemPrefix("rat_igloo"));
			this.generated("rat_tube_" + color.getName(), false, this.itemPrefix("rat_tube"));
		}

		this.singleTex(RatsItemRegistry.RAT_UPGRADE_ADVANCED_ENERGY);
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_ANGEL);
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_AQUATIC);
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_ARISTOCRAT);
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_ARMOR);
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_ASBESTOS);
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_BASIC);
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_BASIC_ENERGY);
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_BASIC_MOUNT);
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_BEAST_MOUNT);
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_BEE);
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_BIG_BUCKET);
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_BLACKLIST);
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_BOTTLER);
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_BOW);
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_BREEDER);
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_BUCKET);
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_CARRAT);
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_CHEF);
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_CHICKEN_MOUNT);
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_CHRISTMAS);
		this.fullbrightSingleTex(RatsItemRegistry.RAT_UPGRADE_COMBINED);
		this.fullbrightSingleTex(RatsItemRegistry.RAT_UPGRADE_COMBINED_CREATIVE);
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_CRAFTING);
		this.fullbrightSingleTex(RatsItemRegistry.RAT_UPGRADE_CREATIVE);
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_CROSSBOW);
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_DAMAGE_PROTECTION);
		ModelFile soul = this.generated("rat_upgrade_soul_demon", false, itemPrefix("rat_upgrade_soul_demon"));
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_DEMON).override().predicate(new ResourceLocation(RatsMod.MODID, "soul"), 1).model(soul).end();
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_DISENCHANTER);
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_DJ);
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_DRAGON);
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_ELITE_ENERGY);
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_ENCHANTER);
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_ENDER);
		this.fullbrightSingleTex(RatsItemRegistry.RAT_UPGRADE_EXTREME_ENERGY);
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_FARMER);
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_FISHERMAN);
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_FLIGHT);
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_GARDENER);
		this.fullbrightSingleTex(RatsItemRegistry.RAT_UPGRADE_GOD);
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_GOLEM_MOUNT);
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_HEALTH);
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_IDOL);
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_JURY_RIGGED);
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_LUMBERJACK);
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_MILKER);
		//this.singleTex(RatsItemRegistry.RAT_UPGRADE_MINER);
		//this.singleTex(RatsItemRegistry.RAT_UPGRADE_MINER_ORE);
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_MOB_FILTER);
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_NO_FLUTE);
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_ORE_DOUBLING);
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_PICKPOCKET);
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_PLACER);
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_PLATTER);
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_POISON);
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_QUARRY);
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_REPLANTER);
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_SCULKED);
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_SHEARS);
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_SPEED);
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_STRENGTH);
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_STRIDER_MOUNT);
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_SUPPORT);
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_TICK_ACCELERATOR);
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_TIME_MANIPULATOR);
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_TNT);
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_TNT_SURVIVOR);
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_UNDEAD);
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_UNDERWATER);
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_VOODOO);
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_WARRIOR);
		this.singleTex(RatsItemRegistry.RAT_UPGRADE_WHITELIST);

		this.generated(RatlantisBlockRegistry.AIR_RAID_SIREN.getId().getPath(), false, this.itemPrefix("air_raid_siren"));
		this.singleTex(RatlantisItemRegistry.ANCIENT_SAWBLADE);
		this.fullbrightSingleTex(RatlantisItemRegistry.ARCANE_TECHNOLOGY);
		this.singleTex(RatlantisItemRegistry.AVIATOR_HAT);
		this.singleTex(RatlantisItemRegistry.BIPLANE_WING);
		this.toBlock(RatlantisBlockRegistry.BLACK_MARBLED_CHEESE.get());
		this.toBlock(RatlantisBlockRegistry.BRAIN_BLOCK.get());
		this.singleTex(RatlantisItemRegistry.CHARGED_RATBOT_BARREL);
		this.singleTex(RatlantisItemRegistry.CHEESE_CANNONBALL);
		this.toBlock(RatlantisBlockRegistry.CHEESE_ORE.get());
		this.generated(RatlantisBlockRegistry.CHUNKY_CHEESE_TOKEN.getId().getPath(), true, this.itemPrefix("chunky_cheese_token"));
		this.toBlock(RatlantisBlockRegistry.COMPRESSED_RAT.get());
		this.generated(RatlantisBlockRegistry.DUTCHRAT_BELL.getId().getPath(), true, this.itemPrefix("dutchrat_bell"));
		this.fullbrightSingleTex(RatlantisItemRegistry.DUTCHRAT_WHEEL);
		this.buildItem(RatlantisItemRegistry.FERAL_BAGH_NAKHS.getId().getPath(), "rats:item/bagh_nakhs", false, null, this.itemPrefix("feral_bagh_nakhs"));
		this.singleTex(RatlantisItemRegistry.FERAL_RAT_CLAW);
		this.fullbrightSingleTex(RatlantisItemRegistry.GEM_OF_RATLANTIS);
		this.buildItem(RatlantisItemRegistry.GHOST_PIRAT_CUTLASS.getId().getPath(), "rats:item/cutlass", true, null, this.itemPrefix("ghost_pirat_cutlass"));
		this.fullbrightSingleTex(RatlantisItemRegistry.GHOST_PIRAT_ECTOPLASM);
		this.fullbrightSingleTex(RatlantisItemRegistry.GHOST_PIRAT_HAT);
		this.fullbrightSingleTex(RatlantisItemRegistry.IDOL_OF_RATLANTIS);
		this.toBlock(RatlantisBlockRegistry.MARBLED_CHEESE.get());
		this.toBlock(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK.get());
		this.toBlock(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_SLAB.get());
		this.toBlock(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_STAIRS.get());
		this.toBlock(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_CHISELED.get());
		this.toBlock(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_CRACKED.get());
		this.toBlock(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_CRACKED_SLAB.get());
		this.toBlock(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_CRACKED_STAIRS.get());
		this.toBlock(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_MOSSY.get());
		this.toBlock(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_MOSSY_SLAB.get());
		this.toBlock(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_MOSSY_STAIRS.get());
		this.toBlock(RatlantisBlockRegistry.MARBLED_CHEESE_CHISELED.get());
		this.toBlock(RatlantisBlockRegistry.MARBLED_CHEESE_DIRT.get());
		this.toBlock(RatlantisBlockRegistry.MARBLED_CHEESE_GOLEM_CORE.get());
		this.toBlock(RatlantisBlockRegistry.MARBLED_CHEESE_GRASS.get());
		this.toBlock(RatlantisBlockRegistry.MARBLED_CHEESE_PILLAR.get());
		this.toBlock(RatlantisBlockRegistry.MARBLED_CHEESE_SLAB.get());
		this.toBlock(RatlantisBlockRegistry.MARBLED_CHEESE_STAIRS.get());
		this.toBlock(RatlantisBlockRegistry.MARBLED_CHEESE_TILE.get());
		this.singleTex(RatlantisItemRegistry.MILITARY_HAT);
		this.toBlock(RatlantisBlockRegistry.ORATCHALCUM_BLOCK.get());
		this.singleTex(RatlantisItemRegistry.ORATCHALCUM_INGOT);
		this.singleTex(RatlantisItemRegistry.RAW_ORATCHALCUM);
		this.singleTex(RatlantisItemRegistry.ORATCHALCUM_NUGGET);
		this.toBlock(RatlantisBlockRegistry.ORATCHALCUM_ORE.get());
		this.singleTexRenderType(RatlantisItemRegistry.PIRAT_BOAT, "minecraft:translucent");
		this.buttonInventory(RatlantisBlockRegistry.PIRAT_BUTTON.getId().getPath(), this.blockPrefix("pirat_planks"));
		this.singleTexRenderType(RatlantisItemRegistry.PIRAT_CHEST_BOAT, "minecraft:translucent");
		this.buildItem(RatlantisItemRegistry.PIRAT_CUTLASS.getId().getPath(), "rats:item/cutlass", false, null, this.itemPrefix("pirat_cutlass"));
		this.generatedRenderType(RatlantisBlockRegistry.PIRAT_DOOR.getId().getPath(), false, "minecraft:translucent", this.itemPrefix("pirat_door"));
		this.fenceInventory(RatlantisBlockRegistry.PIRAT_FENCE.getId().getPath(), this.blockPrefix("pirat_planks"));
		this.toBlock(RatlantisBlockRegistry.PIRAT_FENCE_GATE.get());
		this.generatedRenderType(RatlantisItemRegistry.PIRAT_HANGING_SIGN.getId().getPath(), false, "minecraft:translucent", this.itemPrefix("pirat_hanging_sign"));
		this.toBlock(RatlantisBlockRegistry.PIRAT_LEAVES.get());
		this.toBlock(RatlantisBlockRegistry.PIRAT_LOG.get());
		this.toBlock(RatlantisBlockRegistry.PIRAT_PLANKS.get());
		this.toBlock(RatlantisBlockRegistry.PIRAT_PRESSURE_PLATE.get());
		this.generatedRenderType(RatlantisItemRegistry.PIRAT_SIGN.getId().getPath(), false, "minecraft:translucent", this.itemPrefix("pirat_sign"));
		this.generatedRenderType(RatlantisBlockRegistry.PIRAT_SAPLING.getId().getPath(), false, "minecraft:translucent", this.blockPrefix("pirat_sapling"));
		this.toBlock(RatlantisBlockRegistry.PIRAT_SLAB.get());
		this.toBlock(RatlantisBlockRegistry.PIRAT_STAIRS.get());
		this.toBlockModel(RatlantisBlockRegistry.PIRAT_TRAPDOOR.get(), this.blockPrefix("pirat_trapdoor_bottom"));
		this.toBlock(RatlantisBlockRegistry.PIRAT_WOOD.get());
		this.fullbrightSingleTex(RatlantisItemRegistry.PSIONIC_RAT_BRAIN);
		this.generated(RatlantisItemRegistry.RAS_BANNER_PATTERN.getId().getPath(), false, new ResourceLocation("item/creeper_banner_pattern"));
		this.singleTex(RatlantisItemRegistry.RAT_TOGA);
		this.singleTex(RatlantisItemRegistry.RATBOT_BARREL);
		this.singleTex(RatlantisItemRegistry.RATFISH);
		this.singleTex(RatlantisItemRegistry.RATFISH_BUCKET);
		this.generated(RatlantisBlockRegistry.RATGLOVE_FLOWER.getId().getPath(), false, this.blockPrefix("ratglove_flower"));
		this.singleTex(RatlantisItemRegistry.RATGLOVE_PETALS);
		this.fullbrightSingleTex(RatlantisItemRegistry.RATLANTEAN_FLAME);
		this.toBlock(RatlantisBlockRegistry.RATLANTEAN_GEM_ORE.get());
		this.singleTexTool(RatlantisItemRegistry.RATLANTIS_AXE);
		this.trimmedArmor(RatlantisItemRegistry.RATLANTIS_BOOTS);
		ModelFile bowPulling0 = bow("ratlantis_bow_pulling_0", itemPrefix("ratlantis_bow_pulling_0"));
		ModelFile bowPulling1 = bow("ratlantis_bow_pulling_1", itemPrefix("ratlantis_bow_pulling_1"));
		ModelFile bowPulling2 = bow("ratlantis_bow_pulling_2", itemPrefix("ratlantis_bow_pulling_2"));
		this.bow(RatlantisItemRegistry.RATLANTIS_BOW.getId().getPath(), this.itemPrefix("ratlantis_bow"))
				.override().predicate(new ResourceLocation("pulling"), 1).model(bowPulling0).end()
				.override().predicate(new ResourceLocation("pulling"), 1).predicate(new ResourceLocation("pull"), (float) 0.65).model(bowPulling1).end()
				.override().predicate(new ResourceLocation("pulling"), 1).predicate(new ResourceLocation("pull"), (float) 0.9).model(bowPulling2).end();
		this.trimmedArmor(RatlantisItemRegistry.RATLANTIS_CHESTPLATE);
		this.trimmedArmor(RatlantisItemRegistry.RATLANTIS_HELMET);
		this.singleTexTool(RatlantisItemRegistry.RATLANTIS_HOE);
		this.trimmedArmor(RatlantisItemRegistry.RATLANTIS_LEGGINGS);
		this.singleTexTool(RatlantisItemRegistry.RATLANTIS_PICKAXE);
		this.fullbrightSingleTex(RatlantisItemRegistry.RATLANTIS_RAT_SKULL);
		this.toBlock(RatlantisBlockRegistry.RATLANTIS_REACTOR.get());
		this.singleTexTool(RatlantisItemRegistry.RATLANTIS_SHOVEL);
		this.singleTexTool(RatlantisItemRegistry.RATLANTIS_SWORD);
		this.toBlock(RatlantisBlockRegistry.RATLANTIS_UPGRADE_BLOCK.get());
		this.singleTex(RatlantisItemRegistry.RATTLING_GUN);
		this.toBlock(RatlantisBlockRegistry.STRIPPED_PIRAT_LOG.get());
		this.toBlock(RatlantisBlockRegistry.STRIPPED_PIRAT_WOOD.get());
		this.fullbrightSingleTex(RatlantisItemRegistry.VIAL_OF_SENTIENCE);

		this.singleTex(RatlantisItemRegistry.RAT_UPGRADE_ARCHEOLOGIST);
		this.singleTex(RatlantisItemRegistry.RAT_UPGRADE_AUTOMATON_MOUNT);
		this.singleTex(RatlantisItemRegistry.RAT_UPGRADE_BASIC_RATLANTEAN);
		this.singleTex(RatlantisItemRegistry.RAT_UPGRADE_BIPLANE_MOUNT);
		this.singleTex(RatlantisItemRegistry.RAT_UPGRADE_BUCCANEER);
		this.singleTex(RatlantisItemRegistry.RAT_UPGRADE_ETHEREAL);
		this.singleTex(RatlantisItemRegistry.RAT_UPGRADE_FERAL_BITE);
		this.fullbrightSingleTex(RatlantisItemRegistry.RAT_UPGRADE_NONBELIEVER);
		this.fullbrightSingleTex(RatlantisItemRegistry.RAT_UPGRADE_PSYCHIC);
		this.singleTex(RatlantisItemRegistry.RAT_UPGRADE_RATINATOR);
	}

	private void toBlock(Block b) {
		toBlockModel(b, blockPrefix(ForgeRegistries.BLOCKS.getKey(b).getPath()));
	}

	private void toBlockModel(Block b, ResourceLocation model) {
		withExistingParent(ForgeRegistries.BLOCKS.getKey(b).getPath(), model);
	}

	private void singleTexTool(RegistryObject<Item> item) {
		tool(item.getId().getPath(), false, itemPrefix(item.getId().getPath()));
	}

	private void trimmedArmor(RegistryObject<ArmorItem> armor) {
		ItemModelBuilder base = this.singleTex(armor);
		for (ItemModelGenerators.TrimModelData trim : ItemModelGenerators.GENERATED_TRIM_MODELS) {
			String material = trim.name();
			String name = armor.getId().getPath() + "_" + material + "_trim";
			ModelFile trimModel = this.withExistingParent(name, this.mcLoc("item/generated"))
					.texture("layer0", this.itemPrefix(armor.getId().getPath()))
					.texture("layer1", this.mcLoc("trims/items/" + armor.get().getType().getName() + "_trim_" + material));
			base.override().predicate(new ResourceLocation("trim_type"), trim.itemModelIndex()).model(trimModel).end();
		}
	}

	private ItemModelBuilder singleTex(RegistryObject<? extends Item> item) {
		return generated(item.getId().getPath(), false, itemPrefix(item.getId().getPath()));
	}

	private ItemModelBuilder singleTexRenderType(RegistryObject<? extends Item> item, String renderType) {
		return generatedRenderType(item.getId().getPath(), false, renderType, itemPrefix(item.getId().getPath()));
	}

	private ItemModelBuilder fullbrightSingleTex(RegistryObject<Item> item) {
		return generated(item.getId().getPath(), true, itemPrefix(item.getId().getPath()));
	}

	private ItemModelBuilder generated(String name, boolean fullbright, ResourceLocation... layers) {
		return buildItem(name, "item/generated", fullbright, null, layers);
	}

	private ItemModelBuilder generatedRenderType(String name, boolean fullbright, @Nullable String renderType, ResourceLocation... layers) {
		return buildItem(name, "item/generated", fullbright, renderType, layers);
	}

	private void tool(String name, boolean fullbright, ResourceLocation... layers) {
		buildItem(name, "item/handheld", fullbright, null, layers);
	}

	private ItemModelBuilder bow(String name, ResourceLocation... layers) {
		return buildItem(name, "item/bow", false, null, layers);
	}

	private ItemModelBuilder buildItem(String name, String parent, boolean fullbright, @Nullable String renderType, ResourceLocation... layers) {
		ItemModelBuilder builder = withExistingParent(name, parent);
		for (int i = 0; i < layers.length; i++) {
			builder = builder.texture("layer" + i, layers[i]);
		}
		if (renderType != null) {
			builder = builder.renderType(renderType);
		}
		if (fullbright)
			builder = builder.customLoader(ItemLayerModelBuilder::begin).emissive(15, 15, 0).renderType("minecraft:translucent", 0).end();
		return builder;
	}

	private ResourceLocation blockPrefix(String name) {
		return new ResourceLocation(RatsMod.MODID, "block/" + name);
	}

	private ResourceLocation itemPrefix(String name) {
		return new ResourceLocation(RatsMod.MODID, "item/" + name);
	}
}
