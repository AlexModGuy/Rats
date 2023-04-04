package com.github.alexthe666.rats.server.items.upgrades;

import com.github.alexthe666.rats.client.model.entity.RatModel;
import com.github.alexthe666.rats.server.entity.ai.goal.RatRangedAttackGoal;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.ChangesAIUpgrade;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.HoldsItemUpgrade;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;

public class RangedWeaponRatUpgradeItem extends BaseRatUpgradeItem implements HoldsItemUpgrade, ChangesAIUpgrade {

	private final boolean crossbow;

	public RangedWeaponRatUpgradeItem(Properties properties, boolean crossbow) {
		super(properties, 0, crossbow ? 2 : 1);
		this.crossbow = crossbow;
	}

	@Override
	public List<Goal> addNewWorkGoals(TamedRat rat) {
		return List.of(new RatRangedAttackGoal(rat, 1.0D, 20, 15.0F));
	}

	@Override
	public void renderHeldItem(TamedRat rat, RatModel<?> model, PoseStack stack, MultiBufferSource buffer, int light, float ageInTicks) {
		this.translateToHand(model, false, stack);
		stack.translate(0.01D, 0.1D, -0.02D);
		stack.scale(0.5F, 0.5F, 0.5F);
		Minecraft.getInstance().getItemRenderer().renderStatic(new ItemStack(this.crossbow ? Items.CROSSBOW : Items.BOW), ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, light, OverlayTexture.NO_OVERLAY, stack, buffer, null, rat.getId());
	}
}
