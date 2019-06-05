package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.RatUtils;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageRatCommand extends AbstractMessage<MessageRatCommand> {

    public int ratId;
    public int newCommand;

    public MessageRatCommand(){

    }

    public MessageRatCommand(int ratId, int newCommand) {
        this.ratId = ratId;
        this.newCommand = newCommand;
    }

    @Override
    public void onClientReceived(Minecraft client, MessageRatCommand message, EntityPlayer player, MessageContext messageContext) {
        Entity entity = player.world.getEntityByID(message.ratId);
        if (entity instanceof EntityRat) {
            EntityRat rat = (EntityRat) entity;
            rat.setCommand(RatUtils.wrapCommand(message.newCommand));
        }
    }

    @Override
    public void onServerReceived(MinecraftServer server, MessageRatCommand message, EntityPlayer player, MessageContext messageContext) {
        Entity entity = player.world.getEntityByID(message.ratId);
        if (entity instanceof EntityRat) {
            EntityRat rat = (EntityRat) entity;
            rat.setCommand(RatUtils.wrapCommand(message.newCommand));
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        ratId = buf.readInt();
        newCommand = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(ratId);
        buf.writeInt(newCommand);
    }

}
