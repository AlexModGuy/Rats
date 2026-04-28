package com.github.alexthe666.rats.client.render.block;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.render.RatsRenderType;
import com.github.alexthe666.rats.server.block.entity.RatlantisPortalBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

public class RatlantisPortalRenderer implements BlockEntityRenderer<RatlantisPortalBlockEntity> {
	public static final ResourceLocation PORTAL_BG = ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "textures/environment/ratlantis_sky_portal.png");
	public static final ResourceLocation PORTAL_FG = ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "textures/environment/ratlantis_portal.png");

	public RatlantisPortalRenderer(BlockEntityRendererProvider.Context context) {
	}

	public void render(RatlantisPortalBlockEntity entity, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light, int overlay) {
		Matrix4f matrix4f = stack.last().pose();
		this.renderCube(entity, matrix4f, buffer.getBuffer(RatsRenderType.getRatlantisPortal()));
	}

	private void renderCube(RatlantisPortalBlockEntity entity, Matrix4f matrix, VertexConsumer consumer) {
		float f = 1.0F;
		float f1 = 1.0F;
		float f2 = 1.0F;
		this.renderFace(entity, matrix, consumer, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, f, f1, f2, Direction.SOUTH);
		this.renderFace(entity, matrix, consumer, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, f, f1, f2, Direction.NORTH);
		this.renderFace(entity, matrix, consumer, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, f, f1, f2, Direction.EAST);
		this.renderFace(entity, matrix, consumer, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, f, f1, f2, Direction.WEST);
		this.renderFace(entity, matrix, consumer, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f, f1, f2, Direction.DOWN);
		this.renderFace(entity, matrix, consumer, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, f, f1, f2, Direction.UP);
	}

	private void renderFace(RatlantisPortalBlockEntity entity, Matrix4f matrix, VertexConsumer consumer, float xMin, float xMax, float yMin, float yMax, float p_228884_8_, float p_228884_9_, float p_228884_10_, float p_228884_11_, float red, float green, float blue, Direction direction) {
		if (entity.shouldRenderFace(direction)) {
			consumer.addVertex(matrix, xMin, yMin, p_228884_8_).setColor(red, green, blue, 1.0F);
			consumer.addVertex(matrix, xMax, yMin, p_228884_9_).setColor(red, green, blue, 1.0F);
			consumer.addVertex(matrix, xMax, yMax, p_228884_10_).setColor(red, green, blue, 1.0F);
			consumer.addVertex(matrix, xMin, yMax, p_228884_11_).setColor(red, green, blue, 1.0F);
		}

	}
}