package com.github.alexthe666.rats.server.items.upgrades;

import com.github.alexthe666.rats.client.model.entity.RatModel;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.HoldsItemUpgrade;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

public class PlatterRatUpgradeItem extends BaseRatUpgradeItem implements HoldsItemUpgrade {

	public PlatterRatUpgradeItem(Properties properties) {
		super(properties, 0, 1);
	}

	@Override
	public boolean playIdleAnimation(TamedRat rat) {
		return false;
	}

	@Override
	public void renderHeldItem(TamedRat rat, RatModel<?> model, PoseStack stack, MultiBufferSource buffer, int light, float ageInTicks) {
		if (model.young) {
			stack.translate(0.0F, 0.625F, 0.0F);
			stack.mulPose(Axis.XP.rotationDegrees(20));
			stack.scale(0.5F, 0.5F, 0.5F);
		}
		this.translateToHand(model, true, stack);
		stack.mulPose(Axis.ZP.rotationDegrees(190.0F));
		stack.mulPose(Axis.YP.rotationDegrees(180.0F));
		stack.mulPose(Axis.XN.rotationDegrees(70.0F));

		stack.translate(-0.155F, -0.225F, 0.2F);
		stack.scale(2F, 2F, 2F);
		Minecraft.getInstance().getItemRenderer().renderStatic(new ItemStack(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE), ItemDisplayContext.GROUND, light, OverlayTexture.NO_OVERLAY, stack, buffer, null, rat.getId());
	}
}
