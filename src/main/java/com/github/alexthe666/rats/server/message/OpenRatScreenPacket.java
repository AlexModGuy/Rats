package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.rats.client.gui.RatScreen;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.inventory.RatMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record OpenRatScreenPacket(int containerId, int entityId) {

	public static OpenRatScreenPacket decode(FriendlyByteBuf buf) {
		return new OpenRatScreenPacket(buf.readInt(), buf.readInt());
	}

	public static void encode(OpenRatScreenPacket packet, FriendlyByteBuf buf) {
		buf.writeInt(packet.containerId());
		buf.writeInt(packet.entityId());
	}

	public static class Handler {

		@SuppressWarnings("Convert2Lambda")
		public static void handle(OpenRatScreenPacket packet, Supplier<NetworkEvent.Context> context) {
			context.get().enqueueWork(new Runnable() {
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
			context.get().setPacketHandled(true);
		}
	}
}
