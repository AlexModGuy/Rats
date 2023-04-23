package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.model.entity.FeralRatlanteanModel;
import com.github.alexthe666.rats.client.render.entity.layer.BasicOverlayLayer;
import com.github.alexthe666.rats.client.render.entity.layer.GlowingOverlayLayer;
import com.github.alexthe666.rats.server.entity.monster.FeralRatlantean;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class FeralRatlanteanRenderer extends MobRenderer<FeralRatlantean, FeralRatlanteanModel<FeralRatlantean>> {
	private static final ResourceLocation BLUE_TEXTURE = new ResourceLocation(RatsMod.MODID, "textures/entity/beasts/feral_ratlantean_blue.png");
	private static final ResourceLocation BLACK_TEXTURE = new ResourceLocation(RatsMod.MODID, "textures/entity/beasts/feral_ratlantean_black.png");
	private static final ResourceLocation BROWN_TEXTURE = new ResourceLocation(RatsMod.MODID, "textures/entity/beasts/feral_ratlantean_brown.png");
	private static final ResourceLocation GREEN_TEXTURE = new ResourceLocation(RatsMod.MODID, "textures/entity/beasts/feral_ratlantean_green.png");
	private static final ResourceLocation CLOTHES = new ResourceLocation(RatsMod.MODID, "textures/entity/beasts/feral_ratlantean_clothes.png");
	private static final ResourceLocation EYES = new ResourceLocation(RatsMod.MODID, "textures/entity/beasts/feral_ratlantean_eyes.png");

	public FeralRatlanteanRenderer(EntityRendererProvider.Context context) {
		super(context, new FeralRatlanteanModel<>(), 0.5F);
		this.addLayer(new BasicOverlayLayer<>(this, CLOTHES));
		this.addLayer(new GlowingOverlayLayer<>(this, EYES));
	}

	protected void scale(FeralRatlantean rat, PoseStack stack, float partialTickTime) {
		stack.scale(1.2F, 1.2F, 1.2F);
	}

	public ResourceLocation getTextureLocation(FeralRatlantean entity) {
		return switch (entity.getColorVariant()) {
			case 1 -> BLACK_TEXTURE;
			case 2 -> BROWN_TEXTURE;
			case 3 -> GREEN_TEXTURE;
			default -> BLUE_TEXTURE;
		};
	}
}
