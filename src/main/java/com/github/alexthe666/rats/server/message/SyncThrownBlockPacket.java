package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.projectile.ThrownBlock;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SyncThrownBlockPacket(int blockEntityId, long blockPos) implements CustomPacketPayload {

	public static final Type<SyncThrownBlockPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "sync_thrown_block"));
	public static final StreamCodec<ByteBuf, SyncThrownBlockPacket> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, SyncThrownBlockPacket::blockEntityId,
			ByteBufCodecs.VAR_LONG, SyncThrownBlockPacket::blockPos,
			SyncThrownBlockPacket::new
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(SyncThrownBlockPacket packet, IPayloadContext context) {
		context.enqueueWork(() -> {
			Player player = context.player();
			if (player != null) {
				Entity entity = player.level().getEntity(packet.blockEntityId());
				if (entity instanceof ThrownBlock blocc) {
					BlockPos pos = BlockPos.of(packet.blockPos());
					blocc.setHeldBlockState(player.level().getBlockState(pos));
				}
			}
		});
	}
}






