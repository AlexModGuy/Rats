package com.github.alexthe666.rats.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;

import java.util.concurrent.CompletableFuture;

// Datagen note (1.21): see RatsUpgradeRecipes.
public class RatsRecipes extends RatsUpgradeRecipes {

    public RatsRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        super.buildRecipes(output);
        // intentionally empty — see file header
    }
}
