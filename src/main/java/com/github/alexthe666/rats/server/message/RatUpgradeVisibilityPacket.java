package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.Locale;
import java.util.function.Supplier;

public record RatUpgradeVisibilityPacket(int ratId, EquipmentSlot slot, boolean visible) {

	public static RatUpgradeVisibilityPacket decode(FriendlyByteBuf buf) {
		return new RatUpgradeVisibilityPacket(buf.readInt(), EquipmentSlot.byName(buf.readUtf().toLowerCase(Locale.ROOT)), buf.readBoolean());
	}

	public static void encode(RatUpgradeVisibilityPacket packet, FriendlyByteBuf buf) {
		buf.writeInt(packet.ratId());
		buf.writeUtf(packet.slot().name());
		buf.writeBoolean(packet.visible());
	}

	public static class Handler {
		public static void handle(RatUpgradeVisibilityPacket packet, Supplier<NetworkEvent.Context> context) {
			context.get().enqueueWork(() -> {
				Player player = context.get().getSender();
				if (player != null) {
					Entity entity = player.level().getEntity(packet.ratId());
					if (entity instanceof TamedRat rat) {
						rat.setSlotVisibility(packet.slot(), packet.visible());
					}
				}
			});
			context.get().setPacketHandled(true);
		}
	}
}
