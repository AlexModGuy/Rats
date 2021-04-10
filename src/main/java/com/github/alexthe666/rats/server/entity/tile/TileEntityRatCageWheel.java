package com.github.alexthe666.rats.server.entity.tile;

import com.github.alexthe666.rats.server.blocks.BlockRatCage;
import com.github.alexthe666.rats.server.blocks.BlockRatCageWheel;
import com.github.alexthe666.rats.server.blocks.RatsBlockRegistry;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;

import javax.annotation.Nullable;
import java.util.Random;

public class TileEntityRatCageWheel extends TileEntityRatCageDecorated implements ITickableTileEntity {

    public int useTicks = 0;
    public float wheelRot;
    public float prevWheelRot;
    private Random random;
    private EntityRat wheeler;
    private float goalOfWheel = 0;
    private int dismountCooldown = 0;
    public LazyOptional<RatsEnergyStorage> energyStorage;

    public TileEntityRatCageWheel() {
        super(RatsTileEntityRegistry.RAT_CAGE_WHEEL);
        random = new Random();
        energyStorage = RatsEnergyStorage.create(this, 1000, 10, 10, 0);
    }

    public ItemStack getContainedItem() {
        return new ItemStack(RatsItemRegistry.RAT_WHEEL);
    }

    public void setContainedItem(ItemStack stack) {
    }

    public CompoundNBT write(CompoundNBT compound) {
        compound.putInt("UseTicks", useTicks);
        if(energyStorage.isPresent()){
            compound.putInt("StoredEnergy", energyStorage.orElse(null).energy);
        }
        compound.putInt("DismountCooldown", dismountCooldown);
        return super.write(compound);
    }

    public void read(BlockState state, CompoundNBT compound) {
        super.read(state, compound);
        useTicks = compound.getInt("UseTicks");
        dismountCooldown = compound.getInt("DismountCooldown");
        if (energyStorage.isPresent()) {
            energyStorage.orElse(null).energy = compound.getInt("StoredEnergy");
        }
    }


    @Override
    public void tick() {
        prevWheelRot = wheelRot;
        float wheelAdd = 20F;
        if (dismountCooldown > 0) {
            dismountCooldown--;
        }

        if (wheeler == null && dismountCooldown <= 0) {
            for (EntityRat rat : world.getEntitiesWithinAABB(EntityRat.class, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), (double) pos.getX() + 1, (double) pos.getY() + 1, (double) pos.getZ() + 1))) {
                if (rat.isTamed()) {
                    wheeler = rat;
                }
            }
        }
        if (wheeler != null) {
            double dist = wheeler.getDistanceSq(this.getPos().getX() + 0.5F, this.getPos().getY() + 0.5F, this.getPos().getZ() + 0.5F);
            BlockState ratWheelState = world.getBlockState(wheeler.getPosition());

            Direction facing = Direction.NORTH;
            if (this.getBlockState().getBlock() == RatsBlockRegistry.RAT_CAGE_WHEEL) {
                facing = this.getBlockState().get(BlockRatCageWheel.FACING);
            }
            wheeler.rotationYaw = facing.getHorizontalAngle();
            wheeler.renderYawOffset = wheeler.rotationYaw;
            if (useTicks > 20 && (dist > 3F || ratWheelState != this.getBlockState())) {
                wheeler.setInWheel(false);
                wheeler = null;
            } else if (wheeler != null) {
                wheeler.setPosition(this.getPos().getX() + 0.5F, this.getPos().getY() + 0.15F, this.getPos().getZ() + 0.5F);
                wheeler.setInWheel(true);
                useTicks++;
                RatsEnergyStorage storage = energyStorage.orElse(null);
                int nrg = 10;
                if (wheeler.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_SPEED)) {
                    wheelAdd = 40F;
                    nrg = 20;
                }
                if(storage != null && storage.receiveEnergy(nrg, true) != 0){
                    storage.receiveEnergy(nrg, false);
                }
                wheelRot += wheelAdd;
                BlockRatCage cageBlock = (BlockRatCage) this.getBlockState().getBlock();
                if (useTicks > 200 && useTicks % 100 == 0 && random.nextFloat() > 0.25F) {
                    for (Direction direction : Direction.values()) {
                        if (cageBlock.canFenceConnectTo(world.getBlockState(pos.offset(direction)), false, Direction.DOWN) == 1) {
                            if (wheeler != null && pos != null) {
                                wheeler.setPosition(pos.offset(direction).getX() + 0.5F, pos.offset(direction).getY() + 0.5F, pos.offset(direction).getZ() + 0.5F);
                                wheeler.setInWheel(false);
                                wheeler = null;
                                dismountCooldown = 1200 + random.nextInt(1200);
                            }
                        }
                    }
                }
            }
        } else {
            if (useTicks != 0) {
                wheelRot = wheelRot % 360F;
                goalOfWheel = (MathHelper.floor((wheelRot + 90F) / 90F) * 90F) % 360F;
                prevWheelRot = wheelRot % 360F;
            }
            useTicks = 0;
            if (Math.toRadians(wheelRot) % 90F == 0 || Math.abs(goalOfWheel - wheelRot) < 1.5F) {
                goalOfWheel = 0;
                prevWheelRot = 0;
                wheelRot = 0;
            } else if (wheelRot > goalOfWheel) {
                wheelRot -= Math.min(wheelAdd, wheelRot - goalOfWheel);
            } else if (wheelRot < goalOfWheel) {
                wheelRot += Math.min(wheelAdd, goalOfWheel - wheelRot);
            }
        }
    }

    @Override
    public <T> net.minecraftforge.common.util.LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable Direction facing) {
        if (capability == CapabilityEnergy.ENERGY)
            return energyStorage.cast();
        return super.getCapability(capability, facing);
    }

    private boolean isEnergyFacing(Direction facing) {
        BlockState state = this.getBlockState();
        return facing == null || state.getBlock() == RatsBlockRegistry.RAT_CAGE_WHEEL && (state.get(BlockRatCageWheel.FACING).rotateY() == facing || state.get(BlockRatCageWheel.FACING).rotateYCCW() == facing);
    }
}
