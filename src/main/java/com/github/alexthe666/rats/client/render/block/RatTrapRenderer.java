package com.github.alexthe666.rats.client.render.block;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.model.block.RatTrapModel;
import com.github.alexthe666.rats.server.block.RatTrapBlock;
import com.github.alexthe666.rats.server.block.entity.RatTrapBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class RatTrapRenderer implements BlockEntityRenderer<RatTrapBlockEntity> {
	private static final RatTrapModel<?> MODEL_RAT_TRAP = new RatTrapModel<>();
	private static final RenderType TEXTURE = RenderType.entityCutout(new ResourceLocation(RatsMod.MODID, "textures/model/rat_trap.png"));

	public RatTrapRenderer(BlockEntityRendererProvider.Context context) {
	}

	@Override
	public void render(RatTrapBlockEntity entity, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light, int overlay) {
		float rotation = 0;
		float shutProgress = 0;
		ItemStack bait = ItemStack.EMPTY;
		if (entity.getLevel() != null && entity.getLevel().getBlockState(entity.getBlockPos()).getBlock() instanceof RatTrapBlock) {
			rotation = entity.getLevel().getBlockState(entity.getBlockPos()).getValue(RatTrapBlock.FACING).toYRot();
			shutProgress = entity.shutProgress;
			bait = entity.getBait();
		}
		stack.pushPose();
		stack.translate(0.5D, 1.5D, 0.5D);

		stack.mulPose(Axis.XP.rotationDegrees(180));
		stack.mulPose(Axis.YP.rotationDegrees(rotation));
		VertexConsumer consumer = buffer.getBuffer(TEXTURE);
		MODEL_RAT_TRAP.animateHinge(shutProgress);
		MODEL_RAT_TRAP.renderToBuffer(stack, consumer, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
		if (!bait.isEmpty()) {
			stack.scale(0.4F, 0.4F, 0.4F);
			stack.translate(0, 3.4F, -0.5F);
			stack.mulPose(Axis.XP.rotationDegrees(90));
			stack.mulPose(Axis.YP.rotationDegrees(180));
			Minecraft.getInstance().getItemRenderer().renderStatic(bait, ItemDisplayContext.FIXED, light, overlay, stack, buffer, null, 0);
		}
		stack.popPose();
	}
}
