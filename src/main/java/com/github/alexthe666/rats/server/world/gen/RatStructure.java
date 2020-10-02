package com.github.alexthe666.rats.server.world.gen;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.blocks.RatlantisBlockRegistry;
import com.github.alexthe666.rats.server.blocks.RatsBlockRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

public enum RatStructure {
    PILLAR("marble_pillar", true, 30),
    PILLAR_LEANING("marble_pillar_leaning", true, 30),
    PILLAR_THIN("marble_pillar_thin", true, 20),
    PILLAR_COLLECTION("marble_pillar_collection", true, 20),
    HUT("marble_hut", true, 25),
    CHEESE_STATUETTE("cheese_statuette", true, 25),
    SMALL_AQUADUCT("marble_small_aquaduct", false, 0),
    LARGE_AQUADUCT("marble_large_aquaduct", false, 0),
    SPHINX("marble_rat_sphinx", true, 5),
    LINCOLN("marble_rat_lincoln", true, 7),
    HEAD("marble_rat_head", true, 9),
    COLOSSUS("marble_rat_colossus", true, 5),
    TOWER("marble_tower", true, 20),
    TEMPLE("marble_temple", true, 20),
    PALACE("marble_palace", true, 30),
    FORUM("marble_forum", true, 20),
    GIANT_CHEESE("marble_giant_cheese", true, 4);

    private static final BlockState MARBLED_CHEESE_TILE = RatlantisBlockRegistry.MARBLED_CHEESE_TILE.getDefaultState();
    private static final BlockState MARBLED_CHEESE_BRICK = RatlantisBlockRegistry.MARBLED_CHEESE_BRICK.getDefaultState();
    private static final BlockState MARBLED_CHEESE_BRICK_MOSSY = RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_MOSSY.getDefaultState();
    private static final BlockState MARBLED_CHEESE_BRICK_CRACK = RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_CRACKED.getDefaultState();
    private static final BlockState MARBLED_GRASS = RatlantisBlockRegistry.MARBLED_CHEESE_GRASS.getDefaultState();
    private static final BlockState MARBLED_DIRT = RatlantisBlockRegistry.MARBLED_CHEESE_DIRT.getDefaultState();
    private static NavigableMap<Float, RatStructure> weightMap = new TreeMap<Float, RatStructure>();
    private static float totalWeight;
    public ResourceLocation structureLoc;
    private boolean gen;
    private int weight;

    RatStructure(String file, boolean gen, int weight) {
        structureLoc = new ResourceLocation(RatsMod.MODID, file);
    }

    public static BlockState getRandomCrackedBlock(@Nullable BlockState prev, Random random) {
        float rand = random.nextFloat();
        if (prev != null && prev.getBlock() == Blocks.GRASS) {
            if (rand < 0.075) {
                return MARBLED_CHEESE_TILE;
            } else if (rand < 0.3) {
                return MARBLED_CHEESE_BRICK;
            } else if (rand < 0.55) {
                return MARBLED_CHEESE_BRICK_CRACK;
            } else if (rand < 0.75) {
                return MARBLED_CHEESE_BRICK_MOSSY;
            } else {
                return MARBLED_GRASS;
            }
        } else {
            if (rand < 0.3) {
                return MARBLED_CHEESE_BRICK;
            } else if (rand < 0.6) {
                return MARBLED_CHEESE_BRICK_CRACK;
            } else {
                return MARBLED_CHEESE_BRICK_MOSSY;
            }
        }
    }
}
