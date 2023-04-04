package com.github.alexthe666.rats.server.potion;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

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
}
