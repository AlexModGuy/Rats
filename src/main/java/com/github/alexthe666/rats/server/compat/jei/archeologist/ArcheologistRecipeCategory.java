package com.github.alexthe666.rats.server.compat.jei.archeologist;

import com.github.alexthe666.rats.server.compat.jei.RatsJEIPlugin;
import com.github.alexthe666.rats.server.recipes.SharedRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class ArcheologistRecipeCategory implements IRecipeCategory<SharedRecipe> {

    public ArcheologistDrawable drawable;

    public ArcheologistRecipeCategory() {
        drawable = new ArcheologistDrawable();
    }

    @Override
    public ResourceLocation getUid() {
        return RatsJEIPlugin.ARCHEOLOGIST_RAT_ID;
    }

    @Override
    public Class<? extends SharedRecipe> getRecipeClass() {
        return SharedRecipe.class;
    }

    @SuppressWarnings("deprecation")
    @Override
    public String getTitle() {
        return I18n.format("rats.archeologist_rat.jei_name");
    }

    @Override
    public IDrawable getBackground() {
        return drawable;
    }

    @Override
    public IDrawable getIcon() {
        return null;
    }

    @Override
    public void setIngredients(SharedRecipe sharedRecipe, IIngredients iIngredients) {
        iIngredients.setInput(VanillaTypes.ITEM, sharedRecipe.getInput());
        iIngredients.setOutput(VanillaTypes.ITEM, sharedRecipe.getOutput());

    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, SharedRecipe recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        guiItemStacks.init(0, true, 42, 48);
        guiItemStacks.init(1, false, 113, 48);
        guiItemStacks.set(ingredients);
    }
}
