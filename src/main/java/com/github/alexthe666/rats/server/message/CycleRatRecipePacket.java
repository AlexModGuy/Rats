package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import com.github.alexthe666.rats.server.inventory.RatCraftingTableMenu;
import net.minecraft.world.entity.player.Player;

public record CycleRatRecipePacket(long blockPos, boolean increase) implements CustomPacketPayload {

    public static final Type<CycleRatRecipePacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "cycle_rat_recipe"));

    public static final StreamCodec<FriendlyByteBuf, CycleRatRecipePacket> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_LONG, CycleRatRecipePacket::blockPos,
                ByteBufCodecs.BOOL, CycleRatRecipePacket::increase,
                CycleRatRecipePacket::new
        );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(CycleRatRecipePacket packet, IPayloadContext context) {
        			context.enqueueWork(() -> {
        				Player player = context.player();
        				if (player != null) {
        					if (player.containerMenu instanceof RatCraftingTableMenu table) {
        						table.incrementRecipeIndex(packet.increase());
        					}
        				}
        			});
			
    }
}
