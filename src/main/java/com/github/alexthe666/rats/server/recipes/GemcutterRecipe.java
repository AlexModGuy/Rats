package com.github.alexthe666.rats.server.recipes;

import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.registry.RatsRecipeRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

public class GemcutterRecipe extends BaseRatRecipe {
	public GemcutterRecipe(ResourceLocation id, String group, Ingredient input, ItemStack output) {
		super(RatsRecipeRegistry.GEMCUTTER.get(), RatsRecipeRegistry.GEMCUTTER_SERIALIZER.get(), id, group, input, output);
	}

	@Override
	public ItemStack getToastSymbol() {
		return new ItemStack(RatsItemRegistry.RAT_UPGRADE_GEMCUTTER.get());
	}

	@Override
	public boolean isSpecial() {
		return true;
	}

	@Override
	public boolean matches(Container container, Level level) {
		return this.ingredient.test(container.getItem(0));
	}
}
