package com.github.alexthe666.rats.server.inventory.container;

import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;

public class CraftingContainerWrapper extends TransientCraftingContainer {

	private final IItemHandlerModifiable inv;

	public CraftingContainerWrapper(IItemHandlerModifiable handler) {
		super(null, 3, 3);
		this.inv = handler;
	}

	@Override
	public int getContainerSize() {
		return this.inv.getSlots();
	}

	@Override
	public boolean isEmpty() {
		for (int i = 0; i < this.inv.getSlots(); i++) {
			if (!this.inv.getStackInSlot(i).isEmpty()) return false;
		}
		return true;
	}

	@Override
	public ItemStack getItem(int index) {
		return this.inv.getStackInSlot(index);
	}

	@Override
	public ItemStack removeItemNoUpdate(int index) {
		ItemStack s = this.getItem(index);
		if (s.isEmpty()) return ItemStack.EMPTY;
		this.setItem(index, ItemStack.EMPTY);
		return s;
	}

	@Override
	public ItemStack removeItem(int index, int count) {
		ItemStack stack = this.inv.getStackInSlot(index);
		return stack.isEmpty() ? ItemStack.EMPTY : stack.split(count);
	}

	@Override
	public void setItem(int index, @Nonnull ItemStack stack) {
		this.inv.setStackInSlot(index, stack);
	}

	@Override
	public void clearContent() {
		for (int i = 0; i < this.inv.getSlots(); i++) {
			this.inv.setStackInSlot(i, ItemStack.EMPTY);
		}
	}
}
