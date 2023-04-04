package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.model.entity.RattlingGunBaseModel;
import com.github.alexthe666.rats.client.model.entity.RattlingGunModel;
import com.github.alexthe666.rats.server.entity.ratlantis.RattlingGun;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class RattlingGunRenderer extends EntityRenderer<RattlingGun> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(RatsMod.MODID, "textures/entity/rattling_gun.png");
	private static final ResourceLocation TEXTURE_FIRING = new ResourceLocation(RatsMod.MODID, "textures/entity/rattling_gun_firing.png");
	public static final RattlingGunModel<RattlingGun> GUN_MODEL = new RattlingGunModel<>();
	public static final RattlingGunBaseModel<RattlingGun> GUN_BASE_MODEL = new RattlingGunBaseModel<>();

	public RattlingGunRenderer(EntityRendererProvider.Context context) {
		super(context);
	}


	public void render(RattlingGun entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light) {
		VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
		stack.pushPose();
		stack.pushPose();
		stack.translate(0, 1.5F, 0);
		stack.mulPose(Axis.XP.rotationDegrees(180));
		GUN_BASE_MODEL.renderToBuffer(stack, consumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		stack.mulPose(Axis.YP.rotationDegrees(entity.getYRot()));
		GUN_MODEL.resetToDefaultPose();
		if (!entity.isFiring()) {
			GUN_MODEL.gun1.rotateAngleZ = 0;
			GUN_MODEL.handle1.rotateAngleX = 0;
		} else {
			GUN_MODEL.setupAnim(entity, 0, 0, entity.tickCount + partialTicks, 0, 0);
		}

		GUN_MODEL.renderToBuffer(stack, consumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		stack.popPose();

		if (entity.isFiring()) {
			stack.pushPose();
			stack.translate(0, 1.6F, 0);
			stack.mulPose(Axis.XP.rotationDegrees(180));
			VertexConsumer ivertexbuilder2 = buffer.getBuffer(RenderType.eyes(TEXTURE_FIRING));
			stack.mulPose(Axis.YP.rotationDegrees(entity.getYRot()));
			GUN_MODEL.setupAnim(entity, 0, 0, entity.tickCount + partialTicks, 0, 0);
			GUN_MODEL.renderToBuffer(stack, ivertexbuilder2, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
			stack.popPose();
		}
		stack.popPose();
	}

	public ResourceLocation getTextureLocation(RattlingGun entity) {
		return TEXTURE;
	}
}
