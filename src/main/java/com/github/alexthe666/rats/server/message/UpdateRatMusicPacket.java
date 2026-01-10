package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.util.RatRecordSoundInstance;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.JukeboxPlayable;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record UpdateRatMusicPacket(int id, ItemStack recordStack) implements CustomPacketPayload {

	public static final Type<UpdateRatMusicPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "update_rat_music"));
	public static final StreamCodec<RegistryFriendlyByteBuf, UpdateRatMusicPacket> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, UpdateRatMusicPacket::id,
			ItemStack.OPTIONAL_STREAM_CODEC, UpdateRatMusicPacket::recordStack,
			UpdateRatMusicPacket::new
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	@SuppressWarnings("Convert2Lambda")
	public static void handle(UpdateRatMusicPacket packet, IPayloadContext context) {
		context.enqueueWork(new Runnable() {
			@Override
			public void run() {
				if (Minecraft.getInstance().level != null) {
					Entity entity = Minecraft.getInstance().level.getEntity(packet.id());
					if (entity instanceof TamedRat rat) {
						Minecraft.getInstance().getSoundManager().queueTickingSound(new RatRecordSoundInstance(rat, packet.recordStack()));
						JukeboxPlayable playable = packet.recordStack().get(DataComponents.JUKEBOX_PLAYABLE);
						if (playable != null) {
							playable.song().unwrap(Minecraft.getInstance().level.registryAccess()).ifPresent(holder -> 
								Minecraft.getInstance().gui.setNowPlaying(holder.value().description())
							);
						}
					}
				}
			}
		});
	}
}







