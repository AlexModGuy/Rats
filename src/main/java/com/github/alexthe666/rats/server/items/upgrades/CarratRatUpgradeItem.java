package com.github.alexthe666.rats.server.items.upgrades;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.model.entity.RatModel;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.ChangesTextureUpgrade;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.HoldsItemUpgrade;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

public class CarratRatUpgradeItem extends BaseRatUpgradeItem implements ChangesTextureUpgrade, HoldsItemUpgrade {
	public CarratRatUpgradeItem(Properties properties) {
		super(properties, 1, 1);
	}

	@Override
	public ResourceLocation getTexture() {
		return new ResourceLocation(RatsMod.MODID, "textures/entity/rat/rat_carrat_upgrade.png");
	}

	@Override
	public boolean makesEyesGlowByDefault() {
		return false;
	}

	@Override
	public boolean isFakeHandRender() {
		return true;
	}

	@Override
	public void renderHeldItem(TamedRat rat, RatModel<?> model, PoseStack stack, MultiBufferSource buffer, int light, float ageInTicks) {
		stack.pushPose();
		model.body1.translateRotate(stack);
		stack.pushPose();
		stack.translate(0F, -0.05F, 0.5F);
		stack.mulPose(Axis.XP.rotationDegrees(90.0F));
		stack.scale(2, 2, 2);
		Minecraft.getInstance().getItemRenderer().renderStatic(new ItemStack(Blocks.FERN), ItemDisplayContext.GROUND, light, OverlayTexture.NO_OVERLAY, stack, buffer, null, rat.getId());
		stack.popPose();
		stack.popPose();
	}
}
