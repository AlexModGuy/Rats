package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ChangeRatlantisStatusPacket(boolean enabled) implements CustomPacketPayload {

    public static final Type<ChangeRatlantisStatusPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "change_ratlantis_status"));

    public static final StreamCodec<FriendlyByteBuf, ChangeRatlantisStatusPacket> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.BOOL, ChangeRatlantisStatusPacket::enabled,
                ChangeRatlantisStatusPacket::new
        );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ChangeRatlantisStatusPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> RatsMod.RATLANTIS_DATAPACK_ENABLED = packet.enabled());
			
    }
}
