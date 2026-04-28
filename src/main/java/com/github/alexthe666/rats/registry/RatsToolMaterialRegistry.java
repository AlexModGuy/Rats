package com.github.alexthe666.rats.registry;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.SimpleTier;

public class RatsToolMaterialRegistry {

	public static final Tier CUTLASS = new SimpleTier(
			BlockTags.INCORRECT_FOR_IRON_TOOL,
			300, 5.0F, 4.5F, 20,
			() -> Ingredient.of(Items.IRON_INGOT));

	public static final Tier GHOST_CUTLASS = new SimpleTier(
			BlockTags.INCORRECT_FOR_IRON_TOOL,
			300, 5.0F, 4.5F, 20,
			() -> Ingredient.of(RatlantisItemRegistry.GHOST_PIRAT_ECTOPLASM.get()));

	public static final Tier BAGHNAKHS = new SimpleTier(
			BlockTags.INCORRECT_FOR_STONE_TOOL,
			500, 2.0F, 3.5F, 15,
			() -> Ingredient.of(RatlantisItemRegistry.FERAL_RAT_CLAW.get()));

	public static final Tier PLAGUE_SCYTHE = new SimpleTier(
			BlockTags.INCORRECT_FOR_IRON_TOOL,
			1500, 5.0F, 6.0F, 20,
			() -> Ingredient.of(RatsItemRegistry.PLAGUE_ESSENCE.get()));

	public static final Tier RATLANTIS = new SimpleTier(
			BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
			3500, 9.0F, 7.0F, 20,
			() -> Ingredient.of(RatlantisItemRegistry.ORATCHALCUM_INGOT.get()));

}
