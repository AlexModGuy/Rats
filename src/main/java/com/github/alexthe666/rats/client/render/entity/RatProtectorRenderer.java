package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.model.entity.RatModel;
import com.github.alexthe666.rats.client.render.RatsRenderType;
import com.github.alexthe666.rats.server.entity.misc.RatProtector;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public class RatProtectorRenderer extends AbstractRatRenderer<RatProtector, RatModel<RatProtector>> {

	public static final ResourceLocation BASE_TEXTURE = ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "textures/entity/rat_protector.png");

	public RatProtectorRenderer(EntityRendererProvider.Context context) {
		super(context, new RatModel<>());
		this.addLayer(new Overlay(this));
	}

	public ResourceLocation getTextureLocation(RatProtector entity) {
		return BASE_TEXTURE;
	}

	private static class Overlay extends RenderLayer<RatProtector, RatModel<RatProtector>> {

		public Overlay(RenderLayerParent<RatProtector, RatModel<RatProtector>> parent) {
			super(parent);
		}

		@Override
		public void render(PoseStack stack, MultiBufferSource buffer, int light, RatProtector rat, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
			VertexConsumer vertexBuilder = buffer.getBuffer(RatsRenderType.getYellowGlint());
			this.getParentModel().setupAnim(rat, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
			int color = FastColor.ARGB32.colorFromFloat(1.0F, 0.5F, 0.5F, 0.5F);
			this.getParentModel().renderToBuffer(stack, vertexBuilder, light, OverlayTexture.NO_OVERLAY, color);
		}
	}
}






