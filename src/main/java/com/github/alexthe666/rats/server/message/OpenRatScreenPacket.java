package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import com.github.alexthe666.rats.client.gui.RatScreen;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.inventory.RatMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;

public record OpenRatScreenPacket(int containerId, int entityId) implements CustomPacketPayload {

    public static final Type<OpenRatScreenPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "open_rat_screen"));

    public static final StreamCodec<FriendlyByteBuf, OpenRatScreenPacket> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, OpenRatScreenPacket::containerId,
                ByteBufCodecs.VAR_INT, OpenRatScreenPacket::entityId,
                OpenRatScreenPacket::new
        );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(OpenRatScreenPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Entity entity = Minecraft.getInstance().level.getEntity(packet.entityId());
            if (!(entity instanceof TamedRat rat)) return;
            LocalPlayer localplayer = Minecraft.getInstance().player;
            // Reuse the menu vanilla just opened via ServerPlayer.openMenu — its SimpleContainer was already
            // populated by ClientboundContainerSetContentPacket. Building a fresh RatMenu+SimpleContainer here
            // would throw away those synced slot contents, leaving the screen blank until the next broadcast.
            RatMenu menu;
            if (localplayer.containerMenu instanceof RatMenu existing && existing.containerId == packet.containerId()) {
                menu = existing;
            } else {
                menu = new RatMenu(packet.containerId(), new SimpleContainer(6), localplayer.getInventory());
                localplayer.containerMenu = menu;
            }
            Minecraft.getInstance().setScreen(new RatScreen(menu, localplayer.getInventory(), rat));
        });
    }
}
