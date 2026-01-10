package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.registry.RatsCapabilityRegistry;
import com.github.alexthe666.rats.server.capability.SelectedRat;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record UpdateSelectedRatPacket(int entityId, int ratId) implements CustomPacketPayload {

	public static final Type<UpdateSelectedRatPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "update_selected_rat"));
	public static final StreamCodec<ByteBuf, UpdateSelectedRatPacket> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, UpdateSelectedRatPacket::entityId,
			ByteBufCodecs.VAR_INT, UpdateSelectedRatPacket::ratId,
			UpdateSelectedRatPacket::new
	);

	public UpdateSelectedRatPacket(Entity entity, SelectedRat cap) {
		this(entity.getId(), cap.getSelectedRat().getId());
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	@SuppressWarnings("Convert2Lambda")
	public static void handle(UpdateSelectedRatPacket packet, IPayloadContext context) {
		context.enqueueWork(new Runnable() {
			@Override
			public void run() {
				Entity entity = Minecraft.getInstance().level.getEntity(packet.entityId());
				if (entity instanceof LivingEntity) {
					SelectedRat cap = entity.getCapability(RatsCapabilityRegistry.SELECTED_RAT);
					if (cap != null) {
						cap.setSelectedRat((TamedRat) Minecraft.getInstance().level.getEntity(packet.ratId()));
					}
				}
			}
		});
	}
}







