package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import com.github.alexthe666.rats.server.items.upgrades.MobFilterUpgradeItem;
import java.util.List;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public record UpdateMobFilterPacket(InteractionHand hand, boolean whitelist, List<String> mobs) implements CustomPacketPayload {

    public static final Type<UpdateMobFilterPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "update_mob_filter"));

    public static final StreamCodec<FriendlyByteBuf, UpdateMobFilterPacket> STREAM_CODEC = StreamCodec.composite(
                NeoForgeStreamCodecs.enumCodec(InteractionHand.class), UpdateMobFilterPacket::hand,
                ByteBufCodecs.BOOL, UpdateMobFilterPacket::whitelist,
                ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()), UpdateMobFilterPacket::mobs,
                UpdateMobFilterPacket::new
        );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(UpdateMobFilterPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) context.player();
            if (player != null && player.getItemInHand(packet.hand()).getItem() instanceof MobFilterUpgradeItem) {
        					ItemStack stack = player.getItemInHand(packet.hand());
        					MobFilterUpgradeItem.setWhitelist(stack, packet.whitelist());
        					MobFilterUpgradeItem.setMobs(stack, packet.mobs());
        				}
        			});
		
    }
}
