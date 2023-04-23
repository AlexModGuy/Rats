package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.model.CubeModel;
import com.github.alexthe666.rats.client.model.RatsModelLayers;
import com.github.alexthe666.rats.server.entity.ThrownBlock;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.state.BlockState;

public class ThrownBlockRenderer extends EntityRenderer<ThrownBlock> {
	private static final ResourceLocation LIGHTNING_TEXTURE = new ResourceLocation(RatsMod.MODID, "textures/entity/ratlantis/psychic.png");
	private final CubeModel<ThrownBlock> cube;

	public ThrownBlockRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.cube = new CubeModel<>(context.bakeLayer(RatsModelLayers.THROWN_BLOCK));
		this.shadowRadius = 0.5F;
	}

	public void render(ThrownBlock entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light) {
		float f = entity.tickCount + partialTicks;
		float yaw = entity.yRotO + (entity.getYRot() - entity.yRotO) * partialTicks;
		stack.pushPose();
		BlockState state = entity.getHeldBlockState();
		if (state != null) {
			BlockRenderDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRenderer();
			stack.pushPose();
			stack.translate(-0.5D, 0, 0.5D);
			stack.mulPose(Axis.YP.rotationDegrees(90.0F));
			blockrendererdispatcher.renderSingleBlock(state, stack, buffer, light, OverlayTexture.NO_OVERLAY);
			stack.popPose();
		}
		stack.popPose();

		stack.pushPose();
		stack.scale(1F, -1F, 1F);
		stack.translate(0F, -0.5F, 0F);
		stack.mulPose(Axis.YP.rotationDegrees(yaw - 180));
		VertexConsumer consumer = buffer.getBuffer(RenderType.energySwirl(LIGHTNING_TEXTURE, f * 0.01F, f * 0.01F));
		this.cube.renderToBuffer(stack, consumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		stack.popPose();
		super.render(entity, entityYaw, partialTicks, stack, buffer, light);

	}

	public ResourceLocation getTextureLocation(ThrownBlock entity) {
		return InventoryMenu.BLOCK_ATLAS;
	}
}