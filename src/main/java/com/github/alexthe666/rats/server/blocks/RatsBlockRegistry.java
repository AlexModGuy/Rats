package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.tile.RatlantisTileEntityRegistry;
import com.github.alexthe666.rats.server.entity.tile.RatsTileEntityRegistry;
import com.github.alexthe666.rats.server.items.ItemBlockWearable;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.lang.reflect.Field;


@Mod.EventBusSubscriber(modid = RatsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RatsBlockRegistry {

    public static final Block BLOCK_OF_CHEESE = new BlockCheese();
    public static final Block MILK_CAULDRON = new BlockMilkCauldron();
    public static final Block CHEESE_CAULDRON = new BlockCheeseCauldron();
    public static final Block RAT_HOLE = new BlockRatHole();
    public static final Block RAT_CAGE = new BlockRatCage("rat_cage");
    public static final Block RAT_CAGE_DECORATED = new BlockRatCageDecorated();
    public static final Block RAT_CAGE_BREEDING_LANTERN = new BlockRatCageBreedingLantern();
    public static final Block RAT_CAGE_WHEEL = new BlockRatCageWheel();
    public static final Block FISH_BARREL = new BlockGenericFacing("fish_barrel", Material.WOOD, 2.0F, 10.0F, SoundType.WOOD);
    public static final Block RAT_CRAFTING_TABLE = new BlockRatCraftingTable();
    public static final Block AUTO_CURDLER = new BlockAutoCurdler();
    public static final Block RAT_TRAP = new BlockRatTrap();
    public static final Block RAT_TUBE_COLOR = new BlockRatTube();
    public static final Block RAT_UPGRADE_BLOCK = new BlockRatUpgrade();
    public static final Block DYE_SPONGE = new BlockDyeSponge();
    public static final Block GARBAGE_PILE = new BlockGarbage("garbage_pile", 1.0D);
    public static final Block CURSED_GARBAGE = new BlockCursedGarbage();
    public static final Block COMPRESSED_GARBAGE = new BlockGarbage("compressed_garbage", 2.0D);
    public static final Block PURIFIED_GARBAGE = new BlockPurifiedGarbage();
    public static final Block PIED_GARBAGE = new BlockPiedGarbage();
    public static final Block MARBLED_CHEESE_RAW = new BlockGeneric("marbled_cheese_raw", Material.ROCK, 2.0F, 10.0F, SoundType.STONE);
    public static final Block JACK_O_RATERN = new BlockJackORatern();
    public static final Block BLOCK_OF_BLUE_CHEESE = new BlockGeneric("block_of_blue_cheese", Material.WOOL, 2.0F, 0F, SoundType.SLIME);
    public static final Block PIED_WOOL = new BlockGeneric("pied_wool", Material.WOOL, 1.0F, 0F, SoundType.CLOTH);
    public static final Block UPGRADE_COMBINER = new BlockUpgradeCombiner();
    public static final Block UPGRADE_SEPARATOR = new BlockUpgradeSeparator();
    public static final Block MANHOLE = new BlockGenericTrapDoor(Block.Properties.create(Material.IRON, MaterialColor.BROWN_TERRACOTTA).hardnessAndResistance(10.0F).sound(SoundType.ANVIL).notSolid().variableOpacity()).setRegistryName("rats:manhole");
    public static final Block TRASH_CAN = new BlockTrashCan();
    public static final Block RAT_ATTRACTOR = new BlockRatAttractor();
    public static final Block RAT_QUARRY = new BlockRatQuarry();
    public static final Block RAT_QUARRY_PLATFORM = new BlockRatQuarryPlatform();


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
        if(RatsMod.RATLANTIS_LOADED){
            try {
                for (Field f : RatlantisBlockRegistry.class.getDeclaredFields()) {
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
    }

    @SubscribeEvent
    public static void registerBlockItems(RegistryEvent.Register<Item> event) {
        try {
            for (Field f : RatsBlockRegistry.class.getDeclaredFields()) {
                Object obj = f.get(null);
                if (obj instanceof Block) {
                    Item.Properties props = new Item.Properties();
                    if (!(obj instanceof INoTab)) {
                        props.group(RatsMod.TAB);
                    }
                    if (obj instanceof IUsesTEISR) {
                        props = RatsMod.PROXY.setupTEISR(props);
                    }
                    BlockItem blockItem = new BlockItem((Block) obj, props);
                    if(obj instanceof IWearableBlock){
                        blockItem = new ItemBlockWearable((Block) obj, props);
                    }
                    blockItem.setRegistryName(((Block) obj).getRegistryName());
                    event.getRegistry().register(blockItem);
                } else if (obj instanceof Block[]) {
                    for (Block block : (Block[]) obj) {
                        Item.Properties props = new Item.Properties();
                        if (!(block instanceof INoTab)) {
                            props.group(RatsMod.TAB);
                        }
                        if (block instanceof IUsesTEISR) {
                            RatsMod.PROXY.setupTEISR(props);
                        }
                        BlockItem blockItem = new BlockItem(block, props);
                        blockItem.setRegistryName(block.getRegistryName());
                        event.getRegistry().register(blockItem);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        if(RatsMod.RATLANTIS_LOADED) {
            try {
                for (Field f : RatlantisBlockRegistry.class.getDeclaredFields()) {
                    Object obj = f.get(null);
                    if (obj instanceof Block) {
                        Item.Properties props = new Item.Properties();
                        if (!(obj instanceof INoTab)) {
                            props.group(RatsMod.getRatlantisTab());
                        }
                        if (obj instanceof IUsesTEISR) {
                            props = RatsMod.PROXY.setupTEISR(props);
                        }
                        BlockItem blockItem = new BlockItem((Block) obj, props);
                        if(obj instanceof IWearableBlock){
                            blockItem = new ItemBlockWearable((Block) obj, props);
                        }
                        blockItem.setRegistryName(((Block) obj).getRegistryName());
                        event.getRegistry().register(blockItem);
                    } else if (obj instanceof Block[]) {
                        for (Block block : (Block[]) obj) {
                            Item.Properties props = new Item.Properties();
                            if (!(block instanceof INoTab)) {
                                props.group(RatsMod.getRatlantisTab());
                            }
                            if (block instanceof IUsesTEISR) {
                                RatsMod.PROXY.setupTEISR(props);
                            }
                            BlockItem blockItem = new BlockItem(block, props);
                            blockItem.setRegistryName(block.getRegistryName());
                            event.getRegistry().register(blockItem);
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
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
        if(RatsMod.RATLANTIS_LOADED){
            try {
                for (Field f : RatlantisTileEntityRegistry.class.getDeclaredFields()) {
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
}