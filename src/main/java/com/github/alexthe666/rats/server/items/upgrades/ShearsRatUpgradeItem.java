package com.github.alexthe666.rats.server.items.upgrades;

import com.github.alexthe666.rats.client.model.entity.RatModel;
import com.github.alexthe666.rats.server.entity.ai.goal.RatUseShearsGoal;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.ChangesAIUpgrade;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.HoldsItemUpgrade;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;

public class ShearsRatUpgradeItem extends BaseRatUpgradeItem implements HoldsItemUpgrade, ChangesAIUpgrade {
	public ShearsRatUpgradeItem(Properties properties) {
		super(properties, 0, 1);
	}

	@Override
	public boolean playIdleAnimation(TamedRat rat) {
		return false;
	}

	@Override
	public List<Goal> addNewWorkGoals(TamedRat rat) {
		return List.of(new RatUseShearsGoal(rat));
	}

	@Override
	public void renderHeldItem(TamedRat rat, RatModel<?> model, PoseStack stack, MultiBufferSource buffer, int light, float ageInTicks) {
		stack.pushPose();
		this.translateToHand(model, false, stack);
		stack.mulPose(Axis.ZN.rotationDegrees(90.0F));
		stack.mulPose(Axis.YP.rotationDegrees(15.0F));
		stack.mulPose(Axis.XN.rotationDegrees(90.0F));
		stack.translate(0.1F, 0.0F, -0.075F);
		Minecraft.getInstance().getItemRenderer().renderStatic(new ItemStack(Items.SHEARS), ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, light, OverlayTexture.NO_OVERLAY, stack, buffer, null, rat.getId());
		stack.popPose();
	}
}
