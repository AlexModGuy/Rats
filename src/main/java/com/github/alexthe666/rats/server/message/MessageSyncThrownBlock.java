package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.rats.server.entity.EntityThrownBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageSyncThrownBlock {

    public int blockEntityId;
    public long blockPos;

    public MessageSyncThrownBlock() {

    }

    public MessageSyncThrownBlock(int blockEntityId, long blockPos) {
        this.blockEntityId = blockEntityId;
        this.blockPos = blockPos;
    }

    public static class Handler {
        public Handler() {
        }

        public static void handle(MessageSyncThrownBlock message, Supplier<NetworkEvent.Context> context) {
            ((NetworkEvent.Context)context.get()).setPacketHandled(true);
            PlayerEntity player = context.get().getSender();
            if(player != null) {
                Entity entity = player.world.getEntityByID(message.blockEntityId);
                if (entity instanceof EntityThrownBlock) {
                    BlockPos pos = BlockPos.fromLong(message.blockPos);
                    ((EntityThrownBlock) entity).fallTile = player.world.getBlockState(pos);
                }
            }
        }
    }


    public static MessageSyncThrownBlock read(PacketBuffer buf) {
        return new MessageSyncThrownBlock(buf.readInt(), buf.readLong());
    }

    public static void write(MessageSyncThrownBlock message, PacketBuffer buf) {
        buf.writeInt(message.blockEntityId);
        buf.writeLong(message.blockPos);
    }

}