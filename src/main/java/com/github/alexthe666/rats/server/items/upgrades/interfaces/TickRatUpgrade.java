package com.github.alexthe666.rats.server.items.upgrades.interfaces;

import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;

public interface TickRatUpgrade {
	/**
	 * Allows you to execute some code every tick while a rat has this upgrade.
	 *
	 * @param rat the rat currently holding this upgrade
	 */
	void tick(TamedRat rat);

	/**
	 * A helper method that will spawn some particles around the rat after it finishes crafting something. This is mostly used in the profession upgrades, such as chef and gemcutter.
	 *
	 * @param rat              the rat currently holding this upgrade
	 * @param finishedParticle the particle to spawn when crafting is complete
	 * @param amount           the amount of particles to spawn
	 * @param rareParticle     another particle definition, allows for some variety in particles
	 * @param chance           the chance for the above particle to appear
	 */
	default void createFinishedParticles(TamedRat rat, ParticleOptions finishedParticle, int amount, ParticleOptions rareParticle, float chance) {
		if (rat.cookingProgress > 0) {
			double d2 = rat.getRandom().nextGaussian() * 0.02D;
			double d0 = rat.getRandom().nextGaussian() * 0.02D;
			double d1 = rat.getRandom().nextGaussian() * 0.02D;
			if (rat.cookingProgress == 99) {
				for (int i = 0; i < 3; i++) {
					rat.getLevel().addParticle(ParticleTypes.HAPPY_VILLAGER, rat.getX() + (double) (rat.getRandom().nextFloat() * rat.getBbWidth() * 2.0F) - (double) rat.getBbWidth(), rat.getY() + (double) (rat.getRandom().nextFloat() * rat.getBbHeight()), rat.getZ() + (double) (rat.getRandom().nextFloat() * rat.getBbWidth() * 2.0F) - (double) rat.getBbWidth(), d0, d1, d2);
				}
			} else {
				for (int i = 0; i < amount; i++) {
					rat.getLevel().addParticle(finishedParticle, rat.getX() + (double) (rat.getRandom().nextFloat() * rat.getBbWidth() * 2.0F) - (double) rat.getBbWidth(), rat.getY() + (double) (rat.getRandom().nextFloat() * rat.getBbHeight()), rat.getZ() + (double) (rat.getRandom().nextFloat() * rat.getBbWidth() * 2.0F) - (double) rat.getBbWidth(), d0, d1, d2);
				}
				if (rat.getRandom().nextFloat() < chance) {
					rat.getLevel().addParticle(rareParticle, rat.getX() + (double) (rat.getRandom().nextFloat() * rat.getBbWidth() * 2.0F) - (double) rat.getBbWidth(), rat.getY() + (double) (rat.getRandom().nextFloat() * rat.getBbHeight()), rat.getZ() + (double) (rat.getRandom().nextFloat() * rat.getBbWidth() * 2.0F) - (double) rat.getBbWidth(), d0, d1, d2);
				}
			}
		}
	}
}
