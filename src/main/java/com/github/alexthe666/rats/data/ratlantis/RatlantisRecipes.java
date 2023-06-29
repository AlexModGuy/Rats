package com.github.alexthe666.rats.data.ratlantis;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.data.ratlantis.tags.RatlantisBlockTags;
import com.github.alexthe666.rats.data.ratlantis.tags.RatlantisItemTags;
import com.github.alexthe666.rats.data.tags.RatsItemTags;
import com.github.alexthe666.rats.registry.*;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.function.Consumer;

public class RatlantisRecipes extends RecipeProvider {
	public RatlantisRecipes(PackOutput output) {
		super(output);
	}

	@Override
	protected void buildRecipes(Consumer<FinishedRecipe> consumer) {

		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RatsBlockRegistry.UPGRADE_COMBINER.get())
				.pattern(" I ")
				.pattern("PBP")
				.pattern("PCP")
				.define('I', RatlantisItemRegistry.IDOL_OF_RATLANTIS.get()).unlockedBy("has_idol", has(RatlantisItemRegistry.IDOL_OF_RATLANTIS.get()))
				.define('P', RatlantisBlockRegistry.MARBLED_CHEESE_PILLAR.get()).unlockedBy("has_pillar", has(RatlantisBlockRegistry.MARBLED_CHEESE_PILLAR.get()))
				.define('B', RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_CHISELED.get()).unlockedBy("has_brick", has(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_CHISELED.get()))
				.define('C', RatlantisBlockRegistry.MARBLED_CHEESE_CHISELED.get()).unlockedBy("has_chiseled", has(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_CHISELED.get()))
				.save(consumer);


		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RatsBlockRegistry.UPGRADE_SEPARATOR.get())
				.pattern(" S ")
				.pattern("PBP")
				.pattern("PCP")
				.define('S', RatlantisItemRegistry.ANCIENT_SAWBLADE.get()).unlockedBy("has_saw", has(RatlantisItemRegistry.ANCIENT_SAWBLADE.get()))
				.define('P', RatlantisBlockRegistry.MARBLED_CHEESE_PILLAR.get()).unlockedBy("has_pillar", has(RatlantisBlockRegistry.MARBLED_CHEESE_PILLAR.get()))
				.define('B', RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_CHISELED.get()).unlockedBy("has_brick", has(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_CHISELED.get()))
				.define('C', RatlantisBlockRegistry.MARBLED_CHEESE_CHISELED.get()).unlockedBy("has_chiseled", has(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_CHISELED.get()))
				.save(consumer);


		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RatlantisItemRegistry.RATLANTIS_RAT_SKULL.get())
				.requires(RatsItemRegistry.GOLDEN_RAT_SKULL.get()).unlockedBy("has_skull", has(RatsItemRegistry.GOLDEN_RAT_SKULL.get()))
				.requires(RatlantisItemRegistry.ARCANE_TECHNOLOGY.get()).unlockedBy("has_tech", has(RatlantisItemRegistry.ANCIENT_SAWBLADE.get()))
				.requires(RatlantisItemRegistry.PSIONIC_RAT_BRAIN.get()).unlockedBy("has_brain", has(RatlantisItemRegistry.PSIONIC_RAT_BRAIN.get()))
				.requires(RatlantisItemRegistry.DUTCHRAT_WHEEL.get()).unlockedBy("has_wheel", has(RatlantisItemRegistry.DUTCHRAT_WHEEL.get()))
				.requires(RatlantisItemRegistry.BIPLANE_WING.get()).unlockedBy("has_wing", has(RatlantisItemRegistry.BIPLANE_WING.get()))
				.save(consumer);


		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RatlantisItemRegistry.RATGLOVE_PETALS.get())
				.requires(RatlantisBlockRegistry.RATGLOVE_FLOWER.get(), 9).unlockedBy("has_flower", has(RatlantisBlockRegistry.RATGLOVE_FLOWER.get()))
				.save(consumer);


		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, RatlantisItemRegistry.FERAL_BAGH_NAKHS.get())
				.pattern(" FC")
				.pattern("SFC")
				.pattern(" FC")
				.define('C', RatlantisItemRegistry.FERAL_RAT_CLAW.get()).unlockedBy("has_claw", has(RatlantisItemRegistry.FERAL_RAT_CLAW.get()))
				.define('F', Items.ROTTEN_FLESH)
				.define('S', Tags.Items.STRING)
				.save(consumer);


		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatlantisItemRegistry.GEM_OF_RATLANTIS.get())
				.pattern(" E ")
				.pattern("EPE")
				.pattern(" E ")
				.define('P', RatlantisItemRegistry.RATGLOVE_PETALS.get()).unlockedBy("has_petals", has(RatlantisItemRegistry.RATGLOVE_PETALS.get()))
				.define('E', Tags.Items.GEMS_EMERALD).unlockedBy("has_emerald", has(Tags.Items.GEMS_EMERALD))
				.save(consumer);


		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatlantisItemRegistry.ORATCHALCUM_INGOT.get())
				.pattern("NNN")
				.pattern("NNN")
				.pattern("NNN")
				.define('N', RatlantisItemRegistry.ORATCHALCUM_NUGGET.get()).unlockedBy("has_nugget", has(RatlantisItemRegistry.ORATCHALCUM_NUGGET.get()))
				.save(consumer);


		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RatlantisItemRegistry.ORATCHALCUM_INGOT.get(), 9)
				.requires(RatlantisBlockRegistry.ORATCHALCUM_BLOCK.get()).unlockedBy("has_block", has(RatlantisBlockRegistry.ORATCHALCUM_BLOCK.get()))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "oratchalcum_ingots_from_block"));


		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RatlantisItemRegistry.ORATCHALCUM_NUGGET.get(), 9)
				.requires(RatlantisItemRegistry.ORATCHALCUM_INGOT.get()).unlockedBy("has_ingot", has(RatlantisItemRegistry.ORATCHALCUM_INGOT.get()))
				.save(consumer);


		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, RatlantisItemRegistry.RATLANTIS_HELMET.get())
				.pattern("IBI")
				.pattern("IAI")
				.pattern("III")
				.define('I', RatlantisItemRegistry.ORATCHALCUM_INGOT.get()).unlockedBy("has_ingot", has(RatlantisItemRegistry.ORATCHALCUM_INGOT.get()))
				.define('A', Items.NETHERITE_HELMET)
				.define('B', RatlantisItemRegistry.PSIONIC_RAT_BRAIN.get()).unlockedBy("has_brain", has(RatlantisItemRegistry.ORATCHALCUM_INGOT.get()))
				.save(consumer);


		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, RatlantisItemRegistry.RATLANTIS_CHESTPLATE.get())
				.pattern("ITI")
				.pattern("IAI")
				.pattern("III")
				.define('I', RatlantisItemRegistry.ORATCHALCUM_INGOT.get()).unlockedBy("has_ingot", has(RatlantisItemRegistry.ORATCHALCUM_INGOT.get()))
				.define('A', Items.NETHERITE_CHESTPLATE)
				.define('T', RatlantisItemRegistry.ARCANE_TECHNOLOGY.get()).unlockedBy("has_tech", has(RatlantisItemRegistry.ARCANE_TECHNOLOGY.get()))
				.save(consumer);


		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, RatlantisItemRegistry.RATLANTIS_LEGGINGS.get())
				.pattern("IWI")
				.pattern("IAI")
				.pattern("III")
				.define('I', RatlantisItemRegistry.ORATCHALCUM_INGOT.get()).unlockedBy("has_ingot", has(RatlantisItemRegistry.ORATCHALCUM_INGOT.get()))
				.define('A', Items.NETHERITE_LEGGINGS)
				.define('W', RatlantisItemRegistry.BIPLANE_WING.get()).unlockedBy("has_wing", has(RatlantisItemRegistry.BIPLANE_WING.get()))
				.save(consumer);


		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, RatlantisItemRegistry.RATLANTIS_BOOTS.get())
				.pattern("IWI")
				.pattern("IAI")
				.pattern("III")
				.define('I', RatlantisItemRegistry.ORATCHALCUM_INGOT.get()).unlockedBy("has_ingot", has(RatlantisItemRegistry.ORATCHALCUM_INGOT.get()))
				.define('A', Items.NETHERITE_BOOTS)
				.define('W', RatlantisItemRegistry.DUTCHRAT_WHEEL.get()).unlockedBy("has_wheel", has(RatlantisItemRegistry.DUTCHRAT_WHEEL.get()))
				.save(consumer);


		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, RatlantisItemRegistry.RATLANTIS_SWORD.get())
				.pattern(" I ")
				.pattern("ITI")
				.pattern(" I ")
				.define('I', RatlantisItemRegistry.ORATCHALCUM_INGOT.get()).unlockedBy("has_ingot", has(RatlantisItemRegistry.ORATCHALCUM_INGOT.get()))
				.define('T', Items.NETHERITE_SWORD)
				.save(consumer);


		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, RatlantisItemRegistry.RATLANTIS_PICKAXE.get())
				.pattern(" I ")
				.pattern("ITI")
				.pattern(" I ")
				.define('I', RatlantisItemRegistry.ORATCHALCUM_INGOT.get()).unlockedBy("has_ingot", has(RatlantisItemRegistry.ORATCHALCUM_INGOT.get()))
				.define('T', Items.NETHERITE_PICKAXE)
				.save(consumer);


		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, RatlantisItemRegistry.RATLANTIS_AXE.get())
				.pattern(" I ")
				.pattern("ITI")
				.pattern(" I ")
				.define('I', RatlantisItemRegistry.ORATCHALCUM_INGOT.get()).unlockedBy("has_ingot", has(RatlantisItemRegistry.ORATCHALCUM_INGOT.get()))
				.define('T', Items.NETHERITE_AXE)
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, RatlantisItemRegistry.RATLANTIS_SHOVEL.get())
				.pattern(" I ")
				.pattern("ITI")
				.pattern(" I ")
				.define('I', RatlantisItemRegistry.ORATCHALCUM_INGOT.get()).unlockedBy("has_ingot", has(RatlantisItemRegistry.ORATCHALCUM_INGOT.get()))
				.define('T', Items.NETHERITE_SHOVEL)
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, RatlantisItemRegistry.RATLANTIS_HOE.get())
				.pattern(" I ")
				.pattern("ITI")
				.pattern(" I ")
				.define('I', RatlantisItemRegistry.ORATCHALCUM_INGOT.get()).unlockedBy("has_ingot", has(RatlantisItemRegistry.ORATCHALCUM_INGOT.get()))
				.define('T', Items.NETHERITE_HOE)
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, RatlantisItemRegistry.RATLANTIS_BOW.get())
				.pattern(" IS")
				.pattern("ABS")
				.pattern(" IS")
				.define('A', RatlantisItemRegistry.ARCANE_TECHNOLOGY.get()).unlockedBy("has_tech", has(RatlantisItemRegistry.ARCANE_TECHNOLOGY.get()))
				.define('I', RatlantisItemRegistry.ORATCHALCUM_INGOT.get()).unlockedBy("has_ingot", has(RatlantisItemRegistry.ORATCHALCUM_INGOT.get()))
				.define('B', Items.BOW)
				.define('S', RatlantisItemRegistry.RATLANTEAN_FLAME.get()).unlockedBy("has_flame", has(RatlantisItemRegistry.RATLANTEAN_FLAME.get()))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatlantisItemRegistry.VIAL_OF_SENTIENCE.get())
				.pattern("SCS")
				.pattern("SPS")
				.pattern("SBS")
				.define('C', RatlantisItemRegistry.FERAL_RAT_CLAW.get()).unlockedBy("has_claw", has(RatlantisItemRegistry.FERAL_RAT_CLAW.get()))
				.define('P', RatlantisItemRegistry.RATGLOVE_PETALS.get()).unlockedBy("has_petals", has(RatlantisItemRegistry.RATGLOVE_PETALS.get()))
				.define('B', Items.GLASS_BOTTLE)
				.define('S', RatlantisItemRegistry.RATLANTEAN_FLAME.get()).unlockedBy("has_flame", has(RatlantisItemRegistry.RATLANTEAN_FLAME.get()))
				.save(consumer);


		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, RatlantisItemRegistry.GHOST_PIRAT_HAT.get())
				.pattern(" E ")
				.pattern("EHE")
				.pattern(" E ")
				.define('E', RatlantisItemRegistry.GHOST_PIRAT_ECTOPLASM.get()).unlockedBy("has_ectoplasm", has(RatlantisItemRegistry.GHOST_PIRAT_ECTOPLASM.get()))
				.define('H', RatsItemRegistry.PIRAT_HAT.get()).unlockedBy("has_hat", has(RatsItemRegistry.PIRAT_HAT.get()))
				.save(consumer);


		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RatlantisItemRegistry.GHOST_PIRAT_ECTOPLASM.get(), 4)
				.requires(RatlantisItemRegistry.GHOST_PIRAT_HAT.get()).unlockedBy("has_hat", has(RatlantisItemRegistry.GHOST_PIRAT_HAT.get()))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "ectoplasm_from_hat"));


		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RatlantisItemRegistry.GHOST_PIRAT_ECTOPLASM.get(), 4)
				.requires(RatlantisItemRegistry.GHOST_PIRAT_CUTLASS.get()).unlockedBy("has_cutlass", has(RatlantisItemRegistry.GHOST_PIRAT_CUTLASS.get()))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "ectoplasm_from_cutlass"));


		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RatlantisItemRegistry.GHOST_PIRAT_ECTOPLASM.get(), 10)
				.requires(RatlantisItemRegistry.DUTCHRAT_WHEEL.get()).unlockedBy("has_wheel", has(RatlantisItemRegistry.DUTCHRAT_WHEEL.get()))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "ectoplasm_from_wheel"));


		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, RatlantisItemRegistry.GHOST_PIRAT_CUTLASS.get())
				.pattern(" E ")
				.pattern("ECE")
				.pattern(" E ")
				.define('E', RatlantisItemRegistry.GHOST_PIRAT_ECTOPLASM.get()).unlockedBy("has_ectoplasm", has(RatlantisItemRegistry.GHOST_PIRAT_ECTOPLASM.get()))
				.define('C', RatlantisItemRegistry.PIRAT_CUTLASS.get()).unlockedBy("has_cutlass", has(RatlantisItemRegistry.PIRAT_CUTLASS.get()))
				.save(consumer);


		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatlantisItemRegistry.CHARGED_RATBOT_BARREL.get())
				.pattern("RRR")
				.pattern("RBR")
				.pattern("RRR")
				.define('R', Tags.Items.STORAGE_BLOCKS_REDSTONE)
				.define('B', RatlantisItemRegistry.RATBOT_BARREL.get()).unlockedBy("has_barrel", has(RatlantisItemRegistry.RATBOT_BARREL.get()))
				.save(consumer);


		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatlantisItemRegistry.RATTLING_GUN.get())
				.pattern("I  ")
				.pattern("IBC")
				.pattern("SSS")
				.define('I', Tags.Items.INGOTS_IRON).unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
				.define('B', Tags.Items.STORAGE_BLOCKS_IRON).unlockedBy("has_block", has(Tags.Items.STORAGE_BLOCKS_IRON))
				.define('C', RatlantisItemRegistry.CHARGED_RATBOT_BARREL.get()).unlockedBy("has_barrel", has(RatlantisItemRegistry.CHARGED_RATBOT_BARREL.get()))
				.define('S', Tags.Items.RODS_WOODEN)
				.save(consumer);


		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatlantisItemRegistry.IDOL_OF_RATLANTIS.get())
				.pattern("CHF")
				.pattern("TSR")
				.pattern("OGP")
				.define('C', RatlantisItemRegistry.FERAL_RAT_CLAW.get()).unlockedBy("has_claw", has(RatlantisItemRegistry.FERAL_RAT_CLAW.get()))
				.define('H', RatsItemRegistry.PIRAT_HAT.get()).unlockedBy("has_hat", has(RatsItemRegistry.PIRAT_HAT.get()))
				.define('F', RatlantisItemRegistry.RATLANTEAN_FLAME.get()).unlockedBy("has_flame", has(RatlantisItemRegistry.RATLANTEAN_FLAME.get()))
				.define('T', RatlantisItemRegistry.RAT_TOGA.get()).unlockedBy("has_toga", has(RatlantisItemRegistry.RAT_TOGA.get()))
				.define('S', RatlantisItemRegistry.RATLANTIS_RAT_SKULL.get()).unlockedBy("has_skull", has(RatlantisItemRegistry.RAT_TOGA.get()))
				.define('R', RatlantisItemRegistry.RATFISH.get()).unlockedBy("has_fish", has(RatlantisItemRegistry.RATFISH.get()))
				.define('O', RatlantisBlockRegistry.ORATCHALCUM_BLOCK.get()).unlockedBy("has_block", has(RatlantisBlockRegistry.ORATCHALCUM_BLOCK.get()))
				.define('G', RatlantisItemTags.RATLANTIS_GEMS).unlockedBy("has_gem", has(RatlantisItemTags.RATLANTIS_GEMS))
				.define('P', RatlantisItemRegistry.RATGLOVE_PETALS.get()).unlockedBy("has_petals", has(RatlantisItemRegistry.RATGLOVE_PETALS.get()))
				.save(consumer);


		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RatlantisItemRegistry.RAS_BANNER_PATTERN.get())
				.requires(RatlantisItemRegistry.MILITARY_HAT.get()).unlockedBy("has_hat", has(RatlantisItemRegistry.MILITARY_HAT.get()))
				.requires(Items.PAPER)
				.save(consumer);


		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RatlantisBlockRegistry.CHUNKY_CHEESE_TOKEN.get())
				.requires(Ingredient.of(RatsItemRegistry.TOKEN_PIECE.get()), 9).unlockedBy("has_piece", has(RatsItemRegistry.TOKEN_PIECE.get()))
				.save(consumer);


		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RatlantisBlockRegistry.ORATCHALCUM_BLOCK.get())
				.pattern("III")
				.pattern("III")
				.pattern("III")
				.define('I', RatlantisItemRegistry.ORATCHALCUM_INGOT.get()).unlockedBy("has_ingot", has(RatlantisItemRegistry.ORATCHALCUM_INGOT.get()))
				.save(consumer);


		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RatlantisBlockRegistry.BLACK_MARBLED_CHEESE.get(), 8)
				.pattern("MMM")
				.pattern("MDM")
				.pattern("MMM")
				.define('D', Tags.Items.DYES_BLACK).unlockedBy("has_dye", has(Tags.Items.DYES_BLACK))
				.define('M', RatlantisBlockRegistry.MARBLED_CHEESE.get()).unlockedBy("has_cheese", has(RatlantisBlockRegistry.MARBLED_CHEESE.get()))
				.save(consumer);


		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RatlantisBlockRegistry.MARBLED_CHEESE_SLAB.get(), 6)
				.pattern("MMM")
				.define('M', RatlantisBlockRegistry.MARBLED_CHEESE.get()).unlockedBy("has_cheese", has(RatlantisBlockRegistry.MARBLED_CHEESE.get()))
				.save(consumer);


		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RatlantisBlockRegistry.MARBLED_CHEESE_STAIRS.get(), 4)
				.pattern("M  ")
				.pattern("MM ")
				.pattern("MMM")
				.define('M', RatlantisBlockRegistry.MARBLED_CHEESE.get()).unlockedBy("has_cheese", has(RatlantisBlockRegistry.MARBLED_CHEESE.get()))
				.save(consumer);


		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RatlantisBlockRegistry.MARBLED_CHEESE_TILE.get(), 8)
				.pattern("MMM")
				.pattern("M M")
				.pattern("MMM")
				.define('M', RatlantisBlockRegistry.MARBLED_CHEESE.get()).unlockedBy("has_cheese", has(RatlantisBlockRegistry.MARBLED_CHEESE.get()))
				.save(consumer);


		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RatlantisBlockRegistry.MARBLED_CHEESE_CHISELED.get())
				.pattern("M")
				.pattern("M")
				.define('M', RatlantisBlockRegistry.MARBLED_CHEESE_SLAB.get()).unlockedBy("has_cheese", has(RatlantisBlockRegistry.MARBLED_CHEESE_SLAB.get()))
				.save(consumer);


		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RatlantisBlockRegistry.MARBLED_CHEESE_PILLAR.get(), 2)
				.pattern("M")
				.pattern("M")
				.define('M', RatlantisBlockRegistry.MARBLED_CHEESE.get()).unlockedBy("has_cheese", has(RatlantisBlockRegistry.MARBLED_CHEESE.get()))
				.save(consumer);


		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RatlantisBlockRegistry.MARBLED_CHEESE_BRICK.get(), 4)
				.pattern("MM")
				.pattern("MM")
				.define('M', RatlantisBlockRegistry.MARBLED_CHEESE.get()).unlockedBy("has_cheese", has(RatlantisBlockRegistry.MARBLED_CHEESE.get()))
				.save(consumer);


		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_SLAB.get(), 6)
				.pattern("MMM")
				.define('M', RatlantisBlockRegistry.MARBLED_CHEESE_BRICK.get()).unlockedBy("has_cheese", has(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK.get()))
				.save(consumer);


		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_STAIRS.get(), 4)
				.pattern("M  ")
				.pattern("MM ")
				.pattern("MMM")
				.define('M', RatlantisBlockRegistry.MARBLED_CHEESE_BRICK.get()).unlockedBy("has_cheese", has(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK.get()))
				.save(consumer);


		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_CHISELED.get())
				.pattern("M")
				.pattern("M")
				.define('M', RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_SLAB.get()).unlockedBy("has_cheese", has(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_SLAB.get()))
				.save(consumer);


		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_MOSSY.get())
				.requires(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK.get()).unlockedBy("has_cheese", has(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK.get()))
				.requires(Items.VINE).unlockedBy("has_vine", has(Items.VINE))
				.save(consumer);


		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, RatlantisBlockRegistry.MARBLED_CHEESE_DIRT.get())
				.requires(RatlantisBlockRegistry.MARBLED_CHEESE.get()).unlockedBy("has_cheese", has(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK.get()))
				.requires(ItemTags.DIRT).unlockedBy("has_dirt", has(ItemTags.DIRT))
				.save(consumer);


		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, RatlantisBlockRegistry.MARBLED_CHEESE_GRASS.get())
				.requires(RatlantisBlockRegistry.MARBLED_CHEESE.get()).unlockedBy("has_cheese", has(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK.get()))
				.requires(Items.GRASS_BLOCK).unlockedBy("has_grass", has(Items.GRASS_BLOCK))
				.save(consumer);


		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RatlantisBlockRegistry.MARBLED_CHEESE_GOLEM_CORE.get())
				.pattern("MMM")
				.pattern("MGM")
				.pattern("MMM")
				.define('G', RatlantisItemTags.RATLANTIS_GEMS).unlockedBy("has_gem", has(RatlantisItemTags.RATLANTIS_GEMS))
				.define('M', RatsItemTags.MARBLED_CHEESE).unlockedBy("has_cheese", has(RatsItemTags.MARBLED_CHEESE))
				.save(consumer);


		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RatlantisBlockRegistry.COMPRESSED_RAT.get())
				.pattern("PPP")
				.pattern("PPP")
				.pattern("PPP")
				.define('P', RatlantisItemRegistry.RATGLOVE_PETALS.get()).unlockedBy("has_petals", has(RatlantisItemRegistry.RATGLOVE_PETALS.get()))
				.save(consumer);


		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RatlantisBlockRegistry.BRAIN_BLOCK.get())
				.pattern("BB")
				.pattern("BB")
				.define('B', RatlantisItemRegistry.PSIONIC_RAT_BRAIN.get()).unlockedBy("has_brain", has(RatlantisItemRegistry.PSIONIC_RAT_BRAIN.get()))
				.save(consumer);


		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RatlantisBlockRegistry.PIRAT_PLANKS.get(), 8)
				.pattern("PPP")
				.pattern("PEP")
				.pattern("PPP")
				.define('P', ItemTags.PLANKS)
				.define('E', RatlantisItemRegistry.GHOST_PIRAT_ECTOPLASM.get()).unlockedBy("has_ectoplasm", has(RatlantisItemRegistry.GHOST_PIRAT_ECTOPLASM.get()))
				.save(consumer);


		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, RatlantisBlockRegistry.PIRAT_PLANKS.get(), 4)
				.requires(RatlantisItemTags.PIRAT_LOGS).unlockedBy("has_log", has(RatlantisItemTags.PIRAT_LOGS))
				.group("planks")
				.save(consumer, new ResourceLocation(RatsMod.MODID, "pirat_planks_from_log"));


		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RatlantisBlockRegistry.PIRAT_WOOD.get(), 3)
				.pattern("LL")
				.pattern("LL")
				.define('L', RatlantisBlockRegistry.PIRAT_LOG.get()).unlockedBy("has_log", has(RatlantisBlockRegistry.PIRAT_LOG.get()))
				.group("bark")
				.save(consumer);


		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RatlantisBlockRegistry.STRIPPED_PIRAT_WOOD.get(), 3)
				.pattern("LL")
				.pattern("LL")
				.define('L', RatlantisBlockRegistry.STRIPPED_PIRAT_LOG.get()).unlockedBy("has_log", has(RatlantisBlockRegistry.STRIPPED_PIRAT_LOG.get()))
				.group("bark")
				.save(consumer);


		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, RatlantisBlockRegistry.PIRAT_PRESSURE_PLATE.get())
				.pattern("PP")
				.define('P', RatlantisBlockRegistry.PIRAT_PLANKS.get()).unlockedBy("has_planks", has(RatlantisBlockRegistry.PIRAT_PLANKS.get()))
				.save(consumer);


		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, RatlantisBlockRegistry.PIRAT_TRAPDOOR.get(), 2)
				.pattern("PPP")
				.pattern("PPP")
				.define('P', RatlantisBlockRegistry.PIRAT_PLANKS.get()).unlockedBy("has_planks", has(RatlantisBlockRegistry.PIRAT_PLANKS.get()))
				.save(consumer);


		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RatlantisBlockRegistry.PIRAT_STAIRS.get(), 4)
				.pattern("P  ")
				.pattern("PP ")
				.pattern("PPP")
				.define('P', RatlantisBlockRegistry.PIRAT_PLANKS.get()).unlockedBy("has_planks", has(RatlantisBlockRegistry.PIRAT_PLANKS.get()))
				.save(consumer);


		ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, RatlantisBlockRegistry.PIRAT_BUTTON.get())
				.requires(RatlantisBlockRegistry.PIRAT_PLANKS.get()).unlockedBy("has_planks", has(RatlantisBlockRegistry.PIRAT_PLANKS.get()))
				.save(consumer);


		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RatlantisBlockRegistry.PIRAT_SLAB.get(), 6)
				.pattern("PPP")
				.define('P', RatlantisBlockRegistry.PIRAT_PLANKS.get()).unlockedBy("has_planks", has(RatlantisBlockRegistry.PIRAT_PLANKS.get()))
				.save(consumer);


		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, RatlantisBlockRegistry.PIRAT_FENCE_GATE.get())
				.pattern("SPS")
				.pattern("SPS")
				.define('S', Tags.Items.RODS_WOODEN)
				.define('P', RatlantisBlockRegistry.PIRAT_PLANKS.get()).unlockedBy("has_planks", has(RatlantisBlockRegistry.PIRAT_PLANKS.get()))
				.save(consumer);


		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, RatlantisBlockRegistry.PIRAT_FENCE.get(), 3)
				.pattern("PSP")
				.pattern("PSP")
				.define('S', Tags.Items.RODS_WOODEN)
				.define('P', RatlantisBlockRegistry.PIRAT_PLANKS.get()).unlockedBy("has_planks", has(RatlantisBlockRegistry.PIRAT_PLANKS.get()))
				.save(consumer);


		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, RatlantisBlockRegistry.PIRAT_DOOR.get(), 3)
				.pattern("PP")
				.pattern("PP")
				.pattern("PP")
				.define('P', RatlantisBlockRegistry.PIRAT_PLANKS.get()).unlockedBy("has_planks", has(RatlantisBlockRegistry.PIRAT_PLANKS.get()))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, RatlantisItemRegistry.PIRAT_SIGN.get(), 3)
				.pattern("PPP")
				.pattern("PPP")
				.pattern(" S ")
				.define('P', RatlantisBlockRegistry.PIRAT_PLANKS.get()).unlockedBy("has_planks", has(RatlantisBlockRegistry.PIRAT_PLANKS.get()))
				.define('S', Tags.Items.RODS_WOODEN)
				.group("sign")
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, RatlantisItemRegistry.PIRAT_HANGING_SIGN.get(), 6)
				.pattern("C C")
				.pattern("PPP")
				.pattern("PPP")
				.define('P', RatlantisBlockRegistry.STRIPPED_PIRAT_WOOD.get()).unlockedBy("has_wood", has(RatlantisBlockRegistry.STRIPPED_PIRAT_WOOD.get()))
				.define('C', Items.CHAIN)
				.group("hanging_sign")
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, RatlantisItemRegistry.PIRAT_BOAT.get())
				.pattern("P P")
				.pattern("PPP")
				.group("boat")
				.define('P', RatlantisBlockRegistry.PIRAT_PLANKS.get()).unlockedBy("in_water", insideOf(Blocks.WATER))
				.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.TRANSPORTATION, RatlantisItemRegistry.PIRAT_CHEST_BOAT.get())
				.requires(RatlantisItemRegistry.PIRAT_BOAT.get())
				.requires(Tags.Items.CHESTS_WOODEN)
				.group("chest_boat")
				.unlockedBy("has_boat", has(ItemTags.BOATS))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, RatlantisBlockRegistry.DUTCHRAT_BELL.get())
				.pattern("LLL")
				.pattern("FEF")
				.pattern("FGF")
				.define('E', RatlantisItemRegistry.GHOST_PIRAT_ECTOPLASM.get()).unlockedBy("has_ectoplasm", has(RatlantisItemRegistry.GHOST_PIRAT_ECTOPLASM.get()))
				.define('F', RatlantisBlockRegistry.PIRAT_FENCE.get()).unlockedBy("has_fence", has(RatlantisBlockRegistry.PIRAT_PLANKS.get()))
				.define('G', RatlantisItemTags.RATLANTIS_GEMS).unlockedBy("has_gem", has(RatlantisItemTags.RATLANTIS_GEMS))
				.define('L', RatlantisItemTags.PIRAT_LOGS).unlockedBy("has_log", has(RatlantisItemTags.PIRAT_LOGS))
				.save(consumer);


		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, RatlantisBlockRegistry.AIR_RAID_SIREN.get())
				.pattern(" S ")
				.pattern("BIB")
				.pattern(" S ")
				.define('S', Tags.Items.RODS_WOODEN).unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN))
				.define('I', RatlantisItemRegistry.ORATCHALCUM_INGOT.get()).unlockedBy("has_ingot", has(RatlantisItemRegistry.ORATCHALCUM_INGOT.get()))
				.define('B', RatlantisItemRegistry.RATBOT_BARREL.get()).unlockedBy("has_barrel", has(RatlantisItemRegistry.RATBOT_BARREL.get()))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RatlantisBlockRegistry.RATLANTIS_UPGRADE_BLOCK.get())
				.pattern("UU")
				.pattern("UU")
				.define('U', RatlantisItemRegistry.RAT_UPGRADE_BASIC_RATLANTEAN.get()).unlockedBy("has_upgrade", has(RatlantisItemRegistry.RAT_UPGRADE_BASIC_RATLANTEAN.get()))
				.save(consumer);


		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RatlantisItemRegistry.RAT_UPGRADE_BASIC_RATLANTEAN.get(), 4)
				.requires(RatlantisBlockRegistry.RATLANTIS_UPGRADE_BLOCK.get()).unlockedBy("has_block", has(RatlantisBlockRegistry.RATLANTIS_UPGRADE_BLOCK.get()))
				.save(consumer, new ResourceLocation(RatsMod.MODID, "upgrades/basic_upgrade_from_block"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatsItemRegistry.RAT_UPGRADE_COMBINED.get())
				.pattern("CGC")
				.pattern("GUG")
				.pattern("CGC")
				.define('C', RatlantisItemRegistry.FERAL_RAT_CLAW.get()).unlockedBy("has_claw", has(RatlantisItemRegistry.FERAL_RAT_CLAW.get()))
				.define('G', RatlantisItemTags.RATLANTIS_GEMS).unlockedBy("has_gem", has(RatlantisItemTags.RATLANTIS_GEMS))
				.define('U', RatlantisItemRegistry.RAT_UPGRADE_BASIC_RATLANTEAN.get()).unlockedBy("has_upgrade", has(RatlantisItemRegistry.RAT_UPGRADE_BASIC_RATLANTEAN.get()))
				.save(consumer);


		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatlantisItemRegistry.RAT_UPGRADE_ARCHEOLOGIST.get())
				.pattern("MHM")
				.pattern("BUB")
				.pattern("PTS")
				.define('M', RatsItemTags.MARBLED_CHEESE).unlockedBy("has_cheese", has(RatsItemTags.MARBLED_CHEESE))
				.define('H', RatsItemRegistry.ARCHEOLOGIST_HAT.get()).unlockedBy("has_hat", has(RatsItemRegistry.ARCHEOLOGIST_HAT.get()))
				.define('B', Tags.Items.BONES).unlockedBy("has_bone", has(Tags.Items.BONES))
				.define('P', Items.IRON_PICKAXE).unlockedBy("has_pick", has(Items.IRON_PICKAXE))
				.define('T', RatlantisBlockRegistry.CHUNKY_CHEESE_TOKEN.get()).unlockedBy("has_token", has(RatlantisBlockRegistry.CHUNKY_CHEESE_TOKEN.get()))
				.define('S', Items.IRON_SHOVEL).unlockedBy("has_shovel", has(Items.IRON_SHOVEL))
				.define('U', RatsItemRegistry.RAT_UPGRADE_BASIC.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_BASIC.get()))
				.save(consumer);


		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatlantisItemRegistry.RAT_UPGRADE_AUTOMATON_MOUNT.get())
				.pattern("HSH")
				.pattern("CUC")
				.pattern("ACA")
				.define('H', RatlantisBlockRegistry.MARBLED_CHEESE_RAT_HEAD.get()).unlockedBy("has_head", has(RatlantisBlockRegistry.MARBLED_CHEESE_RAT_HEAD.get()))
				.define('S', RatlantisItemRegistry.ANCIENT_SAWBLADE.get()).unlockedBy("has_saw", has(RatlantisItemRegistry.ANCIENT_SAWBLADE.get()))
				.define('C', RatlantisBlockRegistry.MARBLED_CHEESE_GOLEM_CORE.get()).unlockedBy("has_core", has(RatlantisBlockRegistry.MARBLED_CHEESE_GOLEM_CORE.get()))
				.define('A', RatlantisItemRegistry.ARCANE_TECHNOLOGY.get()).unlockedBy("has_technology", has(RatlantisItemRegistry.ARCANE_TECHNOLOGY.get()))
				.define('U', RatsItemRegistry.RAT_UPGRADE_BASIC_MOUNT.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_BASIC_MOUNT.get()))
				.save(consumer);


		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatlantisItemRegistry.RAT_UPGRADE_BIPLANE_MOUNT.get())
				.pattern("IAI")
				.pattern("BUB")
				.pattern("IWI")
				.define('I', Tags.Items.STORAGE_BLOCKS_IRON).unlockedBy("has_iron", has(Tags.Items.STORAGE_BLOCKS_IRON))
				.define('A', RatlantisItemRegistry.AVIATOR_HAT.get()).unlockedBy("has_hat", has(RatlantisItemRegistry.AVIATOR_HAT.get()))
				.define('B', RatlantisItemRegistry.RATBOT_BARREL.get()).unlockedBy("has_barrel", has(RatlantisItemRegistry.RATBOT_BARREL.get()))
				.define('W', RatlantisItemRegistry.BIPLANE_WING.get()).unlockedBy("has_wing", has(RatlantisItemRegistry.BIPLANE_WING.get()))
				.define('U', RatsItemRegistry.RAT_UPGRADE_BASIC_MOUNT.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_BASIC_MOUNT.get()))
				.save(consumer);


		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatlantisItemRegistry.RAT_UPGRADE_BASIC_RATLANTEAN.get())
				.pattern("GMG")
				.pattern("M M")
				.pattern("GMG")
				.define('G', RatlantisItemTags.RATLANTIS_GEMS).unlockedBy("has_gem", has(RatlantisItemTags.RATLANTIS_GEMS))
				.define('M', RatsItemTags.MARBLED_CHEESE).unlockedBy("has_cheese", has(RatsItemTags.MARBLED_CHEESE))
				.save(consumer);


		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatlantisItemRegistry.RAT_UPGRADE_FERAL_BITE.get())
				.pattern("CCC")
				.pattern("RUS")
				.pattern("CCC")
				.define('C', RatlantisItemRegistry.FERAL_RAT_CLAW.get()).unlockedBy("has_claw", has(RatlantisItemRegistry.FERAL_RAT_CLAW.get()))
				.define('R', Items.ROTTEN_FLESH).unlockedBy("has_flesh", has(Items.ROTTEN_FLESH))
				.define('S', Items.SPIDER_EYE).unlockedBy("has_eye", has(Items.SPIDER_EYE))
				.define('U', RatlantisItemRegistry.RAT_UPGRADE_BASIC_RATLANTEAN.get()).unlockedBy("has_upgrade", has(RatlantisItemRegistry.RAT_UPGRADE_BASIC_RATLANTEAN.get()))
				.save(consumer);


		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatlantisItemRegistry.RAT_UPGRADE_BUCCANEER.get())
				.pattern("CHC")
				.pattern("CUC")
				.pattern("CSC")
				.define('C', RatlantisItemRegistry.CHEESE_CANNONBALL.get()).unlockedBy("has_cannonball", has(RatlantisItemRegistry.CHEESE_CANNONBALL.get()))
				.define('H', RatsItemRegistry.PIRAT_HAT.get()).unlockedBy("has_hat", has(RatsItemRegistry.PIRAT_HAT.get()))
				.define('S', RatlantisItemRegistry.PIRAT_CUTLASS.get()).unlockedBy("has_eye", has(RatlantisItemRegistry.PIRAT_CUTLASS.get()))
				.define('U', RatlantisItemRegistry.RAT_UPGRADE_BASIC_RATLANTEAN.get()).unlockedBy("has_upgrade", has(RatlantisItemRegistry.RAT_UPGRADE_BASIC_RATLANTEAN.get()))
				.save(consumer);


		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatlantisItemRegistry.RAT_UPGRADE_RATINATOR.get())
				.pattern("IHI")
				.pattern("BUB")
				.pattern("ICI")
				.define('I', Tags.Items.STORAGE_BLOCKS_IRON).unlockedBy("has_iron", has(Tags.Items.STORAGE_BLOCKS_IRON))
				.define('H', RatlantisBlockRegistry.MARBLED_CHEESE_RAT_HEAD.get()).unlockedBy("has_head", has(RatlantisBlockRegistry.MARBLED_CHEESE_RAT_HEAD.get()))
				.define('B', RatlantisItemRegistry.RATBOT_BARREL.get()).unlockedBy("has_barrel", has(RatlantisItemRegistry.RATBOT_BARREL.get()))
				.define('C', RatlantisItemRegistry.CHARGED_RATBOT_BARREL.get()).unlockedBy("has_charged_barrel", has(RatlantisItemRegistry.CHARGED_RATBOT_BARREL.get()))
				.define('U', RatlantisItemRegistry.RAT_UPGRADE_BASIC_RATLANTEAN.get()).unlockedBy("has_upgrade", has(RatlantisItemRegistry.RAT_UPGRADE_BASIC_RATLANTEAN.get()))
				.save(consumer);


		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatlantisItemRegistry.RAT_UPGRADE_PSYCHIC.get())
				.pattern("SES")
				.pattern("CBC")
				.pattern("PUP")
				.define('S', Tags.Items.DUSTS_PRISMARINE).unlockedBy("has_shard", has(Tags.Items.DUSTS_PRISMARINE))
				.define('E', Items.ENDER_PEARL).unlockedBy("has_pearl", has(Items.ENDER_PEARL))
				.define('C', Items.END_CRYSTAL).unlockedBy("has_crystal", has(Items.END_CRYSTAL))
				.define('B', RatlantisItemRegistry.PSIONIC_RAT_BRAIN.get()).unlockedBy("has_brain", has(RatlantisItemRegistry.PSIONIC_RAT_BRAIN.get()))
				.define('P', Tags.Items.GEMS_PRISMARINE).unlockedBy("has_crystals", has(Tags.Items.GEMS_PRISMARINE))
				.define('U', RatlantisItemRegistry.RAT_UPGRADE_BASIC_RATLANTEAN.get()).unlockedBy("has_upgrade", has(RatlantisItemRegistry.RAT_UPGRADE_BASIC_RATLANTEAN.get()))
				.save(consumer);


		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatlantisItemRegistry.RAT_UPGRADE_ETHEREAL.get())
				.pattern("HEC")
				.pattern("EUE")
				.pattern("MEM")
				.define('H', RatlantisItemRegistry.GHOST_PIRAT_HAT.get()).unlockedBy("has_hat", has(RatlantisItemRegistry.GHOST_PIRAT_HAT.get()))
				.define('E', RatlantisItemRegistry.GHOST_PIRAT_ECTOPLASM.get()).unlockedBy("has_ectoplasm", has(RatlantisItemRegistry.GHOST_PIRAT_ECTOPLASM.get()))
				.define('C', RatlantisItemRegistry.GHOST_PIRAT_CUTLASS.get()).unlockedBy("has_cutlass", has(RatlantisItemRegistry.GHOST_PIRAT_CUTLASS.get()))
				.define('M', Items.PHANTOM_MEMBRANE).unlockedBy("has_membrane", has(Items.PHANTOM_MEMBRANE))
				.define('U', RatlantisItemRegistry.RAT_UPGRADE_BASIC_RATLANTEAN.get()).unlockedBy("has_upgrade", has(RatlantisItemRegistry.RAT_UPGRADE_BASIC_RATLANTEAN.get()))
				.save(consumer);


		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RatlantisItemRegistry.RAT_UPGRADE_NONBELIEVER.get())
				.pattern("AGB")
				.pattern("GIG")
				.pattern("WGC")
				.define('A', RatlantisItemRegistry.ARCANE_TECHNOLOGY.get()).unlockedBy("has_tech", has(RatlantisItemRegistry.ARCANE_TECHNOLOGY.get()))
				.define('W', RatlantisItemRegistry.DUTCHRAT_WHEEL.get()).unlockedBy("has_ectoplasm", has(RatlantisItemRegistry.DUTCHRAT_WHEEL.get()))
				.define('B', RatlantisItemRegistry.PSIONIC_RAT_BRAIN.get()).unlockedBy("has_cutlass", has(RatlantisItemRegistry.PSIONIC_RAT_BRAIN.get()))
				.define('I', RatlantisItemRegistry.IDOL_OF_RATLANTIS.get()).unlockedBy("has_idol", has(RatlantisItemRegistry.IDOL_OF_RATLANTIS.get()))
				.define('C', RatsItemRegistry.CORRUPT_RAT_SKULL.get()).unlockedBy("has_skull", has(RatsItemRegistry.CORRUPT_RAT_SKULL.get()))
				.define('G', RatsItemRegistry.RAT_UPGRADE_GOD.get()).unlockedBy("has_upgrade", has(RatsItemRegistry.RAT_UPGRADE_GOD.get()))
				.save(consumer);

		SimpleCookingRecipeBuilder.smelting(Ingredient.of(RatsBlockRegistry.MARBLED_CHEESE_RAW.get()), RecipeCategory.BUILDING_BLOCKS, RatlantisBlockRegistry.MARBLED_CHEESE.get(), 0.1F, 200).unlockedBy("has_cheese", has(RatsBlockRegistry.MARBLED_CHEESE_RAW.get())).save(consumer, new ResourceLocation(RatsMod.MODID, "marble_cheese_smelting"));
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(RatlantisBlockRegistry.MARBLED_CHEESE.get()), RecipeCategory.BUILDING_BLOCKS, RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_CRACKED.get(), 0.1F, 200).unlockedBy("has_cheese", has(RatlantisBlockRegistry.MARBLED_CHEESE.get())).save(consumer, new ResourceLocation(RatsMod.MODID, "cracked_marble_cheese_smelting"));

		SimpleCookingRecipeBuilder.smelting(Ingredient.of(RatlantisBlockRegistry.CHEESE_ORE.get()), RecipeCategory.MISC, RatsItemRegistry.CHEESE.get(), 0.25F, 200).unlockedBy("has_ore", has(RatlantisBlockRegistry.CHEESE_ORE.get())).save(consumer, new ResourceLocation(RatsMod.MODID, "cheese_ore_smelting"));
		SimpleCookingRecipeBuilder.blasting(Ingredient.of(RatlantisBlockRegistry.CHEESE_ORE.get()), RecipeCategory.MISC, RatsItemRegistry.CHEESE.get(), 0.25F, 100).unlockedBy("has_ore", has(RatlantisBlockRegistry.CHEESE_ORE.get())).save(consumer, new ResourceLocation(RatsMod.MODID, "cheese_ore_blasting"));

		SimpleCookingRecipeBuilder.smelting(Ingredient.of(RatlantisBlockRegistry.RATLANTEAN_GEM_ORE.get()), RecipeCategory.MISC, RatlantisItemRegistry.GEM_OF_RATLANTIS.get(), 0.5F, 200).unlockedBy("has_ore", has(RatlantisBlockRegistry.RATLANTEAN_GEM_ORE.get())).save(consumer, new ResourceLocation(RatsMod.MODID, "ratlantean_gem_smelting"));
		SimpleCookingRecipeBuilder.blasting(Ingredient.of(RatlantisBlockRegistry.RATLANTEAN_GEM_ORE.get()), RecipeCategory.MISC, RatlantisItemRegistry.GEM_OF_RATLANTIS.get(), 0.5F, 100).unlockedBy("has_ore", has(RatlantisBlockRegistry.RATLANTEAN_GEM_ORE.get())).save(consumer, new ResourceLocation(RatsMod.MODID, "ratlantean_gem_blasting"));

		SimpleCookingRecipeBuilder.smelting(Ingredient.of(RatlantisBlockRegistry.ORATCHALCUM_ORE.get()), RecipeCategory.MISC, RatlantisItemRegistry.ORATCHALCUM_INGOT.get(), 1.0F, 200).unlockedBy("has_ore", has(RatlantisBlockRegistry.ORATCHALCUM_ORE.get())).save(consumer, new ResourceLocation(RatsMod.MODID, "oratchalcum_ore_smelting"));
		SimpleCookingRecipeBuilder.blasting(Ingredient.of(RatlantisBlockRegistry.ORATCHALCUM_ORE.get()), RecipeCategory.MISC, RatlantisItemRegistry.ORATCHALCUM_INGOT.get(), 1.0F, 100).unlockedBy("has_ore", has(RatlantisBlockRegistry.ORATCHALCUM_ORE.get())).save(consumer, new ResourceLocation(RatsMod.MODID, "oratchalcum_ore_blasting"));

		SimpleCookingRecipeBuilder.smelting(Ingredient.of(RatlantisItemRegistry.RAW_ORATCHALCUM.get()), RecipeCategory.MISC, RatlantisItemRegistry.ORATCHALCUM_INGOT.get(), 1.0F, 200).unlockedBy("has_ore", has(RatlantisItemRegistry.RAW_ORATCHALCUM.get())).save(consumer, new ResourceLocation(RatsMod.MODID, "raw_oratchalcum_smelting"));
		SimpleCookingRecipeBuilder.blasting(Ingredient.of(RatlantisItemRegistry.RAW_ORATCHALCUM.get()), RecipeCategory.MISC, RatlantisItemRegistry.ORATCHALCUM_INGOT.get(), 1.0F, 100).unlockedBy("has_ore", has(RatlantisItemRegistry.RAW_ORATCHALCUM.get())).save(consumer, new ResourceLocation(RatsMod.MODID, "raw_oratchalcum_blasting"));

		this.ratlantisStonecuttingRecipe(consumer, Ingredient.of(RatlantisBlockRegistry.MARBLED_CHEESE.get()), RatlantisBlockRegistry.BLACK_MARBLED_CHEESE.get(), "marbled_cheese_to_black");
		this.ratlantisStonecuttingRecipe(consumer, Ingredient.of(RatlantisBlockRegistry.MARBLED_CHEESE.get()), RatlantisBlockRegistry.MARBLED_CHEESE_SLAB.get(), 2, "marbled_cheese_to_slab");
		this.ratlantisStonecuttingRecipe(consumer, Ingredient.of(RatlantisBlockRegistry.MARBLED_CHEESE.get()), RatlantisBlockRegistry.MARBLED_CHEESE_STAIRS.get(), "marbled_cheese_to_stairs");
		this.ratlantisStonecuttingRecipe(consumer, Ingredient.of(RatlantisBlockRegistry.MARBLED_CHEESE.get()), RatlantisBlockRegistry.MARBLED_CHEESE_CHISELED.get(), "marbled_cheese_to_chiseled");
		this.ratlantisStonecuttingRecipe(consumer, Ingredient.of(RatlantisBlockRegistry.MARBLED_CHEESE.get()), RatlantisBlockRegistry.MARBLED_CHEESE_PILLAR.get(), "marbled_cheese_to_pillar");
		this.ratlantisStonecuttingRecipe(consumer, Ingredient.of(RatlantisBlockRegistry.MARBLED_CHEESE.get()), RatlantisBlockRegistry.MARBLED_CHEESE_TILE.get(), "marbled_cheese_to_tile");
		this.ratlantisStonecuttingRecipe(consumer, Ingredient.of(RatlantisBlockRegistry.MARBLED_CHEESE.get()), RatlantisBlockRegistry.MARBLED_CHEESE_BRICK.get(), "marbled_cheese_to_brick");
		this.ratlantisStonecuttingRecipe(consumer, Ingredient.of(RatlantisBlockRegistry.MARBLED_CHEESE.get()), RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_SLAB.get(), 2, "marbled_cheese_to_brick_slab");
		this.ratlantisStonecuttingRecipe(consumer, Ingredient.of(RatlantisBlockRegistry.MARBLED_CHEESE.get()), RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_STAIRS.get(), "marbled_cheese_to_brick_stairs");
		this.ratlantisStonecuttingRecipe(consumer, Ingredient.of(RatlantisBlockRegistry.MARBLED_CHEESE.get()), RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_CHISELED.get(), "marbled_cheese_to_brick_chiseled");

		this.ratlantisStonecuttingRecipe(consumer, Ingredient.of(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK.get()), RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_SLAB.get(), 2, "marbled_cheese_brick_to_slab");
		this.ratlantisStonecuttingRecipe(consumer, Ingredient.of(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK.get()), RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_STAIRS.get(), "marbled_cheese_brick_to_stairs");
		this.ratlantisStonecuttingRecipe(consumer, Ingredient.of(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK.get()), RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_CHISELED.get(), "marbled_cheese_brick_to_chiseled");

		this.archeology(Items.EMERALD, RatlantisItemRegistry.GEM_OF_RATLANTIS.get(), consumer);
		this.archeology(RatsItemRegistry.RAT_UPGRADE_BASIC.get(), RatlantisItemRegistry.RAT_UPGRADE_BASIC_RATLANTEAN.get(), consumer);
		this.archeology(RatsItemRegistry.PIPER_HAT.get(), RatsItemRegistry.PIRAT_HAT.get(), consumer);
		this.archeology(Items.BLAZE_POWDER, RatlantisItemRegistry.RATLANTEAN_FLAME.get(), consumer);
		this.archeology(Items.LEATHER_CHESTPLATE, RatlantisItemRegistry.RAT_TOGA.get(), consumer);
		this.archeology(Items.RABBIT_FOOT, RatlantisItemRegistry.FERAL_RAT_CLAW.get(), consumer);
		this.archeology(Items.FIRE_CHARGE, RatlantisItemRegistry.CHEESE_CANNONBALL.get(), consumer);
		this.archeology(Items.IRON_SWORD, RatlantisItemRegistry.PIRAT_CUTLASS.get(), consumer);
		this.archeology(Items.PHANTOM_MEMBRANE, RatlantisItemRegistry.GHOST_PIRAT_ECTOPLASM.get(), consumer);
		this.archeology(Items.TROPICAL_FISH, RatlantisItemRegistry.RATFISH.get(), consumer);
		this.archeology(Items.DISPENSER, RatlantisItemRegistry.RATBOT_BARREL.get(), consumer);
		this.archeology(Items.NETHERITE_INGOT, RatlantisItemRegistry.ORATCHALCUM_INGOT.get(), consumer);
		this.archeology(Items.SKELETON_SKULL, RatlantisBlockRegistry.MARBLED_CHEESE_RAT_HEAD.get(), consumer);
		this.archeology(Items.BLUE_ORCHID, RatlantisBlockRegistry.RATGLOVE_FLOWER.get(), consumer);
	}

	private void archeology(ItemLike input, ItemLike output, Consumer<FinishedRecipe> consumer) {
		new SingleItemRecipeBuilder(RecipeCategory.MISC, RatsRecipeRegistry.ARCHEOLOGIST_SERIALIZER.get(), Ingredient.of(input), output, 1).unlockedBy("has_input", has(input)).save(consumer, new ResourceLocation(RatsMod.MODID, "archeologist/" + ForgeRegistries.ITEMS.getKey(output.asItem()).getPath()));
	}

	private void ratlantisStonecuttingRecipe(Consumer<FinishedRecipe> recipe, Ingredient input, ItemLike output, String name) {
		this.ratlantisStonecuttingRecipe(recipe, input, output, 1, name);
	}

	private void ratlantisStonecuttingRecipe(Consumer<FinishedRecipe> recipe, Ingredient input, ItemLike output, int count, String name) {
		SingleItemRecipeBuilder.stonecutting(input, RecipeCategory.BUILDING_BLOCKS, output, count).unlockedBy("has_input", has(Arrays.stream(input.getItems()).toList().get(0).getItem())).save(recipe, new ResourceLocation(RatsMod.MODID, "stonecutting/" + name));
	}
}
