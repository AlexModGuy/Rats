package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import com.github.alexthe666.rats.server.block.entity.RatCraftingTableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public record ClearRatRecipePacket(long blockPos) implements CustomPacketPayload {

    public static final Type<ClearRatRecipePacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "clear_rat_recipe"));

    public static final StreamCodec<FriendlyByteBuf, ClearRatRecipePacket> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_LONG, ClearRatRecipePacket::blockPos,
                ClearRatRecipePacket::new
        );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ClearRatRecipePacket packet, IPayloadContext context) {
        			context.enqueueWork(() -> {
        				Player player = context.player();
        				if (player != null) {
        					BlockPos pos = BlockPos.of(packet.blockPos());
        					if (player.level().getBlockEntity(pos) instanceof RatCraftingTableBlockEntity table) {
        						// 1.21: matrixHandler is a direct field now.
        						var handler = table.matrixHandler;
        						for (int slot = 0; slot < handler.getSlots(); slot++) {
        							handler.setStackInSlot(slot, ItemStack.EMPTY);
        						}
        						table.setChanged();
        					}
        				}
        			});
			
    }
}
