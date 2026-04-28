package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.util.RatRecordSoundInstance;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.network.handling.IPayloadContext;

// 1.21 note: music discs are no longer RecordItem; they're vanilla Items carrying the
// JukeboxPlayable data component. The wire form here sends Holder<Item> so the client
// can recover the disc Item; RatRecordSoundInstance pulls the JukeboxSong from the
// Item's JukeboxPlayable component.
public record UpdateRatMusicPacket(int id, Holder<Item> record) implements CustomPacketPayload {

    public static final Type<UpdateRatMusicPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "update_rat_music"));

    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateRatMusicPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, UpdateRatMusicPacket::id,
            ByteBufCodecs.holderRegistry(Registries.ITEM), UpdateRatMusicPacket::record,
            UpdateRatMusicPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(UpdateRatMusicPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().level != null) {
                Entity entity = Minecraft.getInstance().level.getEntity(packet.id());
                if (entity instanceof TamedRat rat) {
                    Minecraft.getInstance().getSoundManager().queueTickingSound(new RatRecordSoundInstance(rat, packet.record().value()));
                    Minecraft.getInstance().gui.setNowPlaying(packet.record().value().getDescription());
                }
            }
        });
    }
}
