package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.render.RatsRenderType;
import com.github.alexthe666.rats.server.entity.projectile.RatlantisArrow;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class RatlantisArrowRenderer extends ArrowRenderer<RatlantisArrow> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(RatsMod.MODID, "textures/entity/ratlantis/ratlantis_arrow.png");

	public RatlantisArrowRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	public void render(RatlantisArrow entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light) {
		stack.pushPose();
		stack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) - 90.0F));
		stack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot())));
		light = 240;
		float f9 = (float) entity.shakeTime - partialTicks;
		if (f9 > 0.0F) {
			float f10 = -Mth.sin(f9 * 3.0F) * f9;
			stack.mulPose(Axis.ZP.rotationDegrees(f10));
		}

		stack.mulPose(Axis.XP.rotationDegrees(45.0F));
		stack.scale(0.05625F, 0.05625F, 0.05625F);
		stack.translate(-4.0D, 0.0D, 0.0D);
		VertexConsumer vertexBuilder = buffer.getBuffer(RatsRenderType.getYellowGlint());
		PoseStack.Pose pose = stack.last();
		Matrix4f matrix4f = pose.pose();
		Matrix3f matrix3f = pose.normal();
		this.vertex(matrix4f, matrix3f, vertexBuilder, -7, -2, -2, 0.0F, 0.15625F, -1, 0, 0, 1);
		this.vertex(matrix4f, matrix3f, vertexBuilder, -7, -2, 2, 0.15625F, 0.15625F, -1, 0, 0, 1);
		this.vertex(matrix4f, matrix3f, vertexBuilder, -7, 2, 2, 0.15625F, 0.3125F, -1, 0, 0, 1);
		this.vertex(matrix4f, matrix3f, vertexBuilder, -7, 2, -2, 0.0F, 0.3125F, -1, 0, 0, 1);
		this.vertex(matrix4f, matrix3f, vertexBuilder, -7, 2, -2, 0.0F, 0.15625F, 1, 0, 0, 1);
		this.vertex(matrix4f, matrix3f, vertexBuilder, -7, 2, 2, 0.15625F, 0.15625F, 1, 0, 0, 1);
		this.vertex(matrix4f, matrix3f, vertexBuilder, -7, -2, 2, 0.15625F, 0.3125F, 1, 0, 0, 1);
		this.vertex(matrix4f, matrix3f, vertexBuilder, -7, -2, -2, 0.0F, 0.3125F, 1, 0, 0, 1);

		for (int j = 0; j < 4; ++j) {
			stack.mulPose(Axis.XP.rotationDegrees(90.0F));
			this.vertex(matrix4f, matrix3f, vertexBuilder, -8, -2, 0, 0.0F, 0.0F, 0, 1, 0, 1);
			this.vertex(matrix4f, matrix3f, vertexBuilder, 8, -2, 0, 0.5F, 0.0F, 0, 1, 0, 1);
			this.vertex(matrix4f, matrix3f, vertexBuilder, 8, 2, 0, 0.5F, 0.15625F, 0, 1, 0, 1);
			this.vertex(matrix4f, matrix3f, vertexBuilder, -8, 2, 0, 0.0F, 0.15625F, 0, 1, 0, 1);
		}

		stack.popPose();
		super.render(entity, entityYaw, partialTicks, stack, buffer, light);
	}

	@Override
	public ResourceLocation getTextureLocation(RatlantisArrow entity) {
		return TEXTURE;
	}
}
