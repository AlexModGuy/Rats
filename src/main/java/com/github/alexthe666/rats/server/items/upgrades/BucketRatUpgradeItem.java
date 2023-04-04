package com.github.alexthe666.rats.server.items.upgrades;

import com.github.alexthe666.rats.client.model.entity.RatModel;
import com.github.alexthe666.rats.server.entity.ai.goal.RatDepositGoal;
import com.github.alexthe666.rats.server.entity.ai.goal.RatPickupGoal;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.ChangesAIUpgrade;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.HoldsItemUpgrade;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

import java.util.List;

public class BucketRatUpgradeItem extends BaseRatUpgradeItem implements HoldsItemUpgrade, ChangesAIUpgrade {

	private final int fluidAmount;

	public BucketRatUpgradeItem(Item.Properties properties, int rarity, int textLength, int fluidAmount) {
		super(properties, rarity, textLength);
		this.fluidAmount = fluidAmount;
	}

	public static ItemStack getBucketFromFluid(FluidStack ingredient) {
		if (ingredient.isEmpty()) {
			return new ItemStack(Items.BUCKET);
		}
		return FluidUtil.getFilledBucket(ingredient);
	}

	public int getMbTransferRate() {
		return this.fluidAmount;
	}

	@Override
	public void renderHeldItem(TamedRat rat, RatModel<?> model, PoseStack stack, MultiBufferSource buffer, int light, float ageInTicks) {
		stack.pushPose();
		if (model.young) {
			stack.translate(0.0F, 0.625F, 0.0F);
			stack.mulPose(Axis.XP.rotationDegrees(20));
			stack.scale(0.5F, 0.5F, 0.5F);
		}
		Minecraft minecraft = Minecraft.getInstance();
		this.translateToHand(model, true, stack);
		stack.mulPose(Axis.ZP.rotationDegrees(190));
		stack.mulPose(Axis.YP.rotationDegrees(180));
		stack.mulPose(Axis.XN.rotationDegrees(40));

		stack.translate(-0.155F, -0.25F, 0.0F);
		stack.scale(1.25F, 1.25F, 1.25F);
		minecraft.getItemRenderer().renderStatic(getBucketFromFluid(rat.transportingFluid), ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, light, OverlayTexture.NO_OVERLAY, stack, buffer, null, rat.getId());
		stack.popPose();
	}

	@Override
	public List<Goal> addNewWorkGoals(TamedRat rat) {
		return List.of(new RatPickupGoal(rat, RatPickupGoal.PickupType.FLUID), new RatDepositGoal(rat, RatDepositGoal.DepositType.FLUID));
	}
}
