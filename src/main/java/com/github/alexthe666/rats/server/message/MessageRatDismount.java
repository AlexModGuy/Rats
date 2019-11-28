package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.rats.server.entity.EntityRat;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageRatDismount extends AbstractMessage<MessageRatDismount> {

    public int ratId;

    public MessageRatDismount() {

    }

    public MessageRatDismount(int ratId) {
        this.ratId = ratId;
    }

    @Override
    public void onClientReceived(Minecraft client, MessageRatDismount message, PlayerEntity player, MessageContext messageContext) {
        Entity entity = player.world.getEntityByID(message.ratId);
        if (entity instanceof EntityRat) {
            EntityRat rat = (EntityRat) entity;
            rat.setPosition(player.posX, player.posY, player.posZ);
        }
    }

    @Override
    public void onServerReceived(MinecraftServer server, MessageRatDismount message, PlayerEntity player, MessageContext messageContext) {
        Entity entity = player.world.getEntityByID(message.ratId);
        if (entity instanceof EntityRat) {
            EntityRat rat = (EntityRat) entity;
            rat.dismountRidingEntity();
            rat.setPosition(player.posX, player.posY, player.posZ);
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        ratId = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(ratId);
    }

}
