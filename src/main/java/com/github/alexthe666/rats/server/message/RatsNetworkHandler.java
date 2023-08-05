package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class RatsNetworkHandler {

	private static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
			new ResourceLocation(RatsMod.MODID, "channel"),
			() -> PROTOCOL_VERSION,
			PROTOCOL_VERSION::equals,
			PROTOCOL_VERSION::equals
	);

	@SuppressWarnings("UnusedAssignment")
	public static void init() {
		int id = 0;
		CHANNEL.registerMessage(id++, ChangeRatlantisStatusPacket.class, ChangeRatlantisStatusPacket::encode, ChangeRatlantisStatusPacket::decode, ChangeRatlantisStatusPacket.Handler::handle);
		CHANNEL.registerMessage(id++, ClearRatRecipePacket.class, ClearRatRecipePacket::encode, ClearRatRecipePacket::decode, ClearRatRecipePacket.Handler::handle);
		CHANNEL.registerMessage(id++, CycleRatRecipePacket.class, CycleRatRecipePacket::encode, CycleRatRecipePacket::decode, CycleRatRecipePacket.Handler::handle);
		CHANNEL.registerMessage(id++, DismountRatPacket.class, DismountRatPacket::encode, DismountRatPacket::decode, DismountRatPacket.Handler::handle);
		CHANNEL.registerMessage(id++, ManageRatStaffPacket.class, ManageRatStaffPacket::encode, ManageRatStaffPacket::decode, ManageRatStaffPacket.Handler::handle);
		CHANNEL.registerMessage(id++, OpenRatScreenPacket.class, OpenRatScreenPacket::encode, OpenRatScreenPacket::decode, OpenRatScreenPacket.Handler::handle);
		CHANNEL.registerMessage(id++, RatCommandPacket.class, RatCommandPacket::encode, RatCommandPacket::decode, RatCommandPacket.Handler::handle);
		CHANNEL.registerMessage(id++, SetDancingRatPacket.class, SetDancingRatPacket::encode, SetDancingRatPacket::decode, SetDancingRatPacket.Handler::handle);
		CHANNEL.registerMessage(id++, SyncArmSwingPacket.class, SyncArmSwingPacket::encode, SyncArmSwingPacket::decode, SyncArmSwingPacket.Handler::handle);
		CHANNEL.registerMessage(id++, SyncPlaguePacket.class, SyncPlaguePacket::encode, SyncPlaguePacket::decode, SyncPlaguePacket.Handler::handle);
		CHANNEL.registerMessage(id++, SyncRatStaffPacket.class, SyncRatStaffPacket::encode, SyncRatStaffPacket::decode, SyncRatStaffPacket.Handler::handle);
		CHANNEL.registerMessage(id++, SyncRatTagPacket.class, SyncRatTagPacket::encode, SyncRatTagPacket::decode, SyncRatTagPacket.Handler::handle);
		CHANNEL.registerMessage(id++, SyncThrownBlockPacket.class, SyncThrownBlockPacket::encode, SyncThrownBlockPacket::decode, SyncThrownBlockPacket.Handler::handle);
		CHANNEL.registerMessage(id++, UpdateCurdlerFluidPacket.class, UpdateCurdlerFluidPacket::encode, UpdateCurdlerFluidPacket::decode, UpdateCurdlerFluidPacket.Handler::handle);
		CHANNEL.registerMessage(id++, UpdateMobFilterPacket.class, UpdateMobFilterPacket::encode, UpdateMobFilterPacket::decode, UpdateMobFilterPacket.Handler::handle);
		CHANNEL.registerMessage(id++, UpdateRatFluidPacket.class, UpdateRatFluidPacket::encode, UpdateRatFluidPacket::decode, UpdateRatFluidPacket.Handler::handle);
		CHANNEL.registerMessage(id++, UpdateRatMusicPacket.class, UpdateRatMusicPacket::encode, UpdateRatMusicPacket::decode, UpdateRatMusicPacket.Handler::handle);
		CHANNEL.registerMessage(id++, UpdateSelectedRatPacket.class, UpdateSelectedRatPacket::encode, UpdateSelectedRatPacket::decode, UpdateSelectedRatPacket.Handler::handle);
	}
}
