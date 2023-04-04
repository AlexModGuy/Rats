package com.github.alexthe666.rats.server.loot;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.registry.RatsLootRegistry;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class RatlantisLoadedLootCondition implements LootItemCondition {

	@Override
	public LootItemConditionType getType() {
		return RatsLootRegistry.RATLANTIS_LOADED.get();
	}

	@Override
	public boolean test(LootContext context) {
		return RatsMod.RATLANTIS_DATAPACK_ENABLED;
	}

	public static class ConditionSerializer implements Serializer<RatlantisLoadedLootCondition> {
		@Override
		public void serialize(JsonObject json, RatlantisLoadedLootCondition value, JsonSerializationContext context) {

		}

		@Override
		public RatlantisLoadedLootCondition deserialize(JsonObject json, JsonDeserializationContext context) {
			return new RatlantisLoadedLootCondition();
		}
	}
}
