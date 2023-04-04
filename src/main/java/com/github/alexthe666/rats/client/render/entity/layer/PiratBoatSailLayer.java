package com.github.alexthe666.rats.client.render.entity.layer;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.model.entity.PiratCannonModel;
import com.github.alexthe666.rats.server.entity.ratlantis.PiratBoat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;

public class PiratBoatSailLayer<T extends PiratBoat, M extends EntityModel<T>> extends RenderLayer<T, M> {
	public static final PiratCannonModel<?> MODEL_PIRAT_CANNON = new PiratCannonModel<>();
	public static final ResourceLocation TEXTURE_PIRATE_CANNON = new ResourceLocation(RatsMod.MODID, "textures/entity/ratlantis/pirat_cannon.png");
	public static final ResourceLocation TEXTURE_PIRATE_CANNON_FIRE = new ResourceLocation(RatsMod.MODID, "textures/entity/ratlantis/pirat_cannon_fire.png");

	public PiratBoatSailLayer(RenderLayerParent<T, M> parent) {
		super(parent);
	}

	@Override
	public void render(PoseStack stack, MultiBufferSource buffer, int light, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		stack.pushPose();
		stack.mulPose(Axis.XP.rotationDegrees(180));
		stack.mulPose(Axis.YP.rotationDegrees(90));
		stack.translate(0F, -0.8F, -0.9F);
		stack.scale(4F, 4F, 4F);
		Minecraft.getInstance().getItemRenderer().renderStatic(entity.banner, ItemDisplayContext.GROUND, light, OverlayTexture.NO_OVERLAY, stack, buffer, null, 0);
		stack.popPose();

		stack.pushPose();
		stack.pushPose();
		stack.mulPose(Axis.YN.rotationDegrees(90));
		stack.translate(0, 0.1F, -0.6F);
		stack.scale(0.75F, 0.75F, 0.75F);
		MODEL_PIRAT_CANNON.renderToBuffer(stack, buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE_PIRATE_CANNON)), light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		stack.popPose();

		if (entity.isFiring()) {
			stack.pushPose();
			stack.mulPose(Axis.YN.rotationDegrees(90));
			stack.translate(0, 0.1F, -0.6F);
			stack.scale(0.75F, 0.75F, 0.75F);
			MODEL_PIRAT_CANNON.renderToBuffer(stack, buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE_PIRATE_CANNON_FIRE)), 240, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
			stack.popPose();
		}
		stack.popPose();
	}
}
