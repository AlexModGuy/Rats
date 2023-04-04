package com.github.alexthe666.rats.server.items.upgrades.interfaces;

import com.github.alexthe666.rats.client.model.entity.RatModel;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.items.upgrades.BaseFlightRatUpgradeItem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;

public interface HoldsItemUpgrade {
	/**
	 * Allows the rat with this upgrade to render an item in its hand/on its body. <br>
	 * If you're rendering an item as a decorative part on a rat instead of a held item (such as wings) make sure you set isFakeHandRenderer to true. You may run into some rendering issues otherwise.
	 * @param rat the rat that is currently has this upgrade
	 * @param model the rat's model, allows you to move the item to a specific body part if needed
	 * @param stack the renderer's PoseStack. Allows you to move, scale, and rotate the item as needed.
	 * @param buffer the MultiBufferSource used in the renderer. Allows you to define RenderTypes for your items, and is also used in Minecraft's item renderer itself.
	 * @param light the brightness the item should render at
	 * @param ageInTicks the rat's current age, in ticks. You can use this to move your item on a timer. To see this used in action, check out {@link BaseFlightRatUpgradeItem}
	 */
	void renderHeldItem(TamedRat rat, RatModel<?> model, PoseStack stack, MultiBufferSource buffer, int light, float ageInTicks);

	/**
	 * a helper method that moves the item to render on the rat's hand.
	 * @param model the model to use as reference
	 * @param left true if the item should render in the left hand, false otherwise
	 * @param stack the renderer's PoseStack
	 */
	default void translateToHand(RatModel<?> model, boolean left, PoseStack stack) {
		model.body1.translateRotate(stack);
		model.body2.translateRotate(stack);
		if (left) {
			model.leftArm.translateRotate(stack);
			model.leftHand.translateRotate(stack);
		} else {
			model.rightArm.translateRotate(stack);
			model.rightHand.translateRotate(stack);
		}
	}

	/**
	 * a helper method that moves the item to render on the rat's head.
	 * @param model the model to use as reference
	 * @param stack the renderer's PoseStack
	 */
	default void translateToHead(RatModel<?> model, PoseStack stack) {
		model.body1.translateRotate(stack);
		model.body2.translateRotate(stack);
		model.neck.translateRotate(stack);
		model.head.translateRotate(stack);
	}

	/**
	 * A method the defines whether the item being rendered should be considered a held item or not.
	 * if a rat is "holding" an item in its hands, this changes a couple of things about the rat:
	 * - the rat will always be in its sitting position. It will walk around like this as well.
	 * - the rat will not play idle animations, such as scratching or sniffing the air.
	 * - the rat will no longer hold items in its mouth.
	 * @return true if the item being rendered isn't a held item
	 */
	default boolean isFakeHandRender() {
		return false;
	}
}
