package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.rats.client.gui.CheeseStaffScreen;
import com.github.alexthe666.rats.client.gui.PatrolStaffScreen;
import com.github.alexthe666.rats.client.gui.RadiusStaffScreen;
import com.github.alexthe666.rats.registry.RatsCapabilityRegistry;
import com.github.alexthe666.rats.server.capability.SelectedRatCapability;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record ManageRatStaffPacket(int entityId, BlockPos pos, int dirOrd, boolean clear, boolean openGUI, int staffToOpen) {

	public ManageRatStaffPacket(int entityId, BlockPos pos, int dirOrd, boolean clear, boolean openGUI) {
		this(entityId, pos, dirOrd, clear, openGUI, 0);
	}

	public static ManageRatStaffPacket decode(FriendlyByteBuf buf) {
		return new ManageRatStaffPacket(buf.readInt(), buf.readBlockPos(), buf.readInt(), buf.readBoolean(), buf.readBoolean(), buf.readInt());
	}

	public static void encode(ManageRatStaffPacket packet, FriendlyByteBuf buf) {
		buf.writeInt(packet.entityId());
		buf.writeBlockPos(packet.pos());
		buf.writeInt(packet.dirOrd());
		buf.writeBoolean(packet.clear());
		buf.writeBoolean(packet.openGUI());
		buf.writeInt(packet.staffToOpen());
	}

	public static class Handler {

		@SuppressWarnings("Convert2Lambda")
		public static void handle(ManageRatStaffPacket packet, Supplier<NetworkEvent.Context> context) {
			context.get().enqueueWork(new Runnable() {
				@Override
				public void run() {
					if (packet.clear()) {
						Minecraft.getInstance().player.getCapability(RatsCapabilityRegistry.SELECTED_RAT).ifPresent(SelectedRatCapability::clearSelectedRat);
					} else {
						Entity e = Minecraft.getInstance().player.level().getEntity(packet.entityId());
						if (e instanceof TamedRat rat) {
							if (packet.openGUI()) {
								switch (packet.staffToOpen()) {
									default ->
											Minecraft.getInstance().setScreen(new CheeseStaffScreen(rat, packet.pos(), Direction.values()[packet.dirOrd()]));
									case 1 ->
											Minecraft.getInstance().setScreen(new RadiusStaffScreen(rat, packet.pos()));
									case 2 ->
											Minecraft.getInstance().setScreen(new PatrolStaffScreen(rat, packet.pos()));
								}
							}
						}
					}
				}
			});
			context.get().setPacketHandled(true);
		}
	}
}