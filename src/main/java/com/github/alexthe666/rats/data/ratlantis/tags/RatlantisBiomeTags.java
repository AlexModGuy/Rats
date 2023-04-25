package com.github.alexthe666.rats.data.ratlantis.tags;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.registry.worldgen.RatlantisBiomeRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class RatlantisBiomeTags extends BiomeTagsProvider {

	public static final TagKey<Biome> DUTCHRAT_SHIP_SPAWNS = create(new ResourceLocation(RatsMod.MODID, "has_structure/dutchrat_ship"));
	public static final TagKey<Biome> BARON_RUNWAY_SPAWNS = create(new ResourceLocation(RatsMod.MODID, "has_structure/baron_runway"));

	public RatlantisBiomeTags(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper helper) {
		super(output, provider, RatsMod.MODID, helper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		this.tag(DUTCHRAT_SHIP_SPAWNS).add(RatlantisBiomeRegistry.RATLANTIS);
		this.tag(BARON_RUNWAY_SPAWNS).add(RatlantisBiomeRegistry.RATLANTIS);

		this.tag(BiomeTags.HAS_BURIED_TREASURE).add(RatlantisBiomeRegistry.RATLANTIS);
		this.tag(BiomeTags.HAS_MINESHAFT).add(RatlantisBiomeRegistry.RATLANTIS);
		this.tag(BiomeTags.HAS_SHIPWRECK).add(RatlantisBiomeRegistry.RATLANTIS);
		this.tag(BiomeTags.HAS_JUNGLE_TEMPLE).add(RatlantisBiomeRegistry.RATLANTIS);

		this.tag(BiomeTags.SPAWNS_WARM_VARIANT_FROGS).add(RatlantisBiomeRegistry.RATLANTIS);
		this.tag(BiomeTags.WITHOUT_WANDERING_TRADER_SPAWNS).add(RatlantisBiomeRegistry.RATLANTIS);
		this.tag(BiomeTags.WITHOUT_PATROL_SPAWNS).add(RatlantisBiomeRegistry.RATLANTIS);
		this.tag(BiomeTags.WITHOUT_ZOMBIE_SIEGES).add(RatlantisBiomeRegistry.RATLANTIS);
	}

	private static TagKey<Biome> create(ResourceLocation name) {
		return TagKey.create(Registries.BIOME, name);
	}
}
