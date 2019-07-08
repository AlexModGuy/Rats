package com.github.alexthe666.rats.server.compat.jei.chef;

import com.github.alexthe666.rats.server.recipes.SharedRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ChefRecipeWrapper implements IRecipeWrapper {

    private SharedRecipe recipe;

    public ChefRecipeWrapper(SharedRecipe recipe){
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
