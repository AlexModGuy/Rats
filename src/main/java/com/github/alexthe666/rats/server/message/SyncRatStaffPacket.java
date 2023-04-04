package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record SyncRatStaffPacket(int entityId, long pos, int facing, int control, int extraData) {

	public SyncRatStaffPacket(int entityId, BlockPos pos, Direction facing, int control) {
		this(entityId, pos.asLong(), facing.ordinal(), control, 0);
	}

	public SyncRatStaffPacket(int entityId, BlockPos pos, Direction facing, int control, int extraData) {
		this(entityId, pos.asLong(), facing.ordinal(), control, extraData);
	}

	public static SyncRatStaffPacket decode(FriendlyByteBuf buf) {
		return new SyncRatStaffPacket(buf.readInt(), buf.readLong(), buf.readInt(), buf.readInt(), buf.readInt());
	}

	public static void encode(SyncRatStaffPacket packet, FriendlyByteBuf buf) {
		buf.writeInt(packet.entityId());
		buf.writeLong(packet.pos());
		buf.writeInt(packet.facing());
		buf.writeInt(packet.control());
		buf.writeInt(packet.extraData());
	}

	public static class Handler {
		public static void handle(SyncRatStaffPacket packet, Supplier<NetworkEvent.Context> context) {
			context.get().enqueueWork(() -> {
				Player player = context.get().getSender();
				if (player != null) {
					Entity e = player.getLevel().getEntity(packet.entityId());
					if (e instanceof TamedRat rat) {
						switch (packet.control) {
							case 0 -> {//deposit
								rat.setDepositPos(GlobalPos.of(player.getLevel().dimension(), BlockPos.of(packet.pos())));
								rat.depositFacing = Direction.values()[packet.facing()];
							}
							case 1 ->//pickup
									rat.setPickupPos(GlobalPos.of(player.getLevel().dimension(), BlockPos.of(packet.pos())));
							case 2 ->//set homepoint
									rat.setHomePoint(GlobalPos.of(player.getLevel().dimension(), BlockPos.of(packet.pos())));
							case 3 ->//detach homepoint
									rat.setHomePoint(null);
							case 4 ->//set radius home point
									rat.setRadiusCenter(GlobalPos.of(player.getLevel().dimension(), BlockPos.of(packet.pos())));
							case 5 ->//set radius scale
									rat.setRadius(packet.extraData());
							case 6 -> {//reset radius
								rat.setRadiusCenter(null);
								rat.setRadius(RatConfig.defaultRatRadius);
							}
							case 7 -> { //reset deposit and pickup
								rat.setPickupPos(null);
								rat.setDepositPos(null);
							}
						}
					}
				}
			});
			context.get().setPacketHandled(true);
		}
	}
}
