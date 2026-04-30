package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.rats.RatsMod;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = RatsMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public final class RatsNetworkHandler {

    private static final String VERSION = "1.0";

    private RatsNetworkHandler() {}

    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar reg = event.registrar(RatsMod.MODID).versioned(VERSION);

        // Server -> Client
        reg.playToClient(ChangeRatlantisStatusPacket.TYPE, ChangeRatlantisStatusPacket.STREAM_CODEC, ChangeRatlantisStatusPacket::handle);
        reg.playToClient(ManageRatStaffPacket.TYPE, ManageRatStaffPacket.STREAM_CODEC, ManageRatStaffPacket::handle);
        reg.playToClient(OpenRatScreenPacket.TYPE, OpenRatScreenPacket.STREAM_CODEC, OpenRatScreenPacket::handle);
        reg.playToClient(SyncPlaguePacket.TYPE, SyncPlaguePacket.STREAM_CODEC, SyncPlaguePacket::handle);
        reg.playToClient(UpdateCurdlerFluidPacket.TYPE, UpdateCurdlerFluidPacket.STREAM_CODEC, UpdateCurdlerFluidPacket::handle);
        reg.playToClient(UpdateRatMusicPacket.TYPE, UpdateRatMusicPacket.STREAM_CODEC, UpdateRatMusicPacket::handle);
        reg.playToClient(UpdateSelectedRatPacket.TYPE, UpdateSelectedRatPacket.STREAM_CODEC, UpdateSelectedRatPacket::handle);

        // Client -> Server
        reg.playToServer(ClearRatRecipePacket.TYPE, ClearRatRecipePacket.STREAM_CODEC, ClearRatRecipePacket::handle);
        reg.playToServer(CycleRatRecipePacket.TYPE, CycleRatRecipePacket.STREAM_CODEC, CycleRatRecipePacket::handle);
        reg.playToServer(DismountRatPacket.TYPE, DismountRatPacket.STREAM_CODEC, DismountRatPacket::handle);
        reg.playToServer(RatCommandPacket.TYPE, RatCommandPacket.STREAM_CODEC, RatCommandPacket::handle);
        reg.playToServer(RatUpgradeVisibilityPacket.TYPE, RatUpgradeVisibilityPacket.STREAM_CODEC, RatUpgradeVisibilityPacket::handle);
        reg.playToServer(SetDancingRatPacket.TYPE, SetDancingRatPacket.STREAM_CODEC, SetDancingRatPacket::handle);
        reg.playToServer(SetGhostMatrixPacket.TYPE, SetGhostMatrixPacket.STREAM_CODEC, SetGhostMatrixPacket::handle);
        reg.playToServer(SyncArmSwingPacket.TYPE, SyncArmSwingPacket.STREAM_CODEC, SyncArmSwingPacket::handle);
        reg.playToServer(SyncRatStaffPacket.TYPE, SyncRatStaffPacket.STREAM_CODEC, SyncRatStaffPacket::handle);
        reg.playToServer(SyncRatTagPacket.TYPE, SyncRatTagPacket.STREAM_CODEC, SyncRatTagPacket::handle);
        reg.playToServer(SyncThrownBlockPacket.TYPE, SyncThrownBlockPacket.STREAM_CODEC, SyncThrownBlockPacket::handle);
        reg.playToServer(UpdateMobFilterPacket.TYPE, UpdateMobFilterPacket.STREAM_CODEC, UpdateMobFilterPacket::handle);
        reg.playToServer(UpdateRatFluidPacket.TYPE, UpdateRatFluidPacket.STREAM_CODEC, UpdateRatFluidPacket::handle);
    }
}
