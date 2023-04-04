package com.github.alexthe666.rats.server.inventory.container;

import com.github.alexthe666.rats.server.items.RatListUpgradeItem;
import com.github.alexthe666.rats.server.items.upgrades.CombinedRatUpgradeItem;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class RatUpgradeContainer implements WorldlyContainer {
	public final ItemStack upgradeStack;
	private NonNullList<ItemStack> items = NonNullList.withSize(27, ItemStack.EMPTY);

	public RatUpgradeContainer(ItemStack upgradeStack) {
		this.upgradeStack = upgradeStack;
		if (!upgradeStack.hasTag()) {
			upgradeStack.setTag(new CompoundTag());
		}
		this.readFromNBT(upgradeStack.getTag());
	}

	private void readFromNBT(CompoundTag tagCompound) {
		this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
		ContainerHelper.loadAllItems(tagCompound, this.items);
	}

	private void writeToNBT(CompoundTag tagCompound) {
		ContainerHelper.saveAllItems(tagCompound, this.items);
	}

	@Override
	public int getContainerSize() {
		return this.items.size();
	}

	@Override
	public boolean isEmpty() {
		return this.items.isEmpty();
	}

	@Override
	public ItemStack getItem(int index) {
		return this.items.get(index);
	}

	@Override
	public ItemStack removeItem(int index, int count) {
		return ContainerHelper.removeItem(this.items, index, count);

	}

	@Override
	public ItemStack removeItemNoUpdate(int index) {
		return ContainerHelper.takeItem(this.items, index);
	}

	@Override
	public void setItem(int index, ItemStack stack) {
		ItemStack itemstack = this.items.get(index);
		this.items.set(index, stack);
		boolean flag = !stack.isEmpty() && stack.sameItem(itemstack) && ItemStack.isSameItemSameTags(stack, itemstack);
		if (stack.getCount() > this.getMaxStackSize()) {
			stack.setCount(this.getMaxStackSize());
		}
		if (!flag) {
			this.setChanged();
		}
	}

	@Override
	public int getMaxStackSize() {
		return 1;
	}

	@Override
	public void setChanged() {
		this.writeToNBT(this.upgradeStack.getTag());
	}

	@Override
	public boolean stillValid(Player player) {
		return true;
	}

	@Override
	public void startOpen(Player player) {
	}

	@Override
	public void stopOpen(Player player) {
		if (!this.upgradeStack.hasTag()) {
			this.upgradeStack.setTag(new CompoundTag());
		}
		this.writeToNBT(this.upgradeStack.getTag());
	}

	@Override
	public boolean canPlaceItem(int index, ItemStack stack) {
		if (this.upgradeStack.getItem() instanceof CombinedRatUpgradeItem) {
			return CombinedRatUpgradeItem.canCombineWithUpgrade(this.upgradeStack, stack);
		}
		return !(stack.getItem() instanceof RatListUpgradeItem);
	}

	@Override
	public void clearContent() {
		this.items.clear();
	}

	@Override
	public int[] getSlotsForFace(Direction side) {
		return new int[0];
	}

	@Override
	public boolean canPlaceItemThroughFace(int index, ItemStack itemStackIn, @Nullable Direction direction) {
		return canPlaceItem(index, itemStackIn);
	}

	@Override
	public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
		return true;
	}
}
