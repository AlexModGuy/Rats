package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.citadel.server.message.PacketBufferUtils;
import com.github.alexthe666.rats.server.entity.EntityRat;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageUpdateRatFluid {

    public int ratId;
    public FluidStack fluid;

    public MessageUpdateRatFluid() {

    }

    public MessageUpdateRatFluid(int ratId, FluidStack fluid) {
        this.ratId = ratId;
        this.fluid = fluid;
    }

    public static class Handler {
        public Handler() {
        }

        public static void handle(MessageUpdateRatFluid message, Supplier<NetworkEvent.Context> context) {
            ((NetworkEvent.Context)context.get()).setPacketHandled(true);
            PlayerEntity player = context.get().getSender();
            if(player != null) {
                Entity entity = player.world.getEntityByID(message.ratId);
                if (entity instanceof EntityRat) {
                    EntityRat rat = (EntityRat) entity;
                    rat.transportingFluid = message.fluid;

                }
            }
        }
    }

    public static MessageUpdateRatFluid read(PacketBuffer buf) {
        return new MessageUpdateRatFluid(buf.readInt(), FluidStack.loadFluidStackFromNBT(PacketBufferUtils.readTag(buf)));
    }

    public static void write(MessageUpdateRatFluid message, PacketBuffer buf) {
        buf.writeInt(message.ratId);
        CompoundNBT fluidTag = new CompoundNBT();
        if (message.fluid != null) {
            message.fluid.writeToNBT(fluidTag);
        }
        PacketBufferUtils.writeTag(buf, fluidTag);
    }
}
