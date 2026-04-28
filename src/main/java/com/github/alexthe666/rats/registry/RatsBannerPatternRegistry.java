package com.github.alexthe666.rats.registry;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class RatsBannerPatternRegistry {

	public static final DeferredRegister<BannerPattern> PATTERNS = DeferredRegister.create(Registries.BANNER_PATTERN, RatsMod.MODID);

	private static DeferredHolder<BannerPattern, BannerPattern> register(String name) {
		return PATTERNS.register(name, () -> new BannerPattern(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, name), "block.rats.banner." + name));
	}

	public static final DeferredHolder<BannerPattern, BannerPattern> RAT_PATTERN = register("rat");
	public static final DeferredHolder<BannerPattern, BannerPattern> CHEESE_PATTERN = register("cheese");
	public static final DeferredHolder<BannerPattern, BannerPattern> RAT_AND_CROSSBONES_BANNER = register("rat_and_crossbones");
	public static final DeferredHolder<BannerPattern, BannerPattern> RAT_AND_SICKLE_BANNER = register("rat_and_sickle");
}
