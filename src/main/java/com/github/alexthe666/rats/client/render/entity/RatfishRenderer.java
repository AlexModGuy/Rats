package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.model.entity.RatFishModel;
import com.github.alexthe666.rats.server.entity.ratlantis.Ratfish;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class RatfishRenderer extends MobRenderer<Ratfish, RatFishModel<Ratfish>> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(RatsMod.MODID, "textures/entity/ratlantis/ratfish.png");

	public RatfishRenderer(EntityRendererProvider.Context context) {
		super(context, new RatFishModel<>(0), 0.3F);
	}

	@Override
	public ResourceLocation getTextureLocation(Ratfish entity) {
		return TEXTURE;
	}

	protected void setupRotations(Ratfish entityLiving, PoseStack stack, float ageInTicks, float yRot, float partialTicks) {
		super.setupRotations(entityLiving, stack, ageInTicks, yRot, partialTicks);
		float f = 1.0F;
		float f1 = 1.0F;
		if (!entityLiving.isInWater()) {
			f = 1.3F;
			f1 = 1.7F;
		}

		float f2 = f * 4.3F * Mth.sin(f1 * 0.6F * ageInTicks);
		stack.mulPose(Axis.YP.rotationDegrees(f2));
		stack.translate(0.0D, 0.0D, -0.4F);
		if (!entityLiving.isInWater()) {
			stack.translate(0.2F, 0.1F, 0.0D);
			stack.mulPose(Axis.ZP.rotationDegrees(90.0F));
		}
	}
}
