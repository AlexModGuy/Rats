package com.github.alexthe666.rats.data.tags;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.registry.RatsBannerPatternRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BannerPatternTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class RatsBannerPatternTags extends BannerPatternTagsProvider {

	public static final TagKey<BannerPattern> RAT_BANNER_PATTERN = create("pattern_item/rat");
	public static final TagKey<BannerPattern> CHEESE_BANNER_PATTERN = create("pattern_item/cheese");
	public static final TagKey<BannerPattern> RAC_BANNER_PATTERN = create("pattern_item/rat_and_crossbones");
	public static final TagKey<BannerPattern> RAS_BANNER_PATTERN = create("pattern_item/rat_and_sickle");

	public RatsBannerPatternTags(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper helper) {
		super(output, provider, RatsMod.MODID, helper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		this.tag(RAT_BANNER_PATTERN).add(RatsBannerPatternRegistry.RAT_PATTERN.getKey());
		this.tag(CHEESE_BANNER_PATTERN).add(RatsBannerPatternRegistry.CHEESE_PATTERN.getKey());
		this.tag(RAC_BANNER_PATTERN).add(RatsBannerPatternRegistry.RAT_AND_CROSSBONES_BANNER.getKey());
		this.tag(RAS_BANNER_PATTERN).add(RatsBannerPatternRegistry.RAT_AND_SICKLE_BANNER.getKey());
	}

	private static TagKey<BannerPattern> create(String name) {
		return TagKey.create(Registries.BANNER_PATTERN, new ResourceLocation(RatsMod.MODID, name));
	}
}
