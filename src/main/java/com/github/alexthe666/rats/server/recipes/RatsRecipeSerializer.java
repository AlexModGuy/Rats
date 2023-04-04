package com.github.alexthe666.rats.server.recipes;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import net.minecraftforge.registries.ForgeRegistries;

public class RatsRecipeSerializer<T extends BaseRatRecipe> implements RecipeSerializer<T> {
	final SingleItemMaker<T> factory;

	public RatsRecipeSerializer(SingleItemMaker<T> factory) {
		this.factory = factory;
	}

	@Override
	public T fromJson(ResourceLocation id, JsonObject object) {
		String s = GsonHelper.getAsString(object, "group", "");
		Ingredient ingredient;
		if (GsonHelper.isArrayNode(object, "ingredient")) {
			ingredient = Ingredient.fromJson(GsonHelper.getAsJsonArray(object, "ingredient"));
		} else {
			ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(object, "ingredient"));
		}

		String s1 = GsonHelper.getAsString(object, "result");
		int i = GsonHelper.getAsInt(object, "count");
		ItemStack itemstack = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(s1)), i);
		return this.factory.create(id, s, ingredient, itemstack);
	}

	@Override
	public T fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
		String s = buf.readUtf();
		Ingredient ingredient = Ingredient.fromNetwork(buf);
		ItemStack itemstack = buf.readItem();
		return this.factory.create(id, s, ingredient, itemstack);
	}

	@Override
	public void toNetwork(FriendlyByteBuf buf, T recipe) {
		buf.writeUtf(recipe.getGroup());
		recipe.getIngredients().get(0).toNetwork(buf);
		buf.writeItem(recipe.getResult());
	}

	public interface SingleItemMaker<T extends SingleItemRecipe> {
		T create(ResourceLocation id, String group, Ingredient input, ItemStack output);
	}
}
