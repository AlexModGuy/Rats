package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.model.entity.BiplaneModel;
import com.github.alexthe666.rats.server.entity.monster.boss.RatBaronPlane;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RatBaronPlaneRenderer extends MobRenderer<RatBaronPlane, BiplaneModel<RatBaronPlane>> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(RatsMod.MODID, "textures/entity/rat_baron_plane.png");

	public RatBaronPlaneRenderer(EntityRendererProvider.Context context) {
		super(context, new BiplaneModel<>(), 1.65F);
	}

	public ResourceLocation getTextureLocation(RatBaronPlane entity) {
		return TEXTURE;
	}
}
