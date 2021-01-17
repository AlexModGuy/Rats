package com.github.alexthe666.rats.server.entity.tile;

import net.minecraft.block.BlockState;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;

public class TileEntityRatCageDecorated extends TileEntity {
    private NonNullList<ItemStack> containedDeco = NonNullList.withSize(1, ItemStack.EMPTY);

    public TileEntityRatCageDecorated() {
        super(RatsTileEntityRegistry.RAT_CAGE_DECORATED);
    }

    public TileEntityRatCageDecorated(TileEntityType type) {
        super(type);
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT tag = new CompoundNBT();
        this.write(tag);
        return new SUpdateTileEntityPacket(pos, 1, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
        read(this.getBlockState(), packet.getNbtCompound());
    }

    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

    public CompoundNBT write(CompoundNBT compound) {
        ItemStackHelper.saveAllItems(compound, this.containedDeco);
        return super.write(compound);
    }

    public void read(BlockState state, CompoundNBT compound) {
        super.read(state, compound);
        containedDeco = NonNullList.withSize(1, ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, containedDeco);
    }

    public ItemStack getContainedItem() {
        return containedDeco.get(0);
    }

    public void setContainedItem(ItemStack stack) {
        containedDeco.set(0, stack);
    }


}
