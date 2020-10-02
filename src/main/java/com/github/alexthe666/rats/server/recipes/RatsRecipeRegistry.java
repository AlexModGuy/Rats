package com.github.alexthe666.rats.server.recipes;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.blocks.RatlantisBlockRegistry;
import com.github.alexthe666.rats.server.blocks.RatsBlockRegistry;
import com.github.alexthe666.rats.server.items.RatlantisItemRegistry;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tileentity.BannerPattern;

import java.util.ArrayList;
import java.util.List;

public class RatsRecipeRegistry {

    public static List<SharedRecipe> CAULDRON_RECIPES = new ArrayList<>();
    public static List<SharedRecipe> RAT_CHEF_RECIPES = new ArrayList<>();
    public static List<SharedRecipe> RAT_ARCHEOLOGIST_RECIPES = new ArrayList<>();
    public static List<SharedRecipe> RAT_GEMCUTTER_RECIPES = new ArrayList<>();

    public static BannerPattern RAT_PATTERN = addBanner("rat", new ItemStack(RatsItemRegistry.RAT_PELT));
    public static BannerPattern CHEESE_PATTERN = addBanner("cheese", new ItemStack(RatsItemRegistry.CHEESE));
    public static BannerPattern RAT_AND_CROSSBONES_BANNER = addBanner("rat_and_crossbones", new ItemStack(RatsItemRegistry.PIRAT_HAT));
    public static BannerPattern RAT_AND_SICKLE_BANNER = addBanner("rat_and_sickle", new ItemStack(RatlantisItemRegistry.MILITARY_HAT));

    public static void preRegister() {
        RAT_CHEF_RECIPES.add(new SharedRecipe(new ItemStack(RatsItemRegistry.ASSORTED_VEGETABLES), new ItemStack(RatsItemRegistry.CONFIT_BYALDI)));
        RAT_CHEF_RECIPES.add(new SharedRecipe(new ItemStack(RatsItemRegistry.CHEESE), new ItemStack(RatsItemRegistry.STRING_CHEESE, 4)));
        RAT_CHEF_RECIPES.add(new SharedRecipe(new ItemStack(RatsItemRegistry.CENTIPEDE), new ItemStack(RatsItemRegistry.POTATO_KNISHES)));
        if (RatsMod.RATLANTIS_LOADED) {
            RAT_ARCHEOLOGIST_RECIPES.add(new SharedRecipe(new ItemStack(Items.EMERALD), new ItemStack(RatlantisItemRegistry.GEM_OF_RATLANTIS)));
            RAT_ARCHEOLOGIST_RECIPES.add(new SharedRecipe(new ItemStack(RatsItemRegistry.RAT_UPGRADE_BASIC), new ItemStack(RatlantisItemRegistry.RAT_UPGRADE_BASIC_RATLANTEAN)));
            RAT_ARCHEOLOGIST_RECIPES.add(new SharedRecipe(new ItemStack(RatsItemRegistry.PIPER_HAT), new ItemStack(RatsItemRegistry.PIRAT_HAT)));
            RAT_ARCHEOLOGIST_RECIPES.add(new SharedRecipe(new ItemStack(Items.BLAZE_POWDER), new ItemStack(RatlantisItemRegistry.RATLANTEAN_FLAME)));
            RAT_ARCHEOLOGIST_RECIPES.add(new SharedRecipe(new ItemStack(Items.LEATHER_CHESTPLATE), new ItemStack(RatlantisItemRegistry.RAT_TOGA)));
            RAT_ARCHEOLOGIST_RECIPES.add(new SharedRecipe(new ItemStack(Items.RABBIT_FOOT), new ItemStack(RatlantisItemRegistry.FERAL_RAT_CLAW)));
            RAT_ARCHEOLOGIST_RECIPES.add(new SharedRecipe(new ItemStack(Items.FIRE_CHARGE), new ItemStack(RatlantisItemRegistry.CHEESE_CANNONBALL)));
            RAT_ARCHEOLOGIST_RECIPES.add(new SharedRecipe(new ItemStack(Items.IRON_SWORD), new ItemStack(RatlantisItemRegistry.PIRAT_CUTLASS)));
            RAT_ARCHEOLOGIST_RECIPES.add(new SharedRecipe(new ItemStack(Items.RABBIT_HIDE), new ItemStack(RatsItemRegistry.RAT_PELT)));
            RAT_ARCHEOLOGIST_RECIPES.add(new SharedRecipe(new ItemStack(Items.PHANTOM_MEMBRANE), new ItemStack(RatlantisItemRegistry.GHOST_PIRAT_ECTOPLASM)));
            RAT_ARCHEOLOGIST_RECIPES.add(new SharedRecipe(new ItemStack(Items.TROPICAL_FISH), new ItemStack(RatlantisItemRegistry.RATFISH)));
            RAT_ARCHEOLOGIST_RECIPES.add(new SharedRecipe(new ItemStack(Blocks.DISPENSER), new ItemStack(RatlantisItemRegistry.RATBOT_BARREL)));
            RAT_ARCHEOLOGIST_RECIPES.add(new SharedRecipe(new ItemStack(Blocks.DIAMOND_BLOCK), new ItemStack(RatlantisItemRegistry.ORATCHALCUM_INGOT)));
            RAT_ARCHEOLOGIST_RECIPES.add(new SharedRecipe(new ItemStack(Blocks.BEACON), new ItemStack(RatlantisItemRegistry.ARCANE_TECHNOLOGY)));
            RAT_ARCHEOLOGIST_RECIPES.add(new SharedRecipe(new ItemStack(Items.DRAGON_BREATH), new ItemStack(RatlantisItemRegistry.PSIONIC_RAT_BRAIN)));
            RAT_ARCHEOLOGIST_RECIPES.add(new SharedRecipe(new ItemStack(Items.CONDUIT), new ItemStack(RatlantisItemRegistry.DUTCHRAT_WHEEL)));
            RAT_ARCHEOLOGIST_RECIPES.add(new SharedRecipe(new ItemStack(Items.ELYTRA), new ItemStack(RatlantisItemRegistry.BIPLANE_WING)));
        }
        RAT_GEMCUTTER_RECIPES.add(new SharedRecipe(new ItemStack(Items.DIAMOND), new ItemStack(RatsItemRegistry.RAT_DIAMOND, 4)));
        RAT_GEMCUTTER_RECIPES.add(new SharedRecipe(new ItemStack(Items.COAL), new ItemStack(RatsItemRegistry.LITTLE_BLACK_SQUASH_BALLS)));
        RAT_GEMCUTTER_RECIPES.add(new SharedRecipe(new ItemStack(RatsItemRegistry.LITTLE_BLACK_WORM), new ItemStack(RatsItemRegistry.CENTIPEDE)));
    }

    public static void register() {
        preRegister();

        CAULDRON_RECIPES.add(new SharedRecipe(new ItemStack(Items.MILK_BUCKET), new ItemStack(RatsBlockRegistry.BLOCK_OF_CHEESE)));
        RAT_ARCHEOLOGIST_RECIPES.add(new SharedRecipe(new ItemStack(RatsBlockRegistry.BLOCK_OF_CHEESE), new ItemStack(RatsBlockRegistry.MARBLED_CHEESE_RAW)));
        RAT_ARCHEOLOGIST_RECIPES.add(new SharedRecipe(new ItemStack(Items.SKELETON_SKULL), new ItemStack(RatlantisBlockRegistry.MARBLED_CHEESE_RAT_HEAD)));
        RAT_ARCHEOLOGIST_RECIPES.add(new SharedRecipe(new ItemStack(Blocks.BLUE_ORCHID), new ItemStack(RatlantisBlockRegistry.RATGLOVE_FLOWER)));

        RatsItemRegistry.CHEF_TOQUE_ARMOR_MATERIAL.setRepairMaterial(Ingredient.fromStacks(new ItemStack(Blocks.WHITE_WOOL)));
        RatsItemRegistry.PIPER_HAT_ARMOR_MATERIAL.setRepairMaterial(Ingredient.fromStacks(new ItemStack(RatsBlockRegistry.PIED_WOOL)));
        RatsItemRegistry.HAT_ARMOR_MATERIAL.setRepairMaterial(Ingredient.fromStacks(new ItemStack(Items.LEATHER)));
        RatsItemRegistry.PIRAT_CUTLASS_MATERIAL.setRepairMaterial(Ingredient.fromStacks(new ItemStack(Items.IRON_INGOT)));
        RatsItemRegistry.BAGHNAKHS_MATERIAL.setRepairMaterial(Ingredient.fromStacks(new ItemStack(RatlantisItemRegistry.FERAL_RAT_CLAW)));
        RatsItemRegistry.PLAGUE_SCYTHE_MATERIAL.setRepairMaterial(Ingredient.fromStacks(new ItemStack(RatsItemRegistry.PLAGUE_ESSENCE)));
        RatsItemRegistry.PLAGUE_MASK_MATERIAL.setRepairMaterial(Ingredient.fromStacks(new ItemStack(RatsItemRegistry.PLAGUE_ESSENCE)));
    }


    public static BannerPattern addBanner(String name, ItemStack craftingStack) {
        return BannerPattern.create(name.toUpperCase(), name, "rats." + name, true);
    }

    public static SharedRecipe getRatChefRecipe(ItemStack stack) {
        for (SharedRecipe recipe : RAT_CHEF_RECIPES) {
            if (ItemStack.areItemsEqual(recipe.getInput(), stack)) {
                return recipe;
            }
        }
        return null;
    }

    public static SharedRecipe getArcheologistRecipe(ItemStack stack) {
        for (SharedRecipe recipe : RAT_ARCHEOLOGIST_RECIPES) {
            if (ItemStack.areItemsEqual(recipe.getInput(), stack)) {
                return recipe;
            }
        }
        return null;
    }

    public static SharedRecipe getGemcutterRecipe(ItemStack stack) {
        for (SharedRecipe recipe : RAT_GEMCUTTER_RECIPES) {
            if (ItemStack.areItemsEqual(recipe.getInput(), stack)) {
                return recipe;
            }
        }
        return null;
    }
}
