package com.github.alexthe666.rats.server.entity.tile;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;

public class TileEntityRatHole extends TileEntity {
    private NonNullList<ItemStack> immitationStack = NonNullList.withSize(1, ItemStack.EMPTY);

    public TileEntityRatHole() {
        setImmitatedBlockState(Blocks.PLANKS.getDefaultState());
    }

    public TileEntityRatHole(IBlockState state) {
        setImmitatedBlockState(state);
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound tag = new NBTTagCompound();
        this.writeToNBT(tag);
        return new SPacketUpdateTileEntity(pos, 1, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        readFromNBT(packet.getNbtCompound());
    }

    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }


    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        ItemStackHelper.saveAllItems(compound, this.immitationStack);
        return super.writeToNBT(compound);
    }

    public void readFromNBT(NBTTagCompound compound) {
        immitationStack = NonNullList.withSize(1, ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, immitationStack);
        super.readFromNBT(compound);
    }

    public IBlockState getImmitatedBlockState() {
        IBlockState defState = Blocks.PLANKS.getDefaultState();
        if (!immitationStack.get(0).isEmpty() && immitationStack.get(0).getItem() instanceof ItemBlock) {
            Block block = ((ItemBlock) immitationStack.get(0).getItem()).getBlock();
            return block.getStateFromMeta(immitationStack.get(0).getItemDamage());
        }
        return defState;
    }

    public void setImmitatedBlockState(IBlockState state) {
        ItemStack stack = new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state));
        immitationStack.set(0, stack);
    }


}
