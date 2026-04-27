package com.github.alexthe666.rats.registry;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.ArrayList;
import java.util.List;

public class RatsDataSerializerRegistry {

	public static final DeferredRegister<EntityDataSerializer<?>> DATA_SERIALIZERS = DeferredRegister.create(NeoForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, RatsMod.MODID);

	public static final DeferredHolder<EntityDataSerializer<?>, EntityDataSerializer<List<GlobalPos>>> GLOBAL_POS_LIST = DATA_SERIALIZERS.register("global_pos_list", () -> new EntityDataSerializer.ForValueType<>() {
		@Override
		public void write(FriendlyByteBuf buf, List<GlobalPos> list) {
			buf.writeCollection(list, FriendlyByteBuf::writeGlobalPos);
		}

		@Override
		public List<GlobalPos> read(FriendlyByteBuf buf) {
			return buf.readList(FriendlyByteBuf::readGlobalPos);
		}

		@Override
		public List<GlobalPos> copy(List<GlobalPos> list) {
			return new ArrayList<>(list);
		}
	});
}
