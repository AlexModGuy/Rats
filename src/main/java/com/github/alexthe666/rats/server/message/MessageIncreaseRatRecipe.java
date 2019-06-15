package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.RatUtils;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatCraftingTable;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageIncreaseRatRecipe extends AbstractMessage<MessageIncreaseRatRecipe> {

    public long blockPos;
    public boolean increase;

    public MessageIncreaseRatRecipe(){

    }

    public MessageIncreaseRatRecipe(long blockPos, boolean increase) {
        this.blockPos = blockPos;
        this.increase = increase;
    }

    @Override
    public void onClientReceived(Minecraft client, MessageIncreaseRatRecipe message, EntityPlayer player, MessageContext messageContext) {
        BlockPos pos = BlockPos.fromLong(message.blockPos);
    }

    @Override
    public void onServerReceived(MinecraftServer server, MessageIncreaseRatRecipe message, EntityPlayer player, MessageContext messageContext) {
        BlockPos pos = BlockPos.fromLong(message.blockPos);
        if(player.world.getTileEntity(pos) instanceof TileEntityRatCraftingTable){
            TileEntityRatCraftingTable table = (TileEntityRatCraftingTable)player.world.getTileEntity(pos);
            if(message.increase){
                table.increaseRecipe();
            }else{
                table.decreaseRecipe();
            }
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        blockPos = buf.readLong();
        increase = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(blockPos);
        buf.writeBoolean(increase);
    }

}
