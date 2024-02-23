package com.github.alexthe666.rats.compat.jei;

import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.minecraft.world.level.ItemLike;

public record CauldronInfoHolder(ItemLike additionStack, ItemLike cauldronContents, ItemLike cauldron,
								 ItemLike result) implements IRecipeCategoryExtension {
}
