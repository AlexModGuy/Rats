package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.model.entity.RatGolemMountModel;
import com.github.alexthe666.rats.client.render.entity.layer.RatGolemMountCracksLayer;
import com.github.alexthe666.rats.server.entity.mount.RatGolemMount;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RatGolemMountRenderer extends MobRenderer<RatGolemMount, RatGolemMountModel<RatGolemMount>> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(RatsMod.MODID, "textures/entity/rat/mount/golem_mount.png");

	public RatGolemMountRenderer(EntityRendererProvider.Context context) {
		super(context, new RatGolemMountModel<>(), 0.75F);
		this.addLayer(new RatGolemMountCracksLayer(this));
	}

	public ResourceLocation getTextureLocation(RatGolemMount entity) {
		return TEXTURE;
	}
}