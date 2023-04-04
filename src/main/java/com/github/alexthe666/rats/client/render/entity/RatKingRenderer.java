package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.model.EmptyModel;
import com.github.alexthe666.rats.client.render.entity.layer.RatKingLayer;
import com.github.alexthe666.rats.server.entity.RatKing;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class RatKingRenderer extends MobRenderer<RatKing, EmptyModel<RatKing>> {

	private static final ResourceLocation TEXTURE_1 = new ResourceLocation(RatsMod.MODID, "textures/entity/rat/rat_black.png");

	public RatKingRenderer(EntityRendererProvider.Context context) {
		super(context, new EmptyModel<>(), 1.0F);
		this.addLayer(new RatKingLayer(this));
	}

	@Override
	public Vec3 getRenderOffset(RatKing king, float partialTicks) {
		return new Vec3(0.0F, 0.0F, 0.0F);
	}

	@Override
	protected void setupRotations(RatKing king, PoseStack stack, float ageInTicks, float yRot, float partialTicks) {
		if (king.hasCustomName()) {
			String s = ChatFormatting.stripFormatting(king.getName().getString());
			if (("Dinnerbone".equals(s) || "Grumm".equals(s))) {
				stack.translate(0.0D, king.getBbHeight() + 0.1F, 0.0D);
				stack.mulPose(Axis.ZP.rotationDegrees(180.0F));
			}
		}
	}

	@Override
	public ResourceLocation getTextureLocation(RatKing entity) {
		return TEXTURE_1;
	}
}
