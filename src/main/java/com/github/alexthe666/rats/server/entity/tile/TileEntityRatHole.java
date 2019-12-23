package com.github.alexthe666.rats.server.entity.tile;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;

public class TileEntityRatHole extends TileEntity {
    private NonNullList<ItemStack> immitationStack = NonNullList.withSize(1, ItemStack.EMPTY);

    public TileEntityRatHole() {
        super(RatsTileEntityRegistry.RAT_HOLE);
        setImmitatedBlockState(Blocks.OAK_PLANKS.getDefaultState());
    }

    public TileEntityRatHole(BlockState state) {
        super(RatsTileEntityRegistry.RAT_HOLE);
        setImmitatedBlockState(state);
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT tag = new CompoundNBT();
        this.write(tag);
        return new SUpdateTileEntityPacket(pos, 1, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
        read(packet.getNbtCompound());
    }

    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }


    public CompoundNBT write(CompoundNBT compound) {
        ItemStackHelper.saveAllItems(compound, this.immitationStack);
        return super.write(compound);
    }

    public void read(CompoundNBT compound) {
        immitationStack = NonNullList.withSize(1, ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, immitationStack);
        super.read(compound);
    }

    public BlockState getImmitatedBlockState() {
        BlockState defState = Blocks.OAK_PLANKS.getDefaultState();
        if (!immitationStack.get(0).isEmpty() && immitationStack.get(0).getItem() instanceof BlockItem) {
            Block block = ((BlockItem) immitationStack.get(0).getItem()).getBlock();
            return block.getDefaultState();
        }
        return defState;
    }

    public void setImmitatedBlockState(BlockState state) {
        ItemStack stack = new ItemStack(state.getBlock(), 1);
        immitationStack.set(0, stack);
    }


}
