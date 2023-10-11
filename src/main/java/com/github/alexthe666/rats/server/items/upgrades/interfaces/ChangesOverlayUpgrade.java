package com.github.alexthe666.rats.server.items.upgrades.interfaces;

import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface ChangesOverlayUpgrade {

	/**
	 * Adds an overlay to a rat that has this upgrade. <br>
	 * Overlays apply over the normal rat texture, if you're looking to change the rat's texture consider using {@link ChangesTextureUpgrade}. <br>
	 * You can define what RenderType the overlay uses, allowing you to make things like fullbright layers if you wish.
	 *
	 * @param rat          the rat the overlay will apply to
	 * @param partialTicks a float that defines the time between ticks. Allows your overlay to smoothly move if it's an overlay like the charged creeper overlay.
	 * @return an overlay RenderType to render on your rat
	 */
	@Nullable
	RenderType getOverlayTexture(ItemStack stack, TamedRat rat, float partialTicks);
}
