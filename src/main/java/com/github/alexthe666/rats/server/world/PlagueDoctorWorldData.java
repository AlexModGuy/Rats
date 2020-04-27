package com.github.alexthe666.rats.server.world;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;

import java.util.UUID;

public class PlagueDoctorWorldData extends WorldSavedData {

    private static final String IDENTIFIER = "rats_travelling_plague_doctor";
    private World world;
    private int tickCounter;
    private int doctorSpawnDelay;
    private int doctorSpawnChance;
    private UUID doctorID;

    public PlagueDoctorWorldData() {
        super(IDENTIFIER);
    }

    public static PlagueDoctorWorldData get(World world) {
        if (world instanceof ServerWorld) {
            ServerWorld overworld = world.getServer().getWorld(DimensionType.OVERWORLD);

            DimensionSavedDataManager storage = overworld.getSavedData();
            PlagueDoctorWorldData data = storage.getOrCreate(PlagueDoctorWorldData::new, IDENTIFIER);
            if(data != null){
                data.world = world;
                data.markDirty();
            }
            return data;
        }
        return null;
    }

    public int getDoctorSpawnDelay() {
        return this.doctorSpawnDelay;
    }

    public void setDoctorSpawnDelay(int delay) {
        this.doctorSpawnDelay = delay;
    }

    public int getDoctorSpawnChance() {
        return this.doctorSpawnChance;
    }

    public void setDoctorSpawnChance(int chance) {
        this.doctorSpawnChance = chance;
    }

    public void setPlagueDoctorID(UUID id) {
        this.doctorID = id;
    }

    public void debug() {
    }


    public void tick() {
        ++this.tickCounter;
    }

    @Override
    public void read(CompoundNBT nbt) {
        if (nbt.contains("PlagueDoctorSpawnDelay", 99)) {
            this.doctorSpawnDelay = nbt.getInt("PlagueDoctorSpawnDelay");
        }

        if (nbt.contains("PlagueDoctorSpawnChance", 99)) {
            this.doctorSpawnChance = nbt.getInt("PlagueDoctorSpawnChance");
        }

        if (nbt.contains("PlagueDoctorId", 8)) {
            this.doctorID = UUID.fromString(nbt.getString("PlagueDoctorId"));
        }

    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putInt("PlagueDoctorSpawnDelay", this.doctorSpawnDelay);
        compound.putInt("PlagueDoctorSpawnChance", this.doctorSpawnChance);
        if (this.doctorID != null) {
            compound.putString("PlagueDoctorId", this.doctorID.toString());
        }
        return compound;
    }
}
