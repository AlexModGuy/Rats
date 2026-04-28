package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import com.github.alexthe666.rats.server.block.entity.AutoCurdlerBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;

public record UpdateCurdlerFluidPacket(long blockPos, FluidStack fluid) implements CustomPacketPayload {

    public static final Type<UpdateCurdlerFluidPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "update_curdler_fluid"));

    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateCurdlerFluidPacket> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_LONG, UpdateCurdlerFluidPacket::blockPos,
                FluidStack.STREAM_CODEC, UpdateCurdlerFluidPacket::fluid,
                UpdateCurdlerFluidPacket::new
        );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(UpdateCurdlerFluidPacket packet, IPayloadContext context) {
        			context.enqueueWork(new Runnable() {
        				@Override
        				public void run() {
        					BlockPos pos = BlockPos.of(packet.blockPos());
        					Level level = Minecraft.getInstance().level;
        					if (level != null && level.getBlockEntity(pos) instanceof AutoCurdlerBlockEntity curdler) {
        						curdler.getTank().setFluid(packet.fluid());
        					}
        				}
        			});
			
    }
}
