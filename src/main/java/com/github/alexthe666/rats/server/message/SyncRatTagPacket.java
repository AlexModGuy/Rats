package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public record SyncRatTagPacket(int ratId, List<GlobalPos> nodes) {

	public static SyncRatTagPacket decode(FriendlyByteBuf buf) {
		return new SyncRatTagPacket(buf.readInt(), buf.readList(FriendlyByteBuf::readGlobalPos));
	}

	public static void encode(SyncRatTagPacket packet, FriendlyByteBuf buf) {
		buf.writeInt(packet.ratId());
		buf.writeCollection(packet.nodes(), FriendlyByteBuf::writeGlobalPos);
	}

	public static class Handler {
		public static void handle(SyncRatTagPacket packet, Supplier<NetworkEvent.Context> context) {
			context.get().setPacketHandled(true);
			context.get().enqueueWork(() -> {
				Player player = context.get().getSender();

				if (player != null) {
					Entity entity = player.getLevel().getEntity(packet.ratId());
					if (entity instanceof TamedRat rat) {
						rat.getPatrolNodes().clear();
						rat.getPatrolNodes().addAll(packet.nodes());
						rat.addAdditionalSaveData(new CompoundTag());
					}
				}

			});
		}
	}
}
