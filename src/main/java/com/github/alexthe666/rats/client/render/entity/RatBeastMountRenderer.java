package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.model.entity.RatBeastMountModel;
import com.github.alexthe666.rats.client.render.entity.layer.GlowingOverlayLayer;
import com.github.alexthe666.rats.server.entity.mount.RatBeastMount;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RatBeastMountRenderer extends MobRenderer<RatBeastMount, RatBeastMountModel<RatBeastMount>> {

	private static final ResourceLocation BLUE_TEXTURE = new ResourceLocation(RatsMod.MODID, "textures/entity/ratlantis/feral_ratlantean_blue.png");
	private static final ResourceLocation BLACK_TEXTURE = new ResourceLocation(RatsMod.MODID, "textures/entity/ratlantis/feral_ratlantean_black.png");
	private static final ResourceLocation BROWN_TEXTURE = new ResourceLocation(RatsMod.MODID, "textures/entity/ratlantis/feral_ratlantean_brown.png");
	private static final ResourceLocation GREEN_TEXTURE = new ResourceLocation(RatsMod.MODID, "textures/entity/ratlantis/feral_ratlantean_green.png");
	private static final ResourceLocation EYE_TEXTURE = new ResourceLocation(RatsMod.MODID, "textures/entity/plague_beast_eyes.png");

	public RatBeastMountRenderer(EntityRendererProvider.Context context) {
		super(context, new RatBeastMountModel<>(), 0.5F);
		this.addLayer(new GlowingOverlayLayer<>(this, EYE_TEXTURE));
	}

	@Override
	protected void scale(RatBeastMount rat, PoseStack stack, float partialTickTime) {
		stack.scale(1.2F, 1.2F, 1.2F);
	}

	public ResourceLocation getTextureLocation(RatBeastMount entity) {
		return switch (entity.getColorVariant()) {
			case 1 -> BLACK_TEXTURE;
			case 2 -> BROWN_TEXTURE;
			case 3 -> GREEN_TEXTURE;
			default -> BLUE_TEXTURE;
		};
	}
}
