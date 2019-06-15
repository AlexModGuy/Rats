package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class RatsBlockRegistry {

    @GameRegistry.ObjectHolder(RatsMod.MODID + ":block_of_cheese")
    public static Block BLOCK_OF_CHEESE = new BlockCheese();

    @GameRegistry.ObjectHolder(RatsMod.MODID + ":milk_cauldron")
    public static Block MILK_CAULDRON = new BlockMilkCauldron();

    @GameRegistry.ObjectHolder(RatsMod.MODID + ":cheese_cauldron")
    public static Block CHEESE_CAULDRON = new BlockCheeseCauldron();

    @GameRegistry.ObjectHolder(RatsMod.MODID + ":rat_hole")
    public static Block RAT_HOLE = new BlockRatHole();

    @GameRegistry.ObjectHolder(RatsMod.MODID + ":rat_trap")
    public static Block RAT_TRAP = new BlockRatTrap();

    @GameRegistry.ObjectHolder(RatsMod.MODID + ":rat_cage")
    public static Block RAT_CAGE = new BlockRatCage();

    @GameRegistry.ObjectHolder(RatsMod.MODID + ":rat_crafting_table")
    public static Block RAT_CRAFTING_TABLE = new BlockRatCraftingTable();
}
