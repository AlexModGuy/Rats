package com.github.alexthe666.rats.server.items.upgrades;

import com.github.alexthe666.rats.registry.RatsRecipeRegistry;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.TickRatUpgrade;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

public class GemcutterRatUpgradeItem extends BaseRatUpgradeItem implements TickRatUpgrade {

	public GemcutterRatUpgradeItem(Properties properties) {
		super(properties, 1, 1);
	}

	@Override
	public boolean isRatHoldingFood(TamedRat rat) {
		return false;
	}

	@Override
	public void tick(TamedRat rat) {
		if (!rat.getMainHandItem().isEmpty()) {
			this.tryGemcutter(rat);
			this.createFinishedParticles(rat, new BlockParticleOption(ParticleTypes.BLOCK, Blocks.DIAMOND_ORE.defaultBlockState()), 1, ParticleTypes.SMOKE, 0.0F);
		}
	}

	private void tryGemcutter(TamedRat rat) {
		ItemStack heldItem = rat.getMainHandItem();
		ItemStack burntItem = rat.getResultForRecipe(RatsRecipeRegistry.GEMCUTTER.get(), heldItem.copyWithCount(1));
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
						if (!rat.level().isClientSide()) {
							rat.spawnAtLocation(burntItem, 0.25F);
						}
					}
				}
				rat.cookingProgress = 0;
			}
		}
	}

	@Override
	public boolean shouldDepositItem(TamedRat rat, ItemStack stack) {
		return rat.getResultForRecipe(RatsRecipeRegistry.ARCHEOLOGIST.get(), stack).isEmpty();
	}
}
