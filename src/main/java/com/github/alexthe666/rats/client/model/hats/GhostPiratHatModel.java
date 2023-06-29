package com.github.alexthe666.rats.client.model.hats;

import com.github.alexthe666.rats.client.render.RatsRenderType;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;

public class GhostPiratHatModel extends PiratHatModel {
	public GhostPiratHatModel(ModelPart root) {
		super(root, RatsRenderType::getGlowingTranslucent);
	}

	@Override
	public void renderToBuffer(PoseStack stack, VertexConsumer builder, int light, int overlay, float red, float green, float blue, float scale) {
		super.renderToBuffer(stack, builder, 0xF000F0, overlay, red, green, blue, scale);
	}
}
