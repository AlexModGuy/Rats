package com.github.alexthe666.rats.server.compat.jei.gemcutter;

import com.github.alexthe666.rats.server.compat.jei.RatsJEIPlugin;
import com.github.alexthe666.rats.server.recipes.SharedRecipe;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

@SuppressWarnings("deprecation")
public class GemcutterRecipeHandler implements IRecipeHandler<GemcutterRecipeWrapper> {

    @Override
    public Class getRecipeClass() {
        return SharedRecipe.class;
    }

    @Override
    public String getRecipeCategoryUid(GemcutterRecipeWrapper recipe) {
        return RatsJEIPlugin.GEMCUTTER_RAT_ID;
    }

    @Override
    public IRecipeWrapper getRecipeWrapper(GemcutterRecipeWrapper recipe) {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(GemcutterRecipeWrapper recipe) {
        return true;
    }
}
