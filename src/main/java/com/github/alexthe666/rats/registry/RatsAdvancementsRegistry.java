package com.github.alexthe666.rats.registry;

import com.github.alexthe666.rats.server.advancements.BlackDeathSummonedTrigger;
import net.minecraft.advancements.CriteriaTriggers;

public class RatsAdvancementsRegistry {
	// 1.21: CriteriaTriggers.register requires (String, Trigger) — pass the trigger ID explicitly.
	public static final BlackDeathSummonedTrigger BLACK_DEATH_SUMMONED = CriteriaTriggers.register("rats:black_death_summoned", new BlackDeathSummonedTrigger());

	public static void init() {
	}
}
