package com.github.alexthe666.rats.server.advancements;

import com.github.alexthe666.rats.registry.RatsAdvancementsRegistry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public class BlackDeathSummonedTrigger extends SimpleCriterionTrigger<BlackDeathSummonedTrigger.TriggerInstance> {

	@Override
	public Codec<TriggerInstance> codec() {
		return TriggerInstance.CODEC;
	}

	public void trigger(ServerPlayer player) {
		this.trigger(player, instance -> true);
	}

	public record TriggerInstance(Optional<ContextAwarePredicate> player) implements SimpleCriterionTrigger.SimpleInstance {
		public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance ->
			instance.group(
				EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player)
			).apply(instance, TriggerInstance::new)
		);

		public static Criterion<TriggerInstance> summoned() {
			return RatsAdvancementsRegistry.BLACK_DEATH_SUMMONED.get().createCriterion(new TriggerInstance(Optional.empty()));
		}
	}
}







