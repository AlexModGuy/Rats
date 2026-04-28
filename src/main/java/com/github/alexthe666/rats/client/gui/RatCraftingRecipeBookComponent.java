package com.github.alexthe666.rats.client.gui;

// PORT-STUB: 1.21 RecipeBookComponent now requires a generic-typed RecipeBookMenu<I, R> as its menu (with RecipeInput).
// Our RatCraftingTableMenu dropped the RecipeBookMenu inheritance because the new generic signature isn't satisfiable
// against our handler-backed crafting state. Without that base, this client-side recipe-book glue can't compile.
// This class is now an empty placeholder so RatCraftingTableScreen still has the symbol; the recipe book button is
// effectively non-functional until the menu is rewritten against the new generics.
public class RatCraftingRecipeBookComponent {
}
