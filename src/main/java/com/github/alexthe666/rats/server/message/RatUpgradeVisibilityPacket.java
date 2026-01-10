package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Locale;

public record RatUpgradeVisibilityPacket(int ratId, EquipmentSlot slot, boolean visible) implements CustomPacketPayload {

	public static final Type<RatUpgradeVisibilityPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "rat_upgrade_visibility"));
	public static final StreamCodec<ByteBuf, RatUpgradeVisibilityPacket> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, RatUpgradeVisibilityPacket::ratId,
			ByteBufCodecs.STRING_UTF8.map(s -> EquipmentSlot.byName(s.toLowerCase(Locale.ROOT)), e -> e.name()), RatUpgradeVisibilityPacket::slot,
			ByteBufCodecs.BOOL, RatUpgradeVisibilityPacket::visible,
			RatUpgradeVisibilityPacket::new
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(RatUpgradeVisibilityPacket packet, IPayloadContext context) {
		context.enqueueWork(() -> {
			Player player = context.player();
			if (player != null) {
				Entity entity = player.level().getEntity(packet.ratId());
				if (entity instanceof TamedRat rat) {
					rat.setSlotVisibility(packet.slot(), packet.visible());
				}
			}
		});
	}
}







