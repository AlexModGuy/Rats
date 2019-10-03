package com.github.alexthe666.rats.server.compat.jei.cauldron;

import com.github.alexthe666.rats.server.compat.jei.RatsJEIPlugin;
import com.github.alexthe666.rats.server.recipes.SharedRecipe;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

@SuppressWarnings("deprecation")
public class CauldronRecipeHandler implements IRecipeHandler<CauldronRecipeWrapper> {

    @Override
    public Class getRecipeClass() {
        return SharedRecipe.class;
    }

    @Override
    public String getRecipeCategoryUid(CauldronRecipeWrapper recipe) {
        return RatsJEIPlugin.CAULDRON_ID;
    }

    @Override
    public IRecipeWrapper getRecipeWrapper(CauldronRecipeWrapper recipe) {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(CauldronRecipeWrapper recipe) {
        return true;
    }
}
