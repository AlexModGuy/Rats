package com.github.alexthe666.rats.server.misc;

import com.github.alexthe666.rats.registry.RatVariantRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RatVariant {

	private final ResourceLocation texture;
	private final boolean breedingExclusive;

	public RatVariant(RatVariant.Properties properties) {
		this(properties.texture, properties.breedingExclusive);
	}

	private RatVariant(ResourceLocation texture, boolean breedingExclusive) {
		this.texture = texture;
		this.breedingExclusive = breedingExclusive;
	}

	public ResourceLocation getTexture() {
		return this.texture;
	}

	public boolean isBreedingExclusive() {
		return this.breedingExclusive;
	}

	public static class Properties {
		private final ResourceLocation texture;
		private boolean breedingExclusive = false;

		public Properties(ResourceLocation texture) {
			this.texture = texture;
		}

		public RatVariant.Properties setBreedingExclusive() {
			this.breedingExclusive = true;
			return this;
		}
	}

	public static RatVariant getRandomVariant(RandomSource random, boolean fromBreeding) {
		// 1.21: Registry.getValues() removed; use stream().toList().
		List<RatVariant> validVariants = new ArrayList<>(RatVariantRegistry.RAT_VARIANT_REGISTRY.stream().toList());
		validVariants.removeIf(variant -> fromBreeding != variant.isBreedingExclusive());
		return validVariants.toArray(RatVariant[]::new)[random.nextInt(validVariants.size())];
	}

	public static RatVariant getRandomBreedingExclusiveVariant(RandomSource random) {
		List<RatVariant> validVariants = new ArrayList<>(RatVariantRegistry.RAT_VARIANT_REGISTRY.stream().toList());
		validVariants.removeIf(variant -> !variant.isBreedingExclusive());
		return validVariants.toArray(RatVariant[]::new)[random.nextInt(validVariants.size())];
	}

	public static RatVariant getVariant(String id) {
		// 1.21: Registry.getValue removed; use get(ResourceLocation).
		return Optional.ofNullable(RatVariantRegistry.RAT_VARIANT_REGISTRY.get(ResourceLocation.parse(id))).orElse(RatVariantRegistry.BLUE.get());
	}

	public static String getVariantId(RatVariant variant) {
		return Optional.ofNullable(RatVariantRegistry.RAT_VARIANT_REGISTRY.getKey(variant)).orElse(RatVariantRegistry.RAT_VARIANT_REGISTRY.getKey(RatVariantRegistry.BLUE.get())).toString();
	}
}
