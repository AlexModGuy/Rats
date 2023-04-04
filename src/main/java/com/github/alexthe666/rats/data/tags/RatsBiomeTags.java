package com.github.alexthe666.rats.data.tags;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class RatsBiomeTags extends BiomeTagsProvider {

	public static final TagKey<Biome> RAT_SPAWN_BIOMES = create(new ResourceLocation(RatsMod.MODID, "rat_spawn_biomes"));
	public static final TagKey<Biome> PIPER_SPAWN_BIOMES = create(new ResourceLocation(RatsMod.MODID, "piper_spawn_biomes"));
	public static final TagKey<Biome> DEMON_RAT_SPAWN_BIOMES = create(new ResourceLocation(RatsMod.MODID, "demon_rat_spawn_biomes"));

	public RatsBiomeTags(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper helper) {
		super(output, provider, RatsMod.MODID, helper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		this.tag(RAT_SPAWN_BIOMES).add(
				Biomes.PLAINS, Biomes.SUNFLOWER_PLAINS, Biomes.SNOWY_PLAINS, Biomes.DESERT,
				Biomes.SWAMP, Biomes.MANGROVE_SWAMP, Biomes.FOREST, Biomes.FLOWER_FOREST,
				Biomes.BIRCH_FOREST, Biomes.DARK_FOREST, Biomes.OLD_GROWTH_BIRCH_FOREST, Biomes.OLD_GROWTH_PINE_TAIGA,
				Biomes.OLD_GROWTH_SPRUCE_TAIGA, Biomes.TAIGA, Biomes.SNOWY_TAIGA, Biomes.SAVANNA,
				Biomes.SAVANNA_PLATEAU, Biomes.WINDSWEPT_HILLS, Biomes.WINDSWEPT_GRAVELLY_HILLS, Biomes.WINDSWEPT_FOREST,
				Biomes.WINDSWEPT_SAVANNA, Biomes.JUNGLE, Biomes.SPARSE_JUNGLE, Biomes.BAMBOO_JUNGLE,
				Biomes.BADLANDS, Biomes.ERODED_BADLANDS, Biomes.WOODED_BADLANDS, Biomes.MEADOW,
				Biomes.GROVE, Biomes.STONY_PEAKS, Biomes.JAGGED_PEAKS, Biomes.BEACH,
				Biomes.SNOWY_BEACH, Biomes.STONY_SHORE, Biomes.DRIPSTONE_CAVES, Biomes.LUSH_CAVES);

		this.tag(PIPER_SPAWN_BIOMES).add(
				Biomes.SWAMP, Biomes.MANGROVE_SWAMP, Biomes.FOREST, Biomes.FLOWER_FOREST,
				Biomes.BIRCH_FOREST, Biomes.DARK_FOREST, Biomes.OLD_GROWTH_BIRCH_FOREST, Biomes.OLD_GROWTH_PINE_TAIGA,
				Biomes.OLD_GROWTH_SPRUCE_TAIGA, Biomes.TAIGA, Biomes.SNOWY_TAIGA, Biomes.SAVANNA,
				Biomes.SAVANNA_PLATEAU, Biomes.WINDSWEPT_HILLS, Biomes.WINDSWEPT_GRAVELLY_HILLS, Biomes.WINDSWEPT_FOREST,
				Biomes.WINDSWEPT_SAVANNA, Biomes.MEADOW, Biomes.GROVE, Biomes.BEACH, Biomes.SNOWY_BEACH);

		this.tag(DEMON_RAT_SPAWN_BIOMES).add(Biomes.BASALT_DELTAS, Biomes.NETHER_WASTES, Biomes.CRIMSON_FOREST);
	}

	private static TagKey<Biome> create(ResourceLocation name) {
		return TagKey.create(Registries.BIOME, name);
	}
}
