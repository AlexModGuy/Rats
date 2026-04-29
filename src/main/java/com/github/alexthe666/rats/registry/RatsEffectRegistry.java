package com.github.alexthe666.rats.registry;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.effect.ConfitByaldiMobEffect;
import com.github.alexthe666.rats.server.effect.PlagueMobEffect;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;

public class RatsEffectRegistry {

	// 1.21: NeoForgeRegistries.MOB_EFFECTS removed; use vanilla BuiltInRegistries.MOB_EFFECT.
	public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(net.minecraft.core.registries.BuiltInRegistries.MOB_EFFECT, RatsMod.MODID);

	public static final DeferredHolder<MobEffect, MobEffect> SYNESTHESIA = MOB_EFFECTS.register("synesthesia", ConfitByaldiMobEffect::new);
	public static final DeferredHolder<MobEffect, MobEffect> PLAGUE = MOB_EFFECTS.register("plague", PlagueMobEffect::new);
}
