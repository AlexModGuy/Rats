package com.github.alexthe666.rats.server.recipes;

import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.registry.RatsRecipeRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

public class ChefRecipe extends BaseRatRecipe {
	public ChefRecipe(String group, Ingredient input, ItemStack output) {
		super(RatsRecipeRegistry.CHEF.get(), RatsRecipeRegistry.CHEF_SERIALIZER.get(), group, input, output);
	}

	@Override
	public ItemStack getToastSymbol() {
		return new ItemStack(RatsItemRegistry.RAT_UPGRADE_CHEF.get());
	}

	@Override
	public boolean isSpecial() {
		return true;
	}

	@Override
	public boolean matches(SingleRecipeInput input, Level level) {
		return this.ingredient.test(input.item());
	}
}
