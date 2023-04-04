package com.github.alexthe666.rats.compat.jei;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.recipes.ArcheologistRecipe;
import com.github.alexthe666.rats.server.recipes.ChefRecipe;
import com.github.alexthe666.rats.server.recipes.GemcutterRecipe;
import mezz.jei.api.recipe.RecipeType;

public class RatsRecipeTypes {
	public static final RecipeType<ArcheologistRecipe> ARCHEOLOGIST = RecipeType.create(RatsMod.MODID, "archeologist", ArcheologistRecipe.class);
	public static final RecipeType<CauldronInfoHolder> CAULDRON = RecipeType.create(RatsMod.MODID, "cauldron", CauldronInfoHolder.class);
	public static final RecipeType<ChefRecipe> CHEF = RecipeType.create(RatsMod.MODID, "chef", ChefRecipe.class);
	public static final RecipeType<GemcutterRecipe> GEMCUTTER = RecipeType.create(RatsMod.MODID, "gemcutter", GemcutterRecipe.class);
}
