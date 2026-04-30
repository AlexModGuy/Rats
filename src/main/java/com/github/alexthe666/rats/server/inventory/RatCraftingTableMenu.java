package com.github.alexthe666.rats.server.inventory;

import com.github.alexthe666.rats.registry.RatsBlockRegistry;
import com.github.alexthe666.rats.registry.RatsMenuRegistry;
import com.github.alexthe666.rats.server.block.entity.RatCraftingTableBlockEntity;
import com.github.alexthe666.rats.server.inventory.container.TableItemHandlers;
import com.github.alexthe666.rats.server.inventory.slot.ImprovedSlotItemHandler;
import com.github.alexthe666.rats.server.inventory.slot.RatCraftingResultSlot;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;


// 1.21: vanilla RecipeBookMenu was generified to RecipeBookMenu<I extends RecipeInput, R extends Recipe<I>>.
// The rat crafting table doesn't surface a RecipeInput (it ticks recipes server-side off its own matrix
// handler), so we extend AbstractContainerMenu directly. The trade-off is that the player's vanilla
// recipe-book sidebar is not wired; transfers happen via JEI's ghost-matrix path instead.
public class RatCraftingTableMenu extends AbstractContainerMenu {

	private final RatCraftingTableBlockEntity table;
	private final ContainerData data;

	public RatCraftingTableMenu(int id, Inventory playerInventory, RatCraftingTableBlockEntity table, ContainerData data) {
		super(RatsMenuRegistry.RAT_CRAFTING_TABLE_CONTAINER.get(), id);
		this.table = table;
		this.data = data;
		// 1.21: handlers are direct fields (not Optional) on the BlockEntity now.
		this.addSlot(new RatCraftingResultSlot(table.resultHandler, playerInventory.player, table, 0, 130, 40));
		//ghost input
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				int finalI = i;
				int finalJ = j;
				this.addSlot(new ImprovedSlotItemHandler(table, table.matrixHandler, finalJ + finalI * 3, 36 + finalJ * 18, 22 + finalI * 18, true));
			}
		}
		//input
		for (int k = 0; k < 9; ++k) {
			this.addSlot(new ImprovedSlotItemHandler(table, table.bufferHandler, k, k * 18 + 8, 96, false));
		}
		//inventory
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 129 + i * 18));
			}
		}
		//hotbar
		for (int k = 0; k < 9; ++k) {
			this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 187));
		}
		this.addDataSlots(data);
	}

	public RatCraftingTableMenu(int i, Inventory playerInventory, FriendlyByteBuf buf) {
		this(i, playerInventory, resolveBlockEntity(buf.readBlockPos()), new SimpleContainerData(2));
	}

	// Client-side menu factory: the chunk holding the rat crafting table may have unloaded between
	// the server emitting the open-screen packet and the client constructing the menu. Returning a
	// throwaway placeholder BE keeps the menu construction from NPE-crashing the client; the next
	// stillValid() check will close the menu cleanly.
	private static RatCraftingTableBlockEntity resolveBlockEntity(net.minecraft.core.BlockPos pos) {
		net.minecraft.client.multiplayer.ClientLevel level = Minecraft.getInstance().level;
		if (level != null && level.getBlockEntity(pos) instanceof RatCraftingTableBlockEntity table) {
			return table;
		}
		return new RatCraftingTableBlockEntity(pos, com.github.alexthe666.rats.registry.RatsBlockRegistry.RAT_CRAFTING_TABLE.get().defaultBlockState());
	}

	public boolean stillValid(Player player) {
		return stillValid(ContainerLevelAccess.create(this.table.getLevel(), this.table.getBlockPos()), player, RatsBlockRegistry.RAT_CRAFTING_TABLE.get());
	}

	public RatCraftingTableBlockEntity getCraftingTable() {
		return this.table;
	}

	public int getCookProgressionScaled() {
		int i = this.data.get(0);
		int j = this.data.get(1);
		return i != 0 ? i * 23 / j : 0;
	}

	public void incrementRecipeIndex(boolean negative) {
		this.table.incrementSelectedRecipe(negative);
	}

	@Override
	public ItemStack quickMoveStack(Player player, int slotIndex) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(slotIndex);

		if (slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();

			if (slotIndex == 0) {
				itemstack1.getItem().onCraftedBy(itemstack1, player.level(), player);

				// Merge result slot to player inv
				if (!this.moveItemStackTo(itemstack1, 19, 55, true)) {
					return ItemStack.EMPTY;
				}

				slot.onQuickCraft(itemstack1, itemstack);
			} else if (slotIndex >= 1 && slotIndex < 10) {
				// Merge matrix to buffer, then to full player inv
				if (!this.moveItemStackTo(itemstack1, 10, 19, false) && !this.moveItemStackTo(itemstack1, 19, 55, true)) {
					return ItemStack.EMPTY;
				}
			} else if (slotIndex >= 10 && slotIndex < 19) {
				// Merge buffer to full player inv
				if (!this.moveItemStackTo(itemstack1, 19, 55, true)) {
					return ItemStack.EMPTY;
				}
			} else if (slotIndex >= 19 && slotIndex < 46) {
				// Merge player inv to buffer, then to hotbar
				if (!this.moveItemStackTo(itemstack1, 10, 19, false) && !this.moveItemStackTo(itemstack1, 46, 55, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(itemstack1, 10, 19, false) && !this.moveItemStackTo(itemstack1, 19, 46, false)) {
				// Merge hotbar to buffer, then to player inv
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(player, itemstack1);

			if (slotIndex == 0) {
				player.drop(itemstack1, false);
			}
		}

		return itemstack;
	}

	@Override
	public void clicked(int slotIndex, int mouseButton, ClickType type, Player player) {
		if (slotIndex > 0 && slotIndex < 10) {
			ItemStack stack = this.getCarried().copyWithCount(1);
			this.slots.get(slotIndex).set(stack);
			this.table.setChanged();
		} else {
			super.clicked(slotIndex, mouseButton, type, player);
		}
	}

	public void clearCraftingContent() {
		var h = this.table.matrixHandler;
		for (int i = 0; i < h.getSlots(); i++) {
			h.setStackInSlot(i, ItemStack.EMPTY);
		}
	}
}
