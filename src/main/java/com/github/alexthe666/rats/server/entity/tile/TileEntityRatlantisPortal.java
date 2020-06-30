package com.github.alexthe666.rats.server.entity.tile;

import com.github.alexthe666.rats.server.blocks.RatsBlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.EndPortalTileEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;

public class TileEntityRatlantisPortal extends EndPortalTileEntity implements ITickableTileEntity {
    private long age;

    public TileEntityRatlantisPortal() {
        super(RatsTileEntityRegistry.RATLANTIS_PORTAL);
    }

    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putLong("Age", this.age);
        return compound;
    }


    public void func_230337_a_(BlockState state, CompoundNBT compound) {
        super.func_230337_a_(state, compound);
        this.age = compound.getLong("Age");
    }

    public void tick() {
        ++this.age;
    }

    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

    public boolean shouldRenderFace(Direction p_184313_1_) {
        if(this.getWorld() == null){
            return true;
        }
        Block block = this.getWorld().getBlockState(this.getPos().offset(p_184313_1_)).getBlock();
        return block != RatsBlockRegistry.RATLANTIS_PORTAL;
    }
}