package com.github.alexthe666.rats.server.recipes;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

// 1.21 RecipeSerializer<T> requires MapCodec<T> codec() and StreamCodec<RegistryFriendlyByteBuf, T>
// streamCodec() — replacing the pre-1.21 fromJson/toJson/fromNetwork/toNetwork quartet. We keep the
// original Rats schema (group + ingredient + result) and use DataComponent-aware ItemStack codecs so
// the resulting recipe round-trips correctly across config-load + the play network.
public class RatsRecipeSerializer<T extends BaseRatRecipe> implements RecipeSerializer<T> {
	private final MapCodec<T> codec;
	private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;
	final SingleItemMaker<T> factory;

	public RatsRecipeSerializer(SingleItemMaker<T> factory) {
		this.factory = factory;
		this.codec = RecordCodecBuilder.mapCodec(instance -> instance.group(
				com.mojang.serialization.Codec.STRING.optionalFieldOf("group", "").forGetter(BaseRatRecipe::getGroup),
				Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(BaseRatRecipe::getInputIngredient),
				ItemStack.STRICT_CODEC.fieldOf("result").forGetter(BaseRatRecipe::getResult)
		).apply(instance, (group, ingredient, result) -> factory.create(group, ingredient, result)));
		this.streamCodec = StreamCodec.composite(
				ByteBufCodecs.STRING_UTF8, BaseRatRecipe::getGroup,
				Ingredient.CONTENTS_STREAM_CODEC, BaseRatRecipe::getInputIngredient,
				ItemStack.STREAM_CODEC, BaseRatRecipe::getResult,
				(group, ingredient, result) -> factory.create(group, ingredient, result)
		);
	}

	@Override
	public MapCodec<T> codec() {
		return this.codec;
	}

	@Override
	public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() {
		return this.streamCodec;
	}

	public interface SingleItemMaker<T extends BaseRatRecipe> {
		T create(String group, Ingredient input, ItemStack output);
	}
}
