package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.citadel.server.message.PacketBufferUtils;
import com.github.alexthe666.rats.server.entity.tile.TileEntityAutoCurdler;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
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
            BlockPos pos = BlockPos.fromLong(message.blockPos);
            if (context.get() != null &&  context.get().getSender() != null && context.get().getSender().world.getTileEntity(pos) instanceof TileEntityAutoCurdler) {
                TileEntityAutoCurdler table = (TileEntityAutoCurdler) context.get().getSender().world.getTileEntity(pos);
                table.tank.setFluid(message.fluid);
            }
        }
    }

    public static MessageAutoCurdlerFluid read(PacketBuffer packetBuffer) {
        return new MessageAutoCurdlerFluid(packetBuffer.readLong(), FluidStack.loadFluidStackFromNBT(PacketBufferUtils.readTag(packetBuffer)));
    }

    public static void write(MessageAutoCurdlerFluid message, PacketBuffer packetBuffer) {
        packetBuffer.writeLong(message.blockPos);
        CompoundNBT fluidTag = new CompoundNBT();
        if (message.fluid != null) {
            message.fluid.writeToNBT(fluidTag);
        }
        PacketBufferUtils.writeTag(packetBuffer, fluidTag);
    }

}
