package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.model.entity.RatlanteanAutomatonModel;
import com.github.alexthe666.rats.client.render.entity.layer.GlowingOverlayLayer;
import com.github.alexthe666.rats.server.entity.ratlantis.RatAutomatonMount;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class RatAutomatonMountRenderer extends MobRenderer<RatAutomatonMount, RatlanteanAutomatonModel<RatAutomatonMount>> {

	private static final ResourceLocation MARBLED_CHEESE_GOLEM_TEXTURE = new ResourceLocation(RatsMod.MODID, "textures/entity/ratlantis/marble_cheese_golem.png");
	private static final ResourceLocation GLOW_TEXTURE = new ResourceLocation(RatsMod.MODID, "textures/entity/ratlantis/marble_cheese_golem_glow.png");

	public RatAutomatonMountRenderer(EntityRendererProvider.Context context) {
		super(context, new RatlanteanAutomatonModel<>(true), 0.95F);
		this.addLayer(new GlowingOverlayLayer<>(this, GLOW_TEXTURE));
	}

	public Vec3 getRenderOffset(RatAutomatonMount rat, float partialTickTime) {
		return new Vec3(0, 0.35F, 0);
	}

	public ResourceLocation getTextureLocation(RatAutomatonMount entity) {
		return MARBLED_CHEESE_GOLEM_TEXTURE;
	}
}
