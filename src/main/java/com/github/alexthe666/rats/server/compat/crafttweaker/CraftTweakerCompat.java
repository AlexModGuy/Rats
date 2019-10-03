package com.github.alexthe666.rats.server.compat.crafttweaker;

import com.github.alexthe666.rats.server.recipes.RatsRecipeRegistry;
import com.github.alexthe666.rats.server.recipes.SharedRecipe;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.rats.recipes")
public class CraftTweakerCompat {

    public static void preInit() {
        CraftTweakerAPI.registerClass(CraftTweakerCompat.class);
    }

    @ZenMethod
    public static void addChefRatRecipe(IItemStack iinput, IItemStack ioutput) {
        RatsRecipeRegistry.RAT_CHEF_RECIPES.add(new SharedRecipe(CraftTweakerMC.getItemStack(iinput), CraftTweakerMC.getItemStack(ioutput)));
    }

    @ZenMethod
    public static void removeChefRatRecipe(IItemStack ioutput) {
        ItemStack output = CraftTweakerMC.getItemStack(ioutput).copy();
        output.setCount(1);
        RatsRecipeRegistry.RAT_CHEF_RECIPES.removeIf(recipe -> recipe.getOutput().copy().isItemEqual(output));
    }

    @ZenMethod
    public static void addArcheologistRatRecipe(IItemStack iinput, IItemStack ioutput) {
        RatsRecipeRegistry.RAT_ARCHEOLOGIST_RECIPES.add(new SharedRecipe(CraftTweakerMC.getItemStack(iinput), CraftTweakerMC.getItemStack(ioutput)));
    }

    @ZenMethod
    public static void removeArcheologistRatRecipe(IItemStack ioutput) {
        ItemStack output = CraftTweakerMC.getItemStack(ioutput).copy();
        output.setCount(1);
        RatsRecipeRegistry.RAT_ARCHEOLOGIST_RECIPES.removeIf(recipe -> recipe.getOutput().copy().isItemEqual(output));
    }

}
