package com.github.alexthe666.rats.server.entity.tile;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.server.blocks.RatsBlockRegistry;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;

public class TileEntityTrashCan extends TileEntity implements ITickableTileEntity {
    public boolean opened = false;
    public float lidProgress;
    public float prevLidProgress;
    public int trashStored;
    private int timeOpen = 0;
    int ticksExisted;

    public TileEntityTrashCan() {
        super(RatsTileEntityRegistry.TRASH_CAN);
    }

    @Override
    public void tick() {
        ticksExisted++;
        prevLidProgress = lidProgress;
        float lidInc = 2F;
        if (opened && lidProgress < 20.0F) {
            lidProgress += lidInc;
        } else if (!opened && lidProgress > 0.0F) {
            lidProgress -= lidInc;
        }
        if(opened){
            timeOpen++;
            if(timeOpen > 30){
                opened = false;
                timeOpen = 0;
            }
        }
    }

    public void depositGarbage() {
        ItemEntity itemEntity = new ItemEntity(world, this.pos.getX() + 0.5D, this.pos.getY() + 1.0D, this.pos.getZ() + 0.5D, new ItemStack(RatsBlockRegistry.GARBAGE_PILE));
        world.addEntity(itemEntity);
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
        compound.putInt("TrashStored", trashStored);
        compound.putInt("TicksExisted", ticksExisted);
        return super.write(compound);
    }

    public void read(CompoundNBT compound) {
        ticksExisted = compound.getInt("TicksExisted");
        trashStored = compound.getInt("TrashStored");
        super.read(compound);
    }
}
