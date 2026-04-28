package com.github.alexthe666.rats.server.message;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SyncPlaguePacket(int entityId, int effectId, byte amplifier, int duration, byte flags) implements CustomPacketPayload {

    public static final Type<SyncPlaguePacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "sync_plague"));

    public static final StreamCodec<FriendlyByteBuf, SyncPlaguePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, SyncPlaguePacket::entityId,
            ByteBufCodecs.VAR_INT, SyncPlaguePacket::effectId,
            ByteBufCodecs.BYTE, SyncPlaguePacket::amplifier,
            ByteBufCodecs.VAR_INT, SyncPlaguePacket::duration,
            ByteBufCodecs.BYTE, SyncPlaguePacket::flags,
            SyncPlaguePacket::new
    );

    public SyncPlaguePacket(int entityId, MobEffectInstance effect) {
        this(
                entityId,
                BuiltInRegistries.MOB_EFFECT.getId(effect.getEffect().value()),
                (byte) (effect.getAmplifier() & 0xFF),
                Math.min(effect.getDuration(), 32767),
                packFlags(effect)
        );
    }

    private static byte packFlags(MobEffectInstance effect) {
        int f = 0;
        if (effect.isAmbient())     f |= 0b001;
        if (effect.isVisible())     f |= 0b010;
        if (effect.showIcon())      f |= 0b100;
        return (byte) f;
    }

    public boolean isEffectAmbient()  { return (this.flags & 0b001) != 0; }
    public boolean isEffectVisible()  { return (this.flags & 0b010) != 0; }
    public boolean effectShowsIcon()  { return (this.flags & 0b100) != 0; }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SyncPlaguePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().level == null) {
                return;
            }
            Entity entity = Minecraft.getInstance().level.getEntity(packet.entityId());
            if (entity instanceof LivingEntity living) {
                Holder<MobEffect> effect = BuiltInRegistries.MOB_EFFECT.getHolder(packet.effectId()).orElse(null);
                if (effect != null) {
                    if (packet.duration() == 0) {
                        living.removeEffect(effect);
                    } else {
                        MobEffectInstance instance = new MobEffectInstance(
                                effect, packet.duration(), packet.amplifier(),
                                packet.isEffectAmbient(), packet.isEffectVisible(), packet.effectShowsIcon());
                        living.forceAddEffect(instance, null);
                    }
                }
            }
        });
    }
}
