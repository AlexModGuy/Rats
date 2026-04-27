package com.github.alexthe666.rats.registry;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.ForgeTier;
import net.neoforged.neoforge.common.TierSortingRegistry;

import java.util.List;

public class RatsToolMaterialRegistry {

	public static final Tier CUTLASS = TierSortingRegistry.registerTier(
			new ForgeTier(2, 300, 5.0F, 4.5F, 20,
					BlockTags.create(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "needs_cutlass")),
					() -> Ingredient.of(Items.IRON_INGOT)),
			ResourceLocation.parse("cutlass"), List.of(Tiers.IRON), List.of(Tiers.DIAMOND));

	public static final Tier GHOST_CUTLASS = TierSortingRegistry.registerTier(
			new ForgeTier(2, 300, 5.0F, 4.5F, 20,
					BlockTags.create(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "needs_cutlass")),
					() -> Ingredient.of(RatlantisItemRegistry.GHOST_PIRAT_ECTOPLASM.get())),
			ResourceLocation.parse("ghost_cutlass"), List.of(Tiers.IRON), List.of(Tiers.DIAMOND));

	public static final Tier BAGHNAKHS = TierSortingRegistry.registerTier(
			new ForgeTier(1, 500, 2.0F, 3.5F, 15,
					BlockTags.create(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "needs_baghnakhs")),
					() -> Ingredient.of(RatlantisItemRegistry.FERAL_RAT_CLAW.get())),
			ResourceLocation.parse("baghnakhs"), List.of(Tiers.STONE), List.of(Tiers.IRON));

	public static final Tier PLAGUE_SCYTHE = TierSortingRegistry.registerTier(
			new ForgeTier(2, 1500, 5.0F, 6.0F, 20,
					BlockTags.create(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "needs_scythe")),
					() -> Ingredient.of(RatsItemRegistry.PLAGUE_ESSENCE.get())),
			ResourceLocation.parse("plague_scythe"), List.of(Tiers.IRON), List.of(Tiers.DIAMOND));

	public static final Tier RATLANTIS = TierSortingRegistry.registerTier(
			new ForgeTier(4, 3500, 9.0F, 7.0F, 20,
					BlockTags.create(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "needs_ratlantis_tool")),
					() -> Ingredient.of(RatlantisItemRegistry.ORATCHALCUM_INGOT.get())),
			ResourceLocation.parse("ratlantis"), List.of(Tiers.NETHERITE), List.of());

}
