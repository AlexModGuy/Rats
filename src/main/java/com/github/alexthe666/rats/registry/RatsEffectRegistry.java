package com.github.alexthe666.rats.registry;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.effect.ConfitByaldiMobEffect;
import com.github.alexthe666.rats.server.effect.PlagueMobEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RatsEffectRegistry {

	public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, RatsMod.MODID);

	public static final RegistryObject<MobEffect> SYNESTHESIA = MOB_EFFECTS.register("synesthesia", ConfitByaldiMobEffect::new);
	public static final RegistryObject<MobEffect> PLAGUE = MOB_EFFECTS.register("plague", PlagueMobEffect::new);
}
