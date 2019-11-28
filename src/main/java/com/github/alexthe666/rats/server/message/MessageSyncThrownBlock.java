package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.rats.server.entity.EntityThrownBlock;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageSyncThrownBlock extends AbstractMessage<MessageSyncThrownBlock> {

    public int blockEntityId;
    public long blockPos;

    public MessageSyncThrownBlock() {

    }

    public MessageSyncThrownBlock(int blockEntityId, long blockPos) {
        this.blockEntityId = blockEntityId;
        this.blockPos = blockPos;
    }

    @Override
    public void onClientReceived(Minecraft client, MessageSyncThrownBlock message, PlayerEntity player, MessageContext messageContext) {
        Entity entity = player.world.getEntityByID(message.blockEntityId);
        if (entity instanceof EntityThrownBlock) {
            BlockPos pos = BlockPos.fromLong(message.blockPos);
            ((EntityThrownBlock) entity).fallTile = player.world.getBlockState(pos);
        }
    }

    @Override
    public void onServerReceived(MinecraftServer server, MessageSyncThrownBlock message, PlayerEntity player, MessageContext messageContext) {
        Entity entity = player.world.getEntityByID(message.blockEntityId);
        if (entity instanceof EntityThrownBlock) {
            BlockPos pos = BlockPos.fromLong(message.blockPos);
            ((EntityThrownBlock) entity).fallTile = player.world.getBlockState(pos);
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        blockEntityId = buf.readInt();
        blockPos = buf.readLong();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(blockEntityId);
        buf.writeLong(blockPos);
    }
}