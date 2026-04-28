package com.github.alexthe666.rats.server.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

// 1.21: SimpleCriterionTrigger uses Codec-based instances; deserializeTrigger/getId/createInstance(JsonObject,...) gone.
public class BlackDeathSummonedTrigger extends SimpleCriterionTrigger<BlackDeathSummonedTrigger.TriggerInstance> {

	@Override
	public Codec<TriggerInstance> codec() {
		return TriggerInstance.CODEC;
	}

	public void trigger(ServerPlayer player) {
		this.trigger(player, instance -> true);
	}

	public record TriggerInstance(Optional<ContextAwarePredicate> player) implements SimpleCriterionTrigger.SimpleInstance {
		public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ContextAwarePredicate.CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player)
		).apply(instance, TriggerInstance::new));

		public static TriggerInstance summoned() {
			return new TriggerInstance(Optional.empty());
		}
	}
}
