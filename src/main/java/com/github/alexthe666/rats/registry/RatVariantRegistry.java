package com.github.alexthe666.rats.registry;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.misc.RatVariant;
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

public class RatVariantRegistry {
	public static final ResourceKey<Registry<RatVariant>> RAT_VARIANT_KEY = ResourceKey.createRegistryKey(new ResourceLocation(RatsMod.MODID, "rat_variant"));
	public static final DeferredRegister<RatVariant> RAT_VARIANTS = DeferredRegister.create(RAT_VARIANT_KEY, RatsMod.MODID);
	public static final Supplier<IForgeRegistry<RatVariant>> RAT_VARIANT_REGISTRY = RAT_VARIANTS.makeRegistry(RegistryBuilder::new);

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

	public static final RegistryObject<RatVariant> BUGRAAK = RAT_VARIANTS.register("bugraak", () -> new RatVariant(new RatVariant.Properties(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/patreon_skins/bugraak.png")).setNamingExclusive("bugraak")));
	public static final RegistryObject<RatVariant> DINO = RAT_VARIANTS.register("dino", () -> new RatVariant(new RatVariant.Properties(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/patreon_skins/dino.png")).setNamingExclusive("dino")));
	public static final RegistryObject<RatVariant> FRIAR = RAT_VARIANTS.register("friar", () -> new RatVariant(new RatVariant.Properties(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/patreon_skins/friar.png")).setNamingExclusive("friar")));
	public static final RegistryObject<RatVariant> GIZMO = RAT_VARIANTS.register("gizmo", () -> new RatVariant(new RatVariant.Properties(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/patreon_skins/gizmo.png")).setNamingExclusive("gizmo")));
	public static final RegistryObject<RatVariant> JULIAN = RAT_VARIANTS.register("julian", () -> new RatVariant(new RatVariant.Properties(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/patreon_skins/julian.png")).setNamingExclusive("julian")));
	public static final RegistryObject<RatVariant> LIL_CHEESE = RAT_VARIANTS.register("lil_cheese", () -> new RatVariant(new RatVariant.Properties(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/patreon_skins/lil_cheese.png")).setNamingExclusive("lil_cheese")));
	public static final RegistryObject<RatVariant> RATATLA = RAT_VARIANTS.register("ratatla", () -> new RatVariant(new RatVariant.Properties(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/patreon_skins/ratatla.png")).setNamingExclusive("ratatla")));
	public static final RegistryObject<RatVariant> RIDDLER = RAT_VARIANTS.register("riddler", () -> new RatVariant(new RatVariant.Properties(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/patreon_skins/riddler.png")).setNamingExclusive("riddler")));
	public static final RegistryObject<RatVariant> SHARVA = RAT_VARIANTS.register("sharva", () -> new RatVariant(new RatVariant.Properties(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/patreon_skins/sharva.png")).setNamingExclusive("sharva")));
	public static final RegistryObject<RatVariant> SHIZUKA = RAT_VARIANTS.register("shizuka", () -> new RatVariant(new RatVariant.Properties(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/patreon_skins/shizuka.png")).setNamingExclusive("shizuka")));
	public static final RegistryObject<RatVariant> SKRAT = RAT_VARIANTS.register("skrat", () -> new RatVariant(new RatVariant.Properties(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/patreon_skins/skrat.png")).setNamingExclusive("skrat")));
	public static final RegistryObject<RatVariant> ZURA = RAT_VARIANTS.register("zura", () -> new RatVariant(new RatVariant.Properties(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/patreon_skins/zura.png")).setNamingExclusive("zura")));
}
