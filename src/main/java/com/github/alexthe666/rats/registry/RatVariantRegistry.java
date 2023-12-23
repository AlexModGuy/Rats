package com.github.alexthe666.rats.registry;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.misc.RatVariant;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class RatVariantRegistry {
	public static final ResourceKey<Registry<RatVariant>> RAT_VARIANT_KEY = ResourceKey.createRegistryKey(new ResourceLocation(RatsMod.MODID, "rat_variant"));
	public static final DeferredRegister<RatVariant> RAT_VARIANTS = DeferredRegister.create(RAT_VARIANT_KEY, RatsMod.MODID);
	public static final Supplier<IForgeRegistry<RatVariant>> RAT_VARIANT_REGISTRY = RAT_VARIANTS.makeRegistry(() -> new RegistryBuilder<RatVariant>().hasTags());

	public static final RegistryObject<RatVariant> BLACK = RAT_VARIANTS.register("black", () -> new RatVariant(new RatVariant.Properties(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/black.png"))));
	public static final RegistryObject<RatVariant> BLUE = RAT_VARIANTS.register("blue", () -> new RatVariant(new RatVariant.Properties(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/blue.png"))));
	public static final RegistryObject<RatVariant> BROWN = RAT_VARIANTS.register("brown", () -> new RatVariant(new RatVariant.Properties(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/brown.png"))));
	public static final RegistryObject<RatVariant> GREEN = RAT_VARIANTS.register("green", () -> new RatVariant(new RatVariant.Properties(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/green.png"))));

	public static final RegistryObject<RatVariant> ALBINO = RAT_VARIANTS.register("albino", () -> new RatVariant(new RatVariant.Properties(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/breeding_variants/albino.png")).setBreedingExclusive()));
	public static final RegistryObject<RatVariant> BROWN_UNDERCOAT = RAT_VARIANTS.register("brown_undercoat", () -> new RatVariant(new RatVariant.Properties(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/breeding_variants/brown_undercoat.png")).setBreedingExclusive()));
	public static final RegistryObject<RatVariant> HOODED = RAT_VARIANTS.register("hooded", () -> new RatVariant(new RatVariant.Properties(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/breeding_variants/hooded.png")).setBreedingExclusive()));
	public static final RegistryObject<RatVariant> BROWN_HOODED = RAT_VARIANTS.register("brown_hooded", () -> new RatVariant(new RatVariant.Properties(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/breeding_variants/brown_hooded.png")).setBreedingExclusive()));
	public static final RegistryObject<RatVariant> GRAY_HOODED = RAT_VARIANTS.register("gray_hooded", () -> new RatVariant(new RatVariant.Properties(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/breeding_variants/gray_hooded.png")).setBreedingExclusive()));
	public static final RegistryObject<RatVariant> YELLOW_HOODED = RAT_VARIANTS.register("yellow_hooded", () -> new RatVariant(new RatVariant.Properties(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/breeding_variants/yellow_hooded.png")).setBreedingExclusive()));
	public static final RegistryObject<RatVariant> SIAMESE = RAT_VARIANTS.register("siamese", () -> new RatVariant(new RatVariant.Properties(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/breeding_variants/siamese.png")).setBreedingExclusive()));
	public static final RegistryObject<RatVariant> WHITE = RAT_VARIANTS.register("white", () -> new RatVariant(new RatVariant.Properties(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/breeding_variants/white.png")).setBreedingExclusive()));
	public static final RegistryObject<RatVariant> HAIRLESS = RAT_VARIANTS.register("hairless", () -> new RatVariant(new RatVariant.Properties(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/breeding_variants/hairless.png")).setBreedingExclusive()));
}
