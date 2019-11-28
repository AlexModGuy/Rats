package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.rats.server.entity.EntityRat;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageDancingRat extends AbstractMessage<MessageDancingRat> {

    public int ratId;
    public boolean setDancing;
    public long blockPos;
    public int moves;

    public MessageDancingRat() {

    }

    public MessageDancingRat(int ratId, boolean setDancing, long blockPos, int moves) {
        this.ratId = ratId;
        this.setDancing = setDancing;
        this.blockPos = blockPos;
        this.moves = moves;
    }

    @Override
    public void onClientReceived(Minecraft client, MessageDancingRat message, PlayerEntity player, MessageContext messageContext) {
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

    @Override
    public void onServerReceived(MinecraftServer server, MessageDancingRat message, PlayerEntity player, MessageContext messageContext) {
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

    @Override
    public void fromBytes(ByteBuf buf) {
        ratId = buf.readInt();
        setDancing = buf.readBoolean();
        blockPos = buf.readLong();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(ratId);
        buf.writeBoolean(setDancing);
        buf.writeLong(blockPos);
    }

}
