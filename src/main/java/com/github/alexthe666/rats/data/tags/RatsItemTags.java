package com.github.alexthe666.rats.data.tags;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.registry.RatsBlockRegistry;
import com.github.alexthe666.rats.registry.RatsItemRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class RatsItemTags extends ItemTagsProvider {

	public static final TagKey<Item> CHEESE_ITEMS = ItemTags.create(new ResourceLocation("forge", "cheese"));

	public static final TagKey<Item> MILK_BUCKETS = ItemTags.create(new ResourceLocation("forge", "milk"));

	public static final TagKey<Item> CRAFTING_TABLES = ItemTags.create(new ResourceLocation("forge", "crafting_tables"));
	public static final TagKey<Item> VEGETABLES = ItemTags.create(new ResourceLocation("forge", "vegetables"));
	public static final TagKey<Item> PLASTICS = ItemTags.create(new ResourceLocation("forge", "plastics"));
	public static final TagKey<Item> HIDES_RAT_WHISKERS = ItemTags.create(new ResourceLocation(RatsMod.MODID, "hides_rat_whiskers"));

	public static final TagKey<Item> IGLOOS = ItemTags.create(new ResourceLocation(RatsMod.MODID, "igloos"));
	public static final TagKey<Item> TUBES = ItemTags.create(new ResourceLocation(RatsMod.MODID, "tubes"));
	public static final TagKey<Item> HAMMOCKS = ItemTags.create(new ResourceLocation(RatsMod.MODID, "hammocks"));
	public static final TagKey<Item> MARBLED_CHEESE = ItemTags.create(new ResourceLocation(RatsMod.MODID, "marbled_cheese"));

	public static final TagKey<Item> STORAGE_BLOCKS_CHEESE = ItemTags.create(new ResourceLocation("forge", "storage_blocks/cheese"));
	public static final TagKey<Item> STORAGE_BLOCKS_BLUE_CHEESE = ItemTags.create(new ResourceLocation("forge", "storage_blocks/blue_cheese"));
	public static final TagKey<Item> STORAGE_BLOCKS_NETHER_CHEESE = ItemTags.create(new ResourceLocation("forge", "storage_blocks/nether_cheese"));

	public RatsItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, CompletableFuture<TagLookup<Block>> tags, @Nullable ExistingFileHelper helper) {
		super(output, provider, tags, RatsMod.MODID, helper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		this.tag(CHEESE_ITEMS).add(RatsItemRegistry.CHEESE.get());
		this.tag(VEGETABLES).add(Items.CARROT, Items.POTATO, Items.BEETROOT);
		this.tag(PLASTICS).add(RatsItemRegistry.RAW_PLASTIC.get());

		this.copy(RatsBlockTags.MARBLED_CHEESE, MARBLED_CHEESE);
		this.copy(RatsBlockTags.CRAFTING_TABLES, CRAFTING_TABLES);
		this.copy(RatsBlockTags.STORAGE_BLOCKS_CHEESE, STORAGE_BLOCKS_CHEESE);
		this.copy(RatsBlockTags.STORAGE_BLOCKS_BLUE_CHEESE, STORAGE_BLOCKS_BLUE_CHEESE);
		this.copy(RatsBlockTags.STORAGE_BLOCKS_NETHER_CHEESE, STORAGE_BLOCKS_NETHER_CHEESE);

		for (DyeColor color : DyeColor.values()) {
			this.tag(HAMMOCKS).add(ResourceKey.create(Registries.ITEM, new ResourceLocation(RatsMod.MODID, "rat_hammock_" + color.getName())));
			this.tag(IGLOOS).add(ResourceKey.create(Registries.ITEM, new ResourceLocation(RatsMod.MODID, "rat_igloo_" + color.getName())));
			this.tag(TUBES).add(ResourceKey.create(Registries.ITEM, new ResourceLocation(RatsMod.MODID, "rat_tube_" + color.getName())));
		}

		this.tag(HIDES_RAT_WHISKERS).addTag(Tags.Items.HEADS).add(
				Items.CARVED_PUMPKIN, Items.JACK_O_LANTERN,
				RatsItemRegistry.BLACK_DEATH_MASK.get(), RatsItemRegistry.PLAGUE_DOCTOR_MASK.get());

		this.tag(Tags.Items.ARMORS_HELMETS).add(
				RatsItemRegistry.PIPER_HAT.get(), RatsItemRegistry.ARCHEOLOGIST_HAT.get(),
				RatsItemRegistry.FARMER_HAT.get(), RatsItemRegistry.FISHERMAN_HAT.get(),
				RatsItemRegistry.PLAGUE_DOCTOR_MASK.get(), RatsItemRegistry.BLACK_DEATH_MASK.get(),
				RatsItemRegistry.HALO_HAT.get(), RatsItemRegistry.PIRAT_HAT.get(),
				RatsItemRegistry.TOP_HAT.get(), RatsItemRegistry.HALO_HAT.get(),
				RatsItemRegistry.SANTA_HAT.get(), RatsItemRegistry.EXTERMINATOR_HAT.get(),
				RatsItemRegistry.PARTY_HAT.get());

		this.tag(ItemTags.MUSIC_DISCS).add(RatsItemRegistry.MUSIC_DISC_MICE_ON_VENUS.get(), RatsItemRegistry.MUSIC_DISC_LIVING_MICE.get());
		this.tag(ItemTags.PIGLIN_LOVED).add(RatsItemRegistry.RAT_KING_CROWN.get(), RatsItemRegistry.GOLDEN_RAT_SKULL.get(), RatsItemRegistry.HALO_HAT.get());
		this.tag(ItemTags.WOOL).add(RatsBlockRegistry.PIED_WOOL.get().asItem());
		this.tag(ItemTags.ARROWS).add(RatsItemRegistry.RAT_ARROW.get());
	}
}
