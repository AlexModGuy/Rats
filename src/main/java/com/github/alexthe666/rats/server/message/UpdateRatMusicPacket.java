package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.rats.client.util.RatRecordSoundInstance;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

//TODO dont assume unsafe casting later on
public record UpdateRatMusicPacket(int id, RecordItem record) {

	public static UpdateRatMusicPacket decode(FriendlyByteBuf buf) {
		return new UpdateRatMusicPacket(buf.readInt(), (RecordItem) buf.readRegistryIdUnsafe(ForgeRegistries.ITEMS));
	}

	public static void encode(UpdateRatMusicPacket packet, FriendlyByteBuf buf) {
		buf.writeInt(packet.id());
		buf.writeRegistryIdUnsafe(ForgeRegistries.ITEMS, packet.record());
	}

	public static class Handler {
		public static void handle(UpdateRatMusicPacket packet, Supplier<NetworkEvent.Context> context) {
			context.get().enqueueWork(() -> {
				if (Minecraft.getInstance().level != null) {
					Entity entity = Minecraft.getInstance().level.getEntity(packet.id());
					if (entity instanceof TamedRat rat) {
						Minecraft.getInstance().getSoundManager().queueTickingSound(new RatRecordSoundInstance(rat, packet.record()));
						Minecraft.getInstance().gui.setNowPlaying(packet.record().getDisplayName());
					}
				}
			});
			context.get().setPacketHandled(true);
		}
	}
}
