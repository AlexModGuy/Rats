package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import java.util.*;

public class RatsNuggetRegistry {

    public static Map<ItemStack, ItemStack> ORE_TO_INGOTS = new TreeMap<>(new Comparator<ItemStack>() {
        @Override
        public int compare(ItemStack o1, ItemStack o2) {
            return o1.getDisplayName().compareTo(o2.getDisplayName());
        }
    });

    public static void init() {
        for(String oreName : OreDictionary.getOreNames()){
            if(oreName.contains("ore") && !OreDictionary.getOres(oreName).isEmpty()){
                ItemStack stack = ItemStack.EMPTY;
                try{
                    stack = OreDictionary.getOres(oreName).get(0);
                }catch (Exception e){
                    RatsMod.logger.warn("Could not make rat nugget for " + oreName);
                }
                ItemStack burntItem = FurnaceRecipes.instance().getSmeltingResult(stack).copy();
                if(!burntItem.isEmpty()){
                    ORE_TO_INGOTS.put(stack, burntItem);
                }
            }
        }
        for(Map.Entry<ItemStack, ItemStack> entry : ORE_TO_INGOTS.entrySet()){
            ItemStack oreStack = entry.getKey();
            ItemStack ingotStack = entry.getValue();
            ItemStack stack = new ItemStack(RatsItemRegistry.RAT_NUGGET_ORE, 1, getNuggetMeta(ingotStack));
            NBTTagCompound poopTag = new NBTTagCompound();
            NBTTagCompound oreTag = new NBTTagCompound();
            oreStack.writeToNBT(oreTag);
            NBTTagCompound ingotTag = new NBTTagCompound();
            ingotStack.writeToNBT(ingotTag);
            poopTag.setTag("OreItem", oreTag);
            poopTag.setTag("IngotItem", ingotTag);
            stack.setTagCompound(poopTag);
            GameRegistry.addSmelting(stack, entry.getValue(), 0.2F);
            OreDictionary.registerOre("ratPoop", stack);
        }

    }

    public static int getNuggetMeta(ItemStack ore){
        int count = 0;
        for(Map.Entry<ItemStack, ItemStack> entry : ORE_TO_INGOTS.entrySet()) {
                if(entry.getValue().isItemEqual(ore)){
                    break;
                }
            count++;
        }
        return count;
    }
}
