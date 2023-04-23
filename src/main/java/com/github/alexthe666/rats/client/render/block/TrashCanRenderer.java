package com.github.alexthe666.rats.client.render.block;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.model.block.TrashCanModel;
import com.github.alexthe666.rats.server.block.TrashCanBlock;
import com.github.alexthe666.rats.server.block.entity.TrashCanBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class TrashCanRenderer implements BlockEntityRenderer<TrashCanBlockEntity> {
	private static final TrashCanModel<?> MODEL_TRASH_CAN = new TrashCanModel<>();
	private static final RenderType TEXTURE = RenderType.entityCutoutNoCull(new ResourceLocation(RatsMod.MODID, "textures/block/trash_can.png"), true);

	public TrashCanRenderer(BlockEntityRendererProvider.Context context) {
	}

	@Override
	public void render(TrashCanBlockEntity entity, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light, int overlay) {
		float rotation = 0;
		if (entity.getLevel() != null && entity.getLevel().getBlockState(entity.getBlockPos()).getBlock() instanceof TrashCanBlock) {
			rotation = entity.getLevel().getBlockState(entity.getBlockPos()).getValue(TrashCanBlock.FACING).toYRot();
		}
		stack.pushPose();
		stack.translate(0.5D, 1.501D, 0.5D);
		stack.mulPose(Axis.XP.rotationDegrees(180));
		stack.mulPose(Axis.YP.rotationDegrees(rotation));
		VertexConsumer consumer = buffer.getBuffer(TEXTURE);
		MODEL_TRASH_CAN.animate(entity);
		MODEL_TRASH_CAN.renderToBuffer(stack, consumer, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
		stack.popPose();
	}
}
