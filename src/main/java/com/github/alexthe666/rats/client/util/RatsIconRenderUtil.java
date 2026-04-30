package com.github.alexthe666.rats.client.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

public class RatsIconRenderUtil {
	// 1.21: BufferBuilder is now produced by tesselator.begin(...), not retrieved separately. The
	// helper allocates its own buffer per call so callers don't need to (and shouldn't) supply one.
	public static void renderPOIIcon(ResourceLocation icon, Vec3 viewVec, @Nullable BlockPos renderPos, float bob, PoseStack stack, Tesselator tesselator) {
		if (renderPos != null && renderPos != BlockPos.ZERO) {
			stack.pushPose();
			RenderSystem.enableBlend();
			RenderSystem.depthMask(false);
			stack.translate(-viewVec.x(), -viewVec.y(), -viewVec.z());
			stack.translate(renderPos.getX() + 0.5F, renderPos.getY() + bob, renderPos.getZ() + 0.5F);
			stack.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation());
			Matrix4f matrix4f = stack.last().pose();
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			RenderSystem.setShaderTexture(0, icon);
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			BufferBuilder buffer = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
			buffer.addVertex(matrix4f, -0.5F, -0.5F, 0).setUv(1.0F, 1.0F);
			buffer.addVertex(matrix4f, -0.5F, 0.5F, 0).setUv(1.0F, 0.0F);
			buffer.addVertex(matrix4f, 0.5F, 0.5F, 0).setUv(0.0F, 0.0F);
			buffer.addVertex(matrix4f, 0.5F, -0.5F, 0).setUv(0.0F, 1.0F);
			com.mojang.blaze3d.vertex.BufferUploader.drawWithShader(buffer.buildOrThrow());
			RenderSystem.disableBlend();
			RenderSystem.depthMask(true);
			stack.popPose();
		}
	}

	public static void renderBox(ResourceLocation texture, Vec3 viewPos, Vec3 centerPos, AABB boxSize, PoseStack stack) {
		RenderSystem.enableBlend();
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, texture);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.disableCull();
		RenderSystem.depthMask(false);
		stack.pushPose();
		stack.translate(-viewPos.x(), -viewPos.y(), -viewPos.z());
		stack.translate(centerPos.x(), centerPos.y(), centerPos.z());
		RenderSystem.runAsFancy(() -> renderMovingAABB(boxSize, stack));
		stack.popPose();
		RenderSystem.depthMask(true);
		RenderSystem.enableCull();
		RenderSystem.disableBlend();
	}

	public static void renderMovingAABB(AABB boundingBox, PoseStack stack) {
		Tesselator tessellator = Tesselator.getInstance();
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		float f3 = Minecraft.getInstance().isPaused() ? 0.0F : (float) (System.currentTimeMillis() % 3000L) / 3000.0F;
		Matrix4f matrix4f = stack.last().pose();
		float maxX = (float) boundingBox.maxX * 0.125F;
		float minX = (float) boundingBox.minX * 0.125F;
		float maxY = (float) boundingBox.maxY * 0.125F;
		float minY = (float) boundingBox.minY * 0.125F;
		float maxZ = (float) boundingBox.maxZ * 0.125F;
		float minZ = (float) boundingBox.minZ * 0.125F;
		BufferBuilder buffer = tessellator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR_NORMAL);
		VertexConsumer vertexbuffer = buffer;
		//north
		vertexbuffer.addVertex(matrix4f, (float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.minZ).setUv(f3 + minX - maxX, f3 + maxY - minY).setColor(255, 255, 255, 255).setNormal(0.0F, 0.0F, -1.0F);
		vertexbuffer.addVertex(matrix4f, (float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.minZ).setUv(f3 + maxX - minX, f3 + maxY - minY).setColor(255, 255, 255, 255).setNormal(0.0F, 0.0F, -1.0F);
		vertexbuffer.addVertex(matrix4f, (float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.minZ).setUv(f3 + maxX - minX, f3 + minY - maxY).setColor(255, 255, 255, 255).setNormal(0.0F, 0.0F, -1.0F);
		vertexbuffer.addVertex(matrix4f, (float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.minZ).setUv(f3 + minX - maxX, f3 + minY - maxY).setColor(255, 255, 255, 255).setNormal(0.0F, 0.0F, -1.0F);

		//south
		vertexbuffer.addVertex(matrix4f, (float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.maxZ).setUv(f3 + minX - maxX, f3 + minY - maxY).setColor(255, 255, 255, 255).setNormal(0.0F, 0.0F, 1.0F);
		vertexbuffer.addVertex(matrix4f, (float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.maxZ).setUv(f3 + maxX - minX, f3 + minY - maxY).setColor(255, 255, 255, 255).setNormal(0.0F, 0.0F, 1.0F);
		vertexbuffer.addVertex(matrix4f, (float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.maxZ).setUv(f3 + maxX - minX, f3 + maxY - minY).setColor(255, 255, 255, 255).setNormal(0.0F, 0.0F, 1.0F);
		vertexbuffer.addVertex(matrix4f, (float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.maxZ).setUv(f3 + minX - maxX, f3 + maxY - minY).setColor(255, 255, 255, 255).setNormal(0.0F, 0.0F, 1.0F);

		//bottom
		vertexbuffer.addVertex(matrix4f, (float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.minZ).setUv(f3 + minX - maxX, f3 + minY - maxY).setColor(255, 255, 255, 255).setNormal(0.0F, -1.0F, 0.0F);
		vertexbuffer.addVertex(matrix4f, (float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.maxZ).setUv(f3 + maxX - minX, f3 + minY - maxY).setColor(255, 255, 255, 255).setNormal(0.0F, -1.0F, 0.0F);
		vertexbuffer.addVertex(matrix4f, (float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.maxZ).setUv(f3 + maxX - minX, f3 + maxZ - minZ).setColor(255, 255, 255, 255).setNormal(0.0F, -1.0F, 0.0F);
		vertexbuffer.addVertex(matrix4f, (float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.minZ).setUv(f3 + minX - maxX, f3 + maxZ - minZ).setColor(255, 255, 255, 255).setNormal(0.0F, -1.0F, 0.0F);

		//top
		vertexbuffer.addVertex(matrix4f, (float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.maxZ).setUv(f3 + minX - maxX, f3 + minY - maxY).setColor(255, 255, 255, 255).setNormal(0.0F, 1.0F, 0.0F);
		vertexbuffer.addVertex(matrix4f, (float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.maxZ).setUv(f3 + maxX - minX, f3 + minY - maxY).setColor(255, 255, 255, 255).setNormal(0.0F, 1.0F, 0.0F);
		vertexbuffer.addVertex(matrix4f, (float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.minZ).setUv(f3 + maxX - minX, f3 + maxZ - minZ).setColor(255, 255, 255, 255).setNormal(0.0F, 1.0F, 0.0F);
		vertexbuffer.addVertex(matrix4f, (float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.minZ).setUv(f3 + minX - maxX, f3 + maxZ - minZ).setColor(255, 255, 255, 255).setNormal(0.0F, 1.0F, 0.0F);

		//west
		vertexbuffer.addVertex(matrix4f, (float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.maxZ).setUv(f3 + minX - maxX, f3 + minY - maxY).setColor(255, 255, 255, 255).setNormal(-1.0F, 0.0F, 0.0F);
		vertexbuffer.addVertex(matrix4f, (float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.minZ).setUv(f3 + minX - maxX, f3 + maxY - minY).setColor(255, 255, 255, 255).setNormal(-1.0F, 0.0F, 0.0F);
		vertexbuffer.addVertex(matrix4f, (float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.minZ).setUv(f3 + maxX - minX, f3 + maxY - minY).setColor(255, 255, 255, 255).setNormal(-1.0F, 0.0F, 0.0F);
		vertexbuffer.addVertex(matrix4f, (float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.maxZ).setUv(f3 + maxX - minX, f3 + minY - maxY).setColor(255, 255, 255, 255).setNormal(-1.0F, 0.0F, 0.0F);

		//east
		vertexbuffer.addVertex(matrix4f, (float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.minZ).setUv(f3 + minX - maxX, f3 + minY - maxY).setColor(255, 255, 255, 255).setNormal(1.0F, 0.0F, 0.0F);
		vertexbuffer.addVertex(matrix4f, (float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.minZ).setUv(f3 + minX - maxX, f3 + maxY - minY).setColor(255, 255, 255, 255).setNormal(1.0F, 0.0F, 0.0F);
		vertexbuffer.addVertex(matrix4f, (float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.maxZ).setUv(f3 + maxX - minX, f3 + maxY - minY).setColor(255, 255, 255, 255).setNormal(1.0F, 0.0F, 0.0F);
		vertexbuffer.addVertex(matrix4f, (float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.maxZ).setUv(f3 + maxX - minX, f3 + minY - maxY).setColor(255, 255, 255, 255).setNormal(1.0F, 0.0F, 0.0F);
		com.mojang.blaze3d.vertex.BufferUploader.drawWithShader(buffer.buildOrThrow());
	}
}
