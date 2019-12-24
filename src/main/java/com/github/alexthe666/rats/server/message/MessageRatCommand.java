package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.RatUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageRatCommand {

    public int ratId;
    public int newCommand;

    public MessageRatCommand() {

    }

    public MessageRatCommand(int ratId, int newCommand) {
        this.ratId = ratId;
        this.newCommand = newCommand;
    }

    public static class Handler {
        public Handler() {
        }

        public static void handle(MessageRatCommand message, Supplier<NetworkEvent.Context> context) {
            ((NetworkEvent.Context)context.get()).setPacketHandled(true);
            PlayerEntity player = context.get().getSender();
            if(player != null) {
                Entity entity = player.world.getEntityByID(message.ratId);
                if (entity instanceof EntityRat) {
                    EntityRat rat = (EntityRat) entity;
                    rat.setCommand(RatUtils.wrapCommand(message.newCommand));
                }
            }
        }
    }

    public static MessageRatCommand read(PacketBuffer buf) {
        return new MessageRatCommand(buf.readInt(), buf.readInt());
    }

    public static void write(MessageRatCommand message, PacketBuffer buf) {
        buf.writeInt(message.ratId);
        buf.writeInt(message.newCommand);
    }

}
