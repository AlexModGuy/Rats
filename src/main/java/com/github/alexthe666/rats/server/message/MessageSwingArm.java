package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.rats.server.compat.TinkersCompatBridge;
import com.github.alexthe666.rats.server.entity.EntityRat;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageSwingArm extends AbstractMessage<MessageSwingArm> {

    public MessageSwingArm() {

    }

    @Override
    public void onClientReceived(Minecraft client, MessageSwingArm message, EntityPlayer player, MessageContext messageContext) {

    }

    @Override
    public void onServerReceived(MinecraftServer server, MessageSwingArm message, EntityPlayer player, MessageContext messageContext) {
        TinkersCompatBridge.onPlayerSwing(player, player.getHeldItem(EnumHand.MAIN_HAND));
    }

    @Override
    public void fromBytes(ByteBuf buf) {
    }

    @Override
    public void toBytes(ByteBuf buf) {
    }

}
