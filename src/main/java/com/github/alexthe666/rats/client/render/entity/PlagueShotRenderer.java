package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.model.entity.RatlanteanSpiritModel;
import com.github.alexthe666.rats.server.entity.projectile.PlagueShot;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class PlagueShotRenderer extends EntityRenderer<PlagueShot> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(RatsMod.MODID, "textures/entity/plague_cloud.png");
	private static final RatlanteanSpiritModel<PlagueShot> MODEL_SPIRIT = new RatlanteanSpiritModel<>();

	public PlagueShotRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	public void render(PlagueShot entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light) {
		stack.pushPose();
		stack.scale(1.5F, -1.5F, 1.5F);
		stack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) + 180.0F));
		stack.mulPose(Axis.XP.rotationDegrees(-Mth.lerp(partialTicks, entity.xRotO, entity.getXRot())));
		stack.translate(0F, -1.5F, 0F);
		VertexConsumer consumer = ItemRenderer.getFoilBuffer(buffer, RenderType.entityCutoutNoCull(TEXTURE), false, true);
		MODEL_SPIRIT.renderToBuffer(stack, consumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		stack.popPose();

		super.render(entity, entityYaw, partialTicks, stack, buffer, light);
	}

	@Override
	public ResourceLocation getTextureLocation(PlagueShot entity) {
		return TEXTURE;
	}
}
