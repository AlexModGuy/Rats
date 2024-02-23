package com.github.alexthe666.rats.server.items.upgrades;

import com.github.alexthe666.rats.client.model.entity.RatModel;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.DamageImmunityUpgrade;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.HoldsItemUpgrade;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public abstract class BaseFlightRatUpgradeItem extends BaseRatUpgradeItem implements HoldsItemUpgrade, DamageImmunityUpgrade {

	public BaseFlightRatUpgradeItem(Properties properties, int rarity, int textLines) {
		super(properties, rarity, textLines);
	}

	@Override
	public boolean canFly(TamedRat rat) {
		return true;
	}

	public abstract ItemStack getWing();

	@Override
	public boolean playIdleAnimation(TamedRat rat) {
		return false;
	}

	@Override
	public void renderHeldItem(TamedRat rat, RatModel<?> model, PoseStack stack, MultiBufferSource buffer, int light, float ageInTicks) {
		Minecraft minecraft = Minecraft.getInstance();
		float wingAngle = !rat.isFlying() ? 0 : Mth.sin(ageInTicks) * 30;
		float wingFold = !rat.isFlying() ? -45 : 0;
		model.body1.translateRotate(stack);
		model.body2.translateRotate(stack);
		stack.pushPose();
		stack.translate(0F, -0.1F, 0F);
		stack.mulPose(Axis.ZN.rotationDegrees(wingAngle));
		stack.mulPose(Axis.YP.rotationDegrees(wingFold));
		stack.translate(0.55F, 0, 0.2F);
		stack.mulPose(Axis.XN.rotationDegrees(90));
		stack.scale(2, 2, 1);
		minecraft.getItemRenderer().renderStatic(this.getWing(), ItemDisplayContext.GROUND, light, OverlayTexture.NO_OVERLAY, stack, buffer, null, rat.getId());
		stack.popPose();
		stack.pushPose();
		stack.translate(0F, -0.1F, 0F);
		stack.mulPose(Axis.ZP.rotationDegrees(wingAngle));
		stack.mulPose(Axis.YN.rotationDegrees(wingFold));
		stack.translate(-0.55F, 0.01F, 0.2F);
		stack.mulPose(Axis.XN.rotationDegrees(90));
		stack.mulPose(Axis.YP.rotationDegrees(180));
		stack.scale(2, 2, 1);
		minecraft.getItemRenderer().renderStatic(this.getWing(), ItemDisplayContext.GROUND, light, OverlayTexture.NO_OVERLAY, stack, buffer, null, rat.getId());
		stack.popPose();
	}

	@Override
	public boolean isFakeHandRender() {
		return true;
	}

	@Override
	public boolean isImmuneToDamageSource(TamedRat rat, DamageSource source) {
		return source.is(DamageTypeTags.IS_FALL);
	}
}
