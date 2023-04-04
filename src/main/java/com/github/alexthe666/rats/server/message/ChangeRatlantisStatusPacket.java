package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record ChangeRatlantisStatusPacket(boolean enabled) {

	public static ChangeRatlantisStatusPacket decode(FriendlyByteBuf buf) {
		return new ChangeRatlantisStatusPacket(buf.readBoolean());
	}

	public static void encode(ChangeRatlantisStatusPacket message, FriendlyByteBuf buf) {
		buf.writeBoolean(message.enabled());
	}

	public static class Handler {
		public static void handle(ChangeRatlantisStatusPacket message, Supplier<NetworkEvent.Context> context) {
			context.get().enqueueWork(() -> RatsMod.RATLANTIS_DATAPACK_ENABLED = message.enabled());
			context.get().setPacketHandled(true);
		}
	}

}
