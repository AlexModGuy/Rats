package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public record SetDancingRatPacket(int ratId, boolean setDancing, long blockPos, int moves) implements CustomPacketPayload {

    public static final Type<SetDancingRatPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "set_dancing_rat"));

    public static final StreamCodec<FriendlyByteBuf, SetDancingRatPacket> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, SetDancingRatPacket::ratId,
                ByteBufCodecs.BOOL, SetDancingRatPacket::setDancing,
                ByteBufCodecs.VAR_LONG, SetDancingRatPacket::blockPos,
                ByteBufCodecs.VAR_INT, SetDancingRatPacket::moves,
                SetDancingRatPacket::new
        );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SetDancingRatPacket packet, IPayloadContext context) {
        			context.enqueueWork(() -> {
        				Player player = context.player();
        				if (player != null) {
        					Entity entity = player.level().getEntity(packet.ratId());
        					if (entity instanceof TamedRat rat) {
        						if (!rat.isDancing() && packet.setDancing()) {
        							rat.setDanceMoves(packet.moves());
        						}
        						rat.setDancing(packet.setDancing());
        						rat.jukeboxPos = BlockPos.of(packet.blockPos());
        					}
        				}
        			});
			
    }
}
