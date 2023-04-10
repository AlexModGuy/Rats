package com.github.alexthe666.rats.compat.jei;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.registry.RatlantisItemRegistry;
import com.github.alexthe666.rats.registry.RatsBlockRegistry;
import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.registry.RatsRecipeRegistry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.*;
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
	public static final ResourceLocation MOD = new ResourceLocation(RatsMod.MODID, "rats");

	private void addDescription(IRecipeRegistration registry, ItemStack itemStack) {
		registry.addIngredientInfo(itemStack, VanillaTypes.ITEM_STACK, Component.translatable(itemStack.getDescriptionId() + ".jei_desc"));
	}

	@Override
	public void registerRecipes(IRecipeRegistration registry) {
		RecipeManager manager = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
		registry.addRecipes(RatsRecipeTypes.CHEF, manager.getAllRecipesFor(RatsRecipeRegistry.CHEF.get()));
		if (RatsMod.RATLANTIS_DATAPACK_ENABLED) {
			registry.addRecipes(RatsRecipeTypes.ARCHEOLOGIST, manager.getAllRecipesFor(RatsRecipeRegistry.ARCHEOLOGIST.get()));
		}
		registry.addRecipes(RatsRecipeTypes.GEMCUTTER, manager.getAllRecipesFor(RatsRecipeRegistry.GEMCUTTER.get()));
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
			this.addDescription(registry, new ItemStack(RatlantisItemRegistry.RAT_UPGRADE_ARCHEOLOGIST.get()));
		}

		if (!RatsMod.RATLANTIS_DATAPACK_ENABLED && !RatsMod.RATLANTIS_ITEMS.isEmpty()) {
			registry.getIngredientManager().removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, RatsMod.RATLANTIS_ITEMS.stream().map(ItemStack::new).toList());
		}

		List<CauldronInfoHolder> cauldrons = new LinkedList<>();
		cauldrons.add(new CauldronInfoHolder(Items.MILK_BUCKET, Items.MILK_BUCKET, Items.CAULDRON, RatsBlockRegistry.MILK_CAULDRON.get()));
		cauldrons.add(new CauldronInfoHolder(Items.AIR, RatsBlockRegistry.BLOCK_OF_CHEESE.get(), RatsBlockRegistry.MILK_CAULDRON.get(), RatsBlockRegistry.CHEESE_CAULDRON.get()));
		cauldrons.add(new CauldronInfoHolder(Items.SUGAR, RatsBlockRegistry.BLOCK_OF_BLUE_CHEESE.get(), RatsBlockRegistry.CHEESE_CAULDRON.get(), RatsBlockRegistry.BLUE_CHEESE_CAULDRON.get()));
		cauldrons.add(new CauldronInfoHolder(Items.LAVA_BUCKET, RatsBlockRegistry.BLOCK_OF_NETHER_CHEESE.get(), RatsBlockRegistry.CHEESE_CAULDRON.get(), RatsBlockRegistry.NETHER_CHEESE_CAULDRON.get()));
		registry.addRecipes(RatsRecipeTypes.CAULDRON, cauldrons);
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		registry.addRecipeCategories(new ChefRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		if (RatsMod.RATLANTIS_DATAPACK_ENABLED) {
			registry.addRecipeCategories(new ArcheologistRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		}
		registry.addRecipeCategories(new GemcutterRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new CauldronRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
		registry.addRecipeCatalyst(new ItemStack(RatsItemRegistry.RAT_UPGRADE_CHEF.get()), RatsRecipeTypes.CHEF);
		if (RatsMod.RATLANTIS_DATAPACK_ENABLED) {
			registry.addRecipeCatalyst(new ItemStack(RatlantisItemRegistry.RAT_UPGRADE_ARCHEOLOGIST.get()), RatsRecipeTypes.ARCHEOLOGIST);
		}
		registry.addRecipeCatalyst(new ItemStack(RatsItemRegistry.RAT_UPGRADE_GEMCUTTER.get()), RatsRecipeTypes.GEMCUTTER);
		registry.addRecipeCatalyst(new ItemStack(Items.CAULDRON), RatsRecipeTypes.CAULDRON);
	}

	@Override
	public void registerItemSubtypes(ISubtypeRegistration registration) {
		registration.useNbtForSubtypes(RatsItemRegistry.RAT_NUGGET_ORE.get());
	}

	@Override
	public ResourceLocation getPluginUid() {
		return MOD;
	}
}
