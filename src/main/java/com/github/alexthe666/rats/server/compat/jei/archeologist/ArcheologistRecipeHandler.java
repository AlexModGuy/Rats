package com.github.alexthe666.rats.server.compat.jei.archeologist;

import com.github.alexthe666.rats.server.compat.jei.RatsJEIPlugin;
import com.github.alexthe666.rats.server.compat.jei.chef.ChefRecipeWrapper;
import com.github.alexthe666.rats.server.recipes.SharedRecipe;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

@SuppressWarnings("deprecation")
public class ArcheologistRecipeHandler implements IRecipeHandler<ArcheologistRecipeWrapper> {

    @Override
    public Class getRecipeClass() {
        return SharedRecipe.class;
    }

    @Override
    public String getRecipeCategoryUid(ArcheologistRecipeWrapper recipe) {
        return RatsJEIPlugin.ARCHEOLOGIST_RAT_ID;
    }

    @Override
    public IRecipeWrapper getRecipeWrapper(ArcheologistRecipeWrapper recipe) {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(ArcheologistRecipeWrapper recipe) {
        return true;
    }
}
