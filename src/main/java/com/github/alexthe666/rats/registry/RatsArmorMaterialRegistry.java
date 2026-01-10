package com.github.alexthe666.rats.registry;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.List;

/**
 * In 1.21.1, ArmorMaterial is a record that must be registered.
 * ArmorItem now takes Holder&lt;ArmorMaterial&gt; instead of ArmorMaterial enum.
 */
public class RatsArmorMaterialRegistry {

	public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS = DeferredRegister.create(Registries.ARMOR_MATERIAL, RatsMod.MODID);

	// Helper method to create defense map
	private static EnumMap<ArmorItem.Type, Integer> makeDefenseMap(int helmet, int chestplate, int leggings, int boots) {
		return Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
			map.put(ArmorItem.Type.BOOTS, boots);
			map.put(ArmorItem.Type.LEGGINGS, leggings);
			map.put(ArmorItem.Type.CHESTPLATE, chestplate);
			map.put(ArmorItem.Type.HELMET, helmet);
			map.put(ArmorItem.Type.BODY, chestplate);
		});
	}

	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> PIPER_HAT = ARMOR_MATERIALS.register("piper_hat",
		() -> new ArmorMaterial(
			makeDefenseMap(2, 1, 1, 1),
			15, // enchantability
			SoundEvents.ARMOR_EQUIP_GENERIC,
			() -> Ingredient.of(RatsBlockRegistry.PIED_WOOL.get()),
			List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "piper_hat"))),
			0.0F, // toughness
			0.0F  // knockback resistance
		));

	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> CHEF_TOQUE = ARMOR_MATERIALS.register("chef_toque",
		() -> new ArmorMaterial(
			makeDefenseMap(1, 1, 1, 1),
			15,
			SoundEvents.ARMOR_EQUIP_GENERIC,
			() -> Ingredient.of(Blocks.WHITE_WOOL),
			List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "chef_toque"))),
			0.0F,
			0.0F
		));

	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> PLAGUE_MASK = ARMOR_MATERIALS.register("plague_mask",
		() -> new ArmorMaterial(
			makeDefenseMap(3, 1, 1, 1),
			15,
			SoundEvents.ARMOR_EQUIP_GENERIC,
			() -> Ingredient.of(RatsItemRegistry.PLAGUE_ESSENCE.get()),
			List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "plague_mask"))),
			1.0F,
			0.0F
		));

	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> RATLANTIS = ARMOR_MATERIALS.register("ratlantis",
		() -> new ArmorMaterial(
			makeDefenseMap(5, 10, 8, 5),
			17,
			SoundEvents.ARMOR_EQUIP_NETHERITE,
			() -> Ingredient.of(RatlantisItemRegistry.ORATCHALCUM_INGOT.get()),
			List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "ratlantis"))),
			4.0F,
			0.1F
		));

	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> FARMER_HAT = ARMOR_MATERIALS.register("farmer_hat",
		() -> new ArmorMaterial(
			makeDefenseMap(1, 1, 1, 1),
			15,
			SoundEvents.ARMOR_EQUIP_GENERIC,
			() -> Ingredient.of(Items.HAY_BLOCK),
			List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "farmer_hat"))),
			0.0F,
			0.0F
		));

	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> TOP_HAT = ARMOR_MATERIALS.register("top_hat",
		() -> new ArmorMaterial(
			makeDefenseMap(1, 1, 1, 1),
			15,
			SoundEvents.ARMOR_EQUIP_GENERIC,
			() -> Ingredient.of(Items.BLACK_WOOL),
			List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "top_hat"))),
			0.0F,
			0.0F
		));

	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> FEZ = ARMOR_MATERIALS.register("fez",
		() -> new ArmorMaterial(
			makeDefenseMap(1, 1, 1, 1),
			100,
			SoundEvents.ARMOR_EQUIP_GENERIC,
			() -> Ingredient.of(Items.RED_WOOL),
			List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "fez"))),
			0.0F,
			0.0F
		));

	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> SANTA_HAT = ARMOR_MATERIALS.register("santa_hat",
		() -> new ArmorMaterial(
			makeDefenseMap(1, 1, 1, 1),
			15,
			SoundEvents.ARMOR_EQUIP_GENERIC,
			() -> Ingredient.of(Items.RED_WOOL),
			List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "santa_hat"))),
			0.0F,
			0.0F
		));

	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> HALO = ARMOR_MATERIALS.register("halo",
		() -> new ArmorMaterial(
			makeDefenseMap(1, 1, 1, 1),
			15,
			SoundEvents.ARMOR_EQUIP_GENERIC,
			() -> Ingredient.of(Items.GOLD_NUGGET),
			List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "halo"))),
			0.0F,
			0.0F
		));

	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> CROWN = ARMOR_MATERIALS.register("crown",
		() -> new ArmorMaterial(
			makeDefenseMap(1, 1, 1, 1),
			15,
			SoundEvents.ARMOR_EQUIP_GENERIC,
			() -> Ingredient.of(Items.GOLD_INGOT),
			List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "crown"))),
			0.0F,
			0.0F
		));

	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> GHOST_HAT = ARMOR_MATERIALS.register("ghost_hat",
		() -> new ArmorMaterial(
			makeDefenseMap(1, 1, 1, 1),
			15,
			SoundEvents.ARMOR_EQUIP_GENERIC,
			() -> Ingredient.of(RatlantisItemRegistry.GHOST_PIRAT_ECTOPLASM.get()),
			List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "ghost_hat"))),
			0.0F,
			0.0F
		));

	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> GENERIC_HAT = ARMOR_MATERIALS.register("generic_hat",
		() -> new ArmorMaterial(
			makeDefenseMap(1, 1, 1, 1),
			100,
			SoundEvents.ARMOR_EQUIP_GENERIC,
			() -> Ingredient.of(Items.LEATHER),
			List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "generic_hat"))),
			0.0F,
			0.0F
		));
}







