package com.github.alexthe666.rats.data.ratlantis.tags;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.registry.RatlantisBlockRegistry;
import com.github.alexthe666.rats.registry.RatlantisItemRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class RatlantisItemTags extends ItemTagsProvider {

	public static final TagKey<Item> ORATCHALCUM_NUGGETS = ItemTags.create(new ResourceLocation("forge", "nuggets/oratchalcum"));
	public static final TagKey<Item> ORATCHALCUM_INGOTS = ItemTags.create(new ResourceLocation("forge", "ingots/oratchalcum"));
	public static final TagKey<Item> RAW_ORATCHALCUM_INGOTS = ItemTags.create(new ResourceLocation("forge", "raw_materials/oratchalcum"));
	public static final TagKey<Item> RATLANTIS_GEMS = ItemTags.create(new ResourceLocation("forge", "ingots/ratlantis_gem"));

	public static final TagKey<Item> PIRAT_LOGS = ItemTags.create(new ResourceLocation(RatsMod.MODID, "pirat_logs"));
	public static final TagKey<Item> STORAGE_BLOCKS_ORATCHALCUM = ItemTags.create(new ResourceLocation("forge", "storage_blocks/oratchalcum"));

	public static final TagKey<Item> ORES_CHEESE = ItemTags.create(new ResourceLocation("forge", "ores/cheese"));
	public static final TagKey<Item> ORES_GEM_OF_RATLANTIS = ItemTags.create(new ResourceLocation("forge", "ores/gem_of_ratlantis"));
	public static final TagKey<Item> ORES_ORATCHALCUM = ItemTags.create(new ResourceLocation("forge", "ores/oratchalcum"));

	public RatlantisItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, CompletableFuture<TagLookup<Block>> tags, @Nullable ExistingFileHelper helper) {
		super(output, provider, tags, RatsMod.MODID, helper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		this.copy(BlockTags.PLANKS, ItemTags.PLANKS);
		this.copy(BlockTags.WOODEN_FENCES, ItemTags.WOODEN_FENCES);
		this.copy(Tags.Blocks.FENCES, Tags.Items.FENCES);
		this.copy(Tags.Blocks.FENCE_GATES, Tags.Items.FENCE_GATES);
		this.copy(Tags.Blocks.FENCES_WOODEN, Tags.Items.FENCES_WOODEN);
		this.copy(Tags.Blocks.FENCE_GATES_WOODEN, Tags.Items.FENCE_GATES_WOODEN);
		this.copy(BlockTags.WOODEN_SLABS, ItemTags.WOODEN_SLABS);
		this.copy(BlockTags.SLABS, ItemTags.SLABS);
		this.copy(BlockTags.WOODEN_STAIRS, ItemTags.WOODEN_STAIRS);
		this.copy(BlockTags.STAIRS, ItemTags.STAIRS);
		this.copy(BlockTags.WOODEN_BUTTONS, ItemTags.WOODEN_BUTTONS);
		this.copy(BlockTags.WOODEN_PRESSURE_PLATES, ItemTags.WOODEN_PRESSURE_PLATES);
		this.copy(BlockTags.WOODEN_TRAPDOORS, ItemTags.WOODEN_TRAPDOORS);
		this.copy(BlockTags.WOODEN_DOORS, ItemTags.WOODEN_DOORS);
		this.copy(RatlantisBlockTags.PIRAT_LOGS, PIRAT_LOGS);

		this.copy(RatlantisBlockTags.STORAGE_BLOCKS_ORATCHALCUM, STORAGE_BLOCKS_ORATCHALCUM);
		this.copy(RatlantisBlockTags.ORES_CHEESE, ORES_CHEESE);
		this.copy(RatlantisBlockTags.ORES_GEM_OF_RATLANTIS, ORES_GEM_OF_RATLANTIS);
		this.copy(RatlantisBlockTags.ORES_ORATCHALCUM, ORES_ORATCHALCUM);
		this.tag(ORATCHALCUM_NUGGETS).add(RatlantisItemRegistry.ORATCHALCUM_NUGGET.get());
		this.tag(ORATCHALCUM_INGOTS).add(RatlantisItemRegistry.ORATCHALCUM_INGOT.get());
		this.tag(RAW_ORATCHALCUM_INGOTS).add(RatlantisItemRegistry.RAW_ORATCHALCUM.get());
		this.tag(RATLANTIS_GEMS).add(RatlantisItemRegistry.GEM_OF_RATLANTIS.get());

		this.tag(Tags.Items.INGOTS).addTag(ORATCHALCUM_INGOTS);
		this.tag(Tags.Items.NUGGETS).addTag(ORATCHALCUM_NUGGETS);
		this.tag(Tags.Items.RAW_MATERIALS).addTag(RAW_ORATCHALCUM_INGOTS);
		this.tag(Tags.Items.GEMS).addTag(RATLANTIS_GEMS);
		this.tag(Tags.Items.ORES).addTag(ORES_ORATCHALCUM).addTag(ORES_GEM_OF_RATLANTIS).addTag(ORES_CHEESE);

		this.tag(Tags.Items.HEADS).add(RatlantisBlockRegistry.MARBLED_CHEESE_RAT_HEAD.get().asItem());
		this.tag(Tags.Items.ARMORS_HELMETS).add(
				RatlantisItemRegistry.RATLANTIS_HELMET.get(), RatlantisItemRegistry.AVIATOR_HAT.get(),
				RatlantisItemRegistry.MILITARY_HAT.get(), RatlantisItemRegistry.GHOST_PIRAT_HAT.get());
		this.tag(Tags.Items.ARMORS_CHESTPLATES).add(RatlantisItemRegistry.RATLANTIS_CHESTPLATE.get());
		this.tag(Tags.Items.ARMORS_LEGGINGS).add(RatlantisItemRegistry.RATLANTIS_LEGGINGS.get());
		this.tag(Tags.Items.ARMORS_BOOTS).add(RatlantisItemRegistry.RATLANTIS_BOOTS.get());

		this.tag(Tags.Items.TOOLS_BOWS).add(RatlantisItemRegistry.RATLANTIS_BOW.get());

		this.tag(ItemTags.CLUSTER_MAX_HARVESTABLES).add(RatlantisItemRegistry.RATLANTIS_PICKAXE.get());
		this.tag(ItemTags.AXES).add(RatlantisItemRegistry.RATLANTIS_AXE.get());
		this.tag(ItemTags.HOES).add(RatlantisItemRegistry.RATLANTIS_HOE.get());
		this.tag(ItemTags.PICKAXES).add(RatlantisItemRegistry.RATLANTIS_PICKAXE.get());
		this.tag(ItemTags.SWORDS).add(RatlantisItemRegistry.RATLANTIS_SWORD.get());
		this.tag(ItemTags.SHOVELS).add(RatlantisItemRegistry.RATLANTIS_SHOVEL.get());

		this.tag(ItemTags.TRIMMABLE_ARMOR).add(
				RatlantisItemRegistry.RATLANTIS_HELMET.get(), RatlantisItemRegistry.RATLANTIS_CHESTPLATE.get(),
				RatlantisItemRegistry.RATLANTIS_LEGGINGS.get(), RatlantisItemRegistry.RATLANTIS_BOOTS.get()
		);

		this.tag(ItemTags.TRIM_MATERIALS).add(RatlantisItemRegistry.GEM_OF_RATLANTIS.get(), RatlantisItemRegistry.ORATCHALCUM_INGOT.get());

		this.tag(ItemTags.BEACON_PAYMENT_ITEMS).add(RatlantisItemRegistry.ORATCHALCUM_INGOT.get());
		this.tag(ItemTags.FISHES).add(RatlantisItemRegistry.RATFISH.get());
	}
}
