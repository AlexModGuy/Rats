package com.github.alexthe666.rats.server.entity.tile;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;

public class TrashCanInventoryWrapperTop implements IItemHandlerModifiable {

    private TileEntityTrashCan tile;
    private Direction side;

    public TrashCanInventoryWrapperTop(TileEntityTrashCan tile, Direction side) {
        this.tile = tile;
        this.side = side;
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        // this.tile.hasProduced = false;
    }

    @Override
    public int getSlots() {
        return 1;
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (this.tile.trashStored >= 7) {
            return stack;
        }
        int extractionAmount = 1;
        ItemStack copy = stack.copy();
        copy.setCount(stack.getCount() - extractionAmount);
        if(!simulate){
            this.tile.trashStored++;
        }
        BlockState blockstate = tile.getWorld().getBlockState(tile.getPos());
        tile.getWorld().notifyBlockUpdate(tile.getPos(), blockstate, blockstate, 3);
        return copy;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return false;
    }

    public static LazyOptional<IItemHandler> create(TileEntityTrashCan trashCan, Direction sides) {
        return LazyOptional.of(() -> new TrashCanInventoryWrapperTop(trashCan, sides));
    }
}
