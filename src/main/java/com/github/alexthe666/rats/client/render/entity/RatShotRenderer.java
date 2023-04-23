package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.model.entity.StaticRatModel;
import com.github.alexthe666.rats.server.entity.projectile.RatShot;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LightLayer;

public class RatShotRenderer extends EntityRenderer<RatShot> {

	private static final RenderType TEXTURE_EYES = RenderType.eyes(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/eyes/glow.png"));
	private static final StaticRatModel<RatShot> MODEL_STATIC_RAT = new StaticRatModel<>(0, false);

	public RatShotRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	public void render(RatShot entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light) {
		long roundedTime = entity.getLevel().getDayTime() % 24000;
		boolean night = roundedTime >= 13000 && roundedTime <= 22000;
		BlockPos ratPos = entity.getLightPosition();
		int brightI = entity.getLevel().getBrightness(LightLayer.SKY, ratPos);
		int brightJ = entity.getLevel().getBrightness(LightLayer.BLOCK, ratPos);
		int brightness;
		if (night) {
			brightness = brightJ;
		} else {
			brightness = Math.max(brightI, brightJ);
		}
		stack.pushPose();
		stack.scale(0.6F, -0.6F, 0.6F);
		float yaw = entity.yRotO + (entity.getYRot() - entity.yRotO) * partialTicks;
		float pitch = entity.xRotO + (entity.getXRot() - entity.xRotO) * partialTicks;
		stack.translate(0F, -1.5F, 0F);
		stack.mulPose(Axis.YP.rotationDegrees(yaw - 180));
		stack.mulPose(Axis.XN.rotationDegrees(pitch));

		VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(entity.getColorVariant().getTexture()));
		float f = (entity.tickCount + partialTicks) * 0.5F;
		float f1 = 1;
		MODEL_STATIC_RAT.setupAnim(entity, f, f1, entity.tickCount + partialTicks, 0, 0);
		MODEL_STATIC_RAT.renderToBuffer(stack, consumer, light, OverlayTexture.pack(OverlayTexture.u(0), OverlayTexture.v(false)), 1.0F, 1.0F, 1.0F, 1.0F);

		if (brightness < 7) {
			VertexConsumer iGlowBuffer = buffer.getBuffer(TEXTURE_EYES);
			MODEL_STATIC_RAT.renderToBuffer(stack, iGlowBuffer, light, OverlayTexture.pack(OverlayTexture.u(0), OverlayTexture.v(false)), 1.0F, 1.0F, 1.0F, 1.0F);
		}
		stack.popPose();

		super.render(entity, entityYaw, partialTicks, stack, buffer, light);
	}

	@Override
	public ResourceLocation getTextureLocation(RatShot entity) {
		return null;
	}
}
