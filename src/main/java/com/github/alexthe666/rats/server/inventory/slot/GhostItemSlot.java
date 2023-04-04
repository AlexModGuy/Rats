package com.github.alexthe666.rats.server.inventory.slot;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class GhostItemSlot extends Slot {
	public GhostItemSlot(Container inventory, int id, int x, int y) {
		super(inventory, id, x, y);
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return true;
	}

	@Override
	public boolean mayPickup(Player player) {
		return false;
	}

	@Override
	public void onTake(Player player, ItemStack stack) {

	}

	@Override
	public ItemStack remove(int slot) {
		return ItemStack.EMPTY;
	}

	@Override
	public void set(ItemStack stack) {
		if (!stack.isEmpty()) {
			stack = stack.copy();
		}
		super.set(stack);
	}
}
