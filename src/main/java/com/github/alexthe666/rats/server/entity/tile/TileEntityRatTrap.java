package com.github.alexthe666.rats.server.entity.tile;

import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.RatUtils;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;

public class TileEntityRatTrap extends TileEntity implements ITickable {
    public boolean isShut;
    public float shutProgress;
    private NonNullList<ItemStack> baitStack = NonNullList.withSize(1, ItemStack.EMPTY);

    @Override
    public void update() {
        boolean prevShut = isShut;
        if (isShut && shutProgress < 6.0F) {
            shutProgress += 1.5F;
        } else if (!isShut && shutProgress > 0.0F) {
            shutProgress -= 1.5F;
        }
        if(RatUtils.isRatFood(baitStack.get(0))){
            killRats();
        }
        if(prevShut != isShut){
            for(EnumFacing facing :EnumFacing.HORIZONTALS){
                world.notifyNeighborsOfStateExcept(this.getPos(), this.blockType, facing);
            }
        }
    }

    private void killRats(){
        if(!isShut){
            float i = this.getPos().getX() + 0.5F;
            float j = this.getPos().getY() + 0.5F;
            float k = this.getPos().getZ() + 0.5F;
            float d0 = 0.65F;
            for (EntityRat rat : world.getEntitiesWithinAABB(EntityRat.class, new AxisAlignedBB((double) i - d0, (double) j - d0, (double) k - d0, (double) i + d0, (double) j + d0, (double) k + d0))) {
                if(!rat.isDead) {
                    rat.setKilledInTrap();
                    isShut = true;
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
        ItemStackHelper.saveAllItems(compound, this.baitStack);
        compound.setBoolean("IsShut", isShut);
        compound.setFloat("ShutProgress", shutProgress);
        return super.writeToNBT(compound);
    }

    public void readFromNBT(NBTTagCompound compound) {
        baitStack = NonNullList.withSize(1, ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, baitStack);
        isShut = compound.getBoolean("IsShut");
        shutProgress = compound.getFloat("ShutProgress");
        super.readFromNBT(compound);
    }

    public void setBaitStack(ItemStack stack){
        baitStack.set(0, stack);
    }

    public ItemStack getBait() {
        return baitStack.get(0);
    }

    public int calculateRedstone() {
        if(this.isShut){
            return 15;
        }
        return 0;
    }

    public void onRedstonePulse(){
        this.isShut = true;
    }


}
