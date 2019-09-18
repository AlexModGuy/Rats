package com.github.alexthe666.rats.server.compat.jei.archeologist;

import com.github.alexthe666.rats.server.recipes.SharedRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

public class ArcheologistRecipeWrapper implements IRecipeWrapper {

    private SharedRecipe recipe;

    public ArcheologistRecipeWrapper(SharedRecipe recipe){
        this.recipe = recipe;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInput(ItemStack.class, recipe.getInput());
        ingredients.setOutput(ItemStack.class, recipe.getOutput());
    }

    public SharedRecipe getRecipeChef() {
        return recipe;
    }
}
