package com.github.alexthe666.rats.server.items.upgrades;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.model.entity.RatModel;
import com.github.alexthe666.rats.server.entity.ai.goal.harvest.RatHarvestTreesGoal;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.ChangesAIUpgrade;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.ChangesOverlayUpgrade;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.DamageImmunityUpgrade;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.HoldsItemUpgrade;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;

public class LumberjackRatUpgradeItem extends BaseRatUpgradeItem implements DamageImmunityUpgrade, ChangesAIUpgrade, HoldsItemUpgrade, ChangesOverlayUpgrade {
	public LumberjackRatUpgradeItem(Properties properties) {
		super(properties, 1, 2);
	}

	@Override
	public RenderType getOverlayTexture(ItemStack stack, TamedRat rat, float partialTicks) {
		return RenderType.entitySmoothCutout(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/upgrades/lumberjack.png"));
	}

	@Override
	public void renderHeldItem(TamedRat rat, RatModel<?> model, PoseStack stack, MultiBufferSource buffer, int light, float ageInTicks) {
		stack.pushPose();
		this.translateToHand(model, false, stack);
		stack.mulPose(Axis.ZP.rotationDegrees(180F));
		stack.translate(0.0F, -0.075F, -0.1F);
		stack.scale(0.65F, 0.65F, 0.65F);
		Minecraft.getInstance().getItemRenderer().renderStatic(new ItemStack(Items.IRON_AXE), ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, light, OverlayTexture.NO_OVERLAY, stack, buffer, null, rat.getId());
		stack.popPose();
	}

	@Override
	public boolean playIdleAnimation(TamedRat rat) {
		return false;
	}

	@Override
	public List<Goal> addNewWorkGoals(TamedRat rat) {
		return List.of(new RatHarvestTreesGoal(rat));
	}

	@Override
	public boolean isImmuneToDamageSource(TamedRat rat, DamageSource source) {
		return source.is(DamageTypes.IN_WALL);
	}
}
