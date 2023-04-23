package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.model.entity.FeralRatlanteanModel;
import com.github.alexthe666.rats.client.render.entity.layer.BasicOverlayLayer;
import com.github.alexthe666.rats.client.render.entity.layer.GlowingOverlayLayer;
import com.github.alexthe666.rats.server.entity.monster.PlagueBeast;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class PlagueBeastRenderer extends MobRenderer<PlagueBeast, FeralRatlanteanModel<PlagueBeast>> {

	private static final ResourceLocation BLUE_TEXTURE = new ResourceLocation(RatsMod.MODID, "textures/entity/beasts/feral_ratlantean_blue.png");
	private static final ResourceLocation BLACK_TEXTURE = new ResourceLocation(RatsMod.MODID, "textures/entity/beasts/feral_ratlantean_black.png");
	private static final ResourceLocation BROWN_TEXTURE = new ResourceLocation(RatsMod.MODID, "textures/entity/beasts/feral_ratlantean_brown.png");
	private static final ResourceLocation GREEN_TEXTURE = new ResourceLocation(RatsMod.MODID, "textures/entity/beasts/feral_ratlantean_green.png");
	private static final ResourceLocation PLAGUE_TEXTURE = new ResourceLocation(RatsMod.MODID, "textures/entity/beasts/plague_beast_overlay.png");
	private static final ResourceLocation EYE_TEXTURE = new ResourceLocation(RatsMod.MODID, "textures/entity/beasts/plague_beast_eyes.png");

	public PlagueBeastRenderer(EntityRendererProvider.Context context) {
		super(context, new FeralRatlanteanModel<>(), 0.5F);
		this.addLayer(new BasicOverlayLayer<>(this, PLAGUE_TEXTURE));
		this.addLayer(new GlowingOverlayLayer<>(this, EYE_TEXTURE));
	}

	protected void scale(PlagueBeast rat, PoseStack stack, float partialTickTime) {

		stack.scale(1.2F, 1.2F, 1.2F);
	}

	public ResourceLocation getTextureLocation(PlagueBeast entity) {
		return switch (entity.getColorVariant()) {
			case 1 -> BLACK_TEXTURE;
			case 2 -> BROWN_TEXTURE;
			case 3 -> GREEN_TEXTURE;
			default -> BLUE_TEXTURE;
		};
	}
}
