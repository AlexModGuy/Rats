package com.github.alexthe666.rats.server.effect;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

public class ConfitByaldiMobEffect extends MobEffect {

	public ConfitByaldiMobEffect() {
		super(MobEffectCategory.BENEFICIAL, 0XFFDD59);
		this.addAttributeModifier(Attributes.ATTACK_SPEED, net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("rats", "confit_byaldi_attack_speed"), 1.0D, AttributeModifier.Operation.ADD_VALUE);
	}

	@Override
	public boolean applyEffectTick(LivingEntity living, int amplifier) {
		if (living.getHealth() < living.getMaxHealth()) {
			living.heal(1.0F);
		}
		if (living instanceof Player player) {
			player.getFoodData().eat(1, 0.1F);
		}
		return true;
	}

	@Override
	public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
		return duration % 40 == 0;
	}

	@Override
	public void onEffectStarted(LivingEntity living, int amplifier) {
		super.onEffectStarted(living, amplifier);
		living.setAbsorptionAmount(living.getAbsorptionAmount() + (float) (20 * (amplifier + 1)));
	}
}
