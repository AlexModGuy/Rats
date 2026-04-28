package com.github.alexthe666.rats.server.loot;

import com.github.alexthe666.rats.registry.RatsLootRegistry;
import com.github.alexthe666.rats.server.entity.rat.Rat;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class RatHasPlagueCondition implements LootItemCondition {

	public static final MapCodec<RatHasPlagueCondition> CODEC = MapCodec.unit(RatHasPlagueCondition::new);

	@Override
	public LootItemConditionType getType() {
		return RatsLootRegistry.RAT_HAS_PLAGUE.get();
	}

	@Override
	public boolean test(LootContext context) {
		if (!context.hasParam(LootContextParams.THIS_ENTITY)) return false;
		return context.getParam(LootContextParams.THIS_ENTITY) instanceof Rat rat && rat.hasPlague();
	}

	public static LootItemCondition.Builder hasPlague() {
		return RatHasPlagueCondition::new;
	}
}
