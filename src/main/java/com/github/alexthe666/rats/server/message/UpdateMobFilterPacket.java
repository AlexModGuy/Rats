package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.items.upgrades.MobFilterUpgradeItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

public record UpdateMobFilterPacket(InteractionHand hand, boolean whitelist, List<String> mobs) implements CustomPacketPayload {

	public static final Type<UpdateMobFilterPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "update_mob_filter"));
	public static final StreamCodec<FriendlyByteBuf, UpdateMobFilterPacket> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.BOOL.map(b -> b ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND, h -> h == InteractionHand.MAIN_HAND), UpdateMobFilterPacket::hand,
			ByteBufCodecs.BOOL, UpdateMobFilterPacket::whitelist,
			ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()), UpdateMobFilterPacket::mobs,
			UpdateMobFilterPacket::new
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(UpdateMobFilterPacket packet, IPayloadContext context) {
		context.enqueueWork(() -> {
			if (context.player() instanceof ServerPlayer player && player.getItemInHand(packet.hand()).getItem() instanceof MobFilterUpgradeItem) {
				ItemStack stack = player.getItemInHand(packet.hand());
				MobFilterUpgradeItem.setWhitelist(stack, packet.whitelist());
				MobFilterUpgradeItem.setMobs(stack, packet.mobs());
			}
		});
	}
}







