package com.github.alexthe666.rats.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.data.ratlantis.tags.RatlantisBlockTags;
import com.github.alexthe666.rats.server.block.*;
import com.github.alexthe666.rats.server.block.entity.PiratHangingSignBlockEntity;
import com.github.alexthe666.rats.server.block.entity.PiratSignBlockEntity;
import com.github.alexthe666.rats.server.items.RatsBlockItem;
import com.github.alexthe666.rats.server.world.PiratTreeGrower;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class RatlantisBlockRegistry {

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, RatsMod.MODID);

	public static final DeferredHolder<Block, Block> RATLANTIS_REACTOR = register("ratlantis_reactor", () -> new RatlantisReactorBlock(Block.Properties.of().requiresCorrectToolForDrops().sound(SoundType.STONE).strength(100.0F).lightLevel(value -> 15)));
	public static final DeferredHolder<Block, Block> RATGLOVE_FLOWER = register("ratglove_flower", () -> new FlowerBlock(RatsEffectRegistry.SYNESTHESIA, 6, Block.Properties.of().pushReaction(PushReaction.DESTROY).mapColor(MapColor.PLANT).noCollission().randomTicks().sound(SoundType.CROP).offsetType(BlockBehaviour.OffsetType.XZ)) {
		@Override
		public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
			Vec3 vec3 = state.getOffset(getter, pos);
			return Block.box(5.0D, 0.0D, 5.0D, 11.0D, 13.0D, 11.0D).move(vec3.x(), vec3.y(), vec3.z());
		}
	});
	public static final DeferredHolder<Block, Block> POTTED_RATGLOVE_FLOWER = BLOCKS.register("potted_ratglove_flower", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, RATGLOVE_FLOWER, BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWER_POT)));
	public static final DeferredHolder<Block, Block> CHUNKY_CHEESE_TOKEN = register("chunky_cheese_token", () -> new ChunkyCheeseTokenBlock(BlockBehaviour.Properties.of().sound(SoundType.METAL).noOcclusion().strength(2.0F, 1000.0F)));
	public static final DeferredHolder<Block, Block> CHEESE_ORE = register("cheese_ore", () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.STONE).requiresCorrectToolForDrops().strength(2.0F, 3.0F)));
	public static final DeferredHolder<Block, Block> RATLANTEAN_GEM_ORE = register("ratlantean_gem_ore", () -> new Block(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().sound(SoundType.STONE).strength(10.0F, 100.0F)));
	public static final DeferredHolder<Block, Block> ORATCHALCUM_ORE = register("oratchalcum_ore", () -> new Block(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().sound(SoundType.STONE).strength(12.0F, 100.0F)));
	public static final DeferredHolder<Block, Block> ORATCHALCUM_BLOCK = register("oratchalcum_block", () -> new Block(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().sound(SoundType.STONE).strength(10.0F, 100.0F)));
	public static final DeferredHolder<Block, Block> MARBLED_CHEESE = register("marbled_cheese", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_WHITE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(2.0F, 10.0F)));
	public static final DeferredHolder<Block, Block> BLACK_MARBLED_CHEESE = register("black_marbled_cheese", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_BLACK).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(2.0F, 10.0F)));
	public static final DeferredHolder<Block, Block> MARBLED_CHEESE_SLAB = register("marbled_cheese_slab", () -> new SlabBlock(Block.Properties.ofFullCopy(MARBLED_CHEESE.get())));
	public static final DeferredHolder<Block, Block> MARBLED_CHEESE_STAIRS = register("marbled_cheese_stairs", () -> new StairBlock(MARBLED_CHEESE.get().defaultBlockState(), Block.Properties.ofFullCopy(MARBLED_CHEESE.get())));
	public static final DeferredHolder<Block, Block> MARBLED_CHEESE_TILE = register("marbled_cheese_tile", () -> new Block(Block.Properties.ofFullCopy(MARBLED_CHEESE.get())));
	public static final DeferredHolder<Block, Block> MARBLED_CHEESE_CHISELED = register("marbled_cheese_chiseled", () -> new Block(Block.Properties.ofFullCopy(MARBLED_CHEESE.get())));
	public static final DeferredHolder<Block, Block> MARBLED_CHEESE_PILLAR = register("marbled_cheese_pillar", () -> new RotatedPillarBlock(Block.Properties.ofFullCopy(MARBLED_CHEESE.get())));
	public static final DeferredHolder<Block, Block> MARBLED_CHEESE_BRICK = register("marbled_cheese_brick", () -> new Block(Block.Properties.ofFullCopy(MARBLED_CHEESE.get())));
	public static final DeferredHolder<Block, Block> MARBLED_CHEESE_BRICK_SLAB = register("marbled_cheese_brick_slab", () -> new SlabBlock(Block.Properties.ofFullCopy(MARBLED_CHEESE_BRICK.get())));
	public static final DeferredHolder<Block, Block> MARBLED_CHEESE_BRICK_STAIRS = register("marbled_cheese_brick_stairs", () -> new StairBlock(MARBLED_CHEESE_BRICK.get().defaultBlockState(), Block.Properties.ofFullCopy(MARBLED_CHEESE_BRICK.get())));
	public static final DeferredHolder<Block, Block> MARBLED_CHEESE_BRICK_CHISELED = register("marbled_cheese_brick_chiseled", () -> new Block(Block.Properties.ofFullCopy(MARBLED_CHEESE_BRICK.get())));
	public static final DeferredHolder<Block, Block> MARBLED_CHEESE_BRICK_CRACKED = register("marbled_cheese_brick_cracked", () -> new Block(Block.Properties.ofFullCopy(MARBLED_CHEESE_BRICK.get())));
	public static final DeferredHolder<Block, Block> MARBLED_CHEESE_BRICK_CRACKED_SLAB = register("marbled_cheese_brick_cracked_slab", () -> new SlabBlock(Block.Properties.ofFullCopy(MARBLED_CHEESE_BRICK_CRACKED.get())));
	public static final DeferredHolder<Block, Block> MARBLED_CHEESE_BRICK_CRACKED_STAIRS = register("marbled_cheese_brick_cracked_stairs", () -> new StairBlock(MARBLED_CHEESE_BRICK_CRACKED.get().defaultBlockState(), Block.Properties.ofFullCopy(MARBLED_CHEESE_BRICK_CRACKED.get())));
	public static final DeferredHolder<Block, Block> MARBLED_CHEESE_BRICK_MOSSY = register("marbled_cheese_brick_mossy", () -> new Block(Block.Properties.ofFullCopy(MARBLED_CHEESE_BRICK.get())));
	public static final DeferredHolder<Block, Block> MARBLED_CHEESE_BRICK_MOSSY_SLAB = register("marbled_cheese_brick_mossy_slab", () -> new SlabBlock(Block.Properties.ofFullCopy(MARBLED_CHEESE_BRICK_MOSSY.get())));
	public static final DeferredHolder<Block, Block> MARBLED_CHEESE_BRICK_MOSSY_STAIRS = register("marbled_cheese_brick_mossy_stairs", () -> new StairBlock(MARBLED_CHEESE_BRICK_MOSSY.get().defaultBlockState(), Block.Properties.ofFullCopy(MARBLED_CHEESE_BRICK_MOSSY.get())));
	public static final DeferredHolder<Block, Block> MARBLED_CHEESE_DIRT = register("marbled_cheese_dirt", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.DIRT).strength(0.5F, 0.0F).sound(SoundType.GRAVEL)));
	public static final DeferredHolder<Block, Block> MARBLED_CHEESE_GRASS = register("marbled_cheese_grass", () -> new MarbledCheeseGrassBlock(Block.Properties.of().mapColor(MapColor.GRASS).strength(0.6F, 0.0F).sound(SoundType.GRASS)));
	public static final DeferredHolder<Block, Block> MARBLED_CHEESE_RAT_HEAD = register("marbled_cheese_rat_head", () -> new RatlanteanAutomatonHeadBlock(Block.Properties.of().mapColor(MapColor.WOOL).sound(SoundType.STONE).lightLevel(value -> 5).noCollission().dynamicShape().strength(1.0F, 0)));
	public static final DeferredHolder<Block, Block> MARBLED_CHEESE_GOLEM_CORE = register("marbled_cheese_golem_core", () -> new Block(Block.Properties.of().mapColor(MapColor.WOOL).requiresCorrectToolForDrops().strength(5.0F, 30.0F).sound(SoundType.STONE).lightLevel(value -> 6)));
	public static final DeferredHolder<Block, Block> RATLANTIS_PORTAL = register("ratlantis_portal", () -> new RatlantisPortalBlock(Block.Properties.of().pushReaction(PushReaction.BLOCK).sound(SoundType.BONE_BLOCK).strength(-1.0F).lightLevel(value -> 15).noCollission()));
	public static final DeferredHolder<Block, Block> COMPRESSED_RAT = register("compressed_rat", () -> new CompressedRatBlock(Block.Properties.of().mapColor(MapColor.WOOL).sound(SoundType.WOOL).strength(0.6F, 0.0F)));
	public static final DeferredHolder<Block, Block> BRAIN_BLOCK = register("brain_block", () -> new SetupHorizontalBlock(Block.Properties.of().mapColor(MapColor.WOOL).sound(SoundType.SLIME_BLOCK).strength(0.6F, 0.0F)));
	public static final DeferredHolder<Block, Block> PIRAT_SAPLING = register("pirat_sapling", () -> new SaplingBlock(PiratTreeGrower.INSTANCE, Block.Properties.of().pushReaction(PushReaction.DESTROY).ignitedByLava().mapColor(MapColor.TERRACOTTA_GREEN).randomTicks().instabreak().lightLevel(value -> 3).noCollission().noOcclusion().sound(SoundType.GRASS)));
	public static final DeferredHolder<Block, Block> POTTED_PIRAT_SAPLING = BLOCKS.register("potted_pirat_sapling", () -> new FlowerPotBlock(PIRAT_SAPLING.get(), Block.Properties.ofFullCopy(Blocks.FLOWER_POT).lightLevel(value -> 3)));
	public static final DeferredHolder<Block, Block> PIRAT_LEAVES = register("pirat_leaves", () -> new LeavesBlock(Block.Properties.ofFullCopy(Blocks.OAK_LEAVES).mapColor(MapColor.TERRACOTTA_GREEN).lightLevel(value -> 3).sound(SoundType.AZALEA_LEAVES)));
	public static final DeferredHolder<Block, Block> PIRAT_LOG = register("pirat_log", () -> new RotatedPillarBlock(Block.Properties.of().ignitedByLava().mapColor(MapColor.COLOR_BROWN).lightLevel(value -> 3).noOcclusion().strength(2.0F).sound(SoundType.WOOD)) {
		public boolean skipRendering(BlockState state, BlockState sideState, Direction direction) {
			return sideState.is(RatlantisBlockTags.PIRAT_LOGS) || super.skipRendering(state, sideState, direction);
		}
	});
	public static final DeferredHolder<Block, Block> STRIPPED_PIRAT_LOG = register("stripped_pirat_log", () -> new RotatedPillarBlock(Block.Properties.of().ignitedByLava().mapColor(MapColor.COLOR_GREEN).lightLevel(value -> 3).noOcclusion().strength(2.0F).sound(SoundType.WOOD)) {
		public boolean skipRendering(BlockState state, BlockState sideState, Direction direction) {
			return sideState.is(RatlantisBlockTags.PIRAT_LOGS) || super.skipRendering(state, sideState, direction);
		}
	});
	public static final DeferredHolder<Block, Block> PIRAT_WOOD = register("pirat_wood", () -> new RotatedPillarBlock(Block.Properties.of().ignitedByLava().mapColor(MapColor.COLOR_GREEN).lightLevel(value -> 3).noOcclusion().strength(2.0F).sound(SoundType.WOOD)) {
		public boolean skipRendering(BlockState state, BlockState sideState, Direction direction) {
			return sideState.is(RatlantisBlockTags.PIRAT_LOGS) || super.skipRendering(state, sideState, direction);
		}
	});
	public static final DeferredHolder<Block, Block> STRIPPED_PIRAT_WOOD = register("stripped_pirat_wood", () -> new RotatedPillarBlock(Block.Properties.of().ignitedByLava().mapColor(MapColor.COLOR_GREEN).lightLevel(value -> 3).noOcclusion().strength(2.0F).sound(SoundType.WOOD)) {
		public boolean skipRendering(BlockState state, BlockState sideState, Direction direction) {
			return sideState.is(RatlantisBlockTags.PIRAT_LOGS) || super.skipRendering(state, sideState, direction);
		}
	});
	public static final DeferredHolder<Block, Block> PIRAT_PLANKS = register("pirat_planks", () -> new HalfTransparentBlock(Block.Properties.of().ignitedByLava().mapColor(MapColor.COLOR_GREEN).lightLevel(value -> 3).noOcclusion().strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredHolder<Block, Block> PIRAT_PRESSURE_PLATE = register("pirat_pressure_plate", () -> new PressurePlateBlock(RatsMod.PIRAT_WOOD_SET, BlockBehaviour.Properties.ofFullCopy(PIRAT_PLANKS.get()).noOcclusion()));
	public static final DeferredHolder<Block, Block> PIRAT_TRAPDOOR = register("pirat_trapdoor", () -> new TrapDoorBlock(RatsMod.PIRAT_WOOD_SET, BlockBehaviour.Properties.ofFullCopy(PIRAT_PLANKS.get())));
	public static final DeferredHolder<Block, Block> PIRAT_STAIRS = register("pirat_stairs", () -> new StairBlock(PIRAT_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(PIRAT_PLANKS.get()).noOcclusion()));
	public static final DeferredHolder<Block, Block> PIRAT_BUTTON = register("pirat_button", () -> new ButtonBlock(RatsMod.PIRAT_WOOD_SET, 30, BlockBehaviour.Properties.ofFullCopy(PIRAT_PLANKS.get()).noOcclusion()));
	public static final DeferredHolder<Block, Block> PIRAT_SLAB = register("pirat_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(PIRAT_PLANKS.get()).noOcclusion()));
	public static final DeferredHolder<Block, Block> PIRAT_FENCE_GATE = register("pirat_fence_gate", () -> new FenceGateBlock(RatsMod.PIRAT_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(PIRAT_PLANKS.get()).noOcclusion()));
	public static final DeferredHolder<Block, Block> PIRAT_FENCE = register("pirat_fence", () -> new FenceBlock(BlockBehaviour.Properties.ofFullCopy(PIRAT_PLANKS.get()).noOcclusion()));
	public static final DeferredHolder<Block, Block> PIRAT_DOOR = register("pirat_door", () -> new DoorBlock(RatsMod.PIRAT_WOOD_SET, BlockBehaviour.Properties.ofFullCopy(PIRAT_PLANKS.get()).noOcclusion()));
	public static final DeferredHolder<Block, Block> PIRAT_SIGN = BLOCKS.register("pirat_sign", () -> new StandingSignBlock(RatsMod.PIRAT_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(PIRAT_PLANKS.get()).noCollission().noOcclusion().strength(1.0F)) {
		@Override
		public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
			return new PiratSignBlockEntity(pos, state);
		}
	});

	public static final DeferredHolder<Block, Block> PIRAT_WALL_SIGN = BLOCKS.register("pirat_wall_sign", () -> new WallSignBlock(RatsMod.PIRAT_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(PIRAT_PLANKS.get()).dropsLike(PIRAT_SIGN.get()).noCollission().noOcclusion().strength(1.0F)) {
		@Override
		public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
			return new PiratSignBlockEntity(pos, state);
		}
	});

	public static final DeferredHolder<Block, Block> PIRAT_HANGING_SIGN = BLOCKS.register("pirat_hanging_sign", () -> new CeilingHangingSignBlock(RatsMod.PIRAT_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(PIRAT_PLANKS.get()).noCollission().noOcclusion().strength(1.0F)) {
		@Override
		public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
			return new PiratHangingSignBlockEntity(pos, state);
		}
	});

	public static final DeferredHolder<Block, Block> PIRAT_WALL_HANGING_SIGN = BLOCKS.register("pirat_wall_hanging_sign", () -> new WallHangingSignBlock(RatsMod.PIRAT_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(PIRAT_PLANKS.get()).dropsLike(PIRAT_HANGING_SIGN.get()).noCollission().noOcclusion().strength(1.0F)) {
		@Override
		public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
			return new PiratHangingSignBlockEntity(pos, state);
		}
	});


	public static final DeferredHolder<Block, Block> DUTCHRAT_BELL = register("dutchrat_bell", () -> new DutchratBellBlock(Block.Properties.of().mapColor(MapColor.GOLD).strength(5.0F).requiresCorrectToolForDrops().sound(SoundType.ANVIL)));
	public static final DeferredHolder<Block, Block> AIR_RAID_SIREN = register("air_raid_siren", () -> new AirRaidSirenBlock(Block.Properties.of().mapColor(MapColor.WOOD).sound(SoundType.METAL).strength(5.0F, 1000.0F)));
	public static final DeferredHolder<Block, Block> RATLANTIS_UPGRADE_BLOCK = register("ratlantis_upgrade_block", () -> new RatUpgradeBlock(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_YELLOW).sound(SoundType.SLIME_BLOCK).strength(0.6F, 0.0F)));

	public static DeferredHolder<Block, Block> register(String name, Supplier<Block> blockSupplier) {
		DeferredHolder<Block, Block> ret = BLOCKS.register(name, blockSupplier);
		RatlantisItemRegistry.ITEMS.register(name, () -> new RatsBlockItem(ret.get(), new Item.Properties()));
		return ret;
	}
}
