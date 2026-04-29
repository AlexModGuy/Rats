package com.github.alexthe666.rats.registry;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.List;

public class RatsDataSerializerRegistry {

	public static final DeferredRegister<EntityDataSerializer<?>> DATA_SERIALIZERS = DeferredRegister.create(NeoForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, RatsMod.MODID);

	// PORT-STUB: 1.21 EntityDataSerializer is now StreamCodec-driven; a List<GlobalPos> stream codec must be supplied.
	public static final DeferredHolder<EntityDataSerializer<?>, EntityDataSerializer<List<GlobalPos>>> GLOBAL_POS_LIST = DATA_SERIALIZERS.register(
			"global_pos_list",
			() -> EntityDataSerializer.forValueType(
					GlobalPos.STREAM_CODEC.<RegistryFriendlyByteBuf>cast()
							.apply(ByteBufCodecs.list())
			)
	);
}
