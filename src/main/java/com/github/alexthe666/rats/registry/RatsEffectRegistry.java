package com.github.alexthe666.rats.registry;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.effect.ConfitByaldiMobEffect;
import com.github.alexthe666.rats.server.effect.PlagueMobEffect;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RatsEffectRegistry {

	public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, RatsMod.MODID);

	public static final DeferredHolder<MobEffect, MobEffect> SYNESTHESIA = MOB_EFFECTS.register("synesthesia", ConfitByaldiMobEffect::new);
	public static final DeferredHolder<MobEffect, MobEffect> PLAGUE = MOB_EFFECTS.register("plague", PlagueMobEffect::new);
}







