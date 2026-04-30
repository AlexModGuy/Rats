package com.github.alexthe666.rats.data;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.registry.RatlantisBlockRegistry;
import com.github.alexthe666.rats.registry.RatsBlockRegistry;
import com.github.alexthe666.rats.server.block.DutchratBellBlock;
import com.github.alexthe666.rats.server.block.RatAttractorBlock;
import com.github.alexthe666.rats.server.block.RatCageBlock;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BellAttachType;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.Objects;

public class BlockModelGenerator extends BlockStateProvider {

	public BlockModelGenerator(PackOutput output, ExistingFileHelper helper) {
		super(output, RatsMod.MODID, helper);
	}

	@Override
	protected void registerStatesAndModels() {
		this.builtinEntity(RatsBlockRegistry.AUTO_CURDLER.get(), Objects.requireNonNull(ResourceLocation.tryParse("minecraft:block/cauldron_side")));
		this.simpleBlock(RatsBlockRegistry.BLOCK_OF_BLUE_CHEESE.get());
		this.simpleBlock(RatsBlockRegistry.BLOCK_OF_NETHER_CHEESE.get());
		this.simpleBlock(RatsBlockRegistry.BLOCK_OF_CHEESE.get());
		this.simpleBlock(RatsBlockRegistry.CHEESE_CAULDRON.get(), this.models().withExistingParent("cauldron_cheese", ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "block/template_cauldron_with_transforms"))
				.texture("bottom", ResourceLocation.parse("block/cauldron_bottom"))
				.texture("content", this.blockPrefix("block_of_cheese"))
				.texture("inside", ResourceLocation.parse("block/cauldron_inner"))
				.texture("particle", ResourceLocation.parse("block/cauldron_side"))
				.texture("side", ResourceLocation.parse("block/cauldron_side"))
				.texture("top", ResourceLocation.parse("block/cauldron_top")));
		this.simpleBlock(RatsBlockRegistry.BLUE_CHEESE_CAULDRON.get(), this.models().withExistingParent("cauldron_blue_cheese", ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "block/template_cauldron_with_transforms"))
				.texture("bottom", ResourceLocation.parse("block/cauldron_bottom"))
				.texture("content", this.blockPrefix("block_of_blue_cheese"))
				.texture("inside", ResourceLocation.parse("block/cauldron_inner"))
				.texture("particle", ResourceLocation.parse("block/cauldron_side"))
				.texture("side", ResourceLocation.parse("block/cauldron_side"))
				.texture("top", ResourceLocation.parse("block/cauldron_top")));
		this.simpleBlock(RatsBlockRegistry.NETHER_CHEESE_CAULDRON.get(), this.models().withExistingParent("cauldron_nether_cheese", ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "block/template_cauldron_with_transforms"))
				.texture("bottom", ResourceLocation.parse("block/cauldron_bottom"))
				.texture("content", this.blockPrefix("block_of_nether_cheese"))
				.texture("inside", ResourceLocation.parse("block/cauldron_inner"))
				.texture("particle", ResourceLocation.parse("block/cauldron_side"))
				.texture("side", ResourceLocation.parse("block/cauldron_side"))
				.texture("top", ResourceLocation.parse("block/cauldron_top")));
		this.simpleBlock(RatsBlockRegistry.MILK_CAULDRON.get(), this.models().withExistingParent("cauldron_milk", ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "block/template_cauldron_with_transforms"))
				.texture("bottom", ResourceLocation.parse("block/cauldron_bottom"))
				.texture("content", ResourceLocation.fromNamespaceAndPath("neoforge", "block/milk_still"))
				.texture("inside", ResourceLocation.parse("block/cauldron_inner"))
				.texture("particle", ResourceLocation.parse("block/cauldron_side"))
				.texture("side", ResourceLocation.parse("block/cauldron_side"))
				.texture("top", ResourceLocation.parse("block/cauldron_top")));
		this.simpleBlock(RatsBlockRegistry.COMPRESSED_GARBAGE.get());
		this.simpleBlock(RatsBlockRegistry.CURSED_GARBAGE.get());
		this.simpleBlock(RatsBlockRegistry.DYE_SPONGE.get());
		this.simpleBlock(RatsBlockRegistry.FISH_BARREL.get(), this.models().cube("fish_barrel",
						ResourceLocation.parse("block/barrel_bottom"), this.blockPrefix("fish_barrel_top"),
						ResourceLocation.parse("block/barrel_side"), ResourceLocation.parse("block/barrel_side"),
						ResourceLocation.parse("block/barrel_side"), ResourceLocation.parse("block/barrel_side"))
				.texture("particle", ResourceLocation.parse("block/barrel_side")));
		ConfiguredModel[] TRASH = new ConfiguredModel[8];
		for (int i = 0; i < TRASH.length; i++) {
			TRASH[i] = ConfiguredModel.builder().weight(i == 0 ? 10 : 1).modelFile(this.models().cubeAll("garbage_" + i, this.blockPrefix("garbage_" + i))).buildLast();
		}
		this.simpleBlock(RatsBlockRegistry.GARBAGE_PILE.get(), TRASH);
		this.horizontalBlock(RatsBlockRegistry.JACK_O_RATERN.get(), this.models().orientable("jack_o_ratern",
				ResourceLocation.parse("block/pumpkin_side"),
				this.blockPrefix("jack_o_ratern"),
				ResourceLocation.parse("block/pumpkin_top")));
		this.trapdoorBlockWithRenderType((TrapDoorBlock) RatsBlockRegistry.MANHOLE.get(), this.blockPrefix("manhole"), true, "translucent");
		this.simpleBlock(RatsBlockRegistry.MARBLED_CHEESE_RAW.get());
		this.simpleBlock(RatsBlockRegistry.PIED_GARBAGE.get());
		this.simpleBlock(RatsBlockRegistry.PIED_WOOL.get());
		this.simpleBlock(RatsBlockRegistry.PURIFIED_GARBAGE.get());
		this.getVariantBuilder(RatsBlockRegistry.RAT_ATTRACTOR.get()).forAllStates(state ->
				ConfiguredModel.builder().modelFile(this.models().getExistingFile(this.blockPrefix("rat_attractor" +
						(state.getValue(RatAttractorBlock.POWERED) ? "_active" : "") +
						(state.getValue(RatAttractorBlock.CONNECTED_UP) ? "" : "_no_chain")))).build());
		this.cageBlock(RatsBlockRegistry.RAT_CAGE.get());
		this.cageBlock(RatsBlockRegistry.RAT_CAGE_BREEDING_LANTERN.get());
		this.cageBlock(RatsBlockRegistry.RAT_CAGE_DECORATED.get());
		this.cageBlock(RatsBlockRegistry.RAT_CAGE_WHEEL.get());
		this.simpleBlock(RatsBlockRegistry.RAT_CRAFTING_TABLE.get(), this.models().cube("rat_crafting_table",
						this.blockPrefix("block_of_cheese"), this.blockPrefix("rat_crafting_table_top"),
						this.blockPrefix("rat_crafting_table_side"), this.blockPrefix("rat_crafting_table_side"),
						this.blockPrefix("rat_crafting_table"), this.blockPrefix("rat_crafting_table"))
				.texture("particle", this.blockPrefix("rat_crafting_table")));
		this.builtinEntity(RatsBlockRegistry.RAT_HOLE.get(), Objects.requireNonNull(ResourceLocation.tryParse("minecraft:block/oak_planks")));
		this.simpleBlock(RatsBlockRegistry.RAT_QUARRY.get(), this.models().cube("rat_quarry",
						this.blockPrefix("rat_quarry_bottom"), this.blockPrefix("rat_quarry_top"),
						this.blockPrefix("rat_quarry_side"), this.blockPrefix("rat_quarry_side"),
						this.blockPrefix("rat_quarry_side"), this.blockPrefix("rat_quarry_side"))
				.texture("particle", this.blockPrefix("rat_quarry_side")));
		this.simpleBlock(RatsBlockRegistry.RAT_QUARRY_PLATFORM.get(), this.models().getExistingFile(this.blockPrefix("rat_quarry_platform")));
		this.simpleBlock(RatsBlockRegistry.RAT_TRAP.get(), this.models().getExistingFile(ResourceLocation.parse("block/oak_planks")));
		this.directionalBlock(RatsBlockRegistry.RAT_UPGRADE_BLOCK.get(), this.models().cubeAll("rat_upgrade_block", ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "block/rat_upgrade_block")));
		this.simpleBlock(RatsBlockRegistry.TRASH_CAN.get(), this.models().cubeAll("trash_can", ResourceLocation.parse("block/cauldron_side")));
		this.simpleBlock(RatsBlockRegistry.UPGRADE_COMBINER.get(), this.models().getExistingFile(this.blockPrefix("upgrade_combiner")));
		this.simpleBlock(RatsBlockRegistry.UPGRADE_SEPARATOR.get(), this.models().getExistingFile(this.blockPrefix("upgrade_separator")));

		this.simpleBlock(RatlantisBlockRegistry.AIR_RAID_SIREN.get(), this.models().getExistingFile(this.blockPrefix("air_raid_siren")));
		this.simpleBlock(RatlantisBlockRegistry.BLACK_MARBLED_CHEESE.get());
		this.simpleBlock(RatlantisBlockRegistry.BRAIN_BLOCK.get(), this.models().cube("brain_block",
						this.blockPrefix("brain_bottom"), this.blockPrefix("brain_top"),
						this.blockPrefix("brain_front"), this.blockPrefix("brain_back"),
						this.blockPrefix("brain_side"), this.blockPrefix("brain_side"))
				.texture("particle", this.blockPrefix("brain_side")));
		this.simpleBlock(RatlantisBlockRegistry.CHEESE_ORE.get());
		this.simpleBlock(RatlantisBlockRegistry.CHUNKY_CHEESE_TOKEN.get(), this.models().cubeAll("chunky_cheese_token", this.itemPrefix("chunky_cheese_token")).renderType(ResourceLocation.parse("cutout")));
		this.horizontalBlock(RatlantisBlockRegistry.COMPRESSED_RAT.get(), this.models().cube("compressed_rat",
						this.blockPrefix("compressed_rat_top"), this.blockPrefix("compressed_rat_top"),
						this.blockPrefix("compressed_rat_front"), this.blockPrefix("compressed_rat_back"),
						this.blockPrefix("compressed_rat_other_side"), this.blockPrefix("compressed_rat_side"))
				.texture("particle", this.blockPrefix("compressed_rat_side")));

		this.getVariantBuilder(RatlantisBlockRegistry.DUTCHRAT_BELL.get()).forAllStates(state -> {
			ModelFile type = switch (state.getValue(DutchratBellBlock.ATTACHMENT)) {
				case FLOOR ->
						this.models().withExistingParent("dutchrat_bell_floor", ResourceLocation.parse("block/bell_floor")).renderType("minecraft:translucent")
								.texture("particle", this.blockPrefix("pirat_log"))
								.texture("bar", this.blockPrefix("pirat_log"))
								.texture("post", this.blockPrefix("pirat_log"));
				case CEILING ->
						this.models().withExistingParent("dutchrat_bell_ceiling", ResourceLocation.parse("block/bell_ceiling")).renderType("minecraft:translucent")
								.texture("particle", this.blockPrefix("pirat_log"))
								.texture("bar", this.blockPrefix("pirat_log"));
				case SINGLE_WALL ->
						this.models().withExistingParent("dutchrat_bell_wall", ResourceLocation.parse("block/bell_wall")).renderType("minecraft:translucent")
								.texture("particle", this.blockPrefix("pirat_log"))
								.texture("bar", this.blockPrefix("pirat_log"));
				case DOUBLE_WALL ->
						this.models().withExistingParent("dutchrat_bell_between_walls", ResourceLocation.parse("block/bell_between_walls")).renderType("minecraft:translucent")
								.texture("particle", this.blockPrefix("pirat_log"))
								.texture("bar", this.blockPrefix("pirat_log"));
			};
			return ConfiguredModel.builder().modelFile(type).rotationY((int) state.getValue(DutchratBellBlock.FACING).toYRot() + (state.getValue(DutchratBellBlock.ATTACHMENT) == BellAttachType.FLOOR ? 0 : 90)).build();
		});
		this.simpleBlock(RatlantisBlockRegistry.MARBLED_CHEESE.get());
		this.simpleBlock(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK.get());
		this.stairsBlock((StairBlock) RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_STAIRS.get(), this.blockPrefix("marbled_cheese_brick"));
		this.slabBlock((SlabBlock) RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_SLAB.get(), this.blockPrefix("marbled_cheese_brick"), this.blockPrefix("marbled_cheese_brick"));
		this.simpleBlock(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_CHISELED.get());
		this.simpleBlock(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_CRACKED.get());
		this.stairsBlock((StairBlock) RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_CRACKED_STAIRS.get(), this.blockPrefix("marbled_cheese_brick_cracked"));
		this.slabBlock((SlabBlock) RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_CRACKED_SLAB.get(), this.blockPrefix("marbled_cheese_brick_cracked"), this.blockPrefix("marbled_cheese_brick_cracked"));
		this.simpleBlock(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_MOSSY.get());
		this.stairsBlock((StairBlock) RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_MOSSY_STAIRS.get(), this.blockPrefix("marbled_cheese_brick_mossy"));
		this.slabBlock((SlabBlock) RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_MOSSY_SLAB.get(), this.blockPrefix("marbled_cheese_brick_mossy"), this.blockPrefix("marbled_cheese_brick_mossy"));
		this.simpleBlock(RatlantisBlockRegistry.MARBLED_CHEESE_CHISELED.get());
		this.simpleBlock(RatlantisBlockRegistry.MARBLED_CHEESE_DIRT.get(), this.models().withExistingParent("marbled_cheese_dirt", this.blockPrefix("overlay_block"))
				.texture("particle", ResourceLocation.parse("block/dirt")).texture("tex", ResourceLocation.parse("block/dirt"))
				.texture("overlay", this.blockPrefix("marbled_cheese_overlay")));
		this.simpleBlock(RatlantisBlockRegistry.MARBLED_CHEESE_GOLEM_CORE.get());
		this.simpleBlock(RatlantisBlockRegistry.MARBLED_CHEESE_GRASS.get(), this.models().withExistingParent("marbled_cheese_grass", this.blockPrefix("grass_overlay_double"))
				.texture("particle", ResourceLocation.parse("block/dirt")).texture("side", ResourceLocation.parse("block/grass_block_side"))
				.texture("bottom", ResourceLocation.parse("block/dirt")).texture("top", ResourceLocation.parse("block/grass_block_top"))
				.texture("overlay", ResourceLocation.parse("block/grass_block_side_overlay")).texture("overlay2", this.blockPrefix("marbled_cheese_overlay")));
		this.simpleBlock(RatlantisBlockRegistry.MARBLED_CHEESE_PILLAR.get(), this.models().cubeColumn("marbled_cheese_pillar", this.blockPrefix("marbled_cheese_pillar"), this.blockPrefix("marbled_cheese_pillar_top")));
		this.horizontalBlock(RatlantisBlockRegistry.MARBLED_CHEESE_RAT_HEAD.get(), this.models().getExistingFile(this.blockPrefix("marbled_cheese_rat_head")));
		this.stairsBlock((StairBlock) RatlantisBlockRegistry.MARBLED_CHEESE_STAIRS.get(), this.blockPrefix("marbled_cheese"));
		this.slabBlock((SlabBlock) RatlantisBlockRegistry.MARBLED_CHEESE_SLAB.get(), this.blockPrefix("marbled_cheese"), this.blockPrefix("marbled_cheese"));
		this.simpleBlock(RatlantisBlockRegistry.MARBLED_CHEESE_TILE.get());
		this.simpleBlock(RatlantisBlockRegistry.ORATCHALCUM_BLOCK.get());
		this.simpleBlock(RatlantisBlockRegistry.ORATCHALCUM_ORE.get());
		this.buttonBlockTranslucent((ButtonBlock) RatlantisBlockRegistry.PIRAT_BUTTON.get(), this.blockPrefix("pirat_planks"));
		this.doorBlockWithRenderType((DoorBlock) RatlantisBlockRegistry.PIRAT_DOOR.get(), this.blockPrefix("pirat_door_bottom"), this.blockPrefix("pirat_door_top"), "minecraft:translucent");
		this.fenceBlockWithRenderType((FenceBlock) RatlantisBlockRegistry.PIRAT_FENCE.get(), this.blockPrefix("pirat_planks"), "minecraft:translucent");
		this.fenceGateBlockWithRenderType((FenceGateBlock) RatlantisBlockRegistry.PIRAT_FENCE_GATE.get(), this.blockPrefix("pirat_planks"), "minecraft:translucent");
		this.builtinEntity(RatlantisBlockRegistry.PIRAT_HANGING_SIGN.get(), this.blockPrefix("pirat_planks"));
		this.simpleBlockTranslucent(RatlantisBlockRegistry.PIRAT_LEAVES.get());
		this.logBlockWithRenderType((RotatedPillarBlock) RatlantisBlockRegistry.PIRAT_LOG.get(), "minecraft:translucent");
		this.simpleBlockTranslucent(RatlantisBlockRegistry.PIRAT_PLANKS.get());
		this.pressurePlateBlockTranslucent((PressurePlateBlock) RatlantisBlockRegistry.PIRAT_PRESSURE_PLATE.get(), this.blockPrefix("pirat_planks"));
		this.simpleBlock(RatlantisBlockRegistry.PIRAT_SAPLING.get(), this.models().cross("pirat_sapling", this.blockPrefix("pirat_sapling")).renderType(ResourceLocation.parse("tripwire")));
		this.simpleBlock(RatlantisBlockRegistry.POTTED_PIRAT_SAPLING.get(), models().withExistingParent(RatlantisBlockRegistry.POTTED_PIRAT_SAPLING.getId().getPath(), "block/flower_pot_cross").renderType(ResourceLocation.parse("tripwire")).texture("plant", blockTexture(RatlantisBlockRegistry.PIRAT_SAPLING.get())));
		this.builtinEntity(RatlantisBlockRegistry.PIRAT_SIGN.get(), this.blockPrefix("pirat_planks"));
		this.slabBlockTranslucent((SlabBlock) RatlantisBlockRegistry.PIRAT_SLAB.get(), this.blockPrefix("pirat_planks"), this.blockPrefix("pirat_planks"));
		this.stairsBlockWithRenderType((StairBlock) RatlantisBlockRegistry.PIRAT_STAIRS.get(), this.blockPrefix("pirat_planks"), "minecraft:translucent");
		this.trapdoorBlockWithRenderType((TrapDoorBlock) RatlantisBlockRegistry.PIRAT_TRAPDOOR.get(), this.blockPrefix("pirat_trapdoor"), true, "minecraft:translucent");
		this.builtinEntity(RatlantisBlockRegistry.PIRAT_WALL_SIGN.get(), this.blockPrefix("pirat_planks"));
		this.builtinEntity(RatlantisBlockRegistry.PIRAT_WALL_HANGING_SIGN.get(), this.blockPrefix("pirat_planks"));
		this.axisBlockWithRenderType((RotatedPillarBlock) RatlantisBlockRegistry.PIRAT_WOOD.get(), this.blockPrefix("pirat_log"), this.blockPrefix("pirat_log"), "minecraft:translucent");
		this.simpleBlock(RatlantisBlockRegistry.RATGLOVE_FLOWER.get(), this.models().cross("ratglove_flower", this.blockPrefix("ratglove_flower")).renderType(ResourceLocation.parse("cutout")));
		this.simpleBlock(RatlantisBlockRegistry.POTTED_RATGLOVE_FLOWER.get(), models().withExistingParent(RatlantisBlockRegistry.POTTED_RATGLOVE_FLOWER.getId().getPath(), "block/flower_pot_cross").renderType(ResourceLocation.parse("cutout")).texture("plant", blockTexture(RatlantisBlockRegistry.RATGLOVE_FLOWER.get())));
		this.simpleBlock(RatlantisBlockRegistry.RATLANTEAN_GEM_ORE.get());
		this.simpleBlock(RatlantisBlockRegistry.RATLANTIS_PORTAL.get(), this.models().cubeAll("ratlantis_portal", ResourceLocation.parse("block/yellow_stained_glass")));
		this.simpleBlock(RatlantisBlockRegistry.RATLANTIS_REACTOR.get());
		this.directionalBlock(RatlantisBlockRegistry.RATLANTIS_UPGRADE_BLOCK.get(), this.models().cubeAll("ratlantis_upgrade_block", ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "block/ratlantis_upgrade_block")));
		this.logBlock((RotatedPillarBlock) RatlantisBlockRegistry.STRIPPED_PIRAT_LOG.get());
		this.axisBlock((RotatedPillarBlock) RatlantisBlockRegistry.STRIPPED_PIRAT_WOOD.get(), this.blockPrefix("stripped_pirat_log"), this.blockPrefix("stripped_pirat_log"));
	}

	public void simpleBlockTranslucent(Block block) {
		simpleBlock(block, models().cubeAll(BuiltInRegistries.BLOCK.getKey(block).getPath(), blockTexture(block)).renderType("minecraft:translucent"));
	}

	public void slabBlockTranslucent(SlabBlock block, ResourceLocation doubleslab, ResourceLocation texture) {
		slabBlock(block, models().slab(BuiltInRegistries.BLOCK.getKey(block).getPath(), texture, texture, texture).renderType("minecraft:translucent"), models().slabTop(BuiltInRegistries.BLOCK.getKey(block).getPath() + "_top", texture, texture, texture).renderType("minecraft:translucent"), models().getExistingFile(doubleslab));
	}

	public void pressurePlateBlockTranslucent(PressurePlateBlock block, ResourceLocation texture) {
		ModelFile pressurePlate = models().pressurePlate(BuiltInRegistries.BLOCK.getKey(block).getPath(), texture).renderType("minecraft:translucent");
		ModelFile pressurePlateDown = models().pressurePlateDown(BuiltInRegistries.BLOCK.getKey(block).getPath() + "_down", texture).renderType("minecraft:translucent");
		pressurePlateBlock(block, pressurePlate, pressurePlateDown);
	}

	public void buttonBlockTranslucent(ButtonBlock block, ResourceLocation texture) {
		ModelFile button = models().button(BuiltInRegistries.BLOCK.getKey(block).getPath(), texture).renderType("minecraft:translucent");
		ModelFile buttonPressed = models().buttonPressed(BuiltInRegistries.BLOCK.getKey(block).getPath() + "_pressed", texture).renderType("minecraft:translucent");
		buttonBlock(block, button, buttonPressed);
	}

	private void builtinEntity(Block b, ResourceLocation particle) {
		simpleBlock(b, models().getBuilder(BuiltInRegistries.BLOCK.getKey(b).getPath())
				.parent(new ModelFile.UncheckedModelFile("builtin/entity"))
				.texture("particle", particle));
	}

	private void cageBlock(Block block) {
		this.getMultipartBuilder(block)
				.part().modelFile(this.models().getExistingFile(this.blockPrefix("rat_cage_top_tube"))).uvLock(false).addModel().condition(RatCageBlock.UP, 2).end()
				.part().modelFile(this.models().getExistingFile(this.blockPrefix("rat_cage_bottom_tube"))).uvLock(false).addModel().condition(RatCageBlock.DOWN, 2).end()
				.part().modelFile(this.models().getExistingFile(this.blockPrefix("rat_cage_bars_tube"))).uvLock(false).rotationY(180).addModel().condition(RatCageBlock.SOUTH, 2).end()
				.part().modelFile(this.models().getExistingFile(this.blockPrefix("rat_cage_bars_tube"))).uvLock(false).rotationY(90).addModel().condition(RatCageBlock.EAST, 2).end()
				.part().modelFile(this.models().getExistingFile(this.blockPrefix("rat_cage_bars_tube"))).uvLock(false).rotationY(270).addModel().condition(RatCageBlock.WEST, 2).end()
				.part().modelFile(this.models().getExistingFile(this.blockPrefix("rat_cage_bars_tube"))).uvLock(false).addModel().condition(RatCageBlock.NORTH, 2).end()

				.part().modelFile(this.models().getExistingFile(this.blockPrefix("rat_cage_top"))).uvLock(false).addModel().condition(RatCageBlock.UP, 0).end()
				.part().modelFile(this.models().getExistingFile(this.blockPrefix("rat_cage_bottom"))).uvLock(false).addModel().condition(RatCageBlock.DOWN, 0).end()
				.part().modelFile(this.models().getExistingFile(this.blockPrefix("rat_cage_bars"))).uvLock(false).rotationY(180).addModel().condition(RatCageBlock.SOUTH, 0).end()
				.part().modelFile(this.models().getExistingFile(this.blockPrefix("rat_cage_bars"))).uvLock(false).rotationY(90).addModel().condition(RatCageBlock.EAST, 0).end()
				.part().modelFile(this.models().getExistingFile(this.blockPrefix("rat_cage_bars"))).uvLock(false).rotationY(270).addModel().condition(RatCageBlock.WEST, 0).end()
				.part().modelFile(this.models().getExistingFile(this.blockPrefix("rat_cage_bars"))).uvLock(false).addModel().condition(RatCageBlock.NORTH, 0).end()

				.part().modelFile(this.models().getExistingFile(this.blockPrefix("rat_cage_bottom_right"))).uvLock(false).addModel().nestedGroup().condition(RatCageBlock.DOWN, 0, 2).condition(RatCageBlock.EAST, 0, 2).end().end()
				.part().modelFile(this.models().getExistingFile(this.blockPrefix("rat_cage_bottom_left"))).uvLock(false).addModel().nestedGroup().condition(RatCageBlock.DOWN, 0, 2).condition(RatCageBlock.WEST, 0, 2).end().end()
				.part().modelFile(this.models().getExistingFile(this.blockPrefix("rat_cage_bottom_left"))).uvLock(false).rotationY(90).addModel().nestedGroup().condition(RatCageBlock.DOWN, 0, 2).condition(RatCageBlock.NORTH, 0, 2).end().end()
				.part().modelFile(this.models().getExistingFile(this.blockPrefix("rat_cage_bottom_right"))).uvLock(false).rotationY(90).addModel().nestedGroup().condition(RatCageBlock.DOWN, 0, 2).condition(RatCageBlock.SOUTH, 0, 2).end().end();
	}

	private ResourceLocation blockPrefix(String name) {
		return ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "block/" + name);
	}

	private ResourceLocation itemPrefix(String name) {
		return ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "item/" + name);
	}
}
