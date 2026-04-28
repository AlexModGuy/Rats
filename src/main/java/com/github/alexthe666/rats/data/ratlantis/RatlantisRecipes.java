package com.github.alexthe666.rats.data.ratlantis;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;

import java.util.concurrent.CompletableFuture;

// PORT-STUB: see RatsUpgradeRecipes.
public class RatlantisRecipes extends RecipeProvider {

    public RatlantisRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        // intentionally empty — see file header
    }
}
