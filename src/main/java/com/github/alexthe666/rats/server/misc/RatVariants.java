package com.github.alexthe666.rats.server.misc;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class RatVariants {
	public static final ResourceKey<Registry<RatVariant>> RAT_VARIANT_KEY = ResourceKey.createRegistryKey(new ResourceLocation(RatsMod.MODID, "rat_variant"));
	public static final DeferredRegister<RatVariant> RAT_VARIANTS = DeferredRegister.create(RAT_VARIANT_KEY, RatsMod.MODID);
	public static final Supplier<IForgeRegistry<RatVariant>> RAT_VARIANT_REGISTRY = RAT_VARIANTS.makeRegistry(RegistryBuilder::new);

	public static final RegistryObject<RatVariant> BLACK = RAT_VARIANTS.register("black", () -> new RatVariant(new RatVariant.Properties().texture(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/rat_black.png"))));
	public static final RegistryObject<RatVariant> BLUE = RAT_VARIANTS.register("blue", () -> new RatVariant(new RatVariant.Properties().texture(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/rat_blue.png"))));
	public static final RegistryObject<RatVariant> BROWN = RAT_VARIANTS.register("brown", () -> new RatVariant(new RatVariant.Properties().texture(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/rat_brown.png"))));
	public static final RegistryObject<RatVariant> GREEN = RAT_VARIANTS.register("green", () -> new RatVariant(new RatVariant.Properties().texture(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/rat_green.png"))));

	public static final RegistryObject<RatVariant> ALBINO = RAT_VARIANTS.register("albino", () -> new RatVariant(new RatVariant.Properties().texture(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/tamed/rat_albino.png")).setBreedingExclusive()));
	public static final RegistryObject<RatVariant> BROWN_UNDERCOAT = RAT_VARIANTS.register("brown_undercoat", () -> new RatVariant(new RatVariant.Properties().texture(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/tamed/rat_brown_undercoat.png")).setBreedingExclusive()));
	public static final RegistryObject<RatVariant> HOODED = RAT_VARIANTS.register("hooded", () -> new RatVariant(new RatVariant.Properties().texture(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/tamed/rat_hooded.png")).setBreedingExclusive()));
	public static final RegistryObject<RatVariant> BROWN_HOODED = RAT_VARIANTS.register("brown_hooded", () -> new RatVariant(new RatVariant.Properties().texture(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/tamed/rat_hooded_brown.png")).setBreedingExclusive()));
	public static final RegistryObject<RatVariant> GRAY_HOODED = RAT_VARIANTS.register("gray_hooded", () -> new RatVariant(new RatVariant.Properties().texture(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/tamed/rat_hooded_gray.png")).setBreedingExclusive()));
	public static final RegistryObject<RatVariant> YELLOW_HOODED = RAT_VARIANTS.register("yellow_hooded", () -> new RatVariant(new RatVariant.Properties().texture(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/tamed/rat_hooded_yellow.png")).setBreedingExclusive()));
	public static final RegistryObject<RatVariant> SIAMESE = RAT_VARIANTS.register("siamese", () -> new RatVariant(new RatVariant.Properties().texture(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/tamed/rat_siamese.png")).setBreedingExclusive()));
	public static final RegistryObject<RatVariant> WHITE = RAT_VARIANTS.register("white", () -> new RatVariant(new RatVariant.Properties().texture(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/tamed/rat_white.png")).setBreedingExclusive()));

	public static final RegistryObject<RatVariant> BUGRAAK = RAT_VARIANTS.register("bugraak", () -> new RatVariant(new RatVariant.Properties().texture(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/patreon/rat_bugraak.png")).setNamingExclusive("bugraak")));
	public static final RegistryObject<RatVariant> DINO = RAT_VARIANTS.register("dino", () -> new RatVariant(new RatVariant.Properties().texture(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/patreon/rat_dino.png")).setNamingExclusive("dino")));
	public static final RegistryObject<RatVariant> FRIAR = RAT_VARIANTS.register("friar", () -> new RatVariant(new RatVariant.Properties().texture(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/patreon/rat_friar.png")).setNamingExclusive("friar")));
	public static final RegistryObject<RatVariant> GIZMO = RAT_VARIANTS.register("gizmo", () -> new RatVariant(new RatVariant.Properties().texture(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/patreon/rat_gizmo.png")).setNamingExclusive("gizmo")));
	public static final RegistryObject<RatVariant> JULIAN = RAT_VARIANTS.register("julian", () -> new RatVariant(new RatVariant.Properties().texture(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/patreon/rat_julian.png")).setNamingExclusive("julian")));
	public static final RegistryObject<RatVariant> LIL_CHEESE = RAT_VARIANTS.register("lil_cheese", () -> new RatVariant(new RatVariant.Properties().texture(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/patreon/rat_lil_cheese.png")).setNamingExclusive("lil_cheese")));
	public static final RegistryObject<RatVariant> RATATLA = RAT_VARIANTS.register("ratatla", () -> new RatVariant(new RatVariant.Properties().texture(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/patreon/rat_ratatla.png")).setNamingExclusive("ratatla")));
	public static final RegistryObject<RatVariant> RIDDLER = RAT_VARIANTS.register("riddler", () -> new RatVariant(new RatVariant.Properties().texture(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/patreon/rat_riddler.png")).setNamingExclusive("riddler")));
	public static final RegistryObject<RatVariant> SHARVA = RAT_VARIANTS.register("sharva", () -> new RatVariant(new RatVariant.Properties().texture(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/patreon/rat_sharva.png")).setNamingExclusive("sharva")));
	public static final RegistryObject<RatVariant> SHIZUKA = RAT_VARIANTS.register("shizuka", () -> new RatVariant(new RatVariant.Properties().texture(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/patreon/rat_shizuka.png")).setNamingExclusive("shizuka")));
	public static final RegistryObject<RatVariant> SKRAT = RAT_VARIANTS.register("skrat", () -> new RatVariant(new RatVariant.Properties().texture(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/patreon/rat_skrat.png")).setNamingExclusive("skrat")));
	public static final RegistryObject<RatVariant> ZURA = RAT_VARIANTS.register("zura", () -> new RatVariant(new RatVariant.Properties().texture(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/patreon/rat_zura.png")).setNamingExclusive("zura")));

	public static RatVariant getRandomVariant(RandomSource random, boolean fromBreeding) {
		List<RatVariant> validVariants = new ArrayList<>(RAT_VARIANT_REGISTRY.get().getValues().stream().toList());
		validVariants.removeIf(variant -> (!fromBreeding && variant.isBreedingExclusive()) || !variant.getName().isEmpty());
		return validVariants.toArray(RatVariant[]::new)[random.nextInt(validVariants.size())];
	}

	public static RatVariant getRandomBreedingExclusiveVariant(RandomSource random) {
		List<RatVariant> validVariants = new ArrayList<>(RAT_VARIANT_REGISTRY.get().getValues().stream().toList());
		validVariants.removeIf(variant -> !variant.isBreedingExclusive());
		return validVariants.toArray(RatVariant[]::new)[random.nextInt(validVariants.size())];
	}

	public static RatVariant getVariant(String id) {
		return Optional.ofNullable(RAT_VARIANT_REGISTRY.get().getValue(new ResourceLocation(id))).orElse(RatVariants.BLUE.get());
	}

	public static String getVariantId(RatVariant variant) {
		return Optional.ofNullable(RAT_VARIANT_REGISTRY.get().getKey(variant)).orElse(RAT_VARIANT_REGISTRY.get().getKey(RatVariants.BLUE.get())).toString();
	}
}
