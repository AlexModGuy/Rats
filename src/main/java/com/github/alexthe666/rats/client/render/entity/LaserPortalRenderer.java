package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.model.entity.LaserPortalModel;
import com.github.alexthe666.rats.server.entity.misc.LaserPortal;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class LaserPortalRenderer extends EntityRenderer<LaserPortal> {

	private static final ResourceLocation PORTAL = new ResourceLocation(RatsMod.MODID, "textures/entity/ratlantis/neo_ratlantean_glow.png");
	private static final LaserPortalModel MODEL_NEO_RATLANTEAN = new LaserPortalModel();

	public LaserPortalRenderer(EntityRendererProvider.Context context) {
		super(context);
		MODEL_NEO_RATLANTEAN.floatyPivot.setRotationPoint(0, 0, 0);
		MODEL_NEO_RATLANTEAN.floatyPivot.rotateAngleY = 0.7853981633974483F;
	}

	public void render(LaserPortal entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light) {
		VertexConsumer consumer = ItemRenderer.getFoilBuffer(buffer, RenderType.entityCutoutNoCull(PORTAL), false, true);
		float d1 = this.interpolateValue(entity.scaleOfPortalPrev, entity.scaleOfPortal, (partialTicks));

		stack.pushPose();
		stack.mulPose(Axis.ZP.rotationDegrees(entity.xRotO + (entity.getXRot() - entity.xRotO) * partialTicks));
		stack.pushPose();
		stack.scale(1.5F * d1, 1.5F * d1, 1.5F * d1);
		stack.translate(0, 0.5F, 0);
		stack.translate(0, 1 - d1, 0);
		stack.mulPose(Axis.YP.rotationDegrees(90));
		stack.mulPose(Axis.XP.rotationDegrees(90));
		stack.mulPose(Axis.ZP.rotationDegrees(entity.yRotO + (entity.getYRot() - entity.yRotO) * partialTicks - 90.0F));
		stack.mulPose(Axis.YP.rotationDegrees((entity.tickCount + partialTicks) * 10));
		MODEL_NEO_RATLANTEAN.renderToBuffer(stack, consumer, 240, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		stack.popPose();
		stack.popPose();
		super.render(entity, entityYaw, partialTicks, stack, buffer, light);
	}

	private float interpolateValue(float start, float end, float pct) {
		return start + (end - start) * pct;
	}

	@Override
	public ResourceLocation getTextureLocation(LaserPortal entity) {
		return PORTAL;
	}
}
