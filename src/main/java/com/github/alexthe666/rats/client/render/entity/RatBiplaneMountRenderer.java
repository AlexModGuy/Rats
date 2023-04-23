package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.model.entity.BiplaneModel;
import com.github.alexthe666.rats.server.entity.mount.RatBiplaneMount;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RatBiplaneMountRenderer extends MobRenderer<RatBiplaneMount, BiplaneModel<RatBiplaneMount>> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(RatsMod.MODID, "textures/entity/rat/rat_biplane_upgrade.png");

	public RatBiplaneMountRenderer(EntityRendererProvider.Context context) {
		super(context, new BiplaneModel<>(), 1.65F);
	}

	public ResourceLocation getTextureLocation(RatBiplaneMount entity) {
		return TEXTURE;
	}
}
