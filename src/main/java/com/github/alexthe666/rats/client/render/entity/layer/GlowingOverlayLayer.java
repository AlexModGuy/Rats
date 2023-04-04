package com.github.alexthe666.rats.client.render.entity.layer;

import com.github.alexthe666.rats.client.model.entity.RatlanteanAutomatonModel;
import com.github.alexthe666.rats.registry.RatlantisItemRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class GlowingOverlayLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
	private final RenderType renderType;

	public GlowingOverlayLayer(RenderLayerParent<T, M> parent, ResourceLocation texture) {
		super(parent);
		this.renderType = RenderType.eyes(texture);
	}

	@Override
	public void render(PoseStack stack, MultiBufferSource buffer, int light, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		VertexConsumer consumer = buffer.getBuffer(this.renderType);
		this.getParentModel().renderToBuffer(stack, consumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

		if (this.getParentModel() instanceof RatlanteanAutomatonModel<?> automaton) {
			stack.pushPose();
			automaton.upperbody.translateAndRotate(stack);
			automaton.armLeft1.translateAndRotate(stack);
			automaton.armLeft2.translateAndRotate(stack);
			automaton.drillArm1.translateAndRotate(stack);
			automaton.drillArm2.translateAndRotate(stack);
			automaton.drilArm3.translateAndRotate(stack);
			stack.translate(-0.05D, -0.975D, 0.3D);
			automaton.blade.translateAndRotate(stack);
			stack.mulPose(Axis.YP.rotationDegrees(90));
			Minecraft.getInstance().getItemRenderer().renderStatic(new ItemStack(RatlantisItemRegistry.ANCIENT_SAWBLADE.get()), ItemDisplayContext.FIXED, light, OverlayTexture.NO_OVERLAY, stack, buffer, null, 0);
			stack.popPose();
		}
	}
}
