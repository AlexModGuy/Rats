package com.github.alexthe666.rats.registry;

import com.github.alexthe666.rats.registry.RatlantisItemRegistry;
import com.github.alexthe666.rats.registry.RatsBlockRegistry;
import com.github.alexthe666.rats.registry.RatsItemRegistry;
import net.minecraft.Util;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;

import java.util.EnumMap;
import java.util.function.Supplier;

public enum RatsArmorMaterialRegistry implements ArmorMaterial {
	PIPER_HAT("piper_hat", 25, Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
		map.put(ArmorItem.Type.BOOTS, 1);
		map.put(ArmorItem.Type.LEGGINGS, 1);
		map.put(ArmorItem.Type.CHESTPLATE, 1);
		map.put(ArmorItem.Type.HELMET, 2);
	}), 15, SoundEvents.ARMOR_EQUIP_GENERIC, 0.0F, 0.0F, () -> Ingredient.of(new ItemStack(RatsBlockRegistry.PIED_WOOL.get()))),
	CHEF_TOQUE("chef_toque", 0, Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
		map.put(ArmorItem.Type.BOOTS, 1);
		map.put(ArmorItem.Type.LEGGINGS, 1);
		map.put(ArmorItem.Type.CHESTPLATE, 1);
		map.put(ArmorItem.Type.HELMET, 1);
	}), 15, SoundEvents.ARMOR_EQUIP_GENERIC, 0.0F, 0.0F, () -> Ingredient.of(new ItemStack(Blocks.WHITE_WOOL))),
	PLAGUE_MASK("plague_mask", 25, Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
		map.put(ArmorItem.Type.BOOTS, 1);
		map.put(ArmorItem.Type.LEGGINGS, 1);
		map.put(ArmorItem.Type.CHESTPLATE, 1);
		map.put(ArmorItem.Type.HELMET, 3);
	}), 15, SoundEvents.ARMOR_EQUIP_GENERIC, 1.0F, 0.0F, () -> Ingredient.of(new ItemStack(RatsItemRegistry.PLAGUE_ESSENCE.get()))),
	RATLANTIS("ratlantis", 40, Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
		map.put(ArmorItem.Type.BOOTS, 4);
		map.put(ArmorItem.Type.LEGGINGS, 6);
		map.put(ArmorItem.Type.CHESTPLATE, 8);
		map.put(ArmorItem.Type.HELMET, 4);
	}), 17, SoundEvents.ARMOR_EQUIP_NETHERITE, 4.0F, 0.1F, () -> Ingredient.of(new ItemStack(RatlantisItemRegistry.ORATCHALCUM_INGOT.get()))),

	FARMER_HAT("farmer_hat", 0, Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
		map.put(ArmorItem.Type.BOOTS, 1);
		map.put(ArmorItem.Type.LEGGINGS, 1);
		map.put(ArmorItem.Type.CHESTPLATE, 1);
		map.put(ArmorItem.Type.HELMET, 1);
	}), 15, SoundEvents.ARMOR_EQUIP_GENERIC, 0.0F, 0.0F, () -> Ingredient.of(new ItemStack(Items.HAY_BLOCK))),
	TOP_HAT("top_hat", 0, Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
		map.put(ArmorItem.Type.BOOTS, 1);
		map.put(ArmorItem.Type.LEGGINGS, 1);
		map.put(ArmorItem.Type.CHESTPLATE, 1);
		map.put(ArmorItem.Type.HELMET, 1);
	}), 15, SoundEvents.ARMOR_EQUIP_GENERIC, 0.0F, 0.0F, () -> Ingredient.of(new ItemStack(Items.BLACK_WOOL))),
	FEZ("fez", 0, Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
		map.put(ArmorItem.Type.BOOTS, 1);
		map.put(ArmorItem.Type.LEGGINGS, 1);
		map.put(ArmorItem.Type.CHESTPLATE, 1);
		map.put(ArmorItem.Type.HELMET, 1);
	}), 100, SoundEvents.ARMOR_EQUIP_GENERIC, 0.0F, 0.0F, () -> Ingredient.of(new ItemStack(Items.RED_WOOL))),
	SANTA_HAT("santa_hat", 0, Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
		map.put(ArmorItem.Type.BOOTS, 1);
		map.put(ArmorItem.Type.LEGGINGS, 1);
		map.put(ArmorItem.Type.CHESTPLATE, 1);
		map.put(ArmorItem.Type.HELMET, 1);
	}), 15, SoundEvents.ARMOR_EQUIP_GENERIC, 0.0F, 0.0F, () -> Ingredient.of(new ItemStack(Items.RED_WOOL))),
	HALO("halo", 0, Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
		map.put(ArmorItem.Type.BOOTS, 1);
		map.put(ArmorItem.Type.LEGGINGS, 1);
		map.put(ArmorItem.Type.CHESTPLATE, 1);
		map.put(ArmorItem.Type.HELMET, 1);
	}), 15, SoundEvents.ARMOR_EQUIP_GENERIC, 0.0F, 0.0F, () -> Ingredient.of(new ItemStack(Items.GOLD_NUGGET))),
	CROWN("crown", 0, Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
		map.put(ArmorItem.Type.BOOTS, 1);
		map.put(ArmorItem.Type.LEGGINGS, 1);
		map.put(ArmorItem.Type.CHESTPLATE, 1);
		map.put(ArmorItem.Type.HELMET, 1);
	}), 15, SoundEvents.ARMOR_EQUIP_GENERIC, 0.0F, 0.0F, () -> Ingredient.of(new ItemStack(Items.GOLD_INGOT))),
	GHOST_HAT("ghost_hat", 0, Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
		map.put(ArmorItem.Type.BOOTS, 1);
		map.put(ArmorItem.Type.LEGGINGS, 1);
		map.put(ArmorItem.Type.CHESTPLATE, 1);
		map.put(ArmorItem.Type.HELMET, 1);
	}), 15, SoundEvents.ARMOR_EQUIP_GENERIC, 0.0F, 0.0F, () -> Ingredient.of(new ItemStack(RatlantisItemRegistry.GHOST_PIRAT_ECTOPLASM.get()))),
	GENERIC_HAT("generic_hat", 0, Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
		map.put(ArmorItem.Type.BOOTS, 1);
		map.put(ArmorItem.Type.LEGGINGS, 1);
		map.put(ArmorItem.Type.CHESTPLATE, 1);
		map.put(ArmorItem.Type.HELMET, 1);
	}), 100, SoundEvents.ARMOR_EQUIP_GENERIC, 0.0F, 0.0F, () -> Ingredient.of(new ItemStack(Items.LEATHER)));

	private static final EnumMap<ArmorItem.Type, Integer> HEALTH_FUNCTION_FOR_TYPE = Util.make(new EnumMap<>(ArmorItem.Type.class), (p_266653_) -> {
		p_266653_.put(ArmorItem.Type.BOOTS, 13);
		p_266653_.put(ArmorItem.Type.LEGGINGS, 15);
		p_266653_.put(ArmorItem.Type.CHESTPLATE, 16);
		p_266653_.put(ArmorItem.Type.HELMET, 11);
	});
	private final String name;
	private final int durabilityMultiplier;
	private final EnumMap<ArmorItem.Type, Integer> protectionFunctionForType;
	private final int enchantability;
	private final SoundEvent equipSound;
	private final float toughness;
	private final float knockback;
	private final Supplier<Ingredient> repairMaterial;

	RatsArmorMaterialRegistry(String name, int durability, EnumMap<ArmorItem.Type, Integer> damageReduction, int enchantability, SoundEvent sound, float toughness, float knockback, Supplier<Ingredient> repairMaterial) {
		this.name = name;
		this.durabilityMultiplier = durability;
		this.protectionFunctionForType = damageReduction;
		this.enchantability = enchantability;
		this.equipSound = sound;
		this.toughness = toughness;
		this.knockback = knockback;
		this.repairMaterial = repairMaterial;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public int getDurabilityForType(ArmorItem.Type type) {
		return HEALTH_FUNCTION_FOR_TYPE.get(type) * this.durabilityMultiplier;
	}

	@Override
	public int getDefenseForType(ArmorItem.Type type) {
		return this.protectionFunctionForType.get(type);
	}

	@Override
	public int getEnchantmentValue() {
		return enchantability;
	}

	@Override
	public SoundEvent getEquipSound() {
		return equipSound;
	}

	@Override
	public float getToughness() {
		return toughness;
	}

	@Override
	public Ingredient getRepairIngredient() {
		return repairMaterial.get();
	}

	@Override
	public float getKnockbackResistance() {
		return this.knockback;
	}
}
