package com.github.alexthe666.rats.server.compat.jei.cauldron;

import com.github.alexthe666.rats.server.compat.jei.RatsJEIPlugin;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.util.text.translation.I18n;

public class CauldronRecipeCategory implements IRecipeCategory<CauldronRecipeWrapper> {

    public CauldronDrawable drawable;

    public CauldronRecipeCategory() {
        drawable = new CauldronDrawable();
    }

    @Override
    public String getUid() {
        return RatsJEIPlugin.CAULDRON_ID;
    }

    @SuppressWarnings("deprecation")
    @Override
    public String getTitle() {
        return I18n.translateToLocal("tile.cauldron.name");
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
    public void setRecipe(IRecipeLayout recipeLayout, CauldronRecipeWrapper recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        guiItemStacks.init(0, true, 34, 30);
        guiItemStacks.init(1, true, 76, 30);
        guiItemStacks.init(2, false, 127, 30);
        guiItemStacks.set(ingredients);
    }
}
