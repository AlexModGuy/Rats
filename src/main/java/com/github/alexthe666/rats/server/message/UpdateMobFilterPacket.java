package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.server.items.upgrades.MobFilterUpgradeItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public record UpdateMobFilterPacket(InteractionHand hand, boolean whitelist, List<String> mobs) {

	public static UpdateMobFilterPacket decode(FriendlyByteBuf buf) {
		return new UpdateMobFilterPacket(buf.readBoolean() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND, buf.readBoolean(), buf.readList(FriendlyByteBuf::readUtf));
	}

	public static void encode(UpdateMobFilterPacket packet, FriendlyByteBuf buf) {
		buf.writeBoolean(packet.hand() == InteractionHand.MAIN_HAND);
		buf.writeBoolean(packet.whitelist());
		buf.writeCollection(packet.mobs(), FriendlyByteBuf::writeUtf);
	}

	public static class Handler {
		public static void handle(UpdateMobFilterPacket packet, Supplier<NetworkEvent.Context> context) {
			context.get().setPacketHandled(true);
			context.get().enqueueWork(() -> {
				ServerPlayer player = context.get().getSender();
				if (player != null && player.getItemInHand(packet.hand()).getItem() instanceof MobFilterUpgradeItem) {
					ItemStack stack = player.getItemInHand(packet.hand());
					MobFilterUpgradeItem.setWhitelist(stack, packet.whitelist());
					MobFilterUpgradeItem.setMobs(stack, packet.mobs());
				}
			});
		}
	}
}
