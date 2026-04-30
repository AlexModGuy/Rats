package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.inventory.RatCraftingTableMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

// JEI ghost-slot sync: client tells server which 9 stacks to display in the rat crafting table matrix.
// Sent when the user clicks the JEI "+" transfer button on a recipe.
public record SetGhostMatrixPacket(long blockPos, List<ItemStack> stacks) implements CustomPacketPayload {

    public static final Type<SetGhostMatrixPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "set_ghost_matrix"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SetGhostMatrixPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_LONG, SetGhostMatrixPacket::blockPos,
            ItemStack.OPTIONAL_STREAM_CODEC.apply(ByteBufCodecs.list(9)), SetGhostMatrixPacket::stacks,
            SetGhostMatrixPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SetGhostMatrixPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            if (player == null) return;
            if (!(player.containerMenu instanceof RatCraftingTableMenu menu)) return;
            BlockPos pos = BlockPos.of(packet.blockPos());
            if (!menu.getCraftingTable().getBlockPos().equals(pos)) return;
            // Defensive: the codec caps the list at 9 but does not require exactly 9. Reject a
            // mismatched payload outright instead of silently truncating, so a tampered client
            // can't half-fill the matrix.
            if (packet.stacks().size() != 9) return;
            var handler = menu.getCraftingTable().matrixHandler;
            for (int i = 0; i < handler.getSlots(); i++) {
                ItemStack stack = packet.stacks().get(i);
                handler.setStackInSlot(i, stack.isEmpty() ? ItemStack.EMPTY : stack.copyWithCount(1));
            }
            menu.getCraftingTable().setChanged();
        });
    }
}
