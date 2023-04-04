package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record RatCommandPacket(int ratId, int newCommand) {

	public static RatCommandPacket decode(FriendlyByteBuf buf) {
		return new RatCommandPacket(buf.readInt(), buf.readInt());
	}

	public static void encode(RatCommandPacket packet, FriendlyByteBuf buf) {
		buf.writeInt(packet.ratId());
		buf.writeInt(packet.newCommand());
	}

	public static class Handler {
		public static void handle(RatCommandPacket packet, Supplier<NetworkEvent.Context> context) {
			context.get().enqueueWork(() -> {
				Player player = context.get().getSender();
				if (player != null) {
					Entity entity = player.getLevel().getEntity(packet.ratId());
					if (entity instanceof TamedRat rat) {
						rat.setCommandInteger(packet.newCommand());
					}
				}
			});
			context.get().setPacketHandled(true);
		}
	}
}
