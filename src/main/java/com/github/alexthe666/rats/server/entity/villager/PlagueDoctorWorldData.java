package com.github.alexthe666.rats.server.entity.villager;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class PlagueDoctorWorldData extends SavedData {
	private static final String IDENTIFIER = "rats_world_data";
	private int doctorSpawnDelay;
	private int doctorSpawnChance;
	private UUID doctorID;

	public PlagueDoctorWorldData() {
		this.setDirty();
	}

	@Nullable
	public static PlagueDoctorWorldData get(Level level) {
		if (level instanceof ServerLevel) {
			ServerLevel server = level.getServer().getLevel(level.dimension());
			DimensionDataStorage storage = server.getDataStorage();
			return storage.computeIfAbsent(PlagueDoctorWorldData::read, PlagueDoctorWorldData::new, IDENTIFIER);
		}
		return null;
	}

	public int getDoctorSpawnDelay() {
		return this.doctorSpawnDelay;
	}

	public void setDoctorSpawnDelay(int delay) {
		this.doctorSpawnDelay = delay;
		this.setDirty();
	}

	public int getDoctorSpawnChance() {
		return this.doctorSpawnChance;
	}

	public void setDoctorSpawnChance(int chance) {
		this.doctorSpawnChance = chance;
		this.setDirty();
	}

	public void setPlagueDoctorID(UUID id) {
		this.doctorID = id;
		this.setDirty();
	}

	public static PlagueDoctorWorldData read(CompoundTag tag) {
		PlagueDoctorWorldData data = new PlagueDoctorWorldData();
		if (tag.contains("PlagueDoctorSpawnDelay", 99)) {
			data.doctorSpawnDelay = tag.getInt("PlagueDoctorSpawnDelay");
		}

		if (tag.contains("PlagueDoctorSpawnChance", 99)) {
			data.doctorSpawnChance = tag.getInt("PlagueDoctorSpawnChance");
		}

		if (tag.contains("PlagueDoctorId", 8)) {
			data.doctorID = UUID.fromString(tag.getString("PlagueDoctorId"));
		}

		return data;
	}

	@Override
	public CompoundTag save(CompoundTag compound) {
		compound.putInt("PlagueDoctorSpawnDelay", this.doctorSpawnDelay);
		compound.putInt("PlagueDoctorSpawnChance", this.doctorSpawnChance);
		if (this.doctorID != null) {
			compound.putString("PlagueDoctorId", this.doctorID.toString());
		}
		return compound;
	}
}
