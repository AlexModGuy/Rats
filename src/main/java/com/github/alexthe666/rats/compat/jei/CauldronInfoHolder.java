package com.github.alexthe666.rats.compat.jei;

import net.minecraft.world.level.ItemLike;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;

public record CauldronInfoHolder(ItemLike additionStack, ItemLike cauldronContents, ItemLike cauldron,
								 ItemLike result) implements IRecipeCategoryExtension {
}







