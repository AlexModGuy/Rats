package com.github.alexthe666.rats.client.render.block;

import com.github.alexthe666.rats.server.block.RatHoleBlock;
import com.github.alexthe666.rats.server.block.entity.RatHoleBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.client.model.data.ModelData;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class RatHoleRenderer implements BlockEntityRenderer<RatHoleBlockEntity> {

	private static final AABB TOP_AABB = new AABB(0.0F, 0.5F, 0.0F, 1.0F, 1.0F, 1.0F);
	private static final AABB WEST_CONNECT_AABB = new AABB(0.0F, 0.0F, 0.25F, 0.25F, 0.5F, 0.75F);
	private static final AABB EAST_CONNECT_AABB = new AABB(0.75F, 0.0F, 0.25F, 1.0F, 0.5F, 0.75F);
	private static final AABB NORTH_CONNECT_AABB = new AABB(0.25F, 0.0F, 0.0F, 0.75F, 0.5F, 0.25F);
	private static final AABB SOUTH_CONNECT_AABB = new AABB(0.25F, 0.0F, 0.75F, 0.75F, 0.5F, 1.0F);

	private static final AABB NORTH_CORNER_AABB = new AABB(0.0F, 0.0F, -0.0F, 0.25F, 0.5F, 0.25F);
	private static final AABB EAST_CORNER_AABB = new AABB(0.75F, 0.0F, -0.0F, 1.0F, 0.5F, 0.25F);
	private static final AABB SOUTH_CORNER_AABB = new AABB(0.0F, 0.0F, 0.75F, 0.25F, 0.5F, 1.0F);
	private static final AABB WEST_CORNER_AABB = new AABB(0.75F, 0.0F, 0.75F, 1.0F, 0.5F, 1.0F);

	private static final RenderType TEXTURE = RenderType.entitySmoothCutout(InventoryMenu.BLOCK_ATLAS);

	public RatHoleRenderer(BlockEntityRendererProvider.Context context) {
	}

	@Override
	public int getViewDistance() {
		return 256;
	}

	private void renderAABB(MultiBufferSource buffer, AABB boundingBox, PoseStack stack, TextureAtlasSprite sprite, int light, int overlay) {
		stack.pushPose();
		VertexConsumer vertexbuffer = buffer.getBuffer(TEXTURE);
		double avgY = boundingBox.maxY - boundingBox.minY;
		double avgX = boundingBox.maxX - boundingBox.minX;
		double avgZ = boundingBox.maxZ - boundingBox.minZ;

		float f1 = (float) ((sprite.getX() + (boundingBox.minX * 16.0D)) / (sprite.getX() / sprite.getU0()));
		float maxUX = (float) Math.min(sprite.getU1(), f1 + avgX * Math.abs(sprite.getU1() - sprite.getU0()));
		float f2 = (float) ((sprite.getX() + (boundingBox.minZ * 16.0D)) / (sprite.getX() / sprite.getU0()));
		float maxUZ = (float) Math.min(sprite.getU1(), f2 + avgZ * Math.abs(sprite.getU1() - sprite.getU0()));
		float f3 = (float) ((sprite.getY() + (sprite.contents().height() - (boundingBox.maxY * 16.0D))) / (sprite.getY() / sprite.getV0()));
		float maxVY = (float) Math.min(sprite.getV1(), f3 + avgY * Math.abs(sprite.getV1() - sprite.getV0()));
		float maxVZ = (float) Math.min(sprite.getV1(), f3 + avgZ * Math.abs(sprite.getV1() - sprite.getV0()));
		Matrix4f matrix4f = stack.last().pose();
		Matrix3f matrix3f = stack.last().normal();
		//back
		vertexbuffer.vertex(matrix4f, (float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.minZ).color(255, 255, 255, 255).uv(maxUX, f3).overlayCoords(overlay).uv2(light).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
		vertexbuffer.vertex(matrix4f, (float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.minZ).color(255, 255, 255, 255).uv(f1, f3).overlayCoords(overlay).uv2(light).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
		vertexbuffer.vertex(matrix4f, (float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.minZ).color(255, 255, 255, 255).uv(f1, maxVY).overlayCoords(overlay).uv2(light).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
		vertexbuffer.vertex(matrix4f, (float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.minZ).color(255, 255, 255, 255).uv(maxUX, maxVY).overlayCoords(overlay).uv2(light).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
		//front
		vertexbuffer.vertex(matrix4f, (float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.maxZ).color(255, 255, 255, 255).uv(f1, maxVY).overlayCoords(overlay).uv2(light).normal(matrix3f, 0.0F, 0.0F, -1.0F).endVertex();
		vertexbuffer.vertex(matrix4f, (float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.maxZ).color(255, 255, 255, 255).uv(maxUX, maxVY).overlayCoords(overlay).uv2(light).normal(matrix3f, 0.0F, 0.0F, -1.0F).endVertex();
		vertexbuffer.vertex(matrix4f, (float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.maxZ).color(255, 255, 255, 255).uv(maxUX, f3).overlayCoords(overlay).uv2(light).normal(matrix3f, 0.0F, 0.0F, -1.0F).endVertex();
		vertexbuffer.vertex(matrix4f, (float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.maxZ).color(255, 255, 255, 255).uv(f1, f3).overlayCoords(overlay).uv2(light).normal(matrix3f, 0.0F, 0.0F, -1.0F).endVertex();
		//tops
		vertexbuffer.vertex(matrix4f, (float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.minZ).color(255, 255, 255, 255).uv(f1, maxVZ).overlayCoords(overlay).uv2(light).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
		vertexbuffer.vertex(matrix4f, (float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.minZ).color(255, 255, 255, 255).uv(maxUX, maxVZ).overlayCoords(overlay).uv2(light).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
		vertexbuffer.vertex(matrix4f, (float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.maxZ).color(255, 255, 255, 255).uv(maxUX, f3).overlayCoords(overlay).uv2(light).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
		vertexbuffer.vertex(matrix4f, (float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.maxZ).color(255, 255, 255, 255).uv(f1, f3).overlayCoords(overlay).uv2(light).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();

		vertexbuffer.vertex(matrix4f, (float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.maxZ).color(255, 255, 255, 255).uv(f1, maxVZ).overlayCoords(overlay).uv2(light).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
		vertexbuffer.vertex(matrix4f, (float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.maxZ).color(255, 255, 255, 255).uv(maxUX, maxVZ).overlayCoords(overlay).uv2(light).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
		vertexbuffer.vertex(matrix4f, (float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.minZ).color(255, 255, 255, 255).uv(maxUX, f3).overlayCoords(overlay).uv2(light).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
		vertexbuffer.vertex(matrix4f, (float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.minZ).color(255, 255, 255, 255).uv(f1, f3).overlayCoords(overlay).uv2(light).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
		//sides
		vertexbuffer.vertex(matrix4f, (float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.maxZ).color(255, 255, 255, 255).uv(maxUZ, maxVY).overlayCoords(overlay).uv2(light).normal(matrix3f, -1.0F, 0.0F, 0.0F).endVertex();
		vertexbuffer.vertex(matrix4f, (float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.maxZ).color(255, 255, 255, 255).uv(maxUZ, f3).overlayCoords(overlay).uv2(light).normal(matrix3f, -1.0F, 0.0F, 0.0F).endVertex();
		vertexbuffer.vertex(matrix4f, (float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.minZ).color(255, 255, 255, 255).uv(f2, f3).overlayCoords(overlay).uv2(light).normal(matrix3f, -1.0F, 0.0F, 0.0F).endVertex();
		vertexbuffer.vertex(matrix4f, (float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.minZ).color(255, 255, 255, 255).uv(f2, maxVY).overlayCoords(overlay).uv2(light).normal(matrix3f, -1.0F, 0.0F, 0.0F).endVertex();

		vertexbuffer.vertex(matrix4f, (float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.minZ).color(255, 255, 255, 255).uv(maxUZ, maxVY).overlayCoords(overlay).uv2(light).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
		vertexbuffer.vertex(matrix4f, (float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.minZ).color(255, 255, 255, 255).uv(maxUZ, f3).overlayCoords(overlay).uv2(light).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
		vertexbuffer.vertex(matrix4f, (float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.maxZ).color(255, 255, 255, 255).uv(f2, f3).overlayCoords(overlay).uv2(light).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
		vertexbuffer.vertex(matrix4f, (float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.maxZ).color(255, 255, 255, 255).uv(f2, maxVY).overlayCoords(overlay).uv2(light).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
		stack.popPose();
	}

	@Override
	public void render(RatHoleBlockEntity entity, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light, int overlay) {
		BlockState state = Blocks.OAK_PLANKS.defaultBlockState();
		boolean connectedNorth = false;
		boolean connectedEast = false;
		boolean connectedSouth = false;
		boolean connectedWest = false;
		if (entity.getLevel() != null && entity.getLevel().getBlockState(entity.getBlockPos()).getBlock() instanceof RatHoleBlock) {
			BlockState actualState = entity.getBlockState();
			connectedNorth = actualState.getValue(RatHoleBlock.NORTH);
			connectedEast = actualState.getValue(RatHoleBlock.EAST);
			connectedSouth = actualState.getValue(RatHoleBlock.SOUTH);
			connectedWest = actualState.getValue(RatHoleBlock.WEST);
			state = entity.getImitatedBlockState();
		}
		stack.pushPose();
		BakedModel model = Minecraft.getInstance().getBlockRenderer().getBlockModel(state);
		this.renderAABB(buffer, TOP_AABB, stack, model.getParticleIcon(ModelData.EMPTY), LevelRenderer.getLightColor(entity.getLevel(), entity.getBlockPos()), overlay);
		this.renderAABB(buffer, EAST_CORNER_AABB, stack, model.getParticleIcon(ModelData.EMPTY), LevelRenderer.getLightColor(entity.getLevel(), entity.getBlockPos()), overlay);
		this.renderAABB(buffer, WEST_CORNER_AABB, stack, model.getParticleIcon(ModelData.EMPTY), LevelRenderer.getLightColor(entity.getLevel(), entity.getBlockPos()), overlay);
		this.renderAABB(buffer, NORTH_CORNER_AABB, stack, model.getParticleIcon(ModelData.EMPTY), LevelRenderer.getLightColor(entity.getLevel(), entity.getBlockPos()), overlay);
		this.renderAABB(buffer, SOUTH_CORNER_AABB, stack, model.getParticleIcon(ModelData.EMPTY), LevelRenderer.getLightColor(entity.getLevel(), entity.getBlockPos()), overlay);
		if (connectedEast) {
			this.renderAABB(buffer, EAST_CONNECT_AABB, stack, model.getParticleIcon(ModelData.EMPTY), LevelRenderer.getLightColor(entity.getLevel(), entity.getBlockPos()), overlay);
		}
		if (connectedWest) {
			this.renderAABB(buffer, WEST_CONNECT_AABB, stack, model.getParticleIcon(ModelData.EMPTY), LevelRenderer.getLightColor(entity.getLevel(), entity.getBlockPos()), overlay);
		}
		if (connectedNorth) {
			this.renderAABB(buffer, NORTH_CONNECT_AABB, stack, model.getParticleIcon(ModelData.EMPTY), LevelRenderer.getLightColor(entity.getLevel(), entity.getBlockPos()), overlay);
		}
		if (connectedSouth) {
			this.renderAABB(buffer, SOUTH_CONNECT_AABB, stack, model.getParticleIcon(ModelData.EMPTY), LevelRenderer.getLightColor(entity.getLevel(), entity.getBlockPos()), overlay);
		}
		if (entity.getLevel() != null) {
			Minecraft.getInstance().getBlockRenderer().renderBreakingTexture(entity.getBlockState(), entity.getBlockPos(), entity.getLevel(), stack, buffer.getBuffer(RenderType.solid()), ModelData.EMPTY);
		}
		stack.popPose();
	}
}
