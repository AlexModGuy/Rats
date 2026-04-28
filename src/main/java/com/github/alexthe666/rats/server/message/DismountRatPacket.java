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
import net.minecraft.world.phys.Vec3;

public record DismountRatPacket(int ratId) implements CustomPacketPayload {

    public static final Type<DismountRatPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "dismount_rat"));

    public static final StreamCodec<FriendlyByteBuf, DismountRatPacket> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, DismountRatPacket::ratId,
                DismountRatPacket::new
        );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(DismountRatPacket packet, IPayloadContext context) {
        			context.enqueueWork(() -> {
        				Player player = context.player();
        				if (player != null) {
        					Entity entity = player.level().getEntity(packet.ratId());
        					if (entity instanceof TamedRat rat) {
        						rat.stopRiding();
        						Vec3 dismountPos = rat.getDismountLocationForPassenger(player);
        						rat.setPos(dismountPos.x(), dismountPos.y(), dismountPos.z());
        					}
        				}
        			});
			
    }
}
