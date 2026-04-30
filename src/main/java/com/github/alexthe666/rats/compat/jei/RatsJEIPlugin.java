package com.github.alexthe666.rats.compat.jei;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.registry.RatlantisItemRegistry;
import com.github.alexthe666.rats.registry.RatsBlockRegistry;
import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.registry.RatsRecipeRegistry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@JeiPlugin
public class RatsJEIPlugin implements IModPlugin {
	public static final ResourceLocation MOD = ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "rats");

	private void addDescription(IRecipeRegistration registry, ItemStack itemStack) {
		registry.addIngredientInfo(itemStack, VanillaTypes.ITEM_STACK, Component.translatable(itemStack.getDescriptionId() + ".jei_desc"));
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime runtime) {
		if (RatConfig.cheesemaking) {
			List<CauldronInfoHolder> cauldrons = new LinkedList<>();
			cauldrons.add(new CauldronInfoHolder(Items.MILK_BUCKET, Items.MILK_BUCKET, Items.CAULDRON, RatsBlockRegistry.MILK_CAULDRON.get()));
			cauldrons.add(new CauldronInfoHolder(Items.AIR, RatsBlockRegistry.BLOCK_OF_CHEESE.get(), RatsBlockRegistry.MILK_CAULDRON.get(), RatsBlockRegistry.CHEESE_CAULDRON.get()));
			cauldrons.add(new CauldronInfoHolder(Items.SUGAR, RatsBlockRegistry.BLOCK_OF_BLUE_CHEESE.get(), RatsBlockRegistry.CHEESE_CAULDRON.get(), RatsBlockRegistry.BLUE_CHEESE_CAULDRON.get()));
			cauldrons.add(new CauldronInfoHolder(Items.LAVA_BUCKET, RatsBlockRegistry.BLOCK_OF_NETHER_CHEESE.get(), RatsBlockRegistry.CHEESE_CAULDRON.get(), RatsBlockRegistry.NETHER_CHEESE_CAULDRON.get()));
			runtime.getRecipeManager().addRecipes(RatsRecipeTypes.CAULDRON, cauldrons);
		}
	}

	@Override
	public void registerRecipes(IRecipeRegistration registry) {
		RecipeManager manager = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
		// 1.21: getAllRecipesFor returns List<RecipeHolder<T>>; unwrap via .value().
		registry.addRecipes(RatsRecipeTypes.CHEF, manager.getAllRecipesFor(RatsRecipeRegistry.CHEF.get()).stream().map(net.minecraft.world.item.crafting.RecipeHolder::value).toList());
		this.addDescription(registry, new ItemStack(RatsItemRegistry.CHEESE.get()));
		this.addDescription(registry, new ItemStack(RatsItemRegistry.CHEESE_STICK.get()));
		this.addDescription(registry, new ItemStack(RatsItemRegistry.RAT_FLUTE.get()));
		this.addDescription(registry, new ItemStack(RatsItemRegistry.RAT_UPGRADE_BASIC.get()));
		this.addDescription(registry, new ItemStack(RatsItemRegistry.RAT_UPGRADE_CHEF.get()));
		this.addDescription(registry, new ItemStack(RatsItemRegistry.RAT_UPGRADE_WHITELIST.get()));
		this.addDescription(registry, new ItemStack(RatsItemRegistry.RAT_UPGRADE_BLACKLIST.get()));
		this.addDescription(registry, new ItemStack(RatsBlockRegistry.RAT_TRAP.get()));
		this.addDescription(registry, new ItemStack(RatsBlockRegistry.RAT_CAGE.get()));
		this.addDescription(registry, new ItemStack(RatsBlockRegistry.RAT_CRAFTING_TABLE.get()));
		if (RatsMod.RATLANTIS_DATAPACK_ENABLED) {
			registry.addRecipes(RatsRecipeTypes.ARCHEOLOGIST, manager.getAllRecipesFor(RatsRecipeRegistry.ARCHEOLOGIST.get()).stream().map(net.minecraft.world.item.crafting.RecipeHolder::value).toList());
			this.addDescription(registry, new ItemStack(RatlantisItemRegistry.RAT_UPGRADE_ARCHEOLOGIST.get()));
		} else {
			if (!RatsMod.RATLANTIS_ITEMS.isEmpty()) {
				registry.getIngredientManager().removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, RatsMod.RATLANTIS_ITEMS.stream().map(ItemStack::new).toList());
			}
		}
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		registry.addRecipeCategories(new ChefRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new CauldronRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		if (RatsMod.RATLANTIS_DATAPACK_ENABLED) {
			registry.addRecipeCategories(new ArcheologistRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		}
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
		registry.addRecipeCatalyst(new ItemStack(RatsItemRegistry.RAT_UPGRADE_CHEF.get()), RatsRecipeTypes.CHEF);
		if (RatConfig.cheesemaking) {
			registry.addRecipeCatalyst(new ItemStack(Items.CAULDRON), RatsRecipeTypes.CAULDRON);
		}
		registry.addRecipeCatalyst(new ItemStack(RatsItemRegistry.RAT_UPGRADE_CRAFTING.get()), RecipeTypes.CRAFTING);
		if (RatsMod.RATLANTIS_DATAPACK_ENABLED) {
			registry.addRecipeCatalyst(new ItemStack(RatlantisItemRegistry.RAT_UPGRADE_ARCHEOLOGIST.get()), RatsRecipeTypes.ARCHEOLOGIST);
		}
	}

	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
		// Register a CRAFTING-specific handler so JEI shows the "+" button on every vanilla crafting
		// recipe when the rat crafting table is open. Also keep the universal fallback for other
		// recipe types (chef, archeologist, etc.) so those can transfer too.
		registration.addRecipeTransferHandler(RatCraftingTableCraftingTransferHandler.INSTANCE, mezz.jei.api.constants.RecipeTypes.CRAFTING);
		registration.addUniversalRecipeTransferHandler(RatCraftingTableTransferHandler.INSTANCE);
	}

	@Override
	public void registerItemSubtypes(ISubtypeRegistration registration) {
		// 1.21: ISubtypeRegistration.useNbtForSubtypes(...) was removed. Subtypes are now expressed by
		// registering a per-item ISubtypeInterpreter that hashes the relevant DataComponents. None of
		// our items vary their JEI page presentation by component data (the rat upgrades are listed
		// once and the rat-nugget variants are separate items, not stack-side subtypes), so this hook
		// stays empty intentionally.
	}

	@Override
	public ResourceLocation getPluginUid() {
		return MOD;
	}
}
