package com.github.alexthe666.rats.server.entity.tile;

import com.github.alexthe666.rats.server.blocks.RatsBlockRegistry;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;

public class TileEntityMilkCauldron extends TileEntity implements ITickable {
    int ticksExisted;

    @Override
    public void update() {
        ticksExisted++;
        if (RatConfig.cheesemaking && ticksExisted >= RatConfig.milkCauldronTime) {
            world.setBlockState(this.getPos(), RatsBlockRegistry.CHEESE_CAULDRON.getDefaultState());
            world.playSound(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), SoundEvents.BLOCK_BREWING_STAND_BREW, SoundCategory.BLOCKS, 1, 1, false);
        }
    }

    public CompoundNBT writeToNBT(CompoundNBT compound) {
        compound.setInt("TicksExisted", ticksExisted);
        return super.writeToNBT(compound);
    }

    public void readFromNBT(CompoundNBT compound) {
        ticksExisted = compound.getInt("TicksExisted");
        super.readFromNBT(compound);
    }

}
