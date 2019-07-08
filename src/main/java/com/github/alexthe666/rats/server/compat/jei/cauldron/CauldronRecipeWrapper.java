package com.github.alexthe666.rats.server.compat.jei.cauldron;

import com.github.alexthe666.rats.server.compat.jei.RatsJEIPlugin;
import com.github.alexthe666.rats.server.recipes.SharedRecipe;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CauldronRecipeWrapper implements IRecipeWrapper {

    private SharedRecipe recipeCauldron;

    public CauldronRecipeWrapper(SharedRecipe recipeCauldron){
        this.recipeCauldron = recipeCauldron;
    }


    @Override
    public void getIngredients(IIngredients ingredients) {
        List<ItemStack> stacks = new ArrayList<>();
        stacks.add(recipeCauldron.getInput());
        stacks.add(new ItemStack(Items.CAULDRON));
        ingredients.setInputs(ItemStack.class, stacks);
        ingredients.setOutput(ItemStack.class, recipeCauldron.getOutput());
    }

    public SharedRecipe getRecipeCauldron() {
        return recipeCauldron;
    }
}
