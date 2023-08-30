package com.github.alexthe666.rats.server.items.upgrades;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.ai.goal.RatDepositGoal;
import com.github.alexthe666.rats.server.entity.ai.goal.RatPickupGoal;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.ChangesAIUpgrade;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.ChangesOverlayUpgrade;
import com.github.alexthe666.rats.server.misc.RatsLangConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EnergyRatUpgradeItem extends BaseRatUpgradeItem implements ChangesOverlayUpgrade, ChangesAIUpgrade {

	private final int transferRate;

	public EnergyRatUpgradeItem(Item.Properties properties, int rarity, int transferRate) {
		super(properties, rarity, 0);
		this.transferRate = transferRate;
	}

	public int getRFTransferRate() {
		return this.transferRate;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, level, tooltip, flag);
		tooltip.add(Component.translatable(RatsLangConstants.RAT_UPGRADE_ENERGY_DESC0).withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.translatable(RatsLangConstants.RAT_UPGRADE_ENERGY_DESC1).withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.translatable(RatsLangConstants.RAT_UPGRADE_ENERGY_TRANSFER, this.transferRate).withStyle(ChatFormatting.GRAY));
	}

	@Override
	public @Nullable RenderType getOverlayTexture(TamedRat rat, float partialTicks) {
		float f = (float) rat.tickCount + partialTicks;
		return rat.getHeldRF() > 0 ? RenderType.energySwirl(new ResourceLocation(RatsMod.MODID, "textures/entity/psychic.png"), f * 0.01F, f * 0.01F) : null;
	}

	@Override
	public List<Goal> addNewWorkGoals(TamedRat rat) {
		return List.of(new RatPickupGoal(rat, RatPickupGoal.PickupType.ENERGY), new RatDepositGoal(rat, RatDepositGoal.DepositType.ENERGY));
	}
}
