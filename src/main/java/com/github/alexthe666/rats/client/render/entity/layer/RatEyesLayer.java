package com.github.alexthe666.rats.client.render.entity.layer;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.model.entity.RatModel;
import com.github.alexthe666.rats.server.entity.rat.AbstractRat;
import com.github.alexthe666.rats.server.entity.rat.Rat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LightLayer;

public class RatEyesLayer<T extends AbstractRat, M extends RatModel<T>> extends RenderLayer<T, M> {
	protected static final RenderType EYES = RenderType.eyes(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/rat_eye_glow.png"));
	protected static final RenderType PLAGUE_EYES = RenderType.eyes(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/rat_eye_plague.png"));
	public RatEyesLayer(RenderLayerParent<T, M> parent) {
		super(parent);
	}

	@Override
	public void render(PoseStack stack, MultiBufferSource buffer, int light, T rat, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		long roundedTime = rat.getLevel().getDayTime() % 24000;
		boolean night = roundedTime >= 13000 && roundedTime <= 23000;
		BlockPos ratPos = rat.getLightPosition();
		int i = rat.getLevel().getBrightness(LightLayer.SKY, ratPos);
		int j = rat.getLevel().getBrightness(LightLayer.BLOCK, ratPos);
		int brightness;
		if (night) {
			brightness = j;
		} else {
			brightness = Math.max(i, j);
		}
		if (rat instanceof Rat plagueable && plagueable.hasPlague()) {
			VertexConsumer consumer = buffer.getBuffer(PLAGUE_EYES);
			this.getParentModel().renderToBuffer(stack, consumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		} else if (brightness < 7) {
			VertexConsumer consumer = buffer.getBuffer(EYES);
			this.getParentModel().renderToBuffer(stack, consumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		}
	}
}