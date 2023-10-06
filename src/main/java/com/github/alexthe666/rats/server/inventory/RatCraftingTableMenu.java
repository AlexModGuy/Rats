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
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;

import java.util.Objects;

public class RatCraftingTableMenu extends RecipeBookMenu<Container> {

	private final RatCraftingTableBlockEntity table;
	private final ContainerData data;

	public RatCraftingTableMenu(int id, Inventory playerInventory, RatCraftingTableBlockEntity table, ContainerData data) {
		super(RatsMenuRegistry.RAT_CRAFTING_TABLE_CONTAINER.get(), id);
		this.table = table;
		this.data = data;
		//result
		table.resultHandler.ifPresent(handler -> this.addSlot(new RatCraftingResultSlot(handler, playerInventory.player, table, 0, 130, 40)));
		//ghost input
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				int finalI = i;
				int finalJ = j;
				table.matrixHandler.ifPresent(handler -> this.addSlot(new ImprovedSlotItemHandler(table, handler, finalJ + finalI * 3, 36 + finalJ * 18, 22 + finalI * 18, true)));
			}
		}
		//input
		for (int k = 0; k < 9; ++k) {
			int finalK = k;
			table.bufferHandler.ifPresent(handler -> this.addSlot(new ImprovedSlotItemHandler(table, handler, finalK, finalK * 18 + 8, 96, false)));
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
		this(i, playerInventory, (RatCraftingTableBlockEntity) Objects.requireNonNull(Minecraft.getInstance().level != null ? Minecraft.getInstance().level.getBlockEntity(buf.readBlockPos()) : null), new SimpleContainerData(2));
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

	@Override
	public void fillCraftSlotsStackedContents(StackedContents contents) {
		this.table.bufferHandler.ifPresent(handler -> ((TableItemHandlers.BufferHandler) handler).fillStackedContents(contents));
	}

	@Override
	public void clearCraftingContent() {
		this.table.matrixHandler.ifPresent(h -> {
			for (int i = 0; i < h.getSlots(); i++) {
				h.setStackInSlot(i, ItemStack.EMPTY);
			}
		});
	}

	@Override
	public boolean recipeMatches(Recipe<? super Container> recipe) {
		return true;
	}

	@Override
	public int getResultSlotIndex() {
		return 0;
	}

	@Override
	public int getGridWidth() {
		return 3;
	}

	@Override
	public int getGridHeight() {
		return 3;
	}

	@Override
	public int getSize() {
		return 10;
	}

	@Override
	public RecipeBookType getRecipeBookType() {
		return RecipeBookType.CRAFTING;
	}

	@Override
	public boolean shouldMoveToInventory(int index) {
		return false;
	}
}
