package com.github.alexthe666.rats.server.items.upgrades;

import com.github.alexthe666.rats.client.model.entity.RatModel;
import com.github.alexthe666.rats.client.render.entity.layer.PiratBoatSailLayer;
import com.github.alexthe666.rats.registry.RatlantisEntityRegistry;
import com.github.alexthe666.rats.registry.RatsSoundRegistry;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.entity.ratlantis.CheeseCannonball;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.HoldsItemUpgrade;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.TickRatUpgrade;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;

public class BuccaneerRatUpgradeItem extends BaseRatUpgradeItem implements HoldsItemUpgrade, TickRatUpgrade {
	public BuccaneerRatUpgradeItem(Properties properties) {
		super(properties, 1, 2);
	}

	@Override
	public void renderHeldItem(TamedRat rat, RatModel<?> model, PoseStack stack, MultiBufferSource buffer, int light, float ageInTicks) {
		stack.pushPose();
		model.body1.translateRotate(stack);
		stack.pushPose();
		stack.translate(0, -0.925F, 0.2F);
		stack.scale(0.5F, 0.5F, 0.5F);
		VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(PiratBoatSailLayer.TEXTURE_PIRATE_CANNON));
		VertexConsumer fireConsumer = buffer.getBuffer(RenderType.eyes(PiratBoatSailLayer.TEXTURE_PIRATE_CANNON_FIRE));
		PiratBoatSailLayer.MODEL_PIRAT_CANNON.renderToBuffer(stack, consumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		if (rat.getVisualFlag()) {
			PiratBoatSailLayer.MODEL_PIRAT_CANNON.renderToBuffer(stack, fireConsumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		}
		stack.popPose();
		stack.popPose();
	}

	@Override
	public boolean isFakeHandRender() {
		return true;
	}

	@Override
	public void tick(TamedRat rat) {
		if (rat.getVisualFlag() && rat.visualCooldown == 0) {
			rat.setVisualFlag(false);
		}
		if (rat.rangedAttackCooldown == 0 && rat.getTarget() != null) {
			rat.rangedAttackCooldown = 60;
			CheeseCannonball cannonball = new CheeseCannonball(RatlantisEntityRegistry.CHEESE_CANNONBALL.get(), rat.getLevel(), rat);
			double extraY = 0.6 + rat.getY();
			double d0 = rat.getTarget().getY() + rat.getTarget().getEyeHeight() - 1.1D;
			double d1 = rat.getTarget().getX() - rat.getX();
			double d3 = rat.getTarget().getZ() - rat.getZ();
			double d2 = d0 - extraY;
			float f = Mth.sqrt((float) (d1 * d1 + d3 * d3)) * 0.2F;
			cannonball.setPos(rat.getX(), extraY, rat.getZ());
			cannonball.shoot(d1, d2 + f, d3, 0.75F, 0.4F);
			rat.setVisualFlag(true);
			rat.visualCooldown = 4;
			rat.playSound(RatsSoundRegistry.PIRAT_SHOOT.get(), 3.0F, 2.3F / (rat.getRandom().nextFloat() * 0.4F + 0.8F));
			if (!rat.getLevel().isClientSide()) {
				rat.getLevel().addFreshEntity(cannonball);
			}
		}
	}
}
