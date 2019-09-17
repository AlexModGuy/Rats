package com.github.alexthe666.rats.server.inventory;

import com.github.alexthe666.rats.server.items.ItemRatListUpgrade;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;

public class InventoryRatUpgrade implements ISidedInventory {
    public ItemStack upgradeStack;
    private NonNullList<ItemStack> items = NonNullList.<ItemStack>withSize(27, ItemStack.EMPTY);

    public InventoryRatUpgrade(ItemStack upgradeStack){
        this.upgradeStack = upgradeStack;
        if (!upgradeStack.hasTagCompound()) {
            upgradeStack.setTagCompound(new NBTTagCompound());
        }
        readFromNBT(upgradeStack.getTagCompound());
    }

    private void readFromNBT(NBTTagCompound tagCompound) {
        this.items = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(tagCompound, this.items);
    }

    private void writeToNBT(NBTTagCompound tagCompound) {
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
        writeToNBT(upgradeStack.getTagCompound());
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer player) {
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        if (!upgradeStack.hasTagCompound()) {
            upgradeStack.setTagCompound(new NBTTagCompound());
        }
        writeToNBT(upgradeStack.getTagCompound());
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return !(stack.getItem() instanceof ItemRatListUpgrade);
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        this.items.clear();
    }

    @Override
    public String getName() {
        return upgradeStack.getDisplayName();
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public ITextComponent getDisplayName() {
        return upgradeStack.getTextComponent();
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return new int[0];
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return isItemValidForSlot(index, itemStackIn);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return true;
    }
}
