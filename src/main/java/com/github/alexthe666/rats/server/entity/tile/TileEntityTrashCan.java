package com.github.alexthe666.rats.server.entity.tile;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.server.blocks.RatsBlockRegistry;
import com.github.alexthe666.rats.server.misc.RatsSoundRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nullable;

public class TileEntityTrashCan extends TileEntity implements ITickableTileEntity {
    public boolean opened = false;
    public float lidProgress;
    public float prevLidProgress;
    public int trashStored;
    int ticksExisted;
    net.minecraftforge.common.util.LazyOptional<? extends net.minecraftforge.items.IItemHandler> upHandler = TrashCanInventoryWrapperTop.create(this, Direction.UP);
    net.minecraftforge.common.util.LazyOptional<? extends net.minecraftforge.items.IItemHandler> downHandler = TrashCanInventoryWrapperBottom.create(this, Direction.DOWN);
    private int timeOpen = 0;

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
        if (opened) {
            timeOpen++;
            if (timeOpen > 30) {
                world.playSound(null, pos, RatsSoundRegistry.TRASH_CAN, SoundCategory.BLOCKS, 0.7F, 0.75F + world.rand.nextFloat() * 0.5F);
                opened = false;
                timeOpen = 0;
            }
        }

        if (trashStored >= 7) {
            BlockPos down = this.getPos().down();
            TileEntity tileEntity = world.getTileEntity(down);
            boolean flag = false;
            if (tileEntity != null && tileEntity.getCapability(net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.UP).isPresent()) {
                flag = true;
            }
            if(!flag){
                trashStored = 0;
                depositGarbage();
                BlockState blockstate = this.world.getBlockState(this.getPos());
                world.notifyBlockUpdate(this.getPos(), blockstate, blockstate, 3);
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
        return new SUpdateTileEntityPacket(pos, -1, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
        func_230337_a_(this.getBlockState(), packet.getNbtCompound());
    }

    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

    public CompoundNBT write(CompoundNBT compound) {
        compound.putInt("TrashStored", trashStored);
        compound.putInt("TicksExisted", ticksExisted);
        return super.write(compound);
    }

    public void func_230337_a_(BlockState state, CompoundNBT compound) {
        super.func_230337_a_(state, compound);
        ticksExisted = compound.getInt("TicksExisted");
        trashStored = compound.getInt("TrashStored");
    }

    @Override
    public <T> net.minecraftforge.common.util.LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable Direction facing) {
        if (!this.removed && facing != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (facing == Direction.DOWN)
                return downHandler.cast();
            else
                return upHandler.cast();
        }
        return super.getCapability(capability, facing);
    }
}
