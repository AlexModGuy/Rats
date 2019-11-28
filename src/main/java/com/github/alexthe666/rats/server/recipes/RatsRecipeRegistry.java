package com.github.alexthe666.rats.server.recipes;

import com.github.alexthe666.rats.server.blocks.RatsBlockRegistry;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.BannerPattern;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class RatsRecipeRegistry {

    public static List<SharedRecipe> CAULDRON_RECIPES = new ArrayList<>();
    public static List<SharedRecipe> RAT_CHEF_RECIPES = new ArrayList<>();
    public static List<SharedRecipe> RAT_ARCHEOLOGIST_RECIPES = new ArrayList<>();
    public static List<SharedRecipe> RAT_GEMCUTTER_RECIPES = new ArrayList<>();

    public static void preRegister() {
        RAT_CHEF_RECIPES.add(new SharedRecipe(new ItemStack(RatsItemRegistry.ASSORTED_VEGETABLES), new ItemStack(RatsItemRegistry.CONFIT_BYALDI)));
        RAT_CHEF_RECIPES.add(new SharedRecipe(new ItemStack(RatsItemRegistry.CHEESE), new ItemStack(RatsItemRegistry.STRING_CHEESE, 4)));
        RAT_CHEF_RECIPES.add(new SharedRecipe(new ItemStack(RatsItemRegistry.CENTIPEDE), new ItemStack(RatsItemRegistry.POTATO_KNISHES)));
        RAT_ARCHEOLOGIST_RECIPES.add(new SharedRecipe(new ItemStack(Items.EMERALD), new ItemStack(RatsItemRegistry.GEM_OF_RATLANTIS)));
        RAT_ARCHEOLOGIST_RECIPES.add(new SharedRecipe(new ItemStack(RatsItemRegistry.RAT_UPGRADE_BASIC), new ItemStack(RatsItemRegistry.RAT_UPGRADE_BASIC_RATLANTEAN)));
        RAT_ARCHEOLOGIST_RECIPES.add(new SharedRecipe(new ItemStack(RatsItemRegistry.PIPER_HAT), new ItemStack(RatsItemRegistry.PIRAT_HAT)));
        RAT_ARCHEOLOGIST_RECIPES.add(new SharedRecipe(new ItemStack(Items.BLAZE_POWDER), new ItemStack(RatsItemRegistry.RATLANTEAN_FLAME)));
        RAT_ARCHEOLOGIST_RECIPES.add(new SharedRecipe(new ItemStack(Items.LEATHER_CHESTPLATE), new ItemStack(RatsItemRegistry.RAT_TOGA)));
        RAT_ARCHEOLOGIST_RECIPES.add(new SharedRecipe(new ItemStack(Items.RABBIT_FOOT), new ItemStack(RatsItemRegistry.FERAL_RAT_CLAW)));
        RAT_ARCHEOLOGIST_RECIPES.add(new SharedRecipe(new ItemStack(Items.FIRE_CHARGE), new ItemStack(RatsItemRegistry.CHEESE_CANNONBALL)));
        RAT_ARCHEOLOGIST_RECIPES.add(new SharedRecipe(new ItemStack(Items.IRON_SWORD), new ItemStack(RatsItemRegistry.PIRAT_CUTLASS)));
        RAT_ARCHEOLOGIST_RECIPES.add(new SharedRecipe(new ItemStack(Items.RABBIT_HIDE), new ItemStack(RatsItemRegistry.RAT_PELT)));
        if (RatConfig.disableRatlantis) {
            RAT_ARCHEOLOGIST_RECIPES.add(new SharedRecipe(new ItemStack(Blocks.BEACON), new ItemStack(RatsItemRegistry.ARCANE_TECHNOLOGY)));
            RAT_ARCHEOLOGIST_RECIPES.add(new SharedRecipe(new ItemStack(Items.DRAGON_BREATH), new ItemStack(RatsItemRegistry.PSIONIC_RAT_BRAIN)));
        }
        RAT_GEMCUTTER_RECIPES.add(new SharedRecipe(new ItemStack(Items.DIAMOND), new ItemStack(RatsItemRegistry.RAT_DIAMOND, 4)));
        RAT_GEMCUTTER_RECIPES.add(new SharedRecipe(new ItemStack(Items.COAL), new ItemStack(RatsItemRegistry.LITTLE_BLACK_SQUASH_BALLS)));
        RAT_GEMCUTTER_RECIPES.add(new SharedRecipe(new ItemStack(RatsItemRegistry.LITTLE_BLACK_WORM), new ItemStack(RatsItemRegistry.CENTIPEDE)));
    }

    public static void register() {
        CAULDRON_RECIPES.add(new SharedRecipe(new ItemStack(Items.MILK_BUCKET), new ItemStack(RatsBlockRegistry.BLOCK_OF_CHEESE)));
        RAT_ARCHEOLOGIST_RECIPES.add(new SharedRecipe(new ItemStack(RatsBlockRegistry.BLOCK_OF_CHEESE), new ItemStack(RatsBlockRegistry.MARBLED_CHEESE_RAW)));
        RAT_ARCHEOLOGIST_RECIPES.add(new SharedRecipe(new ItemStack(Items.SKULL), new ItemStack(RatsBlockRegistry.MARBLED_CHEESE_RAT_HEAD)));
        RAT_ARCHEOLOGIST_RECIPES.add(new SharedRecipe(new ItemStack(Blocks.RED_FLOWER, 1, 1), new ItemStack(RatsBlockRegistry.RATGLOVE_FLOWER)));
        OreDictionary.registerOre("foodCheese", RatsItemRegistry.CHEESE);
        OreDictionary.registerOre("listAllmeatraw", RatsItemRegistry.RAW_RAT);
        OreDictionary.registerOre("foodRatraw", RatsItemRegistry.RAW_RAT);
        OreDictionary.registerOre("listAllmeatcooked", RatsItemRegistry.COOKED_RAT);
        OreDictionary.registerOre("foodRatcooked", RatsItemRegistry.COOKED_RAT);
        OreDictionary.registerOre("blockCheese", RatsBlockRegistry.BLOCK_OF_CHEESE);
        OreDictionary.registerOre("foodVegetable", Items.POTATO);
        OreDictionary.registerOre("foodVegetable", Items.CARROT);
        OreDictionary.registerOre("foodVegetable", Items.BEETROOT);
        OreDictionary.registerOre("foodVegetable", Blocks.PUMPKIN);
        if (RatConfig.disablePlastic) {
            OreDictionary.registerOre("plasticOrGlass", Blocks.GLASS_PANE);
            OreDictionary.registerOre("plasticOrBottle", Items.GLASS_BOTTLE);
        } else {
            OreDictionary.registerOre("plasticOrGlass", RatsItemRegistry.RAW_PLASTIC);
            OreDictionary.registerOre("plasticOrBottle", RatsItemRegistry.PLASTIC_WASTE);
            OreDictionary.registerOre("plastic", RatsItemRegistry.RAW_PLASTIC);
            OreDictionary.registerOre("ingotPlastic", RatsItemRegistry.RAW_PLASTIC);
        }
        OreDictionary.registerOre("listAllwater", Items.WATER_BUCKET);
        OreDictionary.registerOre("listAllseed", Items.WHEAT_SEEDS);
        OreDictionary.registerOre("listAllseed", Items.PUMPKIN_SEEDS);
        OreDictionary.registerOre("listAllseed", Items.BEETROOT_SEEDS);
        OreDictionary.registerOre("listAllseed", Items.MELON_SEEDS);
        OreDictionary.registerOre("flower", new ItemStack(Blocks.RED_FLOWER, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("flower", new ItemStack(Blocks.YELLOW_FLOWER, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("fish", new ItemStack(Items.FISH, 1, OreDictionary.WILDCARD_VALUE));
        if (RatConfig.disableRatlantis) {
            OreDictionary.registerOre("tokenOrIdolFlag", RatsItemRegistry.CHUNKY_CHEESE_TOKEN);
        } else {
            OreDictionary.registerOre("tokenOrIdolFlag", RatsItemRegistry.IDOL_OF_RATLANTIS);
        }
        for (Block block : RatsBlockRegistry.RAT_TUBE_COLOR) {
            OreDictionary.registerOre("ratTube", block);
        }
        for (Item item : RatsItemRegistry.RAT_IGLOOS) {
            OreDictionary.registerOre("ratIgloo", item);
        }
        for (Item item : RatsItemRegistry.RAT_HAMMOCKS) {
            OreDictionary.registerOre("ratHammock", item);
        }
        for (EnumDyeColor color : EnumDyeColor.values()) {
            String woolColor = color.getTranslationKey();
            woolColor = woolColor.substring(0, 1).toUpperCase() + woolColor.substring(1).toLowerCase();
            OreDictionary.registerOre("wool" + woolColor, new ItemStack(Blocks.WOOL, 1, color.getMetadata()));
        }
        OreDictionary.registerOre("woolLightBlue", new ItemStack(Blocks.WOOL, 1, 3));
        OreDictionary.registerOre("ratPoop", RatsItemRegistry.RAT_NUGGET);

        GameRegistry.addSmelting(RatsItemRegistry.RAW_RAT, new ItemStack(RatsItemRegistry.COOKED_RAT), 0.4F);
        GameRegistry.addSmelting(RatsBlockRegistry.MARBLED_CHEESE_RAW, new ItemStack(RatsBlockRegistry.MARBLED_CHEESE), 0.1F);
        GameRegistry.addSmelting(RatsBlockRegistry.MARBLED_CHEESE_BRICK, new ItemStack(RatsBlockRegistry.MARBLED_CHEESE_BRICK_CRACKED), 0.1F);
        GameRegistry.addSmelting(RatsItemRegistry.PLASTIC_WASTE, new ItemStack(RatsItemRegistry.RAW_PLASTIC), 0.5F);
        addBanner("rat", new ItemStack(RatsItemRegistry.RAT_PELT));
        addBanner("cheese", new ItemStack(RatsItemRegistry.CHEESE));
        addBanner("rat_and_crossbones", new ItemStack(RatsItemRegistry.PIRAT_HAT));
        RatsItemRegistry.CHEF_TOQUE_ARMOR_MATERIAL.setRepairItem(new ItemStack(Blocks.WOOL));
        RatsItemRegistry.HAT_ARMOR_MATERIAL.setRepairItem(new ItemStack(Items.LEATHER));
        RatsItemRegistry.PIRAT_CUTLASS_MATERIAL.setRepairItem(new ItemStack(Items.IRON_INGOT));
        RatsItemRegistry.BAGHNAKHS_MATERIAL.setRepairItem(new ItemStack(RatsItemRegistry.FERAL_RAT_CLAW));
        RatsItemRegistry.PLAGUE_SCYTHE_MATERIAL.setRepairItem(new ItemStack(RatsItemRegistry.PLAGUE_ESSENCE));
        RatsItemRegistry.PLAGUE_MASK_MATERIAL.setRepairItem(new ItemStack(RatsItemRegistry.PLAGUE_ESSENCE));
    }


    public static BannerPattern addBanner(String name, ItemStack craftingStack) {
        Class<?>[] classes = {String.class, String.class, ItemStack.class};
        Object[] names = {name, "rats." + name, craftingStack};
        return EnumHelper.addEnum(BannerPattern.class, name.toUpperCase(), classes, names);
    }

    public static SharedRecipe getRatChefRecipe(ItemStack stack) {
        for (SharedRecipe recipe : RAT_CHEF_RECIPES) {
            if (OreDictionary.itemMatches(recipe.getInput(), stack, false)) {
                return recipe;
            }
        }
        return null;
    }

    public static SharedRecipe getArcheologistRecipe(ItemStack stack) {
        for (SharedRecipe recipe : RAT_ARCHEOLOGIST_RECIPES) {
            if (OreDictionary.itemMatches(recipe.getInput(), stack, false)) {
                return recipe;
            }
        }
        return null;
    }

    public static SharedRecipe getGemcutterRecipe(ItemStack stack) {
        for (SharedRecipe recipe : RAT_GEMCUTTER_RECIPES) {
            if (OreDictionary.itemMatches(recipe.getInput(), stack, false)) {
                return recipe;
            }
        }
        return null;
    }
}
