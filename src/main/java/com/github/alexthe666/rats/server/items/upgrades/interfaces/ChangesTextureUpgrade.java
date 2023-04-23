package com.github.alexthe666.rats.server.items.upgrades.interfaces;

import net.minecraft.resources.ResourceLocation;

public interface ChangesTextureUpgrade {

	/**
	 * Changes the base texture of a rat that has this upgrade. <br>
	 * If you don't want to change the entire texture but rather parts of it, or would like to add an overlay to its current texture, consider using {@link ChangesOverlayUpgrade}. <br>
	 *
	 * @return the texture to apply to your rat
	 */
	ResourceLocation getTexture();

	/**
	 * Allows a rat's eyes to render fullbright all the time. Rat's eyes normally only glow if its nighttime, so setting this to true will make that happen all the time.
	 *
	 * @return true if a rat's eyes should always render fullbright, false to keep it as the default (eyes only glow at night)
	 */
	boolean makesEyesGlowByDefault();
}
