package com.github.alexthe666.rats.server.entity.tile;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.server.blocks.RatsBlockRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;

public class TileEntityMilkCauldron extends TileEntity implements ITickableTileEntity {
    int ticksExisted;

    public TileEntityMilkCauldron() {
        super(RatsTileEntityRegistry.MILK_CAULDRON);
    }

    @Override
    public void tick() {
        ticksExisted++;
        if (RatConfig.cheesemaking && ticksExisted >= RatConfig.milkCauldronTime) {
            world.setBlockState(this.getPos(), RatsBlockRegistry.CHEESE_CAULDRON.getDefaultState());
            world.playSound(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), SoundEvents.BLOCK_BREWING_STAND_BREW, SoundCategory.BLOCKS, 1, 1, false);
        }
    }

    public CompoundNBT write(CompoundNBT compound) {
        compound.putInt("TicksExisted", ticksExisted);
        return super.write(compound);
    }

    public void read(BlockState state, CompoundNBT compound) {
        super.read(state, compound);
        ticksExisted = compound.getInt("TicksExisted");
    }

}
