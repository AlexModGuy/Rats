package com.github.alexthe666.rats.client.render.entity.layer;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.model.entity.RatlanteanRatbotModel;
import com.github.alexthe666.rats.server.entity.ratlantis.RatlanteanRatbot;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class RatbotEyesLayer extends RenderLayer<RatlanteanRatbot, RatlanteanRatbotModel<RatlanteanRatbot>> {
	private static final RenderType TEXTURE_EYES_0 = RenderType.eyes(new ResourceLocation(RatsMod.MODID, "textures/entity/ratlantis/ratlantean_ratbot_eyes_0.png"));
	private static final RenderType TEXTURE_EYES_1 = RenderType.eyes(new ResourceLocation(RatsMod.MODID, "textures/entity/ratlantis/ratlantean_ratbot_eyes_1.png"));
	private static final RenderType TEXTURE_EYES_2 = RenderType.eyes(new ResourceLocation(RatsMod.MODID, "textures/entity/ratlantis/ratlantean_ratbot_eyes_2.png"));
	private static final RenderType TEXTURE_EYES_3 = RenderType.eyes(new ResourceLocation(RatsMod.MODID, "textures/entity/ratlantis/ratlantean_ratbot_eyes_3.png"));

	public RatbotEyesLayer(RenderLayerParent<RatlanteanRatbot, RatlanteanRatbotModel<RatlanteanRatbot>> parent) {
		super(parent);
	}

	@Override
	public void render(PoseStack stack, MultiBufferSource buffer, int light, RatlanteanRatbot entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		VertexConsumer consumer = buffer.getBuffer(this.getTextureForTick(entity.tickCount * 3));
		this.getParentModel().renderToBuffer(stack, consumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
	}

	private RenderType getTextureForTick(int tickCount) {
		int tickCap = tickCount % 40;
		if (tickCap > 19) {
			tickCap = tickCap - 20;
			if (tickCap > 15) {
				return TEXTURE_EYES_0;
			} else if (tickCap > 10) {
				return TEXTURE_EYES_1;
			} else if (tickCap > 5) {
				return TEXTURE_EYES_2;
			} else {
				return TEXTURE_EYES_3;
			}
		} else {
			if (tickCap > 15) {
				return TEXTURE_EYES_3;
			} else if (tickCap > 10) {
				return TEXTURE_EYES_2;
			} else if (tickCap > 5) {
				return TEXTURE_EYES_1;
			} else {
				return TEXTURE_EYES_0;
			}
		}
	}
}
