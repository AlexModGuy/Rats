package com.github.alexthe666.rats.server.inventory;

import com.github.alexthe666.rats.server.items.ItemRatListUpgrade;
import com.github.alexthe666.rats.server.items.ItemRatUpgradeCombined;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;

public class InventoryRatUpgrade implements ISidedInventory {
    public ItemStack upgradeStack;
    private NonNullList<ItemStack> items = NonNullList.withSize(27, ItemStack.EMPTY);

    public InventoryRatUpgrade(ItemStack upgradeStack) {
        this.upgradeStack = upgradeStack;
        if (!upgradeStack.hasTag()) {
            upgradeStack.setTag(new CompoundNBT());
        }
        readFromNBT(upgradeStack.getTag());
    }

    private void readFromNBT(CompoundNBT tagCompound) {
        this.items = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(tagCompound, this.items);
    }

    private void writeToNBT(CompoundNBT tagCompound) {
        ItemStackHelper.saveAllItems(tagCompound, this.items);
    }

    @Override
    public int getSizeInventory() {
        return this.items.size();
    }

    @Override
    public boolean isEmpty() {
        return this.items.isEmpty();
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return this.items.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(items, index, count);

    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.items, index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        ItemStack itemstack = this.items.get(index);
        this.items.set(index, stack);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }
        if (!flag) {
            this.markDirty();
        }
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public void markDirty() {
        writeToNBT(upgradeStack.getTag());
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        return true;
    }

    @Override
    public void openInventory(PlayerEntity player) {
    }

    @Override
    public void closeInventory(PlayerEntity player) {
        if (!upgradeStack.hasTag()) {
            upgradeStack.setTag(new CompoundNBT());
        }
        writeToNBT(upgradeStack.getTag());
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if (upgradeStack.getItem() instanceof ItemRatUpgradeCombined) {
            return ItemRatUpgradeCombined.canCombineWithUpgrade(upgradeStack, stack);
        }
        return !(stack.getItem() instanceof ItemRatListUpgrade);
    }

    @Override
    public void clear() {
        this.items.clear();
    }

    public ITextComponent getDisplayName() {
        return upgradeStack.getTextComponent();
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return new int[0];
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, Direction direction) {
        return isItemValidForSlot(index, itemStackIn);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        return true;
    }
}
