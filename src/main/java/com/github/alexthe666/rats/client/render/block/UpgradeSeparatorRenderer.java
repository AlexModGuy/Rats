package com.github.alexthe666.rats.client.render.block;

import com.github.alexthe666.rats.registry.RatlantisItemRegistry;
import com.github.alexthe666.rats.server.block.entity.UpgradeSeparatorBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class UpgradeSeparatorRenderer implements BlockEntityRenderer<UpgradeSeparatorBlockEntity> {
	private static final ItemStack RENDER_STACK = new ItemStack(RatlantisItemRegistry.ANCIENT_SAWBLADE.get());

	public UpgradeSeparatorRenderer(BlockEntityRendererProvider.Context context) {
	}

	@Override
	public void render(UpgradeSeparatorBlockEntity entity, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light, int overlay) {
		stack.pushPose();
		stack.translate(0.5D, 0.15D, 0.5D);
		float f = entity.ratRotationPrev + Minecraft.getInstance().getPartialTick();
		stack.translate(0.0F, 1F + Mth.sin(f * 0.1F) * 0.1F, 0.0F);
		float f1;

		for (f1 = entity.ratRotation - entity.ratRotationPrev; f1 >= (float) Math.PI; f1 -= ((float) Math.PI * 2F)) {
		}

		while (f1 < -(float) Math.PI) {
			f1 += ((float) Math.PI * 2F);
		}
		float f2 = entity.ratRotationPrev + f1 * Minecraft.getInstance().getPartialTick();
		stack.mulPose(Axis.YP.rotationDegrees(-f2 * 0.1F * (180F / (float) Math.PI)));
		stack.mulPose(Axis.ZP.rotationDegrees(180));
		stack.mulPose(Axis.XP.rotationDegrees(90));
		Minecraft.getInstance().getItemRenderer().renderStatic(RENDER_STACK, ItemDisplayContext.FIXED, light, overlay, stack, buffer, null, 0);
		stack.popPose();
	}
}
