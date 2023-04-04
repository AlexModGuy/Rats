package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.ratlantis.GolemBeam;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class GolemBeamRenderer extends EntityRenderer<GolemBeam> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(RatsMod.MODID, "textures/entity/ratlantis/golem_beam.png");

	public GolemBeamRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	public void render(GolemBeam entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light) {
		stack.pushPose();
		stack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) - 90.0F));
		stack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot())));
		float f9 = (float) entity.shakeTime - partialTicks;
		if (f9 > 0.0F) {
			float f10 = -Mth.sin(f9 * 3.0F) * f9;
			stack.mulPose(Axis.ZP.rotationDegrees(f10));
		}

		stack.mulPose(Axis.XP.rotationDegrees(45.0F));
		stack.scale(0.05625F, 0.05625F, 0.05625F);
		stack.translate(-4.0D, 0.0D, 0.0D);
		VertexConsumer consumer = buffer.getBuffer(RenderType.eyes(TEXTURE));
		PoseStack.Pose pose = stack.last();
		Matrix4f matrix4f = pose.pose();
		Matrix3f matrix3f = pose.normal();
		this.vertex(matrix4f, matrix3f, consumer, -7, -2, -2, 0.0F, 0.15625F, -1, 0, 0, 240);
		this.vertex(matrix4f, matrix3f, consumer, -7, -2, 2, 0.15625F, 0.15625F, -1, 0, 0, 240);
		this.vertex(matrix4f, matrix3f, consumer, -7, 2, 2, 0.15625F, 0.3125F, -1, 0, 0, 240);
		this.vertex(matrix4f, matrix3f, consumer, -7, 2, -2, 0.0F, 0.3125F, -1, 0, 0, 240);
		this.vertex(matrix4f, matrix3f, consumer, -7, 2, -2, 0.0F, 0.15625F, 1, 0, 0, 240);
		this.vertex(matrix4f, matrix3f, consumer, -7, 2, 2, 0.15625F, 0.15625F, 1, 0, 0, 240);
		this.vertex(matrix4f, matrix3f, consumer, -7, -2, 2, 0.15625F, 0.3125F, 1, 0, 0, 240);
		this.vertex(matrix4f, matrix3f, consumer, -7, -2, -2, 0.0F, 0.3125F, 1, 0, 0, 240);

		for (int j = 0; j < 4; ++j) {
			stack.mulPose(Axis.XP.rotationDegrees(90.0F));
			this.vertex(matrix4f, matrix3f, consumer, -8, -2, 0, 0.0F, 0.0F, 0, 1, 0, 240);
			this.vertex(matrix4f, matrix3f, consumer, 8, -2, 0, 0.5F, 0.0F, 0, 1, 0, 240);
			this.vertex(matrix4f, matrix3f, consumer, 8, 2, 0, 0.5F, 0.15625F, 0, 1, 0, 240);
			this.vertex(matrix4f, matrix3f, consumer, -8, 2, 0, 0.0F, 0.15625F, 0, 1, 0, 240);
		}

		stack.popPose();
		super.render(entity, entityYaw, partialTicks, stack, buffer, light);
	}

	public void vertex(Matrix4f matrix4f, Matrix3f matrix3f, VertexConsumer consumer, int x, int y, int z, float u, float v, int xNorm, int zNorm, int yNorm, int light) {
		consumer.vertex(matrix4f, (float)x, (float)y, (float)z).color(255, 255, 255, 255).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(matrix3f, (float)xNorm, (float)yNorm, (float)zNorm).endVertex();
	}

	@Override
	public ResourceLocation getTextureLocation(GolemBeam entity) {
		return TEXTURE;
	}
}
