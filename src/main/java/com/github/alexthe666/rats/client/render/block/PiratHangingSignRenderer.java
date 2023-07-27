package com.github.alexthe666.rats.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.level.block.state.properties.WoodType;

public class PiratHangingSignRenderer extends HangingSignRenderer {
	public PiratHangingSignRenderer(BlockEntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void renderSign(PoseStack stack, MultiBufferSource buffer, int light, int overlay, WoodType type, Model model) {
		stack.pushPose();
		float f = this.getSignModelRenderScale();
		stack.scale(f, -f, -f);
		Material material = Sheets.getHangingSignMaterial(type);
		VertexConsumer vertexconsumer = material.buffer(buffer, RenderType::entityTranslucent);
		this.renderSignModel(stack, light, overlay, model, vertexconsumer);
		stack.popPose();
	}

	void renderSignModel(PoseStack stack, int light, int overlay, Model model, VertexConsumer consumer) {
		HangingSignModel sign = (HangingSignModel)model;
		sign.root.render(stack, consumer, light, overlay);
	}
}
