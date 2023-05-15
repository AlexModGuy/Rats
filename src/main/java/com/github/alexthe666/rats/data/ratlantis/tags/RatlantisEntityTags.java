package com.github.alexthe666.rats.data.ratlantis.tags;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.data.tags.RatsEntityTags;
import com.github.alexthe666.rats.registry.RatlantisEntityRegistry;
import com.github.alexthe666.rats.registry.RatsEntityRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class RatlantisEntityTags extends EntityTypeTagsProvider {

	public static final TagKey<EntityType<?>> RATLANTEAN = RatsEntityTags.create("ratlantean");

	public RatlantisEntityTags(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper helper) {
		super(output, provider, RatsMod.MODID, helper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		tag(RatsEntityTags.RATS).add(RatlantisEntityRegistry.GHOST_PIRAT.get(), RatlantisEntityRegistry.PIRAT.get());

		tag(RatsEntityTags.RAT_MOUNTS).add(RatlantisEntityRegistry.RAT_MOUNT_AUTOMATON.get(), RatlantisEntityRegistry.RAT_MOUNT_BIPLANE.get());

		tag(RATLANTEAN).add(
				RatlantisEntityRegistry.FERAL_RATLANTEAN.get(), RatlantisEntityRegistry.RATLANTEAN_RATBOT.get(),
				RatlantisEntityRegistry.GHOST_PIRAT.get(), RatlantisEntityRegistry.RATLANTEAN_AUTOMATON.get(),
				RatlantisEntityRegistry.RATFISH.get(), RatlantisEntityRegistry.RATLANTEAN_SPIRIT.get(),
				RatlantisEntityRegistry.NEO_RATLANTEAN.get(), RatlantisEntityRegistry.DUTCHRAT.get(),
				RatlantisEntityRegistry.RAT_BARON.get(), RatlantisEntityRegistry.RAT_BARON_PLANE.get(),
				RatlantisEntityRegistry.PIRAT.get(), RatsEntityRegistry.RAT.get(), RatsEntityRegistry.RAT_KING.get(),
				EntityType.PARROT, EntityType.PANDA, EntityType.PUFFERFISH, EntityType.DOLPHIN);

		tag(Tags.EntityTypes.BOSSES).add(
				RatlantisEntityRegistry.DUTCHRAT.get(), RatlantisEntityRegistry.NEO_RATLANTEAN.get(),
				RatlantisEntityRegistry.RAT_BARON.get(), RatlantisEntityRegistry.RATLANTEAN_AUTOMATON.get());

		tag(EntityTypeTags.IMPACT_PROJECTILES).add(
				RatlantisEntityRegistry.LASER_BEAM.get(),
				RatlantisEntityRegistry.RATLANTIS_ARROW.get(), RatlantisEntityRegistry.RATTLING_GUN_BULLET.get());

		tag(EntityTypeTags.ARROWS).add(RatlantisEntityRegistry.RATLANTIS_ARROW.get());

		tag(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES).add(
				RatlantisEntityRegistry.DUTCHRAT.get(), RatlantisEntityRegistry.GHOST_PIRAT.get(),
				RatlantisEntityRegistry.RAT_PROTECTOR.get(), RatlantisEntityRegistry.RATLANTEAN_AUTOMATON.get(),
				RatlantisEntityRegistry.RATLANTEAN_RATBOT.get(), RatlantisEntityRegistry.RATLANTEAN_SPIRIT.get());

		tag(EntityTypeTags.FALL_DAMAGE_IMMUNE).add(
				RatlantisEntityRegistry.RATLANTEAN_SPIRIT.get(), RatlantisEntityRegistry.RAT_MOUNT_BIPLANE.get(),
				RatlantisEntityRegistry.DUTCHRAT.get(), RatlantisEntityRegistry.NEO_RATLANTEAN.get(),
				RatlantisEntityRegistry.RAT_BARON_PLANE.get(), RatlantisEntityRegistry.RAT_MOUNT_AUTOMATON.get(),
				RatlantisEntityRegistry.RATLANTEAN_AUTOMATON.get(), RatlantisEntityRegistry.RATLANTEAN_RATBOT.get());

	}
}
