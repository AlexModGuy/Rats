package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.RatsModelLayers;
import com.github.alexthe666.rats.client.model.entity.RatStriderMountModel;
import com.github.alexthe666.rats.server.entity.mount.RatStriderMount;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class RatStriderMountRenderer extends MobRenderer<RatStriderMount, RatStriderMountModel<RatStriderMount>> {

	private static final ResourceLocation STRIDER_LOCATION = new ResourceLocation("textures/entity/strider/strider.png");
	private static final ResourceLocation COLD_LOCATION = new ResourceLocation("textures/entity/strider/strider_cold.png");

	public RatStriderMountRenderer(EntityRendererProvider.Context context) {
		super(context, new RatStriderMountModel<>(context.bakeLayer(RatsModelLayers.RAT_STRIDER_MOUNT)), 0.5F);
		this.addLayer(new AlwaysSaddledLayer<>(this, new RatStriderMountModel<>(context.bakeLayer(RatsModelLayers.RAT_STRIDER_MOUNT)), new ResourceLocation("textures/entity/strider/strider_saddle.png")));
	}

	public ResourceLocation getTextureLocation(RatStriderMount mount) {
		return mount.isSuffocating() ? COLD_LOCATION : STRIDER_LOCATION;
	}

	protected boolean isShaking(RatStriderMount mount) {
		return super.isShaking(mount) || mount.isSuffocating();
	}

	public static class AlwaysSaddledLayer<T extends Entity, M extends EntityModel<T>> extends RenderLayer<T, M> {
		private final ResourceLocation textureLocation;
		private final M model;

		public AlwaysSaddledLayer(RenderLayerParent<T, M> parent, M model, ResourceLocation texture) {
			super(parent);
			this.model = model;
			this.textureLocation = texture;
		}

		public void render(PoseStack stack, MultiBufferSource source, int light, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
			this.getParentModel().copyPropertiesTo(this.model);
			this.model.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks);
			this.model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
			VertexConsumer vertexconsumer = source.getBuffer(RenderType.entityCutoutNoCull(this.textureLocation));
			this.model.renderToBuffer(stack, vertexconsumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		}
	}
}
