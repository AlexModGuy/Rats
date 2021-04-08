package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.citadel.server.message.PacketBufferUtils;
import com.github.alexthe666.rats.RatsMod;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageUpdateTileSlots {

    public long blockPos;
    public CompoundNBT tag;

    public MessageUpdateTileSlots(long blockPos, CompoundNBT tag) {
        this.blockPos = blockPos;
        this.tag = tag;
    }

    public static MessageUpdateTileSlots read(PacketBuffer packetBuffer) {
        return new MessageUpdateTileSlots(packetBuffer.readLong(), PacketBufferUtils.readTag(packetBuffer));
    }

    public static void write(MessageUpdateTileSlots message, PacketBuffer packetBuffer) {
        packetBuffer.writeLong(message.blockPos);
        PacketBufferUtils.writeTag(packetBuffer, message.tag);

    }

    public static class Handler {
        public Handler() {
        }

        public static void handle(MessageUpdateTileSlots message, Supplier<NetworkEvent.Context> context) {
            context.get().setPacketHandled(true);
            context.get().enqueueWork(() -> {
                RatsMod.PROXY.handlePacketUpdateTileSlots(message.blockPos, message.tag);
            });
        }
    }

}
