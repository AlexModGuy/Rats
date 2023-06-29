package com.github.alexthe666.rats.data;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.data.tags.RatsItemTags;
import com.github.alexthe666.rats.registry.RatsBlockRegistry;
import com.github.alexthe666.rats.registry.RatsItemRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class RatsUpgradeRecipes extends RecipeProvider {
	public RatsUpgradeRecipes(PackOutput output) {
		super(output);
	}

	@Override
	protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_BASIC.get(), 4)
				.requires(RatsBlockRegistry.RAT_UPGRADE_BLOCK.get()).unlockedBy("has_block", has(RatsBlockRegistry.RAT_UPGRADE_BLOCK.get()))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/basic_upgrade_from_block"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_BASIC.get())
				.pattern("CCC")
				.pattern("C C")
				.pattern("CCC")
				.define('C', RatsItemTags.NORMAL_CHEESE).unlockedBy("has_cheese", has(RatsItemTags.NORMAL_CHEESE))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/basic_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_JURY_RIGGED.get())
				.pattern("DBG")
				.pattern("USU")
				.pattern("IBR")
				.define('D', Tags.Items.STORAGE_BLOCKS_DIAMOND).unlockedBy("has_diamond", has(Tags.Items.STORAGE_BLOCKS_DIAMOND))
				.define('G', Tags.Items.STORAGE_BLOCKS_GOLD).unlockedBy("has_gold", has(Tags.Items.STORAGE_BLOCKS_GOLD))
				.define('I', Tags.Items.STORAGE_BLOCKS_IRON).unlockedBy("has_iron", has(Tags.Items.STORAGE_BLOCKS_IRON))
				.define('R', Tags.Items.STORAGE_BLOCKS_REDSTONE).unlockedBy("has_redstone", has(Tags.Items.STORAGE_BLOCKS_REDSTONE))
				.define('U', RatsItemRegistry.RAT_UPGRADE_BASIC.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_BASIC.get()))
				.define('B', Tags.Items.SLIMEBALLS).unlockedBy("has_slime", has(Tags.Items.SLIMEBALLS))
				.define('S', Tags.Items.STRING).unlockedBy("has_string", has(Tags.Items.STRING))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/jury_rigged_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_SPEED.get())
				.pattern("DBD")
				.pattern("BUB")
				.pattern("DBD")
				.define('B', Tags.Items.STORAGE_BLOCKS_REDSTONE).unlockedBy("has_redstone", has(Tags.Items.STORAGE_BLOCKS_REDSTONE))
				.define('U', RatsItemRegistry.RAT_UPGRADE_BASIC.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_BASIC.get()))
				.define('D', Tags.Items.DUSTS_REDSTONE).unlockedBy("has_dust", has(Tags.Items.DUSTS_REDSTONE))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/speed_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_PLATTER.get())
				.pattern("III")
				.pattern("CPC")
				.pattern("CUC")
				.define('I', Tags.Items.INGOTS_IRON).unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
				.define('U', RatsItemRegistry.RAT_UPGRADE_BASIC.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_BASIC.get()))
				.define('P', Items.HEAVY_WEIGHTED_PRESSURE_PLATE).unlockedBy("has_plate", has(Items.HEAVY_WEIGHTED_PRESSURE_PLATE))
				.define('C', RatsItemTags.NORMAL_CHEESE).unlockedBy("has_cheese", has(RatsItemTags.NORMAL_CHEESE))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/platter_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_HEALTH.get())
				.pattern("BGB")
				.pattern("GUG")
				.pattern("BGB")
				.define('B', Items.BREAD).unlockedBy("has_bread", has(Items.BREAD))
				.define('U', RatsItemRegistry.RAT_UPGRADE_BASIC.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_BASIC.get()))
				.define('G', Items.GLISTERING_MELON_SLICE).unlockedBy("has_melon", has(Items.GLISTERING_MELON_SLICE))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/health_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_ARMOR.get())
				.pattern("ISI")
				.pattern("IUI")
				.pattern("IBI")
				.define('I', Tags.Items.INGOTS_IRON).unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
				.define('B', Tags.Items.STORAGE_BLOCKS_IRON).unlockedBy("has_block", has(Tags.Items.STORAGE_BLOCKS_IRON))
				.define('U', RatsItemRegistry.RAT_UPGRADE_BASIC.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_BASIC.get()))
				.define('S', Items.SHIELD).unlockedBy("has_shield", has(Items.SHIELD))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/armor_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_BOW.get())
				.pattern("ABA")
				.pattern("AUA")
				.pattern("AAA")
				.define('B', Items.BOW).unlockedBy("has_bow", has(Items.BOW))
				.define('U', RatsItemRegistry.RAT_UPGRADE_BASIC.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_BASIC.get()))
				.define('A', ItemTags.ARROWS).unlockedBy("has_arrow", has(ItemTags.ARROWS))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/bow_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_CROSSBOW.get())
				.pattern("ACA")
				.pattern("IUI")
				.pattern("AAA")
				.define('C', Items.CROSSBOW).unlockedBy("has_bow", has(Items.CROSSBOW))
				.define('I', Tags.Items.INGOTS_IRON).unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
				.define('U', RatsItemRegistry.RAT_UPGRADE_BASIC.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_BASIC.get()))
				.define('A', ItemTags.ARROWS).unlockedBy("has_arrow", has(ItemTags.ARROWS))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/crossbow_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_DEMON.get())
				.pattern("OCO")
				.pattern("CUC")
				.pattern("SCS")
				.define('O', Items.CRYING_OBSIDIAN).unlockedBy("has_obsidian", has(Items.CRYING_OBSIDIAN))
				.define('S', Items.SOUL_SAND).unlockedBy("has_sand", has(Items.SOUL_SAND))
				.define('U', RatsItemRegistry.RAT_UPGRADE_BASIC.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_BASIC.get()))
				.define('C', RatsItemTags.NETHER_CHEESE).unlockedBy("has_cheese", has(RatsItemTags.NETHER_CHEESE))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/demon_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_STRENGTH.get())
				.pattern("BSB")
				.pattern("IUI")
				.pattern("BSB")
				.define('I', Tags.Items.INGOTS_IRON).unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
				.define('U', RatsItemRegistry.RAT_UPGRADE_BASIC.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_BASIC.get()))
				.define('S', Items.IRON_SWORD).unlockedBy("has_sword", has(Items.IRON_SWORD))
				.define('B', Tags.Items.BONES).unlockedBy("has_bone", has(Tags.Items.BONES))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/strength_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_WARRIOR.get())
				.pattern("DSD")
				.pattern("HFA")
				.pattern("DBD")
				.define('D', Tags.Items.GEMS_DIAMOND).unlockedBy("has_diamond", has(Tags.Items.GEMS_DIAMOND))
				.define('B', Tags.Items.STORAGE_BLOCKS_DIAMOND).unlockedBy("has_block", has(Tags.Items.STORAGE_BLOCKS_DIAMOND))
				.define('H', RatsItemRegistry.RAT_UPGRADE_HEALTH.get()).unlockedBy("has_health", has(RatsItemRegistry.RAT_UPGRADE_HEALTH.get()))
				.define('F', RatsItemRegistry.RAT_UPGRADE_STRENGTH.get()).unlockedBy("has_strength", has(RatsItemRegistry.RAT_UPGRADE_STRENGTH.get()))
				.define('A', RatsItemRegistry.RAT_UPGRADE_ARMOR.get()).unlockedBy("has_armor", has(RatsItemRegistry.RAT_UPGRADE_ARMOR.get()))
				.define('S', Items.DIAMOND_SWORD).unlockedBy("has_sword", has(Items.DIAMOND_SWORD))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/warrior_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_GOD.get())
				.pattern("SWS")
				.pattern("WNW")
				.pattern("SWS")
				.define('N', Items.NETHER_STAR).unlockedBy("has_star", has(Items.NETHER_STAR))
				.define('W', RatsItemRegistry.RAT_UPGRADE_WARRIOR.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_WARRIOR.get()))
				.define('S', RatsItemRegistry.GOLDEN_RAT_SKULL.get()).unlockedBy("has_skull", has(RatsItemRegistry.GOLDEN_RAT_SKULL.get()))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/god_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_CHEF.get())
				.pattern("CTC")
				.pattern("CUC")
				.pattern("CGC")
				.define('C', Items.CAKE).unlockedBy("has_cake", has(Items.CAKE))
				.define('U', RatsItemRegistry.RAT_UPGRADE_BASIC.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_BASIC.get()))
				.define('T', RatsItemRegistry.CHEF_TOQUE.get()).unlockedBy("has_toque", has(RatsItemRegistry.CHEF_TOQUE.get()))
				.define('G', Items.GOLDEN_APPLE).unlockedBy("has_apple", has(Items.GOLDEN_APPLE))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/chef_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_CRAFTING.get())
				.pattern("PCP")
				.pattern("STA")
				.pattern("PUP")
				.define('C', RatsItemTags.NORMAL_CHEESE).unlockedBy("has_cheese", has(RatsItemTags.NORMAL_CHEESE))
				.define('U', RatsItemRegistry.RAT_UPGRADE_BASIC.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_BASIC.get()))
				.define('T', Items.CRAFTING_TABLE).unlockedBy("has_table", has(Items.CRAFTING_TABLE))
				.define('P', ItemTags.PLANKS).unlockedBy("has_planks", has(ItemTags.PLANKS))
				.define('S', Items.STONE_PICKAXE).unlockedBy("has_pick", has(Items.STONE_PICKAXE))
				.define('A', Items.STONE_AXE).unlockedBy("has_axe", has(Items.STONE_AXE))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/crafting_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_BLACKLIST.get())
				.pattern("DDD")
				.pattern("DUD")
				.pattern("DDD")
				.define('D', Tags.Items.DYES_BLACK).unlockedBy("has_dye", has(Tags.Items.DYES_BLACK))
				.define('U', RatsItemRegistry.RAT_UPGRADE_BASIC.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_BASIC.get()))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/blacklist_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_WHITELIST.get())
				.pattern("DDD")
				.pattern("DUD")
				.pattern("DDD")
				.define('D', Tags.Items.DYES_WHITE).unlockedBy("has_dye", has(Tags.Items.DYES_WHITE))
				.define('U', RatsItemRegistry.RAT_UPGRADE_BASIC.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_BASIC.get()))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/whitelist_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_REPLANTER.get())
				.pattern("SSS")
				.pattern("SUS")
				.pattern("SSS")
				.define('S', Tags.Items.SEEDS).unlockedBy("has_seeds", has(Tags.Items.SEEDS))
				.define('U', RatsItemRegistry.RAT_UPGRADE_BASIC.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_BASIC.get()))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/replanter_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_FLIGHT.get())
				.pattern("LCL")
				.pattern("WUW")
				.pattern("LCL")
				.define('C', RatsItemTags.NORMAL_CHEESE).unlockedBy("has_cheese", has(RatsItemTags.NORMAL_CHEESE))
				.define('U', RatsItemRegistry.RAT_UPGRADE_BASIC.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_BASIC.get()))
				.define('W', RatsItemRegistry.FEATHERY_WING.get()).unlockedBy("has_wing", has(RatsItemRegistry.FEATHERY_WING.get()))
				.define('L', Tags.Items.GEMS_LAPIS).unlockedBy("has_lapis", has(Tags.Items.GEMS_LAPIS))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/flight_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_DRAGON.get())
				.pattern("FAF")
				.pattern("WUW")
				.pattern("FSF")
				.define('F', Items.FIRE_CHARGE).unlockedBy("has_charge", has(Items.FIRE_CHARGE))
				.define('U', RatsItemRegistry.RAT_UPGRADE_FLIGHT.get()).unlockedBy("has_flight", has(RatsItemRegistry.RAT_UPGRADE_FLIGHT.get()))
				.define('A', RatsItemRegistry.RAT_UPGRADE_ASBESTOS.get()).unlockedBy("has_asbestos", has(RatsItemRegistry.RAT_UPGRADE_ASBESTOS.get()))
				.define('S', RatsItemRegistry.RAT_UPGRADE_WARRIOR.get()).unlockedBy("has_warrior", has(RatsItemRegistry.RAT_UPGRADE_WARRIOR.get()))
				.define('W', RatsItemRegistry.DRAGON_WING.get()).unlockedBy("has_wing", has(RatsItemRegistry.DRAGON_WING.get()))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/dragon_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_BEE.get())
				.pattern("BCB")
				.pattern("CUC")
				.pattern("BCB")
				.define('B', Items.HONEY_BOTTLE).unlockedBy("has_bottle", has(Items.HONEY_BOTTLE))
				.define('U', RatsItemRegistry.RAT_UPGRADE_FLIGHT.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_FLIGHT.get()))
				.define('C', Items.HONEYCOMB_BLOCK).unlockedBy("has_comb", has(Items.HONEYCOMB_BLOCK))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/bee_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_AQUATIC.get())
				.pattern("BFB")
				.pattern("FUF")
				.pattern("BFB")
				.define('B', RatsBlockRegistry.FISH_BARREL.get()).unlockedBy("has_barrel", has(RatsBlockRegistry.FISH_BARREL.get()))
				.define('U', RatsItemRegistry.RAT_UPGRADE_UNDERWATER.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_UNDERWATER.get()))
				.define('F', ItemTags.FISHES).unlockedBy("has_feesh", has(ItemTags.FISHES))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/aquatic_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_ENDER.get())
				.pattern("PEP")
				.pattern("EUE")
				.pattern("PEP")
				.define('P', Items.ENDER_PEARL).unlockedBy("has_pearl", has(Items.ENDER_PEARL))
				.define('U', RatsItemRegistry.RAT_UPGRADE_BASIC.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_BASIC.get()))
				.define('E', Items.ENDER_EYE).unlockedBy("has_eye", has(Items.ENDER_EYE))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/ender_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_LUMBERJACK.get())
				.pattern("RAR")
				.pattern("BUB")
				.pattern("RSR")
				.define('A', Items.IRON_AXE).unlockedBy("has_axe", has(Items.IRON_AXE))
				.define('U', RatsItemRegistry.RAT_UPGRADE_BASIC.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_BASIC.get()))
				.define('S', ItemTags.SAPLINGS).unlockedBy("has_sapling", has(ItemTags.SAPLINGS))
				.define('R', Items.RED_WOOL).unlockedBy("has_red_wool", has(Items.RED_WOOL))
				.define('B', Items.BLACK_WOOL).unlockedBy("has_black_wool", has(Items.BLACK_WOOL))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/lumberjack_upgrade"));

//		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_MINER_ORE.get())
//				.pattern("IPI")
//				.pattern("CUC")
//				.pattern("ICI")
//				.define('P', Items.IRON_PICKAXE).unlockedBy("has_pickaxe", has(Items.IRON_PICKAXE))
//				.define('C', ItemTags.COALS).unlockedBy("has_coal", has(ItemTags.COALS))
//				.define('U', RatsItemRegistry.RAT_UPGRADE_UNDERWATER.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_UNDERWATER.get()))
//				.define('I', ItemTags.IRON_ORES).unlockedBy("has_ore", has(ItemTags.IRON_ORES))
//				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/miner_ore_upgrade"));
//
//		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_MINER.get())
//				.pattern("GPG")
//				.pattern("GUG")
//				.pattern("GIG")
//				.define('P', Items.DIAMOND_PICKAXE).unlockedBy("has_pickaxe", has(Items.DIAMOND_PICKAXE))
//				.define('U', RatsItemRegistry.RAT_UPGRADE_MINER_ORE.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_MINER_ORE.get()))
//				.define('I', Tags.Items.STORAGE_BLOCKS_IRON).unlockedBy("has_iron", has(Tags.Items.STORAGE_BLOCKS_IRON))
//				.define('G', Tags.Items.INGOTS_GOLD).unlockedBy("has_gold", has(Tags.Items.INGOTS_GOLD))
//				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/miner_upgrade"));

		//TODO replace base upgrade with miner once its reworked
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_QUARRY.get(), 3)
				.pattern("PFP")
				.pattern("FUF")
				.pattern("CCC")
				.define('P', RatsItemRegistry.RAT_PAW.get()).unlockedBy("has_paw", has(RatsItemRegistry.RAT_PAW.get()))
				.define('U', RatsItemRegistry.RAT_UPGRADE_BASIC.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_BASIC.get()))
				.define('F', RatsItemRegistry.FILTH.get()).unlockedBy("has_filth", has(RatsItemRegistry.FILTH.get()))
				.define('C', RatsItemTags.STORAGE_BLOCKS_BLUE_CHEESE).unlockedBy("has_cheese", has(RatsItemTags.STORAGE_BLOCKS_BLUE_CHEESE))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/quarry_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_FARMER.get())
				.pattern("BFB")
				.pattern("HUH")
				.pattern("SSS")
				.define('B', Items.HAY_BLOCK).unlockedBy("has_bale", has(Items.HAY_BLOCK))
				.define('H', Items.IRON_HOE).unlockedBy("has_hoe", has(Items.IRON_HOE))
				.define('U', RatsItemRegistry.RAT_UPGRADE_GARDENER.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_GARDENER.get()))
				.define('F', RatsItemRegistry.FARMER_HAT.get()).unlockedBy("has_hat", has(RatsItemRegistry.FARMER_HAT.get()))
				.define('S', Tags.Items.SEEDS).unlockedBy("has_seeds", has(Tags.Items.SEEDS))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/farmer_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_BREEDER.get())
				.pattern("SFS")
				.pattern("CUC")
				.pattern("WEW")
				.define('E', Tags.Items.EGGS).unlockedBy("has_egg", has(Items.HAY_BLOCK))
				.define('W', Tags.Items.CROPS_WHEAT).unlockedBy("has_wheat", has(Tags.Items.CROPS_WHEAT))
				.define('C', Items.CARROT).unlockedBy("has_carrot", has(Items.CARROT))
				.define('U', RatsItemRegistry.RAT_UPGRADE_BASIC.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_BASIC.get()))
				.define('F', RatsItemRegistry.FARMER_HAT.get()).unlockedBy("has_hat", has(RatsItemRegistry.FARMER_HAT.get()))
				.define('S', Tags.Items.SEEDS).unlockedBy("has_seeds", has(Tags.Items.SEEDS))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/breeder_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_FISHERMAN.get())
				.pattern("BHB")
				.pattern("RUR")
				.pattern("FFF")
				.define('B', RatsBlockRegistry.FISH_BARREL.get()).unlockedBy("has_barrel", has(RatsBlockRegistry.FISH_BARREL.get()))
				.define('H', RatsItemRegistry.FISHERMAN_HAT.get()).unlockedBy("has_hat", has(RatsItemRegistry.FISHERMAN_HAT.get()))
				.define('U', RatsItemRegistry.RAT_UPGRADE_BASIC.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_BASIC.get()))
				.define('R', Items.FISHING_ROD).unlockedBy("has_rod", has(Items.FISHING_ROD))
				.define('F', ItemTags.FISHES).unlockedBy("has_fish", has(ItemTags.FISHES))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/fisherman_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_TICK_ACCELERATOR.get())
				.pattern("LTL")
				.pattern("CUC")
				.pattern("LCL")
				.define('T', RatsItemRegistry.TANGLED_RAT_TAILS.get()).unlockedBy("has_tails", has(RatsItemRegistry.TANGLED_RAT_TAILS.get()))
				.define('L', Tags.Items.STORAGE_BLOCKS_LAPIS).unlockedBy("has_lapis", has(Tags.Items.STORAGE_BLOCKS_LAPIS))
				.define('U', RatsItemRegistry.RAT_UPGRADE_BASIC.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_BASIC.get()))
				.define('C', Items.CLOCK).unlockedBy("has_clock", has(Items.CLOCK))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/tick_accelerator_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_ASBESTOS.get())
				.pattern("WBW")
				.pattern("GUG")
				.pattern("WBW")
				.define('B', Items.LAVA_BUCKET).unlockedBy("has_bucket", has(Items.LAVA_BUCKET))
				.define('G', Tags.Items.GLASS).unlockedBy("has_glass", has(Tags.Items.GLASS))
				.define('U', RatsItemRegistry.RAT_UPGRADE_BASIC.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_BASIC.get()))
				.define('W', Items.WHITE_WOOL).unlockedBy("has_wool", has(Items.WHITE_WOOL))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/asbestos_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_UNDERWATER.get())
				.pattern("GGG")
				.pattern("FUF")
				.pattern("SWS")
				.define('G', Tags.Items.GRAVEL).unlockedBy("has_gravel", has(Tags.Items.GRAVEL))
				.define('F', Items.PUFFERFISH).unlockedBy("has_fish", has(Items.PUFFERFISH))
				.define('U', RatsItemRegistry.RAT_UPGRADE_BASIC.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_BASIC.get()))
				.define('W', Items.WATER_BUCKET).unlockedBy("has_bucket", has(Items.WATER_BUCKET))
				.define('S', Tags.Items.SAND).unlockedBy("has_sand", has(Tags.Items.SAND))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/underwater_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_POISON.get())
				.pattern("EEE")
				.pattern("GUG")
				.pattern("PPP")
				.define('E', Items.SPIDER_EYE).unlockedBy("has_eye", has(Items.SPIDER_EYE))
				.define('G', Tags.Items.DUSTS_GLOWSTONE).unlockedBy("has_dust", has(Tags.Items.DUSTS_GLOWSTONE))
				.define('U', RatsItemRegistry.RAT_UPGRADE_BASIC.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_BASIC.get()))
				.define('P', Items.POISONOUS_POTATO).unlockedBy("has_potato", has(Items.POISONOUS_POTATO))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/poison_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_DAMAGE_PROTECTION.get())
				.pattern("CCC")
				.pattern("AUP")
				.pattern("GWG")
				.define('C', RatsItemTags.STORAGE_BLOCKS_CHEESE).unlockedBy("has_cheese", has(RatsItemTags.STORAGE_BLOCKS_CHEESE))
				.define('A', RatsItemRegistry.RAT_UPGRADE_ASBESTOS.get()).unlockedBy("has_asbestos", has(RatsItemRegistry.RAT_UPGRADE_ASBESTOS.get()))
				.define('U', RatsItemRegistry.RAT_UPGRADE_UNDERWATER.get()).unlockedBy("has_underwater", has(RatsItemRegistry.RAT_UPGRADE_UNDERWATER.get()))
				.define('P', RatsItemRegistry.RAT_UPGRADE_POISON.get()).unlockedBy("has_poison", has(RatsItemRegistry.RAT_UPGRADE_POISON.get()))
				.define('G', Items.GOLDEN_APPLE).unlockedBy("has_apple", has(Items.GOLDEN_APPLE))
				.define('W', RatsItemRegistry.FEATHERY_WING.get()).unlockedBy("has_wing", has(RatsItemRegistry.FEATHERY_WING.get()))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/damage_protection_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_ORE_DOUBLING.get())
				.pattern("NDN")
				.pattern("CUC")
				.pattern("NIN")
				.define('N', RatsItemRegistry.RAT_NUGGET.get()).unlockedBy("has_nugget", has(RatsItemRegistry.RAT_NUGGET.get()))
				.define('D', Tags.Items.GEMS_DIAMOND).unlockedBy("has_diamond", has(Tags.Items.GEMS_DIAMOND))
				.define('U', RatsItemRegistry.RAT_UPGRADE_BASIC.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_BASIC.get()))
				.define('C', Tags.Items.STORAGE_BLOCKS_COAL).unlockedBy("has_coal", has(Tags.Items.STORAGE_BLOCKS_COAL))
				.define('I', Tags.Items.STORAGE_BLOCKS_IRON).unlockedBy("has_iron", has(Tags.Items.STORAGE_BLOCKS_IRON))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/ore_doubling_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_BASIC_ENERGY.get())
				.pattern("GTG")
				.pattern("GUG")
				.pattern("GRG")
				.define('T', Items.REDSTONE_TORCH).unlockedBy("has_torch", has(Items.REDSTONE_TORCH))
				.define('U', RatsItemRegistry.RAT_UPGRADE_BASIC.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_BASIC.get()))
				.define('R', Tags.Items.DUSTS_REDSTONE).unlockedBy("has_redstone", has(Tags.Items.DUSTS_REDSTONE))
				.define('G', Tags.Items.INGOTS_GOLD).unlockedBy("has_gold", has(Tags.Items.INGOTS_GOLD))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/basic_energy_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_ADVANCED_ENERGY.get())
				.pattern("DDD")
				.pattern("GUG")
				.pattern("RRR")
				.define('D', Tags.Items.GEMS_DIAMOND).unlockedBy("has_diamond", has(Tags.Items.GEMS_DIAMOND))
				.define('U', RatsItemRegistry.RAT_UPGRADE_BASIC_ENERGY.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_BASIC_ENERGY.get()))
				.define('R', Tags.Items.STORAGE_BLOCKS_REDSTONE).unlockedBy("has_redstone", has(Tags.Items.STORAGE_BLOCKS_REDSTONE))
				.define('G', Tags.Items.INGOTS_GOLD).unlockedBy("has_gold", has(Tags.Items.INGOTS_GOLD))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/advanced_energy_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_ELITE_ENERGY.get())
				.pattern("QBQ")
				.pattern("BUB")
				.pattern("QBQ")
				.define('Q', Tags.Items.GEMS_QUARTZ).unlockedBy("has_quartz", has(Tags.Items.GEMS_QUARTZ))
				.define('U', RatsItemRegistry.RAT_UPGRADE_ADVANCED_ENERGY.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_ADVANCED_ENERGY.get()))
				.define('B', Tags.Items.RODS_BLAZE).unlockedBy("has_blaze", has(Tags.Items.RODS_BLAZE))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/elite_energy_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_EXTREME_ENERGY.get())
				.pattern("FCF")
				.pattern("CUC")
				.pattern("FCF")
				.define('F', Items.POPPED_CHORUS_FRUIT).unlockedBy("has_fruit", has(Items.POPPED_CHORUS_FRUIT))
				.define('U', RatsItemRegistry.RAT_UPGRADE_ELITE_ENERGY.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_ELITE_ENERGY.get()))
				.define('C', RatsItemRegistry.CHARGED_CREEPER_CHUNK.get()).unlockedBy("has_chunk", has(RatsItemRegistry.CHARGED_CREEPER_CHUNK.get()))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/extreme_energy_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_BUCKET.get())
				.pattern("GBG")
				.pattern("GUG")
				.pattern("GGG")
				.define('B', Items.BUCKET).unlockedBy("has_bucket", has(Items.BUCKET))
				.define('U', RatsItemRegistry.RAT_UPGRADE_BASIC.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_BASIC.get()))
				.define('G', Tags.Items.GLASS).unlockedBy("has_glass", has(Tags.Items.GLASS))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/bucket_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_BIG_BUCKET.get())
				.pattern("BDB")
				.pattern("DUD")
				.pattern("BSB")
				.define('B', Items.BUCKET).unlockedBy("has_bucket", has(Items.CHORUS_FRUIT))
				.define('D', Tags.Items.GEMS_DIAMOND).unlockedBy("has_diamond", has(Tags.Items.GEMS_DIAMOND))
				.define('U', RatsItemRegistry.RAT_UPGRADE_BUCKET.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_BUCKET.get()))
				.define('S', Tags.Items.SLIMEBALLS).unlockedBy("has_slime", has(Tags.Items.SLIMEBALLS))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/big_bucket_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_MILKER.get())
				.pattern("LBL")
				.pattern("BUB")
				.pattern("LML")
				.define('L', Tags.Items.LEATHER).unlockedBy("has_leather", has(Tags.Items.LEATHER))
				.define('B', Items.BEEF).unlockedBy("has_beef", has(Items.BEEF))
				.define('U', RatsItemRegistry.RAT_UPGRADE_BUCKET.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_BUCKET.get()))
				.define('M', Items.MILK_BUCKET).unlockedBy("has_milk", has(Items.MILK_BUCKET))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/milker_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_SHEARS.get())
				.pattern("WMW")
				.pattern("MUM")
				.pattern("WSW")
				.define('W', ItemTags.WOOL).unlockedBy("has_wool", has(ItemTags.WOOL))
				.define('M', Items.MUTTON).unlockedBy("has_mutton", has(Items.MUTTON))
				.define('U', RatsItemRegistry.RAT_UPGRADE_BASIC.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_BASIC.get()))
				.define('S', Items.SHEARS).unlockedBy("has_shears", has(Items.SHEARS))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/shears_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_ARISTOCRAT.get())
				.pattern("STS")
				.pattern("GUG")
				.pattern("CEC")
				.define('S', RatsItemRegistry.GOLDEN_RAT_SKULL.get()).unlockedBy("has_skull", has(RatsItemRegistry.GOLDEN_RAT_SKULL.get()))
				.define('T', RatsItemRegistry.TOP_HAT.get()).unlockedBy("has_hat", has(RatsItemRegistry.TOP_HAT.get()))
				.define('G', Tags.Items.INGOTS_GOLD).unlockedBy("has_gold", has(Tags.Items.INGOTS_GOLD))
				.define('U', RatsItemRegistry.RAT_UPGRADE_BASIC.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_BASIC.get()))
				.define('C', Items.GOLDEN_CARROT).unlockedBy("has_carrot", has(Items.GOLDEN_CARROT))
				.define('E', Tags.Items.STORAGE_BLOCKS_EMERALD).unlockedBy("has_emerald", has(Tags.Items.STORAGE_BLOCKS_EMERALD))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/aristocrat_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_TNT.get())
				.pattern("GTG")
				.pattern("TUT")
				.pattern("GTG")
				.define('G', Tags.Items.GUNPOWDER).unlockedBy("has_gunpowder", has(Tags.Items.GUNPOWDER))
				.define('T', Items.TNT).unlockedBy("has_tnt", has(Items.TNT))
				.define('U', RatsItemRegistry.RAT_UPGRADE_BASIC.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_BASIC.get()))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/tnt_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_TNT_SURVIVOR.get())
				.pattern("CTC")
				.pattern("TUT")
				.pattern("CTC")
				.define('C', RatsItemRegistry.CHARGED_CREEPER_CHUNK.get()).unlockedBy("has_chunk", has(RatsItemRegistry.CHARGED_CREEPER_CHUNK.get()))
				.define('T', RatsItemRegistry.RAT_UPGRADE_TNT.get()).unlockedBy("has_tnt", has(RatsItemRegistry.RAT_UPGRADE_TNT.get()))
				.define('U', RatsItemRegistry.RAT_UPGRADE_DAMAGE_PROTECTION.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_DAMAGE_PROTECTION.get()))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/tnt_survivor_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_PLACER.get())
				.pattern("DCD")
				.pattern("CUC")
				.pattern("DCD")
				.define('C', Tags.Items.COBBLESTONE_MOSSY).unlockedBy("has_cobble", has(Tags.Items.COBBLESTONE_MOSSY))
				.define('D', Tags.Items.DYES_PINK).unlockedBy("has_dye", has(Tags.Items.DYES_PINK))
				.define('U', RatsItemRegistry.RAT_UPGRADE_BASIC.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_BASIC.get()))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/placer_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_VOODOO.get())
				.pattern("PHP")
				.pattern("TUT")
				.pattern("PSP")
				.define('P', RatsItemRegistry.PLAGUE_ESSENCE.get()).unlockedBy("has_essence", has(RatsItemRegistry.PLAGUE_ESSENCE.get()))
				.define('H', RatsItemRegistry.TOP_HAT.get()).unlockedBy("has_hat", has(RatsItemRegistry.TOP_HAT.get()))
				.define('T', Items.TOTEM_OF_UNDYING).unlockedBy("has_totem", has(Items.TOTEM_OF_UNDYING))
				.define('U', RatsItemRegistry.RAT_UPGRADE_HEALTH.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_HEALTH.get()))
				.define('S', RatsItemRegistry.RAT_SKULL.get()).unlockedBy("has_skull", has(RatsItemRegistry.RAT_SKULL.get()))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/voodoo_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_GEMCUTTER.get())
				.pattern("FEF")
				.pattern("DUD")
				.pattern("FSF")
				.define('F', Items.FLINT).unlockedBy("has_flint", has(Items.FLINT))
				.define('E', Tags.Items.GEMS_EMERALD).unlockedBy("has_emerald", has(Tags.Items.GEMS_EMERALD))
				.define('D', Tags.Items.GEMS_DIAMOND).unlockedBy("has_diamond", has(Tags.Items.GEMS_DIAMOND))
				.define('U', RatsItemRegistry.RAT_UPGRADE_BASIC.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_BASIC.get()))
				.define('S', Items.STONECUTTER).unlockedBy("has_stonecutter", has(Items.STONECUTTER))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/gemcutter_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_PICKPOCKET.get())
				.pattern("EGE")
				.pattern("PUP")
				.pattern("EBE")
				.define('E', Tags.Items.GEMS_EMERALD).unlockedBy("has_emerald", has(Tags.Items.GEMS_EMERALD))
				.define('G', RatsItemRegistry.GOLDEN_RAT_SKULL.get()).unlockedBy("has_skull", has(RatsItemRegistry.GOLDEN_RAT_SKULL.get()))
				.define('U', RatsItemRegistry.RAT_UPGRADE_BASIC.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_BASIC.get()))
				.define('P', RatsItemRegistry.RAT_PAW.get()).unlockedBy("has_paw", has(RatsItemRegistry.RAT_PAW.get()))
				.define('B', Tags.Items.STORAGE_BLOCKS_EMERALD).unlockedBy("has_emerald_block", has(Tags.Items.STORAGE_BLOCKS_EMERALD))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/pickpocket_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_GARDENER.get())
				.pattern("SFS")
				.pattern("FUF")
				.pattern("SFS")
				.define('F', ItemTags.FLOWERS).unlockedBy("has_flower", has(ItemTags.FLOWERS))
				.define('S', Tags.Items.SEEDS).unlockedBy("has_seeds", has(Tags.Items.SEEDS))
				.define('U', RatsItemRegistry.RAT_UPGRADE_BASIC.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_BASIC.get()))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/gardener_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_IDOL.get())
				.pattern("GCG")
				.pattern("SUS")
				.pattern("GRG")
				.define('G', Tags.Items.STORAGE_BLOCKS_GOLD).unlockedBy("has_gold", has(Tags.Items.STORAGE_BLOCKS_GOLD))
				.define('U', RatsItemRegistry.RAT_UPGRADE_BASIC.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_BASIC.get()))
				.define('S', RatsItemRegistry.GOLDEN_RAT_SKULL.get()).unlockedBy("has_skull", has(RatsItemRegistry.GOLDEN_RAT_SKULL.get()))
				.define('C', RatsItemRegistry.RAT_KING_CROWN.get()).unlockedBy("has_crown", has(RatsItemRegistry.RAT_KING_CROWN.get()))
				.define('R', Tags.Items.STORAGE_BLOCKS_RAW_GOLD).unlockedBy("has_raw_gold", has(Tags.Items.STORAGE_BLOCKS_RAW_GOLD))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/idol_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_SCULKED.get())
				.pattern("SES")
				.pattern("PUP")
				.pattern("WEW")
				.define('E', Items.ECHO_SHARD).unlockedBy("has_shard", has(Items.ECHO_SHARD))
				.define('W', ItemTags.WOOL)
				.define('S', Items.SCULK)
				.define('P', RatsItemRegistry.RAT_PAW.get()).unlockedBy("has_paw", has(RatsItemRegistry.RAT_PAW.get()))
				.define('U', RatsItemRegistry.RAT_UPGRADE_BASIC.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_BASIC.get()))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/sculked_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_TIME_MANIPULATOR.get())
				.pattern("TCT")
				.pattern("ASA")
				.pattern("TCT")
				.define('T', Items.CLOCK)
				.define('S', Items.NETHER_STAR).unlockedBy("has_star", has(Items.NETHER_STAR))
				.define('A', RatsItemRegistry.RAT_UPGRADE_TICK_ACCELERATOR.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_TICK_ACCELERATOR.get()))
				.define('C', RatsItemRegistry.CHARGED_CREEPER_CHUNK.get()).unlockedBy("has_chunk", has(RatsItemRegistry.CHARGED_CREEPER_CHUNK.get()))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/time_manipulator_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_ENCHANTER.get())
				.pattern("BEB")
				.pattern("SUS")
				.pattern("BXB")
				.define('B', Items.BOOK).unlockedBy("has_book", has(Items.BOOK))
				.define('E', Items.ENCHANTING_TABLE).unlockedBy("has_table", has(Items.ENCHANTING_TABLE))
				.define('S', Tags.Items.BOOKSHELVES).unlockedBy("has_shelf", has(Tags.Items.BOOKSHELVES))
				.define('U', RatsItemRegistry.RAT_UPGRADE_BASIC.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_BASIC.get()))
				.define('X', Items.EXPERIENCE_BOTTLE).unlockedBy("has_bottle", has(Items.EXPERIENCE_BOTTLE))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/enchanter_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_DISENCHANTER.get())
				.pattern("SBS")
				.pattern("FUF")
				.pattern("FGF")
				.define('B', Items.BOOK).unlockedBy("has_book", has(Items.BOOK))
				.define('S', Items.STONE_SLAB).unlockedBy("has_slab", has(Items.STONE_SLAB))
				.define('F', Tags.Items.STONE).unlockedBy("has_shelf", has(Tags.Items.STONE))
				.define('U', RatsItemRegistry.RAT_UPGRADE_BASIC.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_BASIC.get()))
				.define('G', Items.GRINDSTONE).unlockedBy("has_grindstone", has(Items.GRINDSTONE))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/disenchanter_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_CHRISTMAS.get())
				.pattern("HHH")
				.pattern("CUC")
				.pattern("SSS")
				.define('H', RatsItemRegistry.SANTA_HAT.get()).unlockedBy("has_hat", has(RatsItemRegistry.SANTA_HAT.get()))
				.define('S', Items.SPRUCE_SAPLING).unlockedBy("has_sapling", has(Items.SPRUCE_SAPLING))
				.define('C', ItemTags.COALS).unlockedBy("has_coal", has(ItemTags.COALS))
				.define('U', RatsItemRegistry.RAT_UPGRADE_BASIC.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_BASIC.get()))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/christmas_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_NO_FLUTE.get())
				.pattern("CFC")
				.pattern("RUR")
				.pattern("CRC")
				.define('F', RatsItemRegistry.RAT_FLUTE.get()).unlockedBy("has_flute", has(RatsItemRegistry.RAT_FLUTE.get()))
				.define('C', Items.CLAY_BALL).unlockedBy("has_clay", has(Items.CLAY_BALL))
				.define('R', Tags.Items.DYES_RED).unlockedBy("has_dye", has(Tags.Items.DYES_RED))
				.define('U', RatsItemRegistry.RAT_UPGRADE_BASIC.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_BASIC.get()))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/flute_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_CARRAT.get())
				.pattern("GCG")
				.pattern("CUC")
				.pattern("AAA")
				.define('G', Items.GOLDEN_CARROT).unlockedBy("has_garrot", has(Items.GOLDEN_CARROT))
				.define('C', Items.CARROT).unlockedBy("has_carrot", has(Items.CLAY_BALL))
				.define('A', RatsItemRegistry.ASSORTED_VEGETABLES.get()).unlockedBy("has_veggies", has(RatsItemRegistry.ASSORTED_VEGETABLES.get()))
				.define('U', RatsItemRegistry.RAT_UPGRADE_BASIC.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_BASIC.get()))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/carrat_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_ANGEL.get())
				.pattern("GHG")
				.pattern("WUW")
				.pattern("GTG")
				.define('G', Tags.Items.INGOTS_GOLD).unlockedBy("has_gold", has(Tags.Items.INGOTS_GOLD))
				.define('H', RatsItemRegistry.HALO_HAT.get()).unlockedBy("has_halo", has(RatsItemRegistry.HALO_HAT.get()))
				.define('T', Items.TOTEM_OF_UNDYING).unlockedBy("has_totem", has(Items.TOTEM_OF_UNDYING))
				.define('W', RatsItemRegistry.FEATHERY_WING.get()).unlockedBy("has_wing", has(RatsItemRegistry.FEATHERY_WING.get()))
				.define('U', RatsItemRegistry.RAT_UPGRADE_BASIC.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_BASIC.get()))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/angel_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_UNDEAD.get())
				.pattern("FBF")
				.pattern("SUS")
				.pattern("FBF")
				.define('F', Items.ROTTEN_FLESH).unlockedBy("has_flesh", has(Items.ROTTEN_FLESH))
				.define('B', Tags.Items.BONES).unlockedBy("has_bone", has(Tags.Items.BONES))
				.define('S', RatsItemRegistry.RAT_SKULL.get()).unlockedBy("has_skull", has(RatsItemRegistry.RAT_SKULL.get()))
				.define('U', RatsItemRegistry.RAT_UPGRADE_BASIC.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_BASIC.get()))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/undead_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_BASIC_MOUNT.get())
				.pattern("CCC")
				.pattern("CSC")
				.pattern("CCC")
				.define('C', RatsItemTags.NORMAL_CHEESE).unlockedBy("has_cheese", has(RatsItemTags.NORMAL_CHEESE))
				.define('S', Items.SADDLE).unlockedBy("has_saddle", has(Items.SADDLE))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/basic_mount_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_CHICKEN_MOUNT.get())
				.pattern("SSS")
				.pattern("WUW")
				.pattern("CEC")
				.define('S', Tags.Items.SEEDS).unlockedBy("has_seeds", has(Tags.Items.SEEDS))
				.define('C', Items.CHICKEN).unlockedBy("has_chicken", has(Items.CHICKEN))
				.define('E', Tags.Items.EGGS).unlockedBy("has_egg", has(Tags.Items.EGGS))
				.define('W', RatsItemRegistry.FEATHERY_WING.get()).unlockedBy("has_wing", has(RatsItemRegistry.FEATHERY_WING.get()))
				.define('U', RatsItemRegistry.RAT_UPGRADE_BASIC_MOUNT.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_BASIC.get()))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/chicken_mount_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_GOLEM_MOUNT.get())
				.pattern("PGP")
				.pattern("IUI")
				.pattern("IEI")
				.define('G', Tags.Items.GLASS).unlockedBy("has_glass", has(Tags.Items.GLASS))
				.define('P', Items.CARVED_PUMPKIN).unlockedBy("has_pumpkin", has(Items.CARVED_PUMPKIN))
				.define('I', Tags.Items.STORAGE_BLOCKS_IRON).unlockedBy("has_iron", has(Tags.Items.STORAGE_BLOCKS_IRON))
				.define('E', Tags.Items.STORAGE_BLOCKS_EMERALD).unlockedBy("has_emerald", has(Tags.Items.STORAGE_BLOCKS_EMERALD))
				.define('U', RatsItemRegistry.RAT_UPGRADE_BASIC_MOUNT.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_BASIC.get()))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/golem_mount_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_STRIDER_MOUNT.get())
				.pattern("WCW")
				.pattern("TUT")
				.pattern("WCW")
				.define('W', Items.WARPED_FUNGUS).unlockedBy("has_fungus", has(Items.WARPED_FUNGUS))
				.define('C', RatsItemTags.NETHER_CHEESE).unlockedBy("has_cheese", has(RatsItemTags.NETHER_CHEESE))
				.define('T', Items.GHAST_TEAR).unlockedBy("has_tear", has(Items.GHAST_TEAR))
				.define('U', RatsItemRegistry.RAT_UPGRADE_BASIC_MOUNT.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_BASIC.get()))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/strider_mount_upgrade"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_BEAST_MOUNT.get())
				.pattern("LSL")
				.pattern("FUF")
				.pattern("LEL")
				.define('L', RatsItemRegistry.PLAGUE_LEECH.get()).unlockedBy("has_leech", has(RatsItemRegistry.PLAGUE_LEECH.get()))
				.define('S', RatsItemRegistry.RAT_SKULL.get()).unlockedBy("has_skull", has(RatsItemRegistry.RAT_SKULL.get()))
				.define('F', RatsItemRegistry.FILTH.get()).unlockedBy("has_filth", has(RatsItemRegistry.FILTH.get()))
				.define('E', RatsItemRegistry.PLAGUE_ESSENCE.get()).unlockedBy("has_essence", has(RatsItemRegistry.PLAGUE_ESSENCE.get()))
				.define('U', RatsItemRegistry.RAT_UPGRADE_BASIC_MOUNT.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_BASIC.get()))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/beast_mount_upgrade"));
	}
}
