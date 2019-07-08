package com.github.alexthe666.rats.server.compat.jei.chef;

import com.github.alexthe666.rats.server.compat.jei.RatsJEIPlugin;
import com.github.alexthe666.rats.server.compat.jei.cauldron.CauldronDrawable;
import com.github.alexthe666.rats.server.compat.jei.cauldron.CauldronRecipeWrapper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.util.text.translation.I18n;

public class ChefRecipeCategory implements IRecipeCategory<ChefRecipeWrapper> {

    public ChefDrawable drawable;

    public ChefRecipeCategory(){
        drawable = new ChefDrawable();
    }
    @Override
    public String getUid() {
        return RatsJEIPlugin.CHEF_RAT_ID;
    }

    @SuppressWarnings("deprecation")
    @Override
    public String getTitle() {
        return I18n.translateToLocal("rats.chef_rat.jei_name");
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
    public void setRecipe(IRecipeLayout recipeLayout, ChefRecipeWrapper recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        guiItemStacks.init(0, true, 42, 48);
        guiItemStacks.init(1, false, 113, 48);
        guiItemStacks.set(ingredients);
    }
}
