package com.github.alexthe666.rats.server.loot;

import com.github.alexthe666.rats.registry.RatsLootRegistry;
import com.github.alexthe666.rats.registry.worldgen.RatlantisDimensionRegistry;
import com.github.alexthe666.rats.server.entity.rat.Rat;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class RatHasTogaInRatlantisCondition implements LootItemCondition {

	@Override
	public LootItemConditionType getType() {
		return RatsLootRegistry.HAS_TOGA_AND_IN_RATLANTIS.get();
	}

	@Override
	public boolean test(LootContext context) {
		if (!context.hasParam(LootContextParams.THIS_ENTITY)) return false;
		return context.getParam(LootContextParams.THIS_ENTITY) instanceof Rat rat && rat.hasToga() && rat.level().dimension().equals(RatlantisDimensionRegistry.DIMENSION_KEY);
	}

	public static class ConditionSerializer implements Serializer<RatHasTogaInRatlantisCondition> {
		@Override
		public void serialize(JsonObject json, RatHasTogaInRatlantisCondition value, JsonSerializationContext context) {

		}

		@Override
		public RatHasTogaInRatlantisCondition deserialize(JsonObject json, JsonDeserializationContext context) {
			return new RatHasTogaInRatlantisCondition();
		}
	}
}
