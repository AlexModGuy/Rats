package com.github.alexthe666.rats.server.recipes;

import com.github.alexthe666.rats.server.blocks.RatsBlockRegistry;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class RatsRecipeRegistry {

    public static void register(){
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
        GameRegistry.addSmelting(RatsItemRegistry.RAW_RAT, new ItemStack(RatsItemRegistry.COOKED_RAT), 1);
    }
}
