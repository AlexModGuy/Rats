package com.github.alexthe666.rats.server.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SingleItemRecipe;

public class RatsRecipeSerializer<T extends BaseRatRecipe> implements RecipeSerializer<T> {
	private final MapCodec<T> codec;
	private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;

	public RatsRecipeSerializer(SingleItemMaker<T> factory) {
		this.codec = RecordCodecBuilder.mapCodec(instance -> instance.group(
				Codec.STRING.optionalFieldOf("group", "").forGetter(recipe -> recipe.getGroup()),
				Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(recipe -> recipe.input()),
				ItemStack.STRICT_CODEC.fieldOf("result").forGetter(recipe -> recipe.getResult())
		).apply(instance, factory::create));

		this.streamCodec = StreamCodec.of(
				(buf, recipe) -> {
					buf.writeUtf(recipe.getGroup());
					Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.input());
					ItemStack.STREAM_CODEC.encode(buf, recipe.getResult());
				},
				buf -> {
					String group = buf.readUtf();
					Ingredient ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
					ItemStack result = ItemStack.STREAM_CODEC.decode(buf);
					return factory.create(group, ingredient, result);
				}
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

	public interface SingleItemMaker<T extends SingleItemRecipe> {
		T create(String group, Ingredient input, ItemStack output);
	}
}







