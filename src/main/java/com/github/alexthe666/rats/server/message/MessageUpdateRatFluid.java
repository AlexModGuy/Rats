package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.rats.server.entity.EntityRat;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageUpdateRatFluid extends AbstractMessage<MessageUpdateRatFluid> {

    public int ratId;
    public FluidStack fluid;

    public MessageUpdateRatFluid() {

    }

    public MessageUpdateRatFluid(int ratId, FluidStack fluid) {
        this.ratId = ratId;
        this.fluid = fluid;
    }

    @Override
    public void onClientReceived(Minecraft client, MessageUpdateRatFluid message, PlayerEntity player, MessageContext messageContext) {
        Entity entity = player.world.getEntityByID(message.ratId);
        if (entity instanceof EntityRat) {
            EntityRat rat = (EntityRat) entity;
            rat.transportingFluid = message.fluid;
        }
    }

    @Override
    public void onServerReceived(MinecraftServer server, MessageUpdateRatFluid message, PlayerEntity player, MessageContext messageContext) {
        Entity entity = player.world.getEntityByID(message.ratId);
        if (entity instanceof EntityRat) {
            EntityRat rat = (EntityRat) entity;
            rat.transportingFluid = message.fluid;

        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        ratId = buf.readInt();
        fluid = FluidStack.loadFluidStackFromNBT(ByteBufUtils.readTag(buf));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(ratId);
        CompoundNBT fluidTag = new CompoundNBT();
        if (fluid != null) {
            fluid.writeToNBT(fluidTag);
        }
        ByteBufUtils.writeTag(buf, fluidTag);
    }

}
