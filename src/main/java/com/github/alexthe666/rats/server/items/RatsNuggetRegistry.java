package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class RatsNuggetRegistry {
    public static ArrayList<ItemStack> ORE_TO_INGOTS = new ArrayList<>();
    private static boolean init = false;

    public static void init() {
        ORE_TO_INGOTS.clear();
        init = false;
        if (!init) {
            Random random = new Random();
            RecipeManager manager = new RecipeManager();
            for (ResourceLocation loc : ItemTags.getCollection().getRegisteredTags()) {
                if (loc.toString().contains("forge:ores")) {
                    Collection<Item> items = ItemTags.getCollection().getOrCreate(loc).getAllElements();
                    ItemStack first = ItemStack.EMPTY;
                    try {
                        for (Item item : items) {
                            if (first == ItemStack.EMPTY) {
                                first = new ItemStack(item);
                            }
                        }
                    } catch (Exception e) {
                        RatsMod.LOGGER.warn("Could not make rat nugget for " + loc.getPath());
                    }
                    ORE_TO_INGOTS.add(first);

                }
            }
            for (ItemStack entry : ORE_TO_INGOTS) {
                ItemStack oreStack = entry;
                ItemStack stack = new ItemStack(RatsItemRegistry.RAT_NUGGET_ORE, 1);
                CompoundNBT poopTag = new CompoundNBT();
                CompoundNBT oreTag = new CompoundNBT();
                oreStack.write(oreTag);
                CompoundNBT ingotTag = new CompoundNBT();
                poopTag.put("OreItem", oreTag);
                poopTag.put("IngotItem", ingotTag);
                stack.setTag(poopTag);
            }
        /*for (String oreName : OreDictionary.getOreNames()) {
            if (oreName.contains("ore") && !OreDictionary.getOres(oreName).isEmpty()) {
                ItemStack stack = ItemStack.EMPTY;
                try {
                    stack = OreDictionary.getOres(oreName).get(0);
                } catch (Exception e) {
                    RatsMod.logger.warn("Could not make rat nugget for " + oreName);
                }
                ItemStack burntItem = FurnaceRecipes.instance().getSmeltingResult(stack).copy();
                if (!burntItem.isEmpty()) {
                    ORE_TO_INGOTS.put(stack, burntItem);
                }
            }
        }
        for (Map.Entry<ItemStack, ItemStack> entry : ORE_TO_INGOTS.entrySet()) {
            ItemStack oreStack = entry.getKey();
            ItemStack ingotStack = entry.getValue();
            ItemStack stack = new ItemStack(RatsItemRegistry.RAT_NUGGET_ORE, 1, getNuggetMeta(ingotStack));
            CompoundNBT poopTag = new CompoundNBT();
            CompoundNBT oreTag = new CompoundNBT();
            oreStack.writeToNBT(oreTag);
            CompoundNBT ingotTag = new CompoundNBT();
            ingotStack.writeToNBT(ingotTag);
            poopTag.setTag("OreItem", oreTag);
            poopTag.setTag("IngotItem", ingotTag);
            stack.setTag(poopTag);
            GameRegistry.addSmelting(stack, entry.getValue(), 0.2F);
            OreDictionary.registerOre("ratPoop", stack);
        }
    */
        }
        init = true;
    }

    public static int getNuggetMeta(ItemStack ore) {
        int count = 0;
        for (ItemStack entry : ORE_TO_INGOTS) {
            if (entry.isItemEqual(ore)) {
                break;
            }
            count++;
        }
        return count;
    }
}
