package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.rats.server.block.entity.AutoCurdlerBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record UpdateCurdlerFluidPacket(long blockPos, FluidStack fluid) {

	public static UpdateCurdlerFluidPacket decode(FriendlyByteBuf buf) {
		return new UpdateCurdlerFluidPacket(buf.readLong(), FluidStack.readFromPacket(buf));
	}

	public static void encode(UpdateCurdlerFluidPacket packet, FriendlyByteBuf buf) {
		buf.writeLong(packet.blockPos());
		packet.fluid().writeToPacket(buf);
	}

	public static class Handler {

		@SuppressWarnings("Convert2Lambda")
		public static void handle(UpdateCurdlerFluidPacket packet, Supplier<NetworkEvent.Context> context) {
			context.get().enqueueWork(new Runnable() {
				@Override
				public void run() {
					BlockPos pos = BlockPos.of(packet.blockPos());
					Level level = Minecraft.getInstance().level;
					if (level != null && level.getBlockEntity(pos) instanceof AutoCurdlerBlockEntity curdler) {
						curdler.getTank().setFluid(packet.fluid());
					}
				}
			});
			context.get().setPacketHandled(true);
		}
	}

}
