package com.github.alexthe666.rats.registry;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.advancements.BlackDeathSummonedTrigger;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RatsAdvancementsRegistry {
	public static final DeferredRegister<CriterionTrigger<?>> TRIGGERS = DeferredRegister.create(Registries.TRIGGER_TYPE, RatsMod.MODID);

	public static final DeferredHolder<CriterionTrigger<?>, BlackDeathSummonedTrigger> BLACK_DEATH_SUMMONED = 
		TRIGGERS.register("black_death_summoned", BlackDeathSummonedTrigger::new);

	public static void init() {
	}
}







