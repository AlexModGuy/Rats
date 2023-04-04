package com.github.alexthe666.rats.data.tags;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.registry.RatsVillagerRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.PoiTypeTags;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class RatsPoiTags extends TagsProvider<PoiType> {
	public RatsPoiTags(PackOutput output, CompletableFuture<HolderLookup.Provider> providers, @Nullable ExistingFileHelper helper) {
		super(output, Registries.POINT_OF_INTEREST_TYPE, providers, RatsMod.MODID, helper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		this.tag(PoiTypeTags.ACQUIRABLE_JOB_SITE).add(RatsVillagerRegistry.TRASH_CAN.getKey());
	}
}
