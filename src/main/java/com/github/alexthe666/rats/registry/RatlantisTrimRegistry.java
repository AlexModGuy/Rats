package com.github.alexthe666.rats.registry;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimMaterial;

import java.util.Map;

public class RatlantisTrimRegistry {

	public static final ResourceKey<TrimMaterial> GEM_OF_RATLANTIS = registerKey("gem_of_ratlantis");
	public static final ResourceKey<TrimMaterial> ORATCHALCUM = registerKey("oratchalcum");

	private static ResourceKey<TrimMaterial> registerKey(String name) {
		return ResourceKey.create(Registries.TRIM_MATERIAL, new ResourceLocation(RatsMod.MODID, name));
	}

	public static void bootstrap(BootstapContext<TrimMaterial> context) {
		register(context, GEM_OF_RATLANTIS, RatlantisItemRegistry.GEM_OF_RATLANTIS.getHolder().get(), Style.EMPTY.withColor(10353514), 0.77F);
		register(context, ORATCHALCUM, RatlantisItemRegistry.ORATCHALCUM_INGOT.getHolder().get(), Style.EMPTY.withColor(11243608), 0.66F);
	}

	private static void register(BootstapContext<TrimMaterial> context, ResourceKey<TrimMaterial> trimKey, Holder<Item> trimItem, Style color, float itemModelIndex) {
		TrimMaterial material = new TrimMaterial(trimKey.location().getPath(), trimItem, itemModelIndex, Map.of(), Component.translatable(Util.makeDescriptionId("trim_material", trimKey.location())).withStyle(color));
		context.register(trimKey, material);
	}
}
