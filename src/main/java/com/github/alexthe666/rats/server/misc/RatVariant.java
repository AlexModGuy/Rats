package com.github.alexthe666.rats.server.misc;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.registry.RatVariantRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import org.antlr.v4.runtime.misc.Triple;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RatVariant {

	private final ResourceLocation texture;
	private final String name;
	private final boolean breedingExclusive;

	public RatVariant(RatVariant.Properties properties) {
		this(properties.texture, properties.name, properties.breedingExclusive);
	}

	private RatVariant(ResourceLocation texture, String name, boolean breedingExclusive) {
		this.texture = texture;
		this.name = name;
		this.breedingExclusive = breedingExclusive;
	}

	public ResourceLocation getTexture() {
		return this.texture;
	}

	public String getName() {
		return this.name;
	}

	public boolean isBreedingExclusive() {
		return this.breedingExclusive;
	}

	public static class Properties {
		private final ResourceLocation texture;
		private String name = "";
		private boolean breedingExclusive = false;

		public Properties(ResourceLocation texture) {
			this.texture = texture;
		}

		public RatVariant.Properties setNamingExclusive(String name) {
			this.name = name;
			return this;
		}

		public RatVariant.Properties setBreedingExclusive() {
			this.breedingExclusive = true;
			return this;
		}

		public static RatVariant.Properties copyFrom(RatVariant rat) {
			RatVariant.Properties props = new RatVariant.Properties(rat.getTexture());
			props.name = rat.name;
			props.breedingExclusive = rat.breedingExclusive;
			return props;
		}
	}

	public static RatVariant getRandomVariant(RandomSource random, boolean fromBreeding) {
		List<RatVariant> validVariants = new ArrayList<>(RatVariantRegistry.RAT_VARIANT_REGISTRY.get().getValues().stream().toList());
		validVariants.removeIf(variant -> (!fromBreeding && variant.isBreedingExclusive()) || !variant.getName().isEmpty());
		return validVariants.toArray(RatVariant[]::new)[random.nextInt(validVariants.size())];
	}

	public static RatVariant getRandomBreedingExclusiveVariant(RandomSource random) {
		List<RatVariant> validVariants = new ArrayList<>(RatVariantRegistry.RAT_VARIANT_REGISTRY.get().getValues().stream().toList());
		validVariants.removeIf(variant -> !variant.isBreedingExclusive());
		return validVariants.toArray(RatVariant[]::new)[random.nextInt(validVariants.size())];
	}

	public static RatVariant getVariant(String id) {
		return Optional.ofNullable(RatVariantRegistry.RAT_VARIANT_REGISTRY.get().getValue(new ResourceLocation(id))).orElse(RatVariantRegistry.BLUE.get());
	}

	public static String getVariantId(RatVariant variant) {
		return Optional.ofNullable(RatVariantRegistry.RAT_VARIANT_REGISTRY.get().getKey(variant)).orElse(RatVariantRegistry.RAT_VARIANT_REGISTRY.get().getKey(RatVariantRegistry.BLUE.get())).toString();
	}
}
