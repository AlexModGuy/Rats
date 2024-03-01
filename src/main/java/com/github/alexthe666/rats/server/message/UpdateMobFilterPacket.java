package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.rats.server.items.upgrades.MobFilterUpgradeItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public record UpdateMobFilterPacket(ItemStack stack, boolean whitelist, List<String> mobs) {

	public static UpdateMobFilterPacket decode(FriendlyByteBuf buf) {
		return new UpdateMobFilterPacket(buf.readItem(), buf.readBoolean(), buf.readList(FriendlyByteBuf::readUtf));
	}

	public static void encode(UpdateMobFilterPacket packet, FriendlyByteBuf buf) {
		buf.writeItem(packet.stack());
		buf.writeBoolean(packet.whitelist());
		buf.writeCollection(packet.mobs(), FriendlyByteBuf::writeUtf);
	}

	public static class Handler {
		public static void handle(UpdateMobFilterPacket packet, Supplier<NetworkEvent.Context> context) {
			context.get().setPacketHandled(true);
			context.get().enqueueWork(() -> {
				MobFilterUpgradeItem.setWhitelist(packet.stack(), packet.whitelist());
				MobFilterUpgradeItem.setMobs(packet.stack(), packet.mobs());
			});
		}
	}
}
