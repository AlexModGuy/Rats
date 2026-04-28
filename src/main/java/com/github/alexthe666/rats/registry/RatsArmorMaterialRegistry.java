package com.github.alexthe666.rats.registry;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

// 1.21 note: ArmorMaterial is now a record registered to BuiltInRegistries
// .ARMOR_MATERIAL; the old enum-implementing-ArmorMaterial pattern from 1.20.1
// no longer compiles. We expose Holder<ArmorMaterial> static fields so the
// existing call sites (HatItem / RatlantisArmorItem) just need their ctor
// signatures updated to take Holder<ArmorMaterial> instead of ArmorMaterial.
//
// Per-armor durability moved out of ArmorMaterial in 1.20.5; consumers should
// compute it via durabilityFor(name, type) and apply via Item.Properties.durability().
public final class RatsArmorMaterialRegistry {

    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS =
            DeferredRegister.create(Registries.ARMOR_MATERIAL, RatsMod.MODID);

    private static final Map<ArmorItem.Type, Integer> BASE_DURABILITY =
            Util.make(new EnumMap<>(ArmorItem.Type.class), m -> {
                m.put(ArmorItem.Type.BOOTS, 13);
                m.put(ArmorItem.Type.LEGGINGS, 15);
                m.put(ArmorItem.Type.CHESTPLATE, 16);
                m.put(ArmorItem.Type.HELMET, 11);
            });

    // Tracks per-registered-material durability multiplier so HatItem etc. can
    // derive the matching Item.Properties.durability() at construction.
    private static final Map<String, Integer> DURABILITY_MULTIPLIER = new HashMap<>();

    public static final Holder<ArmorMaterial> PIPER_HAT = register(
            "piper_hat", 25, defense(1, 1, 1, 2), 15,
            SoundEvents.ARMOR_EQUIP_GENERIC, 0.0F, 0.0F,
            () -> Ingredient.of(new ItemStack(RatsBlockRegistry.PIED_WOOL.get())));

    public static final Holder<ArmorMaterial> CHEF_TOQUE = register(
            "chef_toque", 0, defense(1, 1, 1, 1), 15,
            SoundEvents.ARMOR_EQUIP_GENERIC, 0.0F, 0.0F,
            () -> Ingredient.of(new ItemStack(Blocks.WHITE_WOOL)));

    public static final Holder<ArmorMaterial> PLAGUE_MASK = register(
            "plague_mask", 25, defense(1, 1, 1, 3), 15,
            SoundEvents.ARMOR_EQUIP_GENERIC, 1.0F, 0.0F,
            () -> Ingredient.of(new ItemStack(RatsItemRegistry.PLAGUE_ESSENCE.get())));

    public static final Holder<ArmorMaterial> RATLANTIS = register(
            "ratlantis", 40, defense(5, 8, 10, 5), 17,
            SoundEvents.ARMOR_EQUIP_NETHERITE, 4.0F, 0.1F,
            () -> Ingredient.of(new ItemStack(RatlantisItemRegistry.ORATCHALCUM_INGOT.get())));

    public static final Holder<ArmorMaterial> FARMER_HAT = register(
            "farmer_hat", 0, defense(1, 1, 1, 1), 15,
            SoundEvents.ARMOR_EQUIP_GENERIC, 0.0F, 0.0F,
            () -> Ingredient.of(new ItemStack(Items.HAY_BLOCK)));

    public static final Holder<ArmorMaterial> TOP_HAT = register(
            "top_hat", 0, defense(1, 1, 1, 1), 15,
            SoundEvents.ARMOR_EQUIP_GENERIC, 0.0F, 0.0F,
            () -> Ingredient.of(new ItemStack(Items.BLACK_WOOL)));

    public static final Holder<ArmorMaterial> FEZ = register(
            "fez", 0, defense(1, 1, 1, 1), 100,
            SoundEvents.ARMOR_EQUIP_GENERIC, 0.0F, 0.0F,
            () -> Ingredient.of(new ItemStack(Items.RED_WOOL)));

    public static final Holder<ArmorMaterial> SANTA_HAT = register(
            "santa_hat", 0, defense(1, 1, 1, 1), 15,
            SoundEvents.ARMOR_EQUIP_GENERIC, 0.0F, 0.0F,
            () -> Ingredient.of(new ItemStack(Items.RED_WOOL)));

    public static final Holder<ArmorMaterial> HALO = register(
            "halo", 0, defense(1, 1, 1, 1), 15,
            SoundEvents.ARMOR_EQUIP_GENERIC, 0.0F, 0.0F,
            () -> Ingredient.of(new ItemStack(Items.GOLD_NUGGET)));

    public static final Holder<ArmorMaterial> CROWN = register(
            "crown", 0, defense(1, 1, 1, 1), 15,
            SoundEvents.ARMOR_EQUIP_GENERIC, 0.0F, 0.0F,
            () -> Ingredient.of(new ItemStack(Items.GOLD_INGOT)));

    public static final Holder<ArmorMaterial> GHOST_HAT = register(
            "ghost_hat", 0, defense(1, 1, 1, 1), 15,
            SoundEvents.ARMOR_EQUIP_GENERIC, 0.0F, 0.0F,
            () -> Ingredient.of(new ItemStack(RatlantisItemRegistry.GHOST_PIRAT_ECTOPLASM.get())));

    public static final Holder<ArmorMaterial> GENERIC_HAT = register(
            "generic_hat", 0, defense(1, 1, 1, 1), 100,
            SoundEvents.ARMOR_EQUIP_GENERIC, 0.0F, 0.0F,
            () -> Ingredient.of(new ItemStack(Items.LEATHER)));

    private RatsArmorMaterialRegistry() {}

    private static EnumMap<ArmorItem.Type, Integer> defense(int boots, int leggings, int chestplate, int helmet) {
        EnumMap<ArmorItem.Type, Integer> m = new EnumMap<>(ArmorItem.Type.class);
        m.put(ArmorItem.Type.BOOTS, boots);
        m.put(ArmorItem.Type.LEGGINGS, leggings);
        m.put(ArmorItem.Type.CHESTPLATE, chestplate);
        m.put(ArmorItem.Type.HELMET, helmet);
        return m;
    }

    private static Holder<ArmorMaterial> register(
            String name, int durabilityMultiplier, EnumMap<ArmorItem.Type, Integer> defense,
            int enchantability, Holder<SoundEvent> equipSound,
            float toughness, float knockback, Supplier<Ingredient> repair) {
        DURABILITY_MULTIPLIER.put(name, durabilityMultiplier);
        return ARMOR_MATERIALS.register(name, () -> new ArmorMaterial(
                defense,
                enchantability,
                equipSound,
                repair,
                List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, name))),
                toughness,
                knockback));
    }

    public static int durabilityFor(Holder<ArmorMaterial> material, ArmorItem.Type type) {
        String key = material.unwrapKey().map(k -> k.location().getPath()).orElse("");
        int mult = DURABILITY_MULTIPLIER.getOrDefault(key, 0);
        return BASE_DURABILITY.getOrDefault(type, 0) * mult;
    }
}
