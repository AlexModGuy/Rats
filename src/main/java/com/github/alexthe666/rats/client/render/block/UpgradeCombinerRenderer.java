package com.github.alexthe666.rats.client.render.block;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.model.entity.RatlanteanSpiritModel;
import com.github.alexthe666.rats.client.render.RatsRenderType;
import com.github.alexthe666.rats.server.block.entity.UpgradeCombinerBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class UpgradeCombinerRenderer implements BlockEntityRenderer<UpgradeCombinerBlockEntity> {
	private static final RatlanteanSpiritModel<?> MODEL_SPIRIT = new RatlanteanSpiritModel<>();
	private static final RenderType TEXTURE = RatsRenderType.getGlowingTranslucent(new ResourceLocation(RatsMod.MODID, "textures/entity/ratlantis/upgrade_combiner.png"));

	public UpgradeCombinerRenderer(BlockEntityRendererProvider.Context context) {
	}

	@Override
	public void render(UpgradeCombinerBlockEntity entity, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light, int overlay) {
		stack.pushPose();
		stack.translate(0.5D, 0.0D, 0.5D);
		float f = (float) entity.tickCount + Minecraft.getInstance().getPartialTick();
		stack.translate(0.0F, 3.25F + Mth.sin(f * 0.1F) * 0.1F, 0.0F);
		float f1;

		for (f1 = entity.ratRotation - entity.ratRotationPrev; f1 >= (float) Math.PI; f1 -= ((float) Math.PI * 2F)) {
		}

		while (f1 < -(float) Math.PI) {
			f1 += ((float) Math.PI * 2F);
		}
		float f2 = entity.ratRotationPrev + f1 * Minecraft.getInstance().getPartialTick();
		stack.mulPose(Axis.YP.rotationDegrees(-f2 * (180F / (float) Math.PI) - 90F));
		stack.mulPose(Axis.ZP.rotationDegrees(180));
		stack.scale(1.5F, 1.5F, 1.5F);
		VertexConsumer consumer = buffer.getBuffer(TEXTURE);
		MODEL_SPIRIT.renderToBuffer(stack, consumer, 244, overlay, 1.0F, 1.0F, 1.0F, 0.5F);
		stack.popPose();
	}
}
