package com.github.alexthe666.rats.server.compat.jei.cauldron;

import com.github.alexthe666.rats.server.compat.jei.RatsJEIPlugin;
import com.github.alexthe666.rats.server.recipes.SharedRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class CauldronRecipeCategory implements IRecipeCategory<SharedRecipe> {

    public CauldronDrawable drawable;

    public CauldronRecipeCategory() {
        drawable = new CauldronDrawable();
    }

    @Override
    public ResourceLocation getUid() {
        return RatsJEIPlugin.CAULDRON_ID;
    }

    @Override
    public Class<? extends SharedRecipe> getRecipeClass() {
        return SharedRecipe.class;
    }

    @SuppressWarnings("deprecation")
    @Override
    public String getTitle() {
        return I18n.format("block.cauldron.name");
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
        List<ItemStack> inputs = new ArrayList<>();
        inputs.add(sharedRecipe.getInput());
        inputs.add(new ItemStack(Items.CAULDRON));
        iIngredients.setInputs(VanillaTypes.ITEM, inputs);
        iIngredients.setOutput(VanillaTypes.ITEM, sharedRecipe.getOutput());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, SharedRecipe recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        guiItemStacks.init(0, true, 34, 30);
        guiItemStacks.init(1, true, 76, 30);
        guiItemStacks.init(2, false, 127, 30);
        guiItemStacks.set(ingredients);
    }
}
