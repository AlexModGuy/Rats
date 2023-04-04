package com.github.alexthe666.rats.registry;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class RatsBannerPatternRegistry {

	public static final DeferredRegister<BannerPattern> PATTERNS = DeferredRegister.create(Registries.BANNER_PATTERN, RatsMod.MODID);

	public static final RegistryObject<BannerPattern> RAT_PATTERN = PATTERNS.register("rat", () -> new BannerPattern("ratr"));
	public static final RegistryObject<BannerPattern> CHEESE_PATTERN = PATTERNS.register("cheese", () -> new BannerPattern("ratc"));
	public static final RegistryObject<BannerPattern> RAT_AND_CROSSBONES_BANNER = PATTERNS.register("rat_and_crossbones", () -> new BannerPattern("ratb"));
	public static final RegistryObject<BannerPattern> RAT_AND_SICKLE_BANNER = PATTERNS.register("rat_and_sickle", () -> new BannerPattern("rats"));
}
