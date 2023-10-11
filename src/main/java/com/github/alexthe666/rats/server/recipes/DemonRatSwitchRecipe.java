package com.github.alexthe666.rats.server.recipes;

import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.registry.RatsRecipeRegistry;
import com.github.alexthe666.rats.server.items.upgrades.DemonRatUpgradeItem;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class DemonRatSwitchRecipe extends CustomRecipe {
	public DemonRatSwitchRecipe(ResourceLocation location, CraftingBookCategory category) {
		super(location, category);
	}

	@Override
	public boolean matches(CraftingContainer container, Level level) {
		ItemStack upgrade = null;
		for (int i = 0; i < container.getContainerSize(); ++i) {
			ItemStack stack = container.getItem(i);
			if (!stack.isEmpty()) {
				if (upgrade != null) {
					return false;
				} else {
					if (stack.is(RatsItemRegistry.RAT_UPGRADE_DEMON.get())) {
						upgrade = stack;
					} else {
						return false;
					}
				}
			}
		}
		return upgrade != null;
	}

	@Override
	public ItemStack assemble(CraftingContainer container, RegistryAccess access) {
		for (int i = 0; i < container.getContainerSize(); ++i) {
			ItemStack stack = container.getItem(i);
			if (!stack.isEmpty() && stack.is(RatsItemRegistry.RAT_UPGRADE_DEMON.get())) {
				return DemonRatUpgradeItem.getDemonUpgrade(!DemonRatUpgradeItem.isSoulVersion(stack));
			}
		}
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= 1;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return RatsRecipeRegistry.SWITCH_DEMON.get();
	}
}
