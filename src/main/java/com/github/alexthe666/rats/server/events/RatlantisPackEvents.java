package com.github.alexthe666.rats.server.events;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.message.ChangeRatlantisStatusPacket;
import com.github.alexthe666.rats.server.message.RatsNetworkHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = RatsMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RatlantisPackEvents {

	@SubscribeEvent
	public static void checkDatapackOnWorldLoad(LevelEvent.Load event) {
		if (event.getLevel() instanceof ServerLevel server && server.dimension().equals(Level.OVERWORLD) && event.getLevel().getServer() != null) {
			boolean enabled = event.getLevel().getServer().getWorldData().getDataConfiguration().dataPacks().getEnabled().contains("ratlantis");
			RatsMod.RATLANTIS_DATAPACK_ENABLED = enabled;
			RatsNetworkHandler.CHANNEL.send(PacketDistributor.ALL.noArg(), new ChangeRatlantisStatusPacket(enabled));
			RatsMod.LOGGER.debug("Overworld loaded! Ratlantis datapack is {} for this world.", enabled ? "enabled" : "disabled");
		}
	}

	@SubscribeEvent
	public static void checkDatapackOnPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
		if (!event.getEntity().level().isClientSide() && event.getEntity() instanceof ServerPlayer player) {
			if (event.getEntity().level().getServer() != null) {
				boolean enabled = event.getEntity().level().getServer().getWorldData().getDataConfiguration().dataPacks().getEnabled().contains("ratlantis");
				RatsMod.RATLANTIS_DATAPACK_ENABLED = enabled;
				RatsNetworkHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new ChangeRatlantisStatusPacket(enabled));
				RatsMod.LOGGER.debug("Ratlantis datapack flag has been set to {} for {} ({})", enabled, player.getDisplayName().getString(), player.getStringUUID());
			}
		}
		if (!event.getEntity().level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
			CompoundTag playerData = event.getEntity().getPersistentData();
			CompoundTag data = playerData.getCompound(Player.PERSISTED_NBT_TAG);
			if (data != null && !data.getBoolean("rats_griefing_warning")) {
				event.getEntity().displayClientMessage(Component.translatable("entity.rats.rat.griefing_off").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC), false);
				data.putBoolean("rats_griefing_warning", true);
				playerData.put(Player.PERSISTED_NBT_TAG, data);
			}
		}
	}
}
