package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageAutoCurdlerFluid {

    public long blockPos;
    public FluidStack fluid;

    public MessageAutoCurdlerFluid(long blockPos, FluidStack fluid) {
        this.blockPos = blockPos;
        this.fluid = fluid;
    }

    public static class Handler {
        public Handler() {
        }

        public static void handle(MessageAutoCurdlerFluid message, Supplier<NetworkEvent.Context> context) {
            ((NetworkEvent.Context)context.get()).setPacketHandled(true);
            context.get().enqueueWork(() -> {
                RatsMod.PROXY.handlePacketAutoCurdlerFluid(message.blockPos, message.fluid);
            });
        }
    }

    public static MessageAutoCurdlerFluid read(PacketBuffer packetBuffer) {
        return new MessageAutoCurdlerFluid(packetBuffer.readLong(), FluidStack.readFromPacket(packetBuffer));
    }

    public static void write(MessageAutoCurdlerFluid message, PacketBuffer packetBuffer) {
        packetBuffer.writeLong(message.blockPos);
        message.fluid.writeToPacket(packetBuffer);
    }

}
