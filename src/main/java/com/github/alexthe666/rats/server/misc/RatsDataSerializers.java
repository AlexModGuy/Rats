package com.github.alexthe666.rats.server.misc;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;

public class RatsDataSerializers {

	public static final DeferredRegister<EntityDataSerializer<?>> DATA_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, RatsMod.MODID);

	public static final RegistryObject<EntityDataSerializer<List<GlobalPos>>> GLOBAL_POS_LIST = DATA_SERIALIZERS.register("global_pos_list", () -> new EntityDataSerializer.ForValueType<>() {
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
