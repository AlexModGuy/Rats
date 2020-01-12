package com.github.alexthe666.rats.server.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Tags;

import java.util.ArrayList;

public class RatsNuggetRegistry {
    public static ArrayList<Block> ORE_TO_INGOTS = new ArrayList<>();
    private static boolean init = false;

    public static void init() {
        ORE_TO_INGOTS.clear();
        init = false;
        if (!init) {
            for (Block oreBlock : Tags.Blocks.ORES.getAllElements()) {
                ORE_TO_INGOTS.add(oreBlock);
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
        for (Block entry : ORE_TO_INGOTS) {
            if (Block.getBlockFromItem(ore.getItem()) == entry) {
                break;
            }
            count++;
        }
        return count;
    }
}
