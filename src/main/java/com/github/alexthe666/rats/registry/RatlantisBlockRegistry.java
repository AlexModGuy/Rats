package com.github.alexthe666.rats.registry;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.block.*;
import com.github.alexthe666.rats.server.items.RatsBlockItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class RatlantisBlockRegistry {

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, RatsMod.MODID);

	public static final RegistryObject<Block> RATLANTIS_REACTOR = register("ratlantis_reactor", () -> new RatlantisReactorBlock(Block.Properties.of(Material.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(100.0F).lightLevel(value -> 15)));
	public static final RegistryObject<Block> RATGLOVE_FLOWER = register("ratglove_flower", () -> new FlowerBlock(RatsEffectRegistry.CONFIT_BYALDI, 6, Block.Properties.of(Material.PLANT).noCollission().randomTicks().sound(SoundType.CROP).offsetType(BlockBehaviour.OffsetType.XZ)) {
		@Override
		public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
			Vec3 vec3 = state.getOffset(getter, pos);
			return Block.box(5.0D, 0.0D, 5.0D, 11.0D, 13.0D, 11.0D).move(vec3.x(), vec3.y(), vec3.z());
		}
	});
	public static final RegistryObject<Block> POTTED_RATGLOVE_FLOWER = register("potted_ratglove_flower", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, RATGLOVE_FLOWER, BlockBehaviour.Properties.copy(Blocks.FLOWER_POT)));
	public static final RegistryObject<Block> CHUNKY_CHEESE_TOKEN = register("chunky_cheese_token", () -> new ChunkyCheeseTokenBlock(BlockBehaviour.Properties.of(Material.METAL).sound(SoundType.METAL).noOcclusion().strength(2.0F, 1000.0F)));
	public static final RegistryObject<Block> CHEESE_ORE = register("cheese_ore", () -> new Block(BlockBehaviour.Properties.of(Material.STONE).sound(SoundType.STONE).requiresCorrectToolForDrops().strength(2.0F, 3.0F)));
	public static final RegistryObject<Block> RATLANTEAN_GEM_ORE = register("ratlantean_gem_ore", () -> new Block(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(10.0F, 100.0F)));
	public static final RegistryObject<Block> ORATCHALCUM_ORE = register("oratchalcum_ore", () -> new Block(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(12.0F, 100.0F)));
	public static final RegistryObject<Block> ORATCHALCUM_BLOCK = register("oratchalcum_block", () -> new Block(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(10.0F, 100.0F)));
	public static final RegistryObject<Block> MARBLED_CHEESE = register("marbled_cheese", () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_WHITE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(2.0F, 10.0F)));
	public static final RegistryObject<Block> BLACK_MARBLED_CHEESE = register("black_marbled_cheese", () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_BLACK).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(2.0F, 10.0F)));
	public static final RegistryObject<Block> MARBLED_CHEESE_SLAB = register("marbled_cheese_slab", () -> new SlabBlock(Block.Properties.copy(MARBLED_CHEESE.get())));
	public static final RegistryObject<Block> MARBLED_CHEESE_STAIRS = register("marbled_cheese_stairs", () -> new StairBlock(() -> MARBLED_CHEESE.get().defaultBlockState(), Block.Properties.copy(MARBLED_CHEESE.get())));
	public static final RegistryObject<Block> MARBLED_CHEESE_TILE = register("marbled_cheese_tile", () -> new Block(Block.Properties.copy(MARBLED_CHEESE.get())));
	public static final RegistryObject<Block> MARBLED_CHEESE_CHISELED = register("marbled_cheese_chiseled", () -> new Block(Block.Properties.copy(MARBLED_CHEESE.get())));
	public static final RegistryObject<Block> MARBLED_CHEESE_PILLAR = register("marbled_cheese_pillar", () -> new RotatedPillarBlock(Block.Properties.copy(MARBLED_CHEESE.get())));
	public static final RegistryObject<Block> MARBLED_CHEESE_BRICK = register("marbled_cheese_brick", () -> new Block(Block.Properties.copy(MARBLED_CHEESE.get())));
	public static final RegistryObject<Block> MARBLED_CHEESE_BRICK_SLAB = register("marbled_cheese_brick_slab", () -> new SlabBlock(Block.Properties.copy(MARBLED_CHEESE_BRICK.get())));
	public static final RegistryObject<Block> MARBLED_CHEESE_BRICK_STAIRS = register("marbled_cheese_brick_stairs", () -> new StairBlock(() -> MARBLED_CHEESE_BRICK.get().defaultBlockState(), Block.Properties.copy(MARBLED_CHEESE_BRICK.get())));
	public static final RegistryObject<Block> MARBLED_CHEESE_BRICK_CHISELED = register("marbled_cheese_brick_chiseled", () -> new Block(Block.Properties.copy(MARBLED_CHEESE_BRICK.get())));
	public static final RegistryObject<Block> MARBLED_CHEESE_BRICK_CRACKED = register("marbled_cheese_brick_cracked", () -> new Block(Block.Properties.copy(MARBLED_CHEESE_BRICK.get())));
	public static final RegistryObject<Block> MARBLED_CHEESE_BRICK_CRACKED_SLAB = register("marbled_cheese_brick_cracked_slab", () -> new SlabBlock(Block.Properties.copy(MARBLED_CHEESE_BRICK_CRACKED.get())));
	public static final RegistryObject<Block> MARBLED_CHEESE_BRICK_CRACKED_STAIRS = register("marbled_cheese_brick_cracked_stairs", () -> new StairBlock(() -> MARBLED_CHEESE_BRICK_CRACKED.get().defaultBlockState(), Block.Properties.copy(MARBLED_CHEESE_BRICK_CRACKED.get())));
	public static final RegistryObject<Block> MARBLED_CHEESE_BRICK_MOSSY = register("marbled_cheese_brick_mossy", () -> new Block(Block.Properties.copy(MARBLED_CHEESE_BRICK.get())));
	public static final RegistryObject<Block> MARBLED_CHEESE_BRICK_MOSSY_SLAB = register("marbled_cheese_brick_mossy_slab", () -> new SlabBlock(Block.Properties.copy(MARBLED_CHEESE_BRICK_MOSSY.get())));
	public static final RegistryObject<Block> MARBLED_CHEESE_BRICK_MOSSY_STAIRS = register("marbled_cheese_brick_mossy_stairs", () -> new StairBlock(() -> MARBLED_CHEESE_BRICK_MOSSY.get().defaultBlockState(), Block.Properties.copy(MARBLED_CHEESE_BRICK_MOSSY.get())));
	public static final RegistryObject<Block> MARBLED_CHEESE_DIRT = register("marbled_cheese_dirt", () -> new Block(BlockBehaviour.Properties.of(Material.DIRT, MaterialColor.DIRT).strength(0.5F, 0.0F).sound(SoundType.GRAVEL)));
	public static final RegistryObject<Block> MARBLED_CHEESE_GRASS = register("marbled_cheese_grass", () -> new MarbledCheeseGrassBlock(Block.Properties.of(Material.GRASS).strength(0.6F, 0.0F).sound(SoundType.GRASS)));
	public static final RegistryObject<Block> MARBLED_CHEESE_RAT_HEAD = register("marbled_cheese_rat_head", () -> new RatlanteanAutomatonHeadBlock(Block.Properties.of(Material.STONE).sound(SoundType.STONE).lightLevel(value -> 5).noCollission().dynamicShape().strength(1.0F, 0)));
	public static final RegistryObject<Block> MARBLED_CHEESE_GOLEM_CORE = register("marbled_cheese_golem_core", () -> new Block(Block.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(5.0F, 30.0F).sound(SoundType.STONE).lightLevel(value -> 6)));
	public static final RegistryObject<Block> RATLANTIS_PORTAL = register("ratlantis_portal", () -> new RatlantisPortalBlock(Block.Properties.of(Material.PORTAL).sound(SoundType.BONE_BLOCK).strength(-1.0F).lightLevel(value -> 15).noCollission()));
	public static final RegistryObject<Block> COMPRESSED_RAT = register("compressed_rat", () -> new CompressedRatBlock(Block.Properties.of(Material.WOOL).sound(SoundType.WOOL).strength(0.6F, 0.0F)));
	public static final RegistryObject<Block> BRAIN_BLOCK = register("brain_block", () -> new SetupHorizontalBlock(Block.Properties.of(Material.WOOL).sound(SoundType.SLIME_BLOCK).strength(0.6F, 0.0F)));
	public static final RegistryObject<Block> PIRAT_PLANKS = register("pirat_planks", () -> new Block(Block.Properties.of(Material.WOOD, MaterialColor.COLOR_GREEN).lightLevel(value -> 3).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> PIRAT_LOG = register("pirat_log", () -> new RotatedPillarBlock(Block.Properties.of(Material.WOOD, MaterialColor.COLOR_BROWN).lightLevel(value -> 3).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> STRIPPED_PIRAT_LOG = register("stripped_pirat_log", () -> new RotatedPillarBlock(Block.Properties.of(Material.WOOD, MaterialColor.COLOR_GREEN).lightLevel(value -> 3).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> PIRAT_WOOD = register("pirat_wood", () -> new RotatedPillarBlock(Block.Properties.of(Material.WOOD, MaterialColor.COLOR_GREEN).lightLevel(value -> 3).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> STRIPPED_PIRAT_WOOD = register("stripped_pirat_wood", () -> new RotatedPillarBlock(Block.Properties.of(Material.WOOD, MaterialColor.COLOR_GREEN).lightLevel(value -> 3).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> PIRAT_PRESSURE_PLATE = register("pirat_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.copy(PIRAT_PLANKS.get()), RatsMod.PIRAT_WOOD_SET));
	public static final RegistryObject<Block> PIRAT_TRAPDOOR = register("pirat_trapdoor", () -> new TrapDoorBlock(BlockBehaviour.Properties.copy(PIRAT_PLANKS.get()), RatsMod.PIRAT_WOOD_SET));
	public static final RegistryObject<Block> PIRAT_STAIRS = register("pirat_stairs", () -> new StairBlock(() -> PIRAT_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(PIRAT_PLANKS.get())));
	public static final RegistryObject<Block> PIRAT_BUTTON = register("pirat_button", () -> new ButtonBlock(BlockBehaviour.Properties.copy(PIRAT_PLANKS.get()), RatsMod.PIRAT_WOOD_SET, 30, true));
	public static final RegistryObject<Block> PIRAT_SLAB = register("pirat_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(PIRAT_PLANKS.get())));
	public static final RegistryObject<Block> PIRAT_FENCE_GATE = register("pirat_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties.copy(PIRAT_PLANKS.get()), RatsMod.PIRAT_WOOD_TYPE));
	public static final RegistryObject<Block> PIRAT_FENCE = register("pirat_fence", () -> new FenceBlock(BlockBehaviour.Properties.copy(PIRAT_PLANKS.get())));
	public static final RegistryObject<Block> PIRAT_DOOR = register("pirat_door", () -> new DoorBlock(BlockBehaviour.Properties.copy(PIRAT_PLANKS.get()), RatsMod.PIRAT_WOOD_SET));
	public static final RegistryObject<Block> DUTCHRAT_BELL = register("dutchrat_bell", () -> new DutchratBellBlock(Block.Properties.of(Material.METAL, MaterialColor.GOLD).strength(5.0F).requiresCorrectToolForDrops().sound(SoundType.ANVIL)));
	public static final RegistryObject<Block> AIR_RAID_SIREN = register("air_raid_siren", () -> new AirRaidSirenBlock(Block.Properties.of(Material.METAL).sound(SoundType.METAL).strength(5.0F, 1000.0F).dynamicShape()));

	public static RegistryObject<Block> register(String name, Supplier<Block> blockSupplier) {
		RegistryObject<Block> ret = BLOCKS.register(name, blockSupplier);
		RatlantisItemRegistry.ITEMS.register(name, () -> new RatsBlockItem(ret.get(), new Item.Properties()));
		return ret;
	}
}
