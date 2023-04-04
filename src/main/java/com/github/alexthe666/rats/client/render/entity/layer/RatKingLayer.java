package com.github.alexthe666.rats.client.render.entity.layer;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.model.EmptyModel;
import com.github.alexthe666.rats.client.model.entity.RatKingModel;
import com.github.alexthe666.rats.server.entity.RatKing;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LightLayer;

public class RatKingLayer extends RenderLayer<RatKing, EmptyModel<RatKing>> {
	private static final RenderType TEXTURE_EYES = RenderType.eyes(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/rat_eye_glow.png"));
	private static final RenderType TEXTURE_0 = RenderType.entityCutoutNoCull(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/rat_blue.png"));
	private static final RenderType TEXTURE_1 = RenderType.entityCutoutNoCull(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/rat_black.png"));
	private static final RenderType TEXTURE_2 = RenderType.entityCutoutNoCull(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/rat_brown.png"));
	private static final RenderType TEXTURE_3 = RenderType.entityCutoutNoCull(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/rat_green.png"));
	private static final RatKingModel<RatKing> RAT_MODEL = new RatKingModel<>();

	public RatKingLayer(RenderLayerParent<RatKing, EmptyModel<RatKing>> ratRendererIn) {
		super(ratRendererIn);
	}

	@Override
	public void render(PoseStack stack, MultiBufferSource buffer, int light, RatKing king, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		long roundedTime = king.getLevel().getDayTime() % 24000;
		boolean night = roundedTime >= 13000 && roundedTime <= 22000;
		BlockPos ratPos = king.getLightPosition();
		int brightI = king.getLevel().getBrightness(LightLayer.SKY, ratPos);
		int brightJ = king.getLevel().getBrightness(LightLayer.BLOCK, ratPos);
		int brightness;
		if (night) {
			brightness = brightJ;
		} else {
			brightness = Math.max(brightI, brightJ);
		}

		for (int i = 0; i < RatKing.RAT_COUNT; i++) {
			int deathTime = Math.min(Math.max(0, king.deathTime - i * 5), 5);
			VertexConsumer consumer = buffer.getBuffer(getRatTexture(king.getRatColors(i)));
			stack.pushPose();
			stack.mulPose(Axis.YP.rotationDegrees(i * RatKing.RAT_ANGLE));
			stack.translate(0, 0.6F + (deathTime * 0.01F), -0.8);
			stack.pushPose();
			stack.scale(0.6F, 0.6F, 0.6F);
			RAT_MODEL.setIndex(i);
			RAT_MODEL.setupAnim(king, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
			RAT_MODEL.renderToBuffer(stack, consumer, light, LivingEntityRenderer.getOverlayCoords(king, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
			if (brightness < 7) {
				VertexConsumer eyes = buffer.getBuffer(TEXTURE_EYES);
				RAT_MODEL.renderToBuffer(stack, eyes, light, LivingEntityRenderer.getOverlayCoords(king, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
			}

			stack.popPose();
			stack.popPose();
		}
	}

	private RenderType getRatTexture(int textureIndex) {
		return switch (textureIndex) {
			case 1 -> TEXTURE_1;
			case 2 -> TEXTURE_2;
			case 3 -> TEXTURE_3;
			default -> TEXTURE_0;
		};
	}
}
