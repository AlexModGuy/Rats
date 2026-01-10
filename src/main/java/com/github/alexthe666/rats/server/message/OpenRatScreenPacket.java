package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.gui.RatScreen;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.inventory.RatMenu;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record OpenRatScreenPacket(int containerId, int entityId) implements CustomPacketPayload {

	public static final Type<OpenRatScreenPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "open_rat_screen"));
	public static final StreamCodec<ByteBuf, OpenRatScreenPacket> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, OpenRatScreenPacket::containerId,
			ByteBufCodecs.VAR_INT, OpenRatScreenPacket::entityId,
			OpenRatScreenPacket::new
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	@SuppressWarnings("Convert2Lambda")
	public static void handle(OpenRatScreenPacket packet, IPayloadContext context) {
		context.enqueueWork(new Runnable() {
			@Override
			public void run() {
				Entity entity = Minecraft.getInstance().level.getEntity(packet.entityId());
				if (entity instanceof TamedRat rat) {
					LocalPlayer localplayer = Minecraft.getInstance().player;
					SimpleContainer container = new SimpleContainer(6);
					RatMenu menu = new RatMenu(packet.containerId(), container, localplayer.getInventory());
					localplayer.containerMenu = menu;
					Minecraft.getInstance().setScreen(new RatScreen(menu, localplayer.getInventory(), rat));
				}
			}
		});
	}
}







