package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import com.github.alexthe666.rats.client.gui.CheeseStaffScreen;
import com.github.alexthe666.rats.client.gui.PatrolStaffScreen;
import com.github.alexthe666.rats.client.gui.RadiusStaffScreen;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;

public record ManageRatStaffPacket(int entityId, BlockPos pos, int dirOrd, boolean clear, boolean openGUI, int staffToOpen) implements CustomPacketPayload {

    public static final Type<ManageRatStaffPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "manage_rat_staff"));

    public static final StreamCodec<FriendlyByteBuf, ManageRatStaffPacket> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, ManageRatStaffPacket::entityId,
                BlockPos.STREAM_CODEC, ManageRatStaffPacket::pos,
                ByteBufCodecs.VAR_INT, ManageRatStaffPacket::dirOrd,
                ByteBufCodecs.BOOL, ManageRatStaffPacket::clear,
                ByteBufCodecs.BOOL, ManageRatStaffPacket::openGUI,
                ByteBufCodecs.VAR_INT, ManageRatStaffPacket::staffToOpen,
                ManageRatStaffPacket::new
        );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ManageRatStaffPacket packet, IPayloadContext context) {
        			context.enqueueWork(new Runnable() {
        				@Override
        				public void run() {
        					if (packet.clear()) {
        						com.github.alexthe666.rats.server.capability.SelectedRat.clear(Minecraft.getInstance().player);
        					} else {
        						Entity e = Minecraft.getInstance().player.level().getEntity(packet.entityId());
        						if (e instanceof TamedRat rat) {
        							if (packet.openGUI()) {
        								switch (packet.staffToOpen()) {
        									case 1 ->
        											Minecraft.getInstance().setScreen(new RadiusStaffScreen(rat, packet.pos()));
        									case 2 ->
        											Minecraft.getInstance().setScreen(new PatrolStaffScreen(rat, packet.pos()));
        									default ->
        											Minecraft.getInstance().setScreen(new CheeseStaffScreen(rat, packet.pos(), Direction.values()[packet.dirOrd()]));
        								}
        							}
        						}
        					}
        				}
        			});
			
    }
}
