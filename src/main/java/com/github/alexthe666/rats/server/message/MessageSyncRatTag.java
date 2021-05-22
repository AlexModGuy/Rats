package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.citadel.server.message.PacketBufferUtils;
import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.EntityRat;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageSyncRatTag  {

    public int ratId;
    public CompoundNBT ratTag;

    public MessageSyncRatTag(int ratId, CompoundNBT ratTag) {
        this.ratId = ratId;
        this.ratTag = ratTag;
    }

    public static class Handler {

        public Handler() {
        }

        public static void handle(MessageSyncRatTag message, Supplier<NetworkEvent.Context> context) {
            ((NetworkEvent.Context)context.get()).setPacketHandled(true);
            context.get().enqueueWork(() -> {
                PlayerEntity player = context.get().getSender();
                if(player == null){
                    player = RatsMod.PROXY.getPlayer();
                }
                if(player != null) {
                    Entity entity = player.world.getEntityByID(message.ratId);
                    if (entity instanceof EntityRat) {
                        EntityRat rat = (EntityRat) entity;
                        rat.readAdditional(message.ratTag);
                    }
                }
            });
        }
    }

    public static MessageSyncRatTag read(PacketBuffer buf) {
        return new MessageSyncRatTag(buf.readInt(), PacketBufferUtils.readTag(buf));
    }

    public static void write(MessageSyncRatTag message, PacketBuffer buf) {
        buf.writeInt(message.ratId);
        PacketBufferUtils.writeTag(buf, message.ratTag);
    }
}
