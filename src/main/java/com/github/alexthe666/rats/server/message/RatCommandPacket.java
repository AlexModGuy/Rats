package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public record RatCommandPacket(int ratId, int newCommand) implements CustomPacketPayload {

    public static final Type<RatCommandPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "rat_command"));

    public static final StreamCodec<FriendlyByteBuf, RatCommandPacket> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, RatCommandPacket::ratId,
                ByteBufCodecs.VAR_INT, RatCommandPacket::newCommand,
                RatCommandPacket::new
        );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(RatCommandPacket packet, IPayloadContext context) {
        			context.enqueueWork(() -> {
        				Player player = context.player();
        				if (player != null) {
        					Entity entity = player.level().getEntity(packet.ratId());
        					if (entity instanceof TamedRat rat) {
        						rat.setCommandInteger(packet.newCommand());
        					}
        				}
        			});
			
    }
}
