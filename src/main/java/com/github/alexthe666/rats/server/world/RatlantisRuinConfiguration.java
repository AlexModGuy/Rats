package com.github.alexthe666.rats.server.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

import java.util.Map;

public record RatlantisRuinConfiguration(Map<ResourceLocation, Float> ruins, ResourceLocation defaultRuin,
										 Holder<StructureProcessorList> processor) implements FeatureConfiguration {
	public static final Codec<RatlantisRuinConfiguration> CODEC = RecordCodecBuilder.create(instance ->
			instance.group(
							Codec.unboundedMap(ResourceLocation.CODEC, Codec.FLOAT).fieldOf("ruins").forGetter(config -> config.ruins),
							ResourceLocation.CODEC.fieldOf("fallback_ruin").forGetter(config -> config.defaultRuin),
							StructureProcessorType.LIST_CODEC.fieldOf("processor").orElse(null).forGetter(config -> config.processor))
					.apply(instance, RatlantisRuinConfiguration::new));
}
