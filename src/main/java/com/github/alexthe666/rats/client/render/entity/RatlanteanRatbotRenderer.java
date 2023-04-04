package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.model.entity.RatlanteanRatbotModel;
import com.github.alexthe666.rats.client.render.entity.layer.RatbotEyesLayer;
import com.github.alexthe666.rats.server.entity.ratlantis.RatlanteanRatbot;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RatlanteanRatbotRenderer extends MobRenderer<RatlanteanRatbot, RatlanteanRatbotModel<RatlanteanRatbot>> {
	private static final ResourceLocation RATBOT_TEXTURE = new ResourceLocation(RatsMod.MODID, "textures/entity/ratlantis/ratlantean_ratbot.png");

	public RatlanteanRatbotRenderer(EntityRendererProvider.Context context) {
		super(context, new RatlanteanRatbotModel<>(0.0F), 0.5F);
		this.addLayer(new RatbotEyesLayer(this));

	}

	public ResourceLocation getTextureLocation(RatlanteanRatbot entity) {
		return RATBOT_TEXTURE;
	}

	protected void scale(RatlanteanRatbot rat, PoseStack stack, float partialTickTime) {
		super.scale(rat, stack, partialTickTime);
		stack.scale(1.9F, 1.9F, 1.9F);
		if (!((double) rat.walkAnimation.speed() < 0.01D)) {
			float f1 = rat.walkAnimation.position() - rat.walkAnimation.speed() * (1.0F - partialTickTime) + 6.0F;
			float f2 = (Math.abs(f1 % 13.0F - 6.5F) - 3.25F) / 3.25F;
			stack.mulPose(Axis.ZP.rotationDegrees(6.5F * f2));
		}
	}
}