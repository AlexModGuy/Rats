package com.github.alexthe666.rats.compat.jei;

import com.github.alexthe666.rats.registry.RatsMenuRegistry;
import com.github.alexthe666.rats.server.inventory.RatCraftingTableMenu;
import com.github.alexthe666.rats.server.message.SetGhostMatrixPacket;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// Crafting-specific transfer handler. JEI's RecipeTransferManager looks up handlers keyed by
// (containerClass, recipeType) first and only falls back to the universal handler if no specific
// match exists. Registering this dedicated handler ensures the "+" button reliably appears on
// vanilla crafting recipes when the rat crafting table is open, regardless of how JEI internally
// resolves the universal-fallback ordering.
public class RatCraftingTableCraftingTransferHandler implements IRecipeTransferHandler<RatCraftingTableMenu, RecipeHolder<CraftingRecipe>> {

	public static final RatCraftingTableCraftingTransferHandler INSTANCE = new RatCraftingTableCraftingTransferHandler();

	@Override
	public Class<? extends RatCraftingTableMenu> getContainerClass() {
		return RatCraftingTableMenu.class;
	}

	@Override
	public Optional<MenuType<RatCraftingTableMenu>> getMenuType() {
		return Optional.of(RatsMenuRegistry.RAT_CRAFTING_TABLE_CONTAINER.get());
	}

	@Override
	public mezz.jei.api.recipe.RecipeType<RecipeHolder<CraftingRecipe>> getRecipeType() {
		return mezz.jei.api.constants.RecipeTypes.CRAFTING;
	}

	@Override
	public @Nullable IRecipeTransferError transferRecipe(RatCraftingTableMenu container, RecipeHolder<CraftingRecipe> recipe, IRecipeSlotsView recipeSlots, Player player, boolean maxTransfer, boolean doTransfer) {
		if (!doTransfer) return null;

		// JEI's vanilla CraftingGridHelper builds 9 input slot views in row-major order
		// (createInputSlots iterates y outer, x inner) and pre-places each ingredient at its
		// correct 3x3 grid index via getCraftingIndex(). So the slot-view list is already shape-
		// aligned with our matrix indices — we just copy 1:1.
		List<IRecipeSlotView> inputViews = recipeSlots.getSlotViews(RecipeIngredientRole.INPUT);
		List<ItemStack> matrix = new ArrayList<>(9);
		for (int i = 0; i < 9; i++) matrix.add(ItemStack.EMPTY);
		int n = Math.min(inputViews.size(), 9);
		for (int i = 0; i < n; i++) {
			Optional<ItemStack> display = inputViews.get(i).getDisplayedItemStack();
			matrix.set(i, display.map(s -> s.copyWithCount(1)).orElse(ItemStack.EMPTY));
		}

		for (int i = 0; i < 9; i++) {
			container.getSlot(i + 1).set(matrix.get(i));
		}
		PacketDistributor.sendToServer(new SetGhostMatrixPacket(container.getCraftingTable().getBlockPos().asLong(), matrix));
		return null;
	}
}
