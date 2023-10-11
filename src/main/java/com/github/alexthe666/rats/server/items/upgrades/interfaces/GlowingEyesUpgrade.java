package com.github.alexthe666.rats.server.items.upgrades.interfaces;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.ItemStack;

public interface GlowingEyesUpgrade {

	/**
	 * Adds a special eye texture to a rat that has this upgrade. <br>
	 * This works very similarly to how {@link ChangesOverlayUpgrade} works, it's just a bit more limited. <br>
	 * This works well if you want to add a special eye texture but don't want to do anything else to the texture, or you would like to have an overlay that renders differently than the eyes do.
	 *
	 * @return an eye RenderType to render on your rat
	 */
	RenderType getEyeTexture(ItemStack stack);
}
