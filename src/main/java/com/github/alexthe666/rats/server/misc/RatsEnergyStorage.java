package com.github.alexthe666.rats.server.misc;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;

public class RatsEnergyStorage implements IEnergyStorage, INBTSerializable<CompoundTag> {
	public int energy;
	protected final int capacity;
	protected final int maxReceive;
	protected final int maxExtract;

	public RatsEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
		this.capacity = capacity;
		this.maxReceive = maxReceive;
		this.maxExtract = maxExtract;
		this.energy = Math.max(0, Math.min(capacity, energy));
	}

	public static LazyOptional<RatsEnergyStorage> create(int capacity, int maxReceive, int maxExtract, int energy) {
		return LazyOptional.of(() -> new RatsEnergyStorage(capacity, maxReceive, maxExtract, energy));
	}

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		if (!this.canReceive()) {
			return 0;
		} else {
			int energyReceived = Math.min(this.capacity - this.energy, Math.min(this.maxReceive, maxReceive));
			if (!simulate) {
				this.energy += energyReceived;
			}

			return energyReceived;
		}
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		if (!this.canExtract()) {
			return 0;
		} else {
			int energyExtracted = Math.min(this.energy, Math.min(this.maxExtract, maxExtract));
			if (!simulate) {
				this.energy -= energyExtracted;
			}

			return energyExtracted;
		}
	}

	@Override
	public int getEnergyStored() {
		return this.energy;
	}

	@Override
	public int getMaxEnergyStored() {
		return this.capacity;
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
	public CompoundTag serializeNBT() {
		CompoundTag tag = new CompoundTag();
		tag.putInt("EnergyStored", this.energy);
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		this.energy = nbt.getInt("EnergyStored");
	}
}
