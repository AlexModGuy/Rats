package com.github.alexthe666.rats.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class NothingRenderer extends EntityRenderer<Entity> {

	public NothingRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	public void render(Entity entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light) {
	}

	@Override
	public ResourceLocation getTextureLocation(Entity entity) {
		return null;
	}
}
