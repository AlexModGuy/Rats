package com.github.alexthe666.rats.server.events;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.message.ChangeRatlantisStatusPacket;
import com.github.alexthe666.rats.server.message.RatsNetworkHandler;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = RatsMod.MODID, bus = EventBusSubscriber.Bus.GAME)
public class RatlantisPackEvents {

	@SubscribeEvent
	public static void checkDatapackOnWorldLoad(LevelEvent.Load event) {
		if (event.getLevel() instanceof ServerLevel server && server.dimension().equals(Level.OVERWORLD) && event.getLevel().getServer() != null) {
			boolean enabled = event.getLevel().getServer().getWorldData().getDataConfiguration().dataPacks().getEnabled().contains("ratlantis");
			RatsMod.RATLANTIS_DATAPACK_ENABLED = enabled;
			PacketDistributor.sendToAllPlayers(new ChangeRatlantisStatusPacket(enabled));
			RatsMod.LOGGER.debug("Overworld loaded! Ratlantis datapack is {} for this world.", enabled ? "enabled" : "disabled");
		}
	}

	@SubscribeEvent
	public static void checkDatapackOnSync(OnDatapackSyncEvent event) {
		if (event.getPlayer() != null) {
			boolean enabled = event.getPlayer().serverLevel().getServer().getWorldData().getDataConfiguration().dataPacks().getEnabled().contains("ratlantis");
			RatsMod.RATLANTIS_DATAPACK_ENABLED = enabled;
			PacketDistributor.sendToPlayer(event.getPlayer(), new ChangeRatlantisStatusPacket(enabled));
			RatsMod.LOGGER.debug("Ratlantis datapack flag has been set to {} for {} ({})", enabled, event.getPlayer().getDisplayName().getString(), event.getPlayer().getStringUUID());
		} else {
			event.getPlayerList().getPlayers().forEach(player -> {
				boolean enabled = player.serverLevel().getServer().getWorldData().getDataConfiguration().dataPacks().getEnabled().contains("ratlantis");
				RatsMod.RATLANTIS_DATAPACK_ENABLED = enabled;
				PacketDistributor.sendToPlayer(player, new ChangeRatlantisStatusPacket(enabled));
				RatsMod.LOGGER.debug("Ratlantis datapack flag has been set to {} for {} ({})", enabled, player.getDisplayName().getString(), player.getStringUUID());
			});
		}
	}
}
