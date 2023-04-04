package com.github.alexthe666.rats.server.advancements;

import com.github.alexthe666.rats.RatsMod;
import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class BlackDeathSummonedTrigger extends SimpleCriterionTrigger<BlackDeathSummonedTrigger.TriggerInstance> {

	static final ResourceLocation ID = new ResourceLocation(RatsMod.MODID, "black_death_summoned");

	@Override
	protected BlackDeathSummonedTrigger.TriggerInstance createInstance(JsonObject object, EntityPredicate.Composite composite, DeserializationContext context) {
		return new TriggerInstance(composite);
	}

	public void trigger(ServerPlayer player) {
		this.trigger(player, instance -> true);
	}

	@Override
	public ResourceLocation getId() {
		return ID;
	}

	public static class TriggerInstance extends AbstractCriterionTriggerInstance {

		public TriggerInstance(EntityPredicate.Composite composite) {
			super(BlackDeathSummonedTrigger.ID, composite);
		}

		public static BlackDeathSummonedTrigger.TriggerInstance summoned() {
			return new BlackDeathSummonedTrigger.TriggerInstance(EntityPredicate.Composite.ANY);
		}
	}
}
