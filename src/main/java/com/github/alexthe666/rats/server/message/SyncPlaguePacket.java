package com.github.alexthe666.rats.server.message;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncPlaguePacket {
	private static final int FLAG_AMBIENT = 1;
	private static final int FLAG_VISIBLE = 2;
	private static final int FLAG_SHOW_ICON = 4;
	private final int entityId;
	private final byte effectId;
	private final byte effectAmplifier;
	private final int effectDurationTicks;
	private final byte flags;

	public SyncPlaguePacket(int mobId, MobEffectInstance mobEffectInstance) {
		this.entityId = mobId;
		this.effectId = (byte) (MobEffect.getId(mobEffectInstance.getEffect()) & 255);
		this.effectAmplifier = (byte) (mobEffectInstance.getAmplifier() & 255);
		this.effectDurationTicks = Math.min(mobEffectInstance.getDuration(), 32767);

		byte flags = 0;
		if (mobEffectInstance.isAmbient()) {
			flags = (byte) (flags | FLAG_AMBIENT);
		}

		if (mobEffectInstance.isVisible()) {
			flags = (byte) (flags | FLAG_VISIBLE);
		}

		if (mobEffectInstance.showIcon()) {
			flags = (byte) (flags | FLAG_SHOW_ICON);
		}

		this.flags = flags;
	}

	private SyncPlaguePacket(int entityId, byte effectId, byte effectAmplifier, int effectDurationTicks, byte flags) {
		this.entityId = entityId;
		this.effectId = effectId;
		this.effectAmplifier = effectAmplifier;
		this.effectDurationTicks = effectDurationTicks;
		this.flags = flags;
	}

	public boolean isEffectVisible() {
		return (this.flags & 2) == 2;
	}

	public boolean isEffectAmbient() {
		return (this.flags & 1) == 1;
	}

	public boolean effectShowsIcon() {
		return (this.flags & 4) == 4;
	}

	public static SyncPlaguePacket decode(FriendlyByteBuf buf) {
		return new SyncPlaguePacket(buf.readVarInt(), buf.readByte(), buf.readByte(), buf.readVarInt(), buf.readByte());
	}

	public static void encode(SyncPlaguePacket packet, FriendlyByteBuf buf) {
		buf.writeVarInt(packet.entityId);
		buf.writeByte(packet.effectId);
		buf.writeByte(packet.effectAmplifier);
		buf.writeVarInt(packet.effectDurationTicks);
		buf.writeByte(packet.flags);
	}

	public static class Handler {
		@SuppressWarnings("Convert2Lambda")
		public static void handle(SyncPlaguePacket packet, Supplier<NetworkEvent.Context> context) {
			context.get().enqueueWork(new Runnable() {
				@Override
				public void run() {
					if (Minecraft.getInstance().level == null) {
						return;
					}

					Entity entity = Minecraft.getInstance().level.getEntity(packet.entityId);
					if (entity instanceof LivingEntity living) {
						MobEffect mobeffect = MobEffect.byId(packet.effectId & 0xFF);
						if (mobeffect != null) {
							MobEffectInstance mobeffectinstance = new MobEffectInstance(mobeffect, packet.effectDurationTicks, packet.effectAmplifier, packet.isEffectAmbient(), packet.isEffectVisible(), packet.effectShowsIcon());
							living.forceAddEffect(mobeffectinstance, null);
						}
					}
				}
			});
			context.get().setPacketHandled(true);
		}
	}
}
