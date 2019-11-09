package com.github.alexthe666.rats.server.compat.jei.gemcutter;

import com.github.alexthe666.rats.server.compat.jei.RatsJEIPlugin;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.util.text.translation.I18n;

public class GemcutterRecipeCategory implements IRecipeCategory<GemcutterRecipeWrapper> {

    public GemcutterDrawable drawable;

    public GemcutterRecipeCategory() {
        drawable = new GemcutterDrawable();
    }

    @Override
    public String getUid() {
        return RatsJEIPlugin.GEMCUTTER_RAT_ID;
    }

    @SuppressWarnings("deprecation")
    @Override
    public String getTitle() {
        return I18n.translateToLocal("rats.gemcutter_rat.jei_name");
    }

    @Override
    public String getModName() {
        return "Rats";
    }

    @Override
    public IDrawable getBackground() {
        return drawable;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, GemcutterRecipeWrapper recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        guiItemStacks.init(0, true, 42, 48);
        guiItemStacks.init(1, false, 113, 48);
        guiItemStacks.set(ingredients);
    }
}
