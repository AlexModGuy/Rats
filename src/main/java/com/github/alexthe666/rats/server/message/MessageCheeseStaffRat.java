package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatCraftingTable;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageCheeseStaffRat extends AbstractMessage<MessageCheeseStaffRat> {

        public int entityId;
        public boolean clear;

    public MessageCheeseStaffRat(){

        }

    public MessageCheeseStaffRat(int entityId, boolean clear) {
            this.clear = clear;
            this.entityId = entityId;
        }

        @Override
        public void onClientReceived(Minecraft client, MessageCheeseStaffRat message, EntityPlayer player, MessageContext messageContext) {
            if(message.clear){
                RatsMod.PROXY.setRefrencedRat(null);
            }else{
                Entity e = player.world.getEntityByID(message.entityId);
                if(e instanceof EntityRat){
                    RatsMod.PROXY.setRefrencedRat((EntityRat)e);
                }
            }
        }

        @Override
        public void onServerReceived(MinecraftServer server, MessageCheeseStaffRat message, EntityPlayer player, MessageContext messageContext) {
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            entityId = buf.readInt();
            clear = buf.readBoolean();
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeInt(entityId);
            buf.writeBoolean(clear);
        }

}
