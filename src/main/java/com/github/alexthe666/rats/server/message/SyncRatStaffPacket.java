package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SyncRatStaffPacket(int entityId, BlockPos pos, Direction facing, int control, int extraData) implements CustomPacketPayload {

    public static final Type<SyncRatStaffPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "sync_rat_staff"));

    public static final StreamCodec<FriendlyByteBuf, SyncRatStaffPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, SyncRatStaffPacket::entityId,
            BlockPos.STREAM_CODEC, SyncRatStaffPacket::pos,
            Direction.STREAM_CODEC, SyncRatStaffPacket::facing,
            ByteBufCodecs.VAR_INT, SyncRatStaffPacket::control,
            ByteBufCodecs.VAR_INT, SyncRatStaffPacket::extraData,
            SyncRatStaffPacket::new
    );

    // 4-arg convenience for non-radius commands (extraData defaults to 0).
    public SyncRatStaffPacket(int entityId, BlockPos pos, Direction facing, int control) {
        this(entityId, pos, facing, control, 0);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SyncRatStaffPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            if (player == null) return;
            Entity e = player.level().getEntity(packet.entityId());
            if (!(e instanceof TamedRat rat)) return;
            switch (packet.control()) {
                case 0 -> { // deposit
                    rat.setDepositPos(GlobalPos.of(player.level().dimension(), packet.pos()));
                    rat.depositFacing = packet.facing();
                }
                case 1 -> { // pickup
                    rat.setPickupPos(GlobalPos.of(player.level().dimension(), packet.pos()));
                    rat.pickupFacing = packet.facing();
                }
                case 2 -> // set homepoint
                        rat.setHomePoint(GlobalPos.of(player.level().dimension(), packet.pos()));
                case 3 -> // detach homepoint
                        rat.setHomePoint(null);
                case 4 -> // set radius home point
                        rat.setRadiusCenter(GlobalPos.of(player.level().dimension(), packet.pos()));
                case 5 -> // set radius scale
                        rat.setRadius(packet.extraData());
                case 6 -> { // reset radius
                    rat.setRadiusCenter(null);
                    rat.setRadius(RatConfig.defaultRatRadius);
                }
                case 7 -> { // reset deposit and pickup
                    rat.setPickupPos(null);
                    rat.setDepositPos(null);
                }
            }
        });
    }
}
