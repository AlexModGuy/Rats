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

	//why do these HAVE to be optional? It says the biome is missing if not optional, which makes no sense. AAAAAAAAAAAAAAA
	@Override
	protected void addTags(HolderLookup.Provider provider) {
		this.tag(DUTCHRAT_SHIP_SPAWNS).addOptional(RatlantisBiomeRegistry.RATLANTIS.location());
		this.tag(BARON_RUNWAY_SPAWNS).addOptional(RatlantisBiomeRegistry.RATLANTIS.location());

		this.tag(BiomeTags.HAS_BURIED_TREASURE).addOptional(RatlantisBiomeRegistry.RATLANTIS.location());
		this.tag(BiomeTags.HAS_MINESHAFT).addOptional(RatlantisBiomeRegistry.RATLANTIS.location());
		this.tag(BiomeTags.HAS_SHIPWRECK).addOptional(RatlantisBiomeRegistry.RATLANTIS.location());
		this.tag(BiomeTags.HAS_JUNGLE_TEMPLE).addOptional(RatlantisBiomeRegistry.RATLANTIS.location());

		this.tag(BiomeTags.SPAWNS_WARM_VARIANT_FROGS).addOptional(RatlantisBiomeRegistry.RATLANTIS.location());
		this.tag(BiomeTags.WITHOUT_WANDERING_TRADER_SPAWNS).addOptional(RatlantisBiomeRegistry.RATLANTIS.location());
		this.tag(BiomeTags.WITHOUT_PATROL_SPAWNS).addOptional(RatlantisBiomeRegistry.RATLANTIS.location());
		this.tag(BiomeTags.WITHOUT_ZOMBIE_SIEGES).addOptional(RatlantisBiomeRegistry.RATLANTIS.location());
	}

	private static TagKey<Biome> create(ResourceLocation name) {
		return TagKey.create(Registries.BIOME, name);
	}
}
