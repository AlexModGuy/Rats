package com.github.alexthe666.rats.client.render.entity.layer;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.model.entity.AbstractRatModel;
import com.github.alexthe666.rats.client.render.RatsRenderType;
import com.github.alexthe666.rats.server.entity.rat.AbstractRat;
import com.github.alexthe666.rats.server.entity.rat.Rat;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LightLayer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public class RatEyesLayer<T extends AbstractRat, M extends AbstractRatModel<T>> extends RenderLayer<T, M> {
	protected static final ResourceLocation EYES_TEXTURE = ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "textures/entity/rat/eyes/glow.png");
	protected static final ResourceLocation PLAGUE_EYES_TEXTURE = ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "textures/entity/rat/eyes/plague.png");

	public RatEyesLayer(RenderLayerParent<T, M> parent) {
		super(parent);
	}

	@Override
	public void render(PoseStack stack, MultiBufferSource buffer, int light, T rat, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		long roundedTime = rat.level().getDayTime() % 24000;
		boolean night = roundedTime >= 13000 && roundedTime <= 23000;
		BlockPos ratPos = rat.getLightPosition();
		int i = rat.level().getBrightness(LightLayer.SKY, ratPos);
		int j = rat.level().getBrightness(LightLayer.BLOCK, ratPos);
		int brightness;
		if (night) {
			brightness = j;
		} else {
			brightness = Math.max(i, j);
		}
		if (rat instanceof Rat plagueable && plagueable.hasPlague()) {
			VertexConsumer consumer = buffer.getBuffer(RatsRenderType.getEyesAlphaEnabled(PLAGUE_EYES_TEXTURE));
			this.getParentModel().renderToBuffer(stack, consumer, light, OverlayTexture.NO_OVERLAY, -1);
		} else if (brightness < 7) {
			VertexConsumer consumer = buffer.getBuffer(RatsRenderType.getEyesAlphaEnabled(EYES_TEXTURE));
			this.getParentModel().renderToBuffer(stack, consumer, light, OverlayTexture.NO_OVERLAY, -1);
		}
	}
}






