package com.github.alexthe666.rats.server.compat.jei.chef;

import com.github.alexthe666.rats.server.compat.jei.RatsJEIPlugin;
import com.github.alexthe666.rats.server.recipes.SharedRecipe;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

@SuppressWarnings("deprecation")
public class ChefRecipeHandler implements IRecipeHandler<ChefRecipeWrapper> {

    @Override
    public Class getRecipeClass() {
        return SharedRecipe.class;
    }

    @Override
    public String getRecipeCategoryUid(ChefRecipeWrapper recipe) {
        return RatsJEIPlugin.CHEF_RAT_ID;
    }

    @Override
    public IRecipeWrapper getRecipeWrapper(ChefRecipeWrapper recipe) {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(ChefRecipeWrapper recipe) {
        return true;
    }
}
