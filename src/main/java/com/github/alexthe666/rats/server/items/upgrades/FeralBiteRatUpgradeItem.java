package com.github.alexthe666.rats.server.items.upgrades;

import com.github.alexthe666.rats.registry.RatsParticleRegistry;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.events.ForgeEvents;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.PostAttackUpgrade;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.TickRatUpgrade;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class FeralBiteRatUpgradeItem extends BaseRatUpgradeItem implements PostAttackUpgrade, TickRatUpgrade {
	public FeralBiteRatUpgradeItem(Properties properties) {
		super(properties);
	}

	@Override
	public void afterHit(TamedRat rat, LivingEntity target) {
		target.hurt(rat.damageSources().mobAttack(rat), 5.0F);
		ForgeEvents.maybeAddAndSyncPlague(null, target, 600, 0);
		target.addEffect(new MobEffectInstance(MobEffects.POISON, 600));
	}

	@Override
	public void tick(TamedRat rat) {
		if (rat.getLevel().isClientSide() && rat.getRandom().nextInt(10) == 0) {
			float sitAddition = 0.125f * (rat.sitProgress / 20F);
			float radius = 0.3F - sitAddition;
			float angle = (0.01745329251F * (rat.yBodyRot));
			double extraX = (double) (radius * Mth.sin((float) (Math.PI + angle))) + rat.getX();
			double extraZ = (double) (radius * Mth.cos(angle)) + rat.getZ();
			double extraY = 0.125 + rat.getY() + sitAddition;
			float particleRand = 0.1F;
			rat.getLevel().addParticle(RatsParticleRegistry.SALIVA.get(),
					extraX + (double) (rat.getRandom().nextFloat() * particleRand * 2) - (double) particleRand,
					extraY,
					extraZ + (double) (rat.getRandom().nextFloat() * particleRand * 2) - (double) particleRand,
					0F, 0.0F, 0F);
		}
	}
}
