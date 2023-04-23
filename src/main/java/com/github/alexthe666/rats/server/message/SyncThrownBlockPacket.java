package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.rats.server.entity.projectile.ThrownBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record SyncThrownBlockPacket(int blockEntityId, long blockPos) {

	public static SyncThrownBlockPacket decode(FriendlyByteBuf buf) {
		return new SyncThrownBlockPacket(buf.readInt(), buf.readLong());
	}

	public static void encode(SyncThrownBlockPacket packet, FriendlyByteBuf buf) {
		buf.writeInt(packet.blockEntityId());
		buf.writeLong(packet.blockPos());
	}

	public static class Handler {

		public static void handle(SyncThrownBlockPacket packet, Supplier<NetworkEvent.Context> context) {
			context.get().enqueueWork(() -> {
				Player player = context.get().getSender();
				if (player != null) {
					Entity entity = player.getLevel().getEntity(packet.blockEntityId());
					if (entity instanceof ThrownBlock blocc) {
						BlockPos pos = BlockPos.of(packet.blockPos());
						blocc.setHeldBlockState(player.getLevel().getBlockState(pos));
					}
				}
				context.get().setPacketHandled(true);
			});
		}
	}
}