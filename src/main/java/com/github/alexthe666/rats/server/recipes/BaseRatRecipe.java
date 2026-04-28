package com.github.alexthe666.rats.server.recipes;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleItemRecipe;

// 1.21: SingleItemRecipe constructor no longer takes ResourceLocation id (recipe identity moved to RecipeHolder).
public abstract class BaseRatRecipe extends SingleItemRecipe {
	public BaseRatRecipe(RecipeType<?> type, RecipeSerializer<?> serializer, String group, Ingredient input, ItemStack output) {
		super(type, serializer, group, input, output);
	}

	public final ItemStack getResult() {
		return this.result;
	}

	public final Ingredient getInputIngredient() {
		return this.ingredient;
	}
}
