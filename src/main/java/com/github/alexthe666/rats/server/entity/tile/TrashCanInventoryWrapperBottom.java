package com.github.alexthe666.rats.server.entity.tile;

import com.github.alexthe666.rats.server.blocks.RatsBlockRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nonnull;

public class TrashCanInventoryWrapperBottom implements IItemHandlerModifiable {

    private TileEntityTrashCan tile;
    private Direction side;

    public TrashCanInventoryWrapperBottom(TileEntityTrashCan tile, Direction side) {
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
        return this.tile.trashStored >= 7 ? new ItemStack(RatsBlockRegistry.GARBAGE_PILE) : ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        return stack;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (this.tile.trashStored >= 7) {
            if (!simulate){
                this.tile.trashStored = 0;
                BlockState blockstate = tile.getWorld().getBlockState(tile.getPos());
                tile.getWorld().notifyBlockUpdate(tile.getPos(), blockstate, blockstate, 3);
            }
            return new ItemStack(RatsBlockRegistry.GARBAGE_PILE);
        }
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
        return LazyOptional.of(() -> new TrashCanInventoryWrapperBottom(trashCan, sides));
    }

}
