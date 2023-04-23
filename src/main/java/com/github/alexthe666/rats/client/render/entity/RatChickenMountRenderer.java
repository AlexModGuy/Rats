package com.github.alexthe666.rats.client.render.entity;


import com.github.alexthe666.rats.server.entity.mount.RatChickenMount;
import net.minecraft.client.model.ChickenModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class RatChickenMountRenderer extends MobRenderer<RatChickenMount, ChickenModel<RatChickenMount>> {
	private static final ResourceLocation CHICKEN_TEXTURES = new ResourceLocation("textures/entity/chicken.png");

	public RatChickenMountRenderer(EntityRendererProvider.Context context) {
		super(context, new ChickenModel<>(context.bakeLayer(ModelLayers.CHICKEN)), 0.3F);
	}

	public ResourceLocation getTextureLocation(RatChickenMount entity) {
		return CHICKEN_TEXTURES;
	}

	protected float getBob(RatChickenMount livingBase, float partialTicks) {
		float f = Mth.lerp(partialTicks, livingBase.oFlap, livingBase.wingRotation);
		float f1 = Mth.lerp(partialTicks, livingBase.oFlapSpeed, livingBase.destPos);
		return (Mth.sin(f) + 1.0F) * f1;
	}
}