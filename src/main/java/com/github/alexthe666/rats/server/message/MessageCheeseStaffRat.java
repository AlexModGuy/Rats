package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageCheeseStaffRat {

    public int entityId;
    public boolean clear;

    public MessageCheeseStaffRat(int entityId, boolean clear) {
        this.clear = clear;
        this.entityId = entityId;
    }

    public static class Handler {
        public Handler() {
        }

        public static void handle(MessageCheeseStaffRat message, Supplier<NetworkEvent.Context> context) {
            ((NetworkEvent.Context)context.get()).setPacketHandled(true);
            if (message.clear) {
                RatsMod.PROXY.setRefrencedRat(null);
            } else {
                RatsMod.PROXY.handlePacketCheeseStaffRat(message.entityId, message.clear);
                RatsMod.PROXY.openCheeseStaffGui();
            }
        }
    }

    public static MessageCheeseStaffRat read(PacketBuffer packetBuffer) {
        return new MessageCheeseStaffRat(packetBuffer.readInt(), packetBuffer.readBoolean());
    }

    public static void write(MessageCheeseStaffRat message, PacketBuffer buf) {
        buf.writeInt(message.entityId);
        buf.writeBoolean(message.clear);
    }

}
