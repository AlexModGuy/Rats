package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.registry.RatlantisItemRegistry;
import com.github.alexthe666.rats.server.entity.projectile.DutchratSword;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class DutchratSwordRenderer extends EntityRenderer<DutchratSword> {

	private static final ItemStack PIRAT_SWORD = new ItemStack(RatlantisItemRegistry.GHOST_PIRAT_CUTLASS.get());

	public DutchratSwordRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	public void render(DutchratSword entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light) {
		stack.pushPose();
		stack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) - 90.0F));
		stack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot())));
		stack.translate(0.0F, 0.5F, 0.0F);
		stack.scale(3.0F, 3.0F, 3.0F);
		stack.mulPose(Axis.YP.rotationDegrees(90.0F));
		stack.mulPose(Axis.ZP.rotationDegrees((entity.tickCount + partialTicks) * 10.0F));
		stack.translate(0.0F, -0.15F, 0.0F);
		Minecraft.getInstance().getItemRenderer().renderStatic(PIRAT_SWORD, ItemDisplayContext.GROUND, light, OverlayTexture.NO_OVERLAY, stack, buffer, null, 0);
		stack.popPose();
	}

	@Override
	public ResourceLocation getTextureLocation(DutchratSword entity) {
		return InventoryMenu.BLOCK_ATLAS;
	}
}
