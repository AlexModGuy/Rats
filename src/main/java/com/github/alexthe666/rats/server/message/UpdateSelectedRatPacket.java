package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import com.github.alexthe666.rats.server.capability.SelectedRat;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public record UpdateSelectedRatPacket(int entityId, int ratId) implements CustomPacketPayload {

    public static final Type<UpdateSelectedRatPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "update_selected_rat"));

    public static final StreamCodec<FriendlyByteBuf, UpdateSelectedRatPacket> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, UpdateSelectedRatPacket::entityId,
                ByteBufCodecs.VAR_INT, UpdateSelectedRatPacket::ratId,
                UpdateSelectedRatPacket::new
        );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(UpdateSelectedRatPacket packet, IPayloadContext context) {
        			context.enqueueWork(new Runnable() {
        				@Override
        				public void run() {
        					Entity entity = Minecraft.getInstance().level.getEntity(packet.entityId());
        					if (entity instanceof LivingEntity) {
        						com.github.alexthe666.rats.server.capability.SelectedRat.setLocal((LivingEntity) entity, packet.ratId());
        					}
        				}
        			});
			
    }
}
