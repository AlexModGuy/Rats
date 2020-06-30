package com.github.alexthe666.rats.server.entity.tile;

import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.RatUtils;
import net.minecraft.block.BlockState;
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
import net.minecraft.util.math.AxisAlignedBB;

public class TileEntityRatTrap extends TileEntity implements ITickableTileEntity {
    public boolean isShut;
    public float shutProgress;
    private NonNullList<ItemStack> baitStack = NonNullList.withSize(1, ItemStack.EMPTY);

    public TileEntityRatTrap() {
        super(RatsTileEntityRegistry.RAT_TRAP);
    }

    @Override
    public void tick() {
        boolean prevShut = isShut;
        if (isShut && shutProgress < 6.0F) {
            shutProgress += 1.5F;
        } else if (!isShut && shutProgress > 0.0F) {
            shutProgress -= 1.5F;
        }
        if (RatUtils.isRatFood(baitStack.get(0))) {
            killRats();
        }
    }

    private void killRats() {
        if (!isShut) {
            float i = this.getPos().getX() + 0.5F;
            float j = this.getPos().getY() + 0.5F;
            float k = this.getPos().getZ() + 0.5F;
            float d0 = 0.65F;
            for (EntityRat rat : world.getEntitiesWithinAABB(EntityRat.class, new AxisAlignedBB((double) i - d0, (double) j - d0, (double) k - d0, (double) i + d0, (double) j + d0, (double) k + d0))) {
                isShut = true;
                if (!rat.isDead()) {
                    rat.setKilledInTrap();
                    world.playSound(null, this.getPos(), SoundEvents.BLOCK_IRON_TRAPDOOR_CLOSE, SoundCategory.BLOCKS, 1F, 1F);
                    this.decreaseBait();
                }
            }
        }
    }

    private void decreaseBait() {
        baitStack.get(0).shrink(1);
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT tag = new CompoundNBT();
        this.write(tag);
        return new SUpdateTileEntityPacket(pos, 1, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
        func_230337_a_(this.getBlockState(), packet.getNbtCompound());
    }

    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

    public CompoundNBT write(CompoundNBT compound) {
        ItemStackHelper.saveAllItems(compound, this.baitStack);
        compound.putBoolean("IsShut", isShut);
        compound.putFloat("ShutProgress", shutProgress);
        return super.write(compound);
    }


    public void func_230337_a_(BlockState state, CompoundNBT compound) {
        super.func_230337_a_(state, compound);
        baitStack = NonNullList.withSize(1, ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, baitStack);
        isShut = compound.getBoolean("IsShut");
        shutProgress = compound.getFloat("ShutProgress");
    }

    public void setBaitStack(ItemStack stack) {
        baitStack.set(0, stack);
    }

    public ItemStack getBait() {
        return baitStack.get(0);
    }

    public int calculateRedstone() {
        if (this.isShut) {
            return 15;
        }
        return 0;
    }

    public void onRedstonePulse() {
        this.isShut = true;
    }


}
