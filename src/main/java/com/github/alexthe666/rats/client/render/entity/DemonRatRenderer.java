package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.model.entity.RatModel;
import com.github.alexthe666.rats.server.entity.rat.DemonRat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class DemonRatRenderer extends AbstractRatRenderer<DemonRat> {

	public static final ResourceLocation BASE_TEXTURE = new ResourceLocation(RatsMod.MODID, "textures/entity/demon_rat/demon_rat.png");
	public static final ResourceLocation SOUL_TEXTURE = new ResourceLocation(RatsMod.MODID, "textures/entity/demon_rat/soul_demon_rat.png");
	public static final ResourceLocation BASE_EYE_TEXTURE = new ResourceLocation(RatsMod.MODID, "textures/entity/demon_rat/demon_rat_eye.png");
	public static final ResourceLocation SOUL_EYE_TEXTURE = new ResourceLocation(RatsMod.MODID, "textures/entity/demon_rat/soul_demon_rat_eye.png");

	public DemonRatRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.addLayer(new DemonEyesLayer(this));
	}

	@Override
	protected void scale(DemonRat rat, PoseStack stack, float partialTicks) {
		stack.scale(1.5F, 1.5F, 1.5F);
		super.scale(rat, stack, partialTicks);
	}

	@Override
	public ResourceLocation getTextureLocation(DemonRat entity) {
		return entity.isSoulVariant() ? SOUL_TEXTURE : BASE_TEXTURE;
	}

	public static class DemonEyesLayer extends RenderLayer<DemonRat, RatModel<DemonRat>> {
		public DemonEyesLayer(RenderLayerParent<DemonRat, RatModel<DemonRat>> parent) {
			super(parent);
		}

		@Override
		public void render(PoseStack stack, MultiBufferSource buffer, int light, DemonRat rat, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
			VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.eyes(rat.isSoulVariant() ? SOUL_EYE_TEXTURE : BASE_EYE_TEXTURE));
			this.getParentModel().renderToBuffer(stack, vertexconsumer, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		}
	}
}