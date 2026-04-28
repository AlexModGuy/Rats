package com.github.alexthe666.rats.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.recipes.ArcheologistRecipe;
import com.github.alexthe666.rats.server.recipes.ChefRecipe;
import com.github.alexthe666.rats.server.recipes.DemonRatSwitchRecipe;
import com.github.alexthe666.rats.server.recipes.RatsRecipeSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;

public class RatsRecipeRegistry {

	public static final DeferredRegister<RecipeType<?>> RECIPES = DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, RatsMod.MODID);
	public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, RatsMod.MODID);

	public static final DeferredHolder<RecipeType<?>, RecipeType<ArcheologistRecipe>> ARCHEOLOGIST = RECIPES.register("archeologist", () -> RecipeType.simple(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "archeologist")));
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<ArcheologistRecipe>> ARCHEOLOGIST_SERIALIZER = SERIALIZERS.register("archeologist", () -> new RatsRecipeSerializer<>(ArcheologistRecipe::new));
	public static final DeferredHolder<RecipeType<?>, RecipeType<ChefRecipe>> CHEF = RECIPES.register("chef", () -> RecipeType.simple(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "chef")));
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<ChefRecipe>> CHEF_SERIALIZER = SERIALIZERS.register("chef", () -> new RatsRecipeSerializer<>(ChefRecipe::new));
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<DemonRatSwitchRecipe>> SWITCH_DEMON = SERIALIZERS.register("switch_demon", () -> new SimpleCraftingRecipeSerializer<>(DemonRatSwitchRecipe::new));

}
