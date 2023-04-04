package com.github.alexthe666.rats.server.misc;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.resources.ResourceLocation;

public class RatVariant {

	private final ResourceLocation texture;
	private final String name;
	private final boolean breedingExclusive;

	public RatVariant(RatVariant.Properties properties) {
		this(properties.texture, properties.name, properties.breedingExclusive);
	}

	public RatVariant(ResourceLocation texture, String name, boolean breedingExclusive) {
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
		private ResourceLocation texture = new ResourceLocation(RatsMod.MODID, "textures/entity/rat/rat_black.png");
		private String name = "";
		private boolean breedingExclusive = false;

		public RatVariant.Properties texture(ResourceLocation texture) {
			this.texture = texture;
			return this;
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
			RatVariant.Properties props = new RatVariant.Properties();
			props.texture = rat.texture;
			props.name = rat.name;
			props.breedingExclusive = rat.breedingExclusive;
			return props;
		}
	}
}
