package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.model.entity.FlyingDutchratModel;
import com.github.alexthe666.rats.client.render.RatsRenderType;
import com.github.alexthe666.rats.server.entity.monster.boss.Dutchrat;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import com.github.alexthe666.rats.client.render.entity.layer.DutchratHelmetLayer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public class DutchratRenderer extends MobRenderer<Dutchrat, FlyingDutchratModel<Dutchrat>> {

	private static final ResourceLocation DUTCHRAT_TEXTURE = ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "textures/entity/dutchrat/dutchrat.png");

	public DutchratRenderer(EntityRendererProvider.Context context) {
		super(context, new FlyingDutchratModel<>(), 0.5F);
		this.addLayer(new DutchratGlowLayer<>(this));
		this.addLayer(new DutchratHelmetLayer<>(this, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR))));
		this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()) {
			public void render(PoseStack stack, MultiBufferSource buffer, int light, Dutchrat entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
				if (!entity.hasThrownSword() && entity.getBellSummonTicks() <= 0) {
					super.render(stack, buffer, light, entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
				}
			}
		});
	}

	public ResourceLocation getTextureLocation(Dutchrat entity) {
		return DUTCHRAT_TEXTURE;
	}

	public static class DutchratGlowLayer<T extends Dutchrat, M extends FlyingDutchratModel<T>> extends RenderLayer<T, M> {
		private static final ResourceLocation GLOW_1 = ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "textures/entity/dutchrat/dutchrat_glow_1.png");
		private static final ResourceLocation GLOW_2 = ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "textures/entity/dutchrat/dutchrat_glow_2.png");

		public DutchratGlowLayer(RenderLayerParent<T, M> parent) {
			super(parent);
		}

		@Override
		public void render(PoseStack stack, MultiBufferSource buffer, int light, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
			// 重要：每次只获取一个 buffer，渲染完后再获取下一个
			// 否则 MultiBufferSource 可能会使第一个 buffer 进入无效状态
			VertexConsumer glow1 = buffer.getBuffer(RatsRenderType.getEyesAlphaEnabled(GLOW_1));
			this.getParentModel().renderToBuffer(stack, glow1, 240, OverlayTexture.NO_OVERLAY, -1);
			
			VertexConsumer glow2 = buffer.getBuffer(RatsRenderType.getTranslucentEmissive(GLOW_2));
			this.getParentModel().renderToBuffer(stack, glow2, 240, OverlayTexture.NO_OVERLAY, 0x80FFFFFF);
		}
	}
}







