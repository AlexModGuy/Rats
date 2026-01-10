package com.github.alexthe666.rats.registry;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class RatsDataSerializerRegistry {

	public static final DeferredRegister<EntityDataSerializer<?>> DATA_SERIALIZERS = DeferredRegister.create(NeoForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, RatsMod.MODID);

	public static final StreamCodec<RegistryFriendlyByteBuf, List<GlobalPos>> GLOBAL_POS_LIST_CODEC = 
		ByteBufCodecs.collection(ArrayList::new, GlobalPos.STREAM_CODEC);

	public static final DeferredHolder<EntityDataSerializer<?>, EntityDataSerializer<List<GlobalPos>>> GLOBAL_POS_LIST = DATA_SERIALIZERS.register("global_pos_list", () -> EntityDataSerializer.forValueType(GLOBAL_POS_LIST_CODEC));
}







