package com.github.alexthe666.rats.server.compat.jei;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.blocks.RatsBlockRegistry;
import com.github.alexthe666.rats.server.compat.jei.archeologist.ArcheologistRecipeCategory;
import com.github.alexthe666.rats.server.compat.jei.archeologist.ArcheologistRecipeHandler;
import com.github.alexthe666.rats.server.compat.jei.archeologist.ArcheologistRecipeWrapper;
import com.github.alexthe666.rats.server.compat.jei.cauldron.CauldronRecipeCategory;
import com.github.alexthe666.rats.server.compat.jei.cauldron.CauldronRecipeHandler;
import com.github.alexthe666.rats.server.compat.jei.cauldron.CauldronRecipeWrapper;
import com.github.alexthe666.rats.server.compat.jei.chef.ChefRecipeCategory;
import com.github.alexthe666.rats.server.compat.jei.chef.ChefRecipeHandler;
import com.github.alexthe666.rats.server.compat.jei.chef.ChefRecipeWrapper;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import com.github.alexthe666.rats.server.recipes.RatsRecipeRegistry;
import com.github.alexthe666.rats.server.recipes.SharedRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class RatsJEIPlugin implements IModPlugin {

    public static final String CAULDRON_ID = "rats.cauldron";
    public static final String CHEF_RAT_ID = "rats.chef_rat";
    public static final String ARCHEOLOGIST_RAT_ID = "rats.archeologist_rat";

    @SuppressWarnings("deprecation")
    public void register(IModRegistry registry) {
        if(RatsMod.CONFIG_OPTIONS.cheesemaking) {
            registry.addRecipes(RatsRecipeRegistry.CAULDRON_RECIPES, CAULDRON_ID);
            registry.addRecipeHandlers(new CauldronRecipeHandler());
            registry.handleRecipes(SharedRecipe.class, new CauldronFactory(), CAULDRON_ID);
            registry.addRecipeCategoryCraftingItem(new ItemStack(Items.CAULDRON), CAULDRON_ID);
        }
        registry.addRecipes(RatsRecipeRegistry.RAT_CHEF_RECIPES, CHEF_RAT_ID);
        registry.addRecipeHandlers(new ChefRecipeHandler());
        registry.handleRecipes(SharedRecipe.class, new ChefRatFactory(), CHEF_RAT_ID);
        registry.addRecipeCategoryCraftingItem(new ItemStack(RatsItemRegistry.RAT_UPGRADE_CHEF), CHEF_RAT_ID);
        registry.addRecipes(RatsRecipeRegistry.RAT_ARCHEOLOGIST_RECIPES, ARCHEOLOGIST_RAT_ID);
        registry.addRecipeHandlers(new ArcheologistRecipeHandler());
        registry.handleRecipes(SharedRecipe.class, new ArcheologistRatFactory(), ARCHEOLOGIST_RAT_ID);
        registry.addRecipeCategoryCraftingItem(new ItemStack(RatsItemRegistry.RAT_UPGRADE_ARCHEOLOGIST), ARCHEOLOGIST_RAT_ID);
        addDescription(registry, new ItemStack(RatsItemRegistry.CHEESE));
        addDescription(registry, new ItemStack(RatsItemRegistry.CHEESE_STICK));
        addDescription(registry, new ItemStack(RatsItemRegistry.RAT_FLUTE));
        addDescription(registry, new ItemStack(RatsItemRegistry.RAT_UPGRADE_BASIC));
        addDescription(registry, new ItemStack(RatsItemRegistry.RAT_UPGRADE_CHEF));
        addDescription(registry, new ItemStack(RatsItemRegistry.RAT_UPGRADE_ARCHEOLOGIST));
        addDescription(registry, new ItemStack(RatsItemRegistry.RAT_UPGRADE_WHITELIST));
        addDescription(registry, new ItemStack(RatsItemRegistry.RAT_UPGRADE_BLACKLIST));
        addDescription(registry, new ItemStack(RatsBlockRegistry.RAT_TRAP));
        addDescription(registry, new ItemStack(RatsBlockRegistry.RAT_CAGE));
        addDescription(registry, new ItemStack(RatsBlockRegistry.RAT_CRAFTING_TABLE));

    }

    public void registerCategories(IRecipeCategoryRegistration registry) {
        if(RatsMod.CONFIG_OPTIONS.cheesemaking) {
            registry.addRecipeCategories(new CauldronRecipeCategory());
        }
        registry.addRecipeCategories(new ChefRecipeCategory());
        registry.addRecipeCategories(new ArcheologistRecipeCategory());
    }

    public class CauldronFactory implements IRecipeWrapperFactory<SharedRecipe> {
        @Override
        public IRecipeWrapper getRecipeWrapper(SharedRecipe recipe) {
            return new CauldronRecipeWrapper(recipe);
        }
    }

    public class ChefRatFactory implements IRecipeWrapperFactory<SharedRecipe> {
        @Override
        public IRecipeWrapper getRecipeWrapper(SharedRecipe recipe) {
            return new ChefRecipeWrapper(recipe);
        }
    }

    public class ArcheologistRatFactory implements IRecipeWrapperFactory<SharedRecipe> {
        @Override
        public IRecipeWrapper getRecipeWrapper(SharedRecipe recipe) {
            return new ArcheologistRecipeWrapper(recipe);
        }
    }

    private static void addDescription(IModRegistry registry, ItemStack stack){
        registry.addIngredientInfo(stack, ItemStack.class, stack.getTranslationKey() + ".jei_desc");

    }
}
