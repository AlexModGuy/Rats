package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public record SyncRatTagPacket(int ratId, List<GlobalPos> nodes) implements CustomPacketPayload {

    public static final Type<SyncRatTagPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "sync_rat_tag"));

    public static final StreamCodec<FriendlyByteBuf, SyncRatTagPacket> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, SyncRatTagPacket::ratId,
                GlobalPos.STREAM_CODEC.apply(ByteBufCodecs.list()), SyncRatTagPacket::nodes,
                SyncRatTagPacket::new
        );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SyncRatTagPacket packet, IPayloadContext context) {
        			context.enqueueWork(() -> {
        				Player player = context.player();

        				if (player != null) {
        					Entity entity = player.level().getEntity(packet.ratId());
        					if (entity instanceof TamedRat rat) {
        						rat.getPatrolNodes().clear();
        						rat.getPatrolNodes().addAll(packet.nodes());
        						rat.addAdditionalSaveData(new CompoundTag());
        					}
        				}

        			});
		
    }
}
