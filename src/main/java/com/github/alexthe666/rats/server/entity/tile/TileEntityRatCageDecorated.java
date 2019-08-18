package com.github.alexthe666.rats.server.entity.tile;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;

public class TileEntityRatCageDecorated extends TileEntity {
    private NonNullList<ItemStack> containedDeco = NonNullList.withSize(1, ItemStack.EMPTY);

    public TileEntityRatCageDecorated(){ }

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
        ItemStackHelper.saveAllItems(compound, this.containedDeco);
        return super.writeToNBT(compound);
    }

    public void readFromNBT(NBTTagCompound compound) {
        containedDeco = NonNullList.withSize(1, ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, containedDeco);
        super.readFromNBT(compound);
    }

    public Item getContainedItem(){
        Item defState = Items.AIR;
        if(!containedDeco.get(0).isEmpty()){
            return containedDeco.get(0).getItem();
        }
        return defState;
    }

    public void setContainedItem(ItemStack stack){
        containedDeco.set(0, stack);
    }


}
