package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.rats.server.inventory.RatCraftingTableMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record CycleRatRecipePacket(long blockPos, boolean increase) {

	public static CycleRatRecipePacket decode(FriendlyByteBuf buf) {
		return new CycleRatRecipePacket(buf.readLong(), buf.readBoolean());
	}

	public static void encode(CycleRatRecipePacket packet, FriendlyByteBuf buf) {
		buf.writeLong(packet.blockPos());
		buf.writeBoolean(packet.increase());
	}

	public static class Handler {
		public static void handle(CycleRatRecipePacket packet, Supplier<NetworkEvent.Context> context) {
			context.get().enqueueWork(() -> {
				Player player = context.get().getSender();
				if (player != null) {
					if (player.containerMenu instanceof RatCraftingTableMenu table) {
						table.incrementRecipeIndex(packet.increase());
					}
				}
			});
			context.get().setPacketHandled(true);
		}
	}
}
