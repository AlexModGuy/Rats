package com.github.alexthe666.rats.server.items.upgrades;

import com.github.alexthe666.rats.registry.RatsRecipeRegistry;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.TickRatUpgrade;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;

import java.util.Optional;

public class ChefRatUpgradeItem extends BaseRatUpgradeItem implements TickRatUpgrade {
	public ChefRatUpgradeItem(Properties properties) {
		super(properties, 2, 2);
	}

	@Override
	public boolean isRatHoldingFood(TamedRat rat) {
		return false;
	}

	@Override
	public boolean shouldDepositItem(TamedRat rat, ItemStack stack) {
		return this.getCookingResultFor(rat, stack).isEmpty();
	}

	@Override
	public void tick(TamedRat rat) {
		if (!rat.getMainHandItem().isEmpty()) {
			this.tryCooking(rat);
			this.createFinishedParticles(rat, ParticleTypes.SMOKE, 3, ParticleTypes.FLAME, 0.125F);
		}
	}

	private void tryCooking(TamedRat rat) {
		ItemStack heldItem = rat.getMainHandItem();
		ItemStack burntItem = this.getCookingResultFor(rat, heldItem.copyWithCount(1));
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

	private ItemStack getCookingResultFor(TamedRat rat, ItemStack stack) {
		ItemStack specialChefRecipe = rat.getResultForRecipe(RatsRecipeRegistry.CHEF.get(), stack);
		if (!specialChefRecipe.isEmpty()) {
			return specialChefRecipe.copy();
		}

		Optional<SmeltingRecipe> optional = rat.getLevel().getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SimpleContainer(stack), rat.getLevel());
		if (optional.isPresent()) {
			ItemStack itemstack = optional.get().getResultItem(rat.getLevel().registryAccess());
			if (!itemstack.isEmpty()) {
				ItemStack itemstack1 = itemstack.copy();
				itemstack1.setCount(stack.getCount() * itemstack.getCount());
				return itemstack1;
			}
		}
		return ItemStack.EMPTY;
	}
}
