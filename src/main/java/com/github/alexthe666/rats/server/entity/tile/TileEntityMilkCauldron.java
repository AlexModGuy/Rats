package com.github.alexthe666.rats.server.entity.tile;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.blocks.RatsBlockRegistry;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;

public class TileEntityMilkCauldron extends TileEntity implements ITickable {
    int ticksExisted;

    @Override
    public void update() {
        ticksExisted++;
        if (RatsMod.CONFIG_OPTIONS.cheesemaking && ticksExisted >= RatsMod.CONFIG_OPTIONS.milkCauldronTime) {
            world.setBlockState(this.getPos(), RatsBlockRegistry.CHEESE_CAULDRON.getDefaultState());
            world.playSound(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), SoundEvents.BLOCK_BREWING_STAND_BREW, SoundCategory.BLOCKS, 1, 1, false);
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("TicksExisted", ticksExisted);
        return super.writeToNBT(compound);
    }

    public void readFromNBT(NBTTagCompound compound) {
        ticksExisted = compound.getInteger("TicksExisted");
        super.readFromNBT(compound);
    }

}
