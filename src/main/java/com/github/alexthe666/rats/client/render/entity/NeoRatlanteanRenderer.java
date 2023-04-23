package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.model.entity.NeoRatlanteanModel;
import com.github.alexthe666.rats.client.render.entity.layer.GlowingOverlayLayer;
import com.github.alexthe666.rats.server.entity.monster.boss.NeoRatlantean;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class NeoRatlanteanRenderer extends MobRenderer<NeoRatlantean, NeoRatlanteanModel<NeoRatlantean>> {

	private static final ResourceLocation BLUE_TEXTURE = new ResourceLocation(RatsMod.MODID, "textures/entity/neo_ratlantean/neo_ratlantean_blue.png");
	private static final ResourceLocation BLACK_TEXTURE = new ResourceLocation(RatsMod.MODID, "textures/entity/neo_ratlantean/neo_ratlantean_black.png");
	private static final ResourceLocation BROWN_TEXTURE = new ResourceLocation(RatsMod.MODID, "textures/entity/neo_ratlantean/neo_ratlantean_brown.png");
	private static final ResourceLocation GREEN_TEXTURE = new ResourceLocation(RatsMod.MODID, "textures/entity/neo_ratlantean/neo_ratlantean_green.png");
	private static final ResourceLocation GLOW_TEXTURE = new ResourceLocation(RatsMod.MODID, "textures/entity/neo_ratlantean/neo_ratlantean_glow.png");

	public NeoRatlanteanRenderer(EntityRendererProvider.Context context) {
		super(context, new NeoRatlanteanModel<>(), 0.65F);
		this.addLayer(new GlowingOverlayLayer<>(this, GLOW_TEXTURE));
	}

	public ResourceLocation getTextureLocation(NeoRatlantean entity) {
		return switch (entity.getColorVariant()) {
			case 1 -> BLACK_TEXTURE;
			case 2 -> BROWN_TEXTURE;
			case 3 -> GREEN_TEXTURE;
			default -> BLUE_TEXTURE;
		};
	}
}
