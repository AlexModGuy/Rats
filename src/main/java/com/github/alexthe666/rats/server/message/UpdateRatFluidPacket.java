package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.citadel.server.message.PacketBufferUtils;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record UpdateRatFluidPacket(int ratId, FluidStack fluid) {

	public static UpdateRatFluidPacket decode(FriendlyByteBuf buf) {
		return new UpdateRatFluidPacket(buf.readInt(), FluidStack.loadFluidStackFromNBT(PacketBufferUtils.readTag(buf)));
	}

	public static void encode(UpdateRatFluidPacket packet, FriendlyByteBuf buf) {
		buf.writeInt(packet.ratId);
		CompoundTag fluidTag = new CompoundTag();
		if (packet.fluid != null) {
			packet.fluid.writeToNBT(fluidTag);
		}
		PacketBufferUtils.writeTag(buf, fluidTag);
	}

	public static class Handler {

		public static void handle(UpdateRatFluidPacket packet, Supplier<NetworkEvent.Context> context) {
			context.get().enqueueWork(() -> {
				Player player = context.get().getSender();
				if (player != null) {
					Entity entity = player.getLevel().getEntity(packet.ratId());
					if (entity instanceof TamedRat rat) {
						rat.transportingFluid = packet.fluid();

					}
				}
			});
			context.get().setPacketHandled(true);
		}
	}
}
