package com.github.alexthe666.rats.server.compat.jei;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.server.blocks.RatsBlockRegistry;
import com.github.alexthe666.rats.server.compat.jei.archeologist.ArcheologistRecipeCategory;
import com.github.alexthe666.rats.server.compat.jei.cauldron.CauldronRecipeCategory;
import com.github.alexthe666.rats.server.compat.jei.chef.ChefRecipeCategory;
import com.github.alexthe666.rats.server.compat.jei.gemcutter.GemcutterRecipeCategory;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import com.github.alexthe666.rats.server.recipes.RatsRecipeRegistry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;

@JeiPlugin
public class RatsJEIPlugin implements IModPlugin {
    public static final ResourceLocation MOD = new ResourceLocation("rats:rats");
   public static final ResourceLocation CAULDRON_ID = new ResourceLocation("rats:cauldron");
    public static final ResourceLocation CHEF_RAT_ID = new ResourceLocation("rats:chef_rat");
    public static final ResourceLocation ARCHEOLOGIST_RAT_ID = new ResourceLocation("rats:archeologist_rat");
    public static final ResourceLocation GEMCUTTER_RAT_ID = new ResourceLocation("rats:gemcutter_rat");

  /* public void register(IModRegistry registry) {
        if (RatConfig.cheesemaking) {
            registry.addRecipeHandlers(new CauldronRecipeHandler());
            registry.handleRecipes(SharedRecipe.class, new CauldronFactory(), CAULDRON_ID);
            registry.addRecipeCategoryCraftingItem(new ItemStack(Items.CAULDRON), CAULDRON_ID);
        }
        registry.addRecipeHandlers(new ChefRecipeHandler());
        registry.handleRecipes(SharedRecipe.class, new ChefRatFactory(), CHEF_RAT_ID);
        registry.addRecipeCategoryCraftingItem(new ItemStack(RatsItemRegistry.RAT_UPGRADE_CHEF), CHEF_RAT_ID);

        registry.addRecipeHandlers(new ArcheologistRecipeHandler());
        registry.handleRecipes(SharedRecipe.class, new ArcheologistRatFactory(), ARCHEOLOGIST_RAT_ID);
        registry.addRecipeCategoryCraftingItem(new ItemStack(RatsItemRegistry.RAT_UPGRADE_ARCHEOLOGIST), ARCHEOLOGIST_RAT_ID);

        registry.addRecipeHandlers(new GemcutterRecipeHandler());
        registry.handleRecipes(SharedRecipe.class, new GemcutterRatFactory(), GEMCUTTER_RAT_ID);
        registry.addRecipeCategoryCraftingItem(new ItemStack(RatsItemRegistry.RAT_UPGRADE_GEMCUTTER), GEMCUTTER_RAT_ID);


    }*/

    private void addDescription(IRecipeRegistration registry, ItemStack itemStack) {
        registry.addIngredientInfo(itemStack, VanillaTypes.ITEM, itemStack.getTranslationKey() + ".jei_desc");
    }

    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registry) {

    }

    public void registerRecipes(IRecipeRegistration registry) {
        if(RatConfig.cheesemaking){
            registry.addRecipes(RatsRecipeRegistry.CAULDRON_RECIPES, CAULDRON_ID);
        }
        registry.addRecipes(RatsRecipeRegistry.RAT_CHEF_RECIPES, CHEF_RAT_ID);
        registry.addRecipes(RatsRecipeRegistry.RAT_ARCHEOLOGIST_RECIPES, ARCHEOLOGIST_RAT_ID);
        registry.addRecipes(RatsRecipeRegistry.RAT_GEMCUTTER_RECIPES, GEMCUTTER_RAT_ID);
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
        if (RatConfig.cheesemaking) {
            registry.addRecipeCategories(new CauldronRecipeCategory());
        }
        registry.addRecipeCategories(new ChefRecipeCategory());
        registry.addRecipeCategories(new ArcheologistRecipeCategory());
        registry.addRecipeCategories(new GemcutterRecipeCategory());
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
        registry.addRecipeCatalyst(new ItemStack(Items.CAULDRON), CAULDRON_ID);
        registry.addRecipeCatalyst(new ItemStack(RatsItemRegistry.RAT_UPGRADE_CHEF), CHEF_RAT_ID);
        registry.addRecipeCatalyst(new ItemStack(RatsItemRegistry.RAT_UPGRADE_ARCHEOLOGIST), ARCHEOLOGIST_RAT_ID);
        registry.addRecipeCatalyst(new ItemStack(RatsItemRegistry.RAT_UPGRADE_GEMCUTTER), GEMCUTTER_RAT_ID);
    }

    @Override
    public ResourceLocation getPluginUid() {
        return MOD;
    }
}
