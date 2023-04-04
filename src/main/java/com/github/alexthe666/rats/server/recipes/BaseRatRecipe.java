package com.github.alexthe666.rats.server.recipes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleItemRecipe;

//pain pain pain pain PAINNNNNNNNN
//getResultItem needs RegistryAccess, I cant use that in the serializer. Hate
public abstract class BaseRatRecipe extends SingleItemRecipe {
	public BaseRatRecipe(RecipeType<?> type, RecipeSerializer<?> serializer, ResourceLocation id, String group, Ingredient input, ItemStack output) {
		super(type, serializer, id, group, input, output);
	}

	public final ItemStack getResult() {
		return this.result;
	}
}
