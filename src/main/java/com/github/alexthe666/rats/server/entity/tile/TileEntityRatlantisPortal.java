package com.github.alexthe666.rats.server.entity.tile;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.EndPortalTileEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;

import javax.annotation.Nullable;

public class TileEntityRatlantisPortal extends EndPortalTileEntity implements ITickableTileEntity {
    private long age;

    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putLong("Age", this.age);
        return compound;
    }

    public void read(CompoundNBT compound) {
        super.read(compound);
        this.age = compound.getLong("Age");
    }

    public void tick() {
        ++this.age;
    }



    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

    public boolean shouldRenderFace(Direction p_184313_1_) {
        return this.getBlockState().isSideInvisible(this.getBlockState(), p_184313_1_);
    }
}