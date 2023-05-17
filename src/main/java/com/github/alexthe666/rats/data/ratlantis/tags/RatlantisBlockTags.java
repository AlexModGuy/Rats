package com.github.alexthe666.rats.data.ratlantis.tags;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.data.tags.RatsBlockTags;
import com.github.alexthe666.rats.registry.RatlantisBlockRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class RatlantisBlockTags extends BlockTagsProvider {
	public static final TagKey<Block> PIRAT_LOGS = BlockTags.create(new ResourceLocation(RatsMod.MODID, "pirat_logs"));
	public static final TagKey<Block> PIRAT_ONLY_BLOCKS = BlockTags.create(new ResourceLocation(RatsMod.MODID, "pirat_blocks"));

	public static final TagKey<Block> STORAGE_BLOCKS_ORATCHALCUM = BlockTags.create(new ResourceLocation("forge", "storage_blocks/oratchalcum"));

	public static final TagKey<Block> ORES_CHEESE = BlockTags.create(new ResourceLocation("forge", "ores/cheese"));
	public static final TagKey<Block> ORES_GEM_OF_RATLANTIS = BlockTags.create(new ResourceLocation("forge", "ores/gem_of_ratlantis"));
	public static final TagKey<Block> ORES_ORATCHALCUM = BlockTags.create(new ResourceLocation("forge", "ores/oratchalcum"));

	public RatlantisBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper helper) {
		super(output, provider, RatsMod.MODID, helper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		this.tag(PIRAT_ONLY_BLOCKS).addTag(PIRAT_LOGS).add(
				RatlantisBlockRegistry.PIRAT_PLANKS.get(), RatlantisBlockRegistry.PIRAT_SLAB.get(),
				RatlantisBlockRegistry.PIRAT_STAIRS.get(), RatlantisBlockRegistry.PIRAT_BUTTON.get(),
				RatlantisBlockRegistry.PIRAT_PRESSURE_PLATE.get(),
				RatlantisBlockRegistry.PIRAT_FENCE.get(), RatlantisBlockRegistry.PIRAT_FENCE_GATE.get(),
				RatlantisBlockRegistry.PIRAT_DOOR.get(), RatlantisBlockRegistry.PIRAT_TRAPDOOR.get());

		this.tag(PIRAT_LOGS).add(
				RatlantisBlockRegistry.PIRAT_LOG.get(), RatlantisBlockRegistry.STRIPPED_PIRAT_LOG.get(),
				RatlantisBlockRegistry.PIRAT_WOOD.get(), RatlantisBlockRegistry.STRIPPED_PIRAT_WOOD.get());
		this.tag(BlockTags.LOGS).addTag(PIRAT_LOGS);

		this.tag(STORAGE_BLOCKS_ORATCHALCUM).add(RatlantisBlockRegistry.ORATCHALCUM_BLOCK.get());
		this.tag(Tags.Blocks.STORAGE_BLOCKS).addTag(STORAGE_BLOCKS_ORATCHALCUM);

		this.tag(ORES_CHEESE).add(RatlantisBlockRegistry.CHEESE_ORE.get());
		this.tag(ORES_GEM_OF_RATLANTIS).add(RatlantisBlockRegistry.RATLANTEAN_GEM_ORE.get());
		this.tag(ORES_ORATCHALCUM).add(RatlantisBlockRegistry.ORATCHALCUM_ORE.get());
		this.tag(Tags.Blocks.ORES).addTag(ORES_CHEESE).addTag(ORES_GEM_OF_RATLANTIS).addTag(ORES_ORATCHALCUM);
		this.tag(Tags.Blocks.ORES_IN_GROUND_STONE).addTag(ORES_CHEESE).addTag(ORES_GEM_OF_RATLANTIS).addTag(ORES_ORATCHALCUM);

		this.tag(RatsBlockTags.MARBLED_CHEESE).add(
				RatlantisBlockRegistry.MARBLED_CHEESE.get(), RatlantisBlockRegistry.MARBLED_CHEESE_BRICK.get(),
				RatlantisBlockRegistry.MARBLED_CHEESE_TILE.get(), RatlantisBlockRegistry.MARBLED_CHEESE_CHISELED.get(),
				RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_CRACKED.get(), RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_MOSSY.get(),
				RatlantisBlockRegistry.BLACK_MARBLED_CHEESE.get(), RatlantisBlockRegistry.MARBLED_CHEESE_CHISELED.get(),
				RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_CHISELED.get(), RatlantisBlockRegistry.MARBLED_CHEESE_PILLAR.get());

		this.tag(RatsBlockTags.TRASH_CAN_BLACKLIST)
				.add(RatlantisBlockRegistry.CHUNKY_CHEESE_TOKEN.get(),
						RatlantisBlockRegistry.MARBLED_CHEESE_RAT_HEAD.get(),
						RatlantisBlockRegistry.MARBLED_CHEESE_GOLEM_CORE.get());

		this.tag(BlockTags.PLANKS).add(RatlantisBlockRegistry.PIRAT_PLANKS.get());
		this.tag(BlockTags.WOODEN_BUTTONS).add(RatlantisBlockRegistry.PIRAT_BUTTON.get());
		this.tag(BlockTags.WOODEN_PRESSURE_PLATES).add(RatlantisBlockRegistry.PIRAT_PRESSURE_PLATE.get());
		this.tag(BlockTags.WOODEN_DOORS).add(RatlantisBlockRegistry.PIRAT_DOOR.get());
		this.tag(BlockTags.WOODEN_TRAPDOORS).add(RatlantisBlockRegistry.PIRAT_TRAPDOOR.get());

		this.tag(BlockTags.FENCES).add(RatlantisBlockRegistry.PIRAT_FENCE.get());
		this.tag(BlockTags.WOODEN_FENCES).add(RatlantisBlockRegistry.PIRAT_FENCE.get());
		this.tag(Tags.Blocks.FENCES).add(RatlantisBlockRegistry.PIRAT_FENCE.get());
		this.tag(Tags.Blocks.FENCES_WOODEN).add(RatlantisBlockRegistry.PIRAT_FENCE.get());

		this.tag(BlockTags.FENCE_GATES).add(RatlantisBlockRegistry.PIRAT_FENCE_GATE.get());
		this.tag(Tags.Blocks.FENCE_GATES).add(RatlantisBlockRegistry.PIRAT_FENCE_GATE.get());
		this.tag(Tags.Blocks.FENCE_GATES_WOODEN).add(RatlantisBlockRegistry.PIRAT_FENCE_GATE.get());

		this.tag(BlockTags.WOODEN_SLABS).add(RatlantisBlockRegistry.PIRAT_SLAB.get());
		this.tag(BlockTags.SLABS).add(RatlantisBlockRegistry.MARBLED_CHEESE_SLAB.get(), RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_SLAB.get(),
				RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_CRACKED_SLAB.get(), RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_MOSSY_SLAB.get());

		this.tag(BlockTags.WOODEN_STAIRS).add(RatlantisBlockRegistry.PIRAT_STAIRS.get());
		this.tag(BlockTags.STAIRS).add(RatlantisBlockRegistry.MARBLED_CHEESE_STAIRS.get(), RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_STAIRS.get(),
				RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_CRACKED_STAIRS.get(), RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_MOSSY_STAIRS.get());

		this.tag(BlockTags.SMALL_FLOWERS).add(RatlantisBlockRegistry.RATGLOVE_FLOWER.get());
		this.tag(BlockTags.INVALID_SPAWN_INSIDE).add(RatlantisBlockRegistry.RATLANTIS_PORTAL.get());
		this.tag(BlockTags.BEACON_BASE_BLOCKS).add(RatlantisBlockRegistry.ORATCHALCUM_BLOCK.get());
		this.tag(BlockTags.DEAD_BUSH_MAY_PLACE_ON).add(RatlantisBlockRegistry.MARBLED_CHEESE_GRASS.get(), RatlantisBlockRegistry.MARBLED_CHEESE_DIRT.get());
		this.tag(BlockTags.DRAGON_IMMUNE).add(RatlantisBlockRegistry.RATLANTIS_PORTAL.get(), RatlantisBlockRegistry.AIR_RAID_SIREN.get(), RatlantisBlockRegistry.DUTCHRAT_BELL.get());
		this.tag(BlockTags.PORTALS).add(RatlantisBlockRegistry.RATLANTIS_PORTAL.get());
		this.tag(BlockTags.FLOWER_POTS).add(RatlantisBlockRegistry.POTTED_RATGLOVE_FLOWER.get());

		this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
				RatlantisBlockRegistry.RATLANTIS_REACTOR.get(), RatlantisBlockRegistry.CHUNKY_CHEESE_TOKEN.get(),
				RatlantisBlockRegistry.CHEESE_ORE.get(), RatlantisBlockRegistry.RATLANTEAN_GEM_ORE.get(),
				RatlantisBlockRegistry.ORATCHALCUM_ORE.get(), RatlantisBlockRegistry.ORATCHALCUM_BLOCK.get(),
				RatlantisBlockRegistry.DUTCHRAT_BELL.get(),
				RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_SLAB.get(), RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_STAIRS.get(),
				RatlantisBlockRegistry.MARBLED_CHEESE_SLAB.get(), RatlantisBlockRegistry.MARBLED_CHEESE_STAIRS.get(),
				RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_CRACKED_SLAB.get(), RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_CRACKED_STAIRS.get(),
				RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_MOSSY_SLAB.get(), RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_MOSSY_STAIRS.get(),
				RatlantisBlockRegistry.MARBLED_CHEESE_GOLEM_CORE.get(), RatlantisBlockRegistry.MARBLED_CHEESE_RAT_HEAD.get());

		this.tag(BlockTags.MINEABLE_WITH_AXE).add(RatlantisBlockRegistry.AIR_RAID_SIREN.get());

		this.tag(BlockTags.MINEABLE_WITH_SHOVEL).add(
				RatlantisBlockRegistry.MARBLED_CHEESE_GRASS.get(),
				RatlantisBlockRegistry.MARBLED_CHEESE_DIRT.get());

		this.tag(BlockTags.MINEABLE_WITH_HOE).add(
				RatlantisBlockRegistry.BRAIN_BLOCK.get(),
				RatlantisBlockRegistry.RATLANTIS_UPGRADE_BLOCK.get(),
				RatlantisBlockRegistry.COMPRESSED_RAT.get());

		this.tag(BlockTags.NEEDS_DIAMOND_TOOL).add(RatlantisBlockRegistry.ORATCHALCUM_BLOCK.get(), RatlantisBlockRegistry.ORATCHALCUM_ORE.get());
		this.tag(BlockTags.NEEDS_IRON_TOOL).add(RatlantisBlockRegistry.DUTCHRAT_BELL.get(), RatlantisBlockRegistry.AIR_RAID_SIREN.get(), RatlantisBlockRegistry.RATLANTEAN_GEM_ORE.get());
	}
}
