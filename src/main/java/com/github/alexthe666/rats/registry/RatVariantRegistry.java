package com.github.alexthe666.rats.registry;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.misc.RatVariant;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Supplier;

public class RatVariantRegistry {
	public static final ResourceKey<Registry<RatVariant>> RAT_VARIANT_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "rat_variant"));
	public static final DeferredRegister<RatVariant> RAT_VARIANTS = DeferredRegister.create(RAT_VARIANT_KEY, RatsMod.MODID);
	// 1.21: IForgeRegistry removed; makeRegistry returns Registry<T> directly (not Supplier).
	public static final Registry<RatVariant> RAT_VARIANT_REGISTRY = RAT_VARIANTS.makeRegistry(builder -> builder.sync(true));

	public static final DeferredHolder<RatVariant, RatVariant> BLACK = RAT_VARIANTS.register("black", () -> new RatVariant(new RatVariant.Properties(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "textures/entity/rat/black.png"))));
	public static final DeferredHolder<RatVariant, RatVariant> BLUE = RAT_VARIANTS.register("blue", () -> new RatVariant(new RatVariant.Properties(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "textures/entity/rat/blue.png"))));
	public static final DeferredHolder<RatVariant, RatVariant> BROWN = RAT_VARIANTS.register("brown", () -> new RatVariant(new RatVariant.Properties(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "textures/entity/rat/brown.png"))));
	public static final DeferredHolder<RatVariant, RatVariant> GREEN = RAT_VARIANTS.register("green", () -> new RatVariant(new RatVariant.Properties(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "textures/entity/rat/green.png"))));

	public static final DeferredHolder<RatVariant, RatVariant> ALBINO = RAT_VARIANTS.register("albino", () -> new RatVariant(new RatVariant.Properties(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "textures/entity/rat/breeding_variants/albino.png")).setBreedingExclusive()));
	public static final DeferredHolder<RatVariant, RatVariant> BROWN_UNDERCOAT = RAT_VARIANTS.register("brown_undercoat", () -> new RatVariant(new RatVariant.Properties(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "textures/entity/rat/breeding_variants/brown_undercoat.png")).setBreedingExclusive()));
	public static final DeferredHolder<RatVariant, RatVariant> HOODED = RAT_VARIANTS.register("hooded", () -> new RatVariant(new RatVariant.Properties(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "textures/entity/rat/breeding_variants/hooded.png")).setBreedingExclusive()));
	public static final DeferredHolder<RatVariant, RatVariant> BROWN_HOODED = RAT_VARIANTS.register("brown_hooded", () -> new RatVariant(new RatVariant.Properties(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "textures/entity/rat/breeding_variants/brown_hooded.png")).setBreedingExclusive()));
	public static final DeferredHolder<RatVariant, RatVariant> GRAY_HOODED = RAT_VARIANTS.register("gray_hooded", () -> new RatVariant(new RatVariant.Properties(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "textures/entity/rat/breeding_variants/gray_hooded.png")).setBreedingExclusive()));
	public static final DeferredHolder<RatVariant, RatVariant> YELLOW_HOODED = RAT_VARIANTS.register("yellow_hooded", () -> new RatVariant(new RatVariant.Properties(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "textures/entity/rat/breeding_variants/yellow_hooded.png")).setBreedingExclusive()));
	public static final DeferredHolder<RatVariant, RatVariant> SIAMESE = RAT_VARIANTS.register("siamese", () -> new RatVariant(new RatVariant.Properties(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "textures/entity/rat/breeding_variants/siamese.png")).setBreedingExclusive()));
	public static final DeferredHolder<RatVariant, RatVariant> WHITE = RAT_VARIANTS.register("white", () -> new RatVariant(new RatVariant.Properties(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "textures/entity/rat/breeding_variants/white.png")).setBreedingExclusive()));
	public static final DeferredHolder<RatVariant, RatVariant> HAIRLESS = RAT_VARIANTS.register("hairless", () -> new RatVariant(new RatVariant.Properties(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "textures/entity/rat/breeding_variants/hairless.png")).setBreedingExclusive()));
}
