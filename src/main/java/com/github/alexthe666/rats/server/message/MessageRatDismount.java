package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.rats.server.entity.EntityRat;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageRatDismount {

    public int ratId;

    public MessageRatDismount() {

    }

    public MessageRatDismount(int ratId) {
        this.ratId = ratId;
    }

    public static class Handler {
        public Handler() {
        }

        public static void handle(MessageRatDismount message, Supplier<NetworkEvent.Context> context) {
            ((NetworkEvent.Context)context.get()).setPacketHandled(true);
            PlayerEntity player = context.get().getSender();
            if(player != null) {
                Entity entity = player.world.getEntityByID(message.ratId);
                if (entity instanceof EntityRat) {
                    EntityRat rat = (EntityRat) entity;
                    rat.stopRiding();
                    rat.setPosition(player.getPosX(), player.getPosY(), player.getPosZ());
                }
            }
        }
    }

    public static MessageRatDismount read(PacketBuffer buf) {
        return new MessageRatDismount(buf.readInt());
    }

    public static void write(MessageRatDismount message, PacketBuffer buf) {
        buf.writeInt(message.ratId);
    }
}
