package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.tile.RatsTileEntityRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.LanguageMap;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

import java.lang.reflect.Field;


@Mod.EventBusSubscriber(modid = RatsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(RatsMod.MODID)
public class RatsBlockRegistry {

    public static final Block BLOCK_OF_CHEESE = new BlockCheese();
    public static final Block MILK_CAULDRON = new BlockMilkCauldron();
    public static final Block CHEESE_CAULDRON = new BlockCheeseCauldron();
    public static final Block RAT_HOLE = new BlockRatHole();
    public static final Block RAT_TRAP = new BlockRatTrap();
    public static final Block RAT_CAGE = new BlockRatCage("rat_cage");
    public static final Block RAT_CAGE_DECORATED = new BlockRatCageDecorated();
    public static final Block RAT_CAGE_BREEDING_LANTERN = new BlockRatCageBreedingLantern();
    public static final Block FISH_BARREL = new BlockGenericFacing("fish_barrel", Material.WOOD, 2.0F, 10.0F, SoundType.WOOD);
    public static final Block RAT_CRAFTING_TABLE = new BlockRatCraftingTable();
    public static final Block AUTO_CURDLER = new BlockAutoCurdler();
    public static final Block[] RAT_TUBE_COLOR = new Block[16];
    public static final Block GARBAGE_PILE = new BlockGarbage();
    public static final Block RATGLOVE_FLOWER = new BlockRatgloveFlower();
    public static final Block MARBLED_CHEESE_RAW = new BlockGeneric("marbled_cheese_raw", Material.ROCK, 2.0F, 10.0F, SoundType.STONE);
    public static final Block MARBLED_CHEESE = new BlockGeneric("marbled_cheese", Material.ROCK, 2.0F, 10.0F, SoundType.STONE);
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
    public static final Block JACK_O_RATERN = new BlockJackORatern();
    public static final Block UPGRADE_COMBINER = new BlockUpgradeCombiner();
    public static final Block UPGRADE_SEPARATOR = new BlockUpgradeSeparator();
    public static Fluid MILK_FLUID = new Fluid("milk", new ResourceLocation("rats", "blocks/milk"), new ResourceLocation("rats", "blocks/milk_flowing"), new ResourceLocation("rats", "blocks/milk_flowing")) {
        @Override
        public String getLocalizedName(FluidStack fs) {
            return LanguageMap.getInstance().translateKey("tile.rats.milk.name");
        }
    }.setUnlocalizedName("rats.milk");

    static {
        for (int i = 0; i < 16; i++) {
            RAT_TUBE_COLOR[i] = new BlockRatTube(DyeColor.byId(i).getName());
        }
    }

    static {
        //FluidRegistry.registerFluid(MILK_FLUID);
    }


    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        try {
            for (Field f : RatsBlockRegistry.class.getDeclaredFields()) {
                Object obj = f.get(null);
                if (obj instanceof Block) {
                    event.getRegistry().register((Block) obj);
                } else if (obj instanceof Block[]) {
                    for (Block block : (Block[]) obj) {
                        event.getRegistry().register(block);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @SubscribeEvent
    public static void registerBlockItems(RegistryEvent.Register<Item> event) {
        try {
            for (Field f : RatsBlockRegistry.class.getDeclaredFields()) {
                Object obj = f.get(null);
                if (obj instanceof Block) {
                    BlockItem blockItem = new BlockItem((Block) obj, new Item.Properties().group(RatsMod.TAB));
                    blockItem.setRegistryName(((Block) obj).getRegistryName());
                    event.getRegistry().register(blockItem);
                } else if (obj instanceof Block[]) {
                    for (Block block : (Block[]) obj) {
                        BlockItem blockItem = new BlockItem(block, new Item.Properties().group(RatsMod.TAB));
                        blockItem.setRegistryName(block.getRegistryName());
                        event.getRegistry().register(blockItem);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @SubscribeEvent
    public static void registerTileEntities(final RegistryEvent.Register<TileEntityType<?>> event) {
        try {
            for (Field f : RatsTileEntityRegistry.class.getDeclaredFields()) {
                Object obj = f.get(null);
                if (obj instanceof TileEntityType) {
                    event.getRegistry().register((TileEntityType) obj);
                } else if (obj instanceof TileEntityType[]) {
                    for (TileEntityType te : (TileEntityType[]) obj) {
                        event.getRegistry().register(te);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}