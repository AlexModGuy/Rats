package com.github.alexthe666.rats.server.items.upgrades;

import com.github.alexthe666.rats.registry.RatsBlockRegistry;
import com.github.alexthe666.rats.registry.RatsRecipeRegistry;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.TickRatUpgrade;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public class ArcheologistRatUpgradeItem extends BaseRatUpgradeItem implements TickRatUpgrade {
	public ArcheologistRatUpgradeItem(Properties properties) {
		super(properties, 2, 1);
	}

	@Override
	public boolean isRatHoldingFood(TamedRat rat) {
		return false;
	}

	@Override
	public boolean shouldDepositItem(TamedRat rat, ItemStack stack) {
		return rat.getResultForRecipe(RatsRecipeRegistry.ARCHEOLOGIST.get(), stack).isEmpty();
	}

	@Override
	public void tick(TamedRat rat) {
		if (!rat.getMainHandItem().isEmpty()) {
			this.tryArcheology(rat);
			this.createFinishedParticles(rat, new BlockParticleOption(ParticleTypes.BLOCK, RatsBlockRegistry.GARBAGE_PILE.get().defaultBlockState()), 1, ParticleTypes.ENCHANT, 0.125F);
		}
	}

	private void tryArcheology(TamedRat rat) {
		ItemStack heldItem = rat.getMainHandItem();
		ItemStack burntItem = rat.getResultForRecipe(RatsRecipeRegistry.ARCHEOLOGIST.get(), heldItem);
		if (burntItem.isEmpty()) {
			rat.cookingProgress = 0;
		} else {
			rat.cookingProgress++;
			if (rat.cookingProgress == 100) {
				heldItem.shrink(1);
				if (heldItem.isEmpty()) {
					rat.setItemInHand(InteractionHand.MAIN_HAND, burntItem);
				} else {
					if (!rat.tryDepositItemInContainers(burntItem)) {
						if (!rat.getLevel().isClientSide()) {
							rat.spawnAtLocation(burntItem, 0.25F);
						}
					}
				}
				rat.cookingProgress = 0;
			}
		}
	}
}
