package com.github.alexthe666.rats.registry;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class RatsToolMaterialRegistry {

	public static final Tier CUTLASS = new SimpleTier(
			BlockTags.create(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "needs_cutlass")),
			300, 5.0F, 4.5F, 20, () -> Ingredient.of(Items.IRON_INGOT));

	public static final Tier GHOST_CUTLASS = new SimpleTier(
			BlockTags.create(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "needs_cutlass")),
			300, 5.0F, 4.5F, 20, () -> Ingredient.of(RatlantisItemRegistry.GHOST_PIRAT_ECTOPLASM.get()));

	public static final Tier BAGHNAKHS = new SimpleTier(
			BlockTags.create(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "needs_baghnakhs")),
			500, 2.0F, 3.5F, 15, () -> Ingredient.of(RatlantisItemRegistry.FERAL_RAT_CLAW.get()));

	public static final Tier PLAGUE_SCYTHE = new SimpleTier(
			BlockTags.create(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "needs_scythe")),
			1500, 5.0F, 6.0F, 20, () -> Ingredient.of(RatsItemRegistry.PLAGUE_ESSENCE.get()));

	public static final Tier RATLANTIS = new SimpleTier(
			BlockTags.create(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "needs_ratlantis_tool")),
			3500, 9.0F, 7.0F, 20, () -> Ingredient.of(RatlantisItemRegistry.ORATCHALCUM_INGOT.get()));

	private record SimpleTier(TagKey<Block> incorrectBlocksForDrops, int uses, float speed, float attackDamageBonus,
	                          int enchantmentValue, Supplier<Ingredient> repairIngredient) implements Tier {
		@Override
		public int getUses() {
			return uses;
		}

		@Override
		public float getSpeed() {
			return speed;
		}

		@Override
		public float getAttackDamageBonus() {
			return attackDamageBonus;
		}

		@Override
		public TagKey<Block> getIncorrectBlocksForDrops() {
			return incorrectBlocksForDrops;
		}

		@Override
		public int getEnchantmentValue() {
			return enchantmentValue;
		}

		@Override
		public Ingredient getRepairIngredient() {
			return repairIngredient.get();
		}
	}
}







