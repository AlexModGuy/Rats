package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record DismountRatPacket(int ratId) {

	public static DismountRatPacket decode(FriendlyByteBuf buf) {
		return new DismountRatPacket(buf.readInt());
	}

	public static void encode(DismountRatPacket packet, FriendlyByteBuf buf) {
		buf.writeInt(packet.ratId());
	}

	public static class Handler {
		public static void handle(DismountRatPacket packet, Supplier<NetworkEvent.Context> context) {
			context.get().enqueueWork(() -> {
				Player player = context.get().getSender();
				if (player != null) {
					Entity entity = player.getLevel().getEntity(packet.ratId());
					if (entity instanceof TamedRat rat) {
						rat.stopRiding();
						Vec3 dismountPos = rat.getDismountLocationForPassenger(player);
						rat.setPos(dismountPos.x(), dismountPos.y(), dismountPos.z());
					}
				}
			});
			context.get().setPacketHandled(true);
		}
	}
}
