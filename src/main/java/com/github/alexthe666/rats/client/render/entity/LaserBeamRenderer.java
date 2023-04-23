package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.projectile.LaserBeam;
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

public class LaserBeamRenderer extends EntityRenderer<LaserBeam> {

	private static final ResourceLocation TEXTURE_RED = new ResourceLocation(RatsMod.MODID, "textures/entity/ratlantis/laser_beam.png");
	private static final ResourceLocation TEXTURE_BLUE = new ResourceLocation(RatsMod.MODID, "textures/entity/ratlantis/laser_beam_blue.png");
	private static final RenderType RENDER_TYPE_RED = RenderType.eyes(TEXTURE_RED);
	private static final RenderType RENDER_TYPE_BLUE = RenderType.eyes(TEXTURE_BLUE);

	public LaserBeamRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void render(LaserBeam entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light) {
		stack.pushPose();
		stack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) - 90.0F));
		stack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot())));
		float f9 = (float) entity.shakeTime - partialTicks;
		int r = (int) (entity.getRGB()[0] * 255F);
		int g = (int) (entity.getRGB()[1] * 255F);
		int b = (int) (entity.getRGB()[2] * 255F);
		if (f9 > 0.0F) {
			float f10 = -Mth.sin(f9 * 3.0F) * f9;
			stack.mulPose(Axis.ZP.rotationDegrees(f10));
		}

		stack.mulPose(Axis.XP.rotationDegrees(45.0F));
		stack.scale(0.05625F, 0.05625F, 0.05625F);
		stack.translate(-4.0D, 0.0D, 0.0D);
		VertexConsumer consumer = buffer.getBuffer(r > 200 ? RENDER_TYPE_RED : RENDER_TYPE_BLUE);
		PoseStack.Pose matrixstack$entry = stack.last();
		Matrix4f matrix4f = matrixstack$entry.pose();
		Matrix3f matrix3f = matrixstack$entry.normal();
		light = 240;
		this.vertex(matrix4f, matrix3f, consumer, -7, -2, -2, 0.0F, 0.15625F, -1, 0, 0, light, r, g, b);
		this.vertex(matrix4f, matrix3f, consumer, -7, -2, 2, 0.15625F, 0.15625F, -1, 0, 0, light, r, g, b);
		this.vertex(matrix4f, matrix3f, consumer, -7, 2, 2, 0.15625F, 0.3125F, -1, 0, 0, light, r, g, b);
		this.vertex(matrix4f, matrix3f, consumer, -7, 2, -2, 0.0F, 0.3125F, -1, 0, 0, light, r, g, b);
		this.vertex(matrix4f, matrix3f, consumer, -7, 2, -2, 0.0F, 0.15625F, 1, 0, 0, light, r, g, b);
		this.vertex(matrix4f, matrix3f, consumer, -7, 2, 2, 0.15625F, 0.15625F, 1, 0, 0, light, r, g, b);
		this.vertex(matrix4f, matrix3f, consumer, -7, -2, 2, 0.15625F, 0.3125F, 1, 0, 0, light, r, g, b);
		this.vertex(matrix4f, matrix3f, consumer, -7, -2, -2, 0.0F, 0.3125F, 1, 0, 0, light, r, g, b);
		stack.popPose();
		super.render(entity, entityYaw, partialTicks, stack, buffer, light);
	}

	public void vertex(Matrix4f m4f, Matrix3f m3f, VertexConsumer consumer, float x, float y, float z, float u, float v, int normX, int normZ, int normY, int light, int red, int green, int blue) {
		consumer.vertex(m4f, x, y, z).color(red, green, blue, 255).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(m3f, (float) normX, (float) normY, (float) normZ).endVertex();
	}

	@Override
	public ResourceLocation getTextureLocation(LaserBeam entity) {
		int r = (int) (entity.getRGB()[0] * 255F);
		return r > 200 ? TEXTURE_RED : TEXTURE_BLUE;
	}
}
