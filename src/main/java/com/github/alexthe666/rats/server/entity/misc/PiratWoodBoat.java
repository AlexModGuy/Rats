package com.github.alexthe666.rats.server.entity.misc;

import com.github.alexthe666.rats.registry.RatlantisBlockRegistry;
import com.github.alexthe666.rats.registry.RatlantisEntityRegistry;
import com.github.alexthe666.rats.registry.RatlantisItemRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.network.NetworkHooks;

public class PiratWoodBoat extends Boat {

	private static final EntityDataAccessor<Integer> BOAT_TYPE = SynchedEntityData.defineId(PiratWoodBoat.class, EntityDataSerializers.INT);

	public PiratWoodBoat(EntityType<? extends Boat> type, Level level) {
		super(type, level);
		this.blocksBuilding = true;
	}

	public PiratWoodBoat(Level level, double x, double y, double z) {
		this(RatlantisEntityRegistry.BOAT.get(), level);
		this.setPos(x, y, z);
		this.xo = x;
		this.yo = y;
		this.zo = z;
	}

	public Type getRatsBoatType() {
		return Type.byId(this.getEntityData().get(BOAT_TYPE));
	}

	@Override
	public Item getDropItem() {
		return switch (this.getRatsBoatType()) {
			case PIRAT -> RatlantisItemRegistry.PIRAT_BOAT.get();
		};
	}

	public void setTwilightBoatType(Type boatType) {
		this.getEntityData().set(BOAT_TYPE, boatType.ordinal());
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.getEntityData().define(BOAT_TYPE, Type.PIRAT.ordinal());
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		tag.putString("Type", this.getRatsBoatType().getName());
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag tag) {
		if (tag.contains("Type", 8)) {
			this.setTwilightBoatType(Type.getTypeFromString(tag.getString("Type")));
		}
	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	public enum Type {
		PIRAT(RatlantisBlockRegistry.PIRAT_PLANKS.get(), "pirat");

		private final String name;
		private final Block block;

		Type(Block block, String name) {
			this.name = name;
			this.block = block;
		}

		public String getName() {
			return this.name;
		}

		public Block asPlank() {
			return this.block;
		}

		public String toString() {
			return this.name;
		}

		public static Type byId(int id) {
			Type[] types = values();
			if (id < 0 || id >= types.length) {
				id = 0;
			}

			return types[id];
		}

		public static Type getTypeFromString(String nameIn) {
			Type[] types = values();

			for (Type type : types) {
				if (type.getName().equals(nameIn)) {
					return type;
				}
			}

			return types[0];
		}
	}
}