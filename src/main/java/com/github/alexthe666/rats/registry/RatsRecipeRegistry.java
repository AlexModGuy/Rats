package com.github.alexthe666.rats.registry;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.recipes.ArcheologistRecipe;
import com.github.alexthe666.rats.server.recipes.ChefRecipe;
import com.github.alexthe666.rats.server.recipes.DemonRatSwitchRecipe;
import com.github.alexthe666.rats.server.recipes.RatsRecipeSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RatsRecipeRegistry {

	public static final DeferredRegister<RecipeType<?>> RECIPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, RatsMod.MODID);
	public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, RatsMod.MODID);

	public static final RegistryObject<RecipeType<ArcheologistRecipe>> ARCHEOLOGIST = RECIPES.register("archeologist", () -> RecipeType.simple(new ResourceLocation(RatsMod.MODID, "archeologist")));
	public static final RegistryObject<RecipeSerializer<ArcheologistRecipe>> ARCHEOLOGIST_SERIALIZER = SERIALIZERS.register("archeologist", () -> new RatsRecipeSerializer<>(ArcheologistRecipe::new));
	public static final RegistryObject<RecipeType<ChefRecipe>> CHEF = RECIPES.register("chef", () -> RecipeType.simple(new ResourceLocation(RatsMod.MODID, "chef")));
	public static final RegistryObject<RecipeSerializer<ChefRecipe>> CHEF_SERIALIZER = SERIALIZERS.register("chef", () -> new RatsRecipeSerializer<>(ChefRecipe::new));
	public static final RegistryObject<RecipeSerializer<DemonRatSwitchRecipe>> SWITCH_DEMON = SERIALIZERS.register("switch_demon", () -> new SimpleCraftingRecipeSerializer<>(DemonRatSwitchRecipe::new));

}
