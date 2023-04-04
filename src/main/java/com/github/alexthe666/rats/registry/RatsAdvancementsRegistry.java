package com.github.alexthe666.rats.registry;

import com.github.alexthe666.rats.server.advancements.BlackDeathSummonedTrigger;
import net.minecraft.advancements.CriteriaTriggers;

public class RatsAdvancementsRegistry {
	public static final BlackDeathSummonedTrigger BLACK_DEATH_SUMMONED = CriteriaTriggers.register(new BlackDeathSummonedTrigger());

	public static void init() {
	}
}
