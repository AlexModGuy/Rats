package com.github.alexthe666.rats.server.misc;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class RatsDataSerializers {

	public static final DeferredRegister<EntityDataSerializer<?>> DATA_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, RatsMod.MODID);

	public static final RegistryObject<EntityDataSerializer<List<GlobalPos>>> GLOBAL_POS_LIST = DATA_SERIALIZERS.register("blockpos_list", () -> EntityDataSerializer.simple((buf, o) -> buf.writeCollection(o, FriendlyByteBuf::writeGlobalPos), buf -> buf.readList(FriendlyByteBuf::readGlobalPos)));
}
