package com.github.alexthe666.rats.compat.jei;

import com.github.alexthe666.rats.registry.RatsMenuRegistry;
import com.github.alexthe666.rats.server.inventory.RatCraftingTableMenu;
import com.github.alexthe666.rats.server.message.SetGhostMatrixPacket;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IUniversalRecipeTransferHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// JEI 19.8.1+: IUniversalRecipeTransferHandler<C>. JEI's vanilla CraftingGridHelper always builds
// 9 input slot views (row-major: y outer, x inner) regardless of the recipe's actual shape, and
// pre-places each ingredient at its correct 3x3 grid position via getCraftingIndex(...). So we
// just iterate the 9 input views linearly and copy each displayed stack into the matching matrix
// slot — the shape is already baked into the slot ordering.
public class RatCraftingTableTransferHandler implements IUniversalRecipeTransferHandler<RatCraftingTableMenu> {

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
	public @Nullable IRecipeTransferError transferRecipe(RatCraftingTableMenu container, Object recipe, IRecipeSlotsView recipeSlots, Player player, boolean maxTransfer, boolean doTransfer) {
		if (!doTransfer) return null;

		List<IRecipeSlotView> inputViews = recipeSlots.getSlotViews(RecipeIngredientRole.INPUT);
		List<ItemStack> matrix = new ArrayList<>(9);
		for (int i = 0; i < 9; i++) matrix.add(ItemStack.EMPTY);
		int n = Math.min(inputViews.size(), 9);
		for (int i = 0; i < n; i++) {
			Optional<ItemStack> display = inputViews.get(i).getDisplayedItemStack();
			matrix.set(i, display.map(s -> s.copyWithCount(1)).orElse(ItemStack.EMPTY));
		}

		// Mirror to the local container immediately so the matrix lights up without waiting for the
		// network round-trip. The slot.set chain triggers MatrixHandler.onContentsChanged →
		// updateRecipe(), populating the client-side guideRecipe so the result preview renders.
		for (int i = 0; i < 9; i++) {
			container.getSlot(i + 1).set(matrix.get(i));
		}
		// Authoritative server sync: server fills its matrix, recomputes the recipe, and
		// broadcastChanges sends the result back to the client.
		PacketDistributor.sendToServer(new SetGhostMatrixPacket(container.getCraftingTable().getBlockPos().asLong(), matrix));
		return null;
	}
}
