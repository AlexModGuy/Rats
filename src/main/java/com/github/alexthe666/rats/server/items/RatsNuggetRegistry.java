package com.github.alexthe666.rats.server.items;

import net.minecraft.item.ItemStack;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class RatsNuggetRegistry {

    public static Map<ItemStack, ItemStack> ORE_TO_INGOTS = new TreeMap<>(new Comparator<ItemStack>() {
        @Override
        public int compare(ItemStack o1, ItemStack o2) {
            return o1.getDisplayName().getString().compareTo(o2.getDisplayName().getString());
        }
    });

    public static void init() {
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

    public static int getNuggetMeta(ItemStack ore) {
        int count = 0;
        for (Map.Entry<ItemStack, ItemStack> entry : ORE_TO_INGOTS.entrySet()) {
            if (entry.getValue().isItemEqual(ore)) {
                break;
            }
            count++;
        }
        return count;
    }
}
