package com.github.alexthe666.rats.data;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.data.tags.RatsItemTags;
import com.github.alexthe666.rats.registry.RatsBlockRegistry;
import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.registry.RatsRecipeRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;
import java.util.function.Consumer;

public class RatsRecipes extends RatsUpgradeRecipes {
	public RatsRecipes(PackOutput output) {
		super(output);
	}

	@Override
	protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
		super.buildRecipes(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RatsBlockRegistry.BLOCK_OF_CHEESE.get())
				.pattern("CC")
				.pattern("CC")
				.define('C', RatsItemTags.NORMAL_CHEESE).unlockedBy("has_cheese", has(RatsItemTags.NORMAL_CHEESE))
				.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, RatsBlockRegistry.BLOCK_OF_BLUE_CHEESE.get())
				.requires(RatsBlockRegistry.BLOCK_OF_CHEESE.get()).unlockedBy("has_cheese", has(RatsBlockRegistry.BLOCK_OF_CHEESE.get()))
				.requires(Items.SUGAR).unlockedBy("has_sugar", has(Items.SUGAR))
				.requires(Items.BROWN_MUSHROOM).unlockedBy("has_mushroom", has(Items.BROWN_MUSHROOM))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RatsBlockRegistry.BLOCK_OF_BLUE_CHEESE.get())
				.pattern("CC")
				.pattern("CC")
				.define('C', RatsItemTags.BLUE_CHEESE).unlockedBy("has_cheese", has(RatsItemTags.BLUE_CHEESE))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "blue_cheese_compressed"));

		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RatsBlockRegistry.BLOCK_OF_NETHER_CHEESE.get())
				.pattern("CC")
				.pattern("CC")
				.define('C', RatsItemTags.NETHER_CHEESE).unlockedBy("has_cheese", has(RatsItemTags.NETHER_CHEESE))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "nether_cheese_compressed"));

		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RatsBlockRegistry.RAT_CAGE.get())
				.pattern("BBB")
				.pattern("B B")
				.pattern("IWI")
				.define('B', Items.IRON_BARS).unlockedBy("has_bars", has(Items.IRON_BARS))
				.define('I', Tags.Items.INGOTS_IRON).unlockedBy("has_ingot", has(Tags.Items.INGOTS_IRON))
				.define('W', Tags.Items.CROPS_WHEAT).unlockedBy("has_wheat", has(Tags.Items.CROPS_WHEAT))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RatsBlockRegistry.FISH_BARREL.get())
				.pattern("FFF")
				.pattern("FBF")
				.pattern("FFF")
				.define('F', ItemTags.FISHES).unlockedBy("has_fish", has(ItemTags.FISHES))
				.define('B', Items.BARREL).unlockedBy("has_barrel", has(Items.BARREL))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RatsBlockRegistry.RAT_CRAFTING_TABLE.get())
				.pattern("CCC")
				.pattern("CWC")
				.pattern("CCC")
				.define('C', RatsItemTags.STORAGE_BLOCKS_CHEESE).unlockedBy("has_cheese", has(RatsItemTags.STORAGE_BLOCKS_CHEESE))
				.define('W', Items.CRAFTING_TABLE).unlockedBy("has_table", has(Items.CRAFTING_TABLE))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RatsBlockRegistry.AUTO_CURDLER.get())
				.pattern("CGC")
				.pattern("CHC")
				.pattern("CUC")
				.define('C', RatsItemTags.STORAGE_BLOCKS_CHEESE).unlockedBy("has_cheese", has(RatsItemTags.STORAGE_BLOCKS_CHEESE))
				.define('G', Tags.Items.GLASS_COLORLESS).unlockedBy("has_glass", has(Tags.Items.GLASS_COLORLESS))
				.define('H', Items.HOPPER).unlockedBy("has_hopper", has(Items.HOPPER))
				.define('U', Items.CAULDRON).unlockedBy("has_cauldron", has(Items.CAULDRON))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, RatsBlockRegistry.RAT_TRAP.get())
				.pattern("IT ")
				.pattern("SSS")
				.define('T', Items.IRON_TRAPDOOR).unlockedBy("has_trapdoor", has(Items.IRON_TRAPDOOR))
				.define('I', Tags.Items.INGOTS_IRON).unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
				.define('S', ItemTags.WOODEN_SLABS).unlockedBy("has_slab", has(ItemTags.WOODEN_SLABS))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RatsBlockRegistry.RAT_UPGRADE_BLOCK.get())
				.pattern("UU")
				.pattern("UU")
				.define('U', RatsItemRegistry.RAT_UPGRADE_BASIC.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_BASIC.get()))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RatsBlockRegistry.DYE_SPONGE.get())
				.pattern(" D ")
				.pattern("DSD")
				.pattern(" D ")
				.define('S', Items.SPONGE).unlockedBy("has_sponge", has(Items.SPONGE))
				.define('D', Tags.Items.DYES).unlockedBy("has_dye", has(Tags.Items.DYES))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RatsBlockRegistry.CURSED_GARBAGE.get(), 8)
				.pattern("GGG")
				.pattern("GPG")
				.pattern("GGG")
				.define('G', RatsBlockRegistry.GARBAGE_PILE.get()).unlockedBy("has_garbage", has(RatsBlockRegistry.GARBAGE_PILE.get()))
				.define('P', RatsItemRegistry.PLAGUE_ESSENCE.get()).unlockedBy("has_essence", has(RatsItemRegistry.PLAGUE_ESSENCE.get()))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RatsBlockRegistry.COMPRESSED_GARBAGE.get())
				.pattern("GGG")
				.pattern("GGG")
				.pattern("GGG")
				.define('G', RatsBlockRegistry.GARBAGE_PILE.get()).unlockedBy("has_garbage", has(RatsBlockRegistry.GARBAGE_PILE.get()))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RatsBlockRegistry.PURIFIED_GARBAGE.get(), 8)
				.pattern("GGG")
				.pattern("GPG")
				.pattern("GGG")
				.define('G', RatsBlockRegistry.GARBAGE_PILE.get()).unlockedBy("has_garbage", has(RatsBlockRegistry.GARBAGE_PILE.get()))
				.define('P', RatsItemRegistry.PURIFYING_LIQUID.get()).unlockedBy("has_liquid", has(RatsItemRegistry.PURIFYING_LIQUID.get()))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RatsBlockRegistry.PIED_GARBAGE.get())
				.pattern("PPP")
				.pattern("PGP")
				.pattern("PPP")
				.define('G', RatsBlockRegistry.COMPRESSED_GARBAGE.get()).unlockedBy("has_garbage", has(RatsBlockRegistry.COMPRESSED_GARBAGE.get()))
				.define('P', RatsBlockRegistry.PIED_WOOL.get()).unlockedBy("has_wool", has(RatsBlockRegistry.PIED_WOOL.get()))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RatsBlockRegistry.MARBLED_CHEESE_RAW.get())
				.pattern("CQ")
				.pattern("QC")
				.define('C', RatsItemTags.CHEESE_ITEMS).unlockedBy("has_cheese", has(RatsItemTags.CHEESE_ITEMS))
				.define('Q', Tags.Items.GEMS_QUARTZ).unlockedBy("has_quartz", has(Tags.Items.GEMS_QUARTZ))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RatsBlockRegistry.MARBLED_CHEESE_RAW.get())
				.pattern("QC")
				.pattern("CQ")
				.define('C', RatsItemTags.CHEESE_ITEMS).unlockedBy("has_cheese", has(RatsItemTags.CHEESE_ITEMS))
				.define('Q', Tags.Items.GEMS_QUARTZ).unlockedBy("has_quartz", has(Tags.Items.GEMS_QUARTZ))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "marbled_cheese_raw_alt"));

		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RatsBlockRegistry.JACK_O_RATERN.get())
				.pattern("P")
				.pattern("R")
				.pattern("T")
				.define('P', Items.CARVED_PUMPKIN).unlockedBy("has_pumpkin", has(Items.CARVED_PUMPKIN))
				.define('R', RatsItemRegistry.RAW_RAT.get()).unlockedBy("has_rat", has(RatsItemRegistry.RAW_RAT.get()))
				.define('T', Items.TORCH).unlockedBy("has_torch", has(Items.TORCH))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RatsBlockRegistry.MANHOLE.get())
				.pattern("BBB")
				.pattern("TTT")
				.define('T', Items.IRON_TRAPDOOR).unlockedBy("has_trapdoor", has(Items.IRON_TRAPDOOR))
				.define('B', Items.IRON_BARS).unlockedBy("has_bars", has(Items.IRON_BARS))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RatsBlockRegistry.TRASH_CAN.get())
				.pattern(" N ")
				.pattern("III")
				.pattern(" C ")
				.define('N', Tags.Items.NUGGETS_IRON).unlockedBy("has_nugget", has(Tags.Items.NUGGETS_IRON))
				.define('I', Tags.Items.INGOTS_IRON).unlockedBy("has_ingot", has(Tags.Items.INGOTS_IRON))
				.define('C', Items.CAULDRON).unlockedBy("has_cauldron", has(Items.CAULDRON))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RatsBlockRegistry.RAT_ATTRACTOR.get())
				.pattern("RLR")
				.pattern("RCR")
				.define('R', Tags.Items.DUSTS_REDSTONE).unlockedBy("has_redstone", has(Tags.Items.DUSTS_REDSTONE))
				.define('L', Items.LANTERN).unlockedBy("has_lantern", has(Items.LANTERN))
				.define('C', RatsItemTags.STORAGE_BLOCKS_CHEESE).unlockedBy("has_cheese", has(RatsItemTags.STORAGE_BLOCKS_CHEESE))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RatsBlockRegistry.RAT_QUARRY.get())
				.pattern("BDB")
				.pattern("BCB")
				.pattern("BBB")
				.define('B', RatsItemTags.STORAGE_BLOCKS_BLUE_CHEESE).unlockedBy("has_blu", has(RatsItemTags.STORAGE_BLOCKS_BLUE_CHEESE))
				.define('D', Tags.Items.GEMS_DIAMOND).unlockedBy("has_diamond", has(Tags.Items.GEMS_DIAMOND))
				.define('C', RatsItemTags.CRAFTING_TABLES).unlockedBy("has_table", has(RatsItemTags.CRAFTING_TABLES))
				.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RatsItemRegistry.CHEESE.get(), 4)
				.requires(RatsBlockRegistry.BLOCK_OF_CHEESE.get())
				.unlockedBy("has_cheese", has(RatsBlockRegistry.BLOCK_OF_CHEESE.get()))
				.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RatsItemRegistry.BLUE_CHEESE.get(), 4)
				.requires(RatsBlockRegistry.BLOCK_OF_BLUE_CHEESE.get())
				.unlockedBy("has_cheese", has(RatsBlockRegistry.BLOCK_OF_BLUE_CHEESE.get()))
				.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RatsItemRegistry.NETHER_CHEESE.get(), 4)
				.requires(RatsBlockRegistry.BLOCK_OF_NETHER_CHEESE.get())
				.unlockedBy("has_cheese", has(RatsBlockRegistry.BLOCK_OF_NETHER_CHEESE.get()))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.LEATHER)
				.pattern("PP")
				.pattern("PP")
				.define('P', RatsItemRegistry.RAT_PELT.get()).unlockedBy("has_pelt", has(RatsItemRegistry.RAT_PELT.get()))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "rat_pelt_to_leather"));

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, RatsItemRegistry.CHEESE_STICK.get())
				.pattern(" C")
				.pattern("S ")
				.define('S', Tags.Items.RODS_WOODEN).unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN))
				.define('C', RatsItemTags.NORMAL_CHEESE).unlockedBy("has_cheese", has(RatsItemTags.NORMAL_CHEESE))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, RatsItemRegistry.RADIUS_STICK.get())
				.pattern(" C")
				.pattern("S ")
				.define('S', Tags.Items.RODS_WOODEN).unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN))
				.define('C', RatsItemRegistry.PLAGUE_ESSENCE.get()).unlockedBy("has_cheese", has(RatsItemRegistry.PLAGUE_ESSENCE.get()))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, RatsItemRegistry.PATROL_STICK.get())
				.pattern(" C")
				.pattern("S ")
				.define('S', Tags.Items.RODS_WOODEN).unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN))
				.define('C', RatsItemTags.NETHER_CHEESE).unlockedBy("has_cheese", has(RatsItemTags.NETHER_CHEESE))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, RatsItemRegistry.RAT_WHISTLE.get())
				.pattern("ICI")
				.pattern(" II")
				.define('I', Tags.Items.INGOTS_IRON).unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
				.define('C', RatsItemTags.NORMAL_CHEESE).unlockedBy("has_cheese", has(RatsItemTags.NORMAL_CHEESE))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, RatsItemRegistry.RAT_FLUTE.get())
				.pattern(" NS")
				.pattern("NST")
				.pattern("ST ")
				.define('N', Tags.Items.NUGGETS_IRON).unlockedBy("has_iron", has(Tags.Items.NUGGETS_IRON))
				.define('S', Items.SUGAR_CANE).unlockedBy("has_cane", has(Items.SUGAR_CANE))
				.define('T', ItemTags.TERRACOTTA).unlockedBy("has_cotta", has(ItemTags.TERRACOTTA))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, RatsItemRegistry.GILDED_RAT_FLUTE.get())
				.pattern("  S")
				.pattern(" F ")
				.pattern("T  ")
				.define('S', RatsItemRegistry.GOLDEN_RAT_SKULL.get()).unlockedBy("has_skull", has(RatsItemRegistry.GOLDEN_RAT_SKULL.get()))
				.define('F', RatsItemRegistry.RAT_FLUTE.get()).unlockedBy("has_flute", has(RatsItemRegistry.RAT_FLUTE.get()))
				.define('T', RatsItemRegistry.TANGLED_RAT_TAILS.get()).unlockedBy("has_tails", has(RatsItemRegistry.TANGLED_RAT_TAILS.get()))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, RatsItemRegistry.CHEF_TOQUE.get())
				.pattern("WW")
				.pattern("WW")
				.pattern("SS")
				.define('W', Items.WHITE_WOOL).unlockedBy("has_wool", has(Items.WHITE_WOOL))
				.define('S', Tags.Items.STRING).unlockedBy("has_string", has(Tags.Items.STRING))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, RatsItemRegistry.PIPER_HAT.get())
				.pattern("  F")
				.pattern("WWW")
				.pattern("W W")
				.define('W', RatsBlockRegistry.PIED_WOOL.get()).unlockedBy("has_wool", has(RatsBlockRegistry.PIED_WOOL.get()))
				.define('F', Items.FEATHER).unlockedBy("has_feather", has(Items.FEATHER))
				.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, RatsItemRegistry.ASSORTED_VEGETABLES.get())
				.requires(Ingredient.of(RatsItemTags.VEGETABLES), 9)
				.unlockedBy("has_a_veggie", has(RatsItemTags.VEGETABLES))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, RatsItemRegistry.RAT_BURGER.get())
				.pattern("B")
				.pattern("R")
				.pattern("B")
				.define('R', RatsItemRegistry.COOKED_RAT.get()).unlockedBy("has_rat", has(RatsItemRegistry.COOKED_RAT.get()))
				.define('B', Items.BREAD).unlockedBy("has_bread", has(Items.BREAD))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, RatsItemRegistry.RAT_SACK.get())
				.pattern("L L")
				.pattern("LCL")
				.pattern("LLL")
				.define('C', RatsItemTags.CHEESE_ITEMS).unlockedBy("has_cheese", has(RatsItemTags.CHEESE_ITEMS))
				.define('L', Tags.Items.LEATHER).unlockedBy("has_leather", has(Tags.Items.LEATHER))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, RatsItemRegistry.RAT_CAPTURE_NET.get())
				.pattern("NCN")
				.pattern("CSC")
				.pattern("NCN")
				.define('N', Tags.Items.NUGGETS_IRON).unlockedBy("has_iron", has(Tags.Items.NUGGETS_IRON))
				.define('C', Items.COBWEB).unlockedBy("has_cobweb", has(Items.COBWEB))
				.define('S', RatsItemRegistry.RAT_SACK.get()).unlockedBy("has_sack", has(RatsItemRegistry.RAT_SACK.get()))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.FEATHERY_WING.get())
				.pattern("CFF")
				.pattern("FF ")
				.define('C', Items.CHICKEN).unlockedBy("has_chicken", has(Items.CHICKEN))
				.define('F', Items.FEATHER).unlockedBy("has_feather", has(Items.FEATHER))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.DRAGON_WING.get())
				.pattern("RRR")
				.pattern("LLR")
				.pattern("  L")
				.define('R', Tags.Items.RODS_BLAZE).unlockedBy("has_rod", has(Tags.Items.RODS_BLAZE))
				.define('L', Tags.Items.LEATHER).unlockedBy("has_leather", has(Tags.Items.LEATHER))
				.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RatsItemRegistry.PURIFYING_LIQUID.get())
				.requires(RatsItemRegistry.GOLDEN_RAT_SKULL.get()).unlockedBy("has_skull", has(RatsItemRegistry.GOLDEN_RAT_SKULL.get()))
				.requires(Tags.Items.GEMS_DIAMOND).unlockedBy("has_diamond", has(Tags.Items.GEMS_DIAMOND))
				.requires(Items.GLASS_BOTTLE).unlockedBy("has_bottle", has(Items.GLASS_BOTTLE))
				.requires(RatsItemRegistry.PLAGUE_ESSENCE.get()).unlockedBy("has_essence", has(RatsItemRegistry.PLAGUE_ESSENCE.get()))
				.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RatsItemRegistry.CRIMSON_FLUID.get())
				.requires(RatsItemRegistry.GOLDEN_RAT_SKULL.get()).unlockedBy("has_skull", has(RatsItemRegistry.GOLDEN_RAT_SKULL.get()))
				.requires(RatsItemTags.NETHER_CHEESE).unlockedBy("has_cheese", has(RatsItemTags.NETHER_CHEESE))
				.requires(Items.GLASS_BOTTLE).unlockedBy("has_bottle", has(Items.GLASS_BOTTLE))
				.requires(Items.CRIMSON_FUNGUS).unlockedBy("has_fungus", has(Items.CRIMSON_FUNGUS))
				.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RatsItemRegistry.PLAGUE_ESSENCE.get(), 5)
				.requires(RatsItemRegistry.PLAGUE_SCYTHE.get()).unlockedBy("has_scythe", has(RatsItemRegistry.PLAGUE_SCYTHE.get()))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "scythe_to_essence"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.PLAGUE_DOCTORATE.get())
				.pattern(" E ")
				.pattern("EPE")
				.pattern(" E ")
				.define('E', RatsItemRegistry.PLAGUE_ESSENCE.get()).unlockedBy("has_essence", has(RatsItemRegistry.PLAGUE_ESSENCE.get()))
				.define('P', Items.PAPER).unlockedBy("has_paper", has(Items.PAPER))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, RatsItemRegistry.HERB_BUNDLE.get(), 3)
				.pattern("FFF")
				.pattern("FAF")
				.pattern("FFF")
				.define('A', RatsItemRegistry.ASSORTED_VEGETABLES.get()).unlockedBy("has_veggies", has(RatsItemRegistry.ASSORTED_VEGETABLES.get()))
				.define('F', ItemTags.FLOWERS).unlockedBy("has_flower", has(ItemTags.FLOWERS))
				.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, RatsItemRegistry.PLAGUE_STEW.get())
				.requires(RatsItemRegistry.HERB_BUNDLE.get()).unlockedBy("has_bundle", has(RatsItemRegistry.HERB_BUNDLE.get()))
				.requires(RatsItemRegistry.PLAGUE_LEECH.get()).unlockedBy("has_leech", has(RatsItemRegistry.PLAGUE_LEECH.get()))
				.requires(RatsItemRegistry.TREACLE.get()).unlockedBy("has_treacle", has(RatsItemRegistry.TREACLE.get()))
				.requires(Items.BOWL).unlockedBy("has_bowl", has(Items.BOWL))
				.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.BONE_MEAL)
				.requires(RatsItemRegistry.RAT_SKULL.get()).unlockedBy("has_skull", has(RatsItemRegistry.RAT_SKULL.get()))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "skull_to_bonemeal"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.GOLDEN_RAT_SKULL.get())
				.pattern("GGG")
				.pattern("GSG")
				.pattern("GGG")
				.define('S', RatsItemRegistry.RAT_SKULL.get()).unlockedBy("has_skull", has(RatsItemRegistry.RAT_SKULL.get()))
				.define('G', Tags.Items.INGOTS_GOLD).unlockedBy("has_gold", has(Tags.Items.INGOTS_GOLD))
				.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RatsItemRegistry.CORRUPT_RAT_SKULL.get())
				.requires(RatsItemRegistry.PLAGUE_SCYTHE.get()).unlockedBy("has_scythe", has(RatsItemRegistry.PLAGUE_SCYTHE.get()))
				.requires(RatsItemRegistry.TANGLED_RAT_TAILS.get()).unlockedBy("has_tails", has(RatsItemRegistry.TANGLED_RAT_TAILS.get()))
				.requires(RatsItemRegistry.GOLDEN_RAT_SKULL.get()).unlockedBy("has_skull", has(RatsItemRegistry.GOLDEN_RAT_SKULL.get()))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.FILTH.get())
				.pattern("GGG")
				.pattern("GGG")
				.pattern("GGG")
				.define('G', RatsBlockRegistry.COMPRESSED_GARBAGE.get()).unlockedBy("has_garbage", has(RatsBlockRegistry.COMPRESSED_GARBAGE.get()))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.FILTH_CORRUPTION.get())
				.pattern("GGG")
				.pattern("GGG")
				.pattern("GGG")
				.define('G', RatsItemRegistry.FILTH.get()).unlockedBy("has_garbage", has(RatsItemRegistry.FILTH.get()))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RATBOW_ESSENCE.get())
				.pattern("DDD")
				.pattern("DED")
				.pattern("DDD")
				.define('E', RatsItemRegistry.PLAGUE_ESSENCE.get()).unlockedBy("has_essence", has(RatsItemRegistry.PLAGUE_ESSENCE.get()))
				.define('D', Tags.Items.DYES).unlockedBy("has_dye", has(Tags.Items.DYES))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_WATER_BOTTLE.get())
				.pattern("P")
				.pattern("W")
				.pattern("I")
				.define('P', RatsItemTags.PLASTICS).unlockedBy("has_plastic", has(RatsItemTags.PLASTICS))
				.define('W', Items.WATER_BUCKET).unlockedBy("has_water", has(Items.WATER_BUCKET))
				.define('I', Tags.Items.NUGGETS_IRON).unlockedBy("has_iron", has(Tags.Items.NUGGETS_IRON))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_SEED_BOWL.get())
				.pattern("SSS")
				.pattern("P P")
				.pattern("PPP")
				.define('P', RatsItemTags.PLASTICS).unlockedBy("has_plastic", has(RatsItemTags.PLASTICS))
				.define('S', Tags.Items.SEEDS).unlockedBy("has_seeds", has(Tags.Items.SEEDS))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_BREEDING_LANTERN.get())
				.pattern(" S ")
				.pattern("RLR")
				.pattern(" R ")
				.define('S', Tags.Items.STRING).unlockedBy("has_string", has(Tags.Items.STRING))
				.define('L', Items.REDSTONE_LAMP).unlockedBy("has_lamp", has(Items.REDSTONE_LAMP))
				.define('R', Tags.Items.DYES_RED).unlockedBy("has_dye", has(Tags.Items.DYES_RED))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_WHEEL.get())
				.pattern(" B ")
				.pattern("BIB")
				.pattern("CBC")
				.define('C', RatsItemTags.CHEESE_ITEMS).unlockedBy("has_cheese", has(RatsItemTags.CHEESE_ITEMS))
				.define('B', Items.IRON_BARS).unlockedBy("has_bars", has(Items.IRON_BARS))
				.define('I', Tags.Items.INGOTS_IRON).unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
				.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, RatsItemRegistry.POTATO_PANCAKE.get())
				.requires(Ingredient.of(Items.BAKED_POTATO), 9)
				.unlockedBy("has_potato", has(Items.BAKED_POTATO))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.LITTLE_BLACK_WORM.get())
				.pattern("BPB")
				.pattern("BPB")
				.pattern("BPB")
				.define('P', RatsItemRegistry.POTATO_PANCAKE.get()).unlockedBy("has_pancake", has(RatsItemRegistry.POTATO_PANCAKE.get()))
				.define('B', RatsItemRegistry.LITTLE_BLACK_SQUASH_BALLS.get()).unlockedBy("has_balls", has(RatsItemRegistry.LITTLE_BLACK_SQUASH_BALLS.get()))
				.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RatsItemRegistry.TOKEN_FRAGMENT.get())
				.requires(Ingredient.of(RatsItemRegistry.TINY_COIN.get()), 9)
				.unlockedBy("has_coin", has(RatsItemRegistry.TINY_COIN.get()))
				.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RatsItemRegistry.TOKEN_PIECE.get())
				.requires(Ingredient.of(RatsItemRegistry.TOKEN_FRAGMENT.get()), 9)
				.unlockedBy("has_fragment", has(RatsItemRegistry.TOKEN_FRAGMENT.get()))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, RatsItemRegistry.FARMER_HAT.get())
				.pattern(" B ")
				.pattern("WWW")
				.define('W', Tags.Items.CROPS_WHEAT).unlockedBy("has_wheat", has(Tags.Items.CROPS_WHEAT))
				.define('B', Items.HAY_BLOCK).unlockedBy("has_hay", has(Items.HAY_BLOCK))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, RatsItemRegistry.FISHERMAN_HAT.get())
				.pattern(" F ")
				.pattern("SHS")
				.pattern("FFF")
				.define('F', ItemTags.FISHES).unlockedBy("has_fish", has(ItemTags.FISHES))
				.define('S', Tags.Items.STRING).unlockedBy("has_string", has(Tags.Items.STRING))
				.define('H', RatsItemRegistry.FARMER_HAT.get()).unlockedBy("has_hat", has(RatsItemRegistry.FARMER_HAT.get()))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, RatsItemRegistry.RAT_FEZ.get())
				.pattern(" S ")
				.pattern("WWW")
				.pattern("W W")
				.define('W', Items.RED_WOOL).unlockedBy("has_wool", has(Items.RED_WOOL))
				.define('S', RatsItemRegistry.STRING_CHEESE.get()).unlockedBy("has_cheese", has(RatsItemRegistry.STRING_CHEESE.get()))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, RatsItemRegistry.TOP_HAT.get())
				.pattern(" W ")
				.pattern(" W ")
				.pattern("CRC")
				.define('W', Items.BLACK_WOOL).unlockedBy("has_wool", has(Items.BLACK_WOOL))
				.define('C', Items.BLACK_CARPET).unlockedBy("has_carpet", has(Items.BLACK_CARPET))
				.define('R', Tags.Items.DYES_RED).unlockedBy("has_dye", has(Tags.Items.DYES_RED))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, RatsItemRegistry.HALO_HAT.get())
				.pattern("NGN")
				.pattern("N N")
				.pattern("NGN")
				.define('N', Tags.Items.NUGGETS_GOLD).unlockedBy("has_nugget", has(Tags.Items.NUGGETS_GOLD))
				.define('G', Tags.Items.INGOTS_GOLD).unlockedBy("has_ingot", has(Tags.Items.INGOTS_GOLD))
				.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RatsItemRegistry.RAT_BANNER_PATTERN.get())
				.requires(RatsItemRegistry.RAT_PELT.get()).unlockedBy("has_pelt", has(RatsItemRegistry.RAT_PELT.get()))
				.requires(Items.PAPER).unlockedBy("has_paper", has(Items.PAPER))
				.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RatsItemRegistry.CHEESE_BANNER_PATTERN.get())
				.requires(RatsBlockRegistry.BLOCK_OF_CHEESE.get()).unlockedBy("has_cheese", has(RatsBlockRegistry.BLOCK_OF_CHEESE.get()))
				.requires(Items.PAPER).unlockedBy("has_paper", has(Items.PAPER))
				.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RatsItemRegistry.RAC_BANNER_PATTERN.get())
				.requires(RatsItemRegistry.RAT_SKULL.get()).unlockedBy("has_skull", has(RatsItemRegistry.RAT_SKULL.get()))
				.requires(Items.PAPER).unlockedBy("has_paper", has(Items.PAPER))
				.save(consumer);

		for (DyeColor color : DyeColor.values()) {
			ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(RatsMod.MODID, "rat_hammock_" + color.getName()))))
					.pattern("SCS")
					.pattern("WWW")
					.define('S', Tags.Items.STRING).unlockedBy("has_string", has(Tags.Items.STRING))
					.define('C', RatsItemTags.CHEESE_ITEMS).unlockedBy("has_cheese", has(RatsItemTags.CHEESE_ITEMS))
					.define('W', ForgeRegistries.ITEMS.getValue(new ResourceLocation(color.getName() + "_wool"))).unlockedBy("has_wool", has(ForgeRegistries.ITEMS.getValue(new ResourceLocation(color.getName() + "_wool"))))
					.save(consumer, new ResourceLocation(RatsMod.MODID, "hammocks/rat_hammock_" + color.getName()));

			ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(RatsMod.MODID, "rat_hammock_" + color.getName()))))
					.requires(RatsItemTags.HAMMOCKS).unlockedBy("has_hammock", has(RatsItemTags.HAMMOCKS))
					.requires(color.getTag()).unlockedBy("has_dye", has(color.getTag()))
					.save(consumer, new ResourceLocation(RatsMod.MODID, "hammocks/hammock_dyed_" + color.getName()));

			ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(RatsMod.MODID, "rat_igloo_" + color.getName()))))
					.requires(RatsItemTags.IGLOOS).unlockedBy("has_igloo", has(RatsItemTags.IGLOOS))
					.requires(color.getTag()).unlockedBy("has_dye", has(color.getTag()))
					.save(consumer, new ResourceLocation(RatsMod.MODID, "igloos/rat_igloo_" + color.getName()));

			ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(RatsMod.MODID, "rat_tube_" + color.getName()))), 8)
					.pattern("TTT")
					.pattern("TDT")
					.pattern("TTT")
					.define('T', RatsItemTags.TUBES).unlockedBy("has_tube", has(RatsItemTags.TUBES))
					.define('D', color.getTag()).unlockedBy("has_dye", has(color.getTag()))
					.save(consumer, new ResourceLocation(RatsMod.MODID, "tubes/rat_tube_" + color.getName()));
		}

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(RatsMod.MODID, "rat_tube_white"))), 16)
				.pattern("PPP")
				.pattern("   ")
				.pattern("PPP")
				.define('P', RatsItemRegistry.RAW_PLASTIC.get()).unlockedBy("has_plastic", has(RatsItemRegistry.RAW_PLASTIC.get()))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "tubes/rat_tube_plastic"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(RatsMod.MODID, "rat_igloo_white"))))
				.pattern("PPP")
				.pattern("P P")
				.define('P', RatsItemRegistry.RAW_PLASTIC.get()).unlockedBy("has_plastic", has(RatsItemRegistry.RAW_PLASTIC.get()))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "igloos/rat_igloo_plastic"));


		SimpleCookingRecipeBuilder.smelting(Ingredient.of(RatsItemRegistry.RAW_RAT.get()), RecipeCategory.FOOD, RatsItemRegistry.COOKED_RAT.get(), 0.35F, 200).unlockedBy("has_rat", has(RatsItemRegistry.RAW_RAT.get())).save(consumer, new ResourceLocation(RatsMod.MODID, "cooked_rat_smelting"));
		SimpleCookingRecipeBuilder.smoking(Ingredient.of(RatsItemRegistry.RAW_RAT.get()), RecipeCategory.FOOD, RatsItemRegistry.COOKED_RAT.get(), 0.35F, 100).unlockedBy("has_rat", has(RatsItemRegistry.RAW_RAT.get())).save(consumer, new ResourceLocation(RatsMod.MODID, "cooked_rat_smoking"));
		SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(RatsItemRegistry.RAW_RAT.get()), RecipeCategory.FOOD, RatsItemRegistry.COOKED_RAT.get(), 0.35F, 600).unlockedBy("has_rat", has(RatsItemRegistry.RAW_RAT.get())).save(consumer, new ResourceLocation(RatsMod.MODID, "cooked_rat_campfire"));

		SimpleCookingRecipeBuilder.smelting(Ingredient.of(RatsItemRegistry.PLASTIC_WASTE.get()), RecipeCategory.MISC, RatsItemRegistry.RAW_PLASTIC.get(), 0.1F, 200).unlockedBy("has_plastic", has(RatsItemRegistry.PLASTIC_WASTE.get())).save(consumer, new ResourceLocation(RatsMod.MODID, "raw_plastic_smelting"));
		SimpleCookingRecipeBuilder.blasting(Ingredient.of(RatsItemRegistry.PLASTIC_WASTE.get()), RecipeCategory.MISC, RatsItemRegistry.RAW_PLASTIC.get(), 0.1F, 100).unlockedBy("has_plastic", has(RatsItemRegistry.PLASTIC_WASTE.get())).save(consumer, new ResourceLocation(RatsMod.MODID, "raw_plastic_blasting"));

		this.cooking(RatsItemRegistry.ASSORTED_VEGETABLES.get(), RatsItemRegistry.CONFIT_BYALDI.get(), 1, consumer);
		this.cooking(RatsItemRegistry.CHEESE.get(), RatsItemRegistry.STRING_CHEESE.get(), 4, consumer);
		this.cooking(RatsItemRegistry.CENTIPEDE.get(), RatsItemRegistry.POTATO_KNISHES.get(), 1, consumer);

		this.gemcutter(Items.COAL, RatsItemRegistry.LITTLE_BLACK_SQUASH_BALLS.get(), consumer);
		this.gemcutter(RatsItemRegistry.LITTLE_BLACK_WORM.get(), RatsItemRegistry.CENTIPEDE.get(), consumer);
	}

	private void cooking(ItemLike input, ItemLike output, int amount, Consumer<FinishedRecipe> consumer) {
		new SingleItemRecipeBuilder(RecipeCategory.FOOD, RatsRecipeRegistry.CHEF_SERIALIZER.get(), Ingredient.of(input), output, amount).unlockedBy("has_input", has(input)).save(consumer, new ResourceLocation(RatsMod.MODID, "chef/" + ForgeRegistries.ITEMS.getKey(output.asItem()).getPath()));
	}

	private void gemcutter(ItemLike input, ItemLike output, Consumer<FinishedRecipe> consumer) {
		new SingleItemRecipeBuilder(RecipeCategory.MISC, RatsRecipeRegistry.GEMCUTTER_SERIALIZER.get(), Ingredient.of(input), output, 1).unlockedBy("has_input", has(input)).save(consumer, new ResourceLocation(RatsMod.MODID, "gemcutter/" + ForgeRegistries.ITEMS.getKey(output.asItem()).getPath()));
	}
}
