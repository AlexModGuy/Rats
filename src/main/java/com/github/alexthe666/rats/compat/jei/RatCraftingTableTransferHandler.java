package com.github.alexthe666.rats.compat.jei;

import com.github.alexthe666.rats.registry.RatsMenuRegistry;
import com.github.alexthe666.rats.server.inventory.RatCraftingTableMenu;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class RatCraftingTableTransferHandler implements IRecipeTransferHandler<RatCraftingTableMenu, Object> {

	public static final RatCraftingTableTransferHandler INSTANCE = new RatCraftingTableTransferHandler();

	@Override
	public Class<? extends RatCraftingTableMenu> getContainerClass() {
		return RatCraftingTableMenu.class;
	}

	@Override
	public Optional<MenuType<RatCraftingTableMenu>> getMenuType() {
		return Optional.of(RatsMenuRegistry.RAT_CRAFTING_TABLE_CONTAINER.get());
	}

	@Override
	public RecipeType<Object> getRecipeType() {
		return null;
	}

	@Override
	public @Nullable IRecipeTransferError transferRecipe(RatCraftingTableMenu container, Object recipe, IRecipeSlotsView recipeSlots, Player player, boolean maxTransfer, boolean doTransfer) {
		if (doTransfer) {
			//start at 1 so we dont add anything to the output
			int index = 1;
			for (IRecipeSlotView view : recipeSlots.getSlotViews(RecipeIngredientRole.INPUT)) {
				//moving the displayed stack to first
				Optional<ItemStack> displayStack = view.getDisplayedItemStack();
				container.getSlot(index).set(displayStack.orElse(ItemStack.EMPTY));
				index++;
			}
		}
		return null;
	}
}
