package com.github.alexthe666.rats.registry;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.block.*;
import com.github.alexthe666.rats.server.items.RatsBlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class RatsBlockRegistry {

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, RatsMod.MODID);

	public static final RegistryObject<Block> BLOCK_OF_CHEESE = register("block_of_cheese", () -> new Block(Block.Properties.of(Material.WOOL).sound(SoundType.SLIME_BLOCK).strength(0.6F, 0.0F)));
	public static final RegistryObject<Block> MILK_CAULDRON = register("cauldron_milk", () -> new MilkCauldronBlock(Block.Properties.copy(Blocks.CAULDRON)));
	public static final RegistryObject<Block> CHEESE_CAULDRON = register("cauldron_cheese", () -> new CheeseCauldronBlock(Block.Properties.copy(Blocks.CAULDRON), RatsBlockRegistry.BLOCK_OF_CHEESE, RatsCauldronRegistry.CHEESE));
	public static final RegistryObject<Block> BLUE_CHEESE_CAULDRON = register("cauldron_blue_cheese", () -> new CheeseCauldronBlock(Block.Properties.copy(Blocks.CAULDRON), RatsBlockRegistry.BLOCK_OF_BLUE_CHEESE, RatsCauldronRegistry.BLUE_CHEESE));
	public static final RegistryObject<Block> NETHER_CHEESE_CAULDRON = register("cauldron_nether_cheese", () -> new CheeseCauldronBlock(Block.Properties.copy(Blocks.CAULDRON), RatsBlockRegistry.BLOCK_OF_NETHER_CHEESE, RatsCauldronRegistry.NETHER_CHEESE));
	public static final RegistryObject<Block> RAT_HOLE = BLOCKS.register("rat_hole", () -> new RatHoleBlock(Block.Properties.of(Material.WOOD).sound(SoundType.WOOD).noOcclusion().dynamicShape().strength(1.3F, 0.0F)));
	public static final RegistryObject<Block> RAT_CAGE = register("rat_cage", () -> new RatCageBlock(Block.Properties.of(Material.METAL).sound(SoundType.METAL).noOcclusion().strength(2.0F, 0.0F)));
	public static final RegistryObject<Block> RAT_CAGE_DECORATED = BLOCKS.register("rat_cage_decorated", () -> new RatCageDecoratedBlock(Block.Properties.copy(RAT_CAGE.get())));
	public static final RegistryObject<Block> RAT_CAGE_BREEDING_LANTERN = BLOCKS.register("rat_cage_breeding_lantern", () -> new RatCageBreedingLanternBlock(Block.Properties.copy(RAT_CAGE.get())));
	public static final RegistryObject<Block> RAT_CAGE_WHEEL = BLOCKS.register("rat_cage_wheel", () -> new RatCageWheelBlock(Block.Properties.copy(RAT_CAGE.get())));
	public static final RegistryObject<Block> FISH_BARREL = register("fish_barrel", () -> new Block(BlockBehaviour.Properties.of(Material.WOOD).strength(2.0F, 10.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> RAT_CRAFTING_TABLE = register("rat_crafting_table", () -> new RatCraftingTableBlock(Block.Properties.of(Material.WOOD).sound(SoundType.SLIME_BLOCK).strength(2.0F, 0.0F)));
	public static final RegistryObject<Block> AUTO_CURDLER = register("auto_curdler", () -> new AutoCurdlerBlock(Block.Properties.of(Material.METAL).sound(SoundType.METAL).requiresCorrectToolForDrops().noOcclusion().dynamicShape().strength(2.0F, 0.0F)));
	public static final RegistryObject<Block> RAT_TRAP = register("rat_trap", () -> new RatTrapBlock(Block.Properties.of(Material.WOOD).sound(SoundType.WOOD).noCollission().strength(1.0F, 0.0F)));
	public static final RegistryObject<Block> RAT_TUBE_COLOR = BLOCKS.register("rat_tube", () -> new RatTubeBlock(Block.Properties.of(Material.GLASS).sound(SoundType.WOOD).strength(0.9F, 0.0F)));
	public static final RegistryObject<Block> RAT_UPGRADE_BLOCK = register("rat_upgrade_block", () -> new RatUpgradeBlock(BlockBehaviour.Properties.of(Material.WOOL).sound(SoundType.SLIME_BLOCK).strength(0.6F, 0.0F)));
	public static final RegistryObject<Block> DYE_SPONGE = register("dye_sponge", () -> new DyeSpongeBlock(Block.Properties.of(Material.SPONGE).strength(0.6F, 0.0F).sound(SoundType.CROP)));
	public static final RegistryObject<Block> GARBAGE_PILE = register("garbage_pile", () -> new GarbageBlock(Block.Properties.of(Material.DIRT).sound(SoundType.GRAVEL).strength(0.7F, 1.0F).randomTicks(), 1.0F));
	public static final RegistryObject<Block> CURSED_GARBAGE = register("cursed_garbage", () -> new CursedGarbageBlock(Block.Properties.copy(GARBAGE_PILE.get())));
	public static final RegistryObject<Block> COMPRESSED_GARBAGE = register("compressed_garbage", () -> new GarbageBlock(Block.Properties.copy(GARBAGE_PILE.get()), 2.0F));
	public static final RegistryObject<Block> PURIFIED_GARBAGE = register("purified_garbage", () -> new PurifiedGarbageBlock(Block.Properties.copy(GARBAGE_PILE.get())));
	public static final RegistryObject<Block> PIED_GARBAGE = register("pied_garbage", () -> new PiedGarbageBlock(Block.Properties.copy(GARBAGE_PILE.get())));
	public static final RegistryObject<Block> MARBLED_CHEESE_RAW = register("marbled_cheese_raw", () -> new Block(Block.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(2.0F, 10.0F).sound(SoundType.STONE)));
	public static final RegistryObject<Block> JACK_O_RATERN = register("jack_o_ratern", () -> new SetupHorizontalBlock(Block.Properties.of(Material.VEGETABLE).sound(SoundType.WOOD).strength(1.0F, 0).lightLevel(value -> 15)));
	public static final RegistryObject<Block> BLOCK_OF_BLUE_CHEESE = register("block_of_blue_cheese", () -> new Block(Block.Properties.of(Material.WOOL).strength(0.6F, 0.0F).sound(SoundType.SLIME_BLOCK)));
	public static final RegistryObject<Block> BLOCK_OF_NETHER_CHEESE = register("block_of_nether_cheese", () -> new Block(Block.Properties.of(Material.WOOL).strength(0.6F, 0.0F).sound(SoundType.SLIME_BLOCK)));
	public static final RegistryObject<Block> PIED_WOOL = register("pied_wool", () -> new Block(Block.Properties.of(Material.WOOL).strength(1.0F, 0.0F).sound(SoundType.WOOL)));
	public static final RegistryObject<Block> UPGRADE_COMBINER = register("upgrade_combiner", () -> new UpgradeCombinerBlock(Block.Properties.of(Material.STONE).sound(SoundType.WOOD).requiresCorrectToolForDrops().noOcclusion().dynamicShape().strength(5.0F, 0.0F).lightLevel(value -> 4)));
	public static final RegistryObject<Block> UPGRADE_SEPARATOR = register("upgrade_separator", () -> new UpgradeSeparatorBlock(Block.Properties.of(Material.STONE).sound(SoundType.WOOD).requiresCorrectToolForDrops().noOcclusion().dynamicShape().strength(5.0F, 0.0F).lightLevel(value -> 4)));
	public static final RegistryObject<Block> MANHOLE = register("manhole", () -> new TrapDoorBlock(Block.Properties.of(Material.METAL, MaterialColor.TERRACOTTA_BROWN).requiresCorrectToolForDrops().strength(10.0F).sound(SoundType.ANVIL).noOcclusion().dynamicShape(), BlockSetType.IRON));
	public static final RegistryObject<Block> TRASH_CAN = register("trash_can", () -> new TrashCanBlock(BlockBehaviour.Properties.of(Material.METAL).sound(SoundType.METAL).requiresCorrectToolForDrops().noOcclusion().dynamicShape().strength(2.0F, 0.0F)));
	public static final RegistryObject<Block> RAT_ATTRACTOR = register("rat_attractor", () -> new RatAttractorBlock(Block.Properties.of(Material.METAL).sound(SoundType.LANTERN).requiresCorrectToolForDrops().noOcclusion().dynamicShape().randomTicks().strength(1.0F, 0.0F)));
	public static final RegistryObject<Block> RAT_QUARRY = register("rat_quarry", () -> new RatQuarryBlock(Block.Properties.of(Material.WOOD).sound(SoundType.SLIME_BLOCK).strength(2.0F, 0.0F)));
	public static final RegistryObject<Block> RAT_QUARRY_PLATFORM = register("rat_quarry_platform", () -> new RatQuarryPlatformBlock(Block.Properties.of(Material.WOOL).sound(SoundType.SLIME_BLOCK).noOcclusion().dynamicShape().strength(1.0F, 0.0F)));

	public static RegistryObject<Block> register(String name, Supplier<Block> blockSupplier) {
		RegistryObject<Block> ret = BLOCKS.register(name, blockSupplier);
		RatsItemRegistry.ITEMS.register(name, () -> new RatsBlockItem(ret.get(), new Item.Properties()));
		return ret;
	}
}