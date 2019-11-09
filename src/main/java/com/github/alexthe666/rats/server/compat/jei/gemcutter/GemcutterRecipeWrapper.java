package com.github.alexthe666.rats.server.compat.jei.gemcutter;

import com.github.alexthe666.rats.server.recipes.SharedRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

public class GemcutterRecipeWrapper implements IRecipeWrapper {

    private SharedRecipe recipe;

    public GemcutterRecipeWrapper(SharedRecipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInput(ItemStack.class, recipe.getInput());
        ingredients.setOutput(ItemStack.class, recipe.getOutput());
    }

    public SharedRecipe getRecipeGemcutter() {
        return recipe;
    }
}
