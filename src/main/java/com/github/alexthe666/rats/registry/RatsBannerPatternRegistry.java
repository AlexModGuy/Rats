package com.github.alexthe666.rats.registry;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BannerPattern;

public class RatsBannerPatternRegistry {

	// ResourceKeys for our banner patterns
	public static final ResourceKey<BannerPattern> RAT_PATTERN = createKey("rat");
	public static final ResourceKey<BannerPattern> CHEESE_PATTERN = createKey("cheese");
	public static final ResourceKey<BannerPattern> RAT_AND_CROSSBONES_BANNER = createKey("rat_and_crossbones");
	public static final ResourceKey<BannerPattern> RAT_AND_SICKLE_BANNER = createKey("rat_and_sickle");

	private static ResourceKey<BannerPattern> createKey(String name) {
		return ResourceKey.create(Registries.BANNER_PATTERN, ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, name));
	}

	public static void bootstrap(BootstrapContext<BannerPattern> context) {
		register(context, RAT_PATTERN, "ratr");
		register(context, CHEESE_PATTERN, "ratc");
		register(context, RAT_AND_CROSSBONES_BANNER, "ratb");
		register(context, RAT_AND_SICKLE_BANNER, "rats");
	}

	private static void register(BootstrapContext<BannerPattern> context, ResourceKey<BannerPattern> key, String translationKey) {
		context.register(key, new BannerPattern(key.location(), translationKey));
	}
}







