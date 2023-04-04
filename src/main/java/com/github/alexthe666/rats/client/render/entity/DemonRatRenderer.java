package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.rat.DemonRat;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;

public class DemonRatRenderer extends AbstractRatRenderer<DemonRat> {

	public static final ResourceLocation BASE_TEXTURE = new ResourceLocation(RatsMod.MODID, "textures/entity/rat/demon_rat.png");

	public DemonRatRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.addLayer(new EyesLayer<>(this) {
			@Override
			public RenderType renderType() {
				return RenderType.eyes(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/demon_rat_eye.png"));
			}
		});
	}

	@Override
	protected void scale(DemonRat rat, PoseStack stack, float partialTicks) {
		stack.scale(1.5F, 1.5F, 1.5F);
		super.scale(rat, stack, partialTicks);
	}

	@Override
	public ResourceLocation getTextureLocation(DemonRat entity) {
		return BASE_TEXTURE;
	}
}