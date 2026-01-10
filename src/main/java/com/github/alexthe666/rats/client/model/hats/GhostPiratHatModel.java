package com.github.alexthe666.rats.client.model.hats;

import com.github.alexthe666.rats.client.render.RatsRenderType;
import net.minecraft.client.model.geom.ModelPart;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public class GhostPiratHatModel extends PiratHatModel {
	public GhostPiratHatModel(ModelPart root) {
		super(root, RatsRenderType::getGlowingTranslucent);
	}

	@Override
	public void renderToBuffer(PoseStack stack, VertexConsumer builder, int light, int overlay, int color) {
		// In 1.21, renderToBuffer uses a single int color instead of RGBA floats
		// We use full brightness for ghost effects
		super.renderToBuffer(stack, builder, 0xF000F0, overlay, color);
	}
}







