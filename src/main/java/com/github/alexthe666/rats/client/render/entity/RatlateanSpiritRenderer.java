package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.model.entity.RatlanteanSpiritModel;
import com.github.alexthe666.rats.client.render.entity.layer.GlowingOverlayLayer;
import com.github.alexthe666.rats.server.entity.monster.PlagueCloud;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;

public class RatlateanSpiritRenderer<T extends Mob> extends MobRenderer<T, RatlanteanSpiritModel<T>> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(RatsMod.MODID, "textures/entity/rat/ratlantean_spirit.png");
	private static final ResourceLocation TEXTURE_CLOUD = new ResourceLocation(RatsMod.MODID, "textures/entity/plague_cloud.png");

	public RatlateanSpiritRenderer(EntityRendererProvider.Context context, boolean cloud) {
		super(context, new RatlanteanSpiritModel<>(), 0.5F);
		this.addLayer(new GlowingOverlayLayer<>(this, cloud ? TEXTURE_CLOUD : TEXTURE));

	}

	public ResourceLocation getTextureLocation(T entity) {
		return entity instanceof PlagueCloud ? TEXTURE_CLOUD : TEXTURE;
	}

	protected void scale(T living, PoseStack stack, float partialTickTime) {
		float scale = living instanceof PlagueCloud ? 2 : 1.5F;
		stack.scale(scale, scale, scale);
	}
}