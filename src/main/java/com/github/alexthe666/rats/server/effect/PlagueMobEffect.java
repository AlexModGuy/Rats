package com.github.alexthe666.rats.server.effect;

import com.github.alexthe666.rats.registry.RatsEffectRegistry;
import com.github.alexthe666.rats.registry.RatsSoundRegistry;
import com.github.alexthe666.rats.server.message.RatsNetworkHandler;
import com.github.alexthe666.rats.server.message.SyncPlaguePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public class PlagueMobEffect extends MobEffect {

	public PlagueMobEffect() {
		super(MobEffectCategory.HARMFUL, 0x445637);
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {

	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return duration > 0;
	}

	public List<ItemStack> getCurativeItems() {
		return new ArrayList<>();
	}

	@Override
	public void addAttributeModifiers(LivingEntity entity, AttributeMap attributes, int amplifier) {
		MobEffectInstance effect = entity.getEffect(RatsEffectRegistry.PLAGUE.get());
		if (!entity.isRemoved() && effect != null && entity.level() instanceof ServerLevel) {
			entity.playSound(RatsSoundRegistry.PLAGUE_SPREAD.get(), 1.0F, 1.0F);
			RatsNetworkHandler.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), new SyncPlaguePacket(entity.getId(), effect));
		}
		super.addAttributeModifiers(entity, attributes, amplifier);
	}

	@Override
	public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributes, int amplifier) {
		if (!entity.isRemoved() && entity.level() instanceof ServerLevel) {
			RatsNetworkHandler.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), new SyncPlaguePacket(entity.getId(), new MobEffectInstance(RatsEffectRegistry.PLAGUE.get(), 0)));
		}
		super.removeAttributeModifiers(entity, attributes, amplifier);
	}
}
