package com.github.alexthe666.rats.registry;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.data.tags.RatsBiomeTags;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;

public class RatsBiomeModifierRegistry {
	private static final ResourceKey<BiomeModifier> ADD_RAT_SPAWNS = ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(RatsMod.MODID, "add_rat_spawns"));
	private static final ResourceKey<BiomeModifier> ADD_PIPER_SPAWNS = ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(RatsMod.MODID, "add_piper_spawns"));
	private static final ResourceKey<BiomeModifier> ADD_DEMON_RAT_SPAWNS = ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(RatsMod.MODID, "add_demon_rat_spawns"));

	public static void bootstrap(BootstapContext<BiomeModifier> context) {
		context.register(ADD_RAT_SPAWNS,
				ForgeBiomeModifiers.AddSpawnsBiomeModifier.singleSpawn(
						context.lookup(Registries.BIOME).getOrThrow(RatsBiomeTags.RAT_SPAWN_BIOMES),
						new MobSpawnSettings.SpawnerData(RatsEntityRegistry.RAT.get(), 80, 1, 3)));

		context.register(ADD_PIPER_SPAWNS,
				ForgeBiomeModifiers.AddSpawnsBiomeModifier.singleSpawn(
						context.lookup(Registries.BIOME).getOrThrow(RatsBiomeTags.PIPER_SPAWN_BIOMES),
						new MobSpawnSettings.SpawnerData(RatsEntityRegistry.PIED_PIPER.get(), 25, 1, 1)));

		context.register(ADD_DEMON_RAT_SPAWNS,
				ForgeBiomeModifiers.AddSpawnsBiomeModifier.singleSpawn(
						context.lookup(Registries.BIOME).getOrThrow(RatsBiomeTags.DEMON_RAT_SPAWN_BIOMES),
						new MobSpawnSettings.SpawnerData(RatsEntityRegistry.DEMON_RAT.get(), 15, 1, 1)));
	}
}
