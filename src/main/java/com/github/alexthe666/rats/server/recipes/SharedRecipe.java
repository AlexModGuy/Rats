package com.github.alexthe666.rats.server.recipes;

import net.minecraft.item.ItemStack;

public class SharedRecipe {

    private ItemStack input;
    private ItemStack output;

    public SharedRecipe(ItemStack input, ItemStack output){
        this.input = input;
        this.output = output;
    }

    public ItemStack getInput() {
        return input;
    }

    public ItemStack getOutput() {
        return output;
    }
}
