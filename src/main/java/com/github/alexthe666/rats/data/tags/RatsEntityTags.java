package com.github.alexthe666.rats.data.tags;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.registry.RatsEntityRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class RatsEntityTags extends EntityTypeTagsProvider {
	public RatsEntityTags(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper helper) {
		super(output, provider, RatsMod.MODID, helper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		tag(Tags.EntityTypes.BOSSES).add(
				RatsEntityRegistry.BLACK_DEATH.get(), RatsEntityRegistry.RAT_KING.get());

		tag(EntityTypeTags.IMPACT_PROJECTILES).add(
				RatsEntityRegistry.PLAGUE_SHOT.get(), RatsEntityRegistry.RAT_ARROW.get(),
				RatsEntityRegistry.RAT_SHOT.get());

		tag(EntityTypeTags.ARROWS).add(RatsEntityRegistry.RAT_ARROW.get());

		tag(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS).add(RatsEntityRegistry.RAT.get(), RatsEntityRegistry.RAT_KING.get(), RatsEntityRegistry.RAT_MOUNT_CHICKEN.get());

		tag(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES).add(RatsEntityRegistry.DEMON_RAT.get());
	}
}
