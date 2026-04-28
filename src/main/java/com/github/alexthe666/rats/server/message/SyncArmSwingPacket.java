package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import com.github.alexthe666.rats.server.events.ForgeEvents;
import java.util.Objects;
import net.minecraft.network.FriendlyByteBuf;

public record SyncArmSwingPacket(ItemStack stack) implements CustomPacketPayload {

    public static final Type<SyncArmSwingPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "sync_arm_swing"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncArmSwingPacket> STREAM_CODEC = StreamCodec.composite(
                ItemStack.STREAM_CODEC, SyncArmSwingPacket::stack,
                SyncArmSwingPacket::new
        );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SyncArmSwingPacket packet, IPayloadContext context) {
        			context.enqueueWork(() -> ForgeEvents.handleArmSwing(packet.stack(), Objects.requireNonNull(context.player())));
			
    }
}
