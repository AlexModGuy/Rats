package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.rats.server.entity.tile.TileEntityRatCraftingTable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageIncreaseRatRecipe {

    public long blockPos;
    public boolean increase;

    public MessageIncreaseRatRecipe(long blockPos, boolean increase) {
        this.blockPos = blockPos;
        this.increase = increase;
    }

    public static class Handler {
        public Handler() {
        }

        public static void handle(MessageIncreaseRatRecipe message, Supplier<NetworkEvent.Context> context) {
            ((NetworkEvent.Context)context.get()).setPacketHandled(true);
            context.get().enqueueWork(() -> {
                PlayerEntity player = context.get().getSender();
                if(player != null) {
                    BlockPos pos = BlockPos.fromLong(message.blockPos);
                    if (player.world.getTileEntity(pos) instanceof TileEntityRatCraftingTable) {
                        TileEntityRatCraftingTable table = (TileEntityRatCraftingTable) player.world.getTileEntity(pos);
                        if (message.increase) {
                            table.increaseRecipe();
                        } else {
                            table.decreaseRecipe();
                        }
                    }
                }
            });
        }
    }

    public static MessageIncreaseRatRecipe read(PacketBuffer buf) {
        return new MessageIncreaseRatRecipe(buf.readLong(), buf.readBoolean());
    }

    public static void write(MessageIncreaseRatRecipe message, PacketBuffer buf) {
        buf.writeLong(message.blockPos);
        buf.writeBoolean(message.increase);
    }
}
