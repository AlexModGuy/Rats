package com.github.alexthe666.rats.registry;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.advancements.BlackDeathSummonedTrigger;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RatsAdvancementsRegistry {
	// 1.21: TRIGGER_TYPE registry is frozen by the time FMLCommonSetupEvent runs; register via
	// DeferredRegister against BuiltInRegistries.TRIGGER_TYPES so registration happens during the
	// proper RegisterEvent phase.
	public static final DeferredRegister<CriterionTrigger<?>> TRIGGERS =
			DeferredRegister.create(BuiltInRegistries.TRIGGER_TYPES, RatsMod.MODID);

	public static final DeferredHolder<CriterionTrigger<?>, BlackDeathSummonedTrigger> BLACK_DEATH_SUMMONED =
			TRIGGERS.register("black_death_summoned", BlackDeathSummonedTrigger::new);

	public static void init() {
	}
}
