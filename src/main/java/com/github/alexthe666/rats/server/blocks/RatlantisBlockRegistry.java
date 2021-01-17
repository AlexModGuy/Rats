package com.github.alexthe666.rats.server.blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;

public class RatlantisBlockRegistry {
    public static final Block RATLANTIS_REACTOR = new BlockRatlantisReactor();
    public static final Block RATGLOVE_FLOWER = new BlockRatgloveFlower();
    public static final Block CHUNKY_CHEESE_TOKEN = new BlockChunkyCheeseToken();
    public static final Block CHEESE_ORE = new BlockGeneric("cheese_ore", Material.ROCK, 2.0F, 3.0F, SoundType.STONE);
    public static final Block RATLANTEAN_GEM_ORE = new BlockGeneric("ratlantean_gem_ore", Material.ROCK, 8.0F, 10.0F, SoundType.STONE);
    public static final Block ORATCHALCUM_ORE = new BlockGeneric("oratchalcum_ore", Material.ROCK, 12.0F, 100.0F, SoundType.STONE);
    public static final Block ORATCHALCUM_BLOCK = new BlockGeneric("oratchalcum_block", Material.ROCK, 10.0F, 100.0F, SoundType.STONE);
    public static final Block MARBLED_CHEESE = new BlockGeneric("marbled_cheese", Material.ROCK, 2.0F, 10.0F, SoundType.STONE);
    public static final Block BLACK_MARBLED_CHEESE = new BlockGeneric("black_marbled_cheese", Material.ROCK, 2.0F, 10.0F, SoundType.STONE);
    public static final Block MARBLED_CHEESE_SLAB = new BlockGenericSlab(Block.Properties.create(Material.ROCK, MaterialColor.WHITE_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE), "marbled_cheese_slab");
    public static final Block MARBLED_CHEESE_STAIRS = new BlockGenericStairs(MARBLED_CHEESE.getDefaultState(), Block.Properties.from(MARBLED_CHEESE), "marbled_cheese_stairs");
    public static final Block MARBLED_CHEESE_TILE = new BlockGeneric("marbled_cheese_tile", Material.ROCK, 2.0F, 10.0F, SoundType.STONE);
    public static final Block MARBLED_CHEESE_CHISELED = new BlockGeneric("marbled_cheese_chiseled", Material.ROCK, 2.0F, 10.0F, SoundType.STONE);
    public static final Block MARBLED_CHEESE_PILLAR = new BlockGenericPillar("marbled_cheese_pillar", Material.ROCK, 2.0F, 10.0F, SoundType.STONE);
    public static final Block MARBLED_CHEESE_BRICK = new BlockGeneric("marbled_cheese_brick", Material.ROCK, 2.0F, 10.0F, SoundType.STONE);
    public static final Block MARBLED_CHEESE_BRICK_SLAB = new BlockGenericSlab(Block.Properties.create(Material.ROCK, MaterialColor.WHITE_TERRACOTTA).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE), "marbled_cheese_brick_slab");
    public static final Block MARBLED_CHEESE_BRICK_STAIRS = new BlockGenericStairs(MARBLED_CHEESE_BRICK.getDefaultState(), Block.Properties.from(MARBLED_CHEESE_BRICK), "marbled_cheese_brick_stairs");
    public static final Block MARBLED_CHEESE_BRICK_CHISELED = new BlockGeneric("marbled_cheese_brick_chiseled", Material.ROCK, 2.0F, 10.0F, SoundType.STONE);
    public static final Block MARBLED_CHEESE_BRICK_CRACKED = new BlockGeneric("marbled_cheese_brick_cracked", Material.ROCK, 2.0F, 10.0F, SoundType.STONE);
    public static final Block MARBLED_CHEESE_BRICK_MOSSY = new BlockGeneric("marbled_cheese_brick_mossy", Material.ROCK, 2.0F, 10.0F, SoundType.STONE);
    public static final Block MARBLED_CHEESE_DIRT = new BlockGeneric("marbled_cheese_dirt", Material.ORGANIC, 0.5F, 0F, SoundType.GROUND);
    public static final Block MARBLED_CHEESE_GRASS = new BlockMarbledCheeseGrass();
    public static final Block MARBLED_CHEESE_RAT_HEAD = new BlockMarbledCheeseRatHead();
    public static final Block MARBLED_CHEESE_GOLEM_CORE = new BlockGeneric("marbled_cheese_golem_core", Material.ROCK, 5.0F, 30.0F, SoundType.STONE, 6);
    public static final Block RATLANTIS_PORTAL = new BlockRatlantisPortal();
    public static final Block COMPRESSED_RAT = new BlockCompressedRat();
    public static final Block BRAIN_BLOCK = new BlockBrain();
    public static final Block PIRAT_PLANKS = new Block(Block.Properties.create(Material.WOOD, MaterialColor.GREEN).setLightLevel((p) -> 3).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)).setRegistryName("rats:pirat_planks");
    public static final Block PIRAT_LOG = new RotatedPillarBlock(Block.Properties.create(Material.WOOD, MaterialColor.BROWN).setLightLevel((p) -> 3).hardnessAndResistance(2.0F).sound(SoundType.WOOD)).setRegistryName("rats:pirat_log");
    public static final Block STRIPPED_PIRAT_LOG = new RotatedPillarBlock(Block.Properties.create(Material.WOOD, MaterialColor.GREEN).setLightLevel((p) -> 3).hardnessAndResistance(2.0F).sound(SoundType.WOOD)).setRegistryName("rats:stripped_pirat_log");
    public static final Block PIRAT_WOOD = new RotatedPillarBlock(Block.Properties.create(Material.WOOD, MaterialColor.GREEN).setLightLevel((p) -> 3).hardnessAndResistance(2.0F).sound(SoundType.WOOD)).setRegistryName("rats:pirat_wood");
    public static final Block STRIPPED_PIRAT_WOOD = new RotatedPillarBlock(Block.Properties.create(Material.WOOD, MaterialColor.GREEN).setLightLevel((p) -> 3).hardnessAndResistance(2.0F).sound(SoundType.WOOD)).setRegistryName("rats:stripped_pirat_wood");
    public static final Block PIRAT_PRESSURE_PLATE = new BlockGenericPressurePlate(PressurePlateBlock.Sensitivity.EVERYTHING, Block.Properties.create(Material.WOOD, MaterialColor.GREEN).setLightLevel((p) -> 3).doesNotBlockMovement().hardnessAndResistance(0.5F).sound(SoundType.WOOD)).setRegistryName("rats:pirat_pressure_plate");
    public static final Block PIRAT_TRAPDOOR = new BlockGenericTrapDoor(Block.Properties.create(Material.WOOD, MaterialColor.GREEN).setLightLevel((p) -> 3).hardnessAndResistance(3.0F).sound(SoundType.WOOD)).setRegistryName("rats:pirat_trapdoor");
    public static final Block PIRAT_STAIRS = new BlockGenericStairs(PIRAT_PLANKS.getDefaultState(), Block.Properties.from(PIRAT_PLANKS), "pirat_stairs");
    public static final Block PIRAT_BUTTON = new BlockGenericButton(Block.Properties.create(Material.MISCELLANEOUS).setLightLevel((p) -> 3).doesNotBlockMovement().hardnessAndResistance(0.5F).sound(SoundType.WOOD)).setRegistryName("rats:pirat_button");
    public static final Block PIRAT_SLAB = new SlabBlock(Block.Properties.create(Material.WOOD, MaterialColor.GREEN).setLightLevel((p) -> 3).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)).setRegistryName("rats:pirat_slab");
    public static final Block PIRAT_FENCE_GATE = new FenceGateBlock(Block.Properties.create(Material.WOOD, MaterialColor.GREEN).setLightLevel((p) -> 3).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)).setRegistryName("rats:pirat_fence_gate");
    public static final Block PIRAT_FENCE = new FenceBlock(Block.Properties.create(Material.WOOD, MaterialColor.GREEN).setLightLevel((p) -> 3).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)).setRegistryName("rats:pirat_fence");
    public static final Block PIRAT_DOOR = new BlockGenericDoor(Block.Properties.create(Material.WOOD, MaterialColor.GREEN).setLightLevel((p) -> 3).hardnessAndResistance(3.0F).sound(SoundType.WOOD)).setRegistryName("rats:pirat_door");
    public static final Block DUTCHRAT_BELL = new BlockDutchratBell();
    public static final Block AIR_RAID_SIREN = new BlockAirRaidSiren();

}
