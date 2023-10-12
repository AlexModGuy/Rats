package com.github.alexthe666.rats.server.items.upgrades;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.ai.goal.RatDepositGoal;
import com.github.alexthe666.rats.server.entity.ai.goal.RatPickupGoal;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.ChangesAIUpgrade;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.ChangesOverlayUpgrade;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.TickRatUpgrade;
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
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EnergyRatUpgradeItem extends BaseRatUpgradeItem implements ChangesOverlayUpgrade, ChangesAIUpgrade, TickRatUpgrade {

	private final int transferRate;
	private final int chargeRate;

	public EnergyRatUpgradeItem(Item.Properties properties, int rarity, int transferRate, int itemChargeRate) {
		super(properties, rarity, 0);
		this.transferRate = transferRate;
		this.chargeRate = itemChargeRate;
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
		if (RatConfig.ratsChargeHeldItems) {
			tooltip.add(Component.translatable(RatsLangConstants.RAT_UPGRADE_ENERGY_CHARGE, this.chargeRate).withStyle(ChatFormatting.GRAY));
		}
	}

	@Override
	public @Nullable RenderType getOverlayTexture(ItemStack stack, TamedRat rat, float partialTicks) {
		float f = (float) rat.tickCount + partialTicks;
		return rat.getHeldRF() > 0 ? RenderType.energySwirl(new ResourceLocation(RatsMod.MODID, "textures/entity/psychic.png"), f * 0.01F, f * 0.01F) : null;
	}

	@Override
	public List<Goal> addNewWorkGoals(TamedRat rat) {
		return List.of(new RatPickupGoal(rat, RatPickupGoal.PickupType.ENERGY), new RatDepositGoal(rat, RatDepositGoal.DepositType.ENERGY));
	}

	@Override
	public void tick(TamedRat rat) {
		if (RatConfig.ratsChargeHeldItems && rat.getHeldRF() > 0 && !rat.getMainHandItem().isEmpty()) {
			ItemStack stack = rat.getMainHandItem();
			LazyOptional<IEnergyStorage> optional = stack.getCapability(ForgeCapabilities.ENERGY);
			if (optional.isPresent()) {
				IEnergyStorage energyStorage = optional.orElseThrow(IllegalStateException::new);
				if (energyStorage.getEnergyStored() < energyStorage.getMaxEnergyStored()) {
					int energyToTransfer = Math.min(rat.getHeldRF(), this.chargeRate);
					energyToTransfer = energyToTransfer - energyStorage.receiveEnergy(energyToTransfer, false);
					rat.setHeldRF(Math.max(0, rat.getHeldRF() - energyToTransfer));
				}
			}
		}
	}
}
