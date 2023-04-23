package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.model.entity.RatModel;
import com.github.alexthe666.rats.server.entity.rat.AbstractRat;
import com.github.alexthe666.rats.server.entity.monster.GhostPirat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class GhostPiratRenderer extends AbstractRatRenderer<GhostPirat> {

	private static final ResourceLocation BASE_TEXTURE = new ResourceLocation(RatsMod.MODID, "textures/entity/ghost_pirat/ghost_pirat.png");

	public GhostPiratRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.addLayer(new GhostPiratLayer<>(this));
	}


	protected void scale(GhostPirat rat, PoseStack stack, float partialTicks) {
		super.scale(rat, stack, partialTicks);
		this.shadowRadius = 0.35F;
		stack.scale(2.0F, 2.0F, 2.0F);
	}

	public ResourceLocation getTextureLocation(GhostPirat entity) {
		return BASE_TEXTURE;
	}

	private static class GhostPiratLayer<T extends AbstractRat> extends RenderLayer<T, RatModel<T>> {
		private static final ResourceLocation GHOST_OVERLAY = new ResourceLocation(RatsMod.MODID, "textures/entity/ghost_pirat/ghost_pirat_overlay.png");

		public GhostPiratLayer(RenderLayerParent<T, RatModel<T>> parent) {
			super(parent);
		}

		@Override
		public void render(PoseStack stack, MultiBufferSource buffer, int light, T rat, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
			float f = (float) rat.tickCount + partialTicks;
			VertexConsumer consumer = buffer.getBuffer(RenderType.energySwirl(GHOST_OVERLAY, f * 0.01F, f * 0.01F));
			this.getParentModel().renderToBuffer(stack, consumer, light, OverlayTexture.NO_OVERLAY, 0.5F, 0.5F, 0.5F, 1.0F);
		}
	}
}