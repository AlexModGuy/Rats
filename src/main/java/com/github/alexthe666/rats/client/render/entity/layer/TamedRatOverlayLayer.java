package com.github.alexthe666.rats.client.render.entity.layer;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.model.entity.RatModel;
import com.github.alexthe666.rats.client.render.RatsRenderType;
import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.ChangesOverlayUpgrade;
import com.github.alexthe666.rats.server.misc.RatColorUtil;
import com.github.alexthe666.rats.server.misc.RatUpgradeUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;

public class TamedRatOverlayLayer extends RenderLayer<TamedRat, RatModel<TamedRat>> {
	private static final RenderType TEXTURE_DYED_NOT = RenderType.entitySmoothCutout(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/undyed_part.png"));
	private static final RenderType TEXTURE_DYED = RenderType.entityNoOutline(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/dyed_part.png"));
	private static final RenderType TOGA_TEX = RenderType.entitySmoothCutout(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/upgrades/toga.png"));

	public TamedRatOverlayLayer(RenderLayerParent<TamedRat, RatModel<TamedRat>> parent) {
		super(parent);
	}

	@Override
	public void render(PoseStack stack, MultiBufferSource buffer, int light, TamedRat rat, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if (rat.getRespawnCountdown() > 0 && RatConfig.ratAngelGlint) {
			VertexConsumer consumer = buffer.getBuffer(RatsRenderType.getWhiteGlint());
			this.getParentModel().renderToBuffer(stack, consumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		} else {
			if (rat.hasToga()) {
				VertexConsumer consumer = buffer.getBuffer(TOGA_TEX);
				this.getParentModel().renderToBuffer(stack, consumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
			}
			if (rat.isDyed()) {
				VertexConsumer consumer;
				if (rat.getDyeColor() == 100) {
					RenderType type = RatsRenderType.GlintType.getRenderTypeBasedOnKeyword(rat.getSpecialDye());
					consumer = buffer.getBuffer(type != null ? type : RatsRenderType.getRainbowGlint());
					this.getParentModel().renderToBuffer(stack, consumer, light, LivingEntityRenderer.getOverlayCoords(rat, 0), 1.0F, 1.0F, 1.0F, 1.0F);
					VertexConsumer consumer1 = buffer.getBuffer(TEXTURE_DYED_NOT);
					this.getParentModel().renderToBuffer(stack, consumer1, light, LivingEntityRenderer.getOverlayCoords(rat, 0), 1.0F, 1.0F, 1.0F, 1.0F);
				} else {
					consumer = buffer.getBuffer(TEXTURE_DYED);
					float[] color = RatColorUtil.getDyeRgb(DyeColor.byId(rat.getDyeColor()));
					this.getParentModel().renderToBuffer(stack, consumer, light, LivingEntityRenderer.getOverlayCoords(rat, 0), color[0], color[1], color[2], 1.0F);
				}
			}

			if (RatUpgradeUtils.hasUpgrade(rat, RatsItemRegistry.RAT_UPGRADE_GOD.get()) && RatConfig.ratGodGlint) {
				VertexConsumer vertexBuilder = ItemRenderer.getFoilBuffer(buffer, RenderType.entityCutoutNoCull(this.getTextureLocation(rat)), false, true);
				this.getParentModel().renderToBuffer(stack, vertexBuilder, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
			}

			RatUpgradeUtils.forEachUpgrade(rat, item -> item instanceof ChangesOverlayUpgrade, upgrade -> {
				RenderType overlay = ((ChangesOverlayUpgrade) upgrade.getItem()).getOverlayTexture(rat, partialTicks);
				if (overlay != null) {
					VertexConsumer consumer = buffer.getBuffer(overlay);
					this.getParentModel().setupAnim(rat, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
					this.getParentModel().renderToBuffer(stack, consumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
				}
			});
		}
	}
}