package com.github.alexthe666.rats.server.entity.tile;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;

public class RatsEnergyStorage implements IEnergyStorage, INBTSerializable<CompoundNBT> {

    public int energy;
    protected int capacity;
    protected int maxReceive;
    protected int maxExtract;

    public RatsEnergyStorage(int capacity) {
        this(capacity, capacity, capacity, 0);
    }

    public RatsEnergyStorage(int capacity, int maxTransfer) {
        this(capacity, maxTransfer, maxTransfer, 0);
    }

    public RatsEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        this(capacity, maxReceive, maxExtract, 0);
    }

    public RatsEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
        this.energy = Math.max(0, Math.min(capacity, energy));
    }


    public static LazyOptional<RatsEnergyStorage> create(TileEntityRatCageWheel tile, int capacity, int maxReceive, int maxExtract, int energy) {
        return LazyOptional.of(() -> new RatsEnergyStorage(capacity, maxReceive, maxExtract, energy));
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (!canReceive())
            return 0;

        int energyReceived = Math.min(capacity - energy, Math.min(this.maxReceive, maxReceive));
        if (!simulate)
            energy += energyReceived;
        return energyReceived;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if (!canExtract())
            return 0;

        int energyExtracted = Math.min(energy, Math.min(this.maxExtract, maxExtract));
        if (!simulate)
            energy -= energyExtracted;
        return energyExtracted;
    }

    @Override
    public int getEnergyStored() {
        return energy;
    }

    @Override
    public int getMaxEnergyStored() {
        return capacity;
    }

    @Override
    public boolean canExtract() {
        return this.maxExtract > 0;
    }

    @Override
    public boolean canReceive() {
        return this.maxReceive > 0;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();
        tag.putInt("EnergyStored", energy);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        energy = nbt.getInt("EnergyStored");
    }
}
