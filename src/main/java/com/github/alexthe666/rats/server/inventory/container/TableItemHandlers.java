package com.github.alexthe666.rats.server.inventory.container;

import com.github.alexthe666.rats.server.block.entity.RatCraftingTableBlockEntity;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class TableItemHandlers {
	public static class BufferHandler extends ItemStackHandler implements StackedContentsCompatible {

		private final RatCraftingTableBlockEntity table;

		public BufferHandler(RatCraftingTableBlockEntity table) {
			super(9);
			this.table = table;
		}

		@Override
		protected void onContentsChanged(int slot) {
			this.table.updateHelper();
			this.table.updateRecipe();
			this.table.setChanged();
		}

		@Override
		protected void onLoad() {
			this.table.updateHelper();
		}

		@Override
		public void fillStackedContents(StackedContents contents) {
			contents.clear();
			for (ItemStack itemstack : this.stacks) {
				contents.accountSimpleStack(itemstack);
			}
		}
	}

	public static class MatrixHandler extends ItemStackHandler {

		private final RatCraftingTableBlockEntity table;

		public MatrixHandler(RatCraftingTableBlockEntity table) {
			super(9);
			this.table = table;
		}

		@Override
		protected void onContentsChanged(int slot) {
			this.table.updateRecipe();
			this.table.setChanged();
		}
	}

	public static class ResultHandler extends ItemStackHandler {

		private final RatCraftingTableBlockEntity table;

		public ResultHandler(RatCraftingTableBlockEntity table) {
			super(1);
			this.table = table;
		}

		@Override
		public boolean isItemValid(int slot, @NotNull ItemStack stack) {
			return false;
		}

		@Override
		public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
			ItemStack stack = super.extractItem(slot, amount, simulate);
			if (!stack.isEmpty()) {
				this.table.updateRecipe();
				this.table.setChanged();
			}
			return stack;
		}
	}
}
