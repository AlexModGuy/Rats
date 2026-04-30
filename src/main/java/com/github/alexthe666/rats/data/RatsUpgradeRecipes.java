package com.github.alexthe666.rats.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;

import java.util.concurrent.CompletableFuture;

// Datagen note (1.21): 1.20.5+ rewrote RecipeProvider to take CompletableFuture<HolderLookup
// .Provider> and changed buildRecipes(Consumer<FinishedRecipe>) to
// buildRecipes(RecipeOutput). Pre-generated recipe JSON in
// src/generated/resources/data/rats/recipes/ is the runtime source of truth.
public class RatsUpgradeRecipes extends RecipeProvider {

    public RatsUpgradeRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        // intentionally empty — see file header
    }
}
