package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.rats.server.entity.tile.TileEntityAutoCurdler;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatCraftingTable;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageAutoCurdlerFluid extends AbstractMessage<MessageAutoCurdlerFluid> {

    public long blockPos;
    public FluidStack fluid;

    public MessageAutoCurdlerFluid() {

    }


    public MessageAutoCurdlerFluid(long blockPos, FluidStack fluid) {
        this.blockPos = blockPos;
        this.fluid = fluid;
    }


    @Override
    public void onClientReceived(Minecraft client, MessageAutoCurdlerFluid message, EntityPlayer player, MessageContext messageContext) {
        BlockPos pos = BlockPos.fromLong(message.blockPos);
        if (player.world.getTileEntity(pos) instanceof TileEntityAutoCurdler) {
            TileEntityAutoCurdler table = (TileEntityAutoCurdler) player.world.getTileEntity(pos);
            table.tank.setFluid(message.fluid);
        }
    }

    @Override
    public void onServerReceived(MinecraftServer server, MessageAutoCurdlerFluid message, EntityPlayer player, MessageContext messageContext) {
        BlockPos pos = BlockPos.fromLong(message.blockPos);
        if (player.world.getTileEntity(pos) instanceof TileEntityAutoCurdler) {
            TileEntityAutoCurdler table = (TileEntityAutoCurdler) player.world.getTileEntity(pos);
            table.tank.setFluid(message.fluid);
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        blockPos = buf.readLong();
        fluid = FluidStack.loadFluidStackFromNBT(ByteBufUtils.readTag(buf));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(blockPos);
        NBTTagCompound fluidTag = new NBTTagCompound();
        if(fluid != null){
            fluid.writeToNBT(fluidTag);
        }
        ByteBufUtils.writeTag(buf, fluidTag);
    }

}
