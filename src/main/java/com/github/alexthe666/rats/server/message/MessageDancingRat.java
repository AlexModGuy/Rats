package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.rats.server.entity.EntityRat;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageDancingRat {

    public int ratId;
    public boolean setDancing;
    public long blockPos;
    public int moves;

    public MessageDancingRat(int ratId, boolean setDancing, long blockPos, int moves) {
        this.ratId = ratId;
        this.setDancing = setDancing;
        this.blockPos = blockPos;
        this.moves = moves;
    }

    public static class Handler {
        public Handler() {
        }

        public static void handle(MessageDancingRat message, Supplier<NetworkEvent.Context> context) {
            ((NetworkEvent.Context)context.get()).setPacketHandled(true);
            PlayerEntity player = context.get().getSender();
            if(player != null) {
                Entity entity = player.world.getEntityByID(message.ratId);
                if (entity instanceof EntityRat) {
                    EntityRat rat = (EntityRat) entity;
                    if (!rat.isDancing() && message.setDancing) {
                        rat.setDanceMoves(message.moves);
                    }
                    rat.setDancing(message.setDancing);
                    rat.jukeboxPos = BlockPos.fromLong(message.blockPos);
                }
            }
        }
    }

    public static MessageDancingRat read(PacketBuffer buf) {
        return new MessageDancingRat(buf.readInt(), buf.readBoolean(), buf.readLong(), buf.readInt());
    }

    public static void write(MessageDancingRat message, PacketBuffer buf) {
        buf.writeInt(message.ratId);
        buf.writeBoolean(message.setDancing);
        buf.writeLong(message.blockPos);
        buf.writeInt(message.moves);
    }
}
