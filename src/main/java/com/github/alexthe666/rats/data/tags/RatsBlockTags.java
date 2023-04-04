package com.github.alexthe666.rats.data.tags;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.registry.RatsBlockRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class RatsBlockTags extends BlockTagsProvider {

	public static final TagKey<Block> MARBLED_CHEESE = BlockTags.create(new ResourceLocation(RatsMod.MODID, "marbled_cheese"));
	public static final TagKey<Block> TRASH_CAN_BLACKLIST = BlockTags.create(new ResourceLocation(RatsMod.MODID, "trash_can_blacklist"));
	public static final TagKey<Block> QUARRY_IGNORABLES = BlockTags.create(new ResourceLocation(RatsMod.MODID, "quarry_ignoreables"));
	public static final TagKey<Block> UNRAIDABLE_CONTAINERS = BlockTags.create(new ResourceLocation(RatsMod.MODID, "unraidable_containers"));
	public static final TagKey<Block> DIGGABLE_BLOCKS = BlockTags.create(new ResourceLocation(RatsMod.MODID, "diggable_blocks"));

	public static final TagKey<Block> STORAGE_BLOCKS_CHEESE = BlockTags.create(new ResourceLocation("forge", "storage_blocks/cheese"));
	public static final TagKey<Block> STORAGE_BLOCKS_BLUE_CHEESE = BlockTags.create(new ResourceLocation("forge", "storage_blocks/blue_cheese"));
	public static final TagKey<Block> STORAGE_BLOCKS_NETHER_CHEESE = BlockTags.create(new ResourceLocation("forge", "storage_blocks/nether_cheese"));

	public static final TagKey<Block> CRAFTING_TABLES = BlockTags.create(new ResourceLocation("forge", "crafting_tables"));

	public RatsBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper helper) {
		super(output, provider, RatsMod.MODID, helper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {

		this.tag(CRAFTING_TABLES).add(Blocks.CRAFTING_TABLE, RatsBlockRegistry.RAT_CRAFTING_TABLE.get());

		this.tag(QUARRY_IGNORABLES).add(RatsBlockRegistry.RAT_QUARRY.get(), RatsBlockRegistry.RAT_QUARRY_PLATFORM.get()).addTag(BlockTags.WITHER_IMMUNE);

		this.tag(MARBLED_CHEESE).add(RatsBlockRegistry.MARBLED_CHEESE_RAW.get());

		this.tag(TRASH_CAN_BLACKLIST)
				.add(RatsBlockRegistry.TRASH_CAN.get())
				.addTag(Tags.Blocks.STORAGE_BLOCKS)
				.addTag(BlockTags.WITHER_IMMUNE);

		this.tag(UNRAIDABLE_CONTAINERS);
		this.tag(DIGGABLE_BLOCKS)
				.addTag(BlockTags.PLANKS)
				.addTag(BlockTags.LOGS);

		this.tag(STORAGE_BLOCKS_CHEESE).add(RatsBlockRegistry.BLOCK_OF_CHEESE.get());
		this.tag(STORAGE_BLOCKS_BLUE_CHEESE).add(RatsBlockRegistry.BLOCK_OF_BLUE_CHEESE.get());
		this.tag(STORAGE_BLOCKS_NETHER_CHEESE).add(RatsBlockRegistry.BLOCK_OF_NETHER_CHEESE.get());
		this.tag(Tags.Blocks.STORAGE_BLOCKS).addTag(STORAGE_BLOCKS_CHEESE)
				.addTag(STORAGE_BLOCKS_BLUE_CHEESE).addTag(STORAGE_BLOCKS_NETHER_CHEESE);

		this.tag(BlockTags.CAULDRONS).add(RatsBlockRegistry.CHEESE_CAULDRON.get(), RatsBlockRegistry.MILK_CAULDRON.get(), RatsBlockRegistry.BLUE_CHEESE_CAULDRON.get(), RatsBlockRegistry.NETHER_CHEESE_CAULDRON.get());
		this.tag(BlockTags.WOOL).add(RatsBlockRegistry.PIED_WOOL.get());

		this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
				RatsBlockRegistry.MILK_CAULDRON.get(), RatsBlockRegistry.CHEESE_CAULDRON.get(),
				RatsBlockRegistry.BLUE_CHEESE_CAULDRON.get(), RatsBlockRegistry.NETHER_CHEESE_CAULDRON.get(),
				RatsBlockRegistry.RAT_CAGE.get(), RatsBlockRegistry.RAT_CAGE_BREEDING_LANTERN.get(),
				RatsBlockRegistry.RAT_CAGE_DECORATED.get(), RatsBlockRegistry.RAT_CAGE_WHEEL.get(),
				RatsBlockRegistry.AUTO_CURDLER.get(), RatsBlockRegistry.RAT_TUBE_COLOR.get(),
				RatsBlockRegistry.MARBLED_CHEESE_RAW.get(), RatsBlockRegistry.UPGRADE_COMBINER.get(),
				RatsBlockRegistry.UPGRADE_SEPARATOR.get(), RatsBlockRegistry.MANHOLE.get(),
				RatsBlockRegistry.TRASH_CAN.get(), RatsBlockRegistry.RAT_ATTRACTOR.get(),
				RatsBlockRegistry.RAT_QUARRY.get());

		this.tag(BlockTags.MINEABLE_WITH_AXE).add(
				RatsBlockRegistry.FISH_BARREL.get(), RatsBlockRegistry.RAT_HOLE.get(),
				RatsBlockRegistry.RAT_TRAP.get(), RatsBlockRegistry.JACK_O_RATERN.get());

		this.tag(BlockTags.MINEABLE_WITH_SHOVEL).add(
				RatsBlockRegistry.GARBAGE_PILE.get(), RatsBlockRegistry.COMPRESSED_GARBAGE.get(),
				RatsBlockRegistry.PIED_GARBAGE.get(), RatsBlockRegistry.PURIFIED_GARBAGE.get(),
				RatsBlockRegistry.CURSED_GARBAGE.get());

		this.tag(BlockTags.MINEABLE_WITH_HOE).add(
				RatsBlockRegistry.DYE_SPONGE.get(), RatsBlockRegistry.BLOCK_OF_CHEESE.get(),
				RatsBlockRegistry.BLOCK_OF_BLUE_CHEESE.get(), RatsBlockRegistry.BLOCK_OF_NETHER_CHEESE.get(),
				RatsBlockRegistry.PIED_WOOL.get(), RatsBlockRegistry.RAT_CRAFTING_TABLE.get(),
				RatsBlockRegistry.RAT_UPGRADE_BLOCK.get(), RatsBlockRegistry.RAT_QUARRY_PLATFORM.get());
	}
}
