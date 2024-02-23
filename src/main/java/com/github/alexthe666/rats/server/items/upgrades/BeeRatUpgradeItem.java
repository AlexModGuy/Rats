package com.github.alexthe666.rats.server.items.upgrades;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.ChangesTextureUpgrade;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.PostAttackUpgrade;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.TickRatUpgrade;
import com.github.alexthe666.rats.server.misc.RatUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class BeeRatUpgradeItem extends BaseFlightRatUpgradeItem implements ChangesTextureUpgrade, PostAttackUpgrade, TickRatUpgrade {

	public BeeRatUpgradeItem(Properties properties) {
		super(properties, 1, 3);
	}

	@Override
	public ItemStack getWing() {
		return new ItemStack(RatsItemRegistry.FEATHERY_WING.get());
	}

	@Override
	public ResourceLocation getTexture() {
		return new ResourceLocation(RatsMod.MODID, "textures/entity/rat/upgrades/bee.png");
	}

	@Override
	public boolean makesEyesGlowByDefault() {
		return false;
	}

	@Override
	public void afterHit(TamedRat rat, LivingEntity target) {
		target.addEffect(new MobEffectInstance(MobEffects.POISON, 1200, 1));
	}

	@Override
	public void tick(TamedRat rat) {
		if (rat.randomEffectCooldown == 0) {
			rat.randomEffectCooldown = 500 + rat.getRandom().nextInt(500);
			if (rat.getRandom().nextInt(3) == 0) {
				RatUtils.polinateAround(rat.level(), rat.blockPosition());
			}
		}
	}
}
