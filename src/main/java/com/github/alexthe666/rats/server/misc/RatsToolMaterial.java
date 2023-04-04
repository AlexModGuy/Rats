package com.github.alexthe666.rats.server.misc;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.registry.RatlantisItemRegistry;
import com.github.alexthe666.rats.registry.RatsItemRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.TierSortingRegistry;

import java.util.List;

public class RatsToolMaterial {

	public static final Tier CUTLASS = TierSortingRegistry.registerTier(
			new ForgeTier(2, 300, 5.0F, 6.5F, 30,
					BlockTags.create(new ResourceLocation(RatsMod.MODID, "needs_cutlass")),
					() -> Ingredient.of(Items.IRON_INGOT)),
			new ResourceLocation("cutlass"), List.of(Tiers.IRON), List.of(Tiers.DIAMOND));

	public static final Tier GHOST_CUTLASS = TierSortingRegistry.registerTier(
			new ForgeTier(2, 300, 5.0F, 6.5F, 30,
					BlockTags.create(new ResourceLocation(RatsMod.MODID, "needs_cutlass")),
					() -> Ingredient.of(RatlantisItemRegistry.GHOST_PIRAT_ECTOPLASM.get())),
			new ResourceLocation("ghost_cutlass"), List.of(Tiers.IRON), List.of(Tiers.DIAMOND));

	public static final Tier BAGHNAKHS = TierSortingRegistry.registerTier(
			new ForgeTier(1, 500, 2.0F, 3.5F, 15,
					BlockTags.create(new ResourceLocation(RatsMod.MODID, "needs_baghnakhs")),
					() -> Ingredient.of(RatlantisItemRegistry.FERAL_RAT_CLAW.get())),
			new ResourceLocation("baghnakhs"), List.of(Tiers.STONE), List.of(Tiers.IRON));

	public static final Tier PLAGUE_SCYTHE = TierSortingRegistry.registerTier(
			new ForgeTier(2, 1500, 5.0F, 12F, 30,
					BlockTags.create(new ResourceLocation(RatsMod.MODID, "needs_cutlass")),
					() -> Ingredient.of(RatsItemRegistry.PLAGUE_ESSENCE.get())),
			new ResourceLocation("plague_scythe"), List.of(Tiers.IRON), List.of(Tiers.DIAMOND));

	public static final Tier RATLANTIS = TierSortingRegistry.registerTier(
			new ForgeTier(4, 3500, 8.0F, 15F, 30,
					BlockTags.create(new ResourceLocation(RatsMod.MODID, "needs_ratlantis_tool")),
					() -> Ingredient.of(RatlantisItemRegistry.ORATCHALCUM_INGOT.get())),
			new ResourceLocation("ratlantis"), List.of(Tiers.NETHERITE), List.of());

}
