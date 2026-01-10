package com.github.alexthe666.rats.client.render.entity.layer;

import com.github.alexthe666.rats.client.model.entity.AbstractRatModel;
import com.github.alexthe666.rats.client.render.RatsRenderType;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.ChangesTextureUpgrade;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.GlowingEyesUpgrade;
import com.github.alexthe666.rats.server.misc.RatUpgradeUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class TamedRatEyesLayer extends RatEyesLayer<TamedRat, AbstractRatModel<TamedRat>> {
	public TamedRatEyesLayer(RenderLayerParent<TamedRat, AbstractRatModel<TamedRat>> parent) {
		super(parent);
	}

	@Override
	public void render(PoseStack stack, MultiBufferSource buffer, int light, TamedRat rat, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if (RatUpgradeUtils.forEachUpgradeBool(rat, item -> item instanceof GlowingEyesUpgrade, false)) {
			AtomicBoolean skip = new AtomicBoolean(false);
			AtomicReference<ResourceLocation> tex = new AtomicReference<>(EYES_TEXTURE);
			RatUpgradeUtils.forEachUpgrade(rat, item -> item instanceof GlowingEyesUpgrade, (stack1, slot) -> {
				if (rat.isSlotVisible(slot)) {
					tex.set(((GlowingEyesUpgrade) stack1.getItem()).getEyeTexture(stack1));
				} else {
					skip.set(true);
				}
			});

			if (!skip.get()) {
				if (tex.get() != null || RatUpgradeUtils.forEachUpgradeBool(rat, upgrade -> upgrade instanceof ChangesTextureUpgrade eyeTex && eyeTex.makesEyesGlowByDefault(), false)) {
					VertexConsumer consumer = buffer.getBuffer(RatsRenderType.getEyesAlphaEnabled(tex.get()));
					this.getParentModel().renderToBuffer(stack, consumer, light, OverlayTexture.NO_OVERLAY, -1);
				}
			}
		} else {
			super.render(stack, buffer, light, rat, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
		}
	}
}







