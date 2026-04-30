package com.github.alexthe666.rats.server.inventory.slot;

import com.github.alexthe666.rats.server.block.entity.RatCraftingTableBlockEntity;
import com.google.common.collect.Lists;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

import java.util.List;

public class RatCraftingResultSlot extends SlotItemHandler {

	private final RatCraftingTableBlockEntity table;
	private final Player player;
	private int amountCrafted;

	public RatCraftingResultSlot(IItemHandler handler, Player player, RatCraftingTableBlockEntity te, int slotIndex, int xPosition, int yPosition) {
		super(handler, slotIndex, xPosition, yPosition);
		this.table = te;
		this.player = player;
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return false;
	}

	@Override
	public ItemStack remove(int amount) {
		ItemStack stack = this.getItem();
		this.amountCrafted += stack.getCount();
		// 1.21: handlers are direct fields on the BlockEntity now (not Optional).
		this.table.resultHandler.setStackInSlot(0, ItemStack.EMPTY);
		return stack;
	}

	@Override
	public void setChanged() {
		this.table.updateHelper();
		this.table.updateRecipe();
		this.table.setChanged();
		super.setChanged();
	}

	@Override
	public boolean mayPickup(Player player) {
		return true;
	}

	@Override
	protected void onQuickCraft(ItemStack stack, int amount) {
		this.amountCrafted += amount;
		this.checkTakeAchievements(stack);
	}

	@Override
	protected void onSwapCraft(int amount) {
		this.amountCrafted += amount;
	}

	@Override
	public void onQuickCraft(ItemStack stack, ItemStack other) {
		int i = other.getCount() - stack.getCount();
		if (i > 0) {
			this.onQuickCraft(other, i);
		}
	}

	@Override
	protected void checkTakeAchievements(ItemStack stack) {
		if (this.amountCrafted > 0) {
			stack.onCraftedBy(this.player.level(), this.player, this.amountCrafted);
			EventHooks.firePlayerCraftingEvent(this.player, stack, this.table.matrixWrapper);
		}
		this.amountCrafted = 0;

		RecipeHolder<CraftingRecipe> usedHolder = this.table.getRecipeUsed();
		if (usedHolder != null && this.player instanceof ServerPlayer sp) {
			sp.awardRecipes(List.of(usedHolder));
		}
	}

	@Override
	public void onTake(Player player, ItemStack stack) {
		this.checkTakeAchievements(stack);
		CommonHooks.setCraftingPlayer(player);
		this.table.updateRecipe();
		CommonHooks.setCraftingPlayer(null);
	}
}
